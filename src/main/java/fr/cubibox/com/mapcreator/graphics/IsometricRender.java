package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.maths.Cube2F;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.StringJoiner;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.graphics.Texture.*;
import static fr.cubibox.com.mapcreator.mapObject.Type.*;


public class IsometricRender {
    private double xAngle;
    private double yAngle;
    private Vector2F origin;

    private double heightOffset;


    public IsometricRender(Vector2F origin) {
        this (0, 0.5, origin);
    }
    public IsometricRender(float origin) {
        this (0, 0.5, new Vector2F(origin,origin));
    }
    public IsometricRender(double xAngle, double yAngle, Vector2F origin) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.origin = origin;
        this.heightOffset = (Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * getDIML()/64);
    }

    /**
     * Is to convert just one point
     * @param x
     * @param y
     * @return
     */
    public double[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

    /**
     * Is to convert just one point
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double[] toScreenIso(double x, double y, double z){
        return toScreenIso(x, y ,z, heightOffset);
    }

    public double[] toScreenIso(double x, double y, double z, double heightOffset){
        double relativeX = origin.getX() - x;
        double relativeY = origin.getY() - y;

        double newAngle = xAngle + ((relativeX == 0d && relativeY == 0d) ? 0d : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);

        double finalX = originDistance * Math.cos(newAngle) * getDIML()/xSize * (relativeX < 0d ? -1d : 1d) / 2d;
        double finalY = originDistance * Math.sin(newAngle) * getDIML()/xSize * (relativeX < 0d ? -1d : 1d) / 2d;

        return new double[] {
                getDIML()/2 + (finalX - finalY),
                getDIML()*0.65 - heightOffset * z + (finalY*yAngle + finalX*yAngle)
        };
    }

    public void drawIso(StaticObject obj, GraphicsContext gc) {
        this.heightOffset = (Math.sin(Math.PI / 2d - (yAngle * (Math.PI / 2d))) * getDIML()/64);
        if (obj.getType() == CUBE){
            Cube2F cube = (Cube2F) obj.getPolygon();

            /*
            drawTextureIso(cube.getTextureTop(), gc, obj.getPolygon());
            drawTextureIso(cube.getTextureEast(), gc, obj.getPolygon());
            drawTextureIso(cube.getTextureWest(), gc, obj.getPolygon());
            drawTextureIso(cube.getTextureNorth(), gc, obj.getPolygon());
            drawTextureIso(cube.getTextureSouth(), gc, obj.getPolygon());
            */
            for (Face face : cube.getFaces()){
                face.drawTextureIso(this, gc, cube.getHeight());
            }
        }
        else {
            ArrayList<Vector2F> points = obj.getPolygon().getPoints();
            float currentHeight = obj.getPolygon().getHeight();
            float currentBase = 0;

            switch (obj.getType()) {
                case FLOOR -> {
                    gc.setStroke(Color.rgb(0, 255, 0, 0.3));
                }
                case CELLING -> {
                    gc.setStroke(Color.rgb(255, 0, 0, 0.3));
                    currentHeight = 31;
                }
                default -> { //  WALL or null
                    gc.setStroke(Color.rgb(0, 255, 255, 0.3));
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
    }

    public void drawTextureIso(Texture texture, GraphicsContext gc, Polygon2F pol) {
        double textureWidth = texture.getWidth();
        double textureHeight = texture.getHeight();
        int face = texture.getFace();
        double z = pol.getHeight();

        int p_size = pol.getPoints().size();
        double[] point_x = new double[p_size];
        double[] point_y = new double[p_size];
        int x_iter = 0;
        int y_iter = 0;
        double x_max = 0;
        double y_max = 0;
        double x_min = -1;
        double y_min = -1;
        for (Vector2F point : pol.getPoints()){
            if (point.getX() > x_max) x_max = point.getX();
            if (point.getY() > y_max) y_max = point.getY();
            if (point.getX() < x_min || x_min == -1) x_min = point.getX();
            if (point.getY() < y_min || y_min == -1) y_min = point.getY();
            double[] v = Main.isometricRender.toScreenIso(point.getX(),point.getY(),z);
            point_x[x_iter++] = v[0];
            point_y[y_iter++] = v[1];
        }
        //System.out.println( new StringJoiner("\n").add(x_max+"").add(y_max+"").add(x_min+"").add(y_min+""));

        for (int i = 0; i < points.size(); i ++)
            gc.getPixelWriter().setArgb( (int) (point_x[i]),  (int) (point_y[i]), 150000);

        //clipping
//        switch (face){   // will be face.angle checking
//            case NORTH -> { if (xAngle >= angle1 && xAngle <= angle3) return; }
//            case SOUTH -> { if (xAngle >= angle3 || xAngle <= angle1) return; }
//            case WEST -> { if (xAngle >= angle4 || xAngle <= angle2) return; }
//            case EAST -> { if (xAngle >= angle2 && xAngle <= angle4) return; }
//        };

        int sizeH = (int) (textureHeight/16);
        int sizeW = (int) (textureWidth/16);

        if (false)
        for (int y = 0; y < sizeH; y++) {
            for (int x = 0; x < sizeW; x++) {
                //int rgba = texture.getTexture().getRGB(x, y);

                int rgba = (face == NORTH) ? -7895295 : (face == SOUTH) ? -6371476 : (face == WEST) ? -532727727 : (face == EAST) ? -137427000 : 0;

                //int rgba = 150000;
                double[] coos = switch (face){
                    case TOP -> toScreenIso(x, y, z);
                    case BOTTOM -> toScreenIso(x, y, 0);

                    case NORTH -> toScreenIso(y, 0, x*(z/8));
                    case SOUTH -> toScreenIso(y, sizeW, x*(z/8));

                    case EAST -> toScreenIso(0, x, y*(z/8));
                    case WEST -> toScreenIso(sizeH, x, y*(z/8));

                    default -> throw new IllegalStateException("Unexpected value: " + face);
                };

                if (coos[0] > 0d && coos[1] > 0d)
                    gc.getPixelWriter().setArgb((int) (coos[0]), (int) (coos[1]) , rgba);
            }
        }


        double polWidth = (x_max - x_min);
        double polHeight = (y_max - y_min);

        double pixel_width = polWidth / textureWidth;
        double pixel_height = polHeight / textureHeight;

        double pixel_zw = z/polWidth;
        double pixel_zh = z/polHeight;

        if (false)
        for (double y = y_min; y <= y_max; y += pixel_height){
            for (double x = x_min; x <= x_max; x += pixel_width){
                //System.out.println((x - x_min)/ pixel_width);
                int rgba = -35464641;
                if ((x - x_min)/ pixel_width < textureWidth && (y - y_min)/ pixel_height < textureHeight)
                    rgba = texture.getTexture().getRGB((int)((x - x_min) / pixel_width), (int)((y - y_min)/ pixel_height));
                else
                    rgba = texture.getTexture().getRGB((int)(textureWidth-1), (int)(textureHeight-1));

                double[] coos = switch (face){
                    case TOP -> toScreenIso(x, y, z);
                    case BOTTOM -> toScreenIso(x, y, 0);

                    case NORTH -> toScreenIso(x, y_min, (y - y_min) * pixel_zh);
                    case SOUTH -> toScreenIso(x, y_max, (y - y_min) * pixel_zh);

                    case EAST -> toScreenIso(x_min, y, (x - x_min) * pixel_zw);
                    case WEST -> toScreenIso(x_max, y, (x - x_min) * pixel_zw);

                    default -> throw new IllegalStateException("Unexpected value: " + face);
                };

                if (coos[0] > 0d && coos[1] > 0d)
                    gc.getPixelWriter().setArgb((int) (coos[0]), (int) (coos[1]) , rgba);
            }
        }

        double[][] bounds = switch (face){
            case TOP -> new double[][]{
                    new double[]{0,0},
                    toScreenIso(x_min, y_min, z),
                    toScreenIso(x_max, y_max, z)
            };
            case BOTTOM -> new double[][]{
                    new double[]{0,0},
                    toScreenIso(x_min, y_min, 0),
                    toScreenIso(x_max, y_max, 0)

            };
            case NORTH -> new double[][]{
                    toScreenIso(x_min, y_min, 0),
                    toScreenIso(x_min, y_min, z),
                    toScreenIso(x_max, y_min, z)
            };
            case SOUTH -> new double[][]{
                    toScreenIso(x_min, y_max, 0),
                    toScreenIso(x_min, y_max, z),
                    toScreenIso(x_max, y_max, z)
            };

            case EAST -> new double[][]{
                    toScreenIso(x_min, y_min, 0),
                    toScreenIso(x_min, y_min, z),
                    toScreenIso(x_min, y_max, z)
            };
            case WEST -> new double[][]{
                    toScreenIso(x_max, y_min, 0),
                    toScreenIso(x_max, y_min, z),
                    toScreenIso(x_max, y_max, z)
            };
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };

        double iso_width = bounds[1][0] - bounds[2][0];
        double iso_height = bounds[1][1] - bounds[2][1];

        double[] max_x = bounds[1][0] > bounds[2][0] ? bounds[1] : bounds[2];
        double[] max_y = bounds[1][1] > bounds[2][1] ? bounds[1] : bounds[2];
        double[] min_x = bounds[1][0] < bounds[2][0] ? bounds[1] : bounds[2];
        double[] min_y = bounds[1][1] < bounds[2][1] ? bounds[1] : bounds[2];

        double delta_x = iso_width/iso_height;
        //double delta_y = Math.abs(iso_height/iso_width) * delta_x;
        double delta_y = 1;
        double delta_z = bounds[0][1] - bounds[1][1];

        System.out.println(iso_width + "; " + iso_height + "; " + delta_x);

        int rgba = switch (face){
            case TOP -> 0;
            case NORTH -> -6481156;
            case SOUTH -> -343145324;
            case EAST -> -6848410;
            case WEST -> -15164433;
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };

        //System.out.println(min_y[0] + "; " + max_y[0] + "; " + iso_width + "; " + delta_x);
        System.out.println("z : " + (min_y[1] <= min_y[1]+delta_z) + " -> " + face);
        System.out.println("y / z : " + (min_y[0] >= max_y[0]) + " -> " + face);
        System.out.println("________");

        for (double curr_z = bounds[1][1]; curr_z <= bounds[1][1]+delta_z; curr_z ++){
            double relat_y = curr_z;

            for (double x = 0; x < iso_width; x += Math.abs(delta_x)){
                double relat_x = (bounds[1][0] > bounds[2][0]) ? bounds[1][0] - x : bounds[2][0] + x;
                delta_y = (bounds[1][1] > bounds[2][1]) ? -1 : 1;

                //System.out.println(x + "; " + iso_width + "; " + relat_x + "; " + delta_x);
                if (relat_y > 0d && relat_x > 0d) {
                    for (int filler = 0; filler < delta_x; filler -= delta_y) { // < delta_x
                        if (relat_x + filler < max_x[0] && relat_x + filler > min_x[0]) {
                            gc.getPixelWriter().setArgb((int) relat_x + filler, (int) relat_y, rgba);
                        }
                    }
                    gc.getPixelWriter().setArgb((int) relat_x, (int) relat_y, 0);
                }

                relat_y += delta_y;
            }


            //System.out.println("y / z : " + (min_y[0] <= max_y[0]) + " -> " + face);
//            for (double x = min_y[0]; x <= max_y[0]; x += delta_x){
//                if (relat_y > 0d && x > 0d)
//                    for (int filler = 0; filler < delta_x; filler++) // < delta_x
//                        if (x+filler < max_x[0]) {
////                            int rgba = (x < textureWidth && relat_y < textureHeight) ?
////                                    texture.getTexture().getRGB((int) x, (int) relat_y :
////                                    texture.getTexture().getRGB((int)(textureWidth-1), (int)(textureHeight-1));
//                            gc.getPixelWriter().setArgb((int) x + filler, (int) relat_y, rgba);
//                        }
//
//                relat_y ++;
//            }
        }

        gc.getPixelWriter().setArgb( (int) (bounds[0][0]),  (int) (bounds[0][1]), 150000);
        gc.getPixelWriter().setArgb( (int) (bounds[1][0]),  (int) (bounds[1][1]), 150000);
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
