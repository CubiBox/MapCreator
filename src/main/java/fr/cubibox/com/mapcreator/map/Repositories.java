package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Wall;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Repositories {
    private HashMap<Integer, Sector> sectors;
    private HashMap<Integer, Vector2F> vectors;
    private HashMap<Integer, Wall> walls;
    public Repositories() {
        sectors = new HashMap<>();
        vectors = new HashMap<>();
        walls = new HashMap<>();
    }

    public void add(int id, Sector sector){
        sectors.put(id, sector);
    }
    public void add(int id, Wall wall){
        walls.put(id, wall);
    }
    public void add(int id, Vector2F vector){
        vectors.put(id, vector);
    }


    public void remove(int id, Sector sector){
        sectors.remove(id, sector);
    }
    public void remove(int id, Wall wall){
        walls.remove(id, wall);
    }
    public void remove(int id, Vector2F vector){
        vectors.remove(id, vector);
    }


    public void remove(Sector sector){
        for (int id : sectors.keySet()){
            if (sectors.get(id).equals(sector)) {
                sectors.remove(id);
            }
        }
    }
    public void remove(Wall wall){
        for (int id : walls.keySet()){
            if (walls.get(id).equals(wall)) {
                walls.remove(id);
            }
        }
    }
    public void remove(Vector2F vector){
        for (int id : vectors.keySet()){
            if (vectors.get(id).equals(vector)) {
                vectors.remove(id);
            }
        }
    }


    public boolean contains(int id){
        return (
                sectors.containsKey(id) ||
                        vectors.containsKey(id) ||
                        walls.containsKey(id)
        );
    }

    public boolean contains(Sector sector){
        return (sectors.containsValue(sector));
    }
    public boolean contains(Vector2F vector){
        return (vectors.containsValue(vector));
    }
    public boolean contains(Wall wall){
        return (walls.containsValue(wall));
    }


    public Sector getSectorByID(int id){
        return sectors.get(id);
    }
    public Vector2F getVectorByID(int id){
        return vectors.get(id);
    }
    public Wall getWallByID(int id){
        return walls.get(id);
    }


    public Sector getSector(TreeItem<String> treeItem){
        return sectors.get(treeItem.hashCode());
    }
    public Vector2F getVector(TreeItem<String> treeItem){
        return vectors.get(treeItem.hashCode());
    }
    public Wall getWall(TreeItem<String> treeItem){
        return walls.get(treeItem.hashCode());
    }


    public ArrayList<Sector> getAllSectors(){
        return new ArrayList<>(this.sectors.values());
    }
    public ArrayList<Wall> getAllWalls(){
        return new ArrayList<>(this.walls.values());
    }
    public ArrayList<Vector2F> getAllVectors(){
        return new ArrayList<>(this.vectors.values());
    }


    public ArrayList<Wall> getWalls(Sector sector){
        ArrayList<Wall> walls = new ArrayList<>();
        for (int id : sector.getWallIds()){
            walls.add(getWallByID(id));
        }
        return walls;
    }
    public ArrayList<Vector2F> getVectors(Wall wall){
        ArrayList<Vector2F> vectors = new ArrayList<>();
        vectors.add(getVectorByID(wall.getVector1ID()));
        vectors.add(getVectorByID(wall.getVector2ID()));
        return vectors;
    }

    public ArrayList<Vector2F> getVectors(Sector sector){
        ArrayList<Vector2F> vectors = new ArrayList<>();
        for (int wallId : sector.getWallIds()){
            ArrayList<Vector2F> vec = getVectors(walls.get(wallId));
            if (!vectors.contains(vec.get(0))){
                vectors.add(vec.get(0));
            }
            if (!vectors.contains(vec.get(1))){
                vectors.add(vec.get(1));
            }
        }
        return vectors;
    }



    public void clear() {
        sectors = new HashMap<>();
        vectors = new HashMap<>();
        walls = new HashMap<>();
    }
}
