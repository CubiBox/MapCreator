package fr.cubibox.com.mapcreator.map;
import fr.cubibox.com.mapcreator.iu.Point;
import javafx.scene.shape.Line;

public class Edge {
    private Point p1;
    private Point p2;

    private Line edge;
    // private Texture texture;


    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.edge = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }



    public String toString() {
        return "\t\t@[" + (int)p1.getX() + ";" + (int)p1.getY() + "]-[" + (int)p2.getX() + ";" + (int)p2.getY() + "]\n";
    }

    public Line getEdge() {
        return edge;
    }

    public void setEdge(Line edge) {
        this.edge = edge;
    }

    public Point getA() {
        return p1;
    }

    public void setA(Point p1) {
        this.p1 = p1;
    }

    public Point getB() {
        return p2;
    }

    public void setB(Point p2) {
        this.p2 = p2;
    }
}
