package fr.cubibox.com.mapcreator.map.repositories;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Wall;
import javafx.scene.control.TreeItem;

import java.util.HashMap;

public class WallRepository {
    private HashMap<Integer, Wall> walls;
    public final int ID = 0;

    public WallRepository(HashMap<Integer, Wall> walls) {
        this.walls = walls;
    }
    public WallRepository() {
        this.walls = new HashMap<>();
    }

    public void add(int id, Wall wall){
        walls.put(id, wall);
    }

    public Wall getByID(int id){
        return walls.get(id);
    }

    public Wall getByTreeItem(TreeItem<String> treeItem){
        return walls.get(treeItem.hashCode());
    }
}
