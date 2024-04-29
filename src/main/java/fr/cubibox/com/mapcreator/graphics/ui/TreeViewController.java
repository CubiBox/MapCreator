package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.graphics.ui.pane.PaneController;
import fr.cubibox.com.mapcreator.graphics.ui.pane.TabPaneController;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Wall;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TreeViewController {

    public VBox polyBoard;

    public TreeView<String> sectorTree;

    public PropertyBoardController property;


    private static TreeViewController instance;

    public TreeViewController(){
        property = new PropertyBoardController();
    }

    public void initialize() {
        TreeItem<String> rootItem = new TreeItem<String> ("Sectors");
        rootItem.setExpanded(true);
        sectorTree = new TreeView<String> (rootItem);
        sectorTree.setMaxHeight(500 + 20);
        sectorTree.setMinHeight(500 + 20);
        sectorTree.setShowRoot(true);

        polyBoard.getChildren().add(sectorTree);
        polyBoard.getChildren().add(property.getBoard());

        System.out.println("init treeview");

        MultipleSelectionModel<TreeItem<String>> sectorTreeSelectionModel = sectorTree.getSelectionModel();

        sectorTreeSelectionModel.selectedItemProperty().addListener((item) -> {
            System.out.println("selected");
            System.out.println(item.toString());

            TreeItem<String> currItem = sectorTreeSelectionModel.getSelectedItem();
            String type = currItem.getValue().split(" ")[0];
            int id = Integer.parseInt(currItem.getValue().split(" ")[1]);

            for (Wall wall : Repositories.getInstance().getAllWalls()){
                if (wall.isSelected()) wall.setSelected(false);
            }
            for (Vector2v vec : Repositories.getInstance().getAllVectors()){
                if (vec.isSelected()) vec.setSelected(false);
            }

            if (currItem.getValue().contains("vector")){
                Repositories.getInstance().getVectorByID(id).setSelected(true);
                property.init(Repositories.getInstance().getVectorByID(id));
            }
            else if (currItem.getValue().contains("wall")){
                Repositories.getInstance().getWallByID(id).setSelected(true);
                property.init(Repositories.getInstance().getWallByID(id));
            }
            else if (currItem.getValue().contains("sector")){
                for (int wallId : Repositories.getInstance().getSectorByID(id).getWallIds()) {
                    Repositories.getInstance().getWallByID(wallId).setSelected(true);
                }
                property.init(Repositories.getInstance().getSectorByID(id));
            }
            TabPaneController.getInstance().draw();
        });
    }

    public void actualizeTreeView() {
        for (TreeItem<String> treeItem : sectorTree.getRoot().getChildren()){

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


    public int getIdFromName(String name){
        return Integer.parseInt(name.split(" ")[1]);
    }
}