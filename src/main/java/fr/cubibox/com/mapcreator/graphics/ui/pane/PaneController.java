package fr.cubibox.com.mapcreator.graphics.ui.pane;

import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.graphics.ui.SettingController;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Sector;
import fr.cubibox.com.mapcreator.map.Vector2v;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.*;

public class PaneController {

    private StackPane stackPane;
    private ButtonBar buttonBar;
    private Pane pane;

    private ClassicRender classicRender;
    private IsometricRender isometricRender;

    private RenderPane currentRender;


    private boolean dragState;
    //TODO use Vector2d
    private float[] dragPointOrigin = new float[2];

    private static PaneController instance;

    public PaneController(){
        initialize();
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
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        pane.setPrefSize(500,500);

        classicRender = new ClassicRender(pane);
        isometricRender = new IsometricRender(Math.PI/4, 0.5, pane);

        currentRender = classicRender;

        //zoom
        pane.setOnScroll(event ->{
            double value = event.getDeltaY();

            if (event.isControlDown()) {
                currentRender.zoom(dragState, value, event);
                draw();
            }
        });


        //record drag status
        pane.setOnMouseDragged(event ->{
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
        pane.setOnMouseClicked(event -> {
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
                TabPaneController.getInstance().draw();
            }
            dragState = false;
        });

        buttonBar = new ButtonBar();
        ToggleButton viewButton = new ToggleButton("2D");
        viewButton.setOnMouseReleased((event) -> {
            viewButton.setText(viewButton.isSelected() ? "3D" : "2D");
            switchRender(viewButton.isSelected());
        });
        buttonBar.getButtons().add(viewButton);
        buttonBar.setMaxSize(120,40);
        buttonBar.setMinSize(120,40);

        stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_RIGHT);
        stackPane.getChildren().addAll(pane, buttonBar);
    }


    public void draw(){
        draw(null);
    }

    public void draw(ArrayList<Shape> tempPol) {
        //System.out.println("drawing");
        pane.getChildren().clear();

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

    public StackPane getNodes() {
        return stackPane;
    }
}