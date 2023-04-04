package ADT;

public class Edges {
    private final Nodes[] nodes = new Nodes[2];

    public Edges(Nodes n1, Nodes n2) {
        nodes[0] = n1;
        nodes[1] = n2;
    }

    public Nodes[] getNodes() {
        return nodes;
    }

    public boolean containsNodes(Nodes node) {
        return (node.equals(nodes[0]) || node.equals(nodes[1]));
    }
}
