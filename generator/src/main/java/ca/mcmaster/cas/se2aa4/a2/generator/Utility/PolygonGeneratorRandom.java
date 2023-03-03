package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.Segment;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.*;

/**
 * This Utility class is responsible for generating random Polygons given a list of points
 */
public class PolygonGeneratorRandom {
    private static final ParentLogger logger = new ParentLogger();

    /**
     * This method will take in a {@code Map} of {@code Coordinate} to their {@code Vertex} and
     * generate a {@code List<Polygon>} made using Voronoi Diagram Builder and Convex Hull.
     * @param vertices A {@code Map} of {@code Coordinate} to their {@code Vertex}
     * @param maxSize A {@code Coordinate} containing the information for the maximum size of the mesh
     * @param vertexThickness A {@code double} that gives information on how big the vertex thickness should be
     * @param segmentThickness A {code double} that gives information on how big the segment thickness should be
     * @return {@code List<Polygon>}
     */
    public static List<Polygon> generatePolyRandom(Map<Coordinate, Vertex> vertices, Coordinate maxSize, double vertexThickness, double segmentThickness) {
        // Generate count number of polygons using the given vertices
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        List<Polygon> polygonList = new ArrayList<>();
        ConvexHull convexHullPolygon;

        //Start to create the voronoi diagram
        List<Coordinate> sites = new ArrayList<>(vertices.keySet());

        logger.error(sites.size() + "");
        //Create en envelope for the lower bound of the mesh
        Envelope envelope = new Envelope(new Coordinate(0, 0), maxSize);
        voronoi.setSites(sites);
        voronoi.setTolerance(0.01);
        voronoi.setClipEnvelope(envelope);

        //Create a geometry factory to get the voronoi diagram
        PrecisionModel precision = new PrecisionModel(100);

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

        //For every polygon geometry taken from the voronoi diagram
        for (int i = 0; i < polygonsGeometry.getNumGeometries(); i++) {
            //Get the polygon and convex hull it before mapping the polygon to its original start point
            Geometry polygonGeo = polygonsGeometry.getGeometryN(i);
            Object parentVertexCords = polygonGeo.getUserData();

            convexHullPolygon = new ConvexHull(polygonGeo);

            polygonGeo = convexHullPolygon.getConvexHull();
            polygonToParentCords.put(polygonGeo, (Coordinate) parentVertexCords);
        }

        //For every polygon geometry
        for (Geometry polygon : polygonToParentCords.keySet()) {
            polygonSegmentList.clear();
            polygonCoordinateList_Unique.clear();

            //Add the unique vertex coordinates to a List
            for (int coords = 0; coords < polygon.getCoordinates().length; coords++) {
                coordinate = polygon.getCoordinates()[coords];
                modifyCoords(coordinate, maxSize);

                if (! polygonCoordinateList_Unique.contains(coordinate) || coords == polygon.getCoordinates().length - 1) {
                    polygonCoordinateList_Unique.add(coordinate);
                }
            }

            //Then for each vertex
            for (int coords = 0; coords < polygonCoordinateList_Unique.size() - 1; coords++) {

                //Find the 2 vertices that need to be connected with a segment
                Coordinate verticesCoords = polygonCoordinateList_Unique.get(coords);
                Coordinate verticesCoords2 = polygonCoordinateList_Unique.get(coords+1);

                //If the vertex has not been used before, create a new vertex for it and map it
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

                //Removing identical segments
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
                //Add the segment to the polygon
                polygonSegmentList.add(polygonSegment);
            }

            //Create a polygon with all the information
            Polygon p = new Polygon(polygonSegmentList, vertexThickness, segmentThickness);
            polygonList.add(p);
        }

        return polygonList;
    }

    /**
     * This method takes in a coordinate and with (0, 0) as the minimum, and maxSize as the maximum,
     * it will turn the coordinates into something that is within the designated size.
     * @param coords {@code Coordinate} that you want to modify to fit the mesh
     * @param maxSize {@code Coordinate} that constrains the maximum values of the coordinate
     */
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
