package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.SelfConverter;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Vertex implements Comparable<Vertex>, SelfConverter<Structs.Vertex> {
    private final double x;
    private final double y;
    private final boolean isCentroid;
    private final int thickness;
    private final float[] color;
    private final ParentLogger logger=new ParentLogger();
    private int ID=-1;
    private final ConvertColor colorConverter = new ConvertColor();

    public Vertex(double x, double y, boolean isCentroid, int thickness, float[] color) throws Exception {
        this.x = x;
        this.y = y;
        this.isCentroid = isCentroid;
        this.thickness = thickness;
        if (color.length<4){
            throw new Exception("color is in wrong format");
        }
        this.color = color;
    }

    public double[] getCoordinate() {
        return new double[]{x, y};
    }

    public boolean compare(Vertex v) {
        return this.x == v.x && this.y == v.y;
    }

    public void setID(int i){
        this.ID=i;
    }

    public boolean isCentroid() {
        return isCentroid;
    }

    public int getThickness() {
        return this.thickness;
    }
    public int getID() {
        if(ID==-1){
            logger.error("ID don't exist");
        }
        return ID;
    }

    public float[] getColor() {
        return this.color;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public int compareTo(Vertex v) {
        if(this.x<v.x){
            return -1;
        }
        else if (this.x>v.x){
            return 1;
        }
        if(this.y<v.y){
            return -1;
        }
        else if (this.y>v.y){
            return 1;
        }
        return 0;
    }
    /**
     *  This method takes in itself and
     *  converts the input into a vertex of type Structs.Vertex
     * @return          a Vertex of type Structs.Vertex
     */
    public Structs.Vertex convert() {

        Structs.Vertex v= Structs.Vertex.newBuilder()
                .setX(x)
                .setY(y)
                .build();
        String colorCode= colorConverter.convert(color);
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

        return Structs.Vertex.newBuilder(v).addProperties(color).addProperties(centroid).addProperties(thickness).build();
    }
}
