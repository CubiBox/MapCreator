package fr.cubibox.com.mapcreator.io;

import fr.cubibox.com.mapcreator.graphics.Controller;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;


public class Keyboard {
    private boolean controlDown;
    private boolean shiftDown;
    private boolean altDown;

    public Keyboard(Pane coordinateSystem) {

        coordinateSystem.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case CONTROL -> controlDown = true;
                case SHIFT -> shiftDown = true;
                case ALT -> altDown = true;
            }
        });

        coordinateSystem.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case CONTROL -> controlDown = false;
                case SHIFT -> shiftDown = false;
                case ALT -> altDown = false;
            }
        });
    }

    public boolean isControlDown() {
        return controlDown;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isAltDown() {
        return altDown;
    }
}
