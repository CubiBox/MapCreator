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
        this.base = new Matrix3d(
                new Vector3d(1, 0, 0),
                new Vector3d(0, 1, 0),
                new Vector3d(0, 0, 1)
        );

        origin = new Vector2F(0f, 0f);
    }


    @Override
    public void render() {
        super.render();
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
        if (temp_yAngle >= 0 && temp_yAngle <= 1) {
            setYAngle(temp_yAngle);
        }

        dragPointOrigin[0] = newX;
        dragPointOrigin[1] = newY;
    }

    @Override
    public void drawPolygon(Pane coordinateSystem, Sector sec) {
        super.drawPolygon(coordinateSystem, sec);

        if (SettingController.getInstance().drawableType(sec.getType())) {
            return;
        }

        drawShapes(coordinateSystem, sec);
        if (sec.isShowPoint()) {
            drawPointsLabel(coordinateSystem, sec);
        }
    }

    @Override
    public void drawShapes(Pane coordinateSystem, Sector sec) {
        super.drawShapes(coordinateSystem, sec);

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
    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        for (Vector2v p : Repositories.getInstance().getVectors(pol)) {
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

    /*
    public float[] toScreenIso_(double x, double y, double height){
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
    */

    public float[] toScreenIso(double x, double y, double height){
        Vector3d newVec = new Vector3d(x - 16, y - 16, height);
        newVec.mul(base);

        return new float[] {
                toScreenX(newVec.getX()),
                toScreenY(newVec.getY() + newVec.getZ())
        };
    }
    public float toScreenX(double x){
        //return (float) (cam.getX() + x * xSize);
        return (float) (origin.getX() + cam.getX() + x * xSize);
    }
    public float toScreenY(double y){
        //return (float) (cam.getY() -y * xSize);
        return (float) (origin.getY() - cam.getY() -y * xSize);
    }


    public void updateMatrix(){
        this.base.setMatrix(
                this.base.setIso(xAngle, yAngle, 1, 0, 0),
                this.base.setIso(xAngle, yAngle, 0, 1, 0),
                this.base.setIso(xAngle, yAngle, 0, 0, 1)
        );
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

    public Matrix3d getBase() {
        return base;
    }

    public void setBase(Matrix3d base) {
        this.base = base;
    }
}
