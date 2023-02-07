package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.*;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

public class DotGen {

    private final int width = 500;//x
    private final int height = 500;//y
    private final int square_size = 20;

    public Mesh generate() {
        Vertex[][] vertices = new Vertex[width][height];
        List<Segment> segments = new ArrayList<>();
        // Distribute colors randomly. Vertices are immutable, need to enrich them
        Vertex[] verticesWithColors = new Vertex[width*height];//this is a 1D array
        int count=0;

        Random bag = new Random();
        // Create all the vertices
        for(int x = 0; x < width; x += 1) {
            for(int y = 0; y < height; y += 1) {
                vertices[x][y]=Vertex.newBuilder().setX(x).setY(y).build();

                Vertex v1 = vertices[x][y];
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
                verticesWithColors[count++] = colored;
            }
        }
        //System.out.println(vertices);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {

                /*
                Vertex v2 = verticesWithColors.get(i - 1);
                Segment segment = Segment.newBuilder()
                        .setV1Idx(i - 1)
                        .setV2Idx(i)
                        .build();
                Property segmentColor = Property.newBuilder()
                        .setKey("rgb_color")
                        .setValue(segmentColor(colored.getPropertiesList(), v2.getPropertiesList()))
                        .build();
                Segment segmentColored = Segment.newBuilder(segment)
                        .addProperties(segmentColor)
                        .build();
                segments.add(segmentColored);*/
                if (i + 1 < width) {
                    Segment segment = Segment.newBuilder()
                            .setV1Idx(index1D(i,j,width,height))
                            .setV2Idx(index1D(i+1,j,width,height))
                            .build();
                    Property segmentColor = Property.newBuilder()
                            .setKey("rgb_color")
                            .setValue(segmentColor(colored.getPropertiesList(), v1.getPropertiesList()))
                            .build();

                }
            }
        }
        System.out.println(segments.size());
        return Mesh.newBuilder().addAllVertices(List.of(verticesWithColors)).addAllSegments(segments).build();
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
    private static int index1D(int x, int y, int width, int height){
        int index=x*width+y*height;
        return index;
    }

}
