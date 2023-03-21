package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class LakeTile extends Polygon implements Tile<Polygon> {
    public LakeTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());

        Color lightBlue = new Color(51, 204, 255);
        super.setColor(lightBlue);
    }

    public void affectTile(Polygon polygon) {
        double precipitation = polygon.getPrecipitation();
        polygon.setPrecipitation(precipitation + 50);
    }
}
