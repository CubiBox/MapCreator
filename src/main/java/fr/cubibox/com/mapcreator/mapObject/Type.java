package fr.cubibox.com.mapcreator.mapObject;

public enum Type {
    WALL, FLOOR, CELLING, CUBE;

    public static Type toType(String str_type){
        str_type = str_type.toUpperCase();
        return switch (str_type) {
            case "FLOOR" -> FLOOR;
            case "CELLING" -> CELLING;
            case "CUBE" -> CUBE;
            default -> WALL;
        };
    }
    public static String toString(Type t) {
        return switch (t) {
            case FLOOR -> "Floor";
            case CELLING -> "Celling";
            case CUBE -> "Cube";
            default -> "Wall";
        };
    }
}
