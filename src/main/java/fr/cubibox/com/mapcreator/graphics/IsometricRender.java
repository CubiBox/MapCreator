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
        float heightOffset = (float) ((getDIML()*0.25)-(height*getDIML()/32)*0.25);
        y *= getDIML()/xSize;
        x *= getDIML()/xSize;

        float relativeX = (float) (origin.getX()-x);
        float relativeY = (float) (origin.getY()-y);
        float squareOriginDistance = Math.abs(relativeX*relativeX + relativeY*relativeY);

        return new float[] {
                (float) (getDIML()/2 + (x*0.25 - y*0.25)),
                (float) (heightOffset + (y*this.yAngle + x*this.yAngle))
        };
    }

    public float getxAngle() {
        return xAngle;
    }
    public void setxAngle(float xAngle) {
        this.xAngle = xAngle*0.01f;
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
