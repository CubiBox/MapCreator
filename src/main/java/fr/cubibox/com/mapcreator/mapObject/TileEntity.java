package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;

public class TileEntity extends Entity{
    public TileEntity(Polygon2F polygon, Vector2F origin, String id) {
        super(polygon, origin, id);
    }
}
