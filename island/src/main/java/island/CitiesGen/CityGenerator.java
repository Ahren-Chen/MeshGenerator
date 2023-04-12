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
    /**

     Generates a map with cities and paths using the given vertex, segment, and polygon maps,
     a random generator, and the number of cities to generate.
     @param vertexMap a map of vertex IDs to vertices
     @param segmentMap a map of segment IDs to segments
     @param polygonMap a map of polygon IDs to polygons
     @param bag a random generator
     @param cities the number of cities to generate
     */
    public void generate(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, RandomGen bag, int cities) {

        CityVertexFilter filter = new CityVertexFilter(polygonMap);
        List<Vertex> possibleCities = filter.getViableVerticesSet();

        //Technical debt here with hardcoding the number of cities
        CityChooserFactory cityChooser = new CityChooserFactory(bag, possibleCities, cities);

        City capital = cityChooser.getCapital();
        Set<City> citiesSet = cityChooser.getCities();

        PathFinderAdapter adapter = new PathFinderAdapter(vertexMap, segmentMap, capital, citiesSet);
        List<Nodes> nodesList = adapter.getNodes();
        List<Edges> edgeList = adapter.getEdges();
        Nodes capitalNode = adapter.getCapital();
        Map<Edges, Segment> edgesSegmentMap = adapter.getSegmentEdgesMap();
        Map<City, Nodes> cityNodesMap = adapter.getCityNodesMap();

        PathFinder<Edges, Nodes> pathFinder = new Dijkstra(nodesList.size());

        for (City city : citiesSet) {
            Vertex vertex = city.getVertex();
            vertex.setColor(Color.BLACK);
            vertex.setThickness(city.getSize());

            Nodes cityNode = cityNodesMap.get(city);

            List<Edges> path = pathFinder.findShortestPath(cityNode, capitalNode, nodesList, edgeList);

            for (Edges edge : path) {
                Segment segment = edgesSegmentMap.get(edge);

                segment.setColor(Color.BLACK);
                segment.setThickness(2d);
            }
        }

        Vertex vertex = capital.getVertex();
        vertex.setColor(Color.BLACK);
        vertex.setThickness(capital.getSize());
    }
}
