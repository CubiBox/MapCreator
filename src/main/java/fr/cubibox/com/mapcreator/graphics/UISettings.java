package fr.cubibox.com.mapcreator.graphics;

public class UISettings {
    private static UISettings instance;




    private UISettings() { }
    public static UISettings getInstance(){
        if (instance == null)
            instance = new UISettings();
        return instance;
    }

}
