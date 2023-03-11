package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.maths.Vector2F;

import static fr.cubibox.com.mapcreator.Main.*;


public class IsometricRender {
    private float xAngle;
    private float yAngle;
    private Vector2F origin;


    public IsometricRender(Vector2F origin) {
        this.xAngle = 0.25f;
        this.yAngle = 0f;
        this.origin = origin;
    }
    public IsometricRender(float origin) {
        this.xAngle = 0.25f;
        this.yAngle = 0f;
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
        x *= getDIML()/xSize;
        y *= getDIML()/xSize;
        float heightOffset = (float) ((getDIML()*0.25)-(height*getDIML()/32)*0.25);
        return new float[] {
                (float) (getDIML()/2 + (x*(0.5-this.xAngle) - y*(0.5+ this.xAngle))),
                (float) (heightOffset + (y* this.yAngle + x*this.yAngle))
        };
    }

    public float getxAngle() {
        return xAngle;
    }
    public void setxAngle(float xAngle) {
        this.xAngle = 0.25f + xAngle*0.01f;
    }

    public float getyAngle() {
        return yAngle;
    }
    public void setyAngle(float yAngle) {
        this.yAngle = yAngle*0.01f;
    }

    public Vector2F getOrigin() {
        return origin;
    }
    public void setOrigin(Vector2F origin) {
        this.origin = origin;
    }
}
