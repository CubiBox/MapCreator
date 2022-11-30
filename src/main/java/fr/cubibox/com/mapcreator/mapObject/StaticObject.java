package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;

import java.util.ArrayList;

public class StaticObject extends MapObject{
    private Type type;

    public StaticObject(Polygon2F polygon, Vector2F origin) {
        super(polygon, origin);
    }

    public StaticObject(Polygon2F polygon, Vector2F origin, String id) {
        super(polygon, origin, id);
        this.type=Type.WALL;
    }

    public StaticObject(Polygon2F polygon, Type type) {
        super(polygon, new Vector2F(0,0));
        this.type=type;
    }

    public StaticObject(ArrayList<Vector2F>points, float height, Type type){
        super(new Polygon2F(points,height),new Vector2F(0,0));
        this.type = type;
    }

    public StaticObject(ArrayList<Vector2F>points, float height){
        super(new Polygon2F(points,height),new Vector2F(0,0));
        this.type=Type.WALL;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
