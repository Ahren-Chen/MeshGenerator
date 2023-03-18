package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class TerrainTile extends Polygon implements Tile<Polygon> {
    public TerrainTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.WHITE);
    }

    public void affectTile(Polygon polygon) {    }

    @Override
    public void calculateColor() {
        if (this.precipitation == 0 && this.temperature == 0) {
            this.setColor(Color.YELLOW);
        }
        else {
            this.setColor(Color.WHITE);
        }
    }
}
