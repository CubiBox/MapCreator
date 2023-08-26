package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.old_mapObject.Wall;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public abstract class RenderPane {
    protected ArrayList<Shape> tempPolygons;

    public void render(){

    }

    public boolean drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint) {
        return false;
    }


    public void drawShapes(Pane coordinateSystem, Sector pol) {
    }

    public void drawPolygon(Pane coordinateSystem, Wall obj) {
    }

    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
    }
    public void drawPointShape(Pane coordinateSystem, Vector vector) {
    }

    public void drawGrid(Pane coordinateSystem) {
    }

    public void actualizePolygon(Sector pol) {
    }


    public ArrayList<Shape> getTempPolygons() {
        return tempPolygons;
    }

    public ArrayList<Vector> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        return null;
    }
}
