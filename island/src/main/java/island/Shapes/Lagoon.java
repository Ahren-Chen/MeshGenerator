package island.Shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.EveationGenerator.ElevationGenerator;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.Biomes;
import island.Interfaces.ShapeGen;
import island.Resource.Resource;
import island.River.River;
import island.SoilProfiles.Soil;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;
import island.Utility.RandomGen;

import java.util.*;

public class Lagoon extends Shape implements ShapeGen{

    private double innerRadius;
    private double outerRadius;
    public Mesh generate(Mesh mesh, double max_x, double max_y, int lakes, RandomGen bag, int aquifer, int riversLeft, String elevationOption, Soil soil, Biomes biomes, String heatMapOption) {
        logger.trace("Generating lagoon");
        centerX = max_x/2;
        centerY = max_y/2;
        this.bag = bag;
        super.max_x= max_x;
        super.max_y = max_y;
        this.soil = soil;

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        vertexMap = ConvertFromStructs.convert(structsVertexList);
        segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);
        int startId = segmentMap.size();
        int riverc = riversLeft;

        tileMap = new HashMap<>();

        if (max_x <= max_y) {
            innerRadius = max_x/6;
            outerRadius = max_x * 2/5;
        }
        else {
            innerRadius = max_y/6;
            outerRadius = max_y * 2/5;
        }



        for (Polygon polygon : polygonMap.values()) {
            Vertex centroid = polygon.getCentroid();

            Polygon poly = polygon;
            if (!(withinOuterCircle(centroid))) {
                        poly = new OceanTile(polygon);
                        poly.setIsWater(true);

            }

            updateNeighbors(poly, polygon);

            int ID = polygon.getID();

            tileMap.put(ID, poly);
        }

        for (int key = 0; key < tileMap.size(); key++) {
            Polygon polygon = tileMap.get(key);
            Vertex centroid = polygon.getCentroid();
            Polygon poly = polygon;

            if (withinInnerCircle(centroid)) {
                poly = new LakeTile(polygon);
                poly.setIsWater(true);
            }
            else if (withinOuterCircle(centroid)){

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
                        poly.setIsWater(true);
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

        setElevation(elevationOption);

        int possibleRiversLeft = 0;
        while(riverc>0 && possibleRiversLeft < tileMap.size()){
            Integer start = bag.nextInt(0, tileMap.size());
            Polygon polygon = tileMap.get(start);
            if (polygon.getClass().equals(BiomesTile.class) && !polygon.getCentroid().getIfRiver()) {
                River river1 = new River();
                if(river1.formRiverboolean(polygon)){
                    //System.out.println("one river form");
                    polygon.setIsWater(true);
                    riverc--;
                    List<Segment> river = river1.formRiverWhile(polygon);
                    for (Segment s: river ) {
                        //System.out.println("adding id");
                        startId++;
                        s.setID(startId);
                        segmentMap.put(startId,s);
                    }

                }
            }
            possibleRiversLeft++;
        }

        if (possibleRiversLeft == tileMap.size()) {
            logger.error("No more rivers are possible");
        }

        setHeatMap(heatMapOption);

        Resource random = new Resource();
        tileMap = random.resourceCalculation(tileMap);
        setCities();

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
    @Override
    protected void setElevation(String elevationOption){
        ElevationGenerator elevationGenerator = new ElevationGenerator(bag);
        elevationGenerator.setElevation(vertexMap, segmentMap, tileMap, elevationOption, innerRadius, outerRadius, max_x, max_y);
    }


}
