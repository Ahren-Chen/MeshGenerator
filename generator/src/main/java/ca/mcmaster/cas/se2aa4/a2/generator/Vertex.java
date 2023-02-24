package ca.mcmaster.cas.se2aa4.a2.generator;

import Logging.ParentLogger;

public class Vertex implements Comparable<Vertex>{
    private double x;
    private double y;
    private final boolean isCentroid;
    private final int thickness;
    private final float[] color;
    private final ParentLogger logger=new ParentLogger();
    private int ID=-1;

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
}
