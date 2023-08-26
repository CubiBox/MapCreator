package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.graphics.ui.PropertyBoard;
import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.map.repositories.SectorRepository;
import fr.cubibox.com.mapcreator.map.repositories.VectorRepository;
import fr.cubibox.com.mapcreator.map.repositories.WallRepository;
import fr.cubibox.com.mapcreator.map_old.Map_old;
import fr.cubibox.com.mapcreator.maths.Wall;
import fr.cubibox.com.mapcreator.old_mapObject.Type;
import fr.cubibox.com.mapcreator.maths.Vector;
import fr.cubibox.com.mapcreator.maths.Sector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.old_mapObject.Type.*;

public class Controller implements Initializable {

    public ScrollPane scrollPane;
    @FXML
    private Pane coordinateSystem;
    @FXML
    private ToggleButton walls;
    public ToggleButton bottom;
    public ToggleButton top;

    @FXML
    private ToggleButton isoview;
    @FXML
    private ToggleGroup SelectionOption;
    @FXML
    private ToggleGroup view;
    @FXML
    private ToggleGroup POV;
    @FXML
    private Slider mapSizeSlide;
    @FXML
    private Slider polHeightSlide;

    @FXML
    private VBox polyBoard;
    @FXML
    private TreeView<String> sectorTree;
    private HashMap<TreeItem<String>, Wall> sectorMap;
    private HashMap<TreeItem<String>, Vector> pointMap;
    private HashMap<TreeItem<String>, fr.cubibox.com.mapcreator.maths.Wall> wallMap;

    private SectorRepository sectors;
    private VectorRepository points;
    private WallRepository wallsMap;

    @FXML
    private VBox propertyBoard;

    private PropertyBoard property;
    @FXML
    private Button Reset;
    @FXML
    private Button importer;

    public static RenderPane renderPane;


    private ArrayList<Type> drawablePol;
    private boolean dragState;
    private float[] dragPointOrigin = new float[2];
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        renderPane = new ClassicRender();

        scrollPane.setPrefWidth(screenWidth/2);
        coordinateSystem.setPrefSize(980,980);

        drawPolygons();

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
            Main.setxSize((int) (16*mapSizeSlide.getValue()));
            drawPolygons();
        });
        isoview.setOnAction(this::actualizeView);

        //zoom (WIP)
        coordinateSystem.setOnScroll(event ->{
            double value = event.getDeltaY();
            if (event.isControlDown() && Main.DIML + value < 2000 && Main.DIML + value > scrollPane.getWidth()+20) {
                Main.DIML += value;
                Main.DIMC += value;
                coordinateSystem.setPrefSize(Main.DIMC,Main.DIML);
                drawPolygons();
            }
            System.out.println("scroll");
        });

        //reset pane dimension if windowed (WIP)
        scrollPane.widthProperty().addListener(e -> {
            scrollPane.setPrefWidth(screenWidth/2);
            coordinateSystem.setPrefSize(980,980);
            Main.DIML = 980;
            Main.DIMC = 980;
            drawPolygons();
        });

        //record drag status
        coordinateSystem.setOnMouseDragged(event ->{
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float[] dragPoint = {(float) event.getX(), (float) event.getY()};

                if (renderPane.drag(dragState, dragPointOrigin, dragPoint)) {
                    drawPolygons(renderPane.getTempPolygons());
                }

                dragState = true;
            }
        });

        //set by click
        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                ArrayList<Vector> pts = renderPane.setPolygonByDrag(event.getX(), event.getY(), dragPointOrigin, dragState);
                if (pts != null) {
                    if (pts.size() > 1) {
                        setPolygon(pts);
                    }
                    else {
                        Main.getPoints().add(pts.get(0));
                        polyBoard.getChildren().add(pointBoard(pts.get(0)));
                    }
                }

                dragState = false;
                drawPolygons();
            }
        });

        sectors = new SectorRepository();
        wallsMap = new WallRepository();
        points = new VectorRepository();

        property = new PropertyBoard(propertyBoard, this);

        TreeItem<String> rootItem = new TreeItem<String> ("Sectors");
        rootItem.setExpanded(true);
        sectorTree = new TreeView<String> (rootItem);
        sectorTree.setMaxHeight(500 + 20);
        sectorTree.setMinHeight(500 + 20);
        MultipleSelectionModel<TreeItem<String>> sectorTreeSelectionModel = sectorTree.getSelectionModel();
        sectorTreeSelectionModel.selectedItemProperty().addListener((item) -> {
            System.out.println(item.toString());
            TreeItem<String> currItem = sectorTreeSelectionModel.getSelectedItem();
            propertyBoard.getChildren().clear();
            int id = currItem.hashCode();

            if (currItem.getValue().contains("point")){
                System.out.println(points.getByID(currItem.hashCode()));
                property.init(pointMap.get(currItem));
                propertyBoard.getChildren().add(property.getBoard());
            }
            else if (currItem.getValue().contains("wall")){
                System.out.println(wallMap.get(currItem));
            }
            else {
                System.out.println(sectors.getByID(currItem.hashCode()));
                property.init(sectors.getByID(currItem.hashCode()));
                propertyBoard.getChildren().add(property.getBoard());
            }

            //System.out.println();
            //renderPane.drawPointShape(coordinateSystem, (Vector2F) item);
        });
    }

    public void actualizeAllPolygons(){
        for (Wall obj : Main.walls){
            renderPane.actualizePolygon(obj.getPolygon());
        }
    }


    public VBox pointBoard(Vector p){
        VBox ptsBoard = new VBox();
        HBox nameBoard = new HBox();
        HBox pointBoard = new HBox();

        Color c = Color.rgb((int) (p.getColor().getRed()*255), (int) (p.getColor().getGreen()*255), (int) (p.getColor().getBlue()*255),0.2);
        ptsBoard.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
        ptsBoard.setSpacing(5d);
        ptsBoard.setFillWidth(true);

        HBox xBoard = new HBox();
        HBox yBoard = new HBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        xBoard.setAlignment(Pos.CENTER);
        yBoard.setAlignment(Pos.CENTER);

        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            Main.getPoints().remove(p);
            actualizeBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(240);
        name.getChildren().add(new Label("Point " + Main.getPoints().size()));
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(230);
        delete.getChildren().add(close);
        nameBoard.getChildren().addAll(name,delete);

        // X cursor
        Slider xSlid = new Slider(0,Main.getxSize(),p.getX());
        xSlid.setBlockIncrement(1);
        xSlid.setMajorTickUnit(8);
        xSlid.setShowTickLabels(true);
        xSlid.valueProperty().addListener((obs, oldval, newVal) -> xSlid.setValue(newVal.intValue()));
        xSlid.valueProperty().addListener(event -> {
            p.setX((float) xSlid.getValue());
            p.getCircle().setCenterX(Main.toScreenX(p.getX()));
            p.getCircle().setCenterY(Main.toScreenY(p.getY()));
            Main.setPoints(Vector.shortPoints(Main.vectors));
            drawPolygons();
        });
        xSlid.setPrefWidth(250);
        xBoard.getChildren().addAll(new Label("      X : "),xSlid);

        //Y cursor
        Slider ySlid = new Slider(0,Main.getxSize(),p.getY());
        ySlid.setBlockIncrement(1);
        ySlid.setMajorTickUnit(8);
        ySlid.setShowTickLabels(true);
        ySlid.valueProperty().addListener((obs, oldval, newVal) -> ySlid.setValue(newVal.intValue()));
        ySlid.valueProperty().addListener(event -> {
            p.setY((float) ySlid.getValue());
            p.getCircle().setCenterX(Main.toScreenX(p.getX()));
            p.getCircle().setCenterY(Main.toScreenY(p.getY()));
            Main.setPoints(Vector.shortPoints(Main.vectors));
            drawPolygons();
        });
        ySlid.setPrefWidth(250);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);

        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }
    
    public VBox pointBoard(Vector p, int pointName, Sector pol){
        VBox ptsBoard = new VBox();
        HBox nameBoard = new HBox();
        HBox pointBoard = new HBox();
        HBox xBoard = new HBox();
        HBox yBoard = new HBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        ptsBoard.setStyle(
                "-fx-padding: 3px;" +
                "-fx-spacing: 5px;" +
                "-fx-background-color: #707070; " +
                "-fx-background-radius : 3 3 3 3;"
        );

        xBoard.setAlignment(Pos.CENTER);
        yBoard.setAlignment(Pos.CENTER);

        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            pol.getPoints().remove(p);
            //pol.setupShapes();
            renderPane.actualizePolygon(pol);
            actualizeBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(120);
        Label labelName = new Label("Point " + pointName);
        labelName.setStyle("-fx-text-fill:WHITE;");
        name.getChildren().add(labelName);
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(230);
        delete.getChildren().add(close);
        nameBoard.getChildren().addAll(name,delete);

        // X cursor
        Slider xSlid = new Slider(0,Main.getxSize(),p.getX());
        xSlid.setBlockIncrement(1);
        xSlid.setMajorTickUnit(8);
        xSlid.setShowTickLabels(true);
        xSlid.valueProperty().addListener((obs, oldval, newVal) -> xSlid.setValue(newVal.intValue()));
        xSlid.valueProperty().addListener(event -> {
            p.setX((float) xSlid.getValue());
            p.getCircle().setCenterX(Main.toScreenX(p.getX()));
            p.getCircle().setCenterY(Main.toScreenY(p.getY()));
            renderPane.actualizePolygon(pol);
            drawPolygons();
        });
        xSlid.setPrefWidth(220);
        xBoard.getChildren().addAll(new Label("      X : "),xSlid);

        //Y cursor
        Slider ySlid = new Slider(0,Main.getxSize(),p.getY());
        ySlid.setBlockIncrement(1);
        ySlid.setMajorTickUnit(8);
        ySlid.setShowTickLabels(true);
        ySlid.valueProperty().addListener((obs, oldval, newVal) -> ySlid.setValue(newVal.intValue()));
        ySlid.valueProperty().addListener(event -> {
            p.setY((float) ySlid.getValue());
            p.getCircle().setCenterX(Main.toScreenX(p.getX()));
            p.getCircle().setCenterY(Main.toScreenY(p.getY()));
            renderPane.actualizePolygon(pol);
            drawPolygons();
        });
        ySlid.setPrefWidth(220);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }

    public HBox heightBoard(Sector sector){
        HBox genericBox = new HBox();
        VBox rightPart = new VBox();
        VBox heightBoard = new VBox();
        HBox label = new HBox();

        genericBox.setStyle(
               "-fx-background-radius: 8 0 0 8;" +
               "-fx-background-color: #757575;" +
               "-fx-padding: 10px;" +
               "-fx-spacing: 10px;"
        );

        Label lName = new Label("Height");
        Button help = new Button("?");
        help.setPrefSize(10d,10d);
        help.setOnMouseReleased(event -> {
            System.out.println("no help for now, good luck");
        });
        label.getChildren().addAll(lName,help);

        Slider height = new Slider(0, 31, sector.getHeight());
        height.setPrefWidth(256d);
        height.setBlockIncrement(1);
        height.setMajorTickUnit(8);
        height.setShowTickLabels(true);
        height.valueProperty().addListener(
                (obs, oldval, newVal) -> {
                    height.setValue(newVal.intValue());
                    sector.getPolygon().setHeight((float) height.getValue());
                    sector.getPolygon().setIsoShapes();
                    drawPolygons();
                }
        );
        height.valueProperty().addListener(event -> {
        });
        heightBoard.getChildren().addAll(label,height);

        // Type RadioButtons
        ToggleGroup choise = new ToggleGroup();

        for (Type type : Type.values()) {
            RadioButton typeButton = new RadioButton(type.name().toLowerCase().toUpperCase());
            typeButton.setId(type.name());
            typeButton.setToggleGroup(choise);
            typeButton.selectedProperty().addListener(event -> {
                sector.setType(type);
                drawPolygons();
            });
            if (sector.getType() == type) typeButton.setSelected(true);
            rightPart.getChildren().add(typeButton);
        }

        genericBox.getChildren().addAll(heightBoard,rightPart);
        return genericBox;
    }

    public TreeItem<String> polygonBoard(Wall obj){
        Sector p = obj.getPolygon();

        TreeItem<String> sectorItem = new TreeItem<>("Sector " + obj.getId());
        sectorItem.setExpanded(false);

        TreeItem<String> pointItem = new TreeItem<>("Points");
        pointItem.setExpanded(false);
        TreeItem<String> wallItem = new TreeItem<>("Walls");
        pointItem.setExpanded(false);
        int i = 0;
        for (Vector v : obj.getPolygon().getPoints()) {
            TreeItem<String> point = new TreeItem<>("point " + v);
            TreeItem<String> wall = new TreeItem<>("wall " + v);
            pointItem.getChildren().add(point);
            wallItem.getChildren().add(wall);
            points.add(point.hashCode(), v);
        }
        sectorItem.getChildren().add(wallItem);
        sectorItem.getChildren().add(pointItem);

        //wallMap.put(polItem, obj);
        return sectorItem;
    }

    public void actualizeBoard(){
        polyBoard.getChildren().clear();
        polyBoard.getChildren().add(sectorTree);

        drawPolygons();
    }

    public void actualizeView(ActionEvent ae){
        renderPane = isoview.isSelected() ?
                new IsometricRender(xSize/2) :
                new ClassicRender();

        ImageView imgSclt = new ImageView();
        ImageView img = new ImageView();
        try {
            imgSclt = new ImageView(new Image(Objects.requireNonNull(Main.class.getResource("images/icon.png")).openStream()));
            img = new ImageView(new Image(Objects.requireNonNull(Main.class.getResource("images/wall.png")).openStream()));;
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
        actualizeAllPolygons();
        coordinateSystem.getChildren().clear();
        ArrayList<Vector> vectors = Main.getPoints();

        //draw the grid
        renderPane.drawGrid(coordinateSystem);

        //draw points
        for(Vector vector : vectors) {
            renderPane.drawPointShape(coordinateSystem, vector);
        }

        //draw polygons
        for (Wall obj : Main.getStaticObjects()) {
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

    public void reset(ActionEvent actionEvent) {
        Main.setPlayer1(new Player(Main.getxSize()/2, Main.getxSize()/2));
        Main.setPoints(new ArrayList<>());
        Main.setPolygons(new ArrayList<>());
        actualizeBoard();
        drawPolygons();
    }

    public Type selectType(ToggleGroup tg){
        RadioButton rd = (RadioButton)tg.getSelectedToggle();
        return Type.toType(rd.getId());
    }

    public void setPolygon(ActionEvent actionEvent) {
        if (Main.getPoints().size() >= 2) {
            setPolygon(Main.getPoints());
            Main.setPoints(new ArrayList<>());
            actualizeBoard();
        }
    }
    public void setPolygon(Vector... vectors) {
        setPolygon(new ArrayList<>(Arrays.asList(vectors)));
    }

    public void setPolygon(ArrayList<Vector> vectors) {
        Wall obj = new Wall(
                new Sector(vectors, (float)polHeightSlide.getValue(),selectType(SelectionOption))
                ,selectType(SelectionOption)
        );
        Main.getStaticObjects().add(obj);
        //polyBoard.getChildren().add(polygonBoard(obj));

        TreeItem<String> polItem = polygonBoard(obj);
        sectorTree.getRoot().getChildren().add(polItem);
        sectors.add(polItem.hashCode(), obj.getPolygon());
        //sectorMap.put(polItem, obj);

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
        vBox.setStyle("-fx-background-color: #880000");
        ArrayList<String> listeSave = new ArrayList<>();
        File file = new File("maps");
        boolean res = file.mkdir();
        if (res)
        {
            //System.out.println("le dossier a été créer");
        }
        else
        {
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
                    Map_old.importMap(new File("maps\\" + f.getName()));
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
    /*
        Map map = Main.getMap();
        System.out.println("Exctraction ...");
        for (int i = 0; i < map.getSize()/16; i++) {
            for (int j = 0; j < map.getSize()/16; j++) {
                map.getChunks()[i][j] = new Chunk(findChunkPols(j, i));
            }
        }
        WriteMap(map.getLevelID(), map.exportMap());
    }
     */

    public static void WriteMap(String name, String content)throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name + ".map"));
        writer.write(content);
        writer.close();
    }
}