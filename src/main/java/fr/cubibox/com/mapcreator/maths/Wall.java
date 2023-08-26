package fr.cubibox.com.mapcreator.maths;

public class Wall {
    private String id;
    public static int staticId;

    private Vector a;
    private Vector b;

    public Wall(Vector a, Vector b) {
        this.a = a;
        this.b = b;
        this.id = newId();
    }

    public Vector getA() {
        return a;
    }
    public void setA(Vector a) {
        this.a = a;
    }

    public Vector getB() {
        return b;
    }
    public void setB(Vector b) {
        this.b = b;
    }

    public Vector getNormal() {
        return new Vector(b.getY() - a.getY(), a.getX() - b.getX());
    }

    public float getLength() {
        Vector length = new Vector(
                this.getB().getX() - this.getA().getX(),
                this.getB().getY() - this.getA().getY()
        );

        return (float) (Math.sqrt(length.dot(length)));
    }

    public float getSqLength() {
        Vector length = new Vector(
                this.getB().getX() - this.getA().getX(),
                this.getB().getY() - this.getA().getY()
        );

        return length.dot(length);
    }

    public boolean isPointOnLine(Vector intersectionVector) {
        float x1 = this.getA().getX();
        float y1 = this.getA().getY();
        float x2 = this.getB().getX();
        float y2 = this.getB().getY();
        float x3 = intersectionVector.getX();
        float y3 = intersectionVector.getY();

        float d = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);

        return (d == 0);
    }

    public static String newId(){
        staticId++;
        return "Polygon"+staticId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return
                "\t\t@[" + (int) a.getX() +
                ";" +
                (int) a.getY() +
                "]-[" +
                (int) b.getX() +
                ";" +
                (int) b.getY() +
                "], id='" +
                id + '\'';
    }
}
