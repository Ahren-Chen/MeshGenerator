package island.Interfaces;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.Map;

public interface ElevationGen {
    void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption);

}
