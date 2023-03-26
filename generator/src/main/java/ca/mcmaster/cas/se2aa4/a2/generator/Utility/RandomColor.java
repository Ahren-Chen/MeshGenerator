package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import logging.ParentLogger;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This utility class is for generating random color.
 */
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

    /**
     * This method returns a random Color with full visibility.
     * @return {@code Color}
     */
    public static Color randomColorDefault(){

        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);

        return new Color (red,green, blue, 255);
    }

    /**
     * This method returns a random color with randomized Alpha value
     * @return {@code Color}
     */
    public static Color randomColorAlpha(){

        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);
        int alpha = bag.nextInt(255);

        return new Color (red,green, blue, alpha);
    }
}
