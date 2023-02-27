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
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;


public class Generator {
    private int numOfPolygons;
    private int relaxationLevel;
    private static  final int width = 500;
    private static final int height = 500;
    private static final ParentLogger logger=new ParentLogger();
    private static final double X=25.5;// grid_size in X
    private static final double Y=25.5;// grid_size in Y
    public static final double accuracy= 0.01;
    private double vertexThickness;
    private double segmentThickness;

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

    public Mesh generate(String type, int numOfPolygons, int relaxationLevel, double vThickness, double segThickness) {
        this.numOfPolygons = numOfPolygons;
        this.relaxationLevel = relaxationLevel;
        this.vertexThickness = vThickness;
        this.segmentThickness = segThickness;

        if (type.equalsIgnoreCase("gridMesh")){
            logger.trace("gridMesh");
            return gridMesh(false);
        }
        if( type.equalsIgnoreCase("randomMesh")){
            logger.trace("Random mesh");
            return randomMesh();
        }
        if(type.equalsIgnoreCase("TetrakisSquare")){
            logger.trace("Tetrakis square tiling");
            return gridMesh(true);
        }
        else{
            return null;
        }
    }
    public Mesh gridMesh(boolean TetrakisSquare) {
        //Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();
        Vertex[][] vertices = new Vertex[(int)(width/X)][(int)(height/Y)];
        List<Segment> segmentList = new ArrayList<>();
        List<Polygon> polygonList= new ArrayList<>();
        //Coordinate currentVertexCoordinate;
        //Vertex currentVertex;

        int countV=0;
        // Create all the vertices
        for(int x = 0; x < vertices.length; x += 1) {
            for(int y = 0; y < vertices[x].length; y += 1) {
                vertices[x][y]=new Vertex(x*X, y*Y, false, vertexThickness, RandomColor.randomColorDefault() );
            }
        }
        int countS=0;

        for (int i = 0; i <vertices.length; i+=1) {
            for (int j = 0; j <vertices[i].length; j+=1) {
                Segment segment1=null;
                Segment segment2=null;
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
                Polygon p = new Polygon(set);
                polygonList.add(p);
                p.setID(countP++);
            }
        }

        PolygonNeighbourFinder.set_NeighborGrid(polygonList);
        if (TetrakisSquare) {
            ArrayList<Segment> segments_small = PolygonNeighbourFinder.bonus_segment(polygonList);

            for (int i = 0; i < segments_small.size(); i++) {
                Segment s = segments_small.get(i);
                s.setID(countS++);
                segmentList.add(segments_small.get(i));
            }
        }

        //below is converting
        List<Structs.Vertex> listOfVertices_IO= new ArrayList<>();;
        List<Structs.Segment> listOfSegments_IO = new ArrayList<>();
        List <Structs.Polygon> listOfPolygons_IO= new ArrayList<>();

        //add all the centroids to the list beforehand
        Converter2DTo1D<Vertex, Structs.Vertex> converter2DTo1D= new ConvertVertex();

        List<Vertex> vertices1D= converter2DTo1D.convert(vertices);
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

        for (Polygon polygon: polygonList) {
            Structs.Polygon polygonConverted = polygon.convertToStruct();
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
        //List<Segment> small_segments = PolygonNeighbourFinder.bonus_segment(polygonList);

        List<Vertex> vertexList= new ArrayList<>();
        List<Segment> segmentList= new ArrayList<>();

        assert polygonList != null;

        for (Polygon polygon: polygonList){
            List<Segment> segments = polygon.getSegments();
            segmentList.addAll(segments);
            Vertex centroid = polygon.getCentroid();

            vertexList.add(centroid);
        }

        //segmentList.addAll(small_segments);

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

        findPolygonNeighbours_Random(polygonList);

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

    private void findPolygonNeighbours_Random(List<Polygon> polygonList) {
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        Map<Coordinate, Vertex> centroidCordsToVertex = new HashMap<>();

        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();
            Coordinate cord = new Coordinate(centroid.getX(), centroid.getY());

            centroidCordsToVertex.put(cord, centroid);
        }

        PrecisionModel precisionModel = new PrecisionModel(accuracy);
        GeometryFactory triangulationFactory = new GeometryFactory(precisionModel);

        triangulationBuilder.setSites(centroidCordsToVertex.keySet());
        triangulationBuilder.setTolerance(accuracy);
        Geometry triangles = triangulationBuilder.getTriangles(triangulationFactory);

        Map<Vertex, Set<Vertex>> VertexNeighbours = new HashMap<>();
        Set<Vertex> neighbours = new HashSet<>();

        for (int triangleNum = 0; triangleNum < triangles.getNumGeometries(); triangleNum++) {
            Geometry triangle = triangles.getGeometryN(triangleNum);

            Coordinate c1 = triangle.getCoordinates()[0];
            Coordinate c2 = triangle.getCoordinates()[1];
            Coordinate c3 = triangle.getCoordinates()[2];

            Vertex v1 = centroidCordsToVertex.get(c1);
            Vertex v2 = centroidCordsToVertex.get(c2);
            Vertex v3 = centroidCordsToVertex.get(c3);
            //logger.error(v1.getID() + ", " + v2.getID() + ", " + v3.getID());

            if (VertexNeighbours.containsKey(v1)) {
                neighbours = VertexNeighbours.get(v1);
            }

            neighbours.add(v2);
            neighbours.add(v3);
            VertexNeighbours.put(v1, neighbours);

            neighbours = new HashSet<>();

            if (VertexNeighbours.containsKey(v2)) {
                neighbours = VertexNeighbours.get(v2);
            }

            neighbours.add(v1);
            neighbours.add(v3);
            VertexNeighbours.put(v2, neighbours);

            neighbours = new HashSet<>();

            if (VertexNeighbours.containsKey(v3)) {
                neighbours = VertexNeighbours.get(v3);
            }

            neighbours.add(v1);
            neighbours.add(v2);
            VertexNeighbours.put(v3, neighbours);

            neighbours = new HashSet<>();
        }

        /*for (Vertex v : VertexNeighbours.keySet()) {
            logger.error("key: " + v.getID());
            Set<Vertex> set = VertexNeighbours.get(v);
            for (Vertex vetex : set) {
                logger.error("value: " + vetex.getID());
            }
        }*/

        for (Polygon poly : polygonList) {
            Vertex centroid = poly.getCentroid();
            //logger.error(centroid.getID() + "");

            Set<Vertex> centroidNeighboursSet = VertexNeighbours.get(centroid);
            //logger.error(centroidNeighboursSet + "");
            List<Vertex> centroidNeighbours = centroidNeighboursSet.stream().toList();

            poly.setNeighbors(centroidNeighbours);
        }
    }
}

