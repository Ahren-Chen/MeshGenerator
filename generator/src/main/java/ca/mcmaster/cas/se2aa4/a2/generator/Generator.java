package ca.mcmaster.cas.se2aa4.a2.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;


import Logging.ParentLogger;
import Extractor.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.*;




public class Generator {

    private static  final int width = 500;
    private static final int height = 500;
    private static final int square_size = 20;
    private static final ParentLogger logger=new ParentLogger();
    private static final int X=20;// grid_size in X
    private static final int Y=20;// grid_size in Y

    private final Random bag = SecureRandom.getInstanceStrong();

    public Generator() throws NoSuchAlgorithmException {

    }
    public Mesh generate(String type)throws Exception{
        if (type.equals("grid_mesh")){
            return gridMesh();
        }
        else{
            return null;
        }
    }
    public Mesh gridMesh() throws Exception{
        Vertex[][] vertices = new Vertex[width/X][height/Y];
        List<Segment> segmentList = new ArrayList<>();
        List<Polygon> polygonList= new ArrayList<>();

        Random bag=new Random();

        int countV=0;
        // Create all the vertices
        for(int x = 0; x < width/X; x += 1) {
            for(int y = 0; y < height/Y; y += 1) {
                vertices[x][y]=new Vertex(x*X, y*Y, false, 1, randomColor() );
                vertices[x][y].setID(countV++);
            }
        }
        int countS=0;
        for (int i = 0; i <width/X ; i+=1) {
            for (int j = 0; j <height/Y; j+=1) {
                Segment segment1=null;
                Segment segment2=null;
                if((i+1)<(width/X)){
                    segment1= new Segment(vertices[i][j], vertices[i+1][j]);
                    segment1.setID(countS++);
                    segmentList.add(segment1);
                }
                if((j+1)<(height/Y)){
                    segment2= new Segment(vertices[i][j], vertices[i][j+1]);
                    segment2.setID(countS++);
                    segmentList.add(segment2);
                }
            }
        }

        Converter converter= new ConvertVertex();

        for (int i = 0; i < vertices.length-1; i++) {
            for (int j = 0; j < vertices[i].length-1; j++) {
                int index=index1D(i,j);
                Segment s1= segmentList.get(2*(i)*(height/Y-1)-1+j);
                Segment s2= segmentList.get(2*(i)*(height/Y-1)+j);
                Segment s3= segmentList.get(2*(i)*(height/Y-1)+1+j);
                Segment s4= segmentList.get(2*(i+1)*(height/Y-1)+j);
                List<Segment> set=new ArrayList<>();
                set.add(s1);
                set.add(s2);
                set.add(s3);
                set.add(s4);
                Polygon p= new Polygon(set);
                polygonList.add(p);
            }
        }

        //below is converting
        List<Structs.Vertex> vertices1D = new ArrayList<>();//this is a 1D array
        List<Structs.Segment> segments = new ArrayList<>();
        List <Structs.Polygon> polygons= new ArrayList<>();

        vertices1D=converter.convert(vertices);

        for (Segment segment: segmentList) {
            Vertex v1= segment.getVertices()[0];
            Vertex v2= segment.getVertices()[1];


            Structs.Segment seg= Structs.Segment.newBuilder().setV1Idx(v1.getID()).setV2Idx(v2.getID()).build();
            segments.add(seg);
        }
        for (Polygon polygon: polygonList) {
            float[] color=polygon.getColor();
            String colorCode=toColorCode(color);
            Structs.Property prop= Structs.Property.newBuilder().setKey("rgba_color").setValue(colorCode).build();
            Vertex c=polygon.getCentroid();


            Segment[] segment=polygon.getSegments();

            vertices1D.add((Structs.Vertex) converter.convert(c));
            c.setID(countV++);

            Structs.Polygon p=Structs.Polygon.newBuilder()
                    .setCentroidIdx(c.getID())
                    .setSegmentIdxs(0, segment[0].getID())
                    .setSegmentIdxs(1, segment[1].getID())
                    .setSegmentIdxs(2, segment[2].getID())
                    .setSegmentIdxs(3, segment[3].getID())
                    .addProperties(prop)
                    .build();
            polygons.add(p);
        }

        return Mesh.newBuilder().addAllVertices(vertices1D).addAllSegments(segments).addAllPolygons(polygons).build();
    }

    private String segmentColor(List<Property> vertex1, List<Property> vertex2) throws Exception{
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        float[] colorVertex1;
        float[] colorVertex2;

        try {
        colorVertex1 = Extractor.extractColor(vertex1);
        colorVertex2 = Extractor.extractColor(vertex2);
        }
        catch (Exception e){
            throw e;
        }


        float red = (colorVertex1[0] + colorVertex2[0]) / 2;
        float green = (colorVertex1[1] + colorVertex2[1]) / 2;
        float blue = (colorVertex1[2] + colorVertex2[2]) / 2;

        String color= red + "," + blue +"," + green + "," +1;
        return color;
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
        int index=x-1*height/Y-1+y;
        return index;
    }

    private static float[] randomColor(){

        Random bag = new Random();
        float red = (float)bag.nextInt(255)/255;
        float green = (float)bag.nextInt(255)/255;
        float blue = (float) bag.nextInt(255)/255;

        return new float[] {red,green, blue, 1};
    }

    private static String toColorCode(float[] color){
        float red=color[0];
        float green=color[1];
        float blue=color[2];
        float alpha=color[3];

        String colorCode= red + "," + blue +"," + green + "," +1;
        return colorCode;
    }


}

