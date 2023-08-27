package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.iu.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class Main extends Application {
    public static float DIML = 980;
    public static float DIMC = 980;
    public static float xSize = 16f;

    public static Player player1;

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

    public static Player getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Player player1) {
        Main.player1 = player1;
    }
}
