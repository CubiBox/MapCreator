package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.map.Type;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.*;

import static fr.cubibox.com.mapcreator.map.Type.*;

public class Sector {
    public static int staticId;

    private final TreeItem<String> treeItem;
    private final int id;
    private float ceilHeight;
    private float floorHeight;
    private final HashSet<Integer> wallIds;


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
        this.treeItem = new TreeItem<>("sector");
        this.id = treeItem.hashCode();
        this.treeItem.setValue("sector " + this.id);
        this.ceilHeight = ceilHeight;
        this.floorHeight = floorHeight;
        this.wallIds = new HashSet<>();
        this.type = type;
    }

    public String toName(){
        String out = "";
        out += "Polygon";
        return out;
    }

    public void addWallIds(Integer... wallIds) {
        this.wallIds.addAll(Arrays.asList(wallIds));
    }
    public void addWallId(Integer wallIds) {
        this.wallIds.add(wallIds);
    }
    public void addWallIds(HashSet<Integer> wallID) {
        this.wallIds.addAll(wallID);
    }

    public static int newId(){
        staticId++;
        return staticId;
    }

    public int getId() {
        return id;
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

    public float getCeilHeight() {
        return ceilHeight;
    }

    public float getFloorHeight() {
        return floorHeight;
    }

    public HashSet<Integer> getWallIds() {
        return wallIds;
    }

    public void setCeilHeight(float ceilHeight) {
        this.ceilHeight = ceilHeight;
    }

    public void setFloorHeight(float floorHeight) {
        this.floorHeight = floorHeight;
    }

    public TreeItem<String> getTreeItem() {
        return treeItem;
    }


}
