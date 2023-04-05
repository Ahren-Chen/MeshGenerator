package ADT;

import java.util.List;

public class Nodes {
    private String name;
    private List<Nodes> neighbors;
    private final double elevation;
    private final double x;
    private final double y;

    public Nodes(double elevation, double x, double y) {
        this.elevation = elevation;
        this.x = x;
        this.y = y;
    }

    public Nodes(double x, double y) {
        this.elevation = 0;
        this.x = x;
        this.y = y;
    }
}
