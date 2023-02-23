package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.List;

public class Segment {
    private static final ParentLogger logger=new ParentLogger();
    private Vertex v1;
    private Vertex v2;

    private int ID=-1;
    /***
     * in form of {R,G,B,A} RGG are numbers between 0-1, the value will be calculated by multiply with 255
     * A is alpha value for transparency, 0 is transparent, 1 is not transparent.
     */
    private float[] color;

    public Segment(Vertex v1, Vertex v2 ) {
        if (v1.compareTo(v2)>0){
            this.v2 = v1;
            this.v1 = v2;
        }
        else{
            this.v1 = v1;
            this.v2 = v2;
        }

        this.color = avergeColor_s(v1.getColor(), v2.getColor());
    }

    public Vertex[] getVertices() {
        return new Vertex[]{v1, v2};
    }
    public Vertex getVertice1() {
        return v1;
    }
    public Vertex getVertice2() {
        return v2;
    }

    public void setID(int i){
        this.ID=i;
    }

    public void setVertices(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public float[] getColor() {
        return color;
    }
    public int getID() {
        return ID;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
    public boolean compare(Segment segment) {
        return this.v1 == segment.v1 && this.v2 == segment.v2;
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

    public static float[] avergeColor_s(float[] color1,float[] color2) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        float[] color = new float[4];
        color[0] = (color1[0] + color2[0]) / 2;
        color[1] = (color1[1] + color2[1]) / 2;
        color[2] = (color1[2] + color2[2]) / 2;
        color[3] = (color1[3] + color2[3]) / 2;
        return color;
    }




}
