package Algorithms;

import ADT.Edges;
import ADT.Nodes;
import Interfaces.PathFinder;
import Logging.ParentLogger;
import Utility.NodeConnector;

import java.util.*;

public class Dijkstra implements PathFinder<Edges, Nodes> {
    private static final ParentLogger logger = new ParentLogger();
    private List<Nodes> nodes;
    private final Map<Nodes, Double> distance;
    private final PriorityQueue<Nodes> priorityQueue;
    private final Map<Nodes, Nodes> previousNodes;

    public Dijkstra(int size) {
        distance = new HashMap<>(size);
        priorityQueue = new PriorityQueue<>(size, new Nodes());
        previousNodes = new HashMap<>(size);
        nodes = null;
    }
    private void dijkstraAlgorithm(Nodes source) {

        for (Nodes node : nodes) {
            previousNodes.put(node, null);
            distance.put(node, Double.MAX_VALUE);
        }

        source.setCost(0);

        priorityQueue.add(source);
        distance.put(source, 0d);
        previousNodes.put(source, source);

        while (! priorityQueue.isEmpty()) {
            Nodes node = priorityQueue.remove();

            Set<Edges> neighbors = node.getNeighbors();

            for (Edges edge : neighbors) {
                Nodes next;
                Nodes[] edgeNodes = edge.getNodes();
                if (edgeNodes[0].equals(node)) {
                    next = edgeNodes[1];
                }
                else {
                    next = edgeNodes[0];
                }

                try {
                    if (distance.get(node) + edge.getWeight() < distance.get(next)) {
                        distance.put(next, distance.get(node) + edge.getWeight());

                        previousNodes.put(next, node);

                        next.setCost(distance.get(node) + edge.getWeight());
                        priorityQueue.add(next);
                    }
                }
                catch (NullPointerException e) {
                    throw new RuntimeException("Node is missing from the node list but contains edge connecting to it");
                }
            }
        }
    }
    public List<Edges> findShortestPath(Nodes source, Nodes target, List<Nodes> nodes, List<Edges> edges) {
        if (source == null || target == null || nodes == null || edges == null) {
            throw new RuntimeException("Null element given");
        }
        this.nodes = nodes;

        NodeConnector.setNodeConnections(edges);

        dijkstraAlgorithm(source);
        List<Nodes> nodePath = sortPath(target);
        List<Edges> edgePath = new ArrayList<>();

        for (int pathIndex = 0; pathIndex < nodePath.size() - 1; pathIndex++) {
            Nodes node = nodePath.get(pathIndex);
            Nodes nextNode = nodePath.get(pathIndex + 1);
            Set<Edges> nodeEdges = node.getNeighbors();

            for (Edges edge : nodeEdges) {
                if (edge.containsNodes(node) && edge.containsNodes(nextNode)) {
                    edgePath.add(edge);
                    break;
                }
            }
        }

        return edgePath;
    }

    private List<Nodes> sortPath(Nodes target) {
        List<Nodes> nodePath = new ArrayList<>();
        while(distance.get(target) != 0) {
            nodePath.add(target);
            target = previousNodes.get(target);
            if (target == null) {
                nodePath.clear();
                logger.error("No path to target node");
                break;
            }
        }
        nodePath.add(target);

        Collections.reverse(nodePath);
        return nodePath;
    }
}
