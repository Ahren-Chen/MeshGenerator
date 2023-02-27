package Extractor;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import Logging.*;

import java.util.List;

public class Extractor {
    private static final ParentLogger logger=new ParentLogger();

    public static float[] extractColor(List<Structs.Property> properties) throws Exception {
        //This method extracts the color given a map of properties
        String val="";
        for (Structs.Property property: properties) {
            if(property.getKey().equals("rgba_color")){
                val=property.getValue();
            }
        }

        String[] raw=val.split(",");

        float red;
        float green;
        float blue;
        float alpha;

        try {
            red = Float.parseFloat(raw[0]);
            green = Float.parseFloat(raw[1]);
            blue = Float.parseFloat(raw[2]);
            alpha = Float.parseFloat(raw[3]);

        } catch (IndexOutOfBoundsException e){
            logger.error("Exception in color, missing in elements, rgba");
            throw e;
        }
        catch (Exception e){
            logger.error("Other Exception");
            throw e;
        }

        return new float[] {red, green, blue, alpha};
    }
}
