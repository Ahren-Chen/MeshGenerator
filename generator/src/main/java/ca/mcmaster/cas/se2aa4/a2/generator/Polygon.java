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
    private List<Vertex> neighbours;
    private static final ParentLogger logger= new ParentLogger();
    private final ConvertColor colorConverter = new ConvertColor();
    private int ID=-1;
    private static final int defaultThickness = 3;

    public Polygon(List<Segment> segments) {
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon : " + segments.size());
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        //generate polygon
        centroid = this.calculate_center(this.segments);
    }

    public Polygon(List<Segment> segments, Vertex centroid) {
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon : " + segments.size());
        }

        this.segments = sortSegments(segments);

        //Randomly colored polygons
        this.color = RandomColor.randomColorDefault();

        //generate polygon
        this.centroid = centroid;
    }
    public int getDefaultThickness(){
        return defaultThickness;
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
    public List<Vertex> getNeighbors() {
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
    public void setNeighbors(List<Vertex> centroids){
        this.neighbours = centroids;
    }

    public static List<Polygon> generate (Map<Coordinate, Vertex> vertices, int vertexThickness, int segmentThickness, Coordinate maxSize) {
        // Generate count number of polygons using the given vertices
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        List<Polygon> polygonList = new ArrayList<>();
        ConvexHull convexHullPolygon;

        List<Coordinate> sites = new ArrayList<>(vertices.keySet());
        //logger.error(vertices.keySet() + "");

        Envelope envelope = new Envelope(new Coordinate(0, 0), maxSize);
        voronoi.setSites(sites);
        voronoi.setTolerance(0.01);
        voronoi.setClipEnvelope(envelope);

        PrecisionModel precision = new PrecisionModel(0.01);

        GeometryFactory geomFact = new GeometryFactory(precision);

        Geometry polygonsGeometry = voronoi.getDiagram(geomFact);

        List<Geometry> polygonGeometryList = new ArrayList<>();

        TreeSet<Segment> segmentSet= new TreeSet<>(); // keep track of segments to deregister duplicates

        List<Segment> polygonSegmentList = new ArrayList<>();
        List<Coordinate> polygonCoordinateList_Unique = new ArrayList<>();
        Coordinate coordinate;

        for (int i = 0; i < polygonsGeometry.getNumGeometries(); i++) {
            Geometry polygonGeo = polygonsGeometry.getGeometryN(i);
            convexHullPolygon = new ConvexHull(polygonGeo);

            polygonGeo = convexHullPolygon.getConvexHull();
            polygonGeometryList.add(polygonGeo);
        }

        for (Geometry polygon : polygonGeometryList) {
            polygonSegmentList.clear();
            polygonCoordinateList_Unique.clear();

            Object centroidCord = polygon.getUserData();
            Vertex centroid = null;
            try {
                centroid = vertices.get((Coordinate) centroidCord);
            }
            catch (Exception e) {
                logger.error(e.getMessage());
                System.exit(1);
            }

            for (int coords = 0; coords < polygon.getCoordinates().length; coords++) {
                coordinate = polygon.getCoordinates()[coords];
                modifyCoords(coordinate, maxSize);

                if (! polygonCoordinateList_Unique.contains(coordinate) || coords == polygon.getCoordinates().length - 1) {
                    polygonCoordinateList_Unique.add(coordinate);
                }
            }

            for (int coords = 0; coords < polygonCoordinateList_Unique.size() - 1; coords++) {
                Coordinate verticesCoords = polygonCoordinateList_Unique.get(coords);
                Coordinate verticesCoords2 = polygonCoordinateList_Unique.get(coords+1);

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
                if (v1.compareTo(v2) == 0) {
                    logger.error("identical vertices");
                    logger.error(polygonCoordinateList_Unique + "");
                }
                if (verticesCoords.equals(verticesCoords2)) {
                    logger.error("Identical coordinates");
                }
                
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

            Polygon p = new Polygon(polygonSegmentList, centroid);
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
    public Vertex calculate_center(List<Segment> segments) {
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
            Segment extra = new Segment(nextVertex, startingVertex, defaultThickness);
            sortedSegments.add(extra);
        }
        else if (segments.size() != sortedSegments.size()) {
            logger.error("Extra segments are given but not used, will discard them. Segments used: " +
                    sortedSegments.size() + ", Segments given: " + segments.size());

            for (Segment seg : segments) {
                logger.error(Arrays.toString(seg.getVertice1().getCoordinate()) + ", " + Arrays.toString(seg.getVertice2().getCoordinate()));
            }
            for (Segment seg : sortedSegments) {
                logger.error(Arrays.toString(seg.getVertice1().getCoordinate()) + ", " + Arrays.toString(seg.getVertice2().getCoordinate()));
            }
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
