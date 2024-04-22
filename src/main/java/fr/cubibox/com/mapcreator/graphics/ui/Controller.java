package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.Application;
import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Type;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.map.Wall;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static fr.cubibox.com.mapcreator.map.Type.*;

public class Controller implements Initializable {

    public ScrollPane scrollPane;
    public ToggleButton bottom;
    public ToggleButton top;

    public Repositories repositories;
    public ArrayList<Vector2v> tpmPoints;
    private PropertyBoardController property;


    public static RenderPane renderPane;
    private ArrayList<Type> drawablePol;
    private boolean dragState;
    private float[] dragPointOrigin = new float[2];


    //TODO divide part on many controllers (pane / tab on left/right / menu bar etc...)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //coordinateSystem.setPrefSize(scrollPane.getWidth(),scrollPane.getHeight());

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        renderPane = new ClassicRender(this);

        //scrollPane.setPrefWidth(screenWidth/2);
        //coordinateSystem.setPrefSize(screenWidth,screenWidth);

        // Toggle view buttons
        drawablePol = new ArrayList<>();
        drawablePol.addAll(List.of(values()));
        for(Type type : values()){
            ToggleButton temp = switch (type) {
                case CELLING -> top;
                case FLOOR -> bottom;
                default -> walls;
            };
            temp.selectedProperty().addListener((obs, oldval, newVal) -> {
                if (newVal && !drawablePol.contains(type)){
                    drawablePol.add(type);
                }else if (!newVal){
                    drawablePol.remove(type);
                }
            });
        }
        walls.setOnAction(this::actualizeView);
        top.setOnAction(this::actualizeView);
        bottom.setOnAction(this::actualizeView);

        polHeightSlide.setBlockIncrement(1);
        polHeightSlide.setMajorTickUnit(1);
        polHeightSlide.setShowTickLabels(true);
        polHeightSlide.valueProperty().addListener((obs, oldval, newVal) -> polHeightSlide.setValue(newVal.intValue()));

        mapSizeSlide.setBlockIncrement(1);
        mapSizeSlide.setMajorTickUnit(1);
        mapSizeSlide.setShowTickLabels(true);
        mapSizeSlide.valueProperty().addListener((obs, oldval, newVal) -> mapSizeSlide.setValue(newVal.intValue()));
        mapSizeSlide.valueProperty().addListener(event -> {
            Application.xSize = (int) (16 * mapSizeSlide.getValue());
            drawPolygons();
        });
        isoview.setOnAction(this::actualizeView);

        //zoom (WIP)
        coordinateSystem.setOnScroll(event ->{
            double value = event.getDeltaY();

            if (event.isControlDown()) {
                renderPane.zoom(dragState, value, event);
                drawPolygons();
            }
            System.out.println("scroll");
        });


