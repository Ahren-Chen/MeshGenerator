package ADT;

public class Edges {
    private final Nodes[] nodes = new Nodes[2];
    private double weight;
    public Edges(Nodes n1, Nodes n2) {
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
        double elevation1 = nodes[0].getElevation();
        double elevation2 = nodes[1].getElevation();

        weight =  Math.sqrt(Math.pow(elevation1 - elevation2, 2));
    }
}
