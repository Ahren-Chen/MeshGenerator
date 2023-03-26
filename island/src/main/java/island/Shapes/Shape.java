package island.Shapes;

import island.EveationGenerator.ElevationGenerator;
import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;
import island.Utility.RandomGen;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Shape implements ShapeGen {

    protected Map<Integer, Vertex> vertexMap;
    protected Map<Integer, Segment> segmentMap;
    protected Map<Integer, Polygon> polygonMap;
    protected Map<Integer, Polygon> tileMap;
    protected double max_x;
    protected double max_y;
    protected RandomGen bag;
    protected double centerX;
    protected double centerY;
    protected final ParentLogger logger = new ParentLogger();
    protected Soil soil = new SlowSoil();


    //protected abstract void affectNeighbors();
    protected boolean isLake(RandomGen bag, int lakesLeft) {
        return bag.nextInt(0, 150) < lakesLeft;
    }

    protected boolean hasAquifer(RandomGen bag, int aquifersLeft) {
        return bag.nextInt(0, 151) < aquifersLeft;
    }
    protected boolean isRiver(int riversLeft) {
        return (bag.nextInt(0, 20) < riversLeft);
    }
    protected void setElevation(String elevationOption){
        ElevationGenerator elevationGenerator = new ElevationGenerator(bag);
        elevationGenerator.setElevation(vertexMap, segmentMap, tileMap, elevationOption, max_x, max_y);
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
            if (tile.getClass().equals(LakeTile.class) || tile.hasAquifer() || tile.getCentroid().getIfRiver()) {
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

            neighbor.setNeighbours(neighboringNeighbors);
            //There is technical debt here with abstraction leak and the fact that I am modifying the exact list
        }
    }
    /**
     * This method is used to generate a heat map for the map
     * This method is implemented by rewrite all colours in polygon
     * @param type the type of heat map to generate
     */
    protected void setHeatMap(String type){
        if(type.equals("none")){
            return;
        }
        for (Polygon tile : tileMap.values()) {
            double value = 0;
            int min;
            int max;

            switch (type) {
                case "elevation":
                    value = tile.getElevation();
                    min = 0;
                    max=2000;
                    break;
                case "precipitation":
                    value = tile.getPrecipitation();
                    min=30;
                    max=500;
                    break;
                case "temperature":
                    value = tile.getTemperature();
                    min=-80;
                    max=50;
                    break;
                default :
                    min=-1;
                    max=-1;
            }
            //logger.error(value + "");
            tile.setColor(getHeatMapColor(value, min, max));
        }
    }
    private Color getHeatMapColor(double value, double min, double max) {
        // range check
        if(value>max){
            value=max;
        }
        if(value<min){
            return new Color(255, 255, 255);
        }

        int h = Math.abs((int)((value - min) / (max - min) * (195*3)));

        if(h<230){
            return new Color(255-h, 255, 255);
        }
        else if(h<230*2){
            return new Color(0, 255-(h/2), 255);
        }
        else{
            return new Color(0, 0, 255-(h/3));
        }

    }
}
