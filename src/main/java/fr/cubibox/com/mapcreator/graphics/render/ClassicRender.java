package fr.cubibox.com.mapcreator.graphics.render;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.Controller;
import fr.cubibox.com.mapcreator.map.Type;
import fr.cubibox.com.mapcreator.maths.MathFunction;
import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Wall;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.cubibox.com.mapcreator.Main.xSize;
import static fr.cubibox.com.mapcreator.Main.DIML;
import static fr.cubibox.com.mapcreator.Main.DIMC;
import static fr.cubibox.com.mapcreator.map.Type.WALL;

public class ClassicRender extends RenderPane {

    public ClassicRender(Controller controller) {
        super(controller);
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
                Type type = WALL;
                tempPolygons = new ArrayList<>();
                ArrayList<Vector2F> vectors = new ArrayList<>(Arrays.asList(
                                currentPos,
                                new Vector2F(dragPointOrigin[0], roundY),
                                new Vector2F(dragPointOrigin[0], dragPointOrigin[1]),
                                new Vector2F(roundX, dragPointOrigin[1])
                        ));
                Vector2F buff = vectors.get(vectors.size()-1);
                for (Vector2F vec : vectors) {
                    tempPolygons.add(new Line(
                            Main.toScreenX(buff.getX()), Main.toScreenY(buff.getY()),
                            Main.toScreenX(vec.getX()), Main.toScreenY(vec.getY())
                    ));
                    buff = vec;
                }

                for (Shape shape : tempPolygons){
                    shape.setFill(Color.TRANSPARENT);
                    shape.setStrokeWidth(2.0);
                    shape.setStroke(switch (type) {
                        case FLOOR -> Color.LIME;
                        case CELLING -> Color.RED;
                        default -> Color.CYAN;
                    });
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawPolygon(Pane coordinateSystem, Sector obj) {
        super.drawPolygon(coordinateSystem, obj);

        drawShapes(coordinateSystem, obj);
        if (obj.isShowPoint()) {
            drawPointsLabel(coordinateSystem, obj);
        }
    }

    @Override
    public void drawShapes(Pane coordinateSystem, Sector sec) {
        super.drawShapes(coordinateSystem, sec);

        Vector2F showPoint = null;
        for (Wall wall : controller.repositories.getWalls(sec)) {
            Vector2F vec1 = controller.repositories.getVectorByID(wall.getVector1ID());
            Vector2F vec2 = controller.repositories.getVectorByID(wall.getVector2ID());
            Line line = new Line(
                    Main.toScreenX(vec1.getX()),
                    Main.toScreenY(vec1.getY()),
                    Main.toScreenX(vec2.getX()),
                    Main.toScreenY(vec2.getY())
            );
            line.setFill(new Color(0, 1, 1, 0.3));
            line.setStrokeWidth(wall.isSelected() ? 3.5 : 1.2);
            line.setStroke(Color.CYAN);

            coordinateSystem.getChildren().add(line);

            if (vec1.isSelected()) showPoint = vec1;
            if (vec2.isSelected()) showPoint = vec2;
        }

        if (showPoint != null) {
            Label pointName = new Label(" v");
            pointName.setLayoutX(Main.toScreenX(showPoint.getX()) - 5);
            pointName.setLayoutY(Main.toScreenX(showPoint.getY()) - 20);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);

            coordinateSystem.getChildren().add(showPoint.getCirclePoint());
        }


        /*
        for (Shape shape : pol.getShapes()) {
            shape.setOnMousePressed(event -> {
                if (event.isSecondaryButtonDown()) {
                    System.out.println("here " + pol.getCeilHeight());
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

         */
    }

    @Override
    public void drawPointsLabel(Pane coordinateSystem, Sector pol) {
        super.drawPointsLabel(coordinateSystem, pol);

        int countP = 0;
        float[] v = new float[2];
        for (Vector2F p : controller.repositories.getVectors(pol)) {
            Label pointName = new Label(countP++ + "");
            pointName.setLayoutX(toScreenX(p.getX()) - 5);
            pointName.setLayoutY(toScreenY(p.getY()) - 15);
            pointName.setTextFill(Color.WHITE);
            coordinateSystem.getChildren().add(pointName);
        }
    }

    @Override
    public void drawPointShape(Pane coordinateSystem, Vector2F vector) {
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