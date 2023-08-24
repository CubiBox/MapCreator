package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.map_old.Map_old;
import fr.cubibox.com.mapcreator.mapObject.LivingEntity;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.mapObject.TileEntity;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {
    public static float DIML = 980;
    public static float DIMC = 980;
    public static float xSize = 16f;

    public static ArrayList<Vector2F> points = new ArrayList<>();
    public static ArrayList<StaticObject> staticObjects = new ArrayList<>();
    public static ArrayList<LivingEntity> mobs = new ArrayList<>();
    public static ArrayList<TileEntity> containers = new ArrayList<>();

    public static Player player1;
    public static Map_old mapOld;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sample.fxml")));
        primaryStage.setTitle("Map maker");
        primaryStage.getIcons().add(new Image(new BufferedInputStream(Objects.requireNonNull(Main.class.getResource("images/icon.png")).openStream())));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        player1 = new Player(xSize/2,xSize/2);
        //map = new Map("level1", xSize);

//        File f = new File("maps/level1.map");
//        Map.importMap(f);
//        WriteMap(map.getIdLevel(), map.exportMap());

        launch(args);
    }

    public static float toScreenX(double x){
        return (float)(DIMC*(x)/(xSize));
    }
    public static float toScreenY(double y){
        return (float)(DIML*(y)/(xSize));
    }


    public static ArrayList<StaticObject> getStaticObjects() {
        return staticObjects;
    }
    public static void setPolygons(ArrayList<StaticObject> staticObjects) {
        Main.staticObjects = staticObjects;
    }

    public static Map_old getMap() {
        return mapOld;
    }
/*
    public static void setMap(Map map) {
        Main.map = map;
        ArrayList<Polygon2F> allPol = new ArrayList<>();

        ArrayList<Integer> counPol = new ArrayList<>();
        int xChunk = 0;
        int yChunk = 0;
        for (Chunk[] chunkL : map.getChunks()){
            for (Chunk chunk : chunkL){
                if (chunk.getPols() != null)
                    for (Polygon2F pol : chunk.getPols()){
                        if (!counPol.contains(Integer.parseInt(pol.getId()))){
                            allPol.add(pol);
                            counPol.add(Integer.parseInt(pol.getId()));
                        }
                    }
                xChunk++;
            }
            yChunk++;
            xChunk=0;
        }
        Main.setPolygons(allPol);
    }

 */

    public static float getDIML() {
        return DIML;
    }

    public static void setDIML(float DIML) {
        Main.DIML = DIML;
    }

    public static float getDIMC() {
        return DIMC;
    }

    public static void setDIMC(float DIMC) {
        Main.DIMC = DIMC;
    }

    public static float getxSize() {
        return xSize;
    }

    public static void setxSize(float xSize) {
        Main.xSize = xSize;
    }

    public static ArrayList<Vector2F> getPoints() {
        return points;
    }

    public static void setPoints(ArrayList<Vector2F> points) {
        Main.points = points;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Player player1) {
        Main.player1 = player1;
    }

    public static void setStaticObjects(ArrayList<StaticObject> staticObjects) {
        Main.staticObjects = staticObjects;
    }

    public static ArrayList<LivingEntity> getMobs() {
        return mobs;
    }

    public static void setMobs(ArrayList<LivingEntity> mobs) {
        Main.mobs = mobs;
    }

    public static ArrayList<TileEntity> getContainers() {
        return containers;
    }

    public static void setContainers(ArrayList<TileEntity> containers) {
        Main.containers = containers;
    }

    public static void setMap(Map_old mapOld) {
        Main.mapOld = mapOld;
    }
}
