package fr.cubibox.com.mapcreator.map.repositories;

import fr.cubibox.com.mapcreator.maths.Vector;
import javafx.scene.control.TreeItem;

import java.util.HashMap;

public class VectorRepository {
    private HashMap<Integer, Vector> vectors;
    public final int ID = 0;

    public VectorRepository(HashMap<Integer, Vector> vectors) {
        this.vectors = vectors;
    }
    public VectorRepository() {
        this.vectors = new HashMap<>();
    }

    public void add(int id, Vector vector){
        vectors.put(id, vector);
    }

    public Vector getByID(int id){
        return vectors.get(id);
    }

    public Vector getByTreeItem(TreeItem<String> treeItem){
        return vectors.get(treeItem.hashCode());
    }
}
