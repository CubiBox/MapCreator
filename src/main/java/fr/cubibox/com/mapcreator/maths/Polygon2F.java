package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.Type;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Polygon2F {
    private ArrayList<Line2F> edges;
    private float height;

    //pour l'Editor
    private ArrayList<Vector2F> points;
    private boolean showPoint;
    private javafx.scene.shape.Shape polShape;


    public Polygon2F(ArrayList<Vector2F> points, float height) {
        this.height = height;
        if (!points.isEmpty()) {
            this.points = points;
            setupEdges();
        }
        this.showPoint = false;
    }


    public void setupEdges(){
        edges = new ArrayList<>();
        int pSize = points.size() - 1;
        double[] polPoints = new double[points.size()*2];

        for (int countP = 0; countP < pSize; countP ++){
            edges.add(new Line2F(points.get(countP),points.get(countP+1)));
        }
        int countP = 0;
        for (Vector2F p : points){
            polPoints[countP] = Main.toScreenX(p.getX());
            countP ++;
            polPoints[countP] = Main.toScreenY(p.getY());
            countP ++;
        }
        edges.add(new Line2F(points.get(pSize), points.get(0)));
        this.polShape = new javafx.scene.shape.Polygon(polPoints);
        this.polShape.setFill(Color.TRANSPARENT);
        this.polShape.setStrokeWidth(2.0);
        Color c = Color.CYAN;
        //Color c = Color.rgb((int) (getPoints().get(0).getColor().getRed() * 256), (int) (getPoints().get(0).getColor().getGreen() * 256), (int) (getPoints().get(0).getColor().getBlue() * 256), 0.8);
        this.polShape.setStroke(c);
    }


    public Polygon2F(Vector2F... points) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            edges.add(new Line2F(points[i], points[(i + 1) % points.length]));
        }
    }

    public ArrayList<Line2F> getEdges() {
        return edges;
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

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }



    public void setPolShape(javafx.scene.shape.Shape polShape) {
        this.polShape = polShape;
    }

    public ArrayList<Vector2F> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Vector2F> points) {
        this.points = points;
    }

    public javafx.scene.shape.Shape getPolShape() {
        return polShape;
    }

    public void setPolShape(javafx.scene.shape.Polygon polShape) {
        this.polShape = polShape;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
