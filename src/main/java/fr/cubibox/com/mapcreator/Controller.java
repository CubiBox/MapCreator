package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.graphics.IsometricRender;
import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.map.Map;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.mapObject.Type;
import fr.cubibox.com.mapcreator.maths.MathFunction;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.mapObject.Type.*;

public class Controller implements Initializable {

    public Slider isoAngleSlider;
    public Slider isoAngleSliderHorizontal;
    public AnchorPane base;
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
    private Button Reset;

    @FXML
    private Button importer;

    private ArrayList<Type> drawablePol;
    private boolean dragState;
    private final float[] dragPointOrigin = new float[2];


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        isometricRender = new IsometricRender(xSize/2);

        scrollPane.setPrefWidth(screenWidth/2);
        coordinateSystem.setPrefSize(980,980);

        drawPolygon();
        drawablePol = new ArrayList<>();
        drawablePol.add(WALL);
        drawablePol.add(FLOOR);
        drawablePol.add(CELLING);

        walls.setOnAction(this::actuView);
        walls.selectedProperty().addListener((obs, oldval, newVal) -> {
            if (newVal && !drawablePol.contains(WALL)){
                drawablePol.add(WALL);
            }
            else if (!newVal){
                drawablePol.remove(WALL);
            }
        });
        top.setOnAction(this::actuView);
        top.selectedProperty().addListener((obs, oldval, newVal) -> {
            if (newVal && !drawablePol.contains(CELLING)){
                drawablePol.add(CELLING);
            }
            else if (!newVal){
                drawablePol.remove(CELLING);
            }
        });
        bottom.setOnAction(this::actuView);
        bottom.selectedProperty().addListener((obs, oldval, newVal) -> {
            if (newVal && !drawablePol.contains(FLOOR)){
                drawablePol.add(FLOOR);
            }
            else if (!newVal){
                drawablePol.remove(FLOOR);
            }
        });

        polHeightSlide.setBlockIncrement(1);
        polHeightSlide.setMajorTickUnit(1);
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
        isoAngleSlider.valueProperty().addListener((obs, oldval, newVal) ->{
            isoAngleSlider.setValue(newVal.intValue());
            isometricRender.setyAngleBySlider((float) isoAngleSlider.getValue());
            for (StaticObject obj : Main.staticObjects){
                obj.getPolygon().setIsoShapes();
            }
            drawPolygon();
        });
        isoAngleSliderHorizontal.valueProperty().addListener((obs, oldval, newVal) ->{
            isoAngleSliderHorizontal.setValue(newVal.intValue());
            isometricRender.setxAngleBySlider((float) isoAngleSliderHorizontal.getValue());
            actuAllPolygons();
            drawPolygon();
        });
        isoview.setOnAction(this::actuView);

        //zoom
        coordinateSystem.setOnScroll(event ->{
            double value = event.getDeltaY();
            if (event.isControlDown() && Main.DIML + value < 2000 && Main.DIML + value > scrollPane.getWidth()+20) {
                Main.DIML += value;
                Main.DIMC += value;
                coordinateSystem.setPrefSize(Main.DIMC,Main.DIML);
                actuAllPolygons();
                drawPolygon();
            }
        });

        //reset pane dimension if windowed
        scrollPane.widthProperty().addListener(e -> {
            scrollPane.setPrefWidth(screenWidth/2);
            coordinateSystem.setPrefSize(980,980);
            Main.DIML = 980;
            Main.DIMC = 980;
            actuAllPolygons();
            drawPolygon();
        });

        //record drag status
        coordinateSystem.setOnMouseDragged(event ->{
            if (isoview.isSelected()) {
                if (!dragState || (dragPointOrigin[0]-event.getX()>75 || dragPointOrigin[1]-event.getY()>75)){
                    dragPointOrigin[0] = (float) event.getX();
                    dragPointOrigin[1] = (float) event.getY();
                }

                isometricRender.setxAngle((float) (isometricRender.getxAngle() + (dragPointOrigin[0] - event.getX()) * 0.0045f));
                float temp_yAngle = (float) (isometricRender.getyAngle() - (dragPointOrigin[1] - event.getY()) * 0.001f);
                if (temp_yAngle >= 0 && temp_yAngle <= 0.35) {
                    isometricRender.setyAngle(temp_yAngle);
                }
                actuAllPolygons();
                drawPolygon();

                dragPointOrigin[0] = (float) event.getX();
                dragPointOrigin[1] = (float) event.getY();
            }
            else {
                float roundX = MathFunction.round(Main.toPlotX(event.getX()));
                float roundY = MathFunction.round(Main.toPlotY(event.getY()));

                if (!dragState) {
                    dragPointOrigin[0] = roundX;
                    dragPointOrigin[1] = roundY;
                }

                if ((roundX >= 0 && roundX <= xSize && roundY >= 0 && roundY <= xSize)) {
                    Vector2F currentPos = new Vector2F(roundX, roundY);
                    if (dragState && (dragPointOrigin[0] != currentPos.getX() && dragPointOrigin[1] != currentPos.getY())) {
                        actuAllPolygons();
                        drawPolygon(
                                Polygon2F.topShape(
                                    currentPos,
                                    new Vector2F(dragPointOrigin[0], roundY),
                                    new Vector2F(dragPointOrigin[0], dragPointOrigin[1]),
                                    new Vector2F(roundX, dragPointOrigin[1])
                                )
                        );
                    }
                }
            }
            dragState = true;
        });

        //set point by click
        coordinateSystem.setOnMouseClicked(event -> {
            float roundX = MathFunction.round(Main.toPlotX(event.getX()));
            float roundY = MathFunction.round(Main.toPlotY(event.getY()));
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                if ((roundX >= 0 && roundX <= xSize && roundY >= 0 && roundY <= xSize) && !isoview.isSelected()) {
                    Vector2F currentPos = new Vector2F(roundX, roundY);
                    if (dragState && (dragPointOrigin[0] !=currentPos.getX() && dragPointOrigin[1]!=currentPos.getY())){
                        setPolygon(
                                currentPos,
                                new Vector2F(dragPointOrigin[0], roundY),
                                new Vector2F(dragPointOrigin[0], dragPointOrigin[1]),
                                new Vector2F(roundX, dragPointOrigin[1])
                        );
                    }
                    else {
                        Main.getPoints().add(currentPos);
                        polyBoard.getChildren().add(pointBoard(currentPos));
                    }
                }
                dragState = false;
                System.out.println("reset");
                drawPolygon();
            }
        });


        setPolygon( new Vector2F(xSize/2, xSize/2) );
    }

    public void actuAllPolygons(){
        for (StaticObject obj : Main.staticObjects){
            obj.getPolygon().setIsoShapes();
            obj.getPolygon().setupShapes();
        }
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
                    obj.getPolygon().setIsoShapes();
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

        drawPolygon();
    }

    public void drawPolygon(Shape ... tempPol) {
        if (isoview.isSelected()){
            drawPolygonIso();
            return;
        }
        else if (tempPol != null){
            for (Shape pol : tempPol){
                coordinateSystem.getChildren().add(pol);
            }
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
                    Label pointName = new Label(countP++ + "");
                    pointName.setLayoutX(Main.toScreenX(p.getX()) - 5);
                    pointName.setLayoutY(Main.toScreenY(p.getY()) - 15);
                    pointName.setTextFill(Color.WHITE);
                    coordinateSystem.getChildren().add(pointName);
                }
            }
        }
    }

    public void drawPolygonIso() {
        coordinateSystem.getChildren().clear();
        ArrayList<Vector2F> points = Main.getPoints();

        for (Shape r : drawGrid())
            coordinateSystem.getChildren().add(r);

        //draw the points
        for (Vector2F p : points) {
            float[] v = isometricRender.toScreenIso(p.getX(),p.getY());
            coordinateSystem.getChildren().add(new Circle(v[0],v[1], 3, p.getColor()));
        }

        //draw the polygons
        for (StaticObject obj : Main.getStaticObjects()) {
            if (drawablePol.contains(obj.getType())) {
                Polygon2F pols = obj.getPolygon();

                for (Shape shape : obj.getPolygon().getShapesIso()) {
                    shape.setStroke(
                            switch (obj.getType()) {
                                case WALL -> Color.CYAN;
                                case FLOOR -> Color.LIME;
                                case CELLING -> Color.RED;
                            }
                    );
                    coordinateSystem.getChildren().add(shape);
                }

                if (pols.isShowPoint()) {
                    int countP = 0;
                    for (Vector2F p : pols.getPoints()) {
                        Label pointName = new Label(countP++ + "");
                        float[] v = isometricRender.toScreenIso(p.getX(), p.getY(), obj.getPolygon().getHeight());
                        pointName.setLayoutX(v[0] - 5);
                        pointName.setLayoutY(v[1] - 15);
                        pointName.setTextFill(Color.WHITE);
                        coordinateSystem.getChildren().add(pointName);
                    }
                }
            }
        }
    }

    public ArrayList<Shape> drawGrid() {
        //ToggleButton tb = (ToggleButton) POV.getSelectedToggle();
        ArrayList<Shape> lines = new ArrayList<>();
        ArrayList<Shape> tpmLines = new ArrayList<>();
        boolean isIso = isoview.isSelected();

        Color gray1 = Color.rgb(30, 30, 30);
        Color gray2 = Color.rgb(70, 70, 70);
        Line line1;
        Line line2;

        for (int i = 0; i <= xSize; i++) {
            if (isIso) {
                float[] var1 = isometricRender.toScreenIso(i, 0);
                float[] var2 = isometricRender.toScreenIso(i, xSize);
                float[] var3 = isometricRender.toScreenIso(0, i);
                float[] var4 = isometricRender.toScreenIso(xSize, i);

                line1 = new Line(var1[0], var1[1], var2[0], var2[1]);
                line2 = new Line(var3[0], var3[1], var4[0], var4[1]);
            }
            else {
                line1 = new Line(Main.toScreenX(i), Main.toScreenY(0), Main.toScreenX(i), Main.toScreenY(xSize));
                line2 = new Line(Main.toScreenX(0), Main.toScreenY(i), Main.toScreenX(xSize), Main.toScreenY(i));
            }

            line1.setStroke(gray1);
            line1.setStrokeWidth(3);
            line1.setSmooth(true);

            line2.setStroke(gray1);
            line2.setStrokeWidth(3);
            line2.setSmooth(true);

            if (i % 8 != 0) {
                lines.add(line2);
                lines.add(line1);
            } else {
                tpmLines.add(line1);
                tpmLines.add(line2);
            }
        }

        for (Shape line : tpmLines) {
            line.setStroke(gray2);
            lines.add(line);
        }
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
        if (Main.getPoints().size() >= 2) {
            setPolygon(Main.getPoints());
            Main.setPoints(new ArrayList<>());
        }
    }
    public void setPolygon(Vector2F ... points) {
        setPolygon(new ArrayList<>(Arrays.asList(points)));
    }

    public void setPolygon(ArrayList<Vector2F> points) {
        StaticObject obj = new StaticObject(
                new Polygon2F(points, (float)polHeightSlide.getValue(),selectType(SelectionOption))
                ,selectType(SelectionOption)
        );

        Main.getStaticObjects().add(obj);
        polyBoard.getChildren().add(polygonBoard(obj));
        actuBoard();
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