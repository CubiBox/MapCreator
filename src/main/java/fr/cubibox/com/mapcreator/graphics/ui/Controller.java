package fr.cubibox.com.mapcreator.graphics.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class Controller {

    public MenuBar menubar;
    public MenuBarController menuBarController;

    public ScrollPane pane;
    public PaneController paneController;

    public VBox settingBoard;
    public SettingController settingController;

    public VBox treeviewBoard;
    public TreeViewController treeViewController;

    private static Controller instance;

    private Controller(){
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
    }

    public static Controller getInstance(){
        if (instance == null){
            instance = new Controller();
        }
        return instance;
    }


    public void initialize() {
        //coordinateSystem.setPrefSize(scrollPane.getWidth(),scrollPane.getHeight());


    }


    public void actualizeBoard(){

    }
}