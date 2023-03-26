package island.River2;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

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
        current.getCentroid().setIfRiver(true);
        Polygon next = current.sort_base_elevation().get(0);
        Polygon temp;
        while(!next.getIsWater()&&next.getElevation()<current.getElevation()){
            Vertex v1 = current.getCentroid();
            Vertex v2 = next.getCentroid();
            System.out.println("we are making river ");
            next.getCentroid().setIfRiver(true);

            temp = next;
            next = next.sort_base_elevation().get(0);
            current = temp;

        }
        Vertex v1 = current.getCentroid();
        Vertex v2 = next.getCentroid();
        return next.getIsWater();
    }

    public List<Segment> formRiverWhile(Polygon polygon){
        double thickness = polygon.getSegments().get(0).getThickness();
        Polygon current = polygon;
        current.getCentroid().setIfRiver(true);
        Polygon next = current.sort_base_elevation().get(0);
        Polygon temp;
        while(!next.getIsWater()&&next.getElevation()<current.getElevation()){
            if(next.getCentroid().getIfRiver()){
                thickness = merge(thickness);
                Vertex v1 = current.getCentroid();
                Vertex v2 = next.getCentroid();
                add_river1(v1,v2,thickness);
                System.out.println("we are merging river ");
                //next.getCentroid().setIfRiver(true);
                affectTile(next,thickness);
                temp = next;
                next = next.sort_base_elevation().get(0);
                current = temp;
            }
            Vertex v1 = current.getCentroid();
            Vertex v2 = next.getCentroid();
            add_river1(v1,v2,thickness);
            System.out.println("we are making river ");
            //next.getCentroid().setIfRiver(true);
            affectTile(next,thickness);
            temp = next;
            next = next.sort_base_elevation().get(0);
            current = temp;
        }
        Vertex v1 = current.getCentroid();
        Vertex v2 = next.getCentroid();
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
    private double merge(Double thickness){
        return thickness = 2 * thickness;
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

