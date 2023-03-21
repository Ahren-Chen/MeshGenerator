package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;

import java.util.*;

public class Lagoon implements ShapeGen {

    private double innerRadius;
    private double outerRadius;
    private double centerX;
    private double centerY;
    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh, double max_x, double max_y, int lakes, String aquifier) {
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


        Random bag = new Random();
        int seed = bag.nextInt(Integer.MAX_VALUE);

        if (max_x <= max_y) {
            innerRadius = max_x/6;
            outerRadius = max_x * 2/5;
        }
        else {
            innerRadius = max_y/6;
            outerRadius = max_y * 2/5;
        }
        
        
        for (int key = 0; key < polygonMap.size(); key++) {
            Polygon polygon = polygonMap.get(key);
            Vertex centroid = polygon.getCentroid();

            Polygon poly;
            if (withinInnerCircle(centroid)) {
                poly = new LakeTile(polygon);
            }
            else if (withinOuterCircle(centroid)) {

                if (isLake(seed, key, lakes)) {
                    poly = new LakeTile(polygon);
                    lakes--;
                }
                else {
                    poly = new BiomesTile(polygon);

                }

            }

            else {
                poly = new OceanTile(polygon);
            }

            polygonTileMap.put(polygon, poly);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        setNeighbours(polygonTileMap, tileMap);

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

            tile.calculateWhittakerColor();
            tile.setNeighbours(newNeighbors);
        }
    }

    private boolean isLake(int seed, int key, int lakesLeft) {
        seed = seed + key;

        seed = seed % 150;

        return seed < lakesLeft;
    }
}
