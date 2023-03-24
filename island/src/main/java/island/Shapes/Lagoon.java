package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.EveationGenerator.ElevationGenerator;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.Biomes;
import island.Interfaces.ElevationGen;
import island.Interfaces.ShapeGen;
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;
import island.Utility.RandomGen;
import island.river.River;

import java.util.*;

public class Lagoon extends Shape implements ShapeGen {

    private double innerRadius;
    private double outerRadius;
    public Mesh generate(Mesh mesh, double max_x, double max_y, int lakes, RandomGen bag, int aquifer, int riversLeft, String elevation, Soil soil, Biomes biomes) {
        logger.trace("Generating lagoon");
        centerX = max_x/2;
        centerY = max_y/2;
        this.bag = bag;
        this.max_x= max_x;
        this.max_y = max_y;
        this.soil = soil;

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        vertexMap = ConvertFromStructs.convert(structsVertexList);
        segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);

        tileMap = new HashMap<>();
        
        if (max_x <= max_y) {
            innerRadius = max_x/6;
            outerRadius = max_x * 2/5;
        }
        else {
            innerRadius = max_y/6;
            outerRadius = max_y * 2/5;
        }


        int riverc = 2;
        
        
        for (int key = 0; key < polygonMap.size(); key++) {
            Polygon polygon = polygonMap.get(key);
            Vertex centroid = polygon.getCentroid();

            Polygon poly;

            if (!withinOuterCircle(centroid)) {
                poly = new OceanTile(polygon);
            }

            else if (withinInnerCircle(centroid)) {
                poly = new LakeTile(polygon);
            }
            else {

                List<Polygon> neighbors = polygon.getNeighbours();
                boolean nextToOcean = false;

                for (Polygon neighbor : neighbors) {

                    if (neighbor.getClass().equals(OceanTile.class)) {
                        neighbor.setNextToOcean(true);
                        break;
                    }

                }

                if (!polygon.getNextToOcean()) {
                    if (isLake(bag, lakes)) {
                        poly = new LakeTile(polygon);
                        lakes--;
                    }
                    else {
                        poly = new BiomesTile(polygon, biomes);
                        if (hasAquifer(bag, aquifer)) {
                            ((BiomesTile) poly).setAquifer(true);
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
        if(true) {
            for (Polygon polygon : tileMap.values()) {
                if (polygon.getClass().equals(BiomesTile.class)) {
                    if(riverc>0){
                        River river1 = new River(polygon);
                        if (river1.ifFormed(polygon,polygon.getSegments().get(0).getV1())){
                            river1.formRiver(polygon,polygon.getSegments().get(0).getV1());
                            riverc--;
                        }


                            else{
                                break;
                            }

                    }
                }
            }




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
        return Mesh
                .newBuilder()
                .addAllVertices(vertexList)
                .addAllSegments(segmentList)
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

    @Override
    protected void affectNeighbors() {
        for (Polygon tile : tileMap.values()) {
            List<Polygon> neighbors = tile.getNeighbours();

            for (Polygon polygonNeighbor : neighbors) {
                polygonNeighbor.affectTile(tile);
            }

            tile.calculateWhittakerColor();
        }
    }



    @Override
    protected void setElevation(String elevationOption){
        ElevationGen elevationGen = new ElevationGenerator(bag);
        elevationGen.setElevation(vertexMap, segmentMap, polygonMap, elevationOption, innerRadius, outerRadius, max_x, max_y);
    }
    /*
    private Vertex riverStart(Polygon biomes) {

        Polygon neighborBiomes = null;
        Polygon water = null;
        List<Polygon> biomesNeighbor = biomes.getNeighbours();

        outerLoop:
        for (Polygon neighbor : biomesNeighbor) {
            if (neighbor.getClass().equals(OceanTile.class) || neighbor.getClass().equals(LakeTile.class)) {
                water = neighbor;

                for (Polygon waterNeighbor : water.getNeighbours()) {
                    if (waterNeighbor.getClass().equals(BiomesTile.class) && !waterNeighbor.equals(biomes)) {
                        if (biomesNeighbor.contains(waterNeighbor)) {
                            neighborBiomes = waterNeighbor;
                            break outerLoop;
                        }
                    }
                }
            }
        }

        for (Segment biomesSegment : biomes.getSegments()) {
            Vertex vertex1 = biomesSegment.getV1();
            Vertex vertex2 = biomesSegment.getV2();

            assert water != null;
            for (Segment waterSegment : water.getSegments()) {
                if (waterSegment.containsVertex(vertex1)) {

                    assert neighborBiomes != null;
                    for (Segment neighborBiomesSegment : neighborBiomes.getSegments()) {
                        if (neighborBiomesSegment.containsVertex(vertex1)) {
                            return vertex1;
                        }
                    }
                }
            }

            for (Segment waterSegment : water.getSegments()) {
                if (waterSegment.containsVertex(vertex2)) {

                    assert neighborBiomes != null;
                    for (Segment neighborBiomesSegment : neighborBiomes.getSegments()) {
                        if (neighborBiomesSegment.containsVertex(vertex2)) {
                            return vertex2;
                        }
                    }
                }
            }
        }
        return null;
    }
     */


}
