package ca.mcmaster.cas.se2aa4.a2.generator;

public class Vertex {
    private double x;
    private double y;
    private final boolean isCentroid;

    public Vertex(double x, double y, boolean isCentroid) {
        this.x = x;
        this.y = y;
        this.isCentroid = isCentroid;
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

    public boolean isCentroid() {
        return isCentroid;
    }
}
