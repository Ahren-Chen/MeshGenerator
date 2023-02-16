package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

public class DotGen {

    private static  final int width = 500;
    private static final int height = 500;
    private static final int square_size = 20;

    private static final int X=25;
    private static final int Y=25;

    private final Random bag = SecureRandom.getInstanceStrong();

    public DotGen() throws NoSuchAlgorithmException {

    }

    public Mesh generate() {
        Vertex[][] vertices = new Vertex[X][Y];
        List<Segment> segments = new ArrayList<>();

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        List<Vertex> vertices1D = new ArrayList<>();//this is a 1D array
        int count=0;

        // Create all the vertices
        for(int x = 0; x < X; x += 1) {
            for(int y = 0; y < Y; y += 1) {
                vertices[x][y]=Vertex.newBuilder().setX((double)x).setY((double)y).build();
                Vertex v1 = vertices[x][y];
                Property color = Property.newBuilder()
                        .setKey("rgb_color")
                        .setValue(randomColor())
                        .build();
                Vertex colored = Vertex.newBuilder(v1)
                        .addProperties(color)
                        .build();
                v1=colored;
                vertices1D.add(colored);

            }
        }

        for (int i = 0; i <X ; i+=1) {
            for (int j = 0; j <Y; j+=1) {
                Segment segment=null;
                if(i+25<X){
                    segment= Segment.newBuilder().setV1Idx(index1D(i,j)).setV2Idx(index1D(i+25,j)).build();
                }
                if(j+25<Y){
                    segment= Segment.newBuilder().setV1Idx(index1D(i,j)).setV2Idx(index1D(i,j+25)).build();
                }

                if(segment!=null){
                    List<Property> v1Color=vertices1D.get(segment.getV1Idx()).getPropertiesList();
                    List<Property> v2Color=vertices1D.get(segment.getV2Idx()).getPropertiesList();
                    Property segmentColor= Property.newBuilder()
                            .setKey("rgb_color")
                            .setValue((segmentColor(v1Color,v2Color)))
                            .build();
                    Segment segmentColored = Segment.newBuilder(segment)
                            .addProperties(segmentColor)
                            .build();
                    segments.add(segmentColored);
                }
            }
        }

        //System.out.println(vertices);
        System.out.println(segments.size());
        return Mesh.newBuilder().addAllVertices(vertices1D).addAllSegments(segments).build();
    }

    private String segmentColor(List<Property> vertex1, List<Property> vertex2) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to

        float[] colorVertex1 = extractColor(vertex1);
        float[] colorVertex2 = extractColor(vertex2);

        float red = (colorVertex1[0] + colorVertex2[0]) / 2;
        float green = (colorVertex1[1] + colorVertex2[1]) / 2;
        float blue = (colorVertex1[2] + colorVertex2[2]) / 2;

        return red + "," + green + "," + blue;
    }
    private float[] extractColor(List<Property> properties) {
        //This method extracts the color of an object based on their property
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }

        //If the property color is not found, return black
        if (val == null) {
            return new float[]{0, 0, 0};
        }

        String[] raw = val.split(",");
        float red = Float.parseFloat(raw[0]);
        float green = Float.parseFloat(raw[1]);
        float blue = Float.parseFloat(raw[2]);
        return new float[] {red,green,blue};
    }
    private static Vertex[][] Converter2D(Vertex[] vertices, int width){
        int height=vertices.length/width;
        int count=0;
        Vertex[][] vertices2D=new Vertex[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j <height ; j++) {
                vertices2D[i][j]=vertices[count++];
            }
        }
        return vertices2D;
    }
    private static Vertex[] Converter1D(Vertex[][] vertices){
        int count=0;
        Vertex[] vertices1D=new Vertex[vertices.length*vertices[0].length];

        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices[i].length; j++) {
                vertices1D[count++]=vertices[i][j];
            }
        }
        return vertices1D;
    }
    private static int index1D(int x, int y){
        int index=x*X+y;
        return index;
    }

    private static String randomColor(){

        Random bag = new Random();
        float red = (float)bag.nextInt(255)/255;
        float green = (float)bag.nextInt(255)/255;
        float blue = (float) bag.nextInt(255)/255;
        String colorCode = red + "," + green + "," + blue;
        return colorCode;
    }


    private List<String> remove_duplicate(List<Segment> segments,int begin, int end ,int len){
        ArrayList<String> arr = new ArrayList<>();
        for ( int j = begin;j<end;j++){
            arr.add((segments.get(j)).getV1Idx()+","+segments.get(j).getV2Idx());
        }
        return arr.stream().distinct().collect(Collectors.toList());
    }
    private void add_neighbor(List<Structs.Polygon> Polygons , List<Segment> Segments ,int len){
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
    private int calculate_center(List<Segment> segments,List<Integer> Segments){
        int[]arr = new int[]{0,0};
        for (int i = 0; i < segments.size(); i++) {
            arr[0] = arr[0] + test.idx1D_to2D(Segments.get(segments.get(i).getV1Idx()))[0];
            arr[1] = arr[1] + test.idx1D_to2D(Segments.get(segments.get(i).getV2Idx()))[1];

        }
        arr[0] = arr[0] /(2*segments.size());
        arr[1] = arr[1] /(2*segments.size());
        return arr[0]*500+arr[1];
    }
    private boolean check_for_polygon(List<Segment>segments,int begin,int end,int len){
        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = begin; j < end; j++) {
            arr.add(segments.get(j).getV1Idx());
            arr.add(segments.get(j).getV2Idx());
        }
        return (arr.stream().distinct().collect(Collectors.toList()).size())==len;
    }




}

