package fr.cubibox.com.mapcreator.graphics.ui.pane;

import fr.cubibox.com.mapcreator.Application;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

public enum ViewType {
    SINGLE_VIEW,
    TWO_VIEW_HORIZONTAL,
    TWO_VIEW_VERTICAL,
    THREE_VIEW_PRIMARY_RIGHT,
    THREE_VIEW_PRIMARY_LEFT,
    THREE_VIEW_PRIMARY_BOTTOM,
    THREE_VIEW_PRIMARY_TOP,
    FOUR_VIEW;

    public String getName() {
        return switch (this.ordinal()) {
            case 0 -> "Single view";
            case 1 -> "Two view horizontal";
            case 2 -> "Two view vertical";
            case 3 -> "Three view primary right";
            case 4 -> "Three view primary left";
            case 5 -> "Three view primary bottom";
            case 6 -> "Three view primary top";
            case 7 -> "Four view";
            default -> null;
        };
    }

    public int getPaneNumber() {
        return switch (this.ordinal()) {
            case 1, 2 -> 2;
            case 3, 4, 5, 6 -> 3;
            case 7 -> 4;
            default -> 1;
        };
    }

    public Image getIcon(){
        String icon = switch (this.ordinal()) {
            case 0 -> "single_view";
            case 1 -> "two_view_horizontal";
            case 2 -> "two_view_vertical";
            case 3 -> "three_view_primary_right";
            case 4 -> "three_view_primary_left";
            case 5 -> "three_view_primary_bottom";
            case 6 -> "three_view_primary_top";
            case 7 -> "four_view";
            default -> null;
        };
        try {
            return new Image(Objects.requireNonNull(Application.class.getResource("images/view_icons/" + icon + ".png")).openStream());
        }
        catch (Exception e) {
            return null;
        }
    }

    public MenuItem asMenuItem(){
        MenuItem menuItem = new MenuItem(this.getName());

        Image icon = this.getIcon();
        if (icon != null) {
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(18);
            imageView.setFitWidth(18);
            menuItem.setGraphic(imageView);
        }
        return menuItem;
    }
}
