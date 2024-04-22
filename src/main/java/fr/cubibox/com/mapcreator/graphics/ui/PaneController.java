package fr.cubibox.com.mapcreator.graphics.ui;

import fr.cubibox.com.mapcreator.graphics.render.ClassicRender;
import fr.cubibox.com.mapcreator.graphics.render.IsometricRender;
import fr.cubibox.com.mapcreator.graphics.render.RenderPane;
import fr.cubibox.com.mapcreator.map.Repositories;
import fr.cubibox.com.mapcreator.map.Type;
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

public class PaneController implements Initializable {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    private Pane coordinateSystem;



    public ArrayList<Vector2v> tpmPoints;

    private ClassicRender classicRender;
    private IsometricRender isometricRender;

    private RenderPane renderPane;


    private ArrayList<Type> drawablePol;
    private boolean dragState;
    private float[] dragPointOrigin = new float[2];


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        classicRender = new ClassicRender(this);
        isometricRender = new IsometricRender(0, 0, this);

        renderPane = classicRender;
    }


    public void actualizeBoard(){

    }

    public void actualizeView(ActionEvent ae){

    }

    public void drawPolygons(Shape ... tempPol){
        drawPolygons(new ArrayList<>(Arrays.asList(tempPol)));
    }

    public void drawPolygons(ArrayList<Shape> tempPol) {
        actualizeTreeView();
        coordinateSystem.getChildren().clear();
        ArrayList<Vector2v> vectors = tpmPoints;

        //draw the grid
        renderPane.drawGrid(coordinateSystem);

        //draw points
        for(Vector2v vector : vectors) {
            renderPane.drawPointShape(coordinateSystem, vector);
        }

        //draw polygons
        for (Sector obj : Repositories.getInstance().getAllSectors()) {
            if (drawablePol.contains(obj.getType())) {
                renderPane.drawPolygon(coordinateSystem, obj);
            }
        }

        //draw temp polygon (WIP -> move to renderPane)
        if (tempPol != null && !tempPol.isEmpty() && !isoview.isSelected()){
            for (Shape pol : tempPol){
                coordinateSystem.getChildren().add(pol);
            }
        }
    }
}