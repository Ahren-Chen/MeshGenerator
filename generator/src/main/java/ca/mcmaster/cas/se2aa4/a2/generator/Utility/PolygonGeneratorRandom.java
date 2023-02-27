package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.Segment;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.*;

public class PolygonGeneratorRandom {
    private static final ParentLogger logger = new ParentLogger();

    public static List<Polygon> generatePolyRandom(Map<Coordinate, Vertex> vertices, Coordinate maxSize, double vertexThickness, double segmentThickness) {
        // Generate count number of polygons using the given vertices
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        List<Polygon> polygonList = new ArrayList<>();
        ConvexHull convexHullPolygon;

        List<Coordinate> sites = new ArrayList<>(vertices.keySet());

        Envelope envelope = new Envelope(new Coordinate(0, 0), maxSize);
        voronoi.setSites(sites);
        voronoi.setTolerance(0.01);
        voronoi.setClipEnvelope(envelope);

        PrecisionModel precision = new PrecisionModel(0.01);

        GeometryFactory geomFact = new GeometryFactory(precision);

        Geometry polygonsGeometry;
        try {
            polygonsGeometry = voronoi.getDiagram(geomFact);
        }
        catch (TopologyException ex) {
            throw new RuntimeException("TopologyException with voronoi diagram, trying again");
        }

        TreeSet<Segment> segmentSet= new TreeSet<>(); // keep track of segments to deregister duplicates

        List<Segment> polygonSegmentList = new ArrayList<>();
        List<Coordinate> polygonCoordinateList_Unique = new ArrayList<>();
        Map<Geometry, Coordinate> polygonToParentCords = new HashMap<>();
        Coordinate coordinate;

        for (int i = 0; i < polygonsGeometry.getNumGeometries(); i++) {
            Geometry polygonGeo = polygonsGeometry.getGeometryN(i);
            Object parentVertexCords = polygonGeo.getUserData();

            convexHullPolygon = new ConvexHull(polygonGeo);

            polygonGeo = convexHullPolygon.getConvexHull();
            polygonToParentCords.put(polygonGeo, (Coordinate) parentVertexCords);
        }

        for (Geometry polygon : polygonToParentCords.keySet()) {
            polygonSegmentList.clear();
            polygonCoordinateList_Unique.clear();

            for (int coords = 0; coords < polygon.getCoordinates().length; coords++) {
                coordinate = polygon.getCoordinates()[coords];
                modifyCoords(coordinate, maxSize);

                if (! polygonCoordinateList_Unique.contains(coordinate) || coords == polygon.getCoordinates().length - 1) {
                    polygonCoordinateList_Unique.add(coordinate);
                }
            }

            for (int coords = 0; coords < polygonCoordinateList_Unique.size() - 1; coords++) {
                Coordinate verticesCoords = polygonCoordinateList_Unique.get(coords);
                Coordinate verticesCoords2 = polygonCoordinateList_Unique.get(coords+1);

                if (! coordinateVertexMap.containsKey(verticesCoords)) {

                    Vertex v;
                    v = new Vertex(verticesCoords.getX(), verticesCoords.getY(),
                            false, vertexThickness, RandomColor.randomColorDefault());

                    coordinateVertexMap.put(verticesCoords, v);
                }

                if (! coordinateVertexMap.containsKey(verticesCoords2)) {
                    Vertex v;
                    v = new Vertex(verticesCoords2.getX(), verticesCoords2.getY(),
                            false, vertexThickness, RandomColor.randomColorDefault());
                    coordinateVertexMap.put(verticesCoords2, v);
                }

                Vertex v1 = coordinateVertexMap.get(verticesCoords);
                Vertex v2 = coordinateVertexMap.get(verticesCoords2);
                if (v1.compareTo(v2) == 0) {
                    logger.error("identical vertices");
                    logger.error(polygonCoordinateList_Unique + "");
                }
                if (verticesCoords.equals(verticesCoords2)) {
                    logger.error("Identical coordinates");
                }

                //this is a dumb fix for identical segment removed in list but ID not added
                Segment polygonSegment = new Segment(v1, v2, segmentThickness);
                if(!segmentSet.contains(polygonSegment)){
                    segmentSet.add(polygonSegment);
                }
                else{
                    for (Segment s: segmentSet) {
                        if(s.compareTo(polygonSegment)==0){
                            polygonSegment=s;
                            break;
                        }
                    }
                }
                polygonSegmentList.add(polygonSegment);
            }

            Polygon p = new Polygon(polygonSegmentList, vertexThickness, segmentThickness);
            polygonList.add(p);
        }

        return polygonList;
    }

    private static void modifyCoords(Coordinate coords, Coordinate maxSize) {
        if (coords.getX() < 0) {
            coords.setX(0);
        }
        else if (coords.getX() > maxSize.getX()){
            coords.setX(maxSize.getX());
        }

        if (coords.getY() < 0) {
            coords.setY(0);
        }
        else if (coords.getY() > maxSize.getY()) {
            coords.setY(maxSize.getY());
        }
    }
}
