package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.graphics.Controller;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Wall;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.map.Type.CELLING;
import static fr.cubibox.com.mapcreator.map.Type.FLOOR;


public class IsometricRender extends RenderPane {
    private double xAngle;
    private double yAngle;
    private Vector2F origin;

    public IsometricRender(Vector2F origin, Controller controller) {
        this(0, 0.5,  origin, controller);
    }
    public IsometricRender(float origin, Controller controller) {
        this(0, 0.5,  new Vector2F(origin,origin), controller);
    }
    public IsometricRender(double xAngle, double yAngle, Vector2F origin, Controller controller) {
        super(controller);
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.origin = origin;
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public boolean drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint) {
        super.drag(dragState, dragPointOrigin, dragPoint);
        float newX = dragPoint[0];
        float newY = dragPoint[1];

        if (!dragState || (dragPointOrigin[0]-newX>75 || dragPointOrigin[1]-newY>75) || (dragPointOrigin[0]-newX<-75 || dragPointOrigin[1]-newY<-75)){
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
    public void drawPolygon(Pane coordinateSystem, Sector pol) {
        super.drawPolygon(coordinateSystem, pol);

        drawShapes(coordinateSystem, pol);
        if (pol.isShowPoint()) {
            drawPointsLabel(coordinateSystem, pol);
        }
    }

    @Override
    public void drawShapes(Pane coordinateSystem, Sector pol) {
        super.drawShapes(coordinateSystem, pol);

        //calc here ?
        //for (Shape shape : pol.getShapesIso())
        for (Shape shape : pol.getShapes())
            coordinateSystem.getChildren().add(shape);
    }

    @Override
    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        int countP = 0;
        float[] v;
        for (Vector2F p : controller.repositories.getVectors(pol)) {
            Label pointName = new Label(countP++ + "");
            v = toScreenIso(p.getX(), p.getY(), pol.getCeilHeight());
            pointName.setLayoutX(v[0] - 5);
            pointName.setLayoutY(v[1] - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector2F vector) {
        super.drawPointShape(coordinateSystem, vector);

        float[] v = toScreenIso(vector.getX(), vector.getY());
        coordinateSystem.getChildren().add(new Circle(v[0], v[1], 3, vector.getColor()));
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

    @Override
    public void actualizePolygon(Sector pol) {
        super.actualizePolygon(pol);

        ArrayList<Vector2F> vectors = controller.repositories.getVectors(pol);
        ArrayList<Wall> walls = controller.repositories.getWalls(pol);
        ArrayList<Shape> shapes = new ArrayList<>();

        float currentHeight = pol.getCeilHeight();
        float currentBase = pol.getFloorHeight();
        Color wallColor = new Color(0, 1, 1, 0.3);
        Color color = Color.DARKGRAY;

        if (walls.size() > 1) {
            ArrayList<float[]> topFace = new ArrayList<>();
            ArrayList<float[]> bottomFace = new ArrayList<>();

            for (Wall wall : walls) {
                double[] polPoints = new double[8];

                Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
                Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
                float[] v1 = toScreenIso(vec1.getX(),vec1.getY(),currentHeight);
                float[] v2 = toScreenIso(vec2.getX(),vec2.getY(),currentHeight);
                float[] v3 = toScreenIso(vec2.getX(),vec2.getY(),currentBase);
                float[] v4 = toScreenIso(vec1.getX(),vec1.getY(),currentBase);
                polPoints[0] = v1[0];
                polPoints[1] = v1[1];
                polPoints[2] = v2[0];
                polPoints[3] = v2[1];
                polPoints[4] = v3[0];
                polPoints[5] = v3[1];
                polPoints[6] = v4[0];
                polPoints[7] = v4[1];

                shapes.add(new Polygon(polPoints));

                if (!topFace.contains(v1)) topFace.add(v1);
                if (!bottomFace.contains(v3)) bottomFace.add(v3);
            }

            /**top / bottom faces*/
            /*
            double[] polPoints = new double[topFace.size()*2];
            int i = 0;
            for (float[] vec : topFace){
                polPoints[i++] = vec[0];
                polPoints[i++] = vec[1];
            }
            shapes.add(new Polygon(polPoints));

            i = 0;
            for (float[] vec : bottomFace){
                polPoints[i++] = vec[0];
                polPoints[i++] = vec[1];
            }
            shapes.add(new Polygon(polPoints));
             */

            for (Shape shape : shapes) {
                shape.setFill(wallColor);
                shape.setStrokeWidth(1.0);
                shape.setStroke(color);
            }
        }
        pol.setShapes(shapes);
    }


    public float[] toScreenIso(double x, double y){
        return toScreenIso(x,y,0);
    }

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
