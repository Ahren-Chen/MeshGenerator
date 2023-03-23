package island.river;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class River  {

    private static final ParentLogger logger = new ParentLogger();

    private final Color color =  new Color(255, 0, 0);//currently is red therefor we can find the river very easily

    private double thickness;


    List<Segment> whole_river = new ArrayList<>();


    private boolean River(Polygon p){
        this.thickness = p.getSegments().get(0).getThickness();
        find_through_lowest(p,p.getSegments().get(1).getV1());
        // still debt
        return if_endOcean();
    }
    private void find_through_lowest(Polygon polygon ,Vertex last){
        last.setIfRiver(true);
        List<Polygon> neighbors  = polygon.getNeighbours();
        for (Polygon neighbor:neighbors) {
            List<Segment> segments = neighbor.sort_base_elevation();
            for (Segment segment:segments) {
                if(segment.getV1().compareTo(last)==1&&segment.getV1().getElevation()<segment.getV2().getElevation()){
                    if(ifMerge(segment)){
                        merge();
                    }
                    add_river(segment);
                    last = segment.getV2();
                    affectTile(segment,neighbor);
                    find_through_lowest(neighbor,last);
                }
                if(segment.getV2().compareTo(last)==1&&segment.getV1().getElevation() > segment.getV2().getElevation()){
                    if(ifMerge(segment)){
                        merge();
                    }
                    add_river(segment);
                    last = segment.getV1();
                    affectTile(segment,neighbor);
                    find_through_lowest(neighbor,last);
                }
            }

        }

    }
    private boolean if_endOcean(){
        this.whole_river.size();
        return false;
    }
    private void add_river(Segment s){
        s.setColor(this.color);
        s.setThickness(this.thickness);
        this.whole_river.add(s);

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
