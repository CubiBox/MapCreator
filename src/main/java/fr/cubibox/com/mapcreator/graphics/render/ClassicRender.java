package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.old_mapObject.Wall;
import fr.cubibox.com.mapcreator.maths.MathFunction;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;
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
import static fr.cubibox.com.mapcreator.old_mapObject.Type.WALL;

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
            Vector currentPos = new Vector(roundX, roundY);
            if (dragState && !(dragPointOrigin[0] == currentPos.getX() && dragPointOrigin[1] == currentPos.getY())) {
                tempPolygons = Sector.topShape(
                        //selectType(SelectionOption),
                        WALL,
                        currentPos,
                        new Vector(dragPointOrigin[0], roundY),
                        new Vector(dragPointOrigin[0], dragPointOrigin[1]),
                        new Vector(roundX, dragPointOrigin[1])
                );
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawPolygon(Pane coordinateSystem, Wall obj) {
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
    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        int countP = 0;
        float[] v = new float[2];
        for (Vector p : pol.getPoints()) {
            Label pointName = new Label(countP++ + "");
            pointName.setLayoutX(toScreenX(p.getX()) - 5);
            pointName.setLayoutY(toScreenY(p.getY()) - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector vector) {
        super.drawPointShape(coordinateSystem, vector);

        coordinateSystem.getChildren().add(vector.getCircle());
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
    public void actualizePolygon(Sector pol) {
        super.actualizePolygon(pol);

        ArrayList<Vector> vectors = pol.getPoints();
        ArrayList<Shape> lines = new ArrayList<>();

        Color color = Color.CYAN;
        color = switch (pol.getType()) {
            case FLOOR -> Color.LIME;
            case CELLING -> Color.RED;
            default -> Color.CYAN;
        };

        if (vectors.size() > 1) {
            for (int i = 0; i < vectors.size() - 1; ) {
                Line line = new Line(
                        toScreenX(vectors.get(i).getX()), toScreenY(vectors.get(i).getY()),
                        toScreenX(vectors.get(++i).getX()), toScreenY(vectors.get(i).getY())
                );
                line.setFill(Color.TRANSPARENT);
                line.setStrokeWidth(2.0);
                line.setStroke(color);
                lines.add(line);
            }
        }
        Line line = new Line(
                toScreenX(vectors.get(vectors.size()-1).getX()), toScreenY(vectors.get(vectors.size()-1).getY()),
                toScreenX(vectors.get(0).getX()), toScreenY(vectors.get(0).getY())
        );
        line.setFill(Color.TRANSPARENT);
        line.setStrokeWidth(2.0);
        line.setStroke(color);
        lines.add(line);

        pol.setShapes(lines);
    }

    @Override
    public ArrayList<Vector> setPolygonByDrag(double x, double y, float[] dragPointOrigin, boolean dragState) {
        Vector currentPos = new Vector(MathFunction.round(toPlotX(x)), MathFunction.round(toPlotY(y)));
        ArrayList<Vector> pts = new ArrayList<>();
        pts.add(currentPos);

        if (currentPos.getX() >= 0 && currentPos.getX() <= xSize && currentPos.getY() >= 0 && currentPos.getY() <= xSize) {

            if (dragState && !(dragPointOrigin[0] == currentPos.getX() && dragPointOrigin[1] == currentPos.getY())) {
                if (dragPointOrigin[0] == currentPos.getX() || dragPointOrigin[1] == currentPos.getY())
                    pts.add(new Vector(dragPointOrigin[0], dragPointOrigin[1]));

                else pts.addAll(List.of(
                                new Vector(dragPointOrigin[0], currentPos.getY()),
                                new Vector(dragPointOrigin[0], dragPointOrigin[1]),
                                new Vector(currentPos.getX(), dragPointOrigin[1])
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
