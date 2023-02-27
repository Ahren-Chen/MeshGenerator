package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import ca.mcmaster.cas.se2aa4.a2.generator.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.Segment;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class PolygonNeighbourFinder {
    /**
     *  This method takes in a list of polygons, a list of line segments, and an integer len.
     *  It iterates over each polygon and checks whether any other polygons share a complete set of line segments with it (meaning they are neighbors).
     *  It adds the index of any neighboring polygons to an ArrayList of neighbor indices.
     * @param Polygons  a List of polygons
     */
    public static void set_NeighborGrid(List<Polygon> Polygons){
        List<Vertex> neighbour_list = new ArrayList<>();
        Vertex centroid;
        for (int i = 0; i < Polygons.size();i++){
            for (Polygon polygon : Polygons) {
                if (if_neighbor(polygon, Polygons.get(i))) {
                    centroid = polygon.getCentroid();
                    neighbour_list.add(centroid);
                }
            }
            Polygons.get(i).setNeighbors(neighbour_list);
            neighbour_list=new ArrayList<>();
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
            for (int i = 0; i < p.getSegments().size(); i++) {
                List<Vertex> vertices = remove(p.getSegments());
                for (Vertex vertex : vertices) {
                    segments.add(new Segment(p.getCentroid(), vertex, p.getSegmentThickness()));
                }
            }
        }
        return segments;
    }
    private static List<Vertex> remove(List<Segment> segments){
        ArrayList<Vertex> temp = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();
        for (Segment segment : segments) {
            temp.add(segment.getVertice1());
            temp.add(segment.getVertice2());
        }
        vertices = temp.stream().distinct().collect(Collectors.toList());
        return vertices;
    }

    public static void findPolygonNeighbours_Random(List<Polygon> polygonList, double accuracy) {
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        Map<Coordinate, Vertex> centroidCordsToVertex = new HashMap<>();

        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();
            Coordinate cord = new Coordinate(centroid.getX(), centroid.getY());

            centroidCordsToVertex.put(cord, centroid);
        }

        PrecisionModel precisionModel = new PrecisionModel(accuracy);
        GeometryFactory triangulationFactory = new GeometryFactory(precisionModel);

        triangulationBuilder.setSites(centroidCordsToVertex.keySet());
        triangulationBuilder.setTolerance(accuracy);
        Geometry triangles = triangulationBuilder.getTriangles(triangulationFactory);

        Map<Vertex, Set<Vertex>> VertexNeighbours = new HashMap<>();
        Set<Vertex> neighbours = new HashSet<>();

        for (int triangleNum = 0; triangleNum < triangles.getNumGeometries(); triangleNum++) {
            Geometry triangle = triangles.getGeometryN(triangleNum);

            Coordinate c1 = triangle.getCoordinates()[0];
            Coordinate c2 = triangle.getCoordinates()[1];
            Coordinate c3 = triangle.getCoordinates()[2];

            Vertex v1 = centroidCordsToVertex.get(c1);
            Vertex v2 = centroidCordsToVertex.get(c2);
            Vertex v3 = centroidCordsToVertex.get(c3);

            if (VertexNeighbours.containsKey(v1)) {
                neighbours = VertexNeighbours.get(v1);
            }

            neighbours.add(v2);
            neighbours.add(v3);
            VertexNeighbours.put(v1, neighbours);

            neighbours = new HashSet<>();

            if (VertexNeighbours.containsKey(v2)) {
                neighbours = VertexNeighbours.get(v2);
            }

            neighbours.add(v1);
            neighbours.add(v3);
            VertexNeighbours.put(v2, neighbours);

            neighbours = new HashSet<>();

            if (VertexNeighbours.containsKey(v3)) {
                neighbours = VertexNeighbours.get(v3);
            }

            neighbours.add(v1);
            neighbours.add(v2);
            VertexNeighbours.put(v3, neighbours);

            neighbours = new HashSet<>();
        }

        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();

            Set<Vertex> centroidNeighboursSet = VertexNeighbours.get(centroid);
            List<Vertex> centroidNeighbours = centroidNeighboursSet.stream().toList();

            poly.setNeighbors(centroidNeighbours);
        }

    }
}
