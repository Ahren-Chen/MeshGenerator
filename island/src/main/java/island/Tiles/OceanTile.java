package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.awt.*;
import java.util.List;

public class OceanTile extends Polygon {
    public OceanTile (List<Segment> segments, Vertex centroid, int ID) {
        super(segments, centroid, ID);
        super.setColor(Color.BLUE);
    }
}