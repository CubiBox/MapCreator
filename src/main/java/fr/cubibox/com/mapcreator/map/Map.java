package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.maths.Sector;

import java.util.Arrays;
import java.util.HashSet;

public class Map {
    private final HashSet<Sector> sectors;
    private final HashSet<Wall> walls;

    public Map() {
        sectors = new HashSet<>();
        walls = new HashSet<>();
    }

    public HashSet<Sector> getSectors() {
        return sectors;
    }

    public Sector getSector(int sectorId) {
        for (Sector sector : sectors) {
            if (sector.id == sectorId) {
                return sector;
            }
        }

        return null;
    }

    public HashSet<Wall> getWalls() {
        return walls;
    }

    public Wall getWall(int wallId) {
        for (Wall wall : walls) {
            if (wall.getId() == wallId) {
                return wall;
            }
        }

        return null;
    }

    public Sector getWallSector(int wallId) {
        for (Sector sector : sectors) {
            if (sector.getWallIds().contains(wallId)) {
                return sector;
            }
        }

        return null;
    }

    public void addSectors(Sector... sectors) {
        this.sectors.addAll(Arrays.asList(sectors));
    }

    public void addSector(Sector sector) {
        this.sectors.add(sector);
    }

    public void removeSector(Sector sector) {
        this.sectors.remove(sector);
    }

    public void removeSector(int id) {
        sectors.removeIf(sector -> sector.id == id);
    }

    public void addWalls(Wall... walls) {
        this.walls.addAll(Arrays.asList(walls));
    }

    public void addWall(Wall wall) {
        this.walls.add(wall);
    }

    public void removeWall(Wall wall) {
        this.walls.remove(wall);
    }

    public void removeWall(int id) {
        walls.removeIf(wall -> wall.getId() == id);
    }
}
