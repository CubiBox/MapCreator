package fr.cubibox.com.mapcreator.graphics.ui.pane;

import fr.cubibox.com.mapcreator.graphics.ui.TreeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TabPaneController {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    public VBox vbox;
    @FXML
    public MenuBar viewsSelection;
    @FXML
    public Menu views;

    private ViewPane viewPane;

    private static TabPaneController instance;

    private TabPaneController(){ }
    public static TabPaneController getInstance(){
        if (instance == null){
            instance = new TabPaneController();
        }
        return instance;
    }

    public void draw(){
        for (PaneController pc : viewPane.getPanes()) {
            pc.draw();
        }
    }


    public void initialize() {
        viewPane = new ViewPane(ViewType.SINGLE_VIEW);
        vbox.getChildren().set(1, viewPane.getNodes());

        for (ViewType viewType : ViewType.values()) {
            MenuItem menuItem = viewType.asMenuItem();
            menuItem.setOnAction((event) -> {
                viewPane = new ViewPane(viewType);
                vbox.getChildren().set(1, viewPane.getNodes());
                draw();
            });
            views.getItems().add(menuItem);
        }
    }
}
