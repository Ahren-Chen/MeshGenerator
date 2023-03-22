package island.river;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;

import java.awt.*;

public class river extends Segment {
    river(Segment segment){
        super(segment.getV1(),segment.getV2(),segment.getThickness(),segment.getID());
        Color riverBlue = new Color(0, 21, 128);
        super.setcolor(riverBlue);


    }
    public void affectTile(Polygon polygon) {
        double precipitation = polygon.getPrecipitation();
        polygon.setPrecipitation(precipitation + 20);
    }



}
