package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import ca.mcmaster.cas.se2aa4.a2.generator.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.Segment;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PolygonNeighbourFinder {
    /**
     *  This method takes in a list of polygons, a list of line segments, and an integer len.
     *  It iterates over each polygon and checks whether any other polygons share a complete set of line segments with it (meaning they are neighbors).
     *  It adds the index of any neighboring polygons to an ArrayList of neighbor indices.
     * @param Polygons  a List of polygons
     */
    public static void set_NeighborGrid(List<Polygon> Polygons){
        List<Polygon> neighbour_list = new ArrayList<>();
        for (int i = 0; i < Polygons.size();i++){
            for (Polygon polygon : Polygons) {
                if (if_neighbor(polygon, Polygons.get(i))) {
                    neighbour_list.add(polygon);
                }
            }
            Polygons.get(i).setNeighbors(neighbour_list);
            neighbour_list.clear();
        }
    }

    private static boolean if_neighbor(Polygon p1, Polygon p2){
        for (int i = 0; i < p1.getSegments().size(); i++) {
            for (int j = 0; j < p2.getSegments().size(); j++) {

                if(p1.getSegments().get(i).compare(p2.getSegments().get(j))){
                    return true;
                }
            }
        }
        return false;
    }
    public static ArrayList<Segment> bonus_segment(List<Polygon> polygons){
        ArrayList<Segment> segments = new ArrayList<>();
        for (Polygon p:polygons) {
            Vertex Centroid = p.getCentroid();
            for (int i = 0; i < p.getSegments().size(); i++) {
                List<Vertex> vertices = remove(p.getSegments());
                for (int j = 0; j < vertices.size(); j++) {
                    segments.add(new Segment(p.getCentroid(),vertices.get(j),p.getDefaultThickness()));
                }
            }
        }
        return segments;
    }
    public static List<Vertex> remove(List<Segment> segments){
        ArrayList<Vertex> temp = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            temp.add(segments.get(i).getVertice1());
            temp.add(segments.get(i).getVertice2());
        }
        vertices = temp.stream().distinct().collect(Collectors.toList());
        return vertices;
    }
}
