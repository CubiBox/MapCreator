package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.iu.Point;
import fr.cubibox.com.mapcreator.map.Chunk;
import fr.cubibox.com.mapcreator.map.Map;
import fr.cubibox.com.mapcreator.map.Polygon;
import fr.cubibox.com.mapcreator.map.Type;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;
import java.util.ResourceBundle;

import static fr.cubibox.com.mapcreator.map.Chunk.findChunkPols;
import static fr.cubibox.com.mapcreator.map.Type.*;

public class Controller implements Initializable {

    @FXML
    private Pane coordinateSystem;

    @FXML
    private ToggleButton walls;

    @FXML
    private ToggleGroup SelectionOption;

    @FXML
    private ToggleButton bottom;
    @FXML
    private ToggleButton top;

    @FXML
    private ToggleButton topView;
    @FXML
    private ImageView topViewImage;
    @FXML
    private ToggleButton leftView;
    @FXML
    private ToggleButton rightView;

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
        drawFunction();

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
            drawFunction();
        });

        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float roundX = BigDecimal.valueOf(Main.toPlotX(event.getX()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();
                float roundY = BigDecimal.valueOf(Main.toPlotY(event.getY()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();

                if ((roundX >= 0 && roundX <= Main.xSize && roundY >= 0 && roundY <= Main.xSize)) {
                    Point p = new Point(roundX, roundY);
                    Main.getPoints().add(p);
                    polyBoard.getChildren().add(pointBoard(p));
                }
            }
            drawFunction();
        });
    }


    public VBox pointBoard(Point p){
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
            Main.setPoints(Point.shortPoints());
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
            Main.setPoints(Point.shortPoints());
        });
        ySlid.setPrefWidth(250);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }
    public VBox pointBoard(Point p, int pointName, Polygon pol){
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
            pol.setupEdges();
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
            pol.setupEdges();
            drawFunction();
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
            pol.setupEdges();
            drawFunction();
        });
        ySlid.setPrefWidth(220);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }


    public HBox heightBoard(Polygon p){
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

        Slider height = new Slider(1, 32, 1);
        height.setPrefWidth(256d);
        height.setBlockIncrement(1);
        height.setMajorTickUnit(8);
        height.setShowTickLabels(true);
        height.valueProperty().addListener((obs, oldval, newVal) -> height.setValue(newVal.intValue()));
        height.valueProperty().addListener(event -> {
            p.setHeight((float) height.getValue());
            drawFunction();
        });
        heightBoard.getChildren().addAll(label,height);

        // Type RadioButtons
        ToggleGroup choise = new ToggleGroup();

        RadioButton wallButton = new RadioButton("Wall");
        wallButton.setId("Wall");
        wallButton.setToggleGroup(choise);
        wallButton.selectedProperty().addListener(event -> {
            p.setType(WALL);
            drawFunction();
        });

        RadioButton floorButton = new RadioButton("Floor");
        floorButton.setId("Floor");
        floorButton.setToggleGroup(choise);
        floorButton.selectedProperty().addListener(event -> {
            p.setType(FLOOR);
            drawFunction();
        });

        RadioButton cellingButton = new RadioButton("Celling");
        cellingButton.setId("Celling");
        cellingButton.setToggleGroup(choise);
        cellingButton.selectedProperty().addListener(event -> {
            p.setType(CELLING);
            drawFunction();
        });

        switch (p.getType()) {
            case WALL -> wallButton.setSelected(true);
            case FLOOR -> floorButton.setSelected(true);
            case CELLING -> cellingButton.setSelected(true);
        }

        rightPart.getChildren().addAll(wallButton,floorButton,cellingButton);

        genericBox.getChildren().addAll(heightBoard,rightPart);
        return genericBox;
    }

