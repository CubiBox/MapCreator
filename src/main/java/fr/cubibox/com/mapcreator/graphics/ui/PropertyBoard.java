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
import java.util.function.Consumer;

public class PropertyBoard {
    private Controller controller;
    private VBox propertyBoard;
    private VBox board;

    public PropertyBoard(VBox propertyBoard, Controller controller) {
        this.propertyBoard = propertyBoard;
        this.controller = controller;
        VBox board = new VBox();
        board.setStyle(
                "-fx-background-radius : 8 8 8 8;" +
                "-fx-background-color : #656565;" +
                "-fx-padding : 10px;" +
                "-fx-spacing : 5px"
        );
        this.board = board;
    }

    public void init (Sector sector){
        board.getChildren().clear();

        //close button
        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            this.controller.repositories.remove(sector);
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
                showP.setText("Show Points");
                sector.setShowPoint(false);
            }
            else {
                showP.setText("Hide Points");
                sector.setShowPoint(true);
            }
            this.controller.drawPolygons();
        });


        HBox heightBoard = new HBox();
        VBox rightPart = new VBox();
        VBox heightBox = new VBox();
        HBox label = new HBox();

        Label lName = new Label("Height");
        Button help = new Button("?");
        help.setPrefSize(10d,10d);
        help.setOnMouseReleased(event -> {
            System.out.println("no help for now, good luck");
        });
        label.getChildren().addAll(lName,help);

        Slider ceilHeight = new Slider(0, 31, sector.getCeilHeight());
        ceilHeight.setPrefWidth(256d);
        ceilHeight.setBlockIncrement(1);
        ceilHeight.setMajorTickUnit(8);
        ceilHeight.setShowTickLabels(true);
        ceilHeight.valueProperty().addListener(
                (obs, oldval, newVal) -> {
                    ceilHeight.setValue(newVal.intValue());
                    sector.setCeilHeight((float) ceilHeight.getValue());
                    this.controller.drawPolygons();
                }
        );
        Slider floorHeight = new Slider(0, 31, sector.getFloorHeight());
        floorHeight.setPrefWidth(256d);
        floorHeight.setBlockIncrement(1);
        floorHeight.setMajorTickUnit(8);
        floorHeight.setShowTickLabels(true);
        floorHeight.valueProperty().addListener(
                (obs, oldval, newVal) -> {
                    floorHeight.setValue(newVal.intValue());
                    sector.setFloorHeight((float) floorHeight.getValue());
                    this.controller.drawPolygons();
                }
        );
        heightBox.getChildren().addAll(label,ceilHeight,floorHeight);

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

        // Name Label
        HBox name = new HBox();
        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(120);
        name.getChildren().add(new Label(sector.toName()));

        //add name, delete, show points buttons
        HBox nameBoard = new HBox();
        nameBoard.getChildren().addAll(name,delete,showP);

        //Height Board
        board.getChildren().addAll(heightBox,nameBoard,rightPart);
    }

    public void init (Vector2F p){
        board.getChildren().clear();

        HBox nameBoard = new HBox();
        VBox pointBoard = new VBox();

        HBox xBoard = new HBox();
        HBox yBoard = new HBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        xBoard.setAlignment(Pos.CENTER);
        yBoard.setAlignment(Pos.CENTER);

        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            //controller.tpmPoints.remove(p);
            this.controller.repositories.remove(p);
            this.controller.actualizeBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(240);
        name.getChildren().add(new Label("vector " + p.getId()));
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
            controller.tpmPoints = new ArrayList<>(Vector2F.shortPoints(controller.tpmPoints));
            this.controller.drawPolygons();
        });
        xSlid.setPrefWidth(250);
        xBoard.getChildren().addAll(new Label("X : "),xSlid);

        //Y cursor
        Slider ySlid = new Slider(0,Main.getxSize(),p.getY());
        ySlid.setBlockIncrement(1);
        ySlid.setMajorTickUnit(8);
        ySlid.setShowTickLabels(true);
        ySlid.valueProperty().addListener((obs, oldval, newVal) -> ySlid.setValue(newVal.intValue()));
        ySlid.valueProperty().addListener(event -> {
            p.setY((float) ySlid.getValue());
            controller.tpmPoints = new ArrayList<>(Vector2F.shortPoints(controller.tpmPoints));
            this.controller.drawPolygons();
        });
        ySlid.setPrefWidth(250);
        yBoard.getChildren().addAll(new Label("Y : "),ySlid);

        pointBoard.setAlignment(Pos.CENTER);
        pointBoard.getChildren().addAll(xBoard,yBoard);

        //main board adds
        board.getChildren().addAll(nameBoard,pointBoard);
    }

    public void init (Wall wall){
        board.getChildren().clear();

        HBox nameBoard = new HBox();

        HBox moveBoard = new HBox();
        HBox name = new HBox();
        HBox delete = new HBox();

        moveBoard.setStyle(
                "-fx-background-radius: 4 0 0 4;" +
                "-fx-background-color: #909090;" +
                "-fx-padding: 10px;" +
                "-fx-spacing: 10px;"
        );

        Button close = new Button("X");
        close.setPrefSize(10d,10d);
        close.setOnMouseReleased(event -> {
            //controller.tpmPoints.remove(p);
            this.controller.repositories.remove(wall);
            this.controller.actualizeBoard();
        });

        name.setAlignment(Pos.TOP_LEFT);
        name.setPrefWidth(240);
        name.getChildren().add(new Label("Wall " + wall.getId()));
        delete.setAlignment(Pos.TOP_RIGHT);
        delete.setPrefWidth(230);
        delete.getChildren().add(close);
        nameBoard.getChildren().addAll(name,delete);


        Button xAdd = new Button("->");
        xAdd.setPrefSize(40d,25d);
        xAdd.setOnMouseReleased(event -> {
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            if (vec1.getX() + 1 <= Main.xSize && vec2.getX() + 1 <= Main.xSize) {
                vec1.setX(vec1.getX() + 1);
                vec2.setX(vec2.getX() + 1);
            }
            this.controller.actualizeBoard();
        });

        Button xMinus = new Button("<-");
        xMinus.setPrefSize(40d,25d);
        xMinus.setOnMouseReleased(event -> {
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            if (vec1.getX() - 1 >= 0 && vec2.getX() - 1 >= 0) {
                vec1.setX(vec1.getX() - 1);
                vec2.setX(vec2.getX() - 1);
            }
            this.controller.actualizeBoard();
        });

        Button yAdd = new Button("v");
        yAdd.setPrefSize(40d,25d);
        yAdd.setOnMouseReleased(event -> {
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            if (vec1.getY() + 1 <= Main.xSize && vec2.getY() + 1 <= Main.xSize) {
                vec1.setY(vec1.getY() + 1);
                vec2.setY(vec2.getY() + 1);
            }
            this.controller.actualizeBoard();
        });

        Button yMinus = new Button("^");
        yMinus.setPrefSize(40d,25d);
        yMinus.setOnMouseReleased(event -> {
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            if (vec1.getY() - 1 >= 0 && vec2.getY() - 1 >= 0) {
                vec1.setY(vec1.getY() - 1);
                vec2.setY(vec2.getY() - 1);
            }
            this.controller.actualizeBoard();
        });
        moveBoard.getChildren().addAll(new Label("move : "),xAdd,xMinus,yAdd,yMinus);

        Button split = new Button("subdivide");
        split.setPrefSize(300d,25d);
        split.setOnMouseReleased(event -> {
            controller.repositories.subdivideWall(wall);
            this.controller.actualizeBoard();
        });

        //main board adds
        board.getChildren().addAll(nameBoard,moveBoard,split);
    }

    public VBox getBoard(){
        return board;
    }

}
