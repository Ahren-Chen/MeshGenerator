package island.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.interfaces.Biomes;
import island.soilProfiles.Soil;
import island.tiles.BiomesTile;
import island.tiles.LakeTile;
import island.tiles.OceanTile;
import island.utility.RandomGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bridge extends Shape{
    private double radius;
    public Structs.Mesh generate(Structs.Mesh mesh, double width, double height, int lakes, RandomGen bag, int aquifer, int river, String elevation, Soil soil, Biomes biomes, String heatMapOption) {
        logger.trace("Generating shape");
        this.bag = bag;
        this.max_x = width;
        this.max_y = height;
        this.soil = soil;
        this.radius = Math.min(width, height) / 5;

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        vertexMap = ConvertFromStructs.convert(structsVertexList);
        segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);
        int startId = segmentMap.size();

        tileMap = new HashMap<>();

        int riverc = 2;

        for (Polygon polygon : polygonMap.values()) {
            Vertex centroid = polygon.getCentroid();

            Polygon poly = polygon;
            if (! (withinBridge(centroid))) {
                if (! (withinLeftCircle(centroid))) {
                    if (! (withinRightCircle(centroid))) {
                        poly = new OceanTile(polygon);
                        polygon.setIsWater(true);
                    }
                }
            }

            updateNeighbors(poly, polygon);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        for (int key = 0; key < tileMap.size(); key++) {
            Polygon polygon = tileMap.get(key);
            Vertex centroid = polygon.getCentroid();

            Polygon poly = polygon;

            if (withinRightCircle(centroid) || withinLeftCircle(centroid) || withinBridge(centroid)) {
                List<Polygon> neighbors = polygon.getNeighbours();

                for (Polygon neighbor : neighbors) {

                    if (neighbor.getClass().equals(OceanTile.class)) {
                        polygon.setNextToOcean(true);
                        break;
                    }

                }

                if (!polygon.getNextToOcean()) {
                    if (isLake(bag, lakes)) {
                        poly = new LakeTile(polygon);
                        lakes--;
                        polygon.setIsWater(true);
                    }
                    else {
                        poly = new BiomesTile(polygon, biomes);
                        if (hasAquifer(bag, aquifer)) {
                            poly.setAquifer(true);
                            aquifer--;
                        }

                    }
                }
                else {
                    poly = new BiomesTile(polygon, biomes);
                }
            }

            updateNeighbors(poly, polygon);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        //affectNeighbors();
        calculateAbsorption();

        setElevation(elevation);

        /*
        for (Polygon polygon : tileMap.values()) {
            if (polygon.getClass().equals(BiomesTile.class)) {
                if(riverc>0){
                    River river1 = new River(polygon);
                    polygon.setIsWater(true);
                    List<Segment> river = river1.formRiver(polygon);
                    riverc--;
                    for (Segment s: river ) {
                        startId++;
                        segmentMap.put(startId,s);
                    }


                }
                else{
                    break;
                }
            }
        }*/






        ////// version A
        /*River river;
            for (Polygon polygon : tileMap.values()) {
                if (polygon.getClass().equals(BiomesTile.class)) {
                    List<Polygon> neighbors = polygon.getNeighbours();

                    for (Polygon neighbor : neighbors) {
                        if (neighbor.getClass().equals(LakeTile.class) || neighbor.getClass().equals(OceanTile.class)) {

                            if (isRiver(10)) {
                                Vertex v = riverStart(polygon);

                                if (v == null) {
                                    logger.error("Polygons are not neighbors");
                                    throw new RuntimeException();
                                }
                                river = new River(polygon);
                                river.findRiver(polygon, v, 5);
                            }
                        }
                    }
                }
            }*/



        List<Structs.Polygon> tileList = new ArrayList<>();
        List<Structs.Segment> segmentList = new ArrayList<>();
        List<Structs.Vertex> vertexList = new ArrayList<>();

        for (Polygon tile : tileMap.values()) {
            tileList.add(tile.convertToStruct());
        }
        for (Segment segment : segmentMap.values()) {
            segmentList.add(segment.convertToStruct());
        }
        for (Vertex vertex : vertexMap.values()) {
            vertexList.add(vertex.convertToStruct());
        }

        System.out.println("Seed: " + bag.getSeed());
        return Structs.Mesh
                .newBuilder()
                .addAllVertices(vertexList)
                .addAllSegments(segmentList)
                .addAllPolygons(tileList)
                .build();
    }

    private boolean withinLeftCircle(Vertex vertex) {
        double centerX = max_x/5;
        double centerY = max_y/2;

        double x = vertex.getX();
        double y = vertex.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(radius, 2);
    }

    private boolean withinRightCircle(Vertex vertex) {
        double centerX = max_x*4/5;
        double centerY = max_y/2;

        double x = vertex.getX();
        double y = vertex.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(radius, 2);
    }

    private boolean withinBridge(Vertex vertex) {
        double X1 = max_x/5;
        double Y1 = max_y/2 - radius/2;

        double X2 = max_x*4/5;
        double Y2 = max_y/2 + radius/2;

        double x = vertex.getX();
        double y = vertex.getY();

        if (! (x < X1 || x > X2)) {
            return !(y < Y1 || y > Y2);
        }
        return false;
    }
}

