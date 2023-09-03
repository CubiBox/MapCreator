package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.graphics.Controller;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Wall;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

import static fr.cubibox.com.mapcreator.Main.*;


public class IsometricRender extends RenderPane {
    private double xAngle;
    private double yAngle;

    private final Vector2F origin;
    private final Vector2F cam;
    private double zoom;


    public IsometricRender(Vector2F origin, Controller controller) {
        this(0, 0.5,  origin, controller);
    }
    public IsometricRender(double xAngle, double yAngle, Vector2F origin, Controller controller) {
        super(controller);
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.origin = origin;
        this.cam = new Vector2F(0.f, 0.f);
        this.zoom = getDIML();
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void move(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        super.move(dragState, dragPointOrigin, dragPoint, event);

        float xDistance = dragPointOrigin[0] - dragPoint[0];
        float yDistance = dragPointOrigin[1] - dragPoint[1];

        cam.setX(cam.getX() - xDistance);
        cam.setY(cam.getY() - yDistance);

        dragPointOrigin[0] = dragPoint[0];
        dragPointOrigin[1] = dragPoint[1];
    }

    @Override
    public void zoom(boolean dragState, double value, ScrollEvent event) {
        super.zoom(dragState, value, event);

        zoom += value;
        cam.setX((float) (cam.getX() - value * 0.5));
        cam.setY((float) (cam.getY() - value * 0.5));

        System.out.println(zoom);
    }

    @Override
    public boolean drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        super.drag(dragState, dragPointOrigin, dragPoint, event);
        float newX = dragPoint[0];
        float newY = dragPoint[1];

        if (!dragState || (dragPointOrigin[0] - newX > 75 || dragPointOrigin[1] - newY > 75) || (dragPointOrigin[0] - newX < -75 || dragPointOrigin[1] - newY < -75)) {
            dragPointOrigin[0] = newX;
            dragPointOrigin[1] = newY;
        }

        setXAngle((float) (getXAngle() + (dragPointOrigin[0] - newX) * 0.0045f));
        float temp_yAngle = (float) (getYAngle() - (dragPointOrigin[1] - newY) * 0.001f);
        if (temp_yAngle >= 0 && temp_yAngle <= 1) {
            setYAngle(temp_yAngle);
        }

        dragPointOrigin[0] = newX;
        dragPointOrigin[1] = newY;

        return true;
    }

    @Override
    public void drawPolygon(Pane coordinateSystem, Sector sec) {
        super.drawPolygon(coordinateSystem, sec);

        drawShapes(coordinateSystem, sec);
        if (sec.isShowPoint()) {
            drawPointsLabel(coordinateSystem, sec);
        }
    }

    @Override
    public void drawShapes(Pane coordinateSystem, Sector sec) {
        super.drawShapes(coordinateSystem, sec);

        Vector2F showPoint = null;
        for (Wall wall : controller.repositories.getWalls(sec)){
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            float[] v1 = toScreenIso(vec1.getX(),vec1.getY(),sec.getCeilHeight());
            float[] v2 = toScreenIso(vec2.getX(),vec2.getY(),sec.getCeilHeight());
            float[] v3 = toScreenIso(vec2.getX(),vec2.getY(),sec.getFloorHeight());
            float[] v4 = toScreenIso(vec1.getX(),vec1.getY(),sec.getFloorHeight());
            double[] secPoints = {v1[0],v1[1],v2[0],v2[1],v3[0],v3[1],v4[0],v4[1]};
            Polygon pol = new Polygon(secPoints);
            if (wall.isSelected()) {
                pol.setFill(new Color(0, 1, 1, 0.5));
                pol.setStrokeWidth(2);
                pol.setStroke(new Color(0.8, 0.8, 0.8, 1));
            }
            else {
                pol.setFill(new Color(0, 1, 1, 0.3));
                pol.setStrokeWidth(0.7);
                pol.setStroke(new Color(0.5, 0.5, 0.5, 1));
            }
            coordinateSystem.getChildren().add(pol);

            if (vec1.isSelected()) showPoint = vec1;
            if (vec2.isSelected()) showPoint = vec2;
        }

        if (showPoint != null) {
            Label pointName = new Label(" v");
            float[] v = toScreenIso(showPoint.getX(), showPoint.getY(), sec.getCeilHeight());
            pointName.setLayoutX(v[0] - 5);
            pointName.setLayoutY(v[1] - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);

            float[] v1 = toScreenIso(showPoint.getX(),showPoint.getY(),sec.getCeilHeight());
            float[] v2 = toScreenIso(showPoint.getX(),showPoint.getY(),sec.getFloorHeight());
            Line line = new Line(v1[0],v1[1],v2[0],v2[1]);
            line.setStrokeWidth(2);
            line.setStroke(new Color(0.8, 0.8, 0.8, 1));
            coordinateSystem.getChildren().add(line);
        }
    }

