import Logging.ParentLogger;
import island.CitiesGen.CityVertexFilter;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    private static final ParentLogger logger = new ParentLogger();
    private Map<Integer, Vertex> vertexMap = new HashMap<>();
    private Map<Integer, Segment> segmentMap = new HashMap<>();
    private Map<Integer, Polygon> polygonMap = new HashMap<>();

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

    @AfterAll
    public static void finishTest() {
        logger.info("City test is done");
    }
}
