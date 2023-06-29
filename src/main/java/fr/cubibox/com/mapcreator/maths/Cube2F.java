package fr.cubibox.com.mapcreator.maths;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.Texture;
import fr.cubibox.com.mapcreator.mapObject.Type;
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


    public Cube2F(ArrayList<Vector2F> points, float height, Type type) {
        super(points, height, type);

        this.textureTop = new Texture("dirt.png", 4);
        this.textureNorth = new Texture("dirt.png", 3);
        this.textureEast = new Texture("dirt.png", 2);
        this.textureWest = new Texture("dirt.png", 1);
        this.textureSouth = new Texture("dirt.png", 0);
    }

    public Cube2F(ArrayList<Vector2F> points, float height) {
        this(points, height, WALL);
    }

    public Cube2F(Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)), 0, WALL);
    }

    public Cube2F(Type type, Vector2F... pts) {
        this(new ArrayList<>(Arrays.asList(pts)), 0, type);
    }

    public void setupShapes(){
        setTopShape();
        setIsoShapes();
        //setLeftShape();
        //setRightShape();
        System.out.println("here");
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

    public ArrayList<Shape> getIsoRender(){
        ArrayList<Shape> shapes = new ArrayList<>();
        double[] polPoints = new double[points.size()*2];
        float currentHeight,currentBase;
        Polygon shape;
        Color wallColor;

        switch (type) {
            case FLOOR -> {
                wallColor = new Color(0, 1, 0, 0.3);
                currentHeight = height;
                currentBase = 0;
            }
            case CELLING -> {
                wallColor = new Color(1, 0, 0, 0.3);
                currentHeight = 31;
                currentBase =height;
            }
            case CUBE -> {
                wallColor = new Color(0, 1, 1, 0.3);
                currentHeight = 5;
                currentBase = height;
            }
            default -> { //  WALL or null
                wallColor = new Color(0, 1, 1, 0.3);
                currentHeight = height;
                currentBase = 0;
            }
        }

        //top
        int countP = 0;
        for (Vector2F p : points){
            float[] v = Main.isometricRender.toScreenIso(p.getX(),p.getY(),currentHeight);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        shape = new Polygon(polPoints);
        shape.setFill(type==FLOOR ? new Color(0, 1, 0, 0.3) : Color.TRANSPARENT);
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
            shape.setFill(wallColor);
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
        return null;
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
}
