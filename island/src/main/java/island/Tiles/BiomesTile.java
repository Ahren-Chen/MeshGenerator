package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class BiomesTile extends Polygon implements Tile<Polygon> {

    public BiomesTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.WHITE);
        this.temperature = 20;
        this.precipitation = 200;
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
