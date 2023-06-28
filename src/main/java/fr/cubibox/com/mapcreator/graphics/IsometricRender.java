package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.graphics.Texture.*;


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

    /**
     * Is to convert just one point
     * @param x
     * @param y
     * @return
     */
    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

    /**
     * Is to convert just one point
     * @param x
     * @param y
     * @param height
     * @return
     */
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

    public ArrayList<Shape> getTextureIso(Texture texture) {
        ArrayList<Shape> shapes = new ArrayList<>();
        double width = texture.getWidth();
        double height = texture.getHeight();
        int face = texture.getFace();

        switch (face){
            case NORTH -> { if (xAngle >= angle1 && xAngle <= angle3) return shapes; }
            case SOUTH -> { if (xAngle >= angle3 || xAngle <= angle1) return shapes; }
            case WEST -> { if (xAngle >= angle4 || xAngle <= angle2) return shapes; }
            case EAST -> { if (xAngle >= angle2 && xAngle <= angle4) return shapes; }
        };

        double heightOffset = Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * 0.75;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = texture.getTexture().getRGB(x, y);
                double red = (double)((rgb >> 16) & 0xFF) / 255;
                double green = (double)((rgb >> 8) & 0xFF) / 255;
                double blue = (double)(rgb & 0xFF) / 255;
                double[] coos = switch (face){
                    case TOP -> texture.toScreenIso(x, y, height, xAngle, yAngle, heightOffset);
                    case BOTTOM -> texture.toScreenIso(x, y, 0, xAngle, yAngle, heightOffset);
                    case NORTH -> texture.toScreenIso(y, 0, x, xAngle, yAngle, heightOffset);
                    case SOUTH -> texture.toScreenIso(y, height, x, xAngle, yAngle, heightOffset);
                    case EAST -> texture.toScreenIso(0, x, y, xAngle, yAngle, heightOffset);
                    case WEST -> texture.toScreenIso(height, x, y, xAngle, yAngle, heightOffset);
                    default -> throw new IllegalStateException("Unexpected value: " + face);
                };
                Rectangle rect = new Rectangle(coos[0], coos[1], 1d, 1d);
                rect.setFill(new javafx.scene.paint.Color(
                        red, green, blue, 1
                ));
                shapes.add(rect);
            }
        }
        return shapes;
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
