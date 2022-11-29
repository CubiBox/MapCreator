package fr.cubibox.com.mapcreator.map;

public enum Type {
    WALL, FLOOR, CELLING;

    public static Type toType(String str_type){
        str_type = str_type.toUpperCase();
        return switch (str_type) {
            case "FLOOR" -> FLOOR;
            case "CELLING" -> CELLING;
            default -> WALL;
        };
    }
    public static String toString(Type t) {
        return switch (t) {
            case FLOOR -> "Floor";
            case CELLING -> "Celling";
            default -> "Wall";
        };
    }
}
