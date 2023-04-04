package Algorithms;

import ADT.Edges;
import ADT.Nodes;
import Interfaces.PathFinder;

import java.util.List;

public class Dijkstra implements PathFinder<Edges, Nodes> {
    private final List<Nodes> nodes;
    private final List<Edges> edges;

    public Dijkstra(List<Nodes> nodes, List<Edges> edges) {
        this.edges = edges;
        this.nodes = nodes;
    }
    public List<Edges> findShortestPath(Nodes node1, Nodes node2) {
        return null;
    }
}
