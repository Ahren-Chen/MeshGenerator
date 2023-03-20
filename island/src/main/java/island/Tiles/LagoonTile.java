package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class LagoonTile extends Polygon implements Tile<Polygon> {
    public LagoonTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());

        Color lightBlue = new Color(51, 204, 255);
        super.setColor(lightBlue);
    }

    public void affectTile(Polygon polygon) {
        polygon.setTemperature(0);
        polygon.setPrecipitation(0);
    }
}
