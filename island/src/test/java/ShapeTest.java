import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IslandGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.util.Assert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShapeTest {
    private static final ParentLogger logger = new ParentLogger();
    private static Structs.Mesh aMesh;
    @BeforeAll
    public static void initShapeTest() throws IOException {
        logger.info("\n Initializing Island testing \n");
        String input="../generator/sample.mesh";
        aMesh = new MeshFactory().read(input);
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: aMesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
    }

    @Test
    public void notNullTest(){
        String mode = "lagoon";
        int lakes = 5;
        int aquifer = 5;
        int river = 5;
        String elevationString = "volcano";
        String soil = "fast";
        String biomes = "grassland";
        String heatMapOption = "none";
        int max_x = 500;
        int max_y = 500;
        int seed = 0;
        Structs.Mesh bMesh;
        IslandGenerator generator = new IslandGenerator(aMesh, max_x, max_y, seed);
        bMesh = generator.generate(mode, lakes, aquifer, river, elevationString, soil, biomes, heatMapOption);
        assertNotNull(bMesh);
    }
    @Test
    public void seedTest() {
        String mode = "lagoon";
        int lakes = 5;
        int aquifer = 5;
        int river = 5;
        String elevationString = "volcano";
        String soil = "fast";
        String biomes = "grassland";
        String heatMapOption = "none";
        int max_x = 500;
        int max_y = 500;
        long seed = 5126510;
        Structs.Mesh bMesh;
        Structs.Mesh cMesh;

        IslandGenerator generator = new IslandGenerator(aMesh, max_x, max_y, seed);
        bMesh = generator.generate(mode, lakes, aquifer, river, elevationString, soil, biomes, heatMapOption);

        IslandGenerator generator2 = new IslandGenerator(aMesh, max_x, max_y, seed);
        cMesh= generator2.generate(mode, lakes, aquifer, river, elevationString, soil, biomes, heatMapOption);

        assertEquals(bMesh, cMesh);
    }

    @Test
    public void terrainTest() {
        //Write test that tests whether the color of a terrain tile is white if it is not next to a OceanTile
        //Can also test things like if my neighbors neighbor is an OceanTile, will I still be regular terrain?
    }




    @Test
    public void affectTileTest(){

    }
    @AfterAll
    public static void cmdTestDone() {logger.info("\n Finished testing Cmd \n");}
}
