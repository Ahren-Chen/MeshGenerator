package ca.mcmaster.cas.se2aa4.a2.generator;

import java.awt.*;

public class Vertex {
    private double x;
    private double y;
    private final boolean isCentroid;

    private final Color color;

    public Vertex(double x, double y, boolean isCentroid, Color color) {
        this.x = x;
        this.y = y;
        this.isCentroid = isCentroid;
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

    public boolean isCentroid() {
        return isCentroid;
    }

    public Color getColor() {
        return this.color;
    }
}
