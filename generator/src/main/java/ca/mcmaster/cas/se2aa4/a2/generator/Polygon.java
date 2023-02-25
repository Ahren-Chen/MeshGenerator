package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.ConvertToStruct;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

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
    private static final int defaultThickness = 3;

    public Polygon(List<Segment> segments) {
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon");
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        //generate polygon
        centroid= this.calculate_center(this.segments);
    }
    public int getID(){
        if(ID==-1){
            logger.error("ID don't exist");
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
    public void setNeighbors(List<Polygon>polygons){
        this.neighbours = polygons;
    }

    public static List<Polygon> generate (Hashtable<Coordinate, Vertex> vertices, int vertexThickness, int segmentThickness, Coordinate maxSize) {
        // Generate count number of polygons using the given vertices
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        List<Polygon> polygonList = new ArrayList<>();

        List<Coordinate> sites = new ArrayList<>(vertices.keySet());

        voronoi.setSites(sites);
        voronoi.setTolerance(0.01);

        PrecisionModel precision = new PrecisionModel(Generator.accuracy);

        GeometryFactory geomFact = new GeometryFactory(precision);

        Geometry polygonsGeometry = voronoi.getDiagram(geomFact);

        List<Geometry> polygonGeometryList = new ArrayList<>();

        TreeSet<Segment> segmentSet= new TreeSet<>(); // keep track of segments to deregister duplicates

        for (int i = 0; i < polygonsGeometry.getDimension(); i++) {
            polygonGeometryList.add(polygonsGeometry.getGeometryN(i));
        }

        for (Geometry polygon : polygonGeometryList) {
            List<Segment> polygonSegmentList = new ArrayList<>();

            for (int coords = 0; coords < polygon.getCoordinates().length - 1; coords++) {
                Coordinate verticesCoords = polygon.getCoordinates()[coords];
                Coordinate verticesCoords2 = polygon.getCoordinates()[coords+1];
                modifyCoords(verticesCoords, maxSize);
                modifyCoords(verticesCoords2, maxSize);

                if (! coordinateVertexMap.containsKey(verticesCoords)) {

                    Vertex v;
                    if (vertexThickness <= 0) {
                        v = new Vertex(verticesCoords.getX(), verticesCoords.getY(),
                                false, defaultThickness, RandomColor.randomColorDefault());
                    }
                    else {
                        v = new Vertex(verticesCoords.getX(), verticesCoords.getY(),
                                false, vertexThickness, RandomColor.randomColorDefault());
                    }

                    coordinateVertexMap.put(verticesCoords, v);
                }

                if (! coordinateVertexMap.containsKey(verticesCoords2)) {
                    Vertex v;
                    if (vertexThickness <= 0) {
                        v = new Vertex(verticesCoords2.getX(), verticesCoords2.getY(),
                                false, defaultThickness, RandomColor.randomColorDefault());
                    }
                    else {
                        v = new Vertex(verticesCoords2.getX(), verticesCoords2.getY(),
                                false, vertexThickness, RandomColor.randomColorDefault());
                    }

                    coordinateVertexMap.put(verticesCoords2, v);
                }



                Vertex v1 = coordinateVertexMap.get(verticesCoords);
                Vertex v2 = coordinateVertexMap.get(verticesCoords2);
                
                //this is a dumb fix for identical segment removed in list but ID not added
                Segment polygonSegment = new Segment(v1, v2, segmentThickness);
                if(!segmentSet.contains(polygonSegment)){
                    segmentSet.add(polygonSegment);
                }
                else{
                    for (Segment s: segmentSet) {
                        if(s.compareTo(polygonSegment)==0){
                            polygonSegment=s;
                            break;
                        }
                    }
                }
                polygonSegmentList.add(polygonSegment);
            }

            Polygon p = new Polygon(polygonSegmentList);
            polygonList.add(p);
        }

        return polygonList;
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

        return new Vertex(cords[0], cords[1], true, defaultThickness, color);
    }

    private static void modifyCoords(Coordinate coords, Coordinate maxSize) {
        if (coords.getX() < 0) {
            coords.setX(0);
        }
        else if (coords.getX() > maxSize.getX()){
            coords.setX(maxSize.getX());
        }

        if (coords.getY() < 0) {
            coords.setY(0);
        }
        else if (coords.getY() > maxSize.getY()) {
            coords.setY(maxSize.getY());
        }

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
            logger.error("Segments do not connect to form a closed shape");
            throw new RuntimeException();
        }
        else if (segments.size() != sortedSegments.size()) {
            logger.error("Extra segments are given but not used, will discard them. Segments used: " +
                    sortedSegments.size() + ", Segments given: " + segments.size());
        }

        return sortedSegments;
    }

    public Structs.Polygon convertStruct() {
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
