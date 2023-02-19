package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon {
    int centroid_idx;
    ArrayList<Integer> segment_idxs = new ArrayList<>();
    ArrayList<Integer> neighbor_idxs = new ArrayList<>();
    HashMap<String,String> properties = new HashMap<>();

    private void add_neighbor(List<Structs.Polygon> Polygons , List<Structs.Segment> Segments , int len){
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
    private int calculate_center(List<Structs.Segment> segments, List<Integer> Segments){
        int[]arr = new int[]{0,0};
        for (int i = 0; i < segments.size(); i++) {
            arr[0] = arr[0] + test.idx1D_to2D(Segments.get(segments.get(i).getV1Idx()))[0];
            arr[1] = arr[1] + test.idx1D_to2D(Segments.get(segments.get(i).getV2Idx()))[1];

        }
        arr[0] = arr[0] /(2*segments.size());
        arr[1] = arr[1] /(2*segments.size());
        return arr[0]*500+arr[1];
    }
    private boolean check_for_polygon(List<Structs.Segment>segments, int begin, int end, int len){
        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = begin; j < end; j++) {
            arr.add(segments.get(j).getV1Idx());
            arr.add(segments.get(j).getV2Idx());
        }
        return (arr.stream().distinct().collect(Collectors.toList()).size())==len;
    }
    private List<String> remove_duplicate(List<Structs.Segment> segments, int begin, int end , int len){
        ArrayList<String> arr = new ArrayList<>();
        for ( int j = begin;j<end;j++){
            arr.add((segments.get(j)).getV1Idx()+","+segments.get(j).getV2Idx());
        }
        return arr.stream().distinct().collect(Collectors.toList());
    }
}
