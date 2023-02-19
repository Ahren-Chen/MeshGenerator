package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PropertyExtractor extends Extractor<Object>{
    private final Map<String, String> properties = new HashMap<>();

    public static final int defaultThickness = 3;

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

    public Integer thickness() {
        if (properties.containsKey("thickness")) {
            try {
                int thickness = Integer.parseInt(properties.get("thickness"));

                if (thickness > 0) {
                    return thickness;
                }

                throw new RuntimeException("Thickness is below 0");
            }
            catch (NumberFormatException ex) {
                logger.error("Unable to integer parse thickness value, assuming default");
                return defaultThickness;
            }
            catch (RuntimeException ex) {
                logger.error("Given thickness is below 0, returning default thickness");
                return defaultThickness;
            }
        }

        else {
            return defaultThickness;
        }
    }

    public Boolean centroid() {
        return (properties.containsKey("centroid") && properties.get("centroid").equals("true"));
    }

    private Color extractColor() {
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
        Color color;

        try {
            red = Float.parseFloat(raw[0]);
            green = Float.parseFloat(raw[1]);
            blue = Float.parseFloat(raw[2]);
            alpha = Float.parseFloat(raw[3]);

            color = new Color(red, green, blue, alpha);

        } catch (NumberFormatException ex) {
            logger.error("Gave invalid color components, please enter float from 0-1 (Will default to black): " + ex.getMessage());
            return Color.BLACK;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            logger.error("Did not give enough arguments for color (Will default to black) :" + ex.getMessage());
            return Color.BLACK;
        }
        catch (NullPointerException ex) {
            logger.error("One of the color components was null (Will default to black): " + ex.getMessage());
            return Color.BLACK;
        }
        catch (IllegalArgumentException ex) {
            logger.error("Gave numbers for color not within the range 0-1 (Will default to black): " + ex.getMessage());
            return Color.BLACK;
        }

        return color;
    }
}
