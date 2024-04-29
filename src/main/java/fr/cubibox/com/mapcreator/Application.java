package fr.cubibox.com.mapcreator;

import fr.cubibox.com.mapcreator.graphics.ui.*;
import fr.cubibox.com.mapcreator.graphics.ui.pane.PaneController;
import fr.cubibox.com.mapcreator.graphics.ui.pane.TabPaneController;
import fr.cubibox.com.mapcreator.map.Player;
import fr.cubibox.com.mapcreator.map.Repositories;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui.fxml"));
        fxmlLoader.setController(Controller.getInstance());
        BorderPane gui = fxmlLoader.load();

        FXMLLoader panefxmlLoader = new FXMLLoader(getClass().getResource("tabPane.fxml"));
        panefxmlLoader.setController(TabPaneController.getInstance());
        ScrollPane pane = panefxmlLoader.load();

        FXMLLoader settingfxmlLoader = new FXMLLoader(getClass().getResource("settingBoard.fxml"));
        settingfxmlLoader.setController(SettingController.getInstance());
        VBox setting = settingfxmlLoader.load();

        FXMLLoader menufxmlLoader = new FXMLLoader(getClass().getResource("menubar.fxml"));
        menufxmlLoader.setController(MenuBarController.getInstance());
        MenuBar menuBar = menufxmlLoader.load();

        FXMLLoader rightBoardfxmlLoader = new FXMLLoader(getClass().getResource("treeviewBoard.fxml"));
        rightBoardfxmlLoader.setController(Repositories.getInstance().getTreeViewController());
        VBox rightBoard = rightBoardfxmlLoader.load();

        /*
        FXMLLoader PropertyfxmlLoader = new FXMLLoader(getClass().getResource("propertyBoard.fxml"));
        PropertyfxmlLoader.setController(TreeViewController.getInstance().getPropertyController());
        VBox property = PropertyfxmlLoader.load();
        //TreeViewController.getInstance().initialize();

        rightBoard.getChildren().add(property);
*/

        //gui.setBottom();
        SplitPane structure = new SplitPane();
        structure.setOrientation(Orientation.HORIZONTAL);
        structure.getItems().add(setting);
        structure.getItems().add(pane);
        structure.getItems().add(rightBoard);
        structure.setDividerPositions(0.1f, 0.9f, 0.3f);

        gui.setCenter(structure);
//        gui.setLeft(setting);
        gui.setTop(menuBar);
        gui.setRight(rightBoard);

        Scene scene = new Scene(gui);

        primaryStage.setTitle("Map maker");
        primaryStage.getIcons().add(new Image(new BufferedInputStream(Objects.requireNonNull(Application.class.getResource("images/icon.png")).openStream())));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);

        TabPaneController.getInstance().draw();
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