    @Override
    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        for (Vector2F p : controller.repositories.getVectors(pol)) {
            Label pointName = new Label(p.getId() + "");
            float[] v = toScreenIso(p.getX(), p.getY(), pol.getCeilHeight());
            pointName.setLayoutX(v[0] - 5);
            pointName.setLayoutY(v[1] - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
            drawPointShape(coordinateSystem, p);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector2F vector) {
        super.drawPointShape(coordinateSystem, vector);

        float[] v1 = toScreenIso(vector.getX()+0.05, vector.getY()+0.05);
        float[] v2 = toScreenIso(vector.getX()+0.05, vector.getY()-0.05);
        float[] v3 = toScreenIso(vector.getX()-0.05, vector.getY()-0.05);
        float[] v4 = toScreenIso(vector.getX()-0.05, vector.getY()+0.05);
        Polygon pol = new Polygon(
                v1[0],v1[1],
                v2[0],v2[1],
                v3[0],v3[1],
                v4[0],v4[1]
        );
        pol.setStroke(Color.GOLD);
        coordinateSystem.getChildren().add(pol);
    }

    @Override
    public void drawGrid(Pane coordinateSystem) {
        super.drawGrid(coordinateSystem);

        ArrayList<Shape> lines = new ArrayList<>();
        ArrayList<Shape> tpmLines = new ArrayList<>();

        Line line1,line2;

        for (int i = 0; i <= xSize; i++) {
            float[] var1 = toScreenIso(i, 0);
            float[] var2 = toScreenIso(i, xSize);
            float[] var3 = toScreenIso(0, i);
            float[] var4 = toScreenIso(xSize, i);

            line1 = new Line(var1[0], var1[1], var2[0], var2[1]);
            line2 = new Line(var3[0], var3[1], var4[0], var4[1]);

            line1.setStroke(Color.rgb(30, 30, 30));
            line1.setStrokeWidth(3);
            line1.setSmooth(true);

            line2.setStroke(Color.rgb(30, 30, 30));
            line2.setStrokeWidth(3);
            line2.setSmooth(true);

            if (i % 8 != 0) {
                coordinateSystem.getChildren().add(line2);
                coordinateSystem.getChildren().add(line1);
            } else {
                tpmLines.add(line1);
                tpmLines.add(line2);
            }
        }

        for (Shape line : tpmLines) {
            line.setStroke(Color.rgb(70, 70, 70));
            coordinateSystem.getChildren().add(line);
        }
    }


    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

    public float[] toScreenIso(double x, double y, double height){
        double relativeX = origin.getX() - x;
        double relativeY = origin.getY() - y;
        double newAngle = xAngle + ((relativeX==0 && relativeY==0) ? 0 : Math.atan(relativeY / relativeX));
        double originDistance = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
        double finalX = originDistance * Math.cos(newAngle) * zoom/xSize * (relativeX < 0 ? -1 : 1) /2;
        double finalY = originDistance * Math.sin(newAngle) * zoom/xSize * (relativeX < 0 ? -1 : 1) /2;

        double heightOffset = zoom*0.65 - (Math.sin(Math.PI/2-(yAngle*(Math.PI/2)))*height * zoom/64);
        return new float[] {
                (float) (cam.getX() + zoom/2 + (finalX - finalY)),
                (float) (cam.getY() + heightOffset + (finalY + finalX) * yAngle)
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

    public Vector2F getCam() {
        return cam;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
