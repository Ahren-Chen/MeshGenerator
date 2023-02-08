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

    private static  final int width = 500;
    private static final int height = 500;
    private static final int square_size = 20;


    private final Random bag = SecureRandom.getInstanceStrong();

    public DotGen() throws NoSuchAlgorithmException {

    }

    public Mesh generate() {
        List<Vertex> vertices = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();


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
    private boolean check_for_polygon(List<Segment> segments, int begin ,int end,int len){
        ArrayList<Integer> arr= new ArrayList<>();
        for (int j = begin; j < end; j++) {
                arr.add(segments.get(j).getV1Idx());
                arr.add(segments.get(j).getV2Idx());

        }
        return arr.stream().distinct().collect(Collectors.toList()).size()==len;
    }



}
