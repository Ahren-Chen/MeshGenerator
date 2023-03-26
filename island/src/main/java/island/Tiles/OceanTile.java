package island.Tiles;

import island.IOEncapsulation.Polygon;

import java.awt.*;

public class OceanTile extends Polygon {

    public OceanTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.BLUE);
        super.setNextToOcean(true);
    }
}