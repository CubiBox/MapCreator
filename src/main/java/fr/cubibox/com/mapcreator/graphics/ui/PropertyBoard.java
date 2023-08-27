package fr.cubibox.com.mapcreator.graphics.ui;


import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.Controller;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Wall;
import fr.cubibox.com.mapcreator.map.Type;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PropertyBoard {
    private Controller controller;
    private VBox propertyBoard;
    private VBox board;

    public PropertyBoard(VBox propertyBoard, Controller controller) {
        this.propertyBoard = propertyBoard;
        this.controller = controller;
        this.board = new VBox();
    }

    public void init (Sector sector){
        //Sector p = obj.getPolygon();
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
            //Main.getStaticObjects().remove(sector);
            this.controller.actualizeBoard();
        });
        HBox delete = new HBox();
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(120);
        delete.getChildren().add(close);

        //showPoint button
        Button showP = new Button("Show Points");
        showP.setPrefSize(100d,10d);
        showP.setOnMouseReleased(event -> {
            if (sector.isShowPoint()) {
                showP.setText("Hide Points");
                sector.setShowPoint(false);
            }else {
                showP.setText("Show Points");
                sector.setShowPoint(true);
            }
            this.controller.actualizeBoard();
        });


        HBox heightBoard = new HBox();
        VBox rightPart = new VBox();
        VBox heightBox = new VBox();
        HBox label = new HBox();

        heightBoard.setStyle(
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

        Slider height = new Slider(0, 31, sector.getCeilHeight());
        height.setPrefWidth(256d);
        height.setBlockIncrement(1);
        height.setMajorTickUnit(8);
        height.setShowTickLabels(true);
        height.valueProperty().addListener(
                (obs, oldval, newVal) -> {
                    height.setValue(newVal.intValue());
                    sector.setCeilHeight((float) height.getValue());
                    this.controller.drawPolygons();
                }
        );
        height.valueProperty().addListener(event -> {
        });
        heightBox.getChildren().addAll(label,height);

        // Type RadioButtons
        ToggleGroup choise = new ToggleGroup();

        for (Type type : Type.values()) {
            RadioButton typeButton = new RadioButton(type.name().toLowerCase().toUpperCase());
            typeButton.setId(type.name());
            typeButton.setToggleGroup(choise);
            typeButton.selectedProperty().addListener(event -> {
                sector.setType(type);
                this.controller.drawPolygons();
            });
            if (sector.getType() == type) typeButton.setSelected(true);
            rightPart.getChildren().add(typeButton);
        }

        heightBoard.getChildren().addAll(heightBox,rightPart);

        // Name Label
        HBox name = new HBox();
        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(120);
        name.getChildren().add(new Label(sector.toName()));

        //add name, delete, show points buttons
        HBox nameBoard = new HBox();
        nameBoard.getChildren().addAll(name,delete,showP);

        //Height Board
        polBoard.getChildren().add(heightBox);

        //main board adds
        polBoard.getChildren().addAll(nameBoard,pointBoard);

        this.board = polBoard;
    }


    public void init (Vector2F p){
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
            controller.tpmPoints.remove(p);
            this.controller.actualizeBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(240);
        name.getChildren().add(new Label("Point " + controller.tpmPoints.size()));
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
            controller.tpmPoints = new ArrayList<>(Vector2F.shortPoints(controller.tpmPoints));
            this.controller.drawPolygons();
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
            controller.tpmPoints = new ArrayList<>(Vector2F.shortPoints(controller.tpmPoints));
            this.controller.drawPolygons();
        });
        ySlid.setPrefWidth(250);
        yBoard.getChildren().addAll(new Label("      Y : "),ySlid);

        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        ptsBoard.getChildren().addAll(nameBoard,pointBoard);

        this.board = ptsBoard;
    }

    public void init (Wall wall){

    }

    public VBox getBoard(){
        return board;
    }

}
