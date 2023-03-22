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
    public Mesh generate(Mesh mesh, double max_x, double max_y, int lakes, int seed, int aquifier, int river, String elevation) {
        logger.trace("Generating lagoon");
        centerX = max_x/2;
        centerY = max_y/2;

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        Map<Integer, Vertex> vertexMap = ConvertFromStructs.convert(structsVertexList);
        Map<Integer, Segment> segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        Map<Integer, Polygon> polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);

        Map<Integer, Polygon> tileMap = new HashMap<>();
        
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

                List<Polygon> neighbors = polygon.getNeighbours();
                boolean nextToOcean = false;

                for (Polygon neighbor : neighbors) {
                    if (neighbor.getClass().equals(OceanTile.class)) {
                        nextToOcean = true;
                        break;
                    }
                }

                if (!nextToOcean) {
                    if (isLake(seed, key, lakes)) {
                        poly = new LakeTile(polygon);
                        lakes--;
                        if(river>0){
                            List<Polygon> lake_neighbor = polygon.getNeighbours();
                        }
                    }
                    else {
                        poly = new BiomesTile(polygon);
                        if (hasAquitifer(seed, key, aquifier)) {
                            ((BiomesTile) poly).setAquifer(true);
                            aquifier--;
                        }

                    }
                }
                else {
                    poly = new BiomesTile(polygon);
                }
            }
            else {
                poly = new OceanTile(polygon);
            }

            updateNeighbors(poly, polygon);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        affectNeighbors(tileMap);

        setElevation(vertexMap, segmentMap, tileMap, elevation);

        List<Structs.Polygon> tileList = new ArrayList<>();
        for (Polygon tile : tileMap.values()) {
            tileList.add(tile.convertToStruct());
        }

        System.out.println("Seed: " + seed);
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
    private boolean ifbetweenCircles(Vertex point) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(outerRadius, 2) &&
                (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) >= Math.pow(innerRadius, 2);
    }

    private double distanceToInnerCircle(Vertex point) {
        double x = point.getX();
        double y = point.getY();

        return Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) - innerRadius;
    }


    private void affectNeighbors(Map<Integer, Polygon> tileMap) {
        for (Polygon tile : tileMap.values()) {
            List<Polygon> neighbors = tile.getNeighbours();

            for (Polygon polygonNeighbor : neighbors) {
                polygonNeighbor.affectTile(tile);
            }

            tile.calculateWhittakerColor();
        }
    }
    private void updateNeighbors(Polygon polygonNew, Polygon polygonOld) {
        List<Polygon> neighbors = polygonNew.getNeighbours();

        for (Polygon neighbor : neighbors) {
            List<Polygon> neighboringNeighbors = neighbor.getNeighbours();

            neighboringNeighbors.remove(polygonOld);
            neighboringNeighbors.add(polygonNew);

            //There is technical debt here with abstraction leak and the fact that I am modifying the exact list
        }
    }

    private boolean isLake(int seed, int key, int lakesLeft) {
        seed = seed + key;

        seed = seed % 150;

        return seed < lakesLeft;
    }

    private boolean hasAquitifer(int seed, int key, int aquifiersLeft) {
        seed = seed + key;

        seed = seed % 151;

        return seed < aquifiersLeft;
    }

    private void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption){
        for (Vertex vertex : vertexMap.values()) {
            double x = vertex.getX();
            double y = vertex.getY();
            Random bag= new Random();

            if(ifbetweenCircles(vertex)) {
                vertex.setElevation(bag.nextDouble(10,20));
            }
            else if (withinInnerCircle(vertex)) {
                vertex.setElevation(bag.nextDouble(0,10));
            }
            else{
                vertex.setElevation(bag.nextDouble(-10,0));
            }

            for(Integer i : segmentMap.keySet()){
                Segment segment = segmentMap.get(i);
                segment.updateElevation();
            }
            for (Integer i : polygonMap.keySet()) {
                Polygon polygon = polygonMap.get(i);
                polygon.updateElevation();
            }
        }
    }
    private int find_start(Mesh aMesh,double x, double y){
        int index = 0;
        double min = 0;
        int river_start = 0;
        for (Structs.Vertex v: aMesh.getVerticesList()) {
            double deviation = 0;
            index++;
            deviation = Math.abs(v.getX()-x);
            deviation = deviation + Math.abs(v.getY()-y);
            if(deviation==0){
                return index;
            }
            if(min > deviation){
                min = deviation;
                river_start = index;
            }
        }
        return river_start;
    }
    /* This part is not done yet
    private void river_no_merge(Mesh aMesh,double x, double y){
        int index = find_start(aMesh,x,y);
        Vertex start = ConvertFromStructs.convert(aMesh.getVerticesList()).get(index);
        start.

    }*/


}
