package island.CitiesGen;

import ADT.Edges;
import ADT.Nodes;
import Algorithms.Dijkstra;
import Interfaces.PathFinder;
import island.CityTypes.City;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Utility.RandomGen;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CityGenerator {
    public void generate(Map<Integer, Polygon> polygonMap, RandomGen bag) {

        CityVertexSegmentFilter filter = new CityVertexSegmentFilter(polygonMap);
        List<Vertex> possibleVertices = filter.getViableVerticesSet();
        List<Segment> possibleSegments = filter.getViableSegmentSet();

        //Technical debt here with hardcoding the number of cities
        CityChooserFactory cityChooser = new CityChooserFactory(bag, possibleVertices, 15);

        City capital = cityChooser.getCapital();
        Set<City> cities = cityChooser.getCities();

        PathFinderAdapter adapter = new PathFinderAdapter(possibleVertices, possibleSegments, capital, cities);
        List<Nodes> nodesList = adapter.getNodes();
        List<Edges> edgeList = adapter.getEdges();
        Nodes capitalNode = adapter.getCapital();
        Map<Vertex, Nodes> vertexNodesMap = adapter.getVertexToNodesMap();
        Map<Edges, Segment> edgesSegmentMap = adapter.getSegmentEdgesMap();
        Map<City, Nodes> cityNodesMap = adapter.getCityNodesMap();

        PathFinder<Edges, Nodes> pathFinder = new Dijkstra(nodesList.size());

        for (City city : cities) {
            Vertex vertex = city.getVertex();
            vertex.setColor(Color.BLACK);
            vertex.setThickness(city.getSize());

            Nodes cityNode = cityNodesMap.get(city);

            List<Edges> path = pathFinder.findShortestPath(cityNode, capitalNode, nodesList, edgeList);

            for (Edges edge : path) {
                Segment segment = edgesSegmentMap.get(edge);

                segment.setColor(Color.BLACK);
                segment.setThickness(3d);
            }
        }
    }
}
