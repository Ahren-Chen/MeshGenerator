package ca.mcmaster.cas.se2aa4.a2.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.Converter2DTo1D;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonNeighbourFinder;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.*;
import org.locationtech.jts.geom.*;


public class Generator {
    private int numOfPolygons;
    private int relaxationLevel;
    private static  final int width = 500;
    private static final int height = 500;
    private static final ParentLogger logger=new ParentLogger();
    private static final int X=25;// grid_size in X
    private static final int Y=25;// grid_size in Y
    public static final double accuracy= 0.01;
    private static final int defaultThickness = 3;

    //private static final RandomColor randomColor = null;
    private static final Random bag;

    static {
        try {
            bag = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("Random variable error");
            throw new RuntimeException(e);
        }
    }

    public Generator() throws NoSuchAlgorithmException {}

    public Mesh generate(String type, int numOfPolygons, int relaxationLevel)throws Exception{
        this.numOfPolygons = numOfPolygons;
        this.relaxationLevel = relaxationLevel;

        if (type.equals("gridMesh")){
            return gridMesh();
        }
        if( type.equals("randomMesh")){
            logger.error("Random mesh");
            return randomMesh();
        }
        else{
            return null;
        }
    }
    public Mesh gridMesh() {
        //Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        Vertex[][] vertices = new Vertex[width/X][height/Y];
        List<Segment> segmentList = new ArrayList<>();
        List<Polygon> polygonList= new ArrayList<>();
        //Coordinate currentVertexCoordinate;
        //Vertex currentVertex;

        int countV=0;
        // Create all the vertices
        for(int x = 0; x < width/X; x += 1) {
            for(int y = 0; y < height/Y; y += 1) {
                /*currentVertexCoordinate = new Coordinate(x, y);
                currentVertex = new Vertex(x*X, y*Y, false, 3, randomColor());
                currentVertex.setID(countV);

                coordinateVertexMap.put(currentVertexCoordinate, currentVertex);*/

                vertices[x][y]=new Vertex(x*X, y*Y, false, 1, RandomColor.randomColorDefault() );
                vertices[x][y].setID(countV++);
            }
        }
        int countS=0;
        for (int i = 0; i <width/X ; i+=1) {
            for (int j = 0; j <height/Y; j+=1) {
                Segment segment1=null;
                Segment segment2=null;
                if((i+1)<(width/X)){
                    segment1= new Segment(vertices[i][j], vertices[i+1][j], defaultThickness);
                    segment1.setID(countS++);
                    segmentList.add(segment1);
                }
                if((j+1)<(height/Y)){
                    segment2= new Segment(vertices[i][j], vertices[i][j+1], defaultThickness);
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
                Segment s4;
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
                Polygon p = new Polygon(set);
                polygonList.add(p);
                p.setID(countP++);
            }
        }
        PolygonNeighbourFinder.set_NeighborGrid(polygonList);
        ArrayList<Segment> segments_small  = PolygonNeighbourFinder.bonus_segment(polygonList);
        for (int i = 0; i < segments_small.size(); i++) {
            segments_small.get(i).setID(countS++);
            segmentList.add(segments_small.get(i));
        }

        //below is converting
        List<Structs.Vertex> listOfVertices_IO;
        List<Structs.Segment> listOfSegments_IO = new ArrayList<>();
        List <Structs.Polygon> listOfPolygons_IO= new ArrayList<>();

        Converter2DTo1D<Vertex, Structs.Vertex> converter2DTo1D= new ConvertVertex();

        listOfVertices_IO = converter2DTo1D.convert(vertices);

        for (Segment segment: segmentList) {
            Structs.Segment segmentConverted = segment.convertStruct();
            listOfSegments_IO.add(segmentConverted);
        }

        for (Polygon polygon: polygonList) {
            Vertex centroid = polygon.getCentroid();
            Structs.Vertex centroidConverted = centroid.convertStruct();
            listOfVertices_IO.add(centroidConverted);
            centroid.setID(countV++);

            Structs.Polygon polygonConverted = polygon.convertStruct();
            listOfPolygons_IO.add(polygonConverted);
        }

        return Mesh.newBuilder().addAllVertices(listOfVertices_IO).addAllSegments(listOfSegments_IO).addAllPolygons(listOfPolygons_IO).build();
    }

    private Mesh randomMesh() {

        Coordinate max= new Coordinate(width-accuracy, height-accuracy);

        List<Polygon> polygonList = null;
        Map<Coordinate, Vertex> centroids= randomVertices(numOfPolygons);

        int count=0;
        while(count<relaxationLevel){
             polygonList = Polygon.generate(centroids,3, 3, max);
             centroids.clear();

            for(Polygon polygon: polygonList){
                Vertex centroid = polygon.getCentroid();
                Coordinate coord = new CoordinateXY(centroid.getX(),centroid.getY());
                centroids.put(coord, centroid);
            }
            count++;
        }

        List<Vertex> vertexList= new ArrayList<>();
        List<Segment> segmentList= new ArrayList<>();

        assert polygonList != null;

        for (Polygon polygon: polygonList){
            List<Segment> segments = polygon.getSegments();
            segmentList.addAll(segments);
        }
        for(Segment s: segmentList){
            Vertex v1 = s.getVertice1();
            Vertex v2 = s.getVertice2();

            vertexList.add(v1);
            vertexList.add(v2);
        }

        vertexList.addAll(centroids.values());


        Collections.sort(segmentList);
        Collections.sort(vertexList);

        //remove duplicate
        for (int i = 0; i <segmentList.size()-1; i++) {
            Segment s0= segmentList.get(i);
            Segment s1=segmentList.get(i+1);
            if (s0.compareTo(s1)==0){
                segmentList.remove(i);
                i--;
            }
        }

        for (int i = 0; i < vertexList.size()-1; i++) {
            Vertex v0= vertexList.get(i);
            Vertex v1=vertexList.get(i+1);
            if (v0.compareTo(v1)==0){
                vertexList.remove(i);
                i--;
            }
        }
        //assign ID
        for (int i = 0; i <vertexList.size() ; i++) {
            Vertex v0 = vertexList.get(i);
            v0.setID(i);
            logger.error(i + "");
        }
        for (int i = 0; i < segmentList.size(); i++) {
            Segment s0= segmentList.get(i);
            s0.setID(i);
        }

        for (int i = 0; i < polygonList.size(); i++) {
            Polygon p= polygonList.get(i);
            p.setID(i);
        }

        //PolygonNeighbourFinder.set_NeighborGrid(polygonList);

        List<Structs.Vertex> listOfVertices_IO = new ArrayList<>();
        List<Structs.Segment> listOfSegments_IO = new ArrayList<>();
        List <Structs.Polygon> listOfPolygons_IO= new ArrayList<>();

        for(Vertex v: vertexList){
            Structs.Vertex vertex = v.convertStruct();
            listOfVertices_IO.add(vertex);
        }

        for(Segment segment: segmentList){
            listOfSegments_IO.add(segment.convertStruct());
        }

        //it is possible to have a method convert Polygons, just need to pass vertices to it
        for (Polygon polygon: polygonList) {
            Vertex centroid = polygon.getCentroid();
            Structs.Vertex centroidConverted = centroid.convertStruct();
            centroid.setID(vertexList.size());
            listOfVertices_IO.add(centroidConverted);


            Structs.Polygon polygonConverted = polygon.convertStruct();
            listOfPolygons_IO.add(polygonConverted);
        }

        return Mesh.newBuilder().addAllVertices(listOfVertices_IO).addAllSegments(listOfSegments_IO).addAllPolygons(listOfPolygons_IO).build();
    }
    private Hashtable<Coordinate, Vertex> randomVertices(int num) {
        int count=0;
        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();

        while(count<num){
            double x= bag.nextDouble(0, 5.0);
            x=((double)((int)(x*10000))/100);
            double y= bag.nextDouble(0, 5.0);
            y=((double)((int)(y*10000))/100);
            Vertex v= new Vertex(x,y, false, 3, RandomColor.randomColorDefault());
            Coordinate coord= new CoordinateXY(x,y);
            if(!randomVertices.contains(v)){
                randomVertices.put(coord, v);
                count++;
            }
        }

        return randomVertices;
    }
}

