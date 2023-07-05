package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.cubibox.com.mapcreator.Main.points;
import static fr.cubibox.com.mapcreator.graphics.Texture.*;

public class Face {
    private Vector2F point1;
    private Vector2F point2;
    private double height;
    private double orientation;

    private Texture texture;

    public Face(Vector2F point1, Vector2F point2, double height, double orientation, Texture texture) {
        this.point1 = point1;
        this.point2 = point2;
        this.height = height;
        this.orientation = orientation;
        this.texture = texture;
    }

    public Face(Vector2F point1, Vector2F point2, double height, Texture texture) {
        this(point1, point2, height, 0, texture);
        this.orientation = 0;
    }

    public Face(Vector2F point1, Vector2F point2, Texture texture) {
        this(point1, point2, 0, texture);
    }

    public Face(Vector2F point1, Vector2F point2, double height) {
        this(point1, point2, height, new Texture("dirt.png"));
    }

    public ArrayList<Vector2F> getPoints() {
        return new ArrayList<>(List.of(point1, point2));
    }

    public double getHeight() {
        return height;
    }

    public double getOrientation() {
        return orientation;
    }

    public Texture getTexture() {
        return texture;
    }


    public void drawTextureIso(IsometricRender iso, GraphicsContext gc, double height) {
        double[][] bounds = new double[][]{
                iso.toScreenIso(point2.getX(), point2.getY(), 0),
                iso.toScreenIso(point2.getX(), point2.getY(), height),
                iso.toScreenIso(point1.getX(), point1.getY(), height)
        };

        double iso_width = bounds[1][0] - bounds[2][0];
        double iso_height = bounds[1][1] - bounds[2][1];

        double edgeWidth = Math.sqrt(iso_width * iso_width + iso_height * iso_height);

        double[] max_x = bounds[1][0] > bounds[2][0] ? bounds[1] : bounds[2];
        double[] max_y = bounds[1][1] > bounds[2][1] ? bounds[1] : bounds[2];
        double[] min_x = bounds[1][0] < bounds[2][0] ? bounds[1] : bounds[2];
        double[] min_y = bounds[1][1] < bounds[2][1] ? bounds[1] : bounds[2];

        double delta_x = iso_width/iso_height;
        double delta_y = (bounds[1][1] != bounds[2][1]) ? (bounds[1][1] > bounds[2][1]) ? -1 : 1 : 0;
        double delta_z = bounds[0][1] - bounds[1][1];

        //System.out.println(iso_width + "; " + iso_height + "; " + delta_x);
        int rgba = new Random().nextInt(Integer.MAX_VALUE) * -1;

        for (double curr_z = bounds[1][1]; curr_z <= bounds[1][1]+delta_z; curr_z ++){
            double relat_y = curr_z;
            for (double x = 0; x < iso_width; x += Math.abs(delta_x)){
                double relat_x = bounds[1][0] - x;

                if (relat_y > 0d && relat_x > 0d && relat_x <= max_x[0] && Math.abs(delta_x) != 0) {
                    if (delta_x < 1d) {
                        for (int filler = 0; filler > delta_x; filler -= delta_y) {
                            if (relat_x + filler >= min_x[0])
                                gc.getPixelWriter().setArgb((int) relat_x + filler, (int) relat_y, texture.getRgba(x - filler, curr_z - bounds[1][1], edgeWidth, delta_z));
                        }
                    }
                    else if (delta_x > -1d){
                        for (double filler = delta_x; filler > 0; filler += delta_y) {
                            if (relat_x + filler <= max_x[0])
                                gc.getPixelWriter().setArgb((int) (relat_x + filler), (int) relat_y, texture.getRgba(x - filler, curr_z - bounds[1][1], edgeWidth, delta_z));
                        }
                        if (x + Math.abs(delta_x) > iso_width) {
                            for (double filler = delta_x; filler > 0; filler += delta_y) {
                                double new_relat_x = relat_x + filler - Math.abs(delta_x);
                                if (new_relat_x <= max_x[0] && new_relat_x >= min_x[0])
                                    gc.getPixelWriter().setArgb((int) (new_relat_x), (int) relat_y - 1, texture.getRgba(x + filler, curr_z - bounds[1][1], edgeWidth, delta_z));
                            }
                            gc.getPixelWriter().setArgb((int) (bounds[2][0]), (int) relat_y, 0);
                        }
                    }
                }
                relat_y += delta_y;
            }
        }

        gc.getPixelWriter().setArgb((int) (bounds[1][0]), (int) (bounds[1][1]), 150000);
        gc.getPixelWriter().setArgb((int) (bounds[2][0]), (int) (bounds[2][1]), 150000);
    }
}
