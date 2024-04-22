package fr.cubibox.com.mapcreator.maths;

public class Matrix3d {
    private Vector3d vec1;
    private Vector3d vec2;
    private Vector3d vec3;

    public static Matrix3d cano = new Matrix3d(
            new Vector3d(1, 0, 0),
            new Vector3d(0, 1, 0),
            new Vector3d(0, 0, 1)
    );


    public Matrix3d(Vector3d vec1, Vector3d vec2, Vector3d vec3) {
        setMatrix(vec1, vec2, vec3);
    }

    public void setMatrix(Matrix3d m) {
        setMatrix(m.vec1, m.vec2, m.vec3);
    }
    public void setMatrix(Vector3d vec1, Vector3d vec2, Vector3d vec3) {
        this.vec1 = vec1;
        this.vec2 = vec2;
        this.vec3 = vec3;
    }

    public void setMatrixFromAngles(double xAngle, double yAngle){
        Vector3d vec1 = setIso(xAngle, yAngle, 1, 0, 0);
        Vector3d vec2 = setIso(xAngle, yAngle, 0, 1, 0);
        Vector3d vec3 = setIso(xAngle, yAngle, 0, 0, 1);

        setMatrix(vec1, vec2, vec3);
    }

    public Vector3d setIso(double xAngle, double yAngle, double relativeX, double relativeY, double height){
        double newAngle = xAngle + ((relativeX == 0 && relativeY == 0) ? 0 : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
        double finalX = originDistance * Math.cos(newAngle) * (relativeX < 0 ? -1 : 1) / 2;
        double finalY = originDistance * Math.sin(newAngle) * (relativeX < 0 ? -1 : 1) / 2;

        return new Vector3d(
                finalX - finalY,
                (finalY + finalX) * yAngle,
                Math.sin(Math.PI / 2 - (yAngle * (Math.PI / 2))) * height
        );
    }


    public void mul(double val){
        vec1.mul(val);
        vec2.mul(val);
        vec3.mul(val);
    }
    public void mul(Matrix3d mat){
        vec1.mul(mat.vec1);
        vec2.mul(mat.vec2);
        vec3.mul(mat.vec2);
    }


    public Vector3d getVec1() {
        return vec1;
    }

    public void setVec1(Vector3d vec1) {
        this.vec1 = vec1;
    }

    public Vector3d getVec2() {
        return vec2;
    }

    public void setVec2(Vector3d vec2) {
        this.vec2 = vec2;
    }

    public Vector3d getVec3() {
        return vec3;
    }

    public void setVec3(Vector3d vec3) {
        this.vec3 = vec3;
    }
}
