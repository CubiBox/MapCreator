package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.maths.Vector2F;

import static fr.cubibox.com.mapcreator.Main.*;


public class IsometricRender {
    private float xAngle;
    private float yAngle;
    private Vector2F origin;


    public IsometricRender(Vector2F origin) {
        this.xAngle = 0f;
        this.yAngle = 0.25f;
        this.origin = origin;
    }
    public IsometricRender(float origin) {
        this.xAngle = 0f;
        this.yAngle = 0.25f;
        this.origin = new Vector2F(origin,origin);
    }
    public IsometricRender(float xAngle, float yAngle, Vector2F origin) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.origin = origin;
    }

    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }
    public float[] toScreenIso(double x, double y, double height){
        float heightOffset = (float) (getDIML()*0.6-(height*getDIML()/32)*0.5);

        if (!(x==origin.getX() && y==origin.getY())) {
            double relativeX = origin.getX() - x;
            double relativeY = origin.getY() - y;
            double vectorAngle = xAngle + Math.atan(relativeY / relativeX);
            double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
            x = originDistance * Math.cos(vectorAngle) * getDIML() / xSize * (relativeX < 0 ? -1 : 1);
            y = originDistance * Math.sin(vectorAngle) * getDIML() / xSize * (relativeX < 0 ? -1 : 1);
        }
        return new float[] {
                (float) (getDIML()/2 + (x*0.5 - y*0.5)),
                (float) (heightOffset + (y*this.yAngle + x*this.yAngle))
        };
    }

    public float getxAngle() {
        return xAngle;
    }
    public void setxAngle(float xAngle) {
        this.xAngle = xAngle*0.00045f;
    }

    public float getyAngle() {
        return yAngle;
    }
    public void setyAngle(float yAngle) {
        this.yAngle = 0.25f + yAngle*0.01f;
    }

    public Vector2F getOrigin() {
        return origin;
    }
    public void setOrigin(Vector2F origin) {
        this.origin = origin;
    }
}
