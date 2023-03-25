package island.river;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Tiles.BiomesTile;

import java.awt.*;
import java.util.*;
import java.util.List;

public class River  {

    private static final ParentLogger logger = new ParentLogger();
    private final Color color =  new Color(255, 0, 0);//currently is red therefor we can find the river very easily
    private double thickness;

    private Polygon end_tile;


    List<Segment> whole_river = new ArrayList<>();


    public  River(Polygon p) {
        this.thickness = p.getSegments().get(0).getThickness();
        // still debt

    }



    public List<Segment> formRiver(Polygon polygon ) {
            Polygon current = polygon;
            List<Polygon> neighbors  = polygon.sort_base_elevation();
            Polygon next = neighbors.get(0);
            Polygon temp;
            while(!next.getIsWater()){
                Vertex v1 = current.getCentroid();
                Vertex v2 = next.getCentroid();
                if(v2.getIfRiver()){
                    merge();
                    add_river1(v1,v2);
                    temp = next;
                    next = next.sort_base_elevation().get(0);
                    current = temp;
                }
                else{
                    v2.setIfRiver(true);
                    add_river1(v1,v2);
                    temp = next;
                    next = next.sort_base_elevation().get(0);
                    current = temp;
                }
            }
            return whole_river;

            }



    public void findRiver(Polygon polygon, Vertex last, double thickness) {
        //last.setIfRiver(true);
        List<Polygon> neighbors = polygon.getNeighbours();
        Set<Segment> segmentsContainingVertex = new HashSet<>();
        List<Segment> segmentsUpwards = new ArrayList<>();
        Map<Segment, Polygon> segmentPolygonMap = new HashMap<>();

        if (thickness <= 0) {return; }

        for (Polygon neighbor : neighbors) {
            List<Segment> segments = neighbor.getSegments();

            for (Segment segment : segments) {
                if (segment.containsVertex(last) && !segment.isRiver()) {
                    segmentsContainingVertex.add(segment);
                    segmentPolygonMap.put(segment, neighbor);
                }
            }
        }

        for (Segment segment : segmentsContainingVertex) {
            Vertex next;
            if (segment.getV1().compareTo(last) == 0) {
                next = segment.getV2();
            }
            else {
                next = segment.getV1();
            }

            double elevation1 = last.getElevation();
            double elevation2 = next.getElevation();

            if (elevation1 < elevation2) {
                segmentsUpwards.add(segment);
            }
        }

        int riverSplit = 0;
        if (segmentsUpwards.size() > 1) {
            riverSplit = Math.floorDiv((int) thickness, segmentsUpwards.size());
        }
        for (int segmentIdx = 0; segmentIdx < segmentsUpwards.size(); segmentIdx++) {
            Segment segment = segmentsUpwards.get(segmentIdx);

            Vertex next;
            if (segment.getV1().compareTo(last) == 0) {
                next = segment.getV2();
            }
            else {
                next = segment.getV1();
            }

            if (segmentIdx == segmentsUpwards.size() - 1) {

                add_river(segment, thickness);
                Polygon p = segmentPolygonMap.get(segment);
                findRiver(p, next, thickness);
            }
            else {
                thickness -= riverSplit;
                add_river(segment, thickness);

                Polygon p = segmentPolygonMap.get(segment);
                findRiver(p, next, thickness);
            }
        }
    }
    private boolean if_endOcean(){
        return end_tile.getNextToOcean();
    }
    private void add_river(Segment s, double thickness){
        s.setColor(this.color);
        s.setThickness(thickness);
        this.whole_river.add(s);
    }
    private void add_river1(Vertex v1 , Vertex v2){
        Segment s = new Segment(v1, v2, thickness,0);
        s.setColor(color);
        whole_river.add(s);
    }
    private boolean ifMerge(Segment s){
            if(s.getV2().getIfRiver()&&s.getV1().getIfRiver()){
                return true;
            }
        return false;
    }
    private void merge(){
        this.thickness = 2*thickness;
    }



    public void affectTile(Segment segment, Polygon polygon ) {
        List<Polygon> moist = polygon.getNeighbours();
        for (Polygon p :moist) {
            for (Segment s:p.getSegments()) {
                if(s.compareTo(segment)==1){
                    double precipitation = polygon.getPrecipitation();
                    polygon.setPrecipitation(precipitation + 200*thickness);
                    break;
                }

            }
        }

    }
}
