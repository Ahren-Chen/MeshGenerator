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

    private static final int width = 500;//x
    private static final int height = 500;//y
    private static final int square_size = 20;

    private static final int X=500;
    private static final int Y=500;

    public Mesh generate() {
        Vertex[][] vertices = new Vertex[X][Y];
        List<Segment> segments = new ArrayList<>();
        List<Structs.Polygon> polygons = new ArrayList<>();
        int len = 4;            // the value of len is to represent how many segment can make a polygon


        // Distribute colors randomly. Vertices are immutable, need to enrich them
        List<Vertex> vertices1D = new ArrayList<>();//this is a 1D array
        int count=0;

        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build());
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build());
            }
        }

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        List<Vertex> verticesWithColors = new ArrayList<>();
        Random bag = new Random();
        //System.out.println(vertices);



        for (int vertex = 0; vertex < vertices.size(); vertex++) {
            Vertex v1 = vertices.get(vertex);
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;

            Property color = Property.newBuilder()
                    .setKey("rgb_color")
                    .setValue(colorCode)
                    .build();
            Vertex colored = Vertex.newBuilder(v1)
                    .addProperties(color)
                    .build();
            verticesWithColors.add(colored);

        for (int i = 0; i <X ; i+=25) {
            for (int j = 0; j <Y; j+=25) {
                Segment segment=null;
                if(i+25<=X){
                    segment= Segment.newBuilder().setV1Idx(index1D(i,j)).setV2Idx(index1D(i+25,j)).build();
                }
                if(j+25<=Y){
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
        for (int i = 0; i < segments.size()-len; i = i +len) {
            ArrayList<Integer> arr = new ArrayList<>();
            if (check_for_polygon(segments,i,i+len,len)){
                for (int j = 0; j <len; j++) {
                    arr.add(i+j);
                }
                Structs.Polygon.newBuilder().addAllSegmentIdxs(arr);
            }

        }



        //System.out.println(vertices);
        System.out.println(segments.size());
        return Mesh.newBuilder().addAllVertices(vertices1D).addAllSegments(segments).build();
    }

    private String segmentColor(List<Property> vertex1, List<Property> vertex2) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to

        int[] colorVertex1 = extractColor(vertex1);
        int[] colorVertex2 = extractColor(vertex2);

        int red = (colorVertex1[0] + colorVertex2[0]) / 2;
        int green = (colorVertex1[1] + colorVertex2[1]) / 2;
        int blue = (colorVertex1[2] + colorVertex2[2]) / 2;

        return red + "," + green + "," + blue;
    }
    private int[] extractColor(List<Property> properties) {
        //This method extracts the color of an object based on their property
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }

        //If the property color is not found, return black
        if (val == null) {
            return new int[]{0, 0, 0};
        }

        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new int[] {red,green,blue};
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
        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);
        String colorCode = red + "," + green + "," + blue;
        return colorCode;
    }

}

