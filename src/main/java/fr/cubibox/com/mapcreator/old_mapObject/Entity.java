package fr.cubibox.com.mapcreator.old_mapObject;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;

public abstract class Entity extends MapObject {
    private Sector collision;
    private String UUID;
    private float height;
    private String Inventory; //temporaire


    public Entity(Sector polygon, Vector origin, String id) {
        super(polygon, origin, id);
    }

    public Sector getCollision() {
        return collision;
    }

    public void setCollision(Sector collision) {
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
