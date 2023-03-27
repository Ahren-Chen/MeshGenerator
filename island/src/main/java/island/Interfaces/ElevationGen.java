package island.Interfaces;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.Map;

public interface ElevationGen {
    void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption, double max_x, double max_y);
    void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption, double innerRadius, double outerRadius, double max_x, double max_y);

}
