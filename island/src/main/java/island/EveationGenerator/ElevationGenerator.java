package island.EveationGenerator;
import java.util.*;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ElevationGen;
import island.Tiles.BiomesTile;
import island.Utility.RandomGen;
import org.apache.logging.log4j.Level;

public class ElevationGenerator implements ElevationGen{
    private double max_X;
    private double max_Y;
    private double centerX;
    private double centerY;
    ParentLogger logger = new ParentLogger();
    private double innerRadius=-1;
    private double outerRadius=-1;
    private final RandomGen bag;
    public static final double VolcanoHeight= 2000;

    public ElevationGenerator(RandomGen bag) {
        this.bag = bag;
    }
    @Override
    public void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption, double max_x, double max_y) {
        this.max_X = max_x;
        this.max_Y = max_y;

        centerX = this.max_X / 2;
        centerY = this.max_Y / 2;

        switch (elevationOption) {
            case "volcano" -> setVolcanoElevation(vertexMap, segmentMap, polygonMap);
            case "canyon" -> setCanyonElevation(vertexMap, segmentMap, polygonMap);
            case "mountain" -> setMountainElevation(vertexMap, segmentMap, polygonMap);
            case "arctic" -> setArcticElevation(vertexMap, segmentMap, polygonMap);
            default -> throw new IllegalArgumentException("Invalid elevation option");
        }
    }
    @Override
    public void setElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, String elevationOption, double innerRadius, double outerRadius, double max_x, double max_y) {
        this.innerRadius= innerRadius;
        this.outerRadius= outerRadius;
        setElevation(vertexMap, segmentMap, polygonMap, elevationOption, max_x, max_y);
    }

    private void setVolcanoElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap) {
        for (Vertex vertex : vertexMap.values()) {
            double x = vertex.getX();
            double y = vertex.getY();

            if(innerRadius==-1 || outerRadius==-1){
                innerRadius = max_X / 7;
                outerRadius = max_X / 3;
            }

            double elevation;
            if(ifbetweenCircles(vertex, innerRadius, outerRadius)) {
                logger.trace("Between circles");
                double distance = ((Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)))-innerRadius)/(outerRadius-innerRadius);

                elevation = VolcanoHeight-(distance*VolcanoHeight);
                logger.trace(Double.toString(elevation));
            }
            else if (withinInnerCircle(vertex, innerRadius)) {
                logger.trace("Within inner circle");
                double distance = Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));
                //fake random number for elevation
                elevation = ((distance/innerRadius)*VolcanoHeight);
                logger.trace(Double.toString(elevation));
            }
            else{
                logger.trace("Outside circles");
                elevation=0;
            }
            vertex.setElevation(elevation);

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
    private void setCanyonElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*for( Polygon polygon: polygonMap.values()){
            if(polygon.getClass().equals("BiomesTile")){
                BiomesTile tile= (BiomesTile) polygon;
            }
        }*/
    }
    private void setMountainElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void setArcticElevation(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap) {
        for (Vertex vertex: vertexMap.values()){
            double elevation=1900+bag.nextDouble(-100,100);
            logger.error("Elevation: "+elevation);
            vertex.setElevation(elevation);
        }
    }

    private boolean withinInnerCircle(Vertex point,double innerRadius) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(innerRadius, 2);
    }

    private boolean withinOuterCircle(Vertex point, double outerRadius) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(outerRadius, 2);
    }
    private boolean ifbetweenCircles(Vertex point, double innerRadius, double outerRadius) {
        double x = point.getX();
        double y = point.getY();

        return (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) <= Math.pow(outerRadius, 2) &&
                (Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) >= Math.pow(innerRadius, 2);
    }



}
