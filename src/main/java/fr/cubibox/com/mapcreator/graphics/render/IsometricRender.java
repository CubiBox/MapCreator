package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.map.Sector;
import fr.cubibox.com.mapcreator.graphics.ui.SettingController;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.map.Wall;
import fr.cubibox.com.mapcreator.maths.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Application.*;


public class IsometricRender extends RenderPane {
    private double xAngle;
    private double yAngle;

    private Matrix3d base;


    public IsometricRender(double xAngle, double yAngle, Pane coordinateSystem) {
        super(coordinateSystem);
        this.xAngle = xAngle;
        this.yAngle = yAngle;

        origin = new Vector2F(0f, 0f);

        //TODO no hardcoded value, use screen dim instead
        cam = new Vector2F(600.f, -500.f);

        this.base = new Matrix3d(
                        setIso(new Vector3d(1, 0, 0)),
                        setIso(new Vector3d(0, 1, 0)),
                        setIso(new Vector3d(0, 0, 0.1))
                );
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void move(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        super.move(dragState, dragPointOrigin, dragPoint, event);
        Vector2F distance = getDistance(dragState, dragPointOrigin, dragPoint);

        cam.setX(cam.getX() - distance.getX());
        cam.setY(cam.getY() + distance.getY());

        dragPointOrigin[0] = dragPoint[0];
        dragPointOrigin[1] = dragPoint[1];

        System.out.println(cam);
    }

    @Override
    public void drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint, MouseEvent event) {
        super.drag(dragState, dragPointOrigin, dragPoint, event);
        float newX = dragPoint[0];
        float newY = dragPoint[1];

        if (!dragState || (dragPointOrigin[0] - newX > 75 || dragPointOrigin[1] - newY > 75) || (dragPointOrigin[0] - newX < -75 || dragPointOrigin[1] - newY < -75)) {
            dragPointOrigin[0] = newX;
            dragPointOrigin[1] = newY;
        }

        setXAngle((float) (getXAngle() + (dragPointOrigin[0] - newX) * 0.0045f));
        float temp_yAngle = (float) (getYAngle() - (dragPointOrigin[1] - newY) * 0.001f);
        if (temp_yAngle >= 0 && temp_yAngle <= Math.PI/4) {
            setYAngle(temp_yAngle);
        }

        dragPointOrigin[0] = newX;
        dragPointOrigin[1] = newY;

        System.out.println(xAngle + "; " + yAngle);
    }

    @Override
    public void drawPolygon(Sector sec) {
        super.drawPolygon(sec);

        if (!SettingController.getInstance().drawableType(sec.getType())) {
            return;
        }

        drawShapes(sec);
        if (sec.isShowPoint()) {
            drawPointsLabel(sec);
        }
    }

    @Override
    public void drawShapes(Sector sec) {
        super.drawShapes(sec);

        Vector2F showPoint = null;
        for (Wall wall : Repositories.getInstance().getWalls(sec)){
            Vector2v vec1 = Repositories.getInstance().getVectorByID(wall.getVector1ID());
            Vector2v vec2 = Repositories.getInstance().getVectorByID(wall.getVector2ID());
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
    public void drawPointsLabel(Sector pol) {
        super.drawPointsLabel(pol);

        for (Vector2v p : Repositories.getInstance().getVectors(pol)) {
            Label pointName = new Label(p.getId() + "");
            float[] v = toScreenIso(p.getX(), p.getY(), pol.getCeilHeight());
            pointName.setLayoutX(v[0] - 5);
            pointName.setLayoutY(v[1] - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
            drawPointShape(p);
        }
    }

    @Override
    public void drawPointShape(Vector2F vector) {
        super.drawPointShape(vector);

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
    public void drawGrid() {
        super.drawGrid();

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


    //TODO use vector instead of primitive
    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

    //TODO use vector instead of primitive
    public float[] toScreenIso(double x, double y, double height){
        //TODO replace 16 by the map size
        Vector3d vec = new Vector3d(x - 16, y - 16, height);
        vec.mul(base);

        return new float[] {
                (float) (origin.getX() + cam.getX() + vec.getX() * xSize),
                (float) (origin.getY() - cam.getY() -(vec.getY() + vec.getZ()) * xSize)
        };
    }

    public void updateMatrix(){
        this.base.setMatrix(
                setIso(new Vector3d(1, 0, 0)),
                setIso(new Vector3d(0, 1, 0)),
                setIso(new Vector3d(0, 0, 0.1))
        );
    }

    public Vector3d setIso(Vector3d vec){
        Matrix3d matrix = new Matrix3d(
                new Vector3d(Math.cos(xAngle), -Math.sin(xAngle), 0),
                new Vector3d(Math.sin(xAngle), Math.cos(xAngle), 0),
                new Vector3d(0, 0, 1)
        );
        vec.mul(matrix);

        matrix.setMatrix(
                new Vector3d(1, 0, 0),
                new Vector3d(0, Math.cos(yAngle), -Math.sin(yAngle)),
                new Vector3d(0, Math.sin(yAngle), Math.cos(yAngle))
        );
        vec.mul(matrix);

        return vec;
    }

    public double getXAngle() {
        return xAngle;
    }
    public void setXAngle(double xAngle) {
        this.xAngle = xAngle;
        updateMatrix();
    }

    public double getYAngle() {
        return yAngle;
    }
    public void setYAngle(double yAngle) {
        this.yAngle = yAngle;
        updateMatrix();
    }

    public Matrix3d getBase() {
        return base;
    }

    public void setBase(Matrix3d base) {
        this.base = base;
    }
}
