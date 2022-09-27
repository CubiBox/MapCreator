module fr.cubibox.com.mapcreator {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.cubibox.com.mapcreator to javafx.fxml;
    exports fr.cubibox.com.mapcreator.map;
    opens fr.cubibox.com.mapcreator.map to javafx.fxml;
    exports fr.cubibox.com.mapcreator.iu;
    opens fr.cubibox.com.mapcreator.iu to javafx.fxml;
    exports fr.cubibox.com.mapcreator;
}