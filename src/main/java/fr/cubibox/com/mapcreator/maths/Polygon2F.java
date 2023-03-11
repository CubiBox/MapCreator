package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.Type;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

public class Polygon2F {
    private float height;
    private Type type;

    //pour l'Editor
    private ArrayList<Vector2F> points;
    private boolean showPoint;
    private Shape ShapeTop;
    private Shape ShapeIso;
    private ArrayList<Shape> ShapesIso;
    private Shape ShapeLeft;
    private Shape ShapeRight;


    public Polygon2F(ArrayList<Vector2F> points, float height) {
        this.height = height;
        this.type = Type.WALL;
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
        this.type = Type.WALL;
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


    public void setupShapes(){
        setTopShape();
        setIsoShapes();
        //setLeftShape();
        //setRightShape();
    }

    private void setTopShape() {
        double[] polPoints = new double[points.size()*2];
        int countP = 0;
        for (Vector2F p : points){
            polPoints[countP++] = Main.toScreenX(p.getX());
            polPoints[countP++] = Main.toScreenY(p.getY());
        }
        this.ShapeTop = new javafx.scene.shape.Polygon(polPoints);
        this.ShapeTop.setFill(Color.TRANSPARENT);
        this.ShapeTop.setStrokeWidth(2.0);
        this.ShapeTop.setStroke(Color.CYAN);
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
            pol.setStroke(Color.CYAN);
        }
        this.ShapesIso = shapes;
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


    public void setShapeTop(javafx.scene.shape.Shape shapeTop) {
        this.ShapeTop = shapeTop;
    }

    public ArrayList<Vector2F> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Vector2F> points) {
        this.points = points;
    }

    public javafx.scene.shape.Shape getShapeTop() {
        return ShapeTop;
    }

    public void setPolShape(javafx.scene.shape.Polygon polShape) {
        this.ShapeTop = polShape;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Shape getShapeIso() {
        return ShapeIso;
    }

    public ArrayList<Shape> getShapesIso() {
        return ShapesIso;
    }
}
