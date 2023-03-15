import Logging.ParentLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.IOException;

public class CmdTest {
    private static final ParentLogger logger = new ParentLogger();
    private final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @BeforeAll
    public static void initExtractor() {
        logger.info("\n Initializing Extractor testing \n");
    }

    /*@Test
    public void noInput() {
        exit.expectSystemExit();

        try {
            Main.main(null);
        } catch(IOException ex) {
            logger.fatal("mesh reader error");
        }

        logger.fatal("Failed command line argument check with no input");
        System.exit(1);
    }*/
}
