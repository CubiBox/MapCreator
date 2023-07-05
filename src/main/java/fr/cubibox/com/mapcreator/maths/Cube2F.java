package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.Face;
import fr.cubibox.com.mapcreator.graphics.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.Texture;
import fr.cubibox.com.mapcreator.mapObject.Type;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.cubibox.com.mapcreator.mapObject.Type.*;

public class Cube2F extends Polygon2F {
    private Texture textureTop;
    private Texture textureNorth;
    private Texture textureEast;
    private Texture textureWest;
    private Texture textureSouth;

    private ArrayList<Face> faces;


    public Cube2F(ArrayList<Vector2F> points, float height, Type type) {
        super(points, height, type);
        resetTextures();

        this.textureTop = new Texture("dirt.png", 4);
        this.textureNorth = new Texture("dirt.png", 3);
        this.textureEast = new Texture("dirt.png", 2);
        this.textureWest = new Texture("dirt.png", 1);
        this.textureSouth = new Texture("dirt.png", 0);
    }

    public Cube2F(ArrayList<Vector2F> points, float height) {
        this(points, height, CUBE);
    }

    public Cube2F(Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)), 0, CUBE);
    }

    public Cube2F(Type type, Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)), 0, type);
    }

    public void setupShapes(){
        setTopShape();
        setIsoShapes();
        //setLeftShape();
        //setRightShape();
        resetTextures();
        System.out.println("here");
    }

    private void resetTextures() {
        faces = new ArrayList<>();
        for(int pt = 0; pt < points.size() - 1; pt++){
            faces.add(new Face(points.get(pt), points.get(pt+1), height, new Texture("dirt.png")));
        }
        faces.add(new Face(points.get(points.size()-1), points.get(0), height, new Texture("dirt.png")));
    }

    private void setTopShape() {
        this.shapeTop = topShape(points,type);
    }

    public static ArrayList<Shape> topShape(Type type, Vector2F ... pts) {
        return topShape(new ArrayList<>(Arrays.asList(pts)),type);
    }

    public static ArrayList<Shape> topShape(ArrayList<Vector2F> points, Type ... type) {
        ArrayList<Shape> lines = new ArrayList<>();

        Color color = switch (Arrays.stream(type).toList().get(0)) {
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

    public String toName(){
        String out = "";
        out += "Cube (Polygon)";
        return out;
    }

    public Texture getTextureTop() {
        return textureTop;
    }

    public Texture getTextureNorth() {
        return textureNorth;
    }

    public Texture getTextureEast() {
        return textureEast;
    }

    public Texture getTextureWest() {
        return textureWest;
    }

    public Texture getTextureSouth() {
        return textureSouth;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }
}
