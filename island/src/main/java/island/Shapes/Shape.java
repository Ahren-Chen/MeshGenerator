package island.Shapes;

import island.EveationGenerator.ElevationGenerator;
import island.Interfaces.ShapeGen;
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
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;
import java.util.*;

public abstract class Shape implements ShapeGen {

    protected Map<Integer, Vertex> vertexMap;
    protected Map<Integer, Segment> segmentMap;
    protected Map<Integer, Polygon> polygonMap;
    protected Map<Integer, Polygon> tileMap;
    protected double max_x;
    protected double max_y;
    protected Random bag;
    protected double centerX;
    protected double centerY;
    protected final ParentLogger logger = new ParentLogger();

    protected final Soil soil = new SlowSoil();


    protected abstract void affectNeighbors();
    protected boolean isLake(long seed, int key, int lakesLeft) {
        seed = seed + key;

        seed = seed % 150;

        return seed < lakesLeft;
    }

    protected boolean hasAquifer(long seed, int key, int aquifersLeft) {
        seed = seed + key;

        seed = seed % 151;

        return seed < aquifersLeft;
    }
    protected boolean isRiver(int riversLeft) {
        return (bag.nextInt(0, 20) < riversLeft);
    }
    protected void setElevation(String elevationOption){
        ElevationGenerator elevationGenerator = new ElevationGenerator();
        elevationGenerator.setElevation(vertexMap, segmentMap, polygonMap, elevationOption, max_x, max_y);
    };

    protected Vertex riverStart(Polygon biomes) {

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
    protected int find_start(Mesh aMesh,double x, double y){
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

    protected void calculateAbsorption() {
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

    protected void updateNeighbors(Polygon polygonNew, Polygon polygonOld) {
        List<Polygon> neighbors = polygonNew.getNeighbours();

        for (Polygon neighbor : neighbors) {
            List<Polygon> neighboringNeighbors = neighbor.getNeighbours();

            neighboringNeighbors.remove(polygonOld);
            neighboringNeighbors.add(polygonNew);

            //There is technical debt here with abstraction leak and the fact that I am modifying the exact list
        }
    }

}
