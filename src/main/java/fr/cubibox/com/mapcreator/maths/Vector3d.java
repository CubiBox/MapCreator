package fr.cubibox.com.mapcreator.maths;

public class Vector3d {
    double x;
    double y;
    double z;

    public Vector3d(double x, double y, double z) {
        setVector(x, y, z);
    }
    public Vector3d(Vector3d vec) {
        setVector(vec);
    }

    public void setVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setVector(Vector3d vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    public void mul(double val) {
        x = x * val;
        y = y * val;
        z = z * val;
    }
    public void mul(Vector3d vec) {
        if (vec != null) {
            x = x * vec.x;
            y = y * vec.y;
            z = z * vec.z;
        }
    }

    public void mul(Matrix3d mat) {
        if (mat != null) {
            double x = mat.getVec1().x * this.x + mat.getVec2().x * this.y + mat.getVec3().x * this.z;
            double y = mat.getVec1().y * this.x + mat.getVec2().y * this.y + mat.getVec3().y * this.z;
            double z = mat.getVec1().z * this.x + mat.getVec2().z * this.y + mat.getVec3().z * this.z;

            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void div(double val) {
        if (val != 0) {
            x = x / val;
            y = y / val;
            z = z / val;
        }
    }
    public void div(Vector3d vec) {
        if (vec != null) {
            x = x / vec.x;
            y = y / vec.y;
            z = z / vec.z;
        }
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector2v{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
