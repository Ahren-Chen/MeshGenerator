import Logging.ParentLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CmdTest {
    private static final ParentLogger logger = new ParentLogger();
    private static String[] input;
    private static File testFile;

    @BeforeAll
    public static void initExtractor() {
        logger.info("\n Initializing Cmd testing \n");
        input = null;
        try {
            testFile = new File("test.mesh");
            if (testFile.createNewFile()) {
                System.out.println("File created: " + testFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (
        IOException e) {
            logger.error("An error occurred.");
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void resetInput() {
        input = null;
    }

    @Test
    public void noInput() {
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter an input file path", exception.getMessage());

        input = new String[]{"-i", ""};
        exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("File does not exist", exception.getMessage());
    }

    @Test
    public void wrongInputFile() {
        input = new String[]{"-i", "hisdgfs"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("File does not exist", exception.getMessage());
    }

    @Test
    public void inputIsDirectory() {
        input = new String[]{"-i", "../generator"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Entered an invalid file path. Currently entered a directory", exception.getMessage());
    }

    @Test
    public void invalidOutputName() {
        input = new String[]{"-i", "test.mesh", "-o", "?"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Illegal char <?> at index 0: ?", exception.getMessage());
    }

    @Test
    public void noOutputName() {
        input = new String[] {"-i", "test.mesh"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter a output file name", exception.getMessage());

        input = new String[] {"-i", "test.mesh", "-o"};
        exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Missing argument for option: o", exception.getMessage());

        input = new String[] {"-i", "test.mesh", "-o", ""};
        exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter valid output file name", exception.getMessage());
    }

    @Test
    public void noMode() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter an island mode", exception.getMessage());
    }
    @Test
    public void invalidMode() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "asdkjfhaklsjf"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid island mode, please enter 'lagoon', 'bridge', or 'star'", exception.getMessage());
    }

    @Test
    public void invalidLakes() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-lakes", "-2"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid number of lakes entered, please enter more than or equal to 0 lakes", exception.getMessage());
    }

    @Test
    public void invalidSeed() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-seed", "0"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid seed, please enter an int bigger than 0", exception.getMessage());
    }

    @Test
    public void invalidAquifer() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-aquifer", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid number of aquifers entered, please enter more than or equal to 0", exception.getMessage());
    }

    @Test
    public void invalidRivers() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-river", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid number of rivers entered, please enter more than or equal to 0", exception.getMessage());
    }

    @Test
    public void invalidElevation() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-elevation", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid elevation, please enter 'volcano', 'canyon', 'mountain' or 'arctic'", exception.getMessage());
    }

    @Test
    public void invalidSoil() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-soil", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid soil profile, please enter 'slow', 'medium', or 'fast'", exception.getMessage());
    }

    @Test
    public void invalidBiomes() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-biomes", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid whittaker profile, please enter 'arctic', 'rainforest', 'grassland', or 'desert'", exception.getMessage());
    }

    @Test
    public void invalidHeatMap() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-heatMap", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid heatMap option, please enter 'elevation','precipitation', 'temperature', or 'none'", exception.getMessage());
    }

    @Test
    public void invalidCities() {
        input = new String[] {"-i", "test.mesh", "-o", "island.mesh", "-mode", "star", "-cities", "-5"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid number of cities entered, please enter more than or equal to 0", exception.getMessage());
    }

    @AfterAll
    public static void cmdTestDone() {
        logger.info("\n Finished testing Cmd \n");
        testFile.delete();
    }
}
