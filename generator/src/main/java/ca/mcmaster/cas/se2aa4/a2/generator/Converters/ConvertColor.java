package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import java.awt.*;

public class ConvertColor {
    public String convert(Color colors) {
        float red = colors.getRed() / 255f;
        float green = colors.getGreen() / 255f;
        float blue = colors.getBlue() / 255f;
        float alpha = colors.getAlpha() / 255f;

        return red + "," + green + "," + blue + ","+ alpha;
    }
}
