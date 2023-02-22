package ca.mcmaster.cas.se2aa4.a2.generator;

public class Vertex {
    private double x;
    private double y;
    private final boolean isCentroid;
    private final int thickness;
    private final float[] color;

    private int ID=-1;

    public Vertex(double x, double y, boolean isCentroid, int thickness, float[] color) {
        this.x = x;
        this.y = y;
        this.isCentroid = isCentroid;
        this.thickness = thickness;
        this.color = color;
    }

    public double[] getCoordinate() {
        return new double[]{x, y};
    }

    public boolean compare(Vertex v) {
        return this.x == v.x && this.y == v.y;
    }

    public void setCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
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
        return ID;
    }

    public float[] getColor() {
        return this.color;
    }
}
