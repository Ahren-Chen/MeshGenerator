package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.ConvertToStruct;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;

public class Segment implements Comparable<Segment>, ConvertToStruct<Structs.Segment> {
    private static final ParentLogger logger = new ParentLogger();
    private final Vertex v1;
    private final Vertex v2;

    private final double thickness;
    private static final ConvertColor colorConverter= new ConvertColor();
    private int ID=-1;
    /***
     * in form of {R,G,B,A} RGG are numbers between 0-1, the value will be calculated by multiply with 255
     * A is alpha value for transparency, 0 is transparent, 1 is not transparent.
     */
    private final Color color;

    public Segment(Vertex v1, Vertex v2, double thickness ) {
        if (v1.compareTo(v2)>0){
            this.v2 = v1;
            this.v1 = v2;
        }
        else{
            this.v1 = v1;
            this.v2 = v2;
        }

        this.thickness = thickness;
        this.color = averageColor(v1.getColor(), v2.getColor());
    }
    public Vertex getVertice1() {
        return v1;
    }
    public Vertex getVertice2() {
        return v2;
    }
    public double getThickness() { return thickness; }

    public void setID(int i){
        this.ID=i;
    }

    public Color getColor() {
        return color;
    }
    public int getID() {
        if(ID==-1){
            logger.error("Segment ID don't exist");
        }
        return ID;
    }

    public boolean compare(Segment segment) {
        if (this.v1.compare(segment.v1)) {
            return this.v2.compare(segment.v2);
        } else if (this.v1.compare(segment.v2)) {
            return this.v2.compare(segment.v1);
        }
        return false;
    }

    public static Color averageColor(Color color1, Color color2) {
        //This method gets the color of the segment based on the average of the 2 vertices it connects to
        int Red = (color1.getRed() + color2.getRed()) / 2;
        int Green = (color1.getGreen() + color2.getGreen()) / 2;
        int Blue = (color1.getGreen() + color2.getGreen()) / 2;
        int Alpha = (color1.getAlpha() + color2.getAlpha()) / 2;

        return new Color (Red, Green, Blue, Alpha);
    }


    @Override
    public int compareTo(Segment s) {
        int i=this.v1.compareTo(s.v1);
        if (i==0){
            i=this.v2.compareTo(s.v2);
        }
        return i;
    }

    public Structs.Segment convertToStruct(){

        Structs.Segment seg= Structs.Segment.newBuilder().setV1Idx(v1.getID()).setV2Idx(v2.getID()).build();

        String segmentColor = colorConverter.convert(this.color);

        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(segmentColor).build();

        String segmentThickness = String.valueOf(this.thickness);
        Structs.Property thickness = Structs.Property.newBuilder().setKey("thickness").setKey(segmentThickness).build();

        return Structs.Segment.newBuilder(seg).addProperties(colorProperty).addProperties(thickness).build();

    }
}
