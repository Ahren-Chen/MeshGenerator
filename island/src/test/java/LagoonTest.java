import Logging.ParentLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LagoonTest {
    private static final ParentLogger logger = new ParentLogger();

    @BeforeAll
    public static void initIslandTest() {
        logger.info("\n Initializing Island testing \n");
    }

    @Test
    public void beachTest() {
        //Write a test that tests whether a BiomesTile next to an OceanTile will have a color of Yellow after calculateColor()
        //Same thing for terrain next to lagoon tiles
    }

    @Test
    public void terrainTest() {
        //Write test that tests whether the color of a terrain tile is white if it is not next to a OceanTile
        //Can also test things like if my neighbors neighbor is an OceanTile, will I still be regular terrain?
    }

    @Test
    public void oceanTest() {
        //Write tests regarding neighboring relations or whether precipitation and temperature will affect its color
    }

    @AfterAll
    public static void cmdTestDone() {logger.info("\n Finished testing Cmd \n");}
}
