package ca.mcmaster.cas.se2aa4.a2.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;


import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.*;
import org.locationtech.jts.geom.*;


public class Generator {

    private static  final int width = 500;
    private static final int height = 500;
    private static final ParentLogger logger=new ParentLogger();
    private static final int X=20;// grid_size in X
    private static final int Y=20;// grid_size in Y
    public static final double accuracy= 0.01;
    private static final Random bag;

    static {
        try {
            bag = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("Random variable error");
            throw new RuntimeException(e);
        }
    }

    public Generator() throws NoSuchAlgorithmException {

    }
    public Mesh generate(String type)throws Exception{
        if (type.equals("gridMesh")){
            return gridMesh();
        }
        if( type.equals("")){
            return randomMesh();
        }
        else{
            return null;
        }
    }
    public Mesh gridMesh() throws Exception{
        Vertex[][] vertices = new Vertex[width/X][height/Y];
        List<Segment> segmentList = new ArrayList<>();
        List<Polygon> polygonList= new ArrayList<>();

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

        int countP=0;
        for (int i = 0; i < vertices.length-1; i++) {
            for (int j = 0; j < vertices[i].length-1; j++) {

                Segment s1 = segmentList.get(2 * (i) * (height / Y) + 2 * j - i);
                Segment s2 = segmentList.get(2 * (i) * (height / Y) + 1 + 2 * j - i);
                Segment s3 = segmentList.get(2 * (i) * (height / Y) + 2 + 2 * j - i);
                Segment s4=null;
                if (i==vertices.length-2){
                    s4=segmentList.get(2 * (i+1) * (height / Y) + j - (i + 1));
                }
                else{
                    s4 = segmentList.get(2 * (i + 1) * (height / Y) + 1 + 2 * j - (i + 1));
                }

                List<Segment> set=new ArrayList<>();
                set.add(s1);
                set.add(s2);
                set.add(s3);
                set.add(s4);
                Polygon p= new Polygon(set);
                polygonList.add(p);
                p.setID(countP++);
            }
        }
        Polygon.setNeighbor(polygonList);


        //below is converting
        List<Structs.Vertex> vertices1D;//this is a 1D array
        List<Structs.Segment> segments = new ArrayList<>();
        List <Structs.Polygon> polygons= new ArrayList<>();

        Converter<Vertex, Structs.Vertex> converter= new ConvertVertex();
        vertices1D=converter.convert(vertices);

        for (Segment segment: segmentList) {
            Vertex v1= segment.getVertices()[0];
            Vertex v2= segment.getVertices()[1];
            Structs.Segment seg= Structs.Segment.newBuilder().setV1Idx(v1.getID()).setV2Idx(v2.getID()).build();
            ConvertVertex cv=(ConvertVertex) converter;
            String color= cv.converColor(segment.getColor());
            Structs.Property prop= Structs.Property.newBuilder().setKey("rgba_color").setValue(color).build();
            Structs.Segment newSeg=Structs.Segment.newBuilder(seg).addProperties(prop).build();
            segments.add(newSeg);

        }

        for (Polygon polygon: polygonList) {
            float[] color=polygon.getColor();
            String colorCode=toColorCode(color);
            Structs.Property prop= Structs.Property.newBuilder().setKey("rgba_color").setValue(colorCode).build();
            Vertex c=polygon.getCentroid();

            List<Segment> segment=polygon.getSegments();
            List<Integer> segmentIndex=new ArrayList<>();
            for (Segment s: segment) {
                segmentIndex.add(s.getID());
            }

            ArrayList<Polygon> list= polygon.getNeighbor();
            List<Integer> neighborID=new ArrayList<>();
            for (Polygon p: list) {
                neighborID.add(p.getID());
            }
            vertices1D.add(converter.convert(c));
            c.setID(countV++);
            //logger.error(vertices1D.get(c.getID()).getX() + " " +  vertices1D.get(c.getID()).getY());

            Structs.Polygon p=Structs.Polygon.newBuilder()
                    .setCentroidIdx(c.getID())
                    .addAllSegmentIdxs(segmentIndex)
                    .addAllNeighborIdxs(neighborID)
                    .addProperties(prop)
                    .build();
            polygons.add(p);
        }

        return Mesh.newBuilder().addAllVertices(vertices1D).addAllSegments(segments).addAllPolygons(polygons).build();
    }


    private static float[] randomColor(){

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

        return red + "," + blue +"," + green + "," + alpha;
    }

    private Mesh randomMesh()throws Exception{
        Coordinate max= new Coordinate(width-accuracy, height-accuracy);
        List<Polygon> polygonList=Polygon.generate(randomVertices(20),3, 3, max);
        List<Vertex> vertexList= new ArrayList<>();
        List<Segment> segmentList= new ArrayList<>();
        for( Polygon p: polygonList){
            List<Segment> segments=p.getSegments();
            for(Segment s: segments){
                segmentList.add(s);
            }
        }
        for(Segment s: segmentList){
            Vertex[] v=s.getVertices();
                vertexList.add(v[0]);
                vertexList.add(v[1]);
        }
        Collections.sort(segmentList);
        Collections.sort(vertexList);

        for (int i = 0; i <segmentList.size()-1; i++) {
            Segment s0= segmentList.get(i);
            Segment s1=segmentList.get(i+1);
            if (s0.compareTo(s1)==0){
                segmentList.remove(i);
                i--;
            }
        }

        for (int i = 0; i < vertexList.size(); i++) {
            Vertex s0= vertexList.get(i);
            Segment s1=segmentList.get(i+1);
            if (s0.compareTo(s1)==0){
                segmentList.remove(i);
                i--;
            }
        }

        return null;
    }
    private Hashtable<Coordinate, Vertex> randomVertices(int num)throws Exception{
        int count=0;
        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();

        while(count<num){
            double x= bag.nextDouble(0, 5.0);
            x=((double)((int)(x*10000))/100);
            double y= bag.nextDouble(0, 5.0);
            y=((double)((int)(x*10000))/100);
            Vertex v= new Vertex(x,y, false, 3, randomColor());
            Coordinate coord= new CoordinateXY(x,y);
            randomVertices.put(coord, v);
        }

        return randomVertices;
    }


}

