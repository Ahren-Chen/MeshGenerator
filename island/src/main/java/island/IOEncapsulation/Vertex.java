package island.IOEncapsulation;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;

public class Vertex implements ConvertToStruct<Structs.Vertex> {
    private final Coordinate cords;
    private final boolean isCentroid;
    private final double thickness;
    private final Color color;

    private final ParentLogger logger = new ParentLogger();

    private final int ID;

    public Vertex(Coordinate cords, boolean isCentroid, double thickness, Color color, int ID) {
        this.cords = cords;
        this.isCentroid = isCentroid;
        this.thickness = thickness;
        this.color = color;
        this.ID = ID;
    }

    public Coordinate getCords() {
        return cords;
    }

    public int getID() { return this.ID; }

    /**
     *  This method takes in itself and
     *  converts the input into a vertex of type Structs.Vertex
     * @return          a Vertex of type Structs.Vertex
     */
    public Structs.Vertex convertToStruct() {
        double x = cords.getX();
        double y = cords.getY();

        Structs.Vertex v= Structs.Vertex.newBuilder()
                .setX(x)
                .setY(y)
                .build();

        String colorCode= ConvertColor.convert(color);

        Structs.Property color=Structs.Property.newBuilder()
                .setKey("rgba_color")
                .setValue(colorCode)
                .build();

        Structs.Property centroid = Structs.Property.newBuilder()
                .setKey("centroid")
                .setValue(this.isCentroid + "")
                .build();

        Structs.Property thickness = Structs.Property.newBuilder()
                .setKey("thickness")
                .setValue(this.thickness + "")
                .build();

        //logger.error(this.thickness + " Vertex");
        return Structs.Vertex.newBuilder(v).addProperties(color).addProperties(centroid).addProperties(thickness).build();
    }
}
