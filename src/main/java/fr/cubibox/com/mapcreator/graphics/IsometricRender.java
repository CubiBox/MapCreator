package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.maths.Cube2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.graphics.Texture.*;
import static fr.cubibox.com.mapcreator.mapObject.Type.CUBE;


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

    public void drawIso(StaticObject obj, GraphicsContext gc) {
        if (obj.getType() == CUBE){
            Cube2F cube = (Cube2F) obj.getPolygon();
            int x = (int) obj.getOrigin().getX() * 64;
            int y = (int) obj.getOrigin().getY() * 64;
            drawTextureIso(cube.getTextureTop(), gc, x, y, 0);
            drawTextureIso(cube.getTextureEast(), gc, x, y, 0);
            drawTextureIso(cube.getTextureWest(), gc, x, y, 0);
            drawTextureIso(cube.getTextureNorth(), gc, x, y, 0);
            drawTextureIso(cube.getTextureSouth(), gc, x, y, 0);
        }
        else {
            System.out.println("WIP : " + obj);
        }
    }

    public void drawTextureIso(Texture texture, GraphicsContext gc, int x, int y, int z) {
        double width = texture.getWidth();
        double height = texture.getHeight();
        int face = texture.getFace();

        WritableImage wr = new WritableImage(texture.getTexture().getWidth()*2, texture.getTexture().getHeight()*2);
        PixelWriter pw = wr.getPixelWriter();

        switch (face){
            case NORTH -> { if (xAngle >= angle1 && xAngle <= angle3) return; }
            case SOUTH -> { if (xAngle >= angle3 || xAngle <= angle1) return; }
            case WEST -> { if (xAngle >= angle4 || xAngle <= angle2) return; }
            case EAST -> { if (xAngle >= angle2 && xAngle <= angle4) return; }
        };

        double heightOffset = Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * 0.75;
        for (int relativeY = 0; relativeY < height; relativeY++) {
            for (int relativeX = 0; relativeX < width; relativeX++) {
                int rgba = texture.getTexture().getRGB(relativeX, relativeY);
                double[] coos = switch (face){
                    case TOP -> texture.toScreenIso(relativeX + x, relativeY + y, height + z, xAngle, yAngle, heightOffset);
                    case BOTTOM -> texture.toScreenIso(relativeX + x, relativeY + y, 0, xAngle, yAngle, heightOffset);
                    case NORTH -> texture.toScreenIso(relativeY + y, 0, relativeX + x, xAngle, yAngle, heightOffset);
                    case SOUTH -> texture.toScreenIso(relativeY + y, height, relativeX + x, xAngle, yAngle, heightOffset);
                    case EAST -> texture.toScreenIso(0, relativeX + x, relativeY + y, xAngle, yAngle, heightOffset);
                    case WEST -> texture.toScreenIso(height + z, relativeX + x, relativeY + y, xAngle, yAngle, heightOffset);
                    default -> throw new IllegalStateException("Unexpected value: " + face);
                };
                pw.setArgb((int) (coos[0]), (int) (coos[1]) , rgba);
            }
        }
        Image image = new ImageView(wr).getImage();
        if (image != null)
            gc.drawImage(new ImageView(wr).getImage(), 0, 0);
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
