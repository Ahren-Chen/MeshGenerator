package island.CitiesGen;

import ADT.Edges;
import ADT.Nodes;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathFinderAdapter {
    private final List<Nodes> nodeList;
    private final List<Edges> edgeList;
    public PathFinderAdapter(List<Vertex> vertexList, List<Segment> segmentList, List<Vertex> cityList) {
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();

        Map<Vertex, Nodes> vertexNodesMap = new HashMap<>();

        for (Vertex vertex : vertexList) {
            double elevation = vertex.getElevation();
            double x = vertex.getX();
            double y = vertex.getY();
            boolean isCity = cityList.contains(vertex);
            Nodes node = new Nodes(elevation, x, y, isCity);

            if (isCity) {
                cityList.remove(vertex);
            }

            nodeList.add(node);
            vertexNodesMap.put(vertex, node);
        }

        for (Segment segment : segmentList) {
            Vertex v1 = segment.getV1();
            Vertex v2 = segment.getV2();

            Nodes n1 = vertexNodesMap.get(v1);
            Nodes n2 = vertexNodesMap.get(v2);

            Edges edge = new Edges(n1, n2);

            edgeList.add(edge);
        }
    }

    public List<Nodes> getNodes() {
        return nodeList;
    }

    public List<Edges> getEdges() {
        return edgeList;
    }
}
