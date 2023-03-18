package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class LagoonTile extends Polygon implements Tile<Polygon> {
    public LagoonTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(new Color(51, 204, 255));
    }

    public void affectTile(Polygon polygon) {
        polygon.setTemperature(0);
        polygon.setPrecipitation(0);
    }

    @Override
    public void calculateColor() {
        super.setColor(new Color(51, 204, 255));
    }
}