//    public VBox textureBoard(Polygon p){
//        return null;
//    }


    public VBox polygonBoard(Polygon p){
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
            Main.getPolygons().remove(p);
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
        polBoard.getChildren().addAll(heightBoard(p));

        //main board adds
        polBoard.getChildren().addAll(nameBoard,pointBoard);
        return polBoard;
    }

    public void actuBoard(){
        polyBoard.getChildren().clear();
        for (Point p : Main.getPoints()){
            polyBoard.getChildren().add(pointBoard(p));
        }

        for (Polygon p : Main.getPolygons()){
            VBox pBoard = polygonBoard(p);

            if (p.isShowPoint()) {
                int countPoint = 0;
                for (Point pt : p.getPoints()) {
                    pBoard.getChildren().add(pointBoard(pt, countPoint, p));
                    countPoint++;
                }
            }
            polyBoard.getChildren().add(pBoard);
        }
        drawFunction();
    }

    public void actuSelection(){

    }
    public void actuCheckbox(ActionEvent ae){
        CheckBox cb = (CheckBox) ae.getSource();
        if (cb.isSelected()) {
            try {
                cb.setGraphic(new ImageView(new Image(Objects.requireNonNull(Main.class.getResource("images/icon.png")).openStream())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else topViewImage.setImage(new Image(String.valueOf(new File(String.valueOf(Main.class.getResource("images/wall.png"))))));
    }

    public void drawFunction() {
        coordinateSystem.getChildren().clear();

        ArrayList<Point> points = Main.getPoints();

        for (Rectangle r : drawGrid())
            coordinateSystem.getChildren().add(r);

        //draw the player's point
        //coordinateSystem.getChildren().add(Main.getPlayer1().getPoint().getCircle());

        //draw the points
        for (Point p : points) {
            coordinateSystem.getChildren().add(p.getCircle());
        }

        //draw the polygons
        for (Polygon pols : Main.getPolygons()) {
            coordinateSystem.getChildren().add(pols.getPolShape());
            switch (pols.getType()) {
                case WALL -> pols.getPolShape().setStroke(Color.CYAN);
                case FLOOR -> pols.getPolShape().setStroke(Color.LIME);
                case CELLING -> pols.getPolShape().setStroke(Color.RED);
            }
            if (pols.isShowPoint()) {
                int countP = 0;
                for (Point p : pols.getPoints()) {
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

    public ArrayList<Rectangle> drawGrid(){
        double w = (double) Main.DIMC;
        double h = (double) Main.DIML;
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

        Color gray1 = Color.rgb(30,30,30);
        Color gray2 = Color.rgb(70,70,70);
        Rectangle r;

        for (int i = 0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                r = new Rectangle(0, Main.toScreenY(i + j)-1, w, 2.0);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i=0; i<Main.getxSize(); i+=8)
            for (int j=1; j<=7; j++) {
                r = new Rectangle(Main.toScreenX(i + j)-1, 0, 2.0, h);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i = 0; i<Main.getxSize(); i+=8) {
            r = new Rectangle(0, Main.toScreenY(i)-1.25, w, 2.5);
            r.setFill(gray2);
            rectangles.add(r);
        }

        for (int i=0; i<Main.getxSize(); i+=8) {
            r = new Rectangle(Main.toScreenX(i)-1.25, 0, 2.5, h);
            r.setFill(gray2);
            rectangles.add(r);
        }

        r = new Rectangle(w/2-1, 0, 2, h);
        r.setFill(Color.rgb(160,160,160));
        rectangles.add(r);

        r = new Rectangle(0, h/2-1, w, 2);
        r.setFill(Color.rgb(180,180,180));
        rectangles.add(r);

        return rectangles;
    }

    public Circle drawPoint(double x, double y){
        return new Circle(((x * Main.DIMC/(Main.xSize))),((y * Main.DIML/(Main.xSize))),1, Color.CYAN);
    }

    public void reset(ActionEvent actionEvent) {
        Main.setPlayer1(new Player(Main.getxSize()/2, Main.getxSize()/2));
        Main.setPoints(new ArrayList<>());
        Main.setPolygons(new ArrayList<>());
        actuBoard();
        drawFunction();
    }

    public Type selectType(ToggleGroup tg){
        RadioButton rd = (RadioButton)tg.getSelectedToggle();
        return Type.toType(rd.getId());
    }

    public void setPolygon(ActionEvent actionEvent) {
        if (!Main.getPoints().isEmpty()) {
            Polygon p = new Polygon(Main.getPoints(), (float) polHeightSlide.getValue(), selectType(SelectionOption));
            Main.getPolygons().add(p);
            Main.setPoints(new ArrayList<>());
            polyBoard.getChildren().add(polygonBoard(p));
            actuBoard();
        }
        drawFunction();
    }

    public void setPolyLine(ActionEvent actionEvent) {
        if (!Main.getPoints().isEmpty()) {
            Polygon p = new Polygon(Main.getPoints(), 1, true);
            Main.getPolygons().add(p);
            Main.setPoints(new ArrayList<>());
            polyBoard.getChildren().add(polygonBoard(p));
            actuBoard();
        }
        drawFunction();
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
                    drawFunction();
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
        Map map = Main.getMap();
        System.out.println("Exctraction ...");
        for (int i = 0; i < map.getMapSize()/16; i++) {
            for (int j = 0; j < map.getMapSize()/16; j++) {
                map.getMapContent()[i][j] = new Chunk(findChunkPols(j, i));
            }
        }
        WriteMap(map.getIdLevel(), map.exportMap());
    }

    public static void WriteMap(String name, String content)throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name + ".map"));
        writer.write(content);
        writer.close();
    }
}
