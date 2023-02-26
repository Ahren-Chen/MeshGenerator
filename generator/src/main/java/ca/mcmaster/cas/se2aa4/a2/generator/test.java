package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonNeighbourFinder;

import java.awt.*;
import java.util.ArrayList;

public class test {
    static Polygon test_polygon(int x,int y) throws Exception {
        Color color = new Color(12, 12, 12, 1);

        Vertex v1 = new Vertex(x,y,false,1,color);
        Vertex v2 = new Vertex(x+20,y,false,1,color);
        Vertex v3 = new Vertex(x,y+20,false,1,color);
        Vertex v4 = new Vertex(x+20,y+20,false,1,color);


        Segment s1 = new Segment(v1,v2, 3);
        Segment s2 = new Segment(v1,v3, 3);
        Segment s3 = new Segment(v4,v3, 3);
        Segment s4 = new Segment(v4,v2, 3);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(s1);
        segments.add(s2);
        segments.add(s3);
        segments.add(s4);
        Polygon p = new Polygon(segments);
        return p;
    }
     static int idx2D_to1D(int[] arr){
        int idx = 0;
        if(arr[0]>1){
            idx = arr[0]*50000+arr[1];
            return idx;
        }else {
            idx = arr[1];
            return idx;
        }
    }
     static int[] idx1D_to2D(int n){
        int raw=0;
        int column=0;
        int[] index_2D = new int[2];

        if(n<=50000){
            column = n;
            index_2D[0] =raw;
            index_2D[1] = column;
            return index_2D;
        }else{
            raw = n/50000;
            column = n%50000;
            index_2D[0] = raw;
            index_2D[1] = column;
            return index_2D;
        }
    }
    public static void main(String[] args) throws Exception {
        for (int n:
             idx1D_to2D(62000)) {
            System.out.println(n);
        }
        int[] test =  new int[]{0,40};
        System.out.println(idx2D_to1D(test));

        ArrayList<Polygon> neighbor_test = new ArrayList<>();
        Polygon p1 = test_polygon(0,0);
        Polygon p2 = test_polygon(0,20);
        Polygon p3 = test_polygon(20,0);
        Polygon p4 = test_polygon(20,20);
        neighbor_test.add(p1);
        neighbor_test.add(p2);
        neighbor_test.add(p3);
        neighbor_test.add(p4);

        PolygonNeighbourFinder.set_NeighborGrid(neighbor_test);
        System.out.println(p1.getNeighbors().size());
        System.out.println(p1.getNeighbors().get(1).compare(p3.getCentroid()));




    }
}


