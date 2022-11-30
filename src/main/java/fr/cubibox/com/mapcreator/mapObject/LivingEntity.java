package fr.cubibox.com.mapcreator.mapObject;

import fr.cubibox.com.mapcreator.maths.Polygon2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;

public class LivingEntity extends Entity{
    public LivingEntity(Polygon2F polygon, Vector2F origin, String id) {
        super(polygon, origin, id);
    }
}
