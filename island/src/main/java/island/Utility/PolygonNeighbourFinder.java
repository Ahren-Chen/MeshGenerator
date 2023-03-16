package island.Utility;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This Utility class takes in a List of Polygons and finds the neighbouring polygons within the mesh
 */
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
                    neighbour_list.add(Polygons.get(i));
                }
            }
            Polygons.get(i).setNeighbors(neighbour_list);
            neighbour_list=new ArrayList<>();
        }
    }

    /**
     * This method will determine whether {@code Polygon} p1 is a neighbour of {@code Polygon} p2 in a grid mesh
     * @param p1 {@code Polygon}
     * @param p2 {@code Polygon}
     * @return {@code boolean}
     */
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

    /**
     * This method takes in a List of Polygons and generates the needed segments for the Tetrakis square
     * @param polygons {@code List<Polygon>}
     * @return {@code ArrayList<Segment>}
     */
    public static List<Segment> bonus_segment(List<Polygon> polygons){
        List<Segment> segments = new ArrayList<>();
        List<Vertex> vertices;
        for (Polygon p:polygons) {
            for (int i = 0; i < p.getSegments().size(); i++) {
                vertices = remove(p.getSegments());
                for (Vertex vertex : vertices) {
                    segments.add(new Segment(p.getCentroid(), vertex, p.getSegmentThickness()));
                }
            }
        }
        return segments;
    }

    /**
     * This method gets the vertices from a list of segments
     * @param segments {@code List<Segment>}
     * @return {@code List<Vertex>}
     */
    private static List<Vertex> remove(List<Segment> segments){
        Set<Vertex> temp = new HashSet<>();
        List<Vertex> vertices;
        for (Segment segment : segments) {
            temp.add(segment.getVertice1());
            temp.add(segment.getVertice2());
        }
        vertices = temp.stream().distinct().collect(Collectors.toList());
        return vertices;
    }

    /**
     * This method takes in a List of polygons and calculates the neighbours based on DelaunayTriangulationBuilder
     * @param polygonList {@code List<Polygon>}
     * @param accuracy {@code double}
     */
    public static void findPolygonNeighbours_Random(List<Polygon> polygonList, double accuracy) {
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        Map<Coordinate, Vertex> centroidCordsToVertex = new HashMap<>();
        Map<Vertex, Polygon> centroidToPolygon = new HashMap<>();

        //For each polygon, get its centroid and map it to its coordinates
        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();
            Coordinate cord = new Coordinate(centroid.getX(), centroid.getY());

            centroidCordsToVertex.put(cord, centroid);
            centroidToPolygon.put(centroid, poly);
        }

        //Get the triangulation
        PrecisionModel precisionModel = new PrecisionModel(accuracy);
        GeometryFactory triangulationFactory = new GeometryFactory(precisionModel);

        triangulationBuilder.setSites(centroidCordsToVertex.keySet());
        triangulationBuilder.setTolerance(accuracy);
        Geometry triangles = triangulationBuilder.getTriangles(triangulationFactory);

        Map<Vertex, Set<Vertex>> VertexNeighbours = new HashMap<>();
        Set<Vertex> neighbours = new HashSet<>();

        //For each triangle in the resulting diagram
        for (int triangleNum = 0; triangleNum < triangles.getNumGeometries(); triangleNum++) {
            Geometry triangle = triangles.getGeometryN(triangleNum);

            Coordinate c1 = triangle.getCoordinates()[0];
            Coordinate c2 = triangle.getCoordinates()[1];
            Coordinate c3 = triangle.getCoordinates()[2];

            Vertex v1 = centroidCordsToVertex.get(c1);
            Vertex v2 = centroidCordsToVertex.get(c2);
            Vertex v3 = centroidCordsToVertex.get(c3);

            //I map each vertex to its neighbours as Sets to there will not be duplicates
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

        List<Polygon> polygonNeighbors;
        //For each polygon, I set its neighbours as the neighbouring polygons centroids
        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();

            Set<Vertex> centroidNeighboursSet = VertexNeighbours.get(centroid);

            polygonNeighbors = new ArrayList<>();
            for (Vertex vertex : centroidNeighboursSet) {
                Polygon polygon = centroidToPolygon.get(vertex);
                polygonNeighbors.add(polygon);
            }

            poly.setNeighbours(polygonNeighbors);
        }
    }
}
