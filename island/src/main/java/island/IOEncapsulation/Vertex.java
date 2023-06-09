package island.IOEncapsulation;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;

public class Vertex implements Comparable<Vertex>, ConvertToStruct<Structs.Vertex> {
    private final Coordinate cords;
    private final boolean isCentroid;
    private double thickness;
    private  Color color;

    private final ParentLogger logger = new ParentLogger();

    protected double elevation;
    private final int ID;
    private double riverThickness;
    private boolean ifRiver;

    public Vertex(Coordinate cords, boolean isCentroid, double thickness, Color color, int ID, double elevation,boolean ifRiver) {
        this.cords = cords;
        this.isCentroid = isCentroid;
        this.thickness = thickness;
        this.color = color;
        this.ID = ID;
        this.elevation=elevation;
        this.ifRiver = ifRiver;
        riverThickness = 0;
    }
    public Vertex(Coordinate cords, boolean isCentroid, double thickness, Color color, int ID) {
        this(cords, isCentroid, thickness, color, ID, 0,false);
    }

    public Vertex(Vertex v){
        this(v.cords, v.isCentroid, v.thickness, v.color, v.ID, v.elevation, v.ifRiver);
    }

    public Coordinate getCords() {
        return cords;
    }
    public double getX(){
        return cords.x;
    }
    public double getY(){
        return cords.y;
    }
    public int getID() { return this.ID; }
    public double getElevation() { return this.elevation; }
    public Color getColor() { return this.color; }
    public void setElevation(double elevation) { this.elevation = elevation; }
    public void setRiverThickness(double thickness) { this.riverThickness = thickness; }
    public void setIfRiver(boolean ifRiver){this.ifRiver = ifRiver;}

    public void setThickness(double thickness){this.thickness = thickness;}
    public double getRiverThickness() {return this.riverThickness; }
    public boolean getIfRiver(){return this.ifRiver;}
    public void setColor(Color color){this.color = color;}

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
    @Override
    public int compareTo(Vertex v) {
        if(this.cords.x<v.cords.x){
            return -1;
        }
        else if (this.cords.x>v.cords.x){
            return 1;
        }
        if(this.cords.y<v.cords.y){
            return -1;
        }
        else if (this.cords.y>v.cords.y){
            return 1;
        }
        return 0;
    }


}
