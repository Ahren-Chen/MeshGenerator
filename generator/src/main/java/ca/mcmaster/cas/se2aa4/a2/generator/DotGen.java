package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

public class DotGen {

    private static  final int width = 500;
    private static final int height = 500;
    private static final int square_size = 20;

    private static  final int X = 50000;
    private static final int Y = 50000;

    private final Random bag = SecureRandom.getInstanceStrong();

    public DotGen() throws NoSuchAlgorithmException {
    }

    public Mesh generate() {
        List<Vertex> vertices = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();


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



        for (int vertex = 0; vertex < vertices.size(); vertex++) {
            Vertex v1 = vertices.get(vertex);
            int red = this.bag.nextInt(255);
            int green = this.bag.nextInt(255);
            int blue = this.bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;

            Property color = Property.newBuilder()
                    .setKey("rgb_color")
                    .setValue(colorCode)
                    .build();
            Vertex colored = Vertex.newBuilder(v1)
                    .addProperties(color)
                    .build();
            verticesWithColors.add(colored);

            /**
             * Think about how to you connect a square with 4 dots
             *
             * If both conditions are true, a segment is created between the current vertex and the previous one,
             * with the same RGB color assigned to the segment as the vertices using the segmentColor method.
             *
             * If only the first condition is true,
             * another segment is created between the current vertex and the vertex four iterations ago,
             * with the same RGB color assigned to the segment as the vertices.
             *
             * took me a while to understand, so I added an explaination here
             */

            if (vertex % 100 != 0) {
                if (vertex % 4 != 0) {
                    Vertex v2 = verticesWithColors.get(vertex - 1);
                    Segment segment = Segment.newBuilder()
                            .setV1Idx(vertex - 1)
                            .setV2Idx(vertex)
                            .build();
                    Property segmentColor = Property.newBuilder()
                            .setKey("rgb_color")
                            .setValue(segmentColor(colored.getPropertiesList(), v2.getPropertiesList()))
                            .build();
                    Segment segmentColored = Segment.newBuilder(segment)
                            .addProperties(segmentColor)
                            .build();
                    segments.add(segmentColored);
                } else {
                    Vertex closeLoopV = verticesWithColors.get(vertex - 4);
                    Segment segmentExtra = Segment.newBuilder()
                            .setV1Idx(vertex - 4)
                            .setV2Idx(vertex)
                            .build();
                    Property segmentColorExtra = Property.newBuilder()
                            .setKey("rgb_color")
                            .setValue(segmentColor(colored.getPropertiesList(), closeLoopV.getPropertiesList()))
                            .build();
                    Segment segmentColoredExtra = Segment.newBuilder(segmentExtra)
                            .addProperties(segmentColorExtra)
                            .build();
                    segments.add(segmentColoredExtra);
                }
            }
        }

        System.out.println(segments.size());
        return Mesh.newBuilder().addAllVertices(verticesWithColors).addAllSegments(segments).build();
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
}
