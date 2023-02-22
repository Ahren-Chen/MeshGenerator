package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon {


    private Segment[] segments;
    private float[] color;
    private Vertex centroid;
    private Vertex current;
    private ArrayList<Polygon> neighbor = new ArrayList<>();

    public ArrayList<Polygon> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(ArrayList<Polygon> neighbor) {
        this.neighbor = neighbor;
    }

    public Vertex getCentroid() {
        return centroid;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public float[] getColor() {
        return color;
    }

    private ParentLogger logger= new ParentLogger();

    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }

    public Polygon(Segment[] segments){
        if(segments.length<3){
            logger.error("wrong lenght of segment in Polygon");
        }
        //generate polygon
    }

    public static List<Polygon> generate(List<List<Vertex>> vertices, int count) {
        // Generate count number of polygons using the given vertices
        // Return an array of the generated polygons
        return null;
    }
    /***
     *  This method takes in a list of polygons, a list of line segments, and an integer len.
     *  It iterates over each polygon and checks whether any other polygons share a complete set of line segments with it (meaning they are neighbors).
     *  It adds the index of any neighboring polygons to an ArrayList of neighbor indices.
     * @param Polygons
     * @param Segments
     * @param len
     */
    public ArrayList<Polygon>  setNeighbor(List<Structs.Polygon> Polygons , List<Structs.Segment> Segments , int len){

        for (int i = 0; i < Polygons.size();i++){
            ArrayList<Integer> neighbor_list = new ArrayList<>();
            for (int j = 0; j < Polygons.size();j++){
                ArrayList<String> arr = new ArrayList<>();
                for (int idx:Polygons.get(j).getSegmentIdxsList()){
                    arr.add((Segments.get(idx).getV1Idx()+","+Segments.get(idx).getV2Idx()));
                    if(arr.stream().distinct().collect(Collectors.toList()).size()==2*len-2){
                        neighbor_list.add(j);
                    }
                }
            }
        }

    }

    /***
     * This method takes in a list of line segments and a list of segment indices.
     * It calculates the center point of these line segments by taking the average x and y coordinates of their endpoints.
     * It then returns an integer value that represents the center point's position on a two-dimensional grid.
     * @param segments
     * @param Segments
     * @return
     */
    public int[] calculate_center(List<Segment> segments, List<Segment> Segments){

        int[]arr = {0,0};

        for (int i = 0; i < segments.size(); i++) {
            arr[0] = arr[0] + test.idx1D_to2D(Segments.get(segments.get(i).getV1Idx()))[0];
            arr[1] = arr[1] + test.idx1D_to2D(Segments.get(segments.get(i).getV2Idx()))[1];

        }

        arr[0] = arr[0] /(2*segments.size());
        arr[1] = arr[1] /(2*segments.size());

        return arr;
    }

    /***
     *  This method takes in a list of line segments,
     *  a starting index, an ending index, and an integer len.
     *  It checks whether the subset of line segments between the starting and ending indices forms a closed polygon with len sides.
     *  It does this by adding all the endpoints of the line segments to an ArrayList and checking whether the size of the ArrayList,
     *  after removing duplicates, is equal to len
     * @param segments
     * @param begin
     * @param end
     * @param len
     * @return
     */
    private boolean check_for_polygon(List<Structs.Segment>segments, int begin, int end, int len){

        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = begin; j < end; j++) {
            arr.add(segments.get(j).getV1Idx());
            arr.add(segments.get(j).getV2Idx());
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
}
