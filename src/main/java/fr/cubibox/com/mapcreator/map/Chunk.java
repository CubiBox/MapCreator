package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.*;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Polygon2F;

import java.util.ArrayList;

public class Chunk {
    private ArrayList<MapObject> mapObjects = new ArrayList<MapObject>();
    int originX;
    int originY;

    public Chunk(ArrayList<MapObject> mapObjects) {
        this.mapObjects = mapObjects;
    }

    public Chunk(ArrayList<MapObject> mapObjects, int x, int y) {
        this.mapObjects = mapObjects;
        this.originX = x;
        this.originY = y;
    }

    public ArrayList<StaticObject> getStaticObjects(){
        ArrayList<StaticObject> so = new ArrayList<>();
        for (MapObject mo : mapObjects)
            if (mo instanceof StaticObject)
                so.add((StaticObject) mo);
        return so;
    }

    public ArrayList<LivingEntity> getLivingEntities(){
        ArrayList<LivingEntity> le = new ArrayList<>();
        for (MapObject mo : mapObjects)
            if (mo instanceof LivingEntity)
                le.add((LivingEntity) mo);
        return le;
    }

    public ArrayList<TileEntity> getContainers(){
        ArrayList<TileEntity> te = new ArrayList<>();
        for (MapObject mo : mapObjects)
            if (mo instanceof TileEntity)
                te.add((TileEntity) mo);
        return te;
    }

    public ArrayList<Entity> getEntities(){
        ArrayList<Entity> e = new ArrayList<>();
        for (MapObject mo : mapObjects)
            if (mo instanceof Entity)
                e.add((Entity) mo);
        return e;
    }


    public static ArrayList<Polygon2F> findChunkPols(int x , int y) {
        return null;
    }
    /*
        int maxX = x * 16 + 16;
        int maxY = y * 16 + 16;
        int minX = x * 16;
        int minY = y * 16;

        ArrayList<StaticObject> polIn = Main.getStaticObjects();
        ArrayList<StaticObject> polOut = new ArrayList<>();

        for (StaticObject pol : polIn){
            for (Vector2F p : pol.getPoints()){
                if ((p.getX() >= minX && p.getX() <= maxX) && (p.getY() >= minY && p.getY() <= maxY)){
                    polOut.add(pol);
                    break;
                }
            }
        }
        return polOut;
    }

    public static ArrayList<Polygon2F> findChunkPols(ArrayList<Polygon2F> polIn, int x , int y) {
        int maxX = x * 16 + 16;
        int maxY = y * 16 + 16;
        int minX = x * 16;
        int minY = y * 16;

        ArrayList<Polygon2F> polOut = new ArrayList<>();

        for (Polygon2F pol : polIn){
            for (Vector2F p : pol.getPoints()){
                if ((p.getX() > minX && p.getX() <= maxX) && (p.getY() > minY && p.getY() <= maxY)){
                    polOut.add(pol);
                    break;
                }
            }
        }
        return polOut;
    }
*/

    public int getOriginX() {
        return originX;
    }
    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }
    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public ArrayList<MapObject> getMapObjects() {
        return mapObjects;
    }
    public void setMapObjects(ArrayList<MapObject> mapObjects) {
        this.mapObjects = mapObjects;
    }
}
