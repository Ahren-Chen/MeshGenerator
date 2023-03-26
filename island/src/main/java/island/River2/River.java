package island.River2;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class River {


    private static final ParentLogger logger = new ParentLogger();
    private final Color color =  Color.BLUE;



    List<Segment> whole_river = new ArrayList<>();


    public  River() {

        // still debt
    }
    public boolean formRiverboolean(Polygon polygon){
        Polygon current = polygon;
        //current.getCentroid().setIfRiver(true);
        Polygon next = current.sort_base_elevation().get(0);
        Polygon temp;
        while(!next.getIsWater()&&next.getElevation()<current.getElevation()){
            temp = next;
            next = next.sort_base_elevation().get(0);
            current = temp;

        }
        return (next.getClass().equals(OceanTile.class) || next.getClass().equals((LakeTile.class)));
    }

    public List<Segment> formRiverWhile(Polygon polygon){
        double thickness = polygon.getSegments().get(0).getThickness();
        Polygon current = polygon;
        //current.getCentroid().setIfRiver(true);
        Polygon next = current.sort_base_elevation().get(0);
        Polygon temp;
        current.getCentroid().setRiverThickness(thickness);
        while(!next.getIsWater()&&next.getElevation()<current.getElevation()){
            Vertex v1 = current.getCentroid();
            Vertex v2 = next.getCentroid();

            if(v1.getIfRiver() && v1.getRiverThickness() >= thickness){
                thickness = merge(thickness, v1);
            }
            add_river1(v1,v2,thickness);
            logger.trace("a new river has been created");

            v1.setIfRiver(true);
            v1.setRiverThickness(thickness);
            //affectTile(next,thickness);
            temp = next;
            next = next.sort_base_elevation().get(0);
            current = temp;
            //next.getCentroid().setIfRiver(true);
        }

        Vertex v1 = current.getCentroid();
        Vertex v2 = next.getCentroid();

        if(current.getCentroid().getIfRiver() && v1.getRiverThickness() >= thickness){
            thickness = merge(thickness, v1);
        }

        v2.setIfRiver(true);
        v1.setIfRiver(true);
        v1.setRiverThickness(thickness);

        add_river1(v1, v2,thickness);
        return whole_river;
    }

    public List<Segment> getWhole_river(){
        return whole_river;
    }

    private void add_river1(Vertex v1 , Vertex v2, Double thickness){
        Segment s = new Segment(v1, v2, thickness, 0);
        s.setColor(color);
        whole_river.add(s);
    }
    private double merge(Double thickness, Vertex v){
        return v.getRiverThickness() + thickness;
    }
    private void affectTile( Polygon polygon , Double thickness ) {
        List<Polygon> moist = polygon.getNeighbours();
        for (Polygon p :moist) {
            double precipitation = polygon.getPrecipitation();
            polygon.setPrecipitation(precipitation + 200*thickness);
            break;
        }

    }


}

