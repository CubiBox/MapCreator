package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Polygon2F;

import java.util.ArrayList;

public class Chunk {
    private ArrayList<Polygon2F> pols = new ArrayList<>();
    private boolean isLoad;

    float originX;
    float originY;

    public Chunk(ArrayList<Polygon2F> pols) {
        this.pols = pols;
        this.isLoad = false;
    }

    public Chunk(ArrayList<Polygon2F> pols, int x, int y) {
        this.pols = pols;
        this.isLoad = false;
        this.originX = x;
        this.originY = y;
    }


    public static ArrayList<Polygon2F> findChunkPols(int x , int y) {
        int maxX = x * 16 + 16;
        int maxY = y * 16 + 16;
        int minX = x * 16;
        int minY = y * 16;

        ArrayList<Polygon2F> polIn = Main.getPolygons();
        ArrayList<Polygon2F> polOut = new ArrayList<>();

        for (Polygon2F pol : polIn){
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


    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public ArrayList<Polygon2F> getPols() {
        return pols;
    }

    public void setPols(ArrayList<Polygon2F> pols) {
        this.pols = pols;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }
}
