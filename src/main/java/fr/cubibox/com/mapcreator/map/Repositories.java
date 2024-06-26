package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.graphics.ui.TreeViewController;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Repositories {
    private HashMap<Integer, Sector> sectors;
    private HashMap<Integer, Vector2v> vectors;
    private HashMap<Integer, Wall> walls;


    private ArrayList<Type> drawablePol;
    public ArrayList<Vector2v> tpmPoints;
    public TreeViewController treeViewController;

    private static Repositories instance;

    private Repositories() {
        treeViewController = new TreeViewController();
        sectors = new HashMap<>();
        vectors = new HashMap<>();
        walls = new HashMap<>();
        tpmPoints = new ArrayList<>();
    }

    public static Repositories getInstance(){
        if (instance == null){
            instance = new Repositories();
        }
        return instance;
    }


    public void add(int id, Sector sector){
        sectors.put(id, sector);
    }
    public void add(int id, Wall wall){
        walls.put(id, wall);
    }
    public void add(int id, Vector2v vector){
        vectors.put(id, vector);
    }

    public void remove(int id, Sector sector){
        ArrayList<Integer> wallIDs = new ArrayList<>(sector.getWallIds());
        for (int wallID : wallIDs) {
            remove(wallID, getWallByID(wallID));
            sector.getWallIds().remove(wallID);
        }
        sectors.remove(id, sector);
    }
    public void remove(int id, Wall curr_wall){
        Wall[] connectedWall = getWallsByVectorID(curr_wall.getVector1ID());

        if (connectedWall[0] != null && connectedWall[1] != null) {
            int wall = (connectedWall[0] != curr_wall) ? 0 : 1;
            int vecId =
                    (connectedWall[wall].getVector1ID() == curr_wall.getVector1ID() || connectedWall[wall].getVector2ID() == curr_wall.getVector1ID())
                            ? curr_wall.getVector2ID() : curr_wall.getVector1ID();

            if (vecId == connectedWall[wall].getVector1ID()) {
                connectedWall[wall].setVector1ID(vecId);
            } else connectedWall[wall].setVector2ID(vecId);
        }

        for (Sector sec : getAllSectors()) {
            sec.getWallIds().remove(id);
        }
        walls.remove(id, curr_wall);
    }

    public void remove(int id, Vector2v vector){
        Wall[] walls = getWallsByVectorID(vector.getId());
        int vec1ID = (walls[0].getVector1ID() == vector.getId()) ? walls[0].getVector2ID() : walls[0].getVector1ID();
        int vec2ID = (walls[1].getVector1ID() == vector.getId()) ? walls[1].getVector2ID() : walls[1].getVector1ID();

        Sector sec = getSectorByWallID(walls[0].getId());
        for (Wall wall : walls) {
            remove(wall);
            //sec.getWallIds().remove(wall.getId());
        }
        Wall newWall = new Wall(vec1ID, vec2ID);
        newWall.getTreeItem().getChildren().add(getVectorByID(vec1ID).getTreeItem());
        newWall.getTreeItem().getChildren().add(getVectorByID(vec2ID).getTreeItem());

        add(newWall.getId(), newWall);
        sec.addWallId(newWall.getId());
        sec.getTreeItem().getChildren().get(0).getChildren().add(newWall.getTreeItem());

        vectors.remove(id, vector);
    }

    public void subdivideWall(Wall wall) {
        int vec1ID = wall.getVector1ID();
        int vec2ID = wall.getVector2ID();

        //create new vector
        Vector2v newVec = new Vector2v(
                ((getVectorByID(vec1ID).getX() + getVectorByID(vec2ID).getX())/2),
                ((getVectorByID(vec1ID).getY() + getVectorByID(vec2ID).getY())/2)
        );
        add(newVec.getId(), newVec);
        wall.setVector2ID(newVec.getId());

        //create new wall
        Wall newWall = new Wall(newVec.getId(), vec2ID);
        newWall.getTreeItem().getChildren().add(newVec.getTreeItem());
        newWall.getTreeItem().getChildren().add(getVectorByID(vec2ID).getTreeItem());

        add(newWall.getId(), newWall);

        //add to sector
        getSectorByWallID(wall.getId()).addWallId(newWall.getId());
        getSectorByWallID(wall.getId()).getTreeItem().getChildren().get(0).getChildren().add(newWall.getTreeItem());
        getSectorByWallID(wall.getId()).getTreeItem().getChildren().get(1).getChildren().add(newVec.getTreeItem());

        System.out.println("-----wrong-----");
        System.out.println(newVec.getId());
        System.out.println(vec2ID);
        System.out.println(newWall.getId());
        System.out.println("-----good-----");
        System.out.println(vec1ID);
        System.out.println("------------");
    }


    public void remove(Sector sector){
        remove(sector.id, sector);
    }
    public void remove(Wall wall){
        remove(wall.getId(), wall);
    }
    public void remove(Vector2v vector){
        remove(vector.getId(), vector);
    }

    public boolean contains(Sector sector){
        return (sectors.containsValue(sector));
    }
    public boolean contains(Vector2v vector){
        return (vectors.containsValue(vector));
    }
    public boolean contains(Wall wall){
        return (walls.containsValue(wall));
    }


    public Sector getSectorByID(int id){
        return sectors.get(id);
    }
    public Vector2v getVectorByID(int id){
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
    public ArrayList<Vector2v> getAllVectors(){
        return new ArrayList<>(this.vectors.values());
    }


    public ArrayList<Wall> getWalls(Sector sector){
        ArrayList<Wall> walls = new ArrayList<>();
        for (int id : sector.getWallIds()){
            walls.add(getWallByID(id));
        }
        return walls;
    }
    public ArrayList<Vector2v> getVectors(Wall wall){
        ArrayList<Vector2v> vectors = new ArrayList<>();
        vectors.add(getVectorByID(wall.getVector1ID()));
        vectors.add(getVectorByID(wall.getVector2ID()));
        return vectors;
    }

    public ArrayList<Vector2v> getVectors(Sector sector){
        ArrayList<Vector2v> vectors = new ArrayList<>();
        for (int wallId : sector.getWallIds()){
            ArrayList<Vector2v> vec = getVectors(walls.get(wallId));
            if (!vectors.contains(vec.get(0))){
                vectors.add(vec.get(0));
            }
            if (!vectors.contains(vec.get(1))){
                vectors.add(vec.get(1));
            }
        }
        return vectors;
    }

    public Sector getSectorByWallID(int wallID){
        for (Sector sec : this.sectors.values()){
            if (sec.getWallIds().contains(wallID)){
                return sec;
            }
        }
        return null;
    }

    public Wall[] getWallsByVectorID(int secID){
        Wall[] walls = new Wall[2];
        for (Wall wall : this.walls.values()){
            if (wall.getVector1ID() == secID || wall.getVector2ID() == secID){
                if (walls[0] == null) walls[0] = wall;
                else walls[1] = wall;
            }
        }
        return walls;
    }

    public void clear() {
        sectors = new HashMap<>();
        vectors = new HashMap<>();
        walls = new HashMap<>();
    }

    public TreeViewController getTreeViewController() {
        return treeViewController;
    }
}
