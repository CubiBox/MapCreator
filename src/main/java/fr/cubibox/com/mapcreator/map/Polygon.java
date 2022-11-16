package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.iu.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();
    private float height;
    private String id;

    private Type type;

    private boolean isLine;

    private boolean showPoint = false;

    private Shape polShape;

//    public Polygon(float height){
//        this.height = height;
//    }

    public Polygon(ArrayList<Point> points, float height){
        this.isLine = false;
        this.height = height;
        if (!points.isEmpty()) {
            this.points = points;
            setupEdges();
        }
    }
    public Polygon(ArrayList<Edge> edges, float height, String id){
        this.isLine = false;
        this.height = height;
        this.edges = edges;
        this.id = id;
    }

    public Polygon(ArrayList<Point> points, float height, boolean isLine){
        this.isLine = isLine;
        this.height = height;
        if (!points.isEmpty()) {
            this.points = points;
            setupEdges();
        }
    }

    public Polygon(ArrayList<Edge> edges, ArrayList<Point> points, float height, String id, boolean isLine){
        this.isLine = isLine;
        this.height = height;
        this.points = points;
        this.id = id;
        if (!points.isEmpty()) {
            this.points = points;
            setupEdges();
        }
    }

    public void setupEdges(){
        edges = new ArrayList<>();
        int pSize = points.size() - 1;
        double[] polPoints = new double[points.size()*2];

        for (int countP = 0; countP < pSize; countP ++){
            edges.add(new Edge(points.get(countP),points.get(countP+1)));
        }
        int countP = 0;
        for (Point p : points){
            polPoints[countP] = Main.toScreenX(p.getX());
            countP ++;
            polPoints[countP] = Main.toScreenY(p.getY());
            countP ++;
        }
        if (this.isLine == true) {
            this.polShape = new javafx.scene.shape.Polyline(polPoints);
            this.polShape.setFill(Color.TRANSPARENT);
            this.polShape.setStrokeWidth(2.0);
            Color c = Color.CYAN;
            //Color c = Color.rgb((int) (getPoints().get(0).getColor().getRed() * 256), (int) (getPoints().get(0).getColor().getGreen() * 256), (int) (getPoints().get(0).getColor().getBlue() * 256), 0.8);
            this.polShape.setStroke(c);
        }
        else {
            edges.add(new Edge(points.get(pSize), points.get(0)));
            this.polShape = new javafx.scene.shape.Polygon(polPoints);
            this.polShape.setFill(Color.TRANSPARENT);
            this.polShape.setStrokeWidth(2.0);
            Color c = Color.CYAN;
            //Color c = Color.rgb((int) (getPoints().get(0).getColor().getRed() * 256), (int) (getPoints().get(0).getColor().getGreen() * 256), (int) (getPoints().get(0).getColor().getBlue() * 256), 0.8);
            this.polShape.setStroke(c);
        }
    }

    public String toString(){
        String out = "$"+id+"\n";
        for (Edge e : edges){
            out += e.toString();
        }
        out += "\t\t%" + (int)height + "\n";
        return out;
    }


    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLine() {
        return isLine;
    }

    public void setLine(boolean line) {
        isLine = line;
    }

    public void setPolShape(Shape polShape) {
        this.polShape = polShape;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public Shape getPolShape() {
        return polShape;
    }

    public void setPolShape(javafx.scene.shape.Polygon polShape) {
        this.polShape = polShape;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
