package ca.mcmaster.cas.se2aa4.a2.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

import logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.Converter2DTo1D;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.ConvertTo1DVertices;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonGeneratorRandom;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonNeighbourFinder;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import org.locationtech.jts.geom.*;

public class Generator {
    private int numOfPolygons;
    private int relaxationLevel;
    private int width;
    private int height;
    private static final ParentLogger logger=new ParentLogger();
    private static final double X=25.5;// grid_size in X
    private static final double Y=25.5;// grid_size in Y
    public static final double accuracy= 0.01;
    private double vertexThickness;
    private double segmentThickness;
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
    /**
     * generate method will get the requirement from user and choose which type of mesh
     * should it create
     * @param type String
     * @param relaxationLevel int
     * @param vThickness double
     * @param segThickness double
     * @return Mesh
     */
    public Mesh generate(String type, int numOfPolygons, int relaxationLevel, double vThickness, double segThickness,
                            int width, int height) throws Exception{
        this.numOfPolygons = numOfPolygons;
        this.relaxationLevel = relaxationLevel;
        this.vertexThickness = vThickness;
        this.segmentThickness = segThickness;
        this.width = width;
        this.height = height;

        if (type.equalsIgnoreCase("gridMesh")){
            logger.trace("gridMesh");
            return gridMesh(false);
        }
        if( type.equalsIgnoreCase("randomMesh")){
            logger.trace("Random mesh");
            return randomMesh();
        }
        if(type.equalsIgnoreCase("tetraMesh")){
            logger.trace("Tetrakis square tiling");
            return gridMesh(true);
        }
        else{
            return null;
        }
    }

    /**
     * gridMesh method which according to step 2 it will first create all the vertices and then segments and then polygon
     * before delivery it to IO all the list will go through a converter which make them into Structs.Vertex type same
     * thing with segment and polygon. finally create mesh ann return it.
     * the param is about bonus step it will generate TetrakisSquare mesh when it's true
     * @param TetrakisSquare a boolean variable of whether we are generating a TetrakisSquare or not
     * @return Mesh
     */
    public Mesh gridMesh(boolean TetrakisSquare) {
        //Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        Vertex[][] vertices = new Vertex[(int)(width/X)][(int)(height/Y)];
        List<Segment> segmentList = new ArrayList<>();
        List<Polygon> polygonList= new ArrayList<>();
        //Coordinate currentVertexCoordinate;
        //Vertex currentVertex;

        // Create all the vertices
        for(int x = 0; x < vertices.length; x += 1) {
            for(int y = 0; y < vertices[x].length; y += 1) {
                vertices[x][y]=new Vertex(x*X, y*Y, false, vertexThickness, RandomColor.randomColorDefault() );
            }
        }
        int countS=0;

        for (int i = 0; i <vertices.length; i+=1) {
            for (int j = 0; j <vertices[i].length; j+=1) {
                Segment segment1;
                Segment segment2;
                if((i+1)<(vertices.length)){
                    segment1= new Segment(vertices[i][j], vertices[i+1][j], segmentThickness);
                    segment1.setID(countS++);
                    segmentList.add(segment1);
                }
                if((j+1)<(vertices[i].length)){
                    segment2= new Segment(vertices[i][j], vertices[i][j+1], segmentThickness);

                    segment2.setID(countS++);
                    segmentList.add(segment2);
                }
            }
        }

        int countP=0;
        for (int i = 0; i < vertices.length-1; i++) {
            for (int j = 0; j < vertices[i].length-1; j++) {

                Segment s1 = segmentList.get(2 * (i) * (vertices[i].length) + 2 * j - i);
                Segment s2 = segmentList.get(2 * (i) * (vertices[i].length) + 1 + 2 * j - i);
                Segment s3 = segmentList.get(2 * (i) * (vertices[i].length) + 2 + 2 * j - i);
                Segment s4;
                if (i==vertices.length-2){
                    s4=segmentList.get(2 * (i+1) * (vertices[i].length) + j - (i + 1));
                }
                else{
                    s4 = segmentList.get(2 * (i + 1) * (vertices[i].length) + 1 + 2 * j - (i + 1));
                }

                List<Segment> set=new ArrayList<>();
                set.add(s1);
                set.add(s2);
                set.add(s3);
                set.add(s4);
                Polygon p = new Polygon(set, vertexThickness, segmentThickness);
                polygonList.add(p);
                p.setID(countP++);
            }
        }

        PolygonNeighbourFinder.set_NeighborGrid(polygonList);

        if (TetrakisSquare) {
            List<Segment> segments_small = PolygonNeighbourFinder.bonus_segment(polygonList);

            for (Segment s : segments_small) {
                s.setID(countS++);
                segmentList.add(s);
            }
        }

        //below is converting
        List<Structs.Vertex> listOfVertices_IO= new ArrayList<>();;
        List<Structs.Segment> listOfSegments_IO = new ArrayList<>();
        List <Structs.Polygon> listOfPolygons_IO= new ArrayList<>();
        Converter2DTo1D<Vertex, Vertex> convertTo1D = new ConvertTo1DVertices();

        List<Vertex> vertices1D= convertTo1D.convert(vertices);
        for (Polygon p: polygonList ) {
            Vertex centroid= p.getCentroid();
            centroid.setID(vertices1D.size());
            vertices1D.add(centroid);
        }

        for(Vertex v: vertices1D){
            Structs.Vertex vertex=v.convertToStruct();
            listOfVertices_IO.add(vertex);
        }

        for (Segment segment: segmentList) {
            Structs.Segment segmentConverted = segment.convertToStruct();
            listOfSegments_IO.add(segmentConverted);
        }
        for (Polygon p: polygonList){
            Structs.Polygon polygonConverted = p.convertToStruct();
            listOfPolygons_IO.add(polygonConverted);
        }

        return Mesh
                .newBuilder()
                .addAllVertices(listOfVertices_IO)
                .addAllSegments(listOfSegments_IO)
                .addAllPolygons(listOfPolygons_IO)
                .build();
    }
    /**
     * randomMesh method is similar to gridMesh method but, it's completely opposite order it's comFirst, it uses the
     * random vertex method to randomly generate the center point according to the segment and vertex according to the
     * polygon.
     * @return Mesh
     */

