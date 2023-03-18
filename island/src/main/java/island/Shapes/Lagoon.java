package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Interfaces.Tile;
import island.Tiles.LagoonTile;
import island.Tiles.OceanTile;
import island.Tiles.TerrainTile;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lagoon implements ShapeGen {

    private double innerRadius;
    private double outerRadius;
    private double centerX;
    private double centerY;
    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh, double max_x, double max_y) {
        logger.trace("Generating lagoon");
        centerX = max_x/2;
        centerY = max_y/2;

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        Map<Integer, Vertex> vertexMap = ConvertFromStructs.convert(structsVertexList);
        Map<Integer, Segment> segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        Map<Integer, Polygon> polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);

        Map<Polygon, Polygon> polygonTileMap = new HashMap<>();
        Map<Integer, Polygon> tileMap = new HashMap<>();

        if (max_x <= max_y) {
            innerRadius = max_x/5;
            outerRadius = max_x * 2/5;
        }
        else {
            innerRadius = max_y/5;
            outerRadius = max_y * 2/5;
        }

        for (Polygon polygon : polygonMap.values()) {
            Vertex centroid = polygon.getCentroid();

            Polygon poly;
            if (withinInnerCircle(centroid)) {
                poly = new LagoonTile(polygon);

            }
            else if (withinOuterCircle(centroid)) {
                poly = new TerrainTile(polygon);
            }
            else {
                poly = new OceanTile(polygon);
            }

            polygonTileMap.put(polygon, poly);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        setNeighbours(polygonTileMap, tileMap);

        /*Map<Polygon, Object> polygonTileTypeMap = new HashMap<>();

        List<Polygon> oceanTileList = new ArrayList<>();
        for (Polygon polygon : polygonMap.values()) {
            Polygon poly = new OceanTile(polygon);
            oceanTileList.add(poly);
            polygonTileTypeMap.put(polygon, poly.getClass());
        }

        logger.error(oceanTileList.get(0).getClass() + "");
        */

        List<Structs.Polygon> tileList = new ArrayList<>();
        for (Polygon tile : tileMap.values()) {
            tileList.add(tile.convertToStruct());
        }

        return Mesh
                .newBuilder()
                .addAllVertices(structsVertexList)
                .addAllSegments(structsSegmentList)
                .addAllPolygons(tileList)
                .build();
    }

    private Map<Integer, Polygon> fillLagoon(Shape shape, Map<Integer, Polygon> polygonMap, Map<Polygon, Object> polygonTileTypeMap) {
        Map<Integer, Polygon> lagoonMap = new HashMap<>();

        for (Polygon polygon : polygonMap.values()) {
            Vertex centroid = polygon.getCentroid();
            Coordinate cords = centroid.getCords();
            int ID = polygon.getID();

            double x = cords.getX();
            double y = cords.getY();

            if (shape.contains(x, y)) {
                Polygon poly = new OceanTile(polygon);
                polygonTileTypeMap.put(polygon, poly.getClass());
                lagoonMap.put(ID, poly);
            }
        }
        return lagoonMap;
    }

    private boolean withinInnerCircle(Vertex point) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(innerRadius, 2);
    }

    private boolean withinOuterCircle(Vertex point) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(outerRadius, 2);
    }
    private void setNeighbours(Map<Polygon, Polygon> polygonTileMap, Map<Integer, Polygon> tileMap) {
        for (Polygon tile : tileMap.values()) {
            List<Polygon> neighbors = tile.getNeighbours();
            List<Polygon> newNeighbors = new ArrayList<>();

            for (Polygon polygonNeighbor : neighbors) {
                Polygon tileNeighbor = polygonTileMap.get(polygonNeighbor);

                tileNeighbor.affectTile(tile);
                newNeighbors.add(tileNeighbor);
            }

            tile.setNeighbours(newNeighbors);
        }
    }
}
