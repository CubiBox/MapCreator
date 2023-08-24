package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.shape.Shape;

public class MapObject {

    private Polygon2F polygon;
    private String TextureId;
    private Vector2F origin;


    //for Editor
    private String id;
    private boolean showPoint;
    private javafx.scene.shape.Shape polShape;
    public static int staticId;


    public MapObject(Polygon2F polygon, Vector2F origin) {
        this.polygon = polygon;
        this.origin = origin;
        this.id = newId();
    }

    public MapObject(Polygon2F polygon, Vector2F origin, String id) {
        this.polygon = polygon;
        this.origin = origin;
        this.id = id;
    }


    public static String newId(){
        staticId++;
        return "Polygon"+staticId;
    }

    public Polygon2F getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon2F polygon) {
        this.polygon = polygon;
    }

    public String getTextureId() {
        return TextureId;
    }

    public void setTextureId(String textureId) {
        TextureId = textureId;
    }

    public Vector2F getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2F origin) {
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
