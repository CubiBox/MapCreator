module fr.cubibox.com.mapcreator {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.cubibox.com.mapcreator to javafx.fxml;

    exports fr.cubibox.com.mapcreator.maths;
    opens fr.cubibox.com.mapcreator.maths to javafx.fxml;

    exports fr.cubibox.com.mapcreator.graphics;
    opens fr.cubibox.com.mapcreator.graphics to javafx.fxml;

    exports fr.cubibox.com.mapcreator;
    exports fr.cubibox.com.mapcreator.map;
    opens fr.cubibox.com.mapcreator.map to javafx.fxml;
    exports fr.cubibox.com.mapcreator.graphics.render;
    opens fr.cubibox.com.mapcreator.graphics.render to javafx.fxml;
}