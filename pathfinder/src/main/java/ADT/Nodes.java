package ADT;

import java.util.Comparator;
import java.util.List;

public class Nodes implements Comparator<Nodes> {
    private String name = "";
    private List<Edges> neighbors;
    private final double elevation;
    private final double x;
    private final double y;
    private double cost;

    public Nodes() {
        this.elevation = -1;
        this.x = -1;
        this.y = -1;
        cost = Double.MAX_VALUE;
    }
    public Nodes(double elevation, double x, double y) {
        this.elevation = elevation;
        this.x = x;
        this.y = y;
        this.cost = Double.MAX_VALUE;
    }

    public Nodes(double x, double y) {
        this.elevation = 0;
        this.x = x;
        this.y = y;
        this.cost = Double.MAX_VALUE;
    }

    public void setNeighbors(List<Edges> neighbors) {
        this.neighbors = neighbors;
    }

    public double getElevation() {
        return elevation;
    }
    public List<Edges> getNeighbors() { return neighbors; }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public int compare(Nodes n1, Nodes n2) {
        if (n1.cost < n2.cost) {
            return -1;
        }
        else if (n1.cost > n2.cost) {
            return 1;
        }

        return 0;
    }
}
