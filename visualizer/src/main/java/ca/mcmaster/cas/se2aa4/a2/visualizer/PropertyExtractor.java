package ca.mcmaster.cas.se2aa4.a2.visualizer;

import logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code PropertyExtractor} class describes an extractor that is defined by color, thickness, and centroids.
 * This class is a subclass of the interface {@code AbstractExtractor} for all objects
 * which store a mesh and has properties color, thickness, and centroids.
 * The class is coupled with the IO given
 * @author Ahren
 * @version Feb 2023
 */
public class PropertyExtractor implements AbstractExtractor<Color, Float>{

    /**
     * The {@code Map} containing {@code String} to {@code String} mapping of all properties that the
     * {@code PropertyExtractor} will hold.
     */
    private final Map<String, String> properties = new HashMap<>();

    /**
     * The default thickness that the extractor will assume is there is an error in extracting it from the object
     */
    public static final float defaultThickness = 3;

    /**
     * The {@code ParentLogger} that will be used to assist in debug
     */
    private static final ParentLogger logger = new ParentLogger();

    /**
     * The constructor for {@code PropertyExtractor} where it will take in a {@code List} and extract the properties from it.
     * @param propertiesList A list of {@code List<Structs.Property>} that will be the source of the properties being extracted
     */
    public PropertyExtractor(List<Structs.Property> propertiesList) {
        //For every property except for color, map the key to its value and return it
        for (Structs.Property property : propertiesList) {
            properties.put(property.getKey(), property.getValue());
        }

    }

    /**
     * This method returns the color of the {@code Object} it extracted the properties from.
     * @return {@code Color}
     */
    public Color color() {
        return extractColor();
    }

    /**
     * This method returns the thickness of the {@code Object} it extracted the properties from.
     * @return {@code Float}
     */
    public Float thickness() {

        //Check if a thickness property was given
        if (properties.containsKey("thickness")) {

            //Try to turn the thickness into a float
            try {
                double thicknessDouble = Double.parseDouble(properties.get("thickness"));
                float thickness = (float) (thicknessDouble);

                //Checking whether thickness is a positive number
                if (thickness > 0) {
                    return thickness;
                }

                throw new RuntimeException(thickness + "");
            }

            //If the conversion to float fails, assume default
            catch (NumberFormatException ex) {
                logger.error("Unable to integer parse thickness value, assuming default");
                return defaultThickness;
            }

            //If the conversion fails due to a negative thickness, assume default
            catch (RuntimeException ex) {
                logger.error("Given thickness is below 0, returning default thickness: " + ex.getMessage());
                return defaultThickness;
            }
        }

        //If thickness was not given as a property, assume default
        else {
            logger.error("Thickness not given, assuming default thickness");
            return defaultThickness;
        }
    }

    /**
     * This method returns whether the {@code Object} is a centroid. Based on the properties it extracted from.
     * @return {@code Boolean}
     */
    public Boolean isCentroid() {
        return (properties.containsKey("centroid") &&
                properties.get("centroid")
                        .equals("true"));
    }

    /**
     * This extracts the color in the specific format that is given by the IO from the {@code Object}.
     * This method will return the extracted color as a {@code Color}.
     * @return {@code Color}
     */
    private Color extractColor() {
        //This method extracts the color given a map of properties in the format given by IO
        String val;

        if (properties.containsKey("rgba_color")) {
            val = properties.get("rgba_color");
        } else {
            logger.error("No color given, default assigned to black");
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
