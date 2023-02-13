package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PropertyExtractor {
    private static final Map<String, String> properties = new HashMap<>();

    private static final int defaultThickness = 3;

    private static final ParentLogger logger = new ParentLogger();

    public PropertyExtractor(List<Structs.Property> propertiesList) {
        //For every property except for color, map the key to its value and return it
        for (Structs.Property property : propertiesList) {
            properties.put(property.getKey(), property.getValue());
        }
    }

    public Color color() {
        return extractColor();
    }

    public int thickness() {
        if (properties.containsKey("thickness")) {
            try {
                return Integer.parseInt(properties.get("thickness"));
            }
            catch (NumberFormatException ex) {
                logger.error("Unable to integer parse thickness value, assuming default");
                return defaultThickness;
            }
        }

        else {
            return defaultThickness;
        }
    }

    public boolean centroid() {
        return properties.containsKey("centroid");
    }

    private static Color extractColor() {
        //This method extracts the color given a map of properties

        String val;

        if (properties.containsKey("rgba_color")) {
            val = properties.get("rgba_color");
        } else {
            return Color.BLACK;
        }

        String[] raw = val.split(",");

        float red;
        float green;
        float blue;
        float alpha;

        try {
            red = Float.parseFloat(raw[0]);
            green = Float.parseFloat(raw[1]);
            blue = Float.parseFloat(raw[2]);
            alpha = Float.parseFloat(raw[3]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.error("Converting color failed (Will default to black): " + ex.getMessage());
            return Color.BLACK;
        }

        return new Color(red, green, blue, alpha);
    }
}
