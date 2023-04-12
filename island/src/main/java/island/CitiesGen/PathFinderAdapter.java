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

    /**

     The PathFinderAdapter class is used to create nodes and edges to represent a map for pathfinding algorithms.

     @param vertexMap A Map of vertices.

     @param segmentMap A Map of segments.

     @param capital The capital city.

     @param cityList A Set of cities.
     */
    public PathFinderAdapter(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, City capital, Set<City> cityList) {
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
        Map<Vertex, Nodes> vertexToNodes = new HashMap<>();
        cityNodesMap = new HashMap<>();
        segmentEdgesMap = new HashMap<>();

        if (capital == null || vertexMap == null || segmentMap == null || cityList == null) {
            this.capital = null;
            return;
        }

        List<Vertex> vertexList = new ArrayList<>(vertexMap.values());
        List<Segment> segmentList = new ArrayList<>(segmentMap.values());

        Vertex vertexCapital = capital.getVertex();
        if (! vertexList.contains(vertexCapital)) {
            throw new RuntimeException("Capital vertex is not within possible vertex list");
        }

        //Setting up the capital
        double elevation = vertexCapital.getElevation();
        double x = vertexCapital.getX();
        double y = vertexCapital.getY();
        this.capital = new Nodes(elevation, x, y);

        vertexList.remove(vertexCapital);

        nodeList.add(this.capital);
        vertexToNodes.put(vertexCapital, this.capital);

        //Setting up each city node
        for (City city : cityList) {
            Vertex vertex = city.getVertex();

            if (! vertexList.contains(vertex)) {
                throw new RuntimeException("City vertex is not within possible vertex list");
            }

            //Creating a node for each designated city and removing it from the overall vertexList
            elevation = vertex.getElevation();
            x = vertex.getX();
            y = vertex.getY();
            Nodes node = new Nodes(elevation, x, y);

            vertexList.remove(vertex);
            nodeList.add(node);
            vertexToNodes.put(vertex, node);
            cityNodesMap.put(city, node);
        }

        //Creating nodes for the rest of the vertices and adding them to the appropriate maps/lists
        for (Vertex vertex : vertexList) {
            elevation = vertex.getElevation();
            x = vertex.getX();
            y = vertex.getY();
            Nodes node = new Nodes(elevation, x, y);

            nodeList.add(node);
            vertexToNodes.put(vertex, node);
        }

        //Creating the edges for each of the given segments on the map
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

    /**
     Returns a List of Nodes.
     @return A {@code List} of Nodes.
     */
    public List<Nodes> getNodes() {
        return nodeList;
    }

    /**
     Returns a List of Edges.
     @return A {@code List} of Edges.
     */
    public List<Edges> getEdges() {
        return edgeList;
    }

    /**
     Returns the capital Nodes.
     @return A {@code Nodes}.
     */
    public Nodes getCapital() {
        return capital;
    }

    /**
     Returns a Map of City to Nodes.
     @return A {@code Map} of City to Nodes.
     */
    public Map<City, Nodes> getCityNodesMap() {
        return cityNodesMap;
    }

    /**
     Returns a Map of Edges to Segment.
     @return A {@code Map} of Edges to Segment.
     */
    public Map<Edges, Segment> getSegmentEdgesMap() {
        return segmentEdgesMap;
    }
}