        //record drag status
        coordinateSystem.setOnMouseDragged(event ->{
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float[] dragPoint = {(float) event.getX(), (float) event.getY()};

                if (event.isShiftDown()){
                    renderPane.move(dragState, dragPointOrigin, dragPoint, event);
                }
                else {
                    renderPane.drag(dragState, dragPointOrigin, dragPoint, event);
                }
                drawPolygons(renderPane.getTempPolygons());

                dragState = true;
            }
        });

        //set by click (make intern variable to know if drag is shifted or not)
        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && !event.isShiftDown()) {
                ArrayList<Vector2v> pts = renderPane.setPolygonByDrag(event.getX(), event.getY(), dragPointOrigin, dragState);
                if (pts != null) {
                    if (pts.size() > 1) {
                        setPolygon(pts);
                        tpmPoints = new ArrayList<>();
                    }
                    else {
                        tpmPoints.add(pts.get(0));
                        //polyBoard.getChildren().add(pointBoard(pts.get(0)));
                    }
                }
                drawPolygons();
            }
            dragState = false;
        });

        repositories = Repositories.getInstance();
        tpmPoints = new ArrayList<>();

        property = new PropertyBoardController(propertyBoard, this);

        TreeItem<String> rootItem = new TreeItem<String> ("Sectors");
        rootItem.setExpanded(true);
        sectorTree = new TreeView<String> (rootItem);
        sectorTree.setMaxHeight(500 + 20);
        sectorTree.setMinHeight(500 + 20);

        MultipleSelectionModel<TreeItem<String>> sectorTreeSelectionModel = sectorTree.getSelectionModel();
        sectorTreeSelectionModel.selectedItemProperty().addListener((item) -> {
            System.out.println(item.toString());
            TreeItem<String> currItem = sectorTreeSelectionModel.getSelectedItem();

            for (Wall wall : repositories.getAllWalls()){
                if (wall.isSelected()) wall.setSelected(false);
            }
            for (Vector2v vec : repositories.getAllVectors()){
                if (vec.isSelected()) vec.setSelected(false);
            }

            propertyBoard.getChildren().clear();
            if (currItem.getValue().contains("vector")){
                repositories.getVectorByID(currItem.hashCode()).setSelected(true);
                property.init(repositories.getVectorByID(currItem.hashCode()));
                propertyBoard.getChildren().add(property.getBoard());
            }
            else if (currItem.getValue().contains("wall")){
                repositories.getWallByID(currItem.hashCode()).setSelected(true);
                property.init(repositories.getWallByID(currItem.hashCode()));
                propertyBoard.getChildren().add(property.getBoard());
            }
            else {
                try {
                    for (int id : repositories.getSectorByID(currItem.hashCode()).getWallIds()) {
                        repositories.getWallByID(id).setSelected(true);
                    }
                    property.init(repositories.getSectorByID(currItem.hashCode()));
                    propertyBoard.getChildren().add(property.getBoard());
                }
                catch (NullPointerException npe){
                    System.out.println("id null, skipped");
                }
            }
            drawPolygons();

            //System.out.println();
            //renderPane.drawPointShape(coordinateSystem, (Vector2d) item);
        });

        drawPolygons();
    }


    public void actualizeBoard(){
        polyBoard.getChildren().clear();

        sectorTree.getRoot().getChildren().removeIf(
                obj -> !repositories.contains(obj.hashCode())
        );

        polyBoard.getChildren().add(sectorTree);

        drawPolygons();
    }

    public void actualizeView(ActionEvent ae){
        renderPane = isoview.isSelected() ?
                new IsometricRender(0, 0.5, this) :
                new ClassicRender(this);

        ImageView imgSclt = new ImageView();
        ImageView img = new ImageView();
        try {
            imgSclt = new ImageView(new Image(Objects.requireNonNull(Application.class.getResource("images/icon.png")).openStream()));
            img = new ImageView(new Image(Objects.requireNonNull(Application.class.getResource("images/wall.png")).openStream()));;
        }
        catch (IOException e) { throw new RuntimeException(e); }

        img.setFitWidth(30);
        img.setFitHeight(30);
        imgSclt.setFitWidth(30);
        imgSclt.setFitHeight(30);

        drawPolygons();
    }

    public void drawPolygons(Shape ... tempPol){
        drawPolygons(new ArrayList<>(Arrays.asList(tempPol)));
    }

    public void drawPolygons(ArrayList<Shape> tempPol) {
        actualizeTreeView();
        coordinateSystem.getChildren().clear();
        ArrayList<Vector2v> vectors = tpmPoints;

        //draw the grid
        renderPane.drawGrid(coordinateSystem);

        //draw points
        for(Vector2v vector : vectors) {
            renderPane.drawPointShape(coordinateSystem, vector);
        }

        //draw polygons
        for (Sector obj : repositories.getAllSectors()) {
            if (drawablePol.contains(obj.getType())) {
                renderPane.drawPolygon(coordinateSystem, obj);
            }
        }

        //draw temp polygon (WIP -> move to renderPane)
        if (tempPol != null && !tempPol.isEmpty() && !isoview.isSelected()){
            for (Shape pol : tempPol){
                coordinateSystem.getChildren().add(pol);
            }
        }
    }



    private void actualizeTreeView() {
        for (TreeItem<String> treeItem : sectorTree.getRoot().getChildren()){
            //remove from tree if delete
            treeItem.getChildren().get(0).getChildren().removeIf(
                    treeItemWall -> !repositories.getAllWalls().contains(repositories.getWallByID(treeItemWall.hashCode()))
            );
            treeItem.getChildren().get(1).getChildren().removeIf(
                    treeItemVector -> !repositories.getAllVectors().contains(repositories.getVectorByID(treeItemVector.hashCode()))
            );
            if (!repositories.getAllSectors().contains(repositories.getSectorByID(treeItem.hashCode()))){
                sectorTree.getRoot().getChildren().remove(treeItem);
            }

            //add in tree if added
            /*
            for (int wallID : repositories.getSectorByID(treeItem.hashCode()).getWallIds()) {
                if (!treeItem.getChildren().get(0).getChildren().contains(repositories.getWallByID(wallID).getTreeItem())){
                    treeItem.getChildren().get(0).getChildren().add(repositories.getWallByID(wallID).getTreeItem());
                }
            }
            for (TreeItem<String> vectorItem : treeItem.getChildren().get(1).getChildren()){

            }

             */


            //replace in tree if moved
            //TODO

            for (TreeItem<String> treeItemWall : treeItem.getChildren().get(0).getChildren()){
                Wall currwall = repositories.getWallByID(treeItemWall.hashCode());
                if (treeItemWall.getChildren().get(0).hashCode() != currwall.getVector1ID()) {
                    treeItemWall.getChildren().remove(0);
                    //wall.getChildren().add(repositories.getVectorByID(currwall.getVector1ID()).getTreeItem());
                }
                if (treeItemWall.getChildren().get(1).hashCode() != currwall.getVector2ID()) {
                    treeItemWall.getChildren().remove(1);
                    treeItemWall.getChildren().add(repositories.getVectorByID(currwall.getVector2ID()).getTreeItem());
                    System.out.println(currwall.getVector2ID());
                }
            }

        }
        //sectorTree.refresh();
    }

    public void reset(ActionEvent ignored) {
        //Main.setPlayer1(new Player(Main.getxSize()/2, Main.getxSize()/2));
        repositories.clear();
        sectorTree = new TreeView<>();
        actualizeBoard();
        drawPolygons();
    }

    public Type selectType(ToggleGroup tg){
        RadioButton rd = (RadioButton)tg.getSelectedToggle();
        return Type.toType(rd.getId());
    }

    public void setPolygon(ActionEvent ignored) {
        if (tpmPoints.size() >= 2) {
            setPolygon(tpmPoints);
            tpmPoints = new ArrayList<>();
            actualizeBoard();
        }
    }

    public void setPolygon(ArrayList<Vector2v> vectors) {
        Vector2v buff = vectors.get(vectors.size()-1);
        HashSet<Integer> wallsID = new HashSet<>();
        for (Vector2v vec : vectors) {
            repositories.add(vec.getId(), vec);
            Wall currWall = new Wall(buff.getId(), vec.getId());
            currWall.getTreeItem().getChildren().add(buff.getTreeItem());
            currWall.getTreeItem().getChildren().add(vec.getTreeItem());

            repositories.add(currWall.getId(), currWall);
            wallsID.add(currWall.getId());
            buff = vec;
        }

        Sector obj = new Sector(
                (float) polHeightSlide.getValue(),
                (float) 0.,
                selectType(SelectionOption)
        );
        obj.addWallIds(wallsID);

        TreeItem<String> pointItem = new TreeItem<>("Vectors");
        pointItem.setExpanded(false);
        TreeItem<String> wallItem = new TreeItem<>("Walls");
        wallItem.setExpanded(false);

        for (Vector2v v : repositories.getVectors(obj))
            pointItem.getChildren().add(v.getTreeItem());

        for (Wall w : repositories.getWalls(obj))
            wallItem.getChildren().add(w.getTreeItem());

        obj.getTreeItem().getChildren().add(wallItem);
        obj.getTreeItem().getChildren().add(pointItem);

        sectorTree.getRoot().getChildren().add(obj.getTreeItem());
        repositories.add(obj.id, obj);

        actualizeBoard();
        drawPolygons();
    }


    public void importMapButton(){
        Stage stageSave = new Stage();
        stageSave.setTitle("sauvegarder");
        stageSave.setResizable(false);
        stageSave.setWidth(400);
        stageSave.setHeight(800);
        stageSave.setOnCloseRequest(event -> {
            stageSave.close();
        });
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #202020");
        ArrayList<String> listeSave = new ArrayList<>();
        File file = new File("maps");
        if (file.mkdir()){
            //System.out.println("le dossier a été créer");
        }
        else{
            //System.out.println("le dossier existe deja");
        }

        File[] files = file.listFiles();
        if (files != null){
            for (File f : files){
                listeSave.add(f.getName());
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                Label label = new Label(f.getName());
                label.setTextFill(javafx.scene.paint.Color.WHITE);
                Button button = new Button("charger");
                button.setOnAction(event -> {
//                    if (!Main.getPolygons().isEmpty()) {
//                        try {exportMapButton(null);}
//                        catch (IOException e) {throw new RuntimeException(e);}
//                    }
                    drawPolygons();
                    actualizeBoard();
                    stageSave.close();
                });
                Button button3 = new Button("supprimer");
                button3.setOnAction(event -> {
                    f.delete();
                    hBox.getChildren().clear();
                    listeSave.remove(f.getName());
                });
                hBox.getChildren().addAll(label,button,button3);
                vBox.getChildren().add(hBox);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("erreur");
            alert.setHeaderText("erreur");
            alert.setContentText("Pas de fichier de sauvegarde");
            alert.showAndWait();
        }
        stageSave.setScene(new Scene(vBox));
        stageSave.show();
    }


    public void exportMapButton(ActionEvent actionEvent) throws IOException {

    }

    public static void writeMap(String name, String content)throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name + ".map"));
        writer.write(content);
        writer.close();
    }
}