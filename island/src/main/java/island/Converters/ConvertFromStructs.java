package island.Converters;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.AbstractExtractor;
import island.PropertyExtractor;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertFromStructs {
    private static AbstractExtractor<Color, Float> properties;

    public static Map<Integer, Vertex> convert (List<Structs.Vertex> structsVertexList) {
        Map<Integer, Vertex> vertexMap = new HashMap<>();
        Coordinate cords;
        List<Structs.Property> structsProperties;
        Vertex newVertex;

        for (int vertexIdx = 0; vertexIdx < structsVertexList.size(); vertexIdx++) {
            Structs.Vertex vertex = structsVertexList.get(vertexIdx);
            double x = vertex.getX();
            double y = vertex.getY();
            cords = new Coordinate(x, y);

            structsProperties = vertex.getPropertiesList();

            properties = new PropertyExtractor(structsProperties);

            newVertex = new Vertex(
                    cords,
                    properties.isCentroid(),
                    properties.thickness(),
                    properties.color(),
                    vertexIdx);

            vertexMap.put(vertexIdx, newVertex);
        }

        return vertexMap;
    }

    public static Map<Integer, Segment> convert (List<Structs.Segment> structsSegmentList, Map<Integer, Vertex> vertexMap) {
        Map<Integer, Segment> segmentMap = new HashMap<>();
        Vertex v1;
        Vertex v2;
        List<Structs.Property> structsProperties;
        Segment newSegment;

        for (int segmentIdx = 0; segmentIdx < structsSegmentList.size(); segmentIdx++) {
            Structs.Segment structSegment = structsSegmentList.get(segmentIdx);

            int v1ID = structSegment.getV1Idx();
            int v2ID = structSegment.getV2Idx();

            v1 = vertexMap.get(v1ID);
            v2 = vertexMap.get(v2ID);

            structsProperties = structSegment.getPropertiesList();
            properties = new PropertyExtractor(structsProperties);

            newSegment = new Segment(v1, v2, properties.color(), properties.thickness(), segmentIdx);

            segmentMap.put(segmentIdx, newSegment);
        }

        return segmentMap;
    }
}
