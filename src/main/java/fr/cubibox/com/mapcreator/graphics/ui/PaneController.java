package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Vector2v;
import fr.cubibox.com.mapcreator.maths.Sector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.*;

public class PaneController {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    private Pane coordinateSystem;

    private ClassicRender classicRender;
    private IsometricRender isometricRender;

    private RenderPane renderPane;


    private boolean dragState;
    private float[] dragPointOrigin = new float[2];

    private static PaneController instance;

    private PaneController(){
        classicRender = new ClassicRender(coordinateSystem);
        isometricRender = new IsometricRender(0, 0, coordinateSystem);

        renderPane = classicRender;
    }

    public static PaneController getInstance(){
        if (instance == null){
            instance = new PaneController();
        }
        return instance;
    }

    public void initialize() {
        //zoom
        coordinateSystem.setOnScroll(event ->{
            double value = event.getDeltaY();

            if (event.isControlDown()) {
                renderPane.zoom(dragState, value, event);
                draw();
            }
            System.out.println("scroll");
        });


        //record drag status
        coordinateSystem.setOnMouseDragged(event ->{
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float[] dragPoint = {(float) event.getX(), (float) event.getY()};

                if (event.isShiftDown()){
                    renderPane.move(dragState, dragPointOrigin, dragPoint, event);
                }
                else {
                    renderPane.drag(dragState, dragPointOrigin, dragPoint, event);
                }
                draw(renderPane.getTempPolygons());

                dragState = true;
            }
        });

        //set by click (make intern variable to know if drag is shifted or not)
        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && !event.isShiftDown()) {
                ArrayList<Vector2v> pts = renderPane.setPolygonByDrag(event.getX(), event.getY(), dragPointOrigin, dragState);
                if (pts != null) {
                    if (pts.size() > 1) {
                        SettingController.getInstance().setPolygon(pts);
                        Repositories.getInstance().tpmPoints = new ArrayList<>();
                    }
                    else {
                        Repositories.getInstance().tpmPoints.add(pts.get(0));
                        //polyBoard.getChildren().add(pointBoard(pts.get(0)));
                    }
                }
                draw();
            }
            dragState = false;
        });
    }


    public void draw(Shape ... tempPol){
        draw(new ArrayList<>(Arrays.asList(tempPol)));
    }

    public void draw(ArrayList<Shape> tempPol) {
        coordinateSystem.getChildren().clear();

        //draw the grid
        renderPane.drawGrid(coordinateSystem);

        //draw points
        for (Vector2v vector : Repositories.getInstance().tpmPoints) {
            renderPane.drawPointShape(coordinateSystem, vector);
        }

        //draw polygons
        for (Sector obj : Repositories.getInstance().getAllSectors()) {
//            if (drawable(obj.getType())) {
                renderPane.drawPolygon(coordinateSystem, obj);
//            }
        }

        if (tempPol != null && !tempPol.isEmpty()){
            renderPane.drawTemporaryPolygon(coordinateSystem, tempPol);
        }
    }
}