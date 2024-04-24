package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Wall;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RightBoardController {

    public VBox polyBoard;
    public TreeView sectorTree;

    public PropertyBoardController property;


    private static RightBoardController instance;

    private RightBoardController(){
        property = new PropertyBoardController();
    }

    public static RightBoardController getInstance(){
        if (instance == null){
            instance = new RightBoardController();
        }
        return instance;
    }

    public void initialize() {
        TreeItem<String> rootItem = new TreeItem<String> ("Sectors");
        rootItem.setExpanded(true);
        sectorTree = new TreeView<String> (rootItem);
        sectorTree.setMaxHeight(500 + 20);
        sectorTree.setMinHeight(500 + 20);

        MultipleSelectionModel<TreeItem<String>> sectorTreeSelectionModel = sectorTree.getSelectionModel();
        sectorTreeSelectionModel.selectedItemProperty().addListener((item) -> {
            System.out.println(item.toString());
            TreeItem<String> currItem = sectorTreeSelectionModel.getSelectedItem();

            for (Wall wall : Repositories.getInstance().getAllWalls()){
                if (wall.isSelected()) wall.setSelected(false);
            }
            for (Vector2v vec : Repositories.getInstance().getAllVectors()){
                if (vec.isSelected()) vec.setSelected(false);
            }

            property.getPropertyBoard().getChildren().clear();
            if (currItem.getValue().contains("vector")){
                Repositories.getInstance().getVectorByID(currItem.hashCode()).setSelected(true);
                property.init(Repositories.getInstance().getVectorByID(currItem.hashCode()));
                property.getPropertyBoard().getChildren().add(property.getBoard());
            }
            else if (currItem.getValue().contains("wall")){
                Repositories.getInstance().getWallByID(currItem.hashCode()).setSelected(true);
                property.init(Repositories.getInstance().getWallByID(currItem.hashCode()));
                property.getPropertyBoard().getChildren().add(property.getBoard());
            }
            else {
                try {
                    for (int id : Repositories.getInstance().getSectorByID(currItem.hashCode()).getWallIds()) {
                        Repositories.getInstance().getWallByID(id).setSelected(true);
                    }
                    property.init(Repositories.getInstance().getSectorByID(currItem.hashCode()));
                    property.getPropertyBoard().getChildren().add(property.getBoard());
                }
                catch (NullPointerException npe){
                    System.out.println("id null, skipped");
                }
            }
            PaneController.getInstance().draw();

            //System.out.println();
            //renderPane.drawPointShape(coordinateSystem, (Vector2d) item);
        });
    }

    public PropertyBoardController getPropertyController() {
        return property;
    }
}