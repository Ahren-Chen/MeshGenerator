package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.generator.Utility.RandomColor;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import  ca.mcmaster.cas.se2aa4.a2.generator.*;
import org.locationtech.jts.geom.CoordinateXY;


public class generatorTest {

    @Test
    public void meshIsNotNull() throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh aMesh = generator.generate("gridMesh");
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

        //Polygon.generate(verticesList);
    }

    @Test
    public void gridGenerationTest()throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh myMesh = generator.generate("gridMesh");
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
            Structs.Mesh myMesh = generator.generate("");
            MeshFactory factory = new MeshFactory();
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void PolygonGenerateTest() throws Exception {
        Coordinate max= new Coordinate(499.99, 499.99);
        /*int count=0;
        Hashtable<Coordinate, Vertex> randomVertices=new Hashtable<>();

        while(count<20){
            Vertex v= new Vertex(count,count+5, false, 3, RandomColor.randomColorDefault());
            Coordinate coord= new CoordinateXY(count,count+5);
            randomVertices.put(coord, v);
            count=count+1;
        }*/
        List<Polygon> polygonList=Polygon.generate(randomVertices(20),3, 3, max);
        //assertEquals(20, polygonList.size());
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
}
