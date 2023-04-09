package Utility;

import ADT.Edges;
import ADT.Nodes;

import java.util.List;
import java.util.Set;

public class NodeConnector {
    public static void setNodeConnections(List<Edges> edges) {
        for (Edges edge : edges) {
            Nodes n1 = edge.getNodes()[0];
            Nodes n2 = edge.getNodes()[1];

            Set<Edges> neighbor1 = n1.getNeighbors();
            Set<Edges> neighbor2 = n2.getNeighbors();

            neighbor1.add(edge);
            neighbor2.add(edge);

            n1.setNeighbors(neighbor1);
            n2.setNeighbors(neighbor2);
        }
    }
}
