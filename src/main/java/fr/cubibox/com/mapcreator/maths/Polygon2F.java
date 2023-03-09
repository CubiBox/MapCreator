package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

public class Polygon2F {
    private float height;

    //pour l'Editor
    private ArrayList<Vector2F> points;
    private boolean showPoint;
    private javafx.scene.shape.Shape ShapeTop;
    private javafx.scene.shape.Shape ShapeIso;
    private javafx.scene.shape.Shape ShapeLeft;
    private javafx.scene.shape.Shape ShapeRight;


    public Polygon2F(ArrayList<Vector2F> points, float height) {
        this.height = height;
        if (!points.isEmpty()) {
            this.points = points;
            setupShapes();
        }
        this.showPoint = false;
    }

    public Polygon2F(Vector2F... pts) {
        this.height = 0;
        this.points = new ArrayList<>(Arrays.asList(pts));
        setupShapes();

        this.showPoint = false;
    }


    public void setupShapes(){
        setIsoShape();
        setTopShape();
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

    public void setIsoShape(){
        ArrayList<Double> polPoints = new ArrayList<>();

        float[] v = null;
        for (Vector2F p : points){
            v = Main.toScreenIso(p.getX(),p.getY(),height);
            polPoints.add((double) v[0]);
            polPoints.add((double) v[1]);

            v = Main.toScreenIso(p.getX(),p.getY(),0);
            polPoints.add((double) v[0]);
            polPoints.add((double) v[1]);

            v = Main.toScreenIso(p.getX(),p.getY(),height);
            polPoints.add((double) v[0]);
            polPoints.add((double) v[1]);
        }
        v = Main.toScreenIso(points.get(0).getX(),points.get(0).getY(),height);
        polPoints.add((double) v[0]);
        polPoints.add((double) v[1]);

        for (Vector2F p : points){
            v = Main.toScreenIso(p.getX(),p.getY(),0);
            polPoints.add((double) v[0]);
            polPoints.add((double) v[1]);
        }
        v = Main.toScreenIso(points.get(0).getX(),points.get(0).getY(),0);
        polPoints.add((double) v[0]);
        polPoints.add((double) v[1]);

        int countP = 0;
        double[] points = new double[polPoints.size()];
        for (double d : polPoints)
            points[countP++] = d;

        this.ShapeIso = new javafx.scene.shape.Polygon(points);
        this.ShapeIso.setFill(Color.TRANSPARENT);
        this.ShapeIso.setStrokeWidth(2.0);
        this.ShapeIso.setStroke(Color.CYAN);
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
}
