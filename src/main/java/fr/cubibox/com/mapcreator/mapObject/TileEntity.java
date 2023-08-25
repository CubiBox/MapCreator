package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Sector;
import fr.cubibox.com.mapcreator.maths.Vector;

public class TileEntity extends Entity{
    public TileEntity(Sector polygon, Vector origin, String id) {
        super(polygon, origin, id);
    }
}
