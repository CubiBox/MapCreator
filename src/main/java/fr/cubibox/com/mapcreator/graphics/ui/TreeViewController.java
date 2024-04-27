package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Wall;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TreeViewController {

    public VBox polyBoard;
    @FXML
    public TreeView sectorTree;

    public PropertyBoardController property;


    private static TreeViewController instance;

    private TreeViewController(){
        property = new PropertyBoardController();
    }

    public static TreeViewController getInstance(){
        if (instance == null){
            instance = new TreeViewController();
        }
        return instance;
    }

    public void initialize() {
        TreeItem<String> rootItem = new TreeItem<String> ("Sectors");
        rootItem.setExpanded(true);
        sectorTree = new TreeView<String> (rootItem);
        sectorTree.getRoot().getChildren().addAll(
                new TreeItem<String>("Item 1"),
                new TreeItem<String>("Item 2"),
                new TreeItem<String>("Item 3")
        );
        sectorTree.setMaxHeight(500 + 20);
        sectorTree.setMinHeight(500 + 20);

        MultipleSelectionModel<TreeItem<String>> sectorTreeSelectionModel = sectorTree.getSelectionModel();
        sectorTree.getRoot().getChildren().addListener((ListChangeListener) (item) -> {
            //actualizeTreeView();
            System.out.println(item);
        });

        sectorTreeSelectionModel.selectedItemProperty().addListener((item) -> {
            System.out.println("selected");
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

    private void actualizeTreeView() {
        for (Object obj : sectorTree.getRoot().getChildren()){
            TreeItem<String> treeItem = (TreeItem<String>) obj;

            /*
            //remove from tree if delete
            treeItem.getChildren().get(0).getChildren().removeIf(
                    treeItemWall -> !Repositories.getInstance().getAllWalls().contains(Repositories.getInstance().getWallByID(treeItemWall.hashCode()))
            );
            treeItem.getChildren().get(1).getChildren().removeIf(
                    treeItemVector -> !Repositories.getInstance().getAllVectors().contains(Repositories.getInstance().getVectorByID(treeItemVector.hashCode()))
            );
            if (!Repositories.getInstance().getAllSectors().contains(Repositories.getInstance().getSectorByID(treeItem.hashCode()))){
                sectorTree.getRoot().getChildren().remove(treeItem);
            }
            */

            //add in tree if added

            for (int wallID : Repositories.getInstance().getSectorByID(treeItem.hashCode()).getWallIds()) {
                if (!treeItem.getChildren().get(0).getChildren().contains(Repositories.getInstance().getWallByID(wallID).getTreeItem())){
                    treeItem.getChildren().get(0).getChildren().add(Repositories.getInstance().getWallByID(wallID).getTreeItem());
                }
            }
            for (TreeItem<String> vectorItem : treeItem.getChildren().get(1).getChildren()){

            }




            //replace in tree if moved
            //TODO

            for (TreeItem<String> treeItemWall : treeItem.getChildren().get(0).getChildren()){
                Wall currwall = Repositories.getInstance().getWallByID(treeItemWall.hashCode());
                if (treeItemWall.getChildren().get(0).hashCode() != currwall.getVector1ID()) {
                    treeItemWall.getChildren().remove(0);
                    //wall.getChildren().add(repositories.getVectorByID(currwall.getVector1ID()).getTreeItem());
                }
                if (treeItemWall.getChildren().get(1).hashCode() != currwall.getVector2ID()) {
                    treeItemWall.getChildren().remove(1);
                    treeItemWall.getChildren().add(Repositories.getInstance().getVectorByID(currwall.getVector2ID()).getTreeItem());
                    System.out.println(currwall.getVector2ID());
                }
            }

        }
        //sectorTree.refresh();
    }

    public PropertyBoardController getPropertyController() {
        return property;
    }
}