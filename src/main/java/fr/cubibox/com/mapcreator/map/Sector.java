package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.map.Type;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Shape;

import java.util.*;

import static fr.cubibox.com.mapcreator.map.Type.*;

public class Sector extends fr.cubibox.com.mapcreator.maths.Sector {
    public static int staticId;
    private final TreeItem<String> treeItem;

    private Type type;
    private boolean selected;
    private boolean showPoint;


    //shape for views
    private ArrayList<Shape> shapesIso;
    private ArrayList<Shape> shapeTop;

    private Shape shapeLeft;
    private Shape shapeRight;



    public Sector(float ceilHeight, float floorHeight) {
        this(ceilHeight, floorHeight, WALL);
    }
    public Sector(float ceilHeight, float floorHeight, Type type) {
        super(0, ceilHeight, floorHeight);
        this.treeItem = new TreeItem<>("sector");
        this.treeItem.setValue("sector " + this.id);
        this.id = treeItem.hashCode();
        this.type = type;
    }

    public String toName(){
        String out = "";
        out += "Polygon";
        return out;
    }

    public void addWallIds(HashSet<Integer> wallID) {
        this.wallIds.addAll(wallID);
    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public ArrayList<Shape> getShapeTop() {
        return shapeTop;
    }

    public ArrayList<Shape> getShapesIso() {
        return shapesIso;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setShapesIso(ArrayList<Shape> shapesIso) {
        this.shapesIso = shapesIso;
    }

    public void setShapeTop(ArrayList<Shape> shapeTop) {
        this.shapeTop = shapeTop;
    }

    public TreeItem<String> getTreeItem() {
        return treeItem;
    }


}
