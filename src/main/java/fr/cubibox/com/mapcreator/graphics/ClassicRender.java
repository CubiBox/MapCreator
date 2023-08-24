package fr.cubibox.com.mapcreator.graphics;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.mapObject.StaticObject;
import fr.cubibox.com.mapcreator.maths.MathFunction;
import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static fr.cubibox.com.mapcreator.Main.xSize;
import static fr.cubibox.com.mapcreator.Main.DIML;
import static fr.cubibox.com.mapcreator.Main.DIMC;
import static fr.cubibox.com.mapcreator.mapObject.Type.WALL;

public class ClassicRender extends RenderPane {

    public ClassicRender() {

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public boolean drag(boolean dragState, float[] dragPointOrigin, float[] dragPoint) {
        super.drag(dragState, dragPointOrigin, dragPoint);

        float roundX = MathFunction.round(toPlotX(dragPoint[0]));
        float roundY = MathFunction.round(toPlotY(dragPoint[1]));

        if (!dragState || (dragPointOrigin[0]-dragPoint[0]>75 || dragPointOrigin[1]-dragPoint[1]>75)) {
            dragPointOrigin[0] = roundX;
            dragPointOrigin[1] = roundY;
        }

        if ((roundX >= 0 && roundX <= xSize && roundY >= 0 && roundY <= xSize)) {
            Vector2F currentPos = new Vector2F(roundX, roundY);
            if (dragState && !(dragPointOrigin[0] == currentPos.getX() && dragPointOrigin[1] == currentPos.getY())) {
                tempPolygons = Polygon2F.topShape(
                        //selectType(SelectionOption),
                        WALL,
                        currentPos,
                        new Vector2F(dragPointOrigin[0], roundY),
                        new Vector2F(dragPointOrigin[0], dragPointOrigin[1]),
                        new Vector2F(roundX, dragPointOrigin[1])
                );
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawPolygon(Pane coordinateSystem, StaticObject obj) {
        super.drawPolygon(coordinateSystem, obj);

        Polygon2F pol = obj.getPolygon();

        drawShapes(coordinateSystem, pol);
        if (pol.isShowPoint()) {
            drawPointsLabel(coordinateSystem, pol);
        }
    }

    @Override
    public void drawShapes(Pane coordinateSystem, Polygon2F pol) {
        super.drawShapes(coordinateSystem, pol);

        //for (Shape shape : pol.getShapeTop()) {
        for (Shape shape : pol.getShapes()) {
            shape.setOnMousePressed(event -> {
                if (event.isSecondaryButtonDown()) {
                    System.out.println("here " + pol.getHeight());
                    if (pol.isSelected()) {
                        pol.setSelected(false);
                        shape.setStrokeWidth(2);
                    } else {
                        pol.setSelected(true);
                        shape.setStrokeWidth(5);
                    }
                }
            });
            coordinateSystem.getChildren().add(shape);
        }
    }

    @Override
    public void drawPointsLabel(Pane coordinateSystem, Polygon2F pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        int countP = 0;
        float[] v = new float[2];
        for (Vector2F p : pol.getPoints()) {
            Label pointName = new Label(countP++ + "");
            pointName.setLayoutX(toScreenX(p.getX()) - 5);
            pointName.setLayoutY(toScreenY(p.getY()) - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector2F point) {
        super.drawPointShape(coordinateSystem, point);

        coordinateSystem.getChildren().add(point.getCircle());
    }

    @Override
    public void drawGrid(Pane coordinateSystem) {
        super.drawGrid(coordinateSystem);

        ArrayList<Shape> tpmLines = new ArrayList<>();
        Line line1,line2;

        for (int i = 0; i <= xSize; i++) {
            line1 = new Line(toScreenX(i), toScreenY(0), toScreenX(i), toScreenY(xSize));
            line2 = new Line(toScreenX(0), toScreenY(i), toScreenX(xSize), toScreenY(i));

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
    public void actualizePolygon(Polygon2F pol) {
        super.actualizePolygon(pol);

        ArrayList<Vector2F> points = pol.getPoints();
        ArrayList<Shape> lines = new ArrayList<>();

        Color color = Color.CYAN;
        color = switch (pol.getType()) {
            case FLOOR -> Color.LIME;
            case CELLING -> Color.RED;
            default -> Color.CYAN;
        };

        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; ) {
                Line line = new Line(
                        toScreenX(points.get(i).getX()), toScreenY(points.get(i).getY()),
                        toScreenX(points.get(++i).getX()), toScreenY(points.get(i).getY())
                );
                line.setFill(Color.TRANSPARENT);
                line.setStrokeWidth(2.0);
                line.setStroke(color);
                lines.add(line);
            }
        }
        Line line = new Line(
                toScreenX(points.get(points.size()-1).getX()), toScreenY(points.get(points.size()-1).getY()),
                toScreenX(points.get(0).getX()), toScreenY(points.get(0).getY())
        );
        line.setFill(Color.TRANSPARENT);
        line.setStrokeWidth(2.0);
        line.setStroke(color);
        lines.add(line);

        pol.setShapes(lines);
    }

    @Override
    public ArrayList<Vector2F> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        Vector2F currentPos = new Vector2F(MathFunction.round(toPlotX(x)), MathFunction.round(toPlotY(y)));
        ArrayList<Vector2F> pts = new ArrayList<>();
        pts.add(currentPos);

        if (currentPos.getX() >= 0 && currentPos.getX() <= xSize && currentPos.getY() >= 0 && currentPos.getY() <= xSize) {

            if (dragState && !(dragPointOrigin[0] == currentPos.getX() && dragPointOrigin[1] == currentPos.getY())) {
                if (dragPointOrigin[0] == currentPos.getX() || dragPointOrigin[1] == currentPos.getY())
                    pts.add(new Vector2F(dragPointOrigin[0], dragPointOrigin[1]));

                else pts.addAll(List.of(
                                new Vector2F(dragPointOrigin[0], currentPos.getY()),
                                new Vector2F(dragPointOrigin[0], dragPointOrigin[1]),
                                new Vector2F(currentPos.getX(), dragPointOrigin[1])
                        )
                );
            }
            return pts;
        }
        else return null;
    }

    public static float toScreenX(double x){
        return (float)(DIMC*(x)/(xSize));
    }
    public static float toScreenY(double y){
        return (float)(DIML*(y)/(xSize));
    }

    public static float toPlotX(double scrX){
        return (float) ((scrX/DIMC)*(xSize));
    }
    public static float toPlotY(double scrY){
        return (float) ((scrY/DIML)*(xSize));
    }
}
