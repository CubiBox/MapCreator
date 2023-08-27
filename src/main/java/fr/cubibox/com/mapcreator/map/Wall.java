package fr.cubibox.com.mapcreator.map;

public class Wall {
    public final int id;
    public final double xA, xB, yA, yB;

    public Wall(int id, double xA, double yA, double xB, double yB) {
        this.id = id;
        this.xA = xA;
        this.yA = yA;
        this.xB = xB;
        this.yB = yB;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "id=" + id +
                ", xA=" + xA +
                ", xB=" + xB +
                ", yA=" + yA +
                ", yB=" + yB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wall wall)) return false;

        return id == wall.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
