package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.Application;
import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.map.Type;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.map.Wall;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static fr.cubibox.com.mapcreator.map.Type.*;

public class Controller {

    public MenuBar menubar;
    public MenuBarController menuBarController;

    public ScrollPane pane;
    public PaneController paneController;

    public VBox settingBoard;
    public SettingController settingController;

    public VBox treeviewBoard;
    public RightBoardController rightBoardController;

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