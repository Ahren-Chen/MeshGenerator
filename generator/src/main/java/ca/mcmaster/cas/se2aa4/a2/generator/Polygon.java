package ca.mcmaster.cas.se2aa4.a2.generator;

import logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.ConvertToStruct;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Polygon implements ConvertToStruct<Structs.Polygon> {
    private final List<Segment> segments;
    private final Color color;
    private final Vertex centroid;
    private List<Polygon> neighbours;
    private static final ParentLogger logger= new ParentLogger();
    private final ConvertColor colorConverter = new ConvertColor();
    private int ID=-1;
    private final double vertexThickness;
    private final double segmentThickness;

    /**
     * This is the constructor for the polygon, where it will take in a {@code List<Segment>}, the vertex thickness, and
     * the segment thickness to return a polygon object.
     * @param segments  a List of segments
     * @param segmentThickness a {@code double} value for the thickness of its segments
     * @param vertexThickness a {@code double} value for the thickness of the centroid vertex
     */
    public Polygon(List<Segment> segments, double vertexThickness, double segmentThickness) {

        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon : " + segments.size());
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        this.vertexThickness = vertexThickness;
        this.segmentThickness = segmentThickness;

        //generate polygon centroid
        centroid = this.calculate_center(this.segments);
    }

    public double getVertexThickness(){
        return vertexThickness;
    }

    public double getSegmentThickness() {
        return segmentThickness;
    }

    public int getID(){
        if(ID==-1){
            logger.error("Polygon ID don't exist");
        }
        return ID;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public List<Polygon> getNeighbors() {
        return neighbours;
    }

    public Vertex getCentroid() {
        return centroid;
    }
    public List<Segment> getSegments() {
        return segments;
    }
    public boolean compare(Polygon p) {
        return p.centroid.compare(this.centroid);
    }
    public void setNeighbors(List<Polygon> polygonList){
        this.neighbours = polygonList;
    }

    /**
     * This method takes in a list of line segments and a list of segment indices.
     * It calculates the center point of these line segments by taking the average x and y coordinates of their endpoints.
     * It then returns an integer value that represents the center point's position on a two-dimensional grid.
     * @param segments  a List of segments
     * @return Vertex   a new Vertex
     */
    private Vertex calculate_center(List<Segment> segments) {
        double[] cords = {0, 0};
        int Red = 0, Green = 0, Blue = 0, Alpha = 0;
        Color color;

        for (Segment segment : segments) {
            cords[0] += segment.getVertice1().getX();
            cords[1] += segment.getVertice1().getY();
            cords[0] += segment.getVertice2().getX();
            cords[1] += segment.getVertice2().getY();

            color = segment.getColor();
            Red += color.getRed();
            Green += color.getGreen();
            Blue = color.getBlue();
            Alpha += color.getAlpha();
        }

        int segmentSize = segments.size();

        cords[0] /= 2 * segmentSize;
        cords[1] /= 2 * segmentSize;

        Red /= segmentSize;
        Green /= segmentSize;
        Blue /= segmentSize;
        Alpha /= segmentSize;

        color = new Color(Red, Green, Blue, Alpha);

        return new Vertex(cords[0], cords[1], true, vertexThickness, color);
    }

    /**
     * This method takes in a list of line segments and a list of segment indices.
     * It will return a sorted list of segment by first go through all the segment
     * @param segments a {@code List<Segment>} of segments that the polygon is made of
     * @return Vertex   a new Vertex
     */
    private List<Segment> sortSegments(List<Segment> segments) {
        //Create a new List to store all sorted segments
        List<Segment> sortedSegments = new ArrayList<>();

        //Set up the initial segment
        Segment startingSegment = segments.get(0);

        //Record initial index and the vertex I want to connect to as 'nextVertex'
        Vertex startingVertex = startingSegment.getVertice1();
        Vertex nextVertex = startingSegment.getVertice2();

        //Add the starting segment into the sorted list
        sortedSegments.add(startingSegment);

        //Set up current segment that I am trying to connect to
        Segment currentSegment = startingSegment;

        //For every segment number that should be in the polygon
        for (int segNum = 0; segNum < segments.size(); segNum++) {

            //For each segment in the segments list
            for (Segment segment : segments) {

                //If the current segment is not the current segment, then I need to check if it connects to it
                if (! segment.equals(currentSegment)) {

                    //If this segment v1 is the same as the current next vertex I want to connect with
                    if (segment.getVertice1().equals(nextVertex)) {

                        //Then check if this is the last segment in the polygon (Connecting nextVertex and starting vertex)
                        if (segment.getVertice2().equals(startingVertex)) {
                            nextVertex = startingVertex;
                            sortedSegments.add(segment);
                            break;
                        }

                        //If the next segment is not the last, then I add it to sorted segments and set the nextVertex
                        nextVertex = segment.getVertice2();
                        sortedSegments.add(segment);
                        currentSegment = segment;

                    }

                    //If this segment v2 is the same as the current next vertex I want to connect with
                    else if (segment.getVertice2().equals(nextVertex)) {

                        //Same thing as above but with different Vertices
                        if (segment.getVertice1().equals((startingVertex))) {
                            nextVertex = startingVertex;
                            sortedSegments.add(segment);
                            break;
                        }

                        nextVertex = segment.getVertice1();
                        sortedSegments.add(segment);
                        currentSegment = segment;
                    }
                }
            }

            //At the end of every loop, check whether the polygon was connected
            if (nextVertex.equals(startingVertex)) {
                break;
            }
        }

        //If for some reason, the segments do not connect, then I manually create one to connect them
        if (! nextVertex.equals(startingVertex)) {
            //logger.error("Segments do not connect to form a closed shape");
            Segment extra = new Segment(nextVertex, startingVertex, segmentThickness);
            sortedSegments.add(extra);
        }

        return sortedSegments;
    }

    /**
     * This method takes will convert the polygon object to Structs.Polygon and keep the same attributes
     * @return Structs.Polygon
     */
    public Structs.Polygon convertToStruct() {
        //Convert the color and create a Structs.Property for it
        String polygonColor = colorConverter.convert(this.color);
        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(polygonColor).build();

        List<Integer> segmentIndexList = new ArrayList<>();

        for (Segment s: this.segments) {
            int segmentIdx = s.getID();
            segmentIndexList.add(segmentIdx);
        }

        List<Integer> neighborID = new ArrayList<>();

        for (Polygon p: this.neighbours) {
            neighborID.add(p.getID());
        }

        int centroidIdx = this.centroid.getID();

        return Structs.Polygon.newBuilder()
                .setCentroidIdx(centroidIdx)
                .addAllSegmentIdxs(segmentIndexList)
                .addAllNeighborIdxs(neighborID)
                .addProperties(colorProperty)
                .build();
    }
}
