package island.IOEncapsulation;

import java.util.List;

public class Polygon {
    private final List<Segment> segments;
    private final Vertex centroid;
    private List<Polygon> neighbours;
    private final int ID;

    public Polygon(List<Segment> segments, Vertex centroid, int ID) {
        this.segments = segments;
        this.centroid = centroid;
        this.ID = ID;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public Vertex getCentroid() {
        return centroid;
    }

    public List<Polygon> getNeighbours() {
        return neighbours;
    }

    public int getID() {
        return ID;
    }

    public void setNeighbours(List<Polygon> polygons) {
        this.neighbours = polygons;
    }
}
