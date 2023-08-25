package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.Type;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

import static fr.cubibox.com.mapcreator.mapObject.Type.*;

public class Sector {
    private float height;
    private final Type type;
    private boolean selected;

    private ArrayList<Vector> vectors;
    private boolean showPoint;

    //shape for views
    private ArrayList<Shape> shapesIso;
    private ArrayList<Shape> shapeTop;

    private Shape shapeLeft;
    private Shape shapeRight;

    private ArrayList<Shape> shapes;


    public Sector(ArrayList<Vector> vectors, float height) {
        this(vectors,height, WALL);
    }
    public Sector(Vector... pts) {
        this(new ArrayList<>(Arrays.asList(pts)),0, WALL);
    }
    public Sector(Type type, Vector... pts) {
        this(new ArrayList<>(Arrays.asList(pts)),0, type);
    }
    public Sector(ArrayList<Vector> vectors, float height, Type type) {
        this.height = height;
        this.type = type;
        if (!vectors.isEmpty()) {
            this.vectors = vectors;
            //setupShapes();
        }
        this.showPoint = false;
    }

/*
    public void setupShapes(){
        setTopShape();
        setIsoShapes();
        //setLeftShape();
        //setRightShape();
    }
    */

    private void setTopShape() {
        this.shapeTop = topShape(vectors,type);
    }

    public static ArrayList<Shape> topShape(Type type, Vector... pts) {
        return topShape(new ArrayList<>(Arrays.asList(pts)),type);
    }

    public static ArrayList<Shape> topShape(ArrayList<Vector> vectors, Type type) {
        ArrayList<Shape> lines = new ArrayList<>();

        Color color = Color.CYAN;
        color = switch (type) {
            case FLOOR -> Color.LIME;
            case CELLING -> Color.RED;
            default -> Color.CYAN;
        };

        if (vectors.size() > 1) {
            for (int i = 0; i < vectors.size() - 1; ) {
                Line line = new Line(
                        Main.toScreenX(vectors.get(i).getX()), Main.toScreenY(vectors.get(i).getY()),
                        Main.toScreenX(vectors.get(++i).getX()), Main.toScreenY(vectors.get(i).getY())
                );
                line.setFill(Color.TRANSPARENT);
                line.setStrokeWidth(2.0);
                line.setStroke(color);
                lines.add(line);
            }
        }
        Line line = new Line(
                Main.toScreenX(vectors.get(vectors.size()-1).getX()), Main.toScreenY(vectors.get(vectors.size()-1).getY()),
                Main.toScreenX(vectors.get(0).getX()), Main.toScreenY(vectors.get(0).getY())
        );
        line.setFill(Color.TRANSPARENT);
        line.setStrokeWidth(2.0);
        line.setStroke(color);
        lines.add(line);

        return lines;
    }

    public void setIsoShapes(){
        this.shapesIso = shapes;
    }

    public String toName(){
        String out = "";
        out += "Polygon";
        return out;
    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public ArrayList<Vector> getPoints() {
        return vectors;
    }

    public void setPoints(ArrayList<Vector> vectors) {
        this.vectors = vectors;
    }

    public ArrayList<Shape> getShapeTop() {
        return shapeTop;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
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

    public void setShapesIso(ArrayList<Shape> shapesIso) {
        this.shapesIso = shapesIso;
    }

    public void setShapeTop(ArrayList<Shape> shapeTop) {
        this.shapeTop = shapeTop;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
}
