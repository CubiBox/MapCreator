package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Vector2F;

import static fr.cubibox.com.mapcreator.Main.*;


public class IsometricRender {
    private float xAngle;
    private float yAngle;
    private Vector2F origin;


    public IsometricRender(Vector2F origin) {
        this.xAngle = 0f;
        this.yAngle = 0.5f;
        this.origin = origin;
    }
    public IsometricRender(float origin) {
        this.xAngle = 0f;
        this.yAngle = 0.5f;
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
        double heightOffset = getDIML()*0.65 - (Math.sin(Math.PI/2-(yAngle*(Math.PI/2)))*height * getDIML()/64);

        double relativeX = origin.getX() - x;
        double relativeY = origin.getY() - y;
        double vectorAngle = xAngle + ((relativeX==0 && relativeY==0) ? 0 : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
        x = originDistance * Math.cos(vectorAngle) * getDIML() / xSize * (relativeX < 0 ? -1 : 1) /2;
        y = originDistance * Math.sin(vectorAngle) * getDIML() / xSize * (relativeX < 0 ? -1 : 1) /2;

        return new float[] {
                (float) (getDIML()/2 + (x - y)),
                (float) (heightOffset + (y*yAngle + x*yAngle))
        };
    }

    public float getxAngle() {
        return xAngle;
    }
    public void setxAngleBySlider(float xAngle) {
        this.xAngle = xAngle*0.00045f;
    }
    public void setxAngle(float xAngle) {
        this.xAngle = xAngle;
    }



    public float getyAngle() {
        return yAngle;
    }
    public void setyAngleBySlider(float yAngle) {
        this.yAngle = 0.25f + yAngle*0.01f;
    }
    public void setyAngle(float yAngle) {
        this.yAngle = yAngle;
    }

    public Vector2F getOrigin() {
        return origin;
    }
    public void setOrigin(Vector2F origin) {
        this.origin = origin;
    }
}
