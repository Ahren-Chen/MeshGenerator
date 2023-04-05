package ADT;

import java.util.*;

public class Nodes implements Comparator<Nodes> {
    private String name = "";
    private Set<Edges> neighbors;
    private final double elevation;
    private double cost;

    public Nodes() {
        this.elevation = -1;
        cost = Double.MAX_VALUE;
    }
    public Nodes(double elevation) {
        this.elevation = elevation;
        this.cost = Double.MAX_VALUE;
    }

    public void setNeighbors(Set<Edges> neighbors) {
        this.neighbors = neighbors;
    }

    public double getElevation() {
        return elevation;
    }
    public Set<Edges> getNeighbors() {
        return new HashSet<>(neighbors);
    }
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
