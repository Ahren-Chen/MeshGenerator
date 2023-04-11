import ADT.Nodes;
import Logging.ParentLogger;
import island.CitiesGen.CityChooserFactory;
import island.CitiesGen.CityVertexFilter;
import island.CitiesGen.PathFinderAdapter;
import island.CityTypes.Capital;
import island.CityTypes.City;
import island.CityTypes.MediumCity;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Utility.RandomGen;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    private static final ParentLogger logger = new ParentLogger();
    private final Map<Integer, Vertex> vertexMap = new HashMap<>();
    private final Map<Integer, Segment> segmentMap = new HashMap<>();
    private final Map<Integer, Polygon> polygonMap = new HashMap<>();
    private final Set<City> cities = new HashSet<>();
    private City capital;

    @BeforeAll
    public static void initTest() {
        logger.info("City test begins");
    }

    @BeforeEach
    public void createPolygons() {
        Vertex v1 = new Vertex(new Coordinate(0, 0), false, 3, Color.BLACK, 0, 0, false);
        Vertex v2 = new Vertex(new Coordinate(5, 0), false, 3, Color.BLACK, 1, 0, false);
        Vertex v3 = new Vertex(new Coordinate(0, 5), false, 3, Color.BLACK, 2, 0, false);
        Vertex v4 = new Vertex(new Coordinate(5, 5), false, 3, Color.BLACK, 3, 0, false);

        vertexMap.put(0, v1);
        vertexMap.put(1, v2);
        vertexMap.put(2, v3);
        vertexMap.put(3, v4);

        capital = new Capital(v1);
        cities.add(new MediumCity(v2));
        cities.add(new MediumCity(v4));

        Segment s1 = new Segment(v1, v2, 3, 0);
        Segment s2 = new Segment(v1, v3, 3, 1);
        Segment s3 = new Segment(v2, v3, 3, 2);
        Segment s4 = new Segment(v2, v4, 3, 3);
        Segment s5 = new Segment(v3, v4, 3, 4);

        segmentMap.put(0, s1);
        segmentMap.put(1, s2);
        segmentMap.put(2, s3);
        segmentMap.put(3, s4);
        segmentMap.put(4, s5);

        List<Segment> polySeg1 = new ArrayList<>();
        List<Segment> polySeg2 = new ArrayList<>();

        polySeg1.add(s1);
        polySeg1.add(s3);
        polySeg1.add(s2);

        polySeg2.add(s5);
        polySeg2.add(s4);
        polySeg2.add(s3);

        Polygon p1 = new Polygon(polySeg1, null, 0);
        Polygon p2 = new Polygon(polySeg2, null, 1);

        polygonMap.put(0, p1);
        polygonMap.put(1, p2);
    }
    @Test
    public void filterWater() {
        for (Polygon p : polygonMap.values()) {
            p.setIsWater(true);
        }

        CityVertexFilter filter = new CityVertexFilter(polygonMap);

        assertEquals(0, filter.getViableVerticesSet().size());
    }

    @Test
    public void filterWaterEmpty() {
        CityVertexFilter filter = new CityVertexFilter(new HashMap<>());

        assertEquals(0, filter.getViableVerticesSet().size());
    }

    @Test
    public void filterWaterNull() {
        CityVertexFilter filter = new CityVertexFilter(null);

        assertEquals(0, filter.getViableVerticesSet().size());
    }

    @Test
    public void CityChooseEmptyVertexList() {
        CityChooserFactory chooser = new CityChooserFactory(new RandomGen(), new ArrayList<>(), 500);

        assertNull(chooser.getCapital());
        assertEquals(0, chooser.getCities().size());
    }

    @Test
    public void CityChooseNullVertexList() {
        CityChooserFactory chooser = new CityChooserFactory(new RandomGen(), null, 500);

        assertNull(chooser.getCapital());
        assertEquals(0, chooser.getCities().size());
    }

    @Test
    public void CityChooseMoreCitiesThanVertices() {
        List<Vertex> vertices = new ArrayList<>(vertexMap.values());
        CityChooserFactory chooser = new CityChooserFactory(new RandomGen(), vertices, 500);

        assertNotNull(chooser.getCapital());
        assertEquals(3, chooser.getCities().size());
    }

    @Test
    public void CityChooseNegativeCitiesOr0Cities() {
        List<Vertex> vertices = new ArrayList<>(vertexMap.values());
        CityChooserFactory chooser = new CityChooserFactory(new RandomGen(), vertices, -3);

        assertNull(chooser.getCapital());
        assertEquals(0, chooser.getCities().size());

        chooser = new CityChooserFactory(new RandomGen(), vertices, 0);

        assertNull(chooser.getCapital());
        assertEquals(0, chooser.getCities().size());
    }

    @Test
    public void PathFinderAdapterNullTests() {
        PathFinderAdapter adapter = new PathFinderAdapter(null, segmentMap, capital, cities);

        assertEquals(adapter.getCityNodesMap(), new HashMap<>());
        assertEquals(adapter.getEdges(), new ArrayList<>());
        assertEquals(adapter.getNodes(), new ArrayList<>());
        assertEquals(adapter.getSegmentEdgesMap(), new HashMap<>());
        assertNull(adapter.getCapital());

        adapter = new PathFinderAdapter(vertexMap, null, capital, cities);

        assertEquals(adapter.getCityNodesMap(), new HashMap<>());
        assertEquals(adapter.getEdges(), new ArrayList<>());
        assertEquals(adapter.getNodes(), new ArrayList<>());
        assertEquals(adapter.getSegmentEdgesMap(), new HashMap<>());
        assertNull(adapter.getCapital());

        adapter = new PathFinderAdapter(vertexMap, segmentMap, null, cities);

        assertEquals(adapter.getCityNodesMap(), new HashMap<>());
        assertEquals(adapter.getEdges(), new ArrayList<>());
        assertEquals(adapter.getNodes(), new ArrayList<>());
        assertEquals(adapter.getSegmentEdgesMap(), new HashMap<>());
        assertNull(adapter.getCapital());

        adapter = new PathFinderAdapter(vertexMap, segmentMap, capital, null);

        assertEquals(adapter.getCityNodesMap(), new HashMap<>());
        assertEquals(adapter.getEdges(), new ArrayList<>());
        assertEquals(adapter.getNodes(), new ArrayList<>());
        assertEquals(adapter.getSegmentEdgesMap(), new HashMap<>());
        assertNull(adapter.getCapital());
    }

    @Test
    public void PathFinderAdapterEmptyTests() {

        PathFinderAdapter adapter;
        List<Nodes> nodesList = new ArrayList<>();
        Map<City, Nodes> cityNodesMap = new HashMap<>();

        Exception exception = assertThrows(Exception.class, () -> new PathFinderAdapter(new HashMap<>(), segmentMap, capital, cities));

        assertEquals("Capital vertex is not within possible vertex list", exception.getMessage());

        adapter = new PathFinderAdapter(vertexMap, new HashMap<>(), capital, cities);
        for (Vertex v : vertexMap.values()) {
            Nodes n = new Nodes(v.getElevation(), v.getX(), v.getY());
            nodesList.add(n);
        }

        cityNodesMap.put(new MediumCity(vertexMap.get(1)), nodesList.get(1));
        cityNodesMap.put(new MediumCity(vertexMap.get(3)), nodesList.get(3));

        assertEquals(adapter.getCityNodesMap().size(), cityNodesMap.size());
        assertEquals(adapter.getEdges(), new ArrayList<>());
        assertEquals(adapter.getNodes().size(), nodesList.size());
        assertEquals(adapter.getSegmentEdgesMap(), new HashMap<>());
        assertNotNull(adapter.getCapital());

        Vertex v = new Vertex(new Coordinate(6, 6), false, 3, Color.BLACK, 10, 3, false);
        Capital cap = new Capital(v);

        exception = assertThrows(Exception.class, () -> new PathFinderAdapter(vertexMap, segmentMap, cap, cities));

        assertEquals("Capital vertex is not within possible vertex list", exception.getMessage());

        adapter = new PathFinderAdapter(vertexMap, segmentMap, capital, new HashSet<>());

        assertEquals(0, adapter.getCityNodesMap().size());
        assertEquals(adapter.getEdges().size(), segmentMap.size());
        assertEquals(adapter.getNodes().size(), vertexMap.size());
        assertEquals(adapter.getSegmentEdgesMap().size(), segmentMap.size());
        assertNotNull(adapter.getCapital());
    }

    @Test
    public void PathFinderAdapterCitiesNotInVertexMap() {
        Vertex v = new Vertex(new Coordinate(6, 6), false, 3, Color.BLACK, 10, 3, false);
        City c = new MediumCity(v);
        cities.add(c);

        Exception exception = assertThrows(Exception.class, () -> new PathFinderAdapter(vertexMap, segmentMap, capital, cities));

        assertEquals("City vertex is not within possible vertex list", exception.getMessage());
    }

    @AfterAll
    public static void finishTest() {
        logger.info("City test is done");
    }
}
