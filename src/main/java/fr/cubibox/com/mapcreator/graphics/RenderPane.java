package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
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


    public void drawShapes(Pane coordinateSystem, Polygon2F pol) {
    }

    public void drawPolygon(Pane coordinateSystem, StaticObject obj) {
    }

    public void drawPointsLabel(Pane coordinateSystem, Polygon2F pol) {
    }
    public void drawPointShape(Pane coordinateSystem, Vector2F point) {
    }

    public void drawGrid(Pane coordinateSystem) {
    }

    public void actualizePolygon(Polygon2F pol) {
    }


    public ArrayList<Shape> getTempPolygons() {
        return tempPolygons;
    }

    public ArrayList<Vector2F> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        return null;
    }
}
