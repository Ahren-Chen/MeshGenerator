package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import Logging.ParentLogger;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomColor {
    private static final Random bag;
    private static final ParentLogger logger = new ParentLogger();
    static {
        try {
            bag = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("Random variable error");
            throw new RuntimeException(e);
        }
    }

    public static Color randomColorDefault(){

        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);

        return new Color (red,green, blue, 255);
    }
    public static Color randomColorAlpha(){

        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);
        int alpha = bag.nextInt(255);

        return new Color (red,green, blue, alpha);
    }
}
