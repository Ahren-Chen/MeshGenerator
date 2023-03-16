package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Tiles.OceanTile;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lagoon implements ShapeGen {
    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh) {
        logger.trace("Generating lagoon");

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        Map<Integer, Vertex> vertexMap = ConvertFromStructs.convert(structsVertexList);
        Map<Integer, Segment> segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        Map<Integer, Polygon> polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);

        Map<Polygon, Object> polygonTileTypeMap = new HashMap<>();

        List<Polygon> oceanTileList = new ArrayList<>();
        for (Polygon polygon : polygonMap.values()) {
            Polygon poly = new OceanTile(polygon);
            oceanTileList.add(poly);
            polygonTileTypeMap.put(polygon, poly.getClass());
        }

        logger.error(oceanTileList.get(0).getClass() + "");


        List<Structs.Polygon> tileList = new ArrayList<>();
        for (Polygon tile : oceanTileList) {
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

    private void setNeighbours() {

    }
}
