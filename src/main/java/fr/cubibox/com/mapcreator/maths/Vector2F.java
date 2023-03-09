package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Vector2F {
    private float x;
    private float y;

    private Circle circlePoint;
    private Color color;

    public Vector2F(float x, float y) {
        this.x = x;
        this.y = y;

        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);
        this.color = Color.rgb(R, G, B);
        this.circlePoint = new Circle(Main.toScreenX(x), Main.toScreenY(y), 3, this.color);
    }

    public static ArrayList<Vector2F> shortPoints(ArrayList<Vector2F> currentPoints){
        ArrayList<Vector2F> shortedPoints = new ArrayList<>();

        while (!currentPoints.isEmpty()){
            Vector2F xP = currentPoints.get(0);
            for(Vector2F p : currentPoints){
                if (p.getX() < xP.getX())
                    xP = p;
            }
            currentPoints.remove(xP);
            shortedPoints.add(xP);
        }
        return shortedPoints;
    }

    public Circle getCircle() {
        return circlePoint;
    }

    public void setCircle(Circle circ) {
        this.circlePoint = circ;
    }

    public String toString() {
        return "[" + (int)this.x + ";" + (int)this.y + "]";
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Circle getCirclePoint() {
        return circlePoint;
    }

    public void setCirclePoint(Circle circlePoint) {
        this.circlePoint = circlePoint;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addToX(float x){
        this.x += x;
    }

    public void addToY(float y){
        this.y += y;
    }

    public Vector2F add(Vector2F v) {
        return new Vector2F(x + v.getX(), y + v.getY());
    }

    public Vector2F sub(Vector2F v) {
        return new Vector2F(x - v.getX(), y - v.getY());
    }

    public Vector2F mul(float f) {
        return new Vector2F(x * f, y * f);
    }

    public Vector2F div(float f) {
        return new Vector2F(x / f, y / f);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
    public float dot(Vector2F v) {
        return x * v.getX() + y * v.getY();
    }
    public void subToX(float x){
        this.x -= x;
    }

    public void subToY(float y){
        this.y -= y;
    }
}