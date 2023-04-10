package island.CitiesGen;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.*;

public class CityVertexSegmentFilter {
    private final Set<Vertex> viableVerticesSet;
    private final Set<Segment> viableSegmentSet;
    public CityVertexSegmentFilter (Map<Integer, Polygon> polygons) {
        viableVerticesSet = new HashSet<>();
        viableSegmentSet = new HashSet<>();

        //For every polygon in the mesh
        for (Polygon polygon : polygons.values()) {

            //Only add the vertices if it is not water
            if (! polygon.getIsWater()) {

                //Add all vertices by accessing them through the segments
                for (Segment segment : polygon.getSegments()) {
                    viableVerticesSet.add(segment.getV1());
                    viableVerticesSet.add(segment.getV2());
                    viableSegmentSet.add(segment);
                }
            }
        }
    }

    public List<Vertex> getViableVerticesSet() {
        return new ArrayList<>(viableVerticesSet);
    }

    public List<Segment> getViableSegmentSet() {
        return new ArrayList<>(viableSegmentSet);
    }
}
