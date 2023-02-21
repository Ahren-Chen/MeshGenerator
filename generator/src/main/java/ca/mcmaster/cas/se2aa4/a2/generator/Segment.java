package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.HashMap;
import java.util.List;

public class Segment {
    Vertex v1_ver;
    Vertex v2_ver;

    private static final ParentLogger logger=new ParentLogger();


    public static float[] extractColor(List <Structs.Property> properties) throws Exception {
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

    public static String segmentColor(List<Structs.Property> vertex1, List<Structs.Property> vertex2) throws Exception{
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        float[] colorVertex1;
        float[] colorVertex2;

        try {
            colorVertex1 = extractColor(vertex1);
            colorVertex2 = extractColor(vertex2);
        }
        catch (Exception e){
            throw e;
        }

        float red = (colorVertex1[0] + colorVertex2[0]) / 2;
        float green = (colorVertex1[1] + colorVertex2[1]) / 2;
        float blue = (colorVertex1[2] + colorVertex2[2]) / 2;


        String color= red + "," + blue +"," + green + "," +1;
        return color;
    }

}
