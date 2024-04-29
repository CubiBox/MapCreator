package fr.cubibox.com.mapcreator.graphics.ui.pane;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

import java.util.ArrayList;

public class ViewPane {
    private final ViewType viewType;
    private String name;

    private Node nodes;
    private ArrayList<PaneController> panes;

    public ViewPane(ViewType viewType) {
        this.viewType = viewType;
        this.panes = new ArrayList<>();
        setup();
    }

    private void setup() {
        setNodes();
    }

    private void setNodes() {
        for (int paneNumber = 0; paneNumber < viewType.getPaneNumber(); paneNumber++){
            panes.add(new PaneController());
        }

        SplitPane spV = new SplitPane();
        spV.setOrientation(Orientation.VERTICAL);

        SplitPane spH = new SplitPane();
        spH.setOrientation(Orientation.HORIZONTAL);

        switch (viewType) {
            case SINGLE_VIEW -> nodes = panes.get(0).getNodes();
            case TWO_VIEW_HORIZONTAL -> {
                spH.getItems().add(panes.get(0).getNodes());
                spH.getItems().add(panes.get(1).getNodes());
                nodes = spH;
            }
            case TWO_VIEW_VERTICAL -> {
                spV.getItems().add(panes.get(0).getNodes());
                spV.getItems().add(panes.get(1).getNodes());
                nodes = spV;
            }
            case THREE_VIEW_PRIMARY_RIGHT -> {
                spV.getItems().add(panes.get(1).getNodes());
                spV.getItems().add(panes.get(2).getNodes());

                spH.getItems().add(spV);
                spH.getItems().add(panes.get(0).getNodes());
                nodes = spH;
            }
            case THREE_VIEW_PRIMARY_LEFT -> {
                spV.getItems().add(panes.get(1).getNodes());
                spV.getItems().add(panes.get(2).getNodes());

                spH.getItems().add(panes.get(0).getNodes());
                spH.getItems().add(spV);
                nodes = spH;
            }
            case THREE_VIEW_PRIMARY_BOTTOM -> {
                spH.getItems().add(panes.get(1).getNodes());
                spH.getItems().add(panes.get(2).getNodes());

                spV.getItems().add(spH);
                spV.getItems().add(panes.get(0).getNodes());
                nodes = spV;
            }
            case THREE_VIEW_PRIMARY_TOP -> {
                spH.getItems().add(panes.get(1).getNodes());
                spH.getItems().add(panes.get(2).getNodes());

                spV.getItems().add(panes.get(0).getNodes());
                spV.getItems().add(spH);
                nodes = spV;
            }
            //case FOUR_VIEW -> null;
        };
    }

    public ViewType getViewType() {
        return viewType;
    }

    public Node getNodes() {
        return nodes;
    }

    public ArrayList<PaneController> getPanes() {
        return panes;
    }
}
