package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class OceanTile extends Polygon implements Tile<Polygon> {

    public OceanTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.BLUE);
    }

    public void affectTile(Polygon polygon) {
        polygon.setTemperature(0);
        polygon.setPrecipitation(0);
    }
}