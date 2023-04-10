package island.CitiesGen;

import island.CityTypes.City;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Utility.RandomGen;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CityGenerator {
    public void generate(Map<Integer, Vertex> vertexMap, Map<Integer, Segment> segmentMap, Map<Integer, Polygon> polygonMap, RandomGen bag) {

        CityVertexSegmentFilter filter = new CityVertexSegmentFilter(polygonMap);
        List<Vertex> possibleVertices = filter.getViableVerticesSet();
        List<Segment> possibleSegments = filter.getViableSegmentSet();

        //Technical debt here with hardcoding the number of cities
        CityChooserFactory cityChooser = new CityChooserFactory(bag, possibleVertices, 15);

        City capital = cityChooser.getCapital();
        Set<City> cities = cityChooser.getCities();

        PathFinderAdapter adapter = new PathFinderAdapter(possibleVertices, possibleSegments, capital, cities);
    }
}
