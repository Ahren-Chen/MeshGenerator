import Logging.ParentLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CmdTest {
    private static final ParentLogger logger = new ParentLogger();
    private static String[] input;

    @BeforeAll
    public static void initExtractor() {
        logger.info("\n Initializing Cmd testing \n");
        input = null;
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
        input = new String[]{"-i", "../generator/sample.mesh", "-o", "?"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Illegal char <?> at index 0: ?", exception.getMessage());
    }

    @Test
    public void noOutputName() {
        input = new String[] {"-i", "../generator/sample.mesh"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter a output file name", exception.getMessage());

        input = new String[] {"-i", "../generator/sample.mesh", "-o"};
        exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Missing argument for option: o", exception.getMessage());

        input = new String[] {"-i", "../generator/sample.mesh", "-o", ""};
        exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter valid output file name", exception.getMessage());
    }

    @Test
    public void noMode() {
        input = new String[] {"-i", "../generator/sample.mesh", "-o", "island.mesh"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Please enter an island mode", exception.getMessage());
    }
    @Test
    public void invalidMode() {
        input = new String[] {"-i", "../generator/sample.mesh", "-o", "island.mesh", "-mode", "asdkjfhaklsjf"};
        Exception exception = assertThrows(Exception.class, () -> Main.main(input));

        assertEquals("Invalid island mode, please enter 'lagoon', 'bridge', or 'star'", exception.getMessage());
    }

    @AfterAll
    public static void cmdTestDone() {logger.info("\n Finished testing Cmd \n");}
}
