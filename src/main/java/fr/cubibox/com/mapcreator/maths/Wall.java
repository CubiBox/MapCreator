package fr.cubibox.com.mapcreator.maths;

import javafx.scene.control.TreeItem;

public class Wall {
    private final int id;
    public static int staticId;

    private final int idVec1;
    private final int idVec2;

    private final TreeItem<String> treeItem;

    public Wall(int idVec1, int idVec2) {
        this.idVec1 = idVec1;
        this.idVec2 = idVec2;
        this.treeItem = new TreeItem<>("wall");
        this.id = treeItem.hashCode();
    }

    public int getVector1ID() {
        return idVec1;
    }
    public int getVector2ID() {
        return idVec2;
    }

    /*
    public Vector2F getNormal() {
        return new Vector2F(b.getY() - a.getY(), a.getX() - b.getX());
    }

    public float getLength() {
        Vector2F length = new Vector2F(
                this.getB().getX() - this.getA().getX(),
                this.getB().getY() - this.getA().getY()
        );

        return (float) (Math.sqrt(length.dot(length)));
    }

    public float getSqLength() {
        Vector2F length = new Vector2F(
                this.getB().getX() - this.getA().getX(),
                this.getB().getY() - this.getA().getY()
        );

        return length.dot(length);
    }

    public boolean isPointOnLine(Vector2F intersectionVector) {
        float x1 = this.getA().getX();
        float y1 = this.getA().getY();
        float x2 = this.getB().getX();
        float y2 = this.getB().getY();
        float x3 = intersectionVector.getX();
        float y3 = intersectionVector.getY();

        float d = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);

        return (d == 0);
    }
     */

    public static int newId(){
        staticId++;
        return staticId;
    }

    public int getId() {
        return id;
    }

    public TreeItem<String> getTreeItem() {
        return treeItem;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "id=" + id +
                ", idVec1=" + idVec1 +
                ", idVec2=" + idVec2 +
                '}';
    }
}
