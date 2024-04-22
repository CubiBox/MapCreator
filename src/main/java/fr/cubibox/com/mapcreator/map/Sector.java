package fr.cubibox.com.mapcreator.map;

import java.util.Arrays;
import java.util.HashSet;

public class Sector {
    public int id;

    protected final HashSet<Integer> wallIds;

    protected double ceilHeight;
    protected double floorHeight;


    public Sector(int id, double ceilHeight, double floorHeight) {
        this.id = id;
        this.ceilHeight = ceilHeight;
        this.floorHeight = floorHeight;
        this.wallIds = new HashSet<>();
    }

    public double getCeilHeight() {
        return ceilHeight;
    }

    public void setCeilHeight(double ceilHeight) {
        this.ceilHeight = ceilHeight;
    }

    public double getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(double floorHeight) {
        this.floorHeight = floorHeight;
    }

    public void addWalls(Wall... walls) {
        for (Wall wall : walls) {
            this.wallIds.add(wall.getId());
        }
    }

    public void addWallId(Integer wallId) {
        this.wallIds.add(wallId);
    }

    public void addWallIds(Integer... wallIds) {
        this.wallIds.addAll(Arrays.asList(wallIds));
    }

    public void removeWallId(Integer wallId) {
        this.wallIds.remove(wallId);
    }

    public void removeWallId(int id) {
        wallIds.removeIf(wallId -> wallId == id);
    }

    public HashSet<Integer> getWallIds() {
        return wallIds;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("sector_" + id + ":\n");

        str.append("\twallids: ");
        for (int id : wallIds)
            str.append(id).append(' ');

        str.append("\tceilHeight: " + ceilHeight + '\n');
        str.append("\tfloorHeight: " + floorHeight + '\n');

        return str.toString();
    }
}
