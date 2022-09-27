package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.iu.Point;
import fr.cubibox.com.mapcreator.map.Chunk;
import fr.cubibox.com.mapcreator.map.Map;
import fr.cubibox.com.mapcreator.map.Polygon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ResourceBundle;

import static fr.cubibox.com.mapcreator.map.Chunk.findChunkPols;

public class Controller implements Initializable {

    @FXML
    private Pane coordinateSystem;
    @FXML
    private TextArea functionText;

    @FXML
    private RadioButton addPoints;
    @FXML
    private RadioButton addInt;

    @FXML
    private VBox vBoxPanel;

    @FXML
    private Button Reset;

    @FXML
    private Button importer;


    private int paneWidth = 300;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawFunction();
        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float roundX = BigDecimal.valueOf(Main.toPlotX(event.getX()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();
                float roundY = BigDecimal.valueOf(Main.toPlotY(event.getY()))
                        .setScale(0, BigDecimal.ROUND_HALF_DOWN)
                        .floatValue();

                if ((roundX >= 0 && roundX <= Main.xSize && roundY >= 0 && roundY <= Main.xSize)) {
                    if (addPoints.isSelected()) {
                        Point p = new Point(roundX, roundY);
                        Main.getPoints().add(p);
                        vBoxPanel.getChildren().add(pointBoard(p));
                    }
                    else if (addInt.isSelected()){
                    }
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
        xSlid.setPrefWidth(300);
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
        ySlid.setPrefWidth(300);
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
                "-fx-padding: 2;" +
                "-fx-background-color: #808080"
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
        name.getChildren().add(new Label("Point " + pointName));
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
        xSlid.setPrefWidth(300);
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
        ySlid.setPrefWidth(300);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);


        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        return ptsBoard;
    }


    public VBox polygonBoard(Polygon p){
        VBox polBoard = new VBox();
        HBox nameBoard = new HBox();
        VBox pointBoard = new VBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        polBoard.setStyle(
                "-fx-padding: 5;" +
                "-fx-background-radius: 8 8 8 8;" +
                "-fx-background-color: #656565"
        );
        //Color c = Color.rgb((int) (p.getPoints().get(0).getColor().getRed()*256), (int) (p.getPoints().get(0).getColor().getGreen()*256), (int) (p.getPoints().get(0).getColor().getBlue()*256),0.3);
        //Color c = Color.rgb(0, 255, 255, 0.2);


        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            Main.getPolygons().remove(p);
            actuBoard();
        });

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

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(220);
        if (p.isLine())
            name.getChildren().add(new Label("Line  " + p.getId()));
        else
            name.getChildren().add(new Label("Polygon  " + p.getId()));
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(230);
        delete.getChildren().add(close);
        nameBoard.getChildren().addAll(name,delete,showP);

        //main board adds
        polBoard.getChildren().addAll(nameBoard,pointBoard);

        return polBoard;
    }

    public void actuBoard(){
        vBoxPanel.getChildren().clear();
        for (Point p : Main.getPoints()){
            vBoxPanel.getChildren().add(pointBoard(p));
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
            vBoxPanel.getChildren().add(pBoard);
        }
        drawFunction();
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
        Double w = Double.valueOf(Main.DIMC);
        Double h = Double.valueOf(Main.DIML);
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

    public void setPolygon(ActionEvent actionEvent) {
        if (!Main.getPoints().isEmpty()) {
            Polygon p = new Polygon(Main.getPoints(), 1);
            Main.getPolygons().add(p);
            Main.setPoints(new ArrayList<>());
            vBoxPanel.getChildren().add(polygonBoard(p));
            actuBoard();
        }
        drawFunction();
    }

    public void setPolyLine(ActionEvent actionEvent) {
        if (!Main.getPoints().isEmpty()) {
            Polygon p = new Polygon(Main.getPoints(), 1, true);
            Main.getPolygons().add(p);
            Main.setPoints(new ArrayList<>());
            vBoxPanel.getChildren().add(polygonBoard(p));
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

    public TextArea getFunctionText() {
        return functionText;
    }

    public void setFunctionText(TextArea functionText) {
        this.functionText = functionText;
    }
}
