package Logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class ParentLogger {
    public Logger childLogger = LogManager.getLogger(ParentLogger.class);

    public void info (String message) {
        childLogger.info(message);
    }

    public void debug (String message) {
        childLogger.debug(message);
    }
    public void error (String message) {
        childLogger.error(message);
    }

    public void fatal (String message) {
        childLogger.fatal(message);
    }

    public void setLevel (Level level) {
        Configurator.setAllLevels(childLogger.getName(), level);
    }
}
