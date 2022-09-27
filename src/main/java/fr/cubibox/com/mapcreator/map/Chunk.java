package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.iu.Point;
import fr.cubibox.com.mapcreator.Main;

import java.util.ArrayList;

public class Chunk {
    private ArrayList<Polygon> pols = new ArrayList<>();
    private boolean isLoad;

    float originX;
    float originY;

    public Chunk(ArrayList<Polygon> pols) {
        this.pols = pols;
        this.isLoad = false;
    }

    public Chunk(ArrayList<Polygon> pols, int x, int y) {
        this.pols = pols;
        this.isLoad = false;
        this.originX = x;
        this.originY = y;
    }


    public static ArrayList<Polygon> findChunkPols(int x , int y) {
        int maxX = x * 16 + 16;
        int maxY = y * 16 + 16;
        int minX = x * 16;
        int minY = y * 16;

        ArrayList<Polygon> polIn = Main.getPolygons();
        ArrayList<Polygon> polOut = new ArrayList<>();

        for (Polygon pol : polIn){
            for (Point p : pol.getPoints()){
                if ((p.getX() >= minX && p.getX() <= maxX) && (p.getY() >= minY && p.getY() <= maxY)){
                    polOut.add(pol);
                    break;
                }
            }
        }
        return polOut;
    }

    public static ArrayList<Polygon> findChunkPols(ArrayList<Polygon> polIn, int x , int y) {
        int maxX = x * 16 + 16;
        int maxY = y * 16 + 16;
        int minX = x * 16;
        int minY = y * 16;

        ArrayList<Polygon> polOut = new ArrayList<>();

        for (Polygon pol : polIn){
            for (Point p : pol.getPoints()){
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

    public ArrayList<Polygon> getPols() {
        return pols;
    }

    public void setPols(ArrayList<Polygon> pols) {
        this.pols = pols;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }
}
