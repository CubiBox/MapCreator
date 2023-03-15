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


    //pour l'Editor
    private ArrayList<Vector2F> points;
    private boolean showPoint;
    private ArrayList<Shape> shapeTop;
    private Shape shapeIso;
    private ArrayList<Shape> shapesIso;
    private Shape shapeLeft;
    private Shape shapeRight;


    public Polygon2F(ArrayList<Vector2F> points, float height) {
        this.height = height;
        this.type = WALL;
        if (!points.isEmpty()) {
            this.points = points;
            setupShapes();
        }
        this.showPoint = false;
    }

    public Polygon2F(ArrayList<Vector2F> points, float height, Type type) {
        this.height = height;
        this.type = type;
        if (!points.isEmpty()) {
            this.points = points;
            setupShapes();
        }
        this.showPoint = false;
    }

    public Polygon2F(Vector2F... pts) {
        this.height = 0;
        this.type = WALL;
        this.points = new ArrayList<>(Arrays.asList(pts));
        setupShapes();

        this.showPoint = false;
    }

    public Polygon2F(Type type, Vector2F... pts) {
        this.height = 0;
        this.type = type;
        this.points = new ArrayList<>(Arrays.asList(pts));
        setupShapes();

        this.showPoint = false;
    }

    public static ArrayList<Shape> topShape(Type type, Vector2F ... pts) {
        return topShape(new ArrayList<>(Arrays.asList(pts)),type);
    }

    public static ArrayList<Shape> topShape(ArrayList<Vector2F> points, Type ... type) {
        double[] polPoints = new double[points.size()*2];
        ArrayList<Shape> lines = new ArrayList<>();

        Type currentType = Arrays.stream(type).toList().get(0);
        Color color = Color.CYAN;
        color = switch (Arrays.stream(type).toList().get(0)) {
            case FLOOR -> Color.LIME;
            case CELLING -> Color.RED;
            default -> Color.CYAN;
        };

        if (points.size() > 1) {
            for (int i = 0; i < points.size()-1; ) {
                Line line = new Line(
                        Main.toScreenX(points.get(i).getX()), Main.toScreenY(points.get(i).getY()),
                        Main.toScreenX(points.get(++i).getX()), Main.toScreenY(points.get(i).getY())
                );
                line.setFill(Color.TRANSPARENT);
                line.setStrokeWidth(2.0);
                line.setStroke(color);
                lines.add(line);
            }
            Line line = new Line(
                    Main.toScreenX(points.get(points.size()-1).getX()), Main.toScreenY(points.get(points.size()-1).getY()),
                    Main.toScreenX(points.get(0).getX()), Main.toScreenY(points.get(0).getY())
            );
            line.setFill(Color.TRANSPARENT);
            line.setStrokeWidth(2.0);
            line.setStroke(color);
            lines.add(line);
        }
        return lines;
    }


    public void setupShapes(){
        setTopShape(type);
        setIsoShapes();
        //setLeftShape();
        //setRightShape();
    }

    private void setTopShape(Type type) {
        this.shapeTop = topShape(points,type);
    }


    public void setIsoShapes(){
        ArrayList<Shape> shapes = new ArrayList<>();
        double[] polPoints = new double[points.size()*2];
        Polygon shape = null;
        float currentHeight = switch (type) {
            case WALL, FLOOR -> height;
            case CELLING -> 31;
        };
        float currentBase = switch (type) {
            case WALL, FLOOR -> 0;
            case CELLING -> height;
        };

        //top
        int countP = 0;
        for (Vector2F p : points){
            float[] v = Main.isometricRender.toScreenIso(p.getX(),p.getY(),currentHeight);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        shape = new Polygon(polPoints);
        shape.setFill(new Color(1, 0, 0, 0.3));
        shapes.add(shape);


        //bottom
        countP = 0;
        for (Vector2F p : points){
            float[] v = Main.isometricRender.toScreenIso(p.getX(),p.getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        shape = new Polygon(polPoints);
        shape.setFill(new Color(0, 1, 0, 0.3));
        shapes.add(shape);


        //faces
        for (int i = 0; i < points.size(); i ++){
            polPoints = new double[8];
            countP = 0;

            float[] v = Main.isometricRender.toScreenIso(points.get(i).getX(),points.get(i).getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
            float[] v1 = Main.isometricRender.toScreenIso(points.get(i).getX(),points.get(i).getY(),currentHeight);
            polPoints[countP++] = v1[0];
            polPoints[countP++] = v1[1];

            int i2 = i+1 < points.size() ? i+1 : 0;
            v1 = Main.isometricRender.toScreenIso(points.get(i2).getX(),points.get(i2).getY(),currentHeight);
            polPoints[countP++] = v1[0];
            polPoints[countP++] = v1[1];
            v = Main.isometricRender.toScreenIso(points.get(i2).getX(),points.get(i2).getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];

            shape = new Polygon(polPoints);
            shape.setFill(new Color(0, 1, 1, 0.3));
            shapes.add(shape);
        }

        //add global settings
        for (Shape pol : shapes) {
            pol.setStrokeWidth(2.0);
            pol.setStroke(
                    switch (type) {
                        case FLOOR -> Color.LIME;
                        case CELLING -> Color.RED;
                        default -> Color.CYAN;
                    }
            );
        }
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

    public Shape getShapeIso() {
        return shapeIso;
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
}
