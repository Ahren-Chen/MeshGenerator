package island.CitiesGen;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.*;

public class CityVertexFilter {
    private final Set<Vertex> viableVerticesSet = new HashSet<>();
    /**

     The CityVertexFilter class is used to filter out water vertices from a mesh of polygons and segments.

     @param polygons A Map of polygons.
     */
    public CityVertexFilter(Map<Integer, Polygon> polygons) {
        if (polygons == null) {
            return;
        }

        //For every polygon in the mesh
        for (Polygon polygon : polygons.values()) {

            //Only add the vertices if it is not water
            if (! polygon.getIsWater()) {

                //Add all vertices by accessing them through the segments
                for (Segment segment : polygon.getSegments()) {
                    viableVerticesSet.add(segment.getV1());
                    viableVerticesSet.add(segment.getV2());
                }
            }
        }
    }

    /**
     Returns a List of Vertex that are viable to create cities on.
     @return A {@code List} of Vertex.
     */
    public List<Vertex> getViableVerticesSet() {
        return new ArrayList<>(viableVerticesSet);
    }
}
