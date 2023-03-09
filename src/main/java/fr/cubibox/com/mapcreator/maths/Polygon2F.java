package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Polygon2F {
    private ArrayList<Line2F> edges;
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
            setupEdges();
        }
        this.showPoint = false;
    }


    public void setupEdges(){
        setIsoEdges();

        edges = new ArrayList<>();
        int pSize = points.size() - 1;
        double[] polPoints = new double[points.size()*2];

        for (int countP = 0; countP < pSize; countP ++){
            edges.add(new Line2F(points.get(countP),points.get(countP+1)));
        }
        int countP = 0;
        for (Vector2F p : points){
            polPoints[countP++] = Main.toScreenX(p.getX());
            polPoints[countP++] = Main.toScreenY(p.getY());
        }
        edges.add(new Line2F(points.get(pSize), points.get(0)));
        this.ShapeTop = new javafx.scene.shape.Polygon(polPoints);
        this.ShapeTop.setFill(Color.TRANSPARENT);
        this.ShapeTop.setStrokeWidth(2.0);
        Color c = Color.CYAN;
        //Color c = Color.rgb((int) (getPoints().get(0).getColor().getRed() * 256), (int) (getPoints().get(0).getColor().getGreen() * 256), (int) (getPoints().get(0).getColor().getBlue() * 256), 0.8);
        this.ShapeTop.setStroke(c);
    }

    public void setIsoEdges(){
        ArrayList<Line2F> edges = new ArrayList<>();
        int pSize = points.size() - 1;
        double[] polPoints = new double[points.size()*2];

        for (int countP = 0; countP < pSize; countP ++){
            edges.add(new Line2F(points.get(countP),points.get(countP+1)));
        }
        int countP = 0;
        for (Vector2F p : points){
            float[] v = Main.toScreenIso(p.getX(),p.getY()+height);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        edges.add(new Line2F(points.get(pSize), points.get(0)));
        this.ShapeIso = new javafx.scene.shape.Polygon(polPoints);
        this.ShapeIso.setFill(Color.TRANSPARENT);
        this.ShapeIso.setStrokeWidth(2.0);
        Color c = Color.CYAN;
        //Color c = Color.rgb((int) (getPoints().get(0).getColor().getRed() * 256), (int) (getPoints().get(0).getColor().getGreen() * 256), (int) (getPoints().get(0).getColor().getBlue() * 256), 0.8);
        this.ShapeIso.setStroke(c);
    }

    public Polygon2F(Vector2F... points) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            edges.add(new Line2F(points[i], points[(i + 1) % points.length]));
        }
    }

    public Vector2F[] getAxes() {
        Vector2F[] axes = new Vector2F[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = edges.get(i).getNormal();
        }
        return axes;
    }

    public String toString(){
        String out = "$\n";
        for (Line2F e : edges){
            out += e.toString();
        }
        out += "\t\t%" + (int)height + "\n";
        return out;
    }

    public String toName(){
        String out = "";
        out += "Polygon";
        return out;
    }

    public ArrayList<Line2F> getEdges() {
        return edges;
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

    public void setShapeIso(Shape shapeIso) {
        ShapeIso = shapeIso;
    }
}
