package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.Application;
import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Type;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Wall;
import fr.cubibox.com.mapcreator.maths.Sector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingController {
    public ToggleButton topView;
    public ImageView topViewImage;

    public ToggleButton bottom;
    public ToggleButton top;
    public ToggleButton walls;

    public ToggleButton horizontalView;
    public Slider mapSizeSlide;
    public ToggleButton isoview;
    public RadioButton wall;
    public ToggleGroup SelectionOption;

    public RadioButton floor;
    public RadioButton celling;
    public RadioButton portal;

    public Slider polHeightSlide;
    public Button setPol;

    private static SettingController instance;

    private SettingController(){
    }

    public static SettingController getInstance(){
        if (instance == null){
            instance = new SettingController();
        }
        return instance;
    }

    public void initialize() {

    }

    public void actualizeView(ActionEvent ae){
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

        PaneController.getInstance().draw();
    }

    public Type selectType(ToggleGroup tg){
        RadioButton rd = (RadioButton)tg.getSelectedToggle();
        return Type.toType(rd.getId());
    }

    public void setPolygon(ActionEvent ignored) {
        ArrayList<Vector2v> tpmPoints = Repositories.getInstance().tpmPoints;
        if (Repositories.getInstance().tpmPoints.size() >= 2) {
            setPolygon(Repositories.getInstance().tpmPoints);
            Repositories.getInstance().tpmPoints = new ArrayList<>();
        }
    }

    public void setPolygon(ArrayList<Vector2v> vectors) {
        Vector2v buff = vectors.get(vectors.size()-1);
        HashSet<Integer> wallsID = new HashSet<>();
        for (Vector2v vec : vectors) {
            Repositories.getInstance().add(vec.getId(), vec);
            Wall currWall = new Wall(buff.getId(), vec.getId());
            currWall.getTreeItem().getChildren().add(buff.getTreeItem());
            currWall.getTreeItem().getChildren().add(vec.getTreeItem());

            Repositories.getInstance().add(currWall.getId(), currWall);
            wallsID.add(currWall.getId());
            buff = vec;
        }

        Sector obj = new Sector(
                (float) polHeightSlide.getValue(),
                (float) 0.,
                Type.WALL
//                selectType(SelectionOption)
        );
        obj.addWallIds(wallsID);

        TreeItem<String> pointItem = new TreeItem<>("Vectors");
        pointItem.setExpanded(false);
        TreeItem<String> wallItem = new TreeItem<>("Walls");
        wallItem.setExpanded(false);

        for (Vector2v v : Repositories.getInstance().getVectors(obj))
            pointItem.getChildren().add(v.getTreeItem());

        for (Wall w : Repositories.getInstance().getWalls(obj))
            wallItem.getChildren().add(w.getTreeItem());

        obj.getTreeItem().getChildren().add(wallItem);
        obj.getTreeItem().getChildren().add(pointItem);

//        sectorTree.getRoot().getChildren().add(obj.getTreeItem());
        Repositories.getInstance().add(obj.id, obj);

//        actualizeBoard();
//        drawPolygons();
    }

    public void reset(ActionEvent ignored) {
        //Main.setPlayer1(new Player(Main.getxSize()/2, Main.getxSize()/2));
        Repositories.getInstance().clear();
//        sectorTree = new TreeView<>();
//        actualizeBoard();
//        drawPolygons();
    }
}