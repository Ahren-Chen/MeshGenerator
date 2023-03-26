package island.tiles;

import island.IOEncapsulation.Polygon;
import island.interfaces.Tile;

import java.awt.*;

public class OceanTile extends Polygon implements Tile<Polygon> {

    public OceanTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.BLUE);
        super.setNextToOcean(true);
    }
}