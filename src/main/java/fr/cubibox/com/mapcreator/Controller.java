package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.map.Map;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.mapObject.Type;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
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
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static fr.cubibox.com.mapcreator.mapObject.Type.*;

public class Controller implements Initializable {

    @FXML
    private Pane coordinateSystem;
    @FXML
    private ToggleButton walls;
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
    private Button Reset;

    @FXML
    private Button importer;


    private int paneWidth = 300;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawPolygon();

        polHeightSlide.setBlockIncrement(1);
        polHeightSlide.setMajorTickUnit(8);
        polHeightSlide.setShowTickLabels(true);
        polHeightSlide.valueProperty().addListener(
                (obs, oldval, newVal) -> polHeightSlide.setValue(newVal.intValue())
        );

        mapSizeSlide.setBlockIncrement(1);
        mapSizeSlide.setMajorTickUnit(1);
        mapSizeSlide.setShowTickLabels(true);
        mapSizeSlide.valueProperty().addListener(
                (obs, oldval, newVal) -> mapSizeSlide.setValue(newVal.intValue())
        );
        mapSizeSlide.valueProperty().addListener(event -> {
            Main.setxSize((int) (16*mapSizeSlide.getValue()));
            drawPolygon();
        });
        isoview.setOnAction(this::actuView);

        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && !isoview.isSelected()) {
                float roundX = BigDecimal.valueOf(Main.toPlotX(event.getX()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();
                float roundY = BigDecimal.valueOf(Main.toPlotY(event.getY()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();

                if ((roundX >= 0 && roundX <= Main.xSize && roundY >= 0 && roundY <= Main.xSize)) {
                    Vector2F p = new Vector2F(roundX, roundY);
                    Main.getPoints().add(p);
                    polyBoard.getChildren().add(pointBoard(p));
                }
            }
            drawPolygon();
        });
    }


    public VBox pointBoard(Vector2F p){
        VBox ptsBoard = new VBox();
        HBox nameBoard = new HBox();
        HBox pointBoard = new HBox();
        HBox xBoard = new HBox();
        HBox yBoard = new HBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        Color c = Color.rgb((int) (p.getColor().getRed()*255), (int) (p.getColor().getGreen()*255), (int) (p.getColor().getBlue()*255),0.2);
        ptsBoard.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
        ptsBoard.setSpacing(5d);
        ptsBoard.setFillWidth(true);

        xBoard.setAlignment(Pos.CENTER);
        yBoard.setAlignment(Pos.CENTER);

        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            Main.getPoints().remove(p);
            actuBoard();
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
            Main.setPoints(Vector2F.shortPoints(Main.points));
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
            Main.setPoints(Vector2F.shortPoints(Main.points));
        });
        ySlid.setPrefWidth(250);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }
    
    public VBox pointBoard(Vector2F p, int pointName, Polygon2F pol){
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
            pol.setupShapes();
            actuBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(240);
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
            pol.setupShapes();
            drawPolygon();
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
            pol.setupShapes();
            drawPolygon();
        });
        ySlid.setPrefWidth(220);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }

    public HBox heightBoard(StaticObject obj){
        Polygon2F p = obj.getPolygon();
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

        Slider height = new Slider(0, 31, obj.getPolygon().getHeight());
        height.setPrefWidth(256d);
        height.setBlockIncrement(1);
        height.setMajorTickUnit(8);
        height.setShowTickLabels(true);
        height.valueProperty().addListener(
                (obs, oldval, newVal) -> {
                    height.setValue(newVal.intValue());
                    obj.getPolygon().setHeight((float) height.getValue());
                    obj.getPolygon().setIsoShape();
                    drawPolygon();
                }
        );
        height.valueProperty().addListener(event -> {
        });
        heightBoard.getChildren().addAll(label,height);

        // Type RadioButtons
        ToggleGroup choise = new ToggleGroup();

        RadioButton wallButton = new RadioButton("Wall");
        wallButton.setId("Wall");
        wallButton.setToggleGroup(choise);
        wallButton.selectedProperty().addListener(event -> {
            obj.setType(WALL);
            drawPolygon();
        });

        RadioButton floorButton = new RadioButton("Floor");
        floorButton.setId("Floor");
        floorButton.setToggleGroup(choise);
        floorButton.selectedProperty().addListener(event -> {
            obj.setType(FLOOR);
            drawPolygon();
        });

        RadioButton cellingButton = new RadioButton("Celling");
        cellingButton.setId("Celling");
        cellingButton.setToggleGroup(choise);
        cellingButton.selectedProperty().addListener(event -> {
            obj.setType(CELLING);
            drawPolygon();
        });

        switch (obj.getType()) {
            case WALL -> wallButton.setSelected(true);
            case FLOOR -> floorButton.setSelected(true);
            case CELLING -> cellingButton.setSelected(true);
        }

        rightPart.getChildren().addAll(wallButton,floorButton,cellingButton);

        genericBox.getChildren().addAll(heightBoard,rightPart);
        return genericBox;
    }

    public VBox polygonBoard(StaticObject obj){
        Polygon2F p = obj.getPolygon();
        VBox polBoard = new VBox();
        VBox pointBoard = new VBox();

        // CSS board
        polBoard.setStyle(
                "-fx-background-radius : 8 8 8 8;" +
                "-fx-background-color : #656565;" +
                "-fx-padding : 10px;" +
                "-fx-spacing : 5px"
        );

        //close button
        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            Main.getStaticObjects().remove(obj);
            actuBoard();
        });
        HBox delete = new HBox();
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(230);
        delete.getChildren().add(close);

        //showPoint button
        Button showP = new Button("Show Points");
        showP.setPrefSize(100d,10d);
        showP.setOnMouseReleased(event -> {
            if (p.isShowPoint()) {
                showP.setText("Hide Points");
                p.setShowPoint(false);
            }else {
                showP.setText("Show Points");
                p.setShowPoint(true);
            }
            actuBoard();
        });

        // Name Label
        HBox name = new HBox();
        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(220);
        name.getChildren().add(new Label(p.toName()));

        //add name, delete, show points buttons
        HBox nameBoard = new HBox();
        nameBoard.getChildren().addAll(name,delete,showP);

        //Height Board
        polBoard.getChildren().addAll(heightBoard(obj));

        //main board adds
        polBoard.getChildren().addAll(nameBoard,pointBoard);
        return polBoard;
    }

    public void actuBoard(){
        polyBoard.getChildren().clear();
        for (Vector2F p : Main.getPoints()){
            polyBoard.getChildren().add(pointBoard(p));
        }

        for (StaticObject obj : Main.getStaticObjects()){
            Polygon2F p = obj.getPolygon();
            VBox pBoard = polygonBoard(obj);

            if (p.isShowPoint()) {
                int countPoint = 0;
                for (Vector2F pt : p.getPoints()) {
                    pBoard.getChildren().add(pointBoard(pt, countPoint, p));
                    countPoint++;
                }
            }
            polyBoard.getChildren().add(pBoard);
        }
        drawPolygon();
    }

    public void actuSelection(){

    }

    public void actuView(ActionEvent ae){
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

        ToggleButton cb = (ToggleButton) ae.getSource();
        if (cb.getToggleGroup() != null) {
            for (Toggle toggle : cb.getToggleGroup().getToggles()) {
                ToggleButton tb = (ToggleButton) toggle;
                tb.setGraphic(img);
            }
            ToggleButton cbSlct = (ToggleButton) cb.getToggleGroup().getSelectedToggle();
            cbSlct.setGraphic(imgSclt);

            System.out.println(cbSlct + "; " + cb.getId());
        }
        drawPolygon();
    }

    public void drawPolygon() {
        if (isoview.isSelected()){
            drawPolygonIso();
            return;
        }

        coordinateSystem.getChildren().clear();

        ArrayList<Vector2F> points = Main.getPoints();

        for (Shape r : drawGrid())
            coordinateSystem.getChildren().add(r);

        //draw the player's point
        //coordinateSystem.getChildren().add(Main.getPlayer1().getPoint().getCircle());

        //draw the points
        for (Vector2F p : points) {
            coordinateSystem.getChildren().add(p.getCircle());
        }

        //draw the polygons
        for (StaticObject obj : Main.getStaticObjects()) {
            Polygon2F pols = obj.getPolygon();
            coordinateSystem.getChildren().add(pols.getShapeTop());
            switch (obj.getType()) {
                case WALL -> pols.getShapeTop().setStroke(Color.CYAN);
                case FLOOR -> pols.getShapeTop().setStroke(Color.LIME);
                case CELLING -> pols.getShapeTop().setStroke(Color.RED);
            }
            if (pols.isShowPoint()) {
                int countP = 0;
                for (Vector2F p : pols.getPoints()) {
                    Label pointName = new Label(countP + "");
                    pointName.setLayoutX(Main.toScreenX(p.getX()) - 5);
                    pointName.setLayoutY(Main.toScreenY(p.getY()) - 15);
                    pointName.setTextFill(Color.WHITE);
                    coordinateSystem.getChildren().add(pointName);
                    countP++;
                }
            }
        }
    }

    public void drawPolygonIso() {
        coordinateSystem.getChildren().clear();

        ArrayList<Vector2F> points = Main.getPoints();

        for (Shape r : drawGridIso())
            coordinateSystem.getChildren().add(r);

        //draw the points
        for (Vector2F p : points) {
            Vector2F p2 = new Vector2F(p.getX(),p.getY());
            float[] v = Main.toScreenIso(p.getX(),p.getY());
            p2.getCircle().setCenterX(v[0]);
            p2.getCircle().setCenterY(v[1]);
            p2.getCircle().setFill(p.getColor());
            coordinateSystem.getChildren().add(p2.getCircle());
        }

        //draw the polygons
        for (StaticObject obj : Main.getStaticObjects()) {
            Polygon2F pols = obj.getPolygon();
            coordinateSystem.getChildren().add(pols.getShapeIso());
            System.out.println(pols.getHeight());

            switch (obj.getType()) {
                case WALL -> pols.getShapeIso().setStroke(Color.CYAN);
                case FLOOR -> pols.getShapeIso().setStroke(Color.LIME);
                case CELLING -> pols.getShapeIso().setStroke(Color.RED);
            }

            if (pols.isShowPoint()) {
                int countP = 0;
                for (Vector2F p : pols.getPoints()) {
                    Label pointName = new Label(countP++ + "");

                    float[] v = Main.toScreenIso(p.getX(),p.getY(),obj.getPolygon().getHeight());
                    pointName.setLayoutX(v[0] - 5);
                    pointName.setLayoutY(v[1] - 15);
                    pointName.setTextFill(Color.WHITE);
                    coordinateSystem.getChildren().add(pointName);
                }
            }
        }
    }

    public ArrayList<Rectangle> drawGrid(){

        ToggleButton tb = (ToggleButton) POV.getSelectedToggle();
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

        Color gray1 = Color.rgb(30,30,30);
        Color gray2 = Color.rgb(70,70,70);
        Rectangle r;

        for (int i = 0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                r = new Rectangle(0, Main.toScreenY(i + j)-1, Main.DIMC, 2.0);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i=0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                r = new Rectangle(Main.toScreenX(i + j)-1, 0, 2.0, Main.DIML);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i = 0; i<Main.getxSize(); i+=8) {
            r = new Rectangle(0, Main.toScreenY(i)-1.25, Main.DIMC, 2.5);
            r.setFill(gray2);
            rectangles.add(r);
        }

        for (int i=0; i<Main.getxSize(); i+=8) {
            r = new Rectangle(Main.toScreenX(i)-1.25, 0, 2.5, Main.DIML);
            r.setFill(gray2);
            rectangles.add(r);
        }

        r = new Rectangle(Main.DIMC/2-1, 0, 2, Main.DIML);
        r.setFill(Color.rgb(160,160,160));
        rectangles.add(r);

        r = new Rectangle(0, Main.DIML/2-1, Main.DIMC, 2);
        r.setFill(Color.rgb(180,180,180));
        rectangles.add(r);

        return rectangles;
    }

    public ArrayList<Shape> drawGridIso(){
        ArrayList<Shape> lines = new ArrayList<>();

        Color gray1 = Color.rgb(30,30,30);
        Color gray2 = Color.rgb(70,70,70);
        Line l;

        for (int x = 0; x <= Main.xSize; x++) {
            float[] v1 = Main.toScreenIso(x,0);
            float[] v2 = Main.toScreenIso(x,Main.xSize);
            l = new Line(v1[0],v1[1],v2[0],v2[1]);
            l.setStroke(gray1);
            l.setStrokeWidth(5);
            lines.add(l);
        }

        for (int y = 0; y <= Main.xSize; y++) {
            float[] v1 = Main.toScreenIso(0,y);
            float[] v2 = Main.toScreenIso(Main.xSize,y);
            l = new Line(v1[0],v1[1],v2[0],v2[1]);
            l.setStroke(gray1);
            l.setStrokeWidth(5);
            //l.setSmooth(true);
            lines.add(l);
        }


/*
        for (int i = 0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                l = new Line(0, Main.toScreenY(i + j)-1, Main.DIMC, 2.0);
                l.setFill(gray1);
                lines.add(l);
            }

        for (int i=0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                l = new Rectangle(Main.toScreenX(i + j)-1, 0, 2.0, Main.DIML);
                l.setFill(gray1);
                lines.add(l);
            }

        for (int i = 0; i<Main.getxSize(); i+=8) {
            l = new Rectangle(0, Main.toScreenY(i)-1.25, Main.DIMC, 2.5);
            l.setFill(gray2);
            lines.add(l);
        }

        for (int i=0; i<Main.getxSize(); i+=8) {
            l = new Rectangle(Main.toScreenX(i)-1.25, 0, 2.5, Main.DIML);
            l.setFill(gray2);
            lines.add(l);
        }

        l = new Rectangle(Main.DIMC/2-1, 0, 2, Main.DIML);
        l.setFill(Color.rgb(160,160,160));
        lines.add(l);

        l = new Rectangle(0, Main.DIML/2-1, Main.DIMC, 2);
        l.setFill(Color.rgb(180,180,180));
        lines.add(l);
*/
        return lines;
    }

    public void reset(ActionEvent actionEvent) {
        Main.setPlayer1(new Player(Main.getxSize()/2, Main.getxSize()/2));
        Main.setPoints(new ArrayList<>());
        Main.setPolygons(new ArrayList<>());
        actuBoard();
        drawPolygon();
    }

    public Type selectType(ToggleGroup tg){
        RadioButton rd = (RadioButton)tg.getSelectedToggle();
        return Type.toType(rd.getId());
    }

    public void setPolygon(ActionEvent actionEvent) {
        if (!Main.getPoints().isEmpty()) {
            StaticObject obj = new StaticObject(new Polygon2F(Main.getPoints(), (float) polHeightSlide.getValue()),selectType(SelectionOption));
            Main.getStaticObjects().add(obj);
            Main.setPoints(new ArrayList<>());
            polyBoard.getChildren().add(polygonBoard(obj));
            actuBoard();
        }
        drawPolygon();
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
                    Map.importMap(new File("maps\\" + f.getName()));
                    drawPolygon();
                    actuBoard();
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
