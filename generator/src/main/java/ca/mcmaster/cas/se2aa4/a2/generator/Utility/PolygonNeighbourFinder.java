package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import ca.mcmaster.cas.se2aa4.a2.generator.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.Segment;

import java.util.ArrayList;
import java.util.List;

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
}
