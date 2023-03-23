package island.river;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.ArrayList;
import java.util.List;

public class River  {

    private static final ParentLogger logger = new ParentLogger();

    private double thickness;

    List<Segment> whole_river = new ArrayList<>();


    private River(Polygon p){
        this.thickness = p.getSegments().get(0).getThickness();
        find_through_lowest(p,p.getSegments().get(1).getV1());
        // still debt
    }
    private void find_through_lowest(Polygon polygon ,Vertex last){
        last.setIfRiver(true);
        List<Polygon> neighbors  = polygon.getNeighbours();
        for (Polygon neighbor:neighbors) {
            List<Segment> segments = neighbor.sort_base_elevation();
            for (Segment segment:segments) {
                if(segment.getV1().compareTo(last)==1&&segment.getV1().getElevation()>segment.getV2().getElevation()){

                    whole_river.add(segment);
                    last = segment.getV2();
                    affectTile(segment,neighbor);
                    find_through_lowest(neighbor,last);
                }
                if(segment.getV2().compareTo(last)==1&&segment.getV1().getElevation()< segment.getV2().getElevation()){
                    whole_river.add(segment);
                    last = segment.getV1();
                    affectTile(segment,neighbor);
                    find_through_lowest(neighbor,last);
                }
            }

        }
    }

    private void findRiver(Polygon polygon, Vertex last) {

    }
    private boolean ifMerge(Segment s){

        return true;
    }
    private void merge(){

    }






    public void affectTile(Segment segment, Polygon polygon ) {
        List<Polygon> moist = polygon.getNeighbours();
        for (Polygon p :moist) {
            for (Segment s:p.getSegments()) {
                if(s.compareTo(segment)==1){
                    double precipitation = polygon.getPrecipitation();
                    polygon.setPrecipitation(precipitation + 20);
                    break;
                }

            }
        }

    }









}
