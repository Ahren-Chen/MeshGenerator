package island.IOEncapsulation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;

import java.awt.*;

public class Segment implements ConvertToStruct<Structs.Segment> {
    private final Vertex v1;
    private final Vertex v2;
    private final Color color;
    private final double thickness;
    private final int ID;
    public Segment(Vertex v1, Vertex v2, Color color, double thickness, int ID) {
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
        this.thickness = thickness;
        this.ID = ID;
    }

    public Structs.Segment convertToStruct(){

        Structs.Segment seg= Structs.Segment.newBuilder().setV1Idx(v1.getID()).setV2Idx(v2.getID()).build();

        String segmentColor = ConvertColor.convert(this.color);

        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(segmentColor).build();

        String segmentThickness = String.valueOf(this.thickness);
        Structs.Property thickness = Structs.Property.newBuilder().setKey("thickness").setValue(segmentThickness).build();

        return Structs.Segment.newBuilder(seg).addProperties(colorProperty).addProperties(thickness).build();

    }
}
