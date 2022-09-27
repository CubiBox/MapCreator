package fr.cubibox.com.mapcreator.iu;

import fr.cubibox.com.mapcreator.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Point {
	private float x;
	private float y;
	private Circle circlePoint;

	private Color color;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;

		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		this.color = Color.rgb(R, G, B);
		this.circlePoint = new Circle(Main.toScreenX(x), Main.toScreenY(y), 3, this.color);
	}

	public static ArrayList<Point> shortPoints(){
		ArrayList<Point> currentPoints = Main.points;
		ArrayList<Point> shortedPoints = new ArrayList<Point>();

		while (!currentPoints.isEmpty()){
			Point xP = currentPoints.get(0);
			for(Point p : currentPoints){
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

	public void setRect(Circle circ) {
		this.circlePoint = circ;
	}

	public String toString() {
		return "[" + (int)this.x + ";" + (int)this.y + "]";
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
}
