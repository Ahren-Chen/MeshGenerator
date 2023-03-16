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

    @AfterAll
    public static void cmdTestDone() {logger.info("\n Finished testing Cmd \n");}
}
