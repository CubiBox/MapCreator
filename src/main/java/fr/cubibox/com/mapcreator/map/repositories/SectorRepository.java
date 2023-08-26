package fr.cubibox.com.mapcreator.map.repositories;

import fr.cubibox.com.mapcreator.maths.Sector;
import javafx.scene.control.TreeItem;

import java.util.HashMap;

public class SectorRepository {
    private HashMap<Integer, Sector> sectors;
    public final int ID = 0;

    public SectorRepository(HashMap<Integer, Sector> sectors) {
        this.sectors = sectors;
    }
    public SectorRepository() {
        this.sectors = new HashMap<>();
    }

    public void add(int id, Sector sector){
        sectors.put(id, sector);
    }

    public Sector getByID(int id){
        return sectors.get(id);
    }

    public Sector getByTreeItem(TreeItem<String> treeItem){
        return sectors.get(treeItem.hashCode());
    }
}
