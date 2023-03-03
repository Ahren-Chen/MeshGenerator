package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.generator.Utility.ConvertTo1DVertices;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonGeneratorRandom;
import ca.mcmaster.cas.se2aa4.a2.generator.Utility.PolygonNeighbourFinder;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class generatorTest {

    @Test
    public void meshIsNotNull() throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh aMesh = generator.generate("gridMesh", 50, 10, 3, 3);
            assertNotNull(aMesh);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void generatePolygon() throws Exception{
        int countV = 0;
        int width = 500;
        int height = 500;
        int Y = 100;
        int X = 100;

        Vertex[][] vertices = new Vertex[width/X][height/Y];

        // Create all the vertices
        for(int x = 0; x < width/X; x += 1) {
            for(int y = 0; y < height/Y; y += 1) {
                vertices[x][y]=new Vertex(x*X, y*Y, false, 1, new Color (0.1f, 0.1f, 0.1f, 1) );
                vertices[x][y].setID(countV++);
            }
        }

        List<List<Vertex>> verticesList = new ArrayList<>();

        for (Vertex[] row : vertices) {
            List<Vertex> temp = new ArrayList<>(Arrays.asList(row));
            verticesList.add(temp);
        }
        ConvertTo1DVertices converter = new ConvertTo1DVertices();
        List<Vertex> verticeList1D = converter.convert(verticesList);

        Map<Coordinate, Vertex> coordinateVertexMap = new HashMap<>();

        for (Vertex v : verticeList1D) {
            Coordinate cord = new Coordinate(v.getX(), v.getY());
            coordinateVertexMap.put(cord, v);
        }
        Coordinate maxSize = new Coordinate(width, height);
        PolygonGeneratorRandom.generatePolyRandom(coordinateVertexMap, maxSize, 3 ,3);
    }

    @Test
    public void gridGenerationTest()throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh myMesh = generator.generate("gridMesh", 50, 10, 3, 3);
            MeshFactory factory = new MeshFactory();
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void TetrakisSquareTest()throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh myMesh = generator.generate("TetrakisSquare", 50, 10, 3, 3);
            MeshFactory factory = new MeshFactory();
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void RandomGenerationTest()throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh myMesh = generator.generate("randomMesh", 200, 1, 3 , 3);
            MeshFactory factory = new MeshFactory();
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    static Polygon test_polygon(int x,int y) throws Exception {
        Color color = new Color(12, 12, 12, 1);

        Vertex v1 = new Vertex(x,y,false,1,color);
        Vertex v2 = new Vertex(x+20,y,false,1,color);
        Vertex v3 = new Vertex(x,y+20,false,1,color);
        Vertex v4 = new Vertex(x+20,y+20,false,1,color);


        Segment s1 = new Segment(v1,v2, 3);
        Segment s2 = new Segment(v1,v3, 3);
        Segment s3 = new Segment(v4,v3, 3);
        Segment s4 = new Segment(v4,v2, 3);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(s1);
        segments.add(s2);
        segments.add(s3);
        segments.add(s4);
        Polygon p = new Polygon(segments, 3, 3);
        return p;
    }
    @Test
    static int idx2D_to1D(int[] arr){
        int idx = 0;
        if(arr[0]>1){
            idx = arr[0]*50000+arr[1];
            return idx;
        }else {
            idx = arr[1];
            return idx;
        }
    }
    @Test
    static int[] idx1D_to2D(int n){
        int raw=0;
        int column=0;
        int[] index_2D = new int[2];

        if(n<=50000){
            column = n;
            index_2D[0] =raw;
            index_2D[1] = column;
            return index_2D;
        }else{
            raw = n/50000;
            column = n%50000;
            index_2D[0] = raw;
            index_2D[1] = column;
            return index_2D;
        }
    }
    @Test
    public static void main(String[] args) throws Exception {
        for (int n:
                idx1D_to2D(62000)) {
            System.out.println(n);
        }
        int[] test =  new int[]{0,40};
        System.out.println(idx2D_to1D(test));

        ArrayList<Polygon> neighbor_test = new ArrayList<>();
        Polygon p1 = test_polygon(0,0);
        Polygon p2 = test_polygon(0,20);
        Polygon p3 = test_polygon(20,0);
        Polygon p4 = test_polygon(20,20);
        neighbor_test.add(p1);
        neighbor_test.add(p2);
        neighbor_test.add(p3);
        neighbor_test.add(p4);

        PolygonNeighbourFinder.set_NeighborGrid(neighbor_test);
        assertNotNull(p1.getNeighbors());
        assertEquals(2,p1.getNeighbors().size());
        assertTrue(p1.getNeighbors().get(1).compare(p3.getCentroid()));



    }
/*
    @Test
    public void PolygonGenerateTest() throws Exception {
        Coordinate max= new Coordinate(499.99, 499.99);
        int count=0;
        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();

        while(count<20){
            Vertex v= new Vertex(count,count+5, false, 3, RandomColor.randomColorDefault());
            Coordinate coord= new CoordinateXY(count,count+5);
            randomVertices.put(coord, v);
            count=count+1;
        }
        List<Polygon> polygonList=Polygon.generate(randomVertices(20),3, 3, max);
        assertEquals(20, polygonList.size());
    }
    private Hashtable<Coordinate, Vertex> randomVertices(int num)throws Exception{
        int count=0;
        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();
        Random bag= new Random();
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
*/
}
