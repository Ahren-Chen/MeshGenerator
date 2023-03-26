package island.eveationGenerator;
import java.util.*;

import logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.interfaces.ElevationGen;
import island.utility.RandomGen;

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
        max_X= max_x;
        max_Y= max_y;

        centerX = max_X / 2;
        centerY = max_Y / 2;

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
                //fake random number for elevation
                elevation = VolcanoHeight-((distance*VolcanoHeight)*bag.nextDouble(0.97, 1));
                logger.trace(Double.toString(elevation));
                vertex.setElevation(1000);
            }
            else if (withinInnerCircle(vertex, innerRadius)) {
                logger.trace("Within inner circle");
                double distance = Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));
                //fake random number for elevation
                elevation = ((VolcanoHeight - (innerRadius/(distance+1/VolcanoHeight)))*bag.nextDouble(0.97, 1));
                logger.trace(Double.toString(elevation));
                vertex.setElevation(2000);
            }
            else{
                logger.trace("Outside circles");
                elevation=0;
                vertex.setElevation(0);
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
            vertex.setElevation(1900+bag.nextDouble(-100,100));
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
