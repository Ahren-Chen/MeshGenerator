package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.SelfConverter;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Polygon implements SelfConverter<Structs.Polygon> {
    private final List<Segment> segments;
    private final Color color;
    private final Vertex centroid;
    private List<Polygon> neighbors = new ArrayList<>();
    private static final ParentLogger logger= new ParentLogger();
    private final ConvertColor colorConverter = new ConvertColor();
    private int ID=-1;
    private static final int defaultThickness = 3;

    public Polygon(List<Segment> segments)throws Exception{
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon");
        }
        if(!check_for_polygon(segments)){
            logger.error("Segment Given not a Polygon");
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
        return new ArrayList<>(neighbors);
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
    public void setNeighbors(ArrayList<Polygon>polygons){
        this.neighbors = polygons;
    }


    public static List<Polygon> generate (Hashtable<Coordinate, Vertex> vertices, int vertexThickness, int segmentThickness, Coordinate maxSize) throws Exception {
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

        for (int i = 0; i < polygonsGeometry.getDimension(); i++) {
            polygonGeometryList.add(polygonsGeometry.getGeometryN(i));
        }

        for (Geometry polygon : polygonGeometryList) {
            List<Segment> polygonSegmentList = new ArrayList<>();

            for (int coords = 0; coords < polygon.getCoordinates().length - 1; coords++) {
                Coordinate verticesCoords = polygon.getCoordinates()[coords];
                modifyCoords(verticesCoords, maxSize);

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

                Coordinate verticesCoords2 = polygon.getCoordinates()[coords+1];

                Vertex v1 = coordinateVertexMap.get(verticesCoords);
                Vertex v2 = coordinateVertexMap.get(verticesCoords2);

                Segment polygonSegment = new Segment(v1, v2, segmentThickness);
                polygonSegmentList.add(polygonSegment);
            }

            Polygon p = new Polygon(polygonSegmentList);
            polygonList.add(p);
        }

        return polygonList;
    }
    /***
     *  This method takes in a list of polygons, a list of line segments, and an integer len.
     *  It iterates over each polygon and checks whether any other polygons share a complete set of line segments with it (meaning they are neighbors).
     *  It adds the index of any neighboring polygons to an ArrayList of neighbor indices.
     * @param Polygons
     */
    public static void set_Neighbor(List<Polygon> Polygons){
        for (int i = 0; i < Polygons.size();i++){
            ArrayList<Polygon> neighbor_list = new ArrayList<>();
            for (int j = 0; j < Polygons.size();j++){
                ArrayList<Segment> arr = new ArrayList<>();
                if(Polygons.get(i).compare(Polygons.get(j))){
                } else if (Polygons.get(i).if_neighbor(Polygons.get(j))) {
                    neighbor_list.add(Polygons.get(j));
                }
            }
            Polygons.get(i).setNeighbors(neighbor_list);
        }
    }

    /***
     * This method takes in a list of line segments and a list of segment indices.
     * It calculates the center point of these line segments by taking the average x and y coordinates of their endpoints.
     * It then returns an integer value that represents the center point's position on a two-dimensional grid.
     * @param
     * @return
     */
    public Vertex calculate_center(List<Segment> segments) throws Exception {
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

        cords[0] /= 2 * segments.size();
        cords[1] /= 2 * segments.size();

        int segmentSize = segments.size();

        Red /= segmentSize;
        Green /= segmentSize;
        Blue /= segmentSize;
        Alpha /= segmentSize;

        color = new Color(Red, Green, Blue, Alpha);

        return new Vertex(cords[0], cords[1], true, 3, color);
    }

    /***
     *  This method takes in a list of line segments,
     *  It checks whether the subset of line segments between the starting and ending indices forms a closed polygon with len sides.
     *  It does this by adding all the endpoints of the line segments to an ArrayList and checking whether the size of the ArrayList,
     *  after removing duplicates, is equal to len
     * @param segments
     * @return
     */
    private boolean check_for_polygon(List<Segment>segments){
        int len = 4;
        List<Integer> arr = new ArrayList<>();
        for (Segment segment : segments) {
            arr.add(segment.getVertice1().getID());
            arr.add(segment.getVertice2().getID());
        }
        return arr.size() == len;
    }

    /***
     *  This method takes in a list of line segments, a starting index, an ending index,
     *  and an integer len.
     *  It creates an ArrayList of strings representing the endpoints of the line segments in the specified subset,
     *  then removes any duplicates from this ArrayList and returns the resulting list.
     *  This is useful for checking whether a subset of line segments represents a closed polygon,
     *  as in the check_for_polygon method.
     * @param segments
     * @param begin
     * @param end
     * @return
     */
    private List<String> remove_duplicate(List<Structs.Segment> segments, int begin, int end){
        List<String> arr = new ArrayList<>();
        for ( int j = begin;j<end;j++){
            arr.add((segments.get(j)).getV1Idx()+","+segments.get(j).getV2Idx());
        }
        return arr;
    }
    /*public static float[] avergeColor_p(float[] color1,float[] color2,float[] color3,float[] color4) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        float[] color = new float[3];
        color[0] = (color1[0] + color2[0]+color3[0]+color4[0]) / 4;
        color[1] = (color1[1] + color2[1]+color3[1]+color4[1]) / 4;
        color[2] = (color1[2] + color2[2]+color3[2]+color4[2]) / 4;
        color[3] = (color1[3] + color2[3]+color3[3]+color4[3]) / 4;
        return color;
    }*/
    private boolean if_neighbor(Polygon p){
        for (int i = 0; i < this.segments.size(); i++) {
            for (int j = 0; j < this.segments.size(); j++) {
                if(this.getSegments().get(i).compare(p.getSegments().get(j))){
                    return true;
                }
            }
        }
        return false;
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

    public Structs.Polygon convert() {
        String polygonColor = colorConverter.convert(this.color);
        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(polygonColor).build();

        List<Integer> segmentIndexList = new ArrayList<>();

        for (Segment s: this.segments) {
            int segmentIdx = s.getID();
            segmentIndexList.add(segmentIdx);
        }

        List<Integer> neighborID = new ArrayList<>();

        for (Polygon p: this.neighbors) {
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
