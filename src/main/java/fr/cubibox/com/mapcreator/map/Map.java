package fr.cubibox.com.mapcreator.map;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.Vector;

public class Map {
    String mapVersion;
    String name;
    int size;

    ArrayList<Vector2v> vectors;
    ArrayList<Wall> walls;
    ArrayList<Sector> sectors;
    ArrayList<Partition> partitions;

    public Map(){
        mapVersion = "0.1";
        name = "test";
        size = 16;

        vectors = Repositories.getInstance().getAllVectors();
        walls = Repositories.getInstance().getAllWalls();
        sectors = Repositories.getInstance().getAllSectors();

        System.out.println(writer());
//        vectors = Repositories.getInstance().getAllVectors();
    }

    public String writer(){
        StringJoiner str = new StringJoiner("\n");
        str.add("map_version: " + mapVersion);
        str.add("name: " + name);
        str.add("size: " + size);

        str.add("vectors:");
        for (Vector2v vec : vectors){
            str.add("\t" + vec.getId() + ": " + vec.getX() + ' ' + vec.getY());
        }

        str.add("");

        str.add("walls:");
        for (Wall wall : walls){
            str.add("\t" + wall.getId() + ": " + wall.getVector1ID() + ' ' + wall.getVector2ID());
        }

        str.add("");

        str.add("sectors:");
        for (Sector sector : sectors){
            str.add("\t" + sector.getId() + ":");
            str.add("\t\tceil: " + sector.getCeilHeight());
            str.add("\t\tfloor: " + sector.getFloorHeight());
            str.add("\t\twallsID:");
            for (int wallId : sector.getWallIds()) {
                str.add("\t\t\t" + wallId);
            }
        }

        return str.toString();
    }

    public static Map parser(String file){
        return null;
    }
}
