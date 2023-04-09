package ADT;

import Logging.ParentLogger;

public class Edges {
    private static ParentLogger logger = new ParentLogger();
    private final Nodes[] nodes = new Nodes[2];
    private double weight;
    public Edges(Nodes n1, Nodes n2) {
        if (n1.equals(n2)) {
            logger.error("Trying to make an edge with the same nodes");
            throw new RuntimeException("Trying to make an edge with the same nodes");
        }
        nodes[0] = n1;
        nodes[1] = n2;
        calculateWeight();
    }

    public Nodes[] getNodes() {
        return nodes;
    }
    public double getWeight() { return weight; }
    public boolean containsNodes(Nodes node) {
        return (node.equals(nodes[0]) || node.equals(nodes[1]));
    }
    private void calculateWeight() {
        double x1 = nodes[0].getCords()[0];
        double y1 = nodes[0].getCords()[1];
        double x2 = nodes[1].getCords()[0];
        double y2 = nodes[1].getCords()[1];

        weight =  Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
