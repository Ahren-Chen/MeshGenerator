package ADT;

import Logging.ParentLogger;

import java.util.*;

public class Nodes implements Comparator<Nodes> {
    private static ParentLogger logger = new ParentLogger();
    private String name = "";
    private Set<Edges> neighbors = new HashSet<>();
    private final double elevation;
    private double cost;
    private final double x;
    private final double y;

    public Nodes() {
        this.elevation = -1;
        cost = Double.MAX_VALUE;
        x = -1;
        y = -1;
    }
    public Nodes(double elevation, double x, double y) {

        if (elevation < 0) {
            logger.error("Elevation below 0, assuming default of 0");
            this.elevation = 0;
        }
        else {
            this.elevation = elevation;
        }

        this.cost = Double.MAX_VALUE;
        this.x = x;
        this.y = y;
    }

    public void setNeighbors(Set<Edges> neighbors) {
        this.neighbors = neighbors;
    }
    public double[] getCords() {
        return new double[] {x, y};
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
