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
        private final Color color =  new Color(255, 0, 0);//currently is red therefor we can find the river very easily
        private double thickness ;



        List<Segment> whole_river = new ArrayList<>();


        public  River(Polygon p) {
            this.thickness = p.getSegments().get(0).getThickness();
            // still debt

        }

    public List<Segment> formRiver(Polygon polygon ) {
        Polygon current = polygon;
        Polygon next = current.sort_base_elevation().get(0);
        if(!next.getIsWater()&&next.getElevation()<current.getElevation()){
            Vertex v1 = current.getCentroid();
            Vertex v2 = current.getCentroid();
            add_river1(v1,v2);
            formRiver(next);
            System.out.println("we are making river ");
        }
        return whole_river;
    }

    private void add_river1(Vertex v1 , Vertex v2){
        Segment s = new Segment(v1, v2, thickness,0);
        s.setColor(color);
        this.whole_river.add(s);
    }

}
