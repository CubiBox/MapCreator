package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.map.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static float DIML = 980;
    public static float DIMC = 980;
    public static float xSize = 32f;

    public static Player player1;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gui.fxml")));
        primaryStage.setTitle("Map maker");
        primaryStage.getIcons().add(new Image(new BufferedInputStream(Objects.requireNonNull(Application.class.getResource("images/icon.png")).openStream())));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        player1 = new Player(xSize/2,xSize/2);

//        map = new Map("level1", xSize);
//        File f = new File("maps/level1.map");
//        Map.importMap(f);
//        WriteMap(map.getIdLevel(), map.exportMap());

        launch(args);
    }
}
