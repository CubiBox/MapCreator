package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;

public class Vector2v extends Vector2F{
    private static int globalID = 0;

    private final int id;
    private boolean selected;
    private final TreeItem<String> treeItem;


    public Vector2v(float x, float y) {
        super(x, y);

        this.treeItem = new TreeItem<>("vector2f");
        this.id = applyGlobalID();
        this.treeItem.setValue("vector2F " + this.id);
        this.selected = false;
    }

    public static ArrayList<Vector2v> shortPoints(ArrayList<Vector2v> currentVectors){
        ArrayList<Vector2v> shortedVectors = new ArrayList<>();

        while (!currentVectors.isEmpty()){
            Vector2v xP = currentVectors.get(0);
            for(Vector2v p : currentVectors){
                if (p.getX() < xP.getX())
                    xP = p;
            }
            currentVectors.remove(xP);
            shortedVectors.add(xP);
        }
        return shortedVectors;
    }

    public String toString() {
        return "vec_" + id + ": " + this.getX() + ' ' + this.getY() + '\n';
    }

    public TreeItem<String> getTreeItem() {
        return treeItem;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private static int applyGlobalID(){
        return globalID++;
    }
}