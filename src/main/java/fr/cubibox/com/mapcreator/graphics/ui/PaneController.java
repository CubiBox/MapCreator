package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Sector;
import fr.cubibox.com.mapcreator.map.Vector2v;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.*;

public class PaneController {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    private Pane coordinateSystem;

    private ClassicRender classicRender;
    private IsometricRender isometricRender;

    private RenderPane currentRender;


    private boolean dragState;
    private float[] dragPointOrigin = new float[2];

    private static PaneController instance;

    private PaneController(){
    }

    public void switchRender(boolean isIsoSelected){
        currentRender = isIsoSelected ? isometricRender : classicRender;
        draw();
    }

    public static PaneController getInstance(){
        if (instance == null){
            instance = new PaneController();
        }
        return instance;
    }

    public void initialize() {
        classicRender = new ClassicRender(coordinateSystem);
        isometricRender = new IsometricRender(Math.PI/4, 0.5, coordinateSystem);

        currentRender = classicRender;


        //zoom
        coordinateSystem.setOnScroll(event ->{
            double value = event.getDeltaY();

            if (event.isControlDown()) {
                currentRender.zoom(dragState, value, event);
                draw();
            }
        });


        //record drag status
        coordinateSystem.setOnMouseDragged(event ->{
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                float[] dragPoint = {(float) event.getX(), (float) event.getY()};

                if (event.isShiftDown()){
                    currentRender.move(dragState, dragPointOrigin, dragPoint, event);
                }
                else {
                    currentRender.drag(dragState, dragPointOrigin, dragPoint, event);
                }
                draw(currentRender.getTempPolygons());

                dragState = true;
            }
        });

        //set by click (make intern variable to know if drag is shifted or not)
        coordinateSystem.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && !event.isShiftDown()) {
                ArrayList<Vector2v> pts = currentRender.setPolygonByDrag(event.getX(), event.getY(), dragPointOrigin, dragState);
                if (pts != null) {
                    if (pts.size() > 1) {
                        SettingController.getInstance().setPolygon(pts);
                        currentRender.setTempPolygons(new ArrayList<>());
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


    public void draw(){
        draw(null);
    }

    public void draw(ArrayList<Shape> tempPol) {
        //System.out.println("drawing");
        coordinateSystem.getChildren().clear();

        //draw the grid
        currentRender.drawGrid();

        //draw points
        for (Vector2v vector : Repositories.getInstance().tpmPoints) {
            currentRender.drawPointShape(vector);
        }

        //draw polygons
        for (Sector obj : Repositories.getInstance().getAllSectors()) {
            currentRender.drawPolygon(obj);
        }

        //draw temporary polygons (ephemeral ones)
        if (tempPol != null && !tempPol.isEmpty()){
            currentRender.drawTemporaryPolygon(tempPol);
        }
    }
}