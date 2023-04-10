package island.IOEncapsulation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;

import java.awt.*;
import java.util.List;

public class Segment implements Comparable <Segment>, ConvertToStruct<Structs.Segment> {
    private final Vertex v1;
    private final Vertex v2;
    private  Color color;
    private double thickness;
    private  int ID;

    protected double elevation;

    public Segment(Vertex v1, Vertex v2, double thickness, int ID) {
        this.v1 = v1;
        this.v2 = v2;
        this.color = new Color(0, 0, 0, 0);
        this.thickness = thickness;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public Vertex getV1(){
        return v1;
        //Abstraction leak, technical debt, will fix later
        //return new Vertex(v1);
    }

    public Vertex getV2(){
        return v2;
        //return new Vertex(v2);
    }

    public double getThickness(){
        return thickness;
    }
    public double getElevation() { return this.elevation;}
    public void setElevation(double elevation) { this.elevation = elevation; }
    public void setColor( Color color) { this.color = color; }
    public void setThickness(Double thickness){this.thickness = thickness;}

    public void setID(int ID){this.ID = ID;}
    public Structs.Segment convertToStruct(){

        Structs.Segment seg= Structs.Segment.newBuilder().setV1Idx(v1.getID()).setV2Idx(v2.getID()).build();

        String segmentColor = ConvertColor.convert(this.color);

        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(segmentColor).build();

        String segmentThickness = String.valueOf(this.thickness);
        Structs.Property thickness = Structs.Property.newBuilder().setKey("thickness").setValue(segmentThickness).build();

        return Structs.Segment.newBuilder(seg).addProperties(colorProperty).addProperties(thickness).build();

    }

    public boolean isRiver() {return (color.equals(Color.RED)); }

    @Override
    public int compareTo(Segment s) {
        int i=this.v1.compareTo(s.v1);
        if (i==0){
            i=this.v2.compareTo(s.v2);
        }
        return i;
    }

    public void updateElevation(){
        this.elevation = (v1.getElevation() + v2.getElevation())/2;
    }

    public boolean containsVertex(Vertex v) {
        return (v1.compareTo(v) == 0 || v2.compareTo(v) == 0);
    }

}
