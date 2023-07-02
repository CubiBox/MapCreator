package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static fr.cubibox.com.mapcreator.Main.isometricRender;

public class Texture {
    public static final int BOTTOM = 5;
    public static final int WEST = 4;
    public static final int EAST = 3;
    public static final int SOUTH = 2;
    public static final int NORTH = 1;
    public static final int TOP = 0;

    public static final double angle1 = Math.PI/4;
    public static final double angle2 = Math.PI/4*3;
    public static final double angle3 = Math.PI/4*5;
    public static final double angle4 = Math.PI/4*7;


    private final int width;
    private final int height;
    private final int face;
    private BufferedImage texture;

    public Texture(String path) {
        this(path, 0);
    }

    public Texture(String path, int face) {
        System.out.println("textures/" + path);
        path = Main.class.getResource("textures/" + path).getPath();
        try {
            this.texture = upscale(ImageIO.read(new File(path)), 8d, 8d);
        }
        catch (IOException ignored) { }
        this.height = this.texture.getHeight();
        this.width = this.texture.getWidth();
        this.face = face;
    }

    public Texture(String path, int width, int height, int face) {
        try {
            this.texture = upscale(ImageIO.read(new File(path)), width, height);
        }
        catch (IOException ignored) { }

        this.width = width;
        this.height = height;
        this.face = face;
    }

    public BufferedImage convertImage(double xAngle, double yAngle){
        BufferedImage texture = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = texture.createGraphics();

        switch (face){
            case NORTH -> { if (xAngle >= angle1 && xAngle <= angle3) return null; }
            case SOUTH -> { if (xAngle >= angle3 || xAngle <= angle1) return null; }
            case WEST -> { if (xAngle >= angle4 || xAngle <= angle2) return null; }
            case EAST -> { if (xAngle >= angle2 && xAngle <= angle4) return null; }
        };

        double heightOffset = Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * 0.75;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = this.texture.getRGB(x, y);
                double[] coos = switch (face){
                    case TOP -> toScreenIso(x, y, height, xAngle, yAngle, heightOffset);
                    case BOTTOM -> toScreenIso(x, y, 0, xAngle, yAngle, heightOffset);
                    case NORTH -> toScreenIso(y, 0, x, xAngle, yAngle, heightOffset);
                    case SOUTH -> toScreenIso(y, height, x, xAngle, yAngle, heightOffset);
                    case EAST -> toScreenIso(0, x, y, xAngle, yAngle, heightOffset);
                    case WEST -> toScreenIso(height, x, y, xAngle, yAngle, heightOffset);
                    default -> throw new IllegalStateException("Unexpected value: " + face);
                };

                g2.setColor(new Color(rgba));
                g2.fillRect((int) (coos[0]), (int) (coos[1]),1,1);
            }
        }
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, texture.getWidth(), texture.getHeight());
        g2.dispose();

        return texture;
    }


    public double[] toScreenIso(double x, double y, double z, double xAngle, double yAngle, double heightOffset){
        double relativeX = (width / 2d) - x;
        double relativeY = (height / 2d) - y;

        double newAngle = xAngle + ((relativeX == 0d && relativeY == 0d) ? 0d : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);

        double finalX = originDistance * Math.cos(newAngle) * (relativeX < 0d ? -1d : 1d) / 2d;
        double finalY = originDistance * Math.sin(newAngle) * (relativeX < 0d ? -1d : 1d) / 2d;

        //double heightOffset = height - (Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * z * 0.8);

        return new double[] {
                height/2d + (finalX - finalY),
                height - heightOffset*z + (yAngle * (finalY + finalX))
        };
    }

    public BufferedImage upscale(BufferedImage image, double width_scale, double height_scale) {
        int height = (int) (image.getWidth()*width_scale);
        int width = (int) (image.getHeight()*height_scale);
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = output.createGraphics();

        for (int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++) {
                g2.setColor(new Color(image.getRGB(x, y)));
                g2.fillRect(
                        (int) (x * width_scale),
                        (int) (y * height_scale),
                        (int) (1 * width_scale),
                        (int) (1 * height_scale)
                );
            }
        }
        g2.dispose();
        return output;
    }

    public BufferedImage upscale(BufferedImage image, double scale) {
        int height = (int) (image.getWidth()*scale);
        int width = (int) (image.getHeight()*scale);
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = output.createGraphics();

        for (int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++) {
                g2.setColor(new Color(image.getRGB(x, y)));
                g2.fillRect(
                        (int) (x * scale),
                        (int) (y * scale),
                        (int) (1 * scale),
                        (int) (1 * scale)
                );
            }
        }
        g2.dispose();
        return output;
    }

