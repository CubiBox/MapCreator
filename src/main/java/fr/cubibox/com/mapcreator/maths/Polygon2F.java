package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.Type;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

import static fr.cubibox.com.mapcreator.mapObject.Type.*;

public class Polygon2F {
    private float height;
    private final Type type;
    private boolean selected;

    private ArrayList<Vector2F> points;
    private boolean showPoint;

    //shape for views
    private ArrayList<Shape> shapesIso;
    private ArrayList<Shape> shapeTop;

    private Shape shapeLeft;
    private Shape shapeRight;

    private ArrayList<Shape> shapes;


    public Polygon2F(ArrayList<Vector2F> points, float height) {
        this(points,height, WALL);
    }
    public Polygon2F(Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)),0, WALL);
    }
    public Polygon2F(Type type, Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)),0, type);
    }
    public Polygon2F(ArrayList<Vector2F> points, float height, Type type) {
        this.height = height;
        this.type = type;
        if (!points.isEmpty()) {
            this.points = points;
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
        this.shapeTop = topShape(points,type);
    }

    public static ArrayList<Shape> topShape(Type type, Vector2F ... pts) {
        return topShape(new ArrayList<>(Arrays.asList(pts)),type);
    }

    public static ArrayList<Shape> topShape(ArrayList<Vector2F> points, Type type) {
        ArrayList<Shape> lines = new ArrayList<>();

        Color color = Color.CYAN;
        color = switch (type) {
            case FLOOR -> Color.LIME;
            case CELLING -> Color.RED;
            default -> Color.CYAN;
        };

        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; ) {
                Line line = new Line(
                        Main.toScreenX(points.get(i).getX()), Main.toScreenY(points.get(i).getY()),
                        Main.toScreenX(points.get(++i).getX()), Main.toScreenY(points.get(i).getY())
                );
                line.setFill(Color.TRANSPARENT);
                line.setStrokeWidth(2.0);
                line.setStroke(color);
                lines.add(line);
            }
        }
        Line line = new Line(
                Main.toScreenX(points.get(points.size()-1).getX()), Main.toScreenY(points.get(points.size()-1).getY()),
                Main.toScreenX(points.get(0).getX()), Main.toScreenY(points.get(0).getY())
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

    public ArrayList<Vector2F> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Vector2F> points) {
        this.points = points;
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
