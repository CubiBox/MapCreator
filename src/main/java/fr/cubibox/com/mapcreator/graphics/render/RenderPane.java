package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.graphics.ui.PaneController;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.maths.Sector;
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



    public RenderPane(Pane coordinateSystem) {
        this.cam = new Vector2F(0.f, 0.f);
        this.zoom = 900;
        this.origin = new Vector2F(xSize/2,xSize/2);
    }

    public void render(){

    }

    public void drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
    }

    public void move(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        float xDistance = dragPointOrigin[0] - dragPoint[0];
        float yDistance = dragPointOrigin[1] - dragPoint[1];

        //|| (xDistance > 75 || yDistance > 75) || (xDistance < -75 || yDistance < -75)
        if (!dragState) {
            dragPointOrigin[0] = dragPoint[0];
            dragPointOrigin[1] = dragPoint[1];
            xDistance = 0;
            yDistance = 0;
        }


        cam.setX(cam.getX() - xDistance);
        cam.setY(cam.getY() - yDistance);

        dragPointOrigin[0] = dragPoint[0];
        dragPointOrigin[1] = dragPoint[1];
    }

    public void zoom(boolean dragState, double value, ScrollEvent event) {
        zoom += value;
        cam.setX((float) (cam.getX() - value * 0.5));
        cam.setY((float) (cam.getY() - value * 0.5));

        System.out.println(zoom);
    }


    public void drawShapes(Pane coordinateSystem, Sector pol) {
    }

    public void drawPolygon(Pane coordinateSystem, Sector obj) {
    }

    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
    }
    public void drawPointShape(Pane coordinateSystem, Vector2F vector) {
    }

    public void drawTemporaryPolygon(Pane coordinateSystem, ArrayList<Shape> shape){
    }

    public void drawGrid(Pane coordinateSystem) {
    }


    public ArrayList<Shape> getTempPolygons() {
        return tempPolygons;
    }

    public ArrayList<Vector2v> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        return null;
    }
}