/*
    public void trash(){
        ArrayList<Vector2F> points = obj.getPolygon().getPoints();
        float currentHeight = obj.getPolygon().getHeight();
        float currentBase = 0;

        switch (obj.getType()) {
            case FLOOR -> {
                gc.setStroke(javafx.scene.paint.Color.rgb(0, 255, 0, 0.3));
            }
            case CELLING -> {
                gc.setStroke(javafx.scene.paint.Color.rgb(255, 0, 0, 0.3));
                currentHeight = 31;
            }
            default -> { //  WALL or null
                gc.setStroke(javafx.scene.paint.Color.rgb(0, 255, 255, 0.3));
            }
        }

        //top
        double[] point_x = new double[points.size()];
        double[] point_y = new double[points.size()];
        int x_iter = 0;
        int y_iter = 0;
        for (Vector2F p : points){
            double[] v = Main.isometricRender.toScreenIso(p.getX(),p.getY(),currentHeight);
            point_x[x_iter++] = v[0];
            point_y[y_iter++] = v[1];
        }
        //gc.setStroke(obj.getType()==FLOOR ? new Color(0, 1, 0, 0.3) : Color.TRANSPARENT);
        gc.strokePolygon(point_x, point_y, point_x.length);


        //bottom
        point_x = new double[points.size()];
        point_y = new double[points.size()];
        x_iter = 0;
        y_iter = 0;
        for (Vector2F p : points){
            double[] v = Main.isometricRender.toScreenIso(p.getX(),p.getY(),currentBase);
            point_x[x_iter++] = v[0];
            point_y[y_iter++] = v[1];
        }
        gc.strokePolygon(point_x, point_y, point_x.length);


        //faces
        for (int i = 0; i < points.size(); i ++){
            point_x = new double[points.size()];
            point_y = new double[points.size()];
            x_iter = 0;
            y_iter = 0;

            double[] v = isometricRender.toScreenIso(points.get(i).getX(),points.get(i).getY(),currentBase);
            point_x[x_iter++] = v[0];
            point_y[y_iter++] = v[1];
            double[] v1 = isometricRender.toScreenIso(points.get(i).getX(),points.get(i).getY(),currentHeight);
            point_x[x_iter++] = v1[0];
            point_y[y_iter++] = v1[1];

            int i2 = i+1 < points.size() ? i+1 : 0;
            v1 = isometricRender.toScreenIso(points.get(i2).getX(),points.get(i2).getY(),currentHeight);
            point_x[x_iter++] = v1[0];
            point_y[y_iter++] = v1[1];
            v = isometricRender.toScreenIso(points.get(i2).getX(),points.get(i2).getY(),currentBase);
            point_x[x_iter++] = v[0];
            point_y[y_iter++] = v[1];

            gc.strokePolygon(point_x, point_y, point_x.length);
        }
    }

 */


    public BufferedImage upscale(BufferedImage image, int width, int height) {
        double widthScale = image.getWidth()/(double)width;
        double heightScale = image.getHeight()/(double)height;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = output.createGraphics();

        for (int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++) {
                g2.setColor(new Color(image.getRGB(x, y)));
                g2.fillRect(
                        (int) (x * widthScale),
                        (int) (y * heightScale),
                        (int) (1 * widthScale),
                        (int) (1 * heightScale)
                );
            }
        }
        g2.dispose();
        return output;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public int getFace() {
        return face;
    }
}
