package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.Main.*;
import static fr.cubibox.com.mapcreator.mapObject.Type.CELLING;
import static fr.cubibox.com.mapcreator.mapObject.Type.FLOOR;


public class IsometricRender extends RenderPane {
    private double xAngle;
    private double yAngle;
    private Vector origin;

    public IsometricRender(Vector origin) {
        this(0, 0.5,  origin);
    }
    public IsometricRender(float origin) {
        this(0, 0.5,  new Vector(origin,origin));
    }
    public IsometricRender(double xAngle, double yAngle, Vector origin) {
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
    public void drawPolygon(Pane coordinateSystem, StaticObject obj) {
        super.drawPolygon(coordinateSystem, obj);

        Sector pol = obj.getPolygon();

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
        for (Vector p : pol.getPoints()) {
            Label pointName = new Label(countP++ + "");
            v = toScreenIso(p.getX(), p.getY(), pol.getHeight());
            pointName.setLayoutX(v[0] - 5);
            pointName.setLayoutY(v[1] - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector vector) {
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

        ArrayList<Vector> vectors = pol.getPoints();
        ArrayList<Shape> shapes = new ArrayList<>();

        float currentHeight,currentBase;
        Color wallColor;
        switch (pol.getType()) {
            case FLOOR -> {
                wallColor = new Color(0, 1, 0, 0.3);
                currentHeight = pol.getHeight();
                currentBase = 0;
            }
            case CELLING -> {
                wallColor = new Color(1, 0, 0, 0.3);
                currentHeight = 31;
                currentBase = pol.getHeight();
            }
            default -> { //  WALL or null
                wallColor = new Color(0, 1, 1, 0.3);
                currentHeight = pol.getHeight();
                currentBase = 0;
            }
        }


        double[] polPoints = new double[vectors.size()*2];

        //top
        int countP = 0;
        for (Vector p : vectors){
            float[] v = toScreenIso(p.getX(),p.getY(),currentHeight);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        Polygon shape = new Polygon(polPoints);
        shape.setFill(pol.getType() == FLOOR ? new Color(0, 1, 0, 0.3) : Color.TRANSPARENT);
        shapes.add(shape);


        //bottom
        countP = 0;
        for (Vector p : vectors){
            float[] v = toScreenIso(p.getX(),p.getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
        }
        shape = new Polygon(polPoints);
        shape.setFill(pol.getType() == CELLING ? new Color(1, 0, 0, 0.3) : Color.TRANSPARENT);
        shapes.add(shape);


        //faces
        for (int i = 0; i < vectors.size(); i ++){
            polPoints = new double[8];
            countP = 0;

            float[] v = toScreenIso(vectors.get(i).getX(), vectors.get(i).getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP++] = v[1];
            float[] v1 = toScreenIso(vectors.get(i).getX(), vectors.get(i).getY(),currentHeight);
            polPoints[countP++] = v1[0];
            polPoints[countP++] = v1[1];

            int i2 = i+1 < vectors.size() ? i+1 : 0;
            v1 = toScreenIso(vectors.get(i2).getX(), vectors.get(i2).getY(),currentHeight);
            polPoints[countP++] = v1[0];
            polPoints[countP++] = v1[1];
            v = toScreenIso(vectors.get(i2).getX(), vectors.get(i2).getY(),currentBase);
            polPoints[countP++] = v[0];
            polPoints[countP] = v[1];

            shape = new Polygon(polPoints);
            shape.setFill(wallColor);
            shapes.add(shape);
        }

        //add global settings
        for (Shape polShape : shapes) {
            polShape.setStrokeWidth(2.0);
            polShape.setStroke(
                    switch (pol.getType()) {
                        case FLOOR -> Color.LIME;
                        case CELLING -> Color.RED;
                        default -> Color.CYAN;
                    }
            );
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

    public Vector getOrigin() {
        return origin;
    }
    public void setOrigin(Vector origin) {
        this.origin = origin;
    }
}
