package island.Converters;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.IOEncapsulation.Polygon;
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
    private static final ParentLogger logger = new ParentLogger();
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

    public static Map<Integer, Polygon> convert (List<Structs.Polygon> structsPolygonList,
                                                 Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap) {
        Map<Integer, Polygon> polygonMap = new HashMap<>();
        for (int polygonIdx = 0; polygonIdx < structsPolygonList.size(); polygonIdx++) {
            Structs.Polygon polygon = structsPolygonList.get(polygonIdx);

            int centroidIdx = polygon.getCentroidIdx();
            Vertex centroid = vertexMap.get(centroidIdx);

            List<Integer> segmentIdxList = polygon.getSegmentIdxsList();
            List<Segment> polygonSegments = new ArrayList<>();
            for (Integer idx : segmentIdxList) {

                Segment segment = segmentMap.get(idx);
                polygonSegments.add(segment);
            }

            island.IOEncapsulation.Polygon newPolygon = new island.IOEncapsulation.Polygon(polygonSegments, centroid, polygonIdx);

            polygonMap.put(polygonIdx, newPolygon);
        }
        setPolygonNeighbor(structsPolygonList, polygonMap);

        return polygonMap;
    }

    private static void setPolygonNeighbor (List<Structs.Polygon> structsPolygonList, Map<Integer, Polygon> polygonMap) {
        List<Integer> polygonNeighborIdx;
        Polygon currentPolygon;
        Polygon neighborPolygon;
        Structs.Polygon polygon;

        for (int polygonIdx = 0; polygonIdx < structsPolygonList.size(); polygonIdx++) {
            polygon = structsPolygonList.get(polygonIdx);

            polygonNeighborIdx = polygon.getNeighborIdxsList();
            //logger.error(polygonMap.size() + " " + structsPolygonList.size());

            currentPolygon = polygonMap.get(polygonIdx);

            List<Polygon> neighbors = new ArrayList<>();
            for (int neighborIdx : polygonNeighborIdx) {
                neighborPolygon = polygonMap.get(neighborIdx);

                neighbors.add(neighborPolygon);
            }
            currentPolygon.setNeighbours(neighbors);
            //polygonMap.put(polygonIdx, currentPolygon);
        }

    }
}