    private Mesh randomMesh() throws Exception {

        //Setting the max size of the width and length of the coordinates and what I should constrain it to
        Coordinate max= new Coordinate(width-accuracy, height-accuracy);

        //Create a mapping of centroid coordinates to their associated Vertex
        List<Polygon> polygonList = null;
        Map<Coordinate, Vertex> centroids = randomVertices(numOfPolygons);

        //Generate a new List of polygons and relax it as many times as specified
        int count=0;
        while(count<relaxationLevel){

            polygonList = PolygonGeneratorRandom.generatePolyRandom(centroids, max, vertexThickness, segmentThickness);
            centroids.clear();

            //For each polygon generated, map the coordinates to the centroid
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
            Vertex centroid = polygon.getCentroid();

            vertexList.add(centroid);
        }

        for(Segment s: segmentList){
            Vertex v1 = s.getVertice1();
            Vertex v2 = s.getVertice2();

            vertexList.add(v1);
            vertexList.add(v2);
        }

        Collections.sort(segmentList);
        Collections.sort(vertexList);

        //remove duplicate
        for (int i = 0; i <segmentList.size()-1; i++) {
            Segment s0 = segmentList.get(i);
            Segment s1 = segmentList.get(i+1);
            if (s0.compareTo(s1) == 0){
                segmentList.remove(i);
                i--;
            }
        }

        for (int i = 0; i < vertexList.size()-1; i++) {
            Vertex v0 = vertexList.get(i);
            Vertex v1 = vertexList.get(i+1);
            if (v0.compareTo(v1) == 0){
                vertexList.remove(i);
                i--;
            }
        }
        //assign ID

        for (int i = 0; i <vertexList.size() ; i++) {
            Vertex v0 = vertexList.get(i);
            v0.setID(i);
        }
        for (int i = 0; i < segmentList.size(); i++) {
            Segment s0= segmentList.get(i);
            s0.setID(i);
        }

        for (int i = 0; i < polygonList.size(); i++) {
            Polygon p= polygonList.get(i);
            p.setID(i);
        }

        PolygonNeighbourFinder.findPolygonNeighbours_Random(polygonList, accuracy);

        List<Structs.Vertex> listOfVertices_IO = new ArrayList<>();
        List<Structs.Segment> listOfSegments_IO = new ArrayList<>();
        List <Structs.Polygon> listOfPolygons_IO= new ArrayList<>();

        for(Vertex v: vertexList){
            Structs.Vertex vertex = v.convertToStruct();
            listOfVertices_IO.add(vertex);
        }

        for(Segment segment: segmentList){
            listOfSegments_IO.add(segment.convertToStruct());
        }

        //it is possible to have a method convert Polygons, just need to pass vertices to it
        for (Polygon polygon: polygonList) {
            Structs.Polygon polygonConverted = polygon.convertToStruct();
            listOfPolygons_IO.add(polygonConverted);
        }

        return Mesh
                .newBuilder()
                .addAllVertices(listOfVertices_IO)
                .addAllSegments(listOfSegments_IO)
                .addAllPolygons(listOfPolygons_IO)
                .build();
    }

    /**
     * randomVertices method Randomly generate the coordinates of x and y and use them to generate vertexes, and then
     * store them in a hashtable so that there is no need to worry about duplication. The variable num can control the
     * number of vertexes generated
     * @param num int value of the number of random vertices to generate
     * @return randomVertices
     */

    private Hashtable<Coordinate, Vertex> randomVertices(int num) throws Exception {

        int count=0;

        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();

        while(count<num){
            double x = bag.nextDouble(0, (double) width/100);
            x = ((double)((int)(x*10000))/100);
            double y = bag.nextDouble(0, (double) width/100);
            if(x<0 || y<0){
                throw new Exception();
            }
            y = ((double)((int)(y*10000))/100);

            Vertex v = new Vertex(x,y, false, vertexThickness, RandomColor.randomColorDefault());
            Coordinate coord = new CoordinateXY(x,y);

            if(!randomVertices.contains(v)){
                randomVertices.put(coord, v);
                count++;
            }
        }

        return randomVertices;
    }
}

