package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.EveationGenerator.ElevationGenerator;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ElevationGen;
import island.Interfaces.ShapeGen;
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;
import island.river.River;

import java.util.*;

public class Lagoon extends Shape implements ShapeGen {
    private Random bag;
    private double innerRadius;
    private double outerRadius;
    private double centerX;
    private double centerY;
    private final ParentLogger logger = new ParentLogger();
    private final Soil soil = new SlowSoil();
    private Map<Integer, Vertex> vertexMap;
    private Map<Integer, Segment> segmentMap;
    private Map<Integer, Polygon> polygonMap;
    private Map<Integer, Polygon> tileMap;


    public Mesh generate(Mesh mesh, double max_x, double max_y, int lakes, long seed, int aquifer, int riversLeft, String elevation) {
        logger.trace("Generating lagoon");
        centerX = max_x/2;
        centerY = max_y/2;
        bag = new Random(seed);
        this.max_x= max_x;
        this.max_y = max_y;

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
                int river= riversLeft;
                if(river>0){

                    River river1 = new River(polygon);
                    if (river1.ifFormed(polygon,polygon.getSegments().get(0).getV1())){
                        river1.formRiver(polygon,polygon.getSegments().get(0).getV1());
                        river--;
                    }

                    else{

                        if (isLake(seed, key, lakes)){
                            river1.formRiver(polygon,polygon.getSegments().get(0).getV1());
                            lakes--;
                            river--;
                        }
                        else{
                            break;
                        }
                    }

                }

                if (!polygon.getNextToOcean()) {
                    if (isLake(seed, key, lakes)) {
                        poly = new LakeTile(polygon);
                        lakes--;
                    }
                    else {
                        poly = new BiomesTile(polygon);
                        if (hasAquifer(seed, key, aquifer)) {
                            ((BiomesTile) poly).setAquifer(true);
                            aquifer--;
                        }

                    }
                }
                else {
                    poly = new BiomesTile(polygon);
                }
            }

            updateNeighbors(poly, polygon);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        //affectNeighbors();
        calculateAbsorption();

        setElevation(elevation);

        River river;
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

        System.out.println("Seed: " + seed);
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
    private void updateNeighbors(Polygon polygonNew, Polygon polygonOld) {
        List<Polygon> neighbors = polygonNew.getNeighbours();

        for (Polygon neighbor : neighbors) {
            List<Polygon> neighboringNeighbors = neighbor.getNeighbours();

            neighboringNeighbors.remove(polygonOld);
            neighboringNeighbors.add(polygonNew);

            //There is technical debt here with abstraction leak and the fact that I am modifying the exact list
        }
    }

    private boolean isLake(long seed, int key, int lakesLeft) {
        seed = seed + key;

        seed = seed % 150;

        return seed < lakesLeft;
    }
    private boolean isRiver(int riversLeft) {
        return (bag.nextInt(0, 20) < riversLeft);
    }
    private boolean hasAquifer(long seed, int key, int aquifersLeft) {
        seed = seed + key;

        seed = seed % 151;

        return seed < aquifersLeft;
    }
    @Override
    protected void setElevation(String elevationOption){
        ElevationGen elevationGen = new ElevationGenerator();
        elevationGen.setElevation(vertexMap, segmentMap, polygonMap, elevationOption, max_x, max_y);

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

    private void calculateAbsorption() {
        List<Polygon> lakeList = new ArrayList<>();
        for (Polygon tile : tileMap.values()) {
            if (tile.getClass().equals(LakeTile.class) || tile.hasAquifer()) {
                lakeList.add(tile);
            }
        }

        for (Polygon tile : tileMap.values()) {

            soil.calculateAbsorption(tile, lakeList);
            tile.calculateWhittakerColor();
            //logger.error(tile.getPrecipitation() + "");
        }
    }
}
