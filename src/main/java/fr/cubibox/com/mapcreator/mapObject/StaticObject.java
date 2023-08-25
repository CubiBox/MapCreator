package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;

import java.util.ArrayList;

public class StaticObject extends MapObject{
    private Type type;

    public StaticObject(Sector polygon, Vector origin) {
        super(polygon, origin);
    }

    public StaticObject(Sector polygon, Vector origin, String id) {
        super(polygon, origin, id);
        this.type=Type.WALL;
    }

    public StaticObject(Sector polygon, Type type) {
        super(polygon, new Vector(0,0));
        this.type=type;
    }

    public StaticObject(ArrayList<Vector> vectors, float height, Type type){
        super(new Sector(vectors,height),new Vector(0,0));
        this.type = type;
    }

    public StaticObject(ArrayList<Vector> vectors, float height){
        super(new Sector(vectors,height),new Vector(0,0));
        this.type=Type.WALL;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
