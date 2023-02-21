package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.HashMap;
import java.util.List;

public class Segment {
    private static final ParentLogger logger=new ParentLogger();
    private Vertex v1;
    private Vertex v2;
    /***
     * in form of {R,G,B,A} RGG are numbers between 0-1, the value will be calculated by multiply with 255
     * A is alpha value for transparency, 0 is transparent, 1 is not transparent.
     */
    private float[] color;

    public Segment(Vertex v1, Vertex v2, float[] color) {
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
    }

    public Vertex[] getVertices() {
        return new Vertex[]{v1, v2};
    }

    public void setVertices(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

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
