package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
    /*
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
    }*/
}
