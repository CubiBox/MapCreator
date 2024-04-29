package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.Application;
import fr.cubibox.com.mapcreator.graphics.ui.pane.PaneController;
import fr.cubibox.com.mapcreator.graphics.ui.pane.TabPaneController;
import fr.cubibox.com.mapcreator.map.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class SettingController {
    public ToggleButton topView;
    public ImageView topViewImage;

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
        System.out.println("init settings");
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

        TabPaneController.getInstance().draw();
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
            TabPaneController.getInstance().draw();
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

        Sector sec = new Sector(
                (float) polHeightSlide.getValue(),
                (float) 0.,
                Type.WALL
//                selectType(SelectionOption)
        );
        sec.addWallIds(wallsID);

        TreeItem<String> pointItem = new TreeItem<>("Vectors");
        pointItem.setExpanded(false);
        TreeItem<String> wallItem = new TreeItem<>("Walls");
        wallItem.setExpanded(false);

        for (Vector2v v : Repositories.getInstance().getVectors(sec))
            pointItem.getChildren().add(v.getTreeItem());

        for (Wall w : Repositories.getInstance().getWalls(sec))
            wallItem.getChildren().add(w.getTreeItem());

        sec.getTreeItem().getChildren().add(wallItem);
        sec.getTreeItem().getChildren().add(pointItem);

        Repositories.getInstance().add(sec.id, sec);

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