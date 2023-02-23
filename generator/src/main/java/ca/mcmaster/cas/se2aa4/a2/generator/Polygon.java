package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

public class Polygon {

    private ArrayList<Segment> segments= new ArrayList<>();
    private float[] color;
    private Vertex centroid;
    private Vertex current;

    private static final int defaultThickness = 3;
    private ArrayList<Polygon> neighbor = new ArrayList<>();
    private static final ParentLogger logger= new ParentLogger();
    public Polygon(ArrayList<Vertex> Vertexs) {


        // Calculate centroid and set it to the centroid instance variable
        // Set current to the first vertex in the array
    }

    public Polygon(List<Segment> segments)throws Exception{
        if(segments.size()<3){
            logger.error("wrong length of segment in Polygon");
        }
        this.segments.addAll(segments);

        //generate polygon
        centroid= this.calculate_center(this.segments);
    }

    public ArrayList<Polygon> getNeighbor() {
        return neighbor;
    }



    public Vertex getCentroid() {
        return centroid;
    }

    public List<Segment> getSegments() {
        return (List)segments;
    }

    public float[] getColor() {
        if(color==null){
            return randomColor();
        }
        return color;
    }

    public void setSegments(Segment[] segments) {
        this.segments = new ArrayList<>(Arrays.asList(segments));
    }
    public boolean compare(Polygon p) {
        if (p.centroid.compare(this.centroid)){
            return true;
        }
        return false;
    }



    public static List<Polygon> generate (List<List<Vertex>> vertices, int vertexThickness, int segmentThickness, Coordinate maxSize) throws Exception {
        // Generate count number of polygons using the given vertices
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        List<Polygon> polygonList = new ArrayList<>();

        List<Coordinate> sites = new ArrayList<>();
        for (List<Vertex> row: vertices) {
            for (Vertex v: row) {
                double X = v.getX();
                double Y = v.getY();

                sites.add(new Coordinate(X, Y));
            }
        }
        voronoi.setSites(sites);
        voronoi.setTolerance(0.01);

        PrecisionModel precision = new PrecisionModel(0.01);

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
                        v = new Vertex(verticesCoords.getX(), verticesCoords.getY(), false, defaultThickness, randomColor());
                    }
                    else {
                        v = new Vertex(verticesCoords.getX(), verticesCoords.getY(), false, vertexThickness, randomColor());
                    }

                    coordinateVertexMap.put(verticesCoords, v);
                }

                Coordinate verticesCoords2 = polygon.getCoordinates()[coords+1];

                Vertex v1 = coordinateVertexMap.get(verticesCoords);
                Vertex v2 = coordinateVertexMap.get(verticesCoords2);

                Segment polygonSegment = new Segment(v1, v2);
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
    public ArrayList<Polygon> setNeighbor(ArrayList<Polygon> Polygons ){
        int len = 4;
        for (int i = 0; i < Polygons.size();i++){
            ArrayList<Polygon> neighbor_list = new ArrayList<>();
            for (int j = 0; j < Polygons.size();j++){
                ArrayList<Segment> arr = new ArrayList<>();
                for (Polygon p : Polygons){
                    if(p.equals(Polygons.get(j))){
                        break;
                    } else if (if_neighbor(p)) {
                        neighbor_list.add(p);
                    }
                }
            }
        }
        return null;
    }

    /***
     * This method takes in a list of line segments and a list of segment indices.
     * It calculates the center point of these line segments by taking the average x and y coordinates of their endpoints.
     * It then returns an integer value that represents the center point's position on a two-dimensional grid.
     * @param
     * @return
     */
    public Vertex calculate_center(ArrayList<Segment>segments)throws Exception{

        double[]arr = {0,0};
        float[] color = new float[4];

        for (int i = 0; i < segments.size(); i++) {
            arr[0] = arr[0] + segments.get(i).getVertice1().getX();
            arr[1] = arr[1] + segments.get(i).getVertice1().getY();
            color[0] = color[0]+segments.get(i).getColor()[0]/4;
            color[1] = color[1]+segments.get(i).getColor()[1]/4;
            color[2] = color[2]+segments.get(i).getColor()[2]/4;
            color[3] = color[3]+segments.get(i).getColor()[3]/4;
        }

        arr[0] = arr[0] /(2*segments.size());
        arr[1] = arr[1] /(2*segments.size());

        Vertex center  = new Vertex(arr[0],arr[1],true,1, color);

        return center;
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
        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = 0; j < segments.size(); j++) {
            arr.add(segments.get(j).getVertice1().getID());
            arr.add(segments.get(j).getVertice2().getID());
        }
        return (arr.stream().distinct().collect(Collectors.toList()).size())==len;
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
     * @param len
     * @return
     */
    private List<String> remove_duplicate(List<Structs.Segment> segments, int begin, int end , int len){
        ArrayList<String> arr = new ArrayList<>();
        for ( int j = begin;j<end;j++){
            arr.add((segments.get(j)).getV1Idx()+","+segments.get(j).getV2Idx());
        }
        return arr.stream().distinct().collect(Collectors.toList());
    }
    public static float[] avergeColor_p(float[] color1,float[] color2,float[] color3,float[] color4) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        float[] color = new float[3];
        color[0] = (color1[0] + color2[0]+color3[0]+color4[0]) / 4;
        color[1] = (color1[1] + color2[1]+color3[1]+color4[1]) / 4;
        color[2] = (color1[2] + color2[2]+color3[2]+color4[2]) / 4;
        color[3] = (color1[3] + color2[3]+color3[3]+color4[3]) / 4;
        return color;
    }
    private static float[] randomColor(){

        Random bag = new Random();
        float red = (float)bag.nextInt(255)/255;
        float green = (float)bag.nextInt(255)/255;
        float blue = (float) bag.nextInt(255)/255;

        return new float[] {red,green, blue, 1};
    }
    private boolean if_neighbor(Polygon p){
        for (int i = 0; i < this.segments.size(); i++) {
            for (int j = 0; j < this.segments.size(); j++) {
                if(p.segments.get(j).compare(this.segments.get(i))){
                    return true;
                }
            }
        }
        return false;
    }

    private static Coordinate modifyCoords(Coordinate coords, Coordinate maxSize) {
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

        return coords;
    }
}
