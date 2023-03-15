package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.maths.Vector2F;

import static fr.cubibox.com.mapcreator.Main.*;


public class IsometricRender {
    private double xAngle;
    private double yAngle;
    private Vector2F origin;


    public IsometricRender(Vector2F origin) {
        this.xAngle = 0;
        this.yAngle = 0.5;
        this.origin = origin;
    }
    public IsometricRender(float origin) {
        this.xAngle = 0;
        this.yAngle = 0.5;
        this.origin = new Vector2F(origin,origin);
    }
    public IsometricRender(double xAngle, double yAngle, Vector2F origin) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.origin = origin;
    }

    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

    public float[] toScreenIso(double x, double y, double height){
        double relativeX = origin.getX() - x;
        double relativeY = origin.getY() - y;
        double newAngle = xAngle + ((relativeX==0 && relativeY==0) ? 0 : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
        double finalX = originDistance * Math.cos(newAngle) * getDIML()/xSize * (relativeX < 0 ? -1 : 1) /2;
        double finalY = originDistance * Math.sin(newAngle) * getDIML()/xSize * (relativeX < 0 ? -1 : 1) /2;

        double heightOffset = getDIML()*0.65 - (Math.sin(Math.PI/2-(yAngle*(Math.PI/2)))*height * getDIML()/64);
        return new float[] {
                (float) (getDIML()/2 + (finalX - finalY)),
                (float) (heightOffset + (finalY*yAngle + finalX*yAngle))
        };
    }

    public double getXAngle() {
        return xAngle;
    }
    public void setXAngle(double xAngle) {
        this.xAngle = xAngle;
    }



    public double getYAngle() {
        return yAngle;
    }
    public void setYAngle(double yAngle) {
        this.yAngle = yAngle;
    }

    public Vector2F getOrigin() {
        return origin;
    }
    public void setOrigin(Vector2F origin) {
        this.origin = origin;
    }
}
