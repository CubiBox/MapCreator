package fr.cubibox.com.mapcreator.map;

public class Portal extends Wall {
    public final int linkedPortalId;

    public Portal(int id, double xA, double yA, double xB, double yB, int linkedPortalId) {
        super(id, xA, yA, xB, yB);
        this.linkedPortalId = linkedPortalId;
    }
}
