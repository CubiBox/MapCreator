package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.mapObject.MapObject;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;

public abstract class Entity extends MapObject {
    private Polygon2F collision;
    private String UUID;
    private float height;
    private String Inventory; //temporaire


    public Entity(Polygon2F polygon, Vector2F origin, String id) {
        super(polygon, origin, id);
    }

    public Polygon2F getCollision() {
        return collision;
    }

    public void setCollision(Polygon2F collision) {
        this.collision = collision;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getInventory() {
        return Inventory;
    }

    public void setInventory(String inventory) {
        Inventory = inventory;
    }
}
