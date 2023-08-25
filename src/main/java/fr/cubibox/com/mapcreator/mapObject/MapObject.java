package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;
import javafx.scene.shape.Shape;

public class MapObject {

    private Sector polygon;
    private String TextureId;
    private Vector origin;


    //for Editor
    private String id;
    private boolean showPoint;
    private javafx.scene.shape.Shape polShape;
    public static int staticId;


    public MapObject(Sector polygon, Vector origin) {
        this.polygon = polygon;
        this.origin = origin;
        this.id = newId();
    }

    public MapObject(Sector polygon, Vector origin, String id) {
        this.polygon = polygon;
        this.origin = origin;
        this.id = id;
    }


    public static String newId(){
        staticId++;
        return "Polygon"+staticId;
    }

    public Sector getPolygon() {
        return polygon;
    }

    public void setPolygon(Sector polygon) {
        this.polygon = polygon;
    }

    public String getTextureId() {
        return TextureId;
    }

    public void setTextureId(String textureId) {
        TextureId = textureId;
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public Shape getPolShape() {
        return polShape;
    }

    public void setPolShape(Shape polShape) {
        this.polShape = polShape;
    }

    @Override
    public String toString() {
        return "MapObject{" +
                "polygon=" + polygon +
                ", TextureId='" + TextureId + '\'' +
                ", origin=" + origin +
                ", id='" + id + '\'' +
                ", showPoint=" + showPoint +
                ", polShape=" + polShape +
                '}';
    }
}
