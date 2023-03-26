package island.converters;

import java.awt.*;

/**
 * The ConvertColor utility class converts an object {@code Color}
 * into a {@code String} format in {@code float} values and send it back.
 * @author Ahren, Mike, Simon
 * @version Feb 2023
 */
public class ConvertColor {

    /**
     * This method converts {@code Color} into {@code String} format.
     * @param colors {@code Color}
     * @return String the color represented as a string with float values
     */
    public static String convert(Color colors) {
        float red = colors.getRed() / 255f;
        float green = colors.getGreen() / 255f;
        float blue = colors.getBlue() / 255f;
        float alpha = colors.getAlpha() / 255f;

        return red + "," + green + "," + blue + ","+ alpha;
    }
}
