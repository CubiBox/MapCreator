package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.graphics.ui.pane.PaneController;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Sector;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Application.xSize;

public abstract class RenderPane {
    protected ArrayList<Shape> tempPolygons;
    protected PaneController controller;

    protected Vector2F origin;
    protected Vector2F cam;
    protected double zoom;

    protected Pane coordinateSystem;



    public RenderPane(Pane coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
        this.cam = new Vector2F(0.f, 0.f);
        this.zoom = 900;
        this.origin = new Vector2F(xSize/2,xSize/2);
    }

    public void render(){

    }

    //TODO use vector instead of float[]
    public void drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
    }

    //TODO use vector instead of float[]
    public void move(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        if (!dragState) {
            dragPointOrigin[0] = dragPoint[0];
            dragPointOrigin[1] = dragPoint[1];
        }
    }

    public void zoom(boolean dragState, double value, ScrollEvent event) {
        zoom += value;
        cam.setX((float) (cam.getX() - value * 0.5));
        cam.setY((float) (cam.getY() - value * 0.5));

        System.out.println(zoom);
    }


    public void drawShapes(Sector pol) {
    }

    public void drawPolygon(Sector obj) {
    }

    public void drawPointsLabel(Sector pol) {
    }
    public void drawPointShape(Vector2F vector) {
    }

    public void drawTemporaryPolygon(ArrayList<Shape> shape){
    }

    public void drawGrid() {
    }


    public ArrayList<Shape> getTempPolygons() {
        return tempPolygons;
    }

    //TODO use vector instead of float[]
    public ArrayList<Vector2v> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        return null;
    }

    //TODO use vector instead of float[]
    public Vector2F getDistance(boolean dragState, float[] dragPointOrigin, float[] dragPoint){
        float xDistance = dragPointOrigin[0] - dragPoint[0];
        float yDistance = dragPointOrigin[1] - dragPoint[1];

        if (!dragState) {
            xDistance = 0;
            yDistance = 0;
        }

        return new Vector2F(xDistance, yDistance);
    }

    public Vector2F getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2F origin) {
        this.origin = origin;
    }

    public Vector2F getCam() {
        return cam;
    }

    public void setCam(Vector2F cam) {
        this.cam = cam;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setTempPolygons(ArrayList<Shape> arrayList) {
        tempPolygons = arrayList;
    }
}
