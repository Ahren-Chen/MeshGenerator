package ADT;

import java.util.Comparator;
import java.util.List;

public class Nodes implements Comparator<Nodes> {
    private String name;
    private List<Edges> neighbors;
    private final double elevation;
    private final double x;
    private final double y;
    private double cost;
    private final int ID;

    public Nodes() {
        this.elevation = -1;
        this.x = -1;
        this.y = -1;
        cost = 0;
        ID = 0;
    }
    public Nodes(double elevation, double x, double y, double cost, int ID) {
        this.elevation = elevation;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.ID = ID;
    }

    public Nodes(double x, double y, double cost, int ID) {
        this.elevation = 0;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.ID = ID;
    }

    public void setNeighbors(List<Edges> neighbors) {
        this.neighbors = neighbors;
    }

    public double getElevation() {
        return elevation;
    }
    public double getCost() { return cost; }
    public int getID() {return ID; }
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
