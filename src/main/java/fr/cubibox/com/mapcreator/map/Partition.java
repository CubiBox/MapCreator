package fr.cubibox.com.mapcreator.map;

public class Partition {
    private static int globalID = 0;

    private static int applyGlobalID(){
        return globalID++;
    }
}
