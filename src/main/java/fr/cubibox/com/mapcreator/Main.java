package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import fr.cubibox.com.mapcreator.iu.Point;
import fr.cubibox.com.mapcreator.map.Chunk;
import fr.cubibox.com.mapcreator.map.Map;
import fr.cubibox.com.mapcreator.map.Polygon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Controller.WriteMap;
import static fr.cubibox.com.mapcreator.map.Chunk.findChunkPols;

public class Main extends Application {
    public static float DIML = 1000;
    public static float DIMC = 1000;
    public static float xSize = 16f;

    public static ArrayList<Point> points = new ArrayList<>();
    public static ArrayList<Polygon> polygons = new ArrayList<>();

    public static Player player1;

    public static Map map;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Map maker");
        primaryStage.getIcons().add(new Image(new BufferedInputStream(Main.class.getResource("images/icon.png").openStream())));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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
        return (float) ((DIML*(y)/(xSize)));
    }

    public static float toPlotX(double scrX){
        return (float) ((scrX/DIMC)*(xSize));
    }

    public static float toPlotY(double scrY){
        return (float) ((scrY/DIML)*(xSize));
    }


    public static ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public static void setPolygons(ArrayList<Polygon> polygons) {
        Main.polygons = polygons;
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        Main.map = map;
        ArrayList<Polygon> allPol = new ArrayList<>();

        ArrayList<Integer> counPol = new ArrayList<>();
        int xChunk = 0;
        int yChunk = 0;
        for (Chunk[] chunkL : map.getMapContent()){
            for (Chunk chunk : chunkL){
                if (chunk.getPols() != null)
                    for (Polygon pol : chunk.getPols()){
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

    public static ArrayList<Point> getPoints() {
        return points;
    }

    public static void setPoints(ArrayList<Point> points) {
        Main.points = points;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Player player1) {
        Main.player1 = player1;
    }
}
