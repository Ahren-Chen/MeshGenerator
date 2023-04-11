package island.CitiesGen;

import ADT.Edges;
import ADT.Nodes;
import island.CityTypes.City;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;

import java.util.*;

public class PathFinderAdapter {
    private final List<Nodes> nodeList;
    private final List<Edges> edgeList;
    private final Nodes capital;
    private final Map<City, Nodes> cityNodesMap;
    private final Map<Edges, Segment> segmentEdgesMap;
    public PathFinderAdapter(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, City capital, Set<City> cityList) {
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();

        Map<Vertex, Nodes> vertexToNodes = new HashMap<>();
        cityNodesMap = new HashMap<>();
        segmentEdgesMap = new HashMap<>();

        List<Vertex> vertexList = new ArrayList<>(vertexMap.values());
        List<Segment> segmentList = new ArrayList<>(segmentMap.values());

        Vertex vertexCapital = capital.getVertex();
        double elevation = vertexCapital.getElevation();
        double x = vertexCapital.getX();
        double y = vertexCapital.getY();
        this.capital = new Nodes(elevation, x, y);

        vertexList.remove(vertexCapital);

        nodeList.add(this.capital);
        vertexToNodes.put(vertexCapital, this.capital);

        for (City city : cityList) {
            Vertex vertex = city.getVertex();
            elevation = vertex.getElevation();
            x = vertex.getX();
            y = vertex.getY();
            Nodes node = new Nodes(elevation, x, y);

            vertexList.remove(vertex);
            nodeList.add(node);
            vertexToNodes.put(vertex, node);
            cityNodesMap.put(city, node);
        }

        for (Vertex vertex : vertexList) {
            elevation = vertex.getElevation();
            x = vertex.getX();
            y = vertex.getY();
            Nodes node = new Nodes(elevation, x, y);

            nodeList.add(node);
            vertexToNodes.put(vertex, node);
        }

        for (Segment segment : segmentList) {
            Vertex v1 = segment.getV1();
            Vertex v2 = segment.getV2();

            Nodes n1 = vertexToNodes.get(v1);
            Nodes n2 = vertexToNodes.get(v2);

            Edges edge = new Edges(n1, n2);

            edgeList.add(edge);
            segmentEdgesMap.put(edge, segment);
        }
    }

    public List<Nodes> getNodes() {
        return nodeList;
    }

    public List<Edges> getEdges() {
        return edgeList;
    }

    public Nodes getCapital() {
        return capital;
    }

    public Map<City, Nodes> getCityNodesMap() {
        return cityNodesMap;
    }

    public Map<Edges, Segment> getSegmentEdgesMap() {
        return segmentEdgesMap;
    }
}
