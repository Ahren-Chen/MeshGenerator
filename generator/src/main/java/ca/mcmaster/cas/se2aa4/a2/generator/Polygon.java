package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.ConvertToStruct;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Polygon implements ConvertToStruct<Structs.Polygon> {
    private final List<Segment> segments;
    private final Color color;
    private final Vertex centroid;
    private final Vertex parentPoint;
    private List<Vertex> neighbours;
    private static final ParentLogger logger= new ParentLogger();
    private final ConvertColor colorConverter = new ConvertColor();
    private int ID=-1;
    private final double vertexThickness;
    private final double segmentThickness;

    public Polygon(List<Segment> segments, double vertexThickness, double segmentThickness) {
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon : " + segments.size());
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        this.vertexThickness = vertexThickness;
        this.segmentThickness = segmentThickness;
        //generate polygon
        centroid = this.calculate_center(this.segments);
        parentPoint = null;
    }

    public Polygon(List<Segment> segments, Vertex parentPoint, double vertexThickness, double segmentThickness) {
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon : " + segments.size());
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        this.vertexThickness = vertexThickness;
        this.segmentThickness = segmentThickness;
        //generate polygon
        this.centroid = calculate_center(this.segments);
        this.parentPoint = parentPoint;
    }
    public double getVertexThickness(){
        return vertexThickness;
    }
    public double getSegmentThickness() { return segmentThickness; }
    public int getID(){
        if(ID==-1){
            logger.error("Polygon ID don't exist");
        }
        return ID;
    }
    public void setID(int ID){
        this.ID=ID;
    }
    public List<Vertex> getNeighbors() {
        return neighbours;
    }

    public Vertex getCentroid() {

        return centroid;
    }
    public List<Segment> getSegments() {

        return segments;
    }
    public Vertex getParentPoint() {
        return parentPoint;
    }
    public boolean compare(Polygon p) {
        return p.centroid.compare(this.centroid);
    }
    public void setNeighbors(List<Vertex> centroids){
        this.neighbours = centroids;
    }

    /***
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

        //logger.error(this.vertexThickness  + "");
        return new Vertex(cords[0], cords[1], true, vertexThickness, color);
    }

    private List<Segment> sortSegments(List<Segment> segments) {
        List<Segment> sortedSegments = new ArrayList<>();

        Segment startingSegment = segments.get(0);

        Vertex startingVertex = startingSegment.getVertice1();
        Vertex nextVertex = startingSegment.getVertice2();

        sortedSegments.add(startingSegment);

        Segment currentSegment = startingSegment;

        for (int segNum = 0; segNum < segments.size(); segNum++) {
            for (Segment segment : segments) {

                if (! segment.equals(currentSegment)) {

                    if (segment.getVertice1().equals(nextVertex)) {
                        if (segment.getVertice2().equals(startingVertex)) {
                            nextVertex = startingVertex;
                            sortedSegments.add(segment);
                            break;
                        }

                        nextVertex = segment.getVertice2();
                        sortedSegments.add(segment);
                        currentSegment = segment;

                    } else if (segment.getVertice2().equals(nextVertex)) {
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

            if (nextVertex.equals(startingVertex)) {
                break;
            }
        }

        if (! nextVertex.equals(startingVertex)) {
            //logger.error("Segments do not connect to form a closed shape");
            Segment extra = new Segment(nextVertex, startingVertex, segmentThickness);
            sortedSegments.add(extra);
        }

        return sortedSegments;
    }

    public Structs.Polygon convertToStruct() {
        String polygonColor = colorConverter.convert(this.color);
        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(polygonColor).build();

        List<Integer> segmentIndexList = new ArrayList<>();

        for (Segment s: this.segments) {
            int segmentIdx = s.getID();
            segmentIndexList.add(segmentIdx);
        }

        List<Integer> neighborID = new ArrayList<>();

        for (Vertex v: this.neighbours) {
            neighborID.add(v.getID());
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
