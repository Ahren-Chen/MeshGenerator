package ADT;

import java.util.List;

public class Nodes {
    private String name;
    private List<Nodes> neighbors;
    private final double elevation;

    public Nodes(double elevation) {
        this.elevation = elevation;
    }

    public Nodes() {
        this.elevation = 0;
    }
}
