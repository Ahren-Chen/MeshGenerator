import ADT.Edges;
import ADT.Nodes;
import Algorithms.Dijkstra;
import Interfaces.PathFinder;
import Logging.ParentLogger;
import Utility.NodeConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {

    private static final ParentLogger logger = new ParentLogger();
    private List<Nodes> nodesList;
    private List<Edges> edgesList;

    @BeforeAll
    public static void initTest() {
        logger.info("Beginning test");
    }

    @BeforeEach
    public void setGraph() {
        List<Nodes> nodesList = new ArrayList<>();
        List<Edges> edgesList = new ArrayList<>();

        Nodes n1 = new Nodes (0, 0, 0, false);
        Nodes n2 = new Nodes (0, 3, 0, false);
        Nodes n3 = new Nodes (0, 3, 4, false);
        nodesList.add(n1);
        nodesList.add(n2);
        nodesList.add(n3);

        edgesList.add(new Edges(n1, n2));
        edgesList.add(new Edges(n1, n3));
        edgesList.add(new Edges(n2, n3));

        this.nodesList = nodesList;
        this.edgesList = edgesList;
    }
    @Test
    public void nodeCreation() {
        List<Nodes> nodeStorage = new ArrayList<>();

        nodeStorage.add(new Nodes());
        nodeStorage.add(new Nodes(5, 0, 0, false));
        nodeStorage.add(new Nodes(-5, 0, 0, true));

        assertEquals(nodeStorage.get(0).getElevation(), -1);
        assertEquals(nodeStorage.get(1).getElevation(), 5);
        assertEquals(nodeStorage.get(2).getElevation(), 0);
    }

    @Test
    public void sameNodeEdgeCreation() {
        Exception exception = assertThrows(Exception.class, () -> new Edges(nodesList.get(0), nodesList.get(0)));

        assertEquals("Trying to make an edge with the same nodes", exception.getMessage());
    }

    @Test
    public void neighborTest() {
        NodeConnector.setNodeConnections(edgesList);

        Nodes n1 = nodesList.get(0);
        Nodes n2 = nodesList.get(1);

        Set<Edges> n1Neighbors = n1.getNeighbors();
        Set<Edges> n2Neighbors = n2.getNeighbors();

        assertTrue(n1Neighbors.contains(edgesList.get(0)));
        assertTrue(n2Neighbors.contains(edgesList.get(0)));
    }

    @Test
    public void dijkstraTest() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra(nodesList.size());

        List<Edges> result = algorithm.findShortestPath(nodesList.get(0), nodesList.get(2), nodesList, edgesList);
        List<Edges> expected = new ArrayList<>();
        expected.add(edgesList.get(1));

        assertEquals(expected, result);
    }

    @Test
    public void dijkstraTest2() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra(nodesList.size());
        Edges edge = edgesList.remove(1);

        List<Edges> result = algorithm.findShortestPath(nodesList.get(0), nodesList.get(2), nodesList, edgesList);
        List<Edges> expected = new ArrayList<>();
        expected.add(edgesList.get(0));
        expected.add(edgesList.get(1));

        assertEquals(expected, result);
    }

    @Test
    public void emptyNodeList() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra(nodesList.size());
        edgesList.remove(1);

        Exception exception = assertThrows(Exception.class, () -> algorithm.findShortestPath(nodesList.get(0), nodesList.get(2), new ArrayList<>(), edgesList));

        assertEquals("Node is missing from the node list but contains edge connecting to it", exception.getMessage());
    }

    @Test
    public void emptyEdgeList() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra(nodesList.size());
        edgesList.remove(1);

        List<Edges> result = algorithm.findShortestPath(nodesList.get(0), nodesList.get(2), nodesList, new ArrayList<>());
        List<Edges> expected = new ArrayList<>();

        assertEquals(expected, result);
    }

    @Test
    public void nullArgumentTest() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra((nodesList.size()));
        Exception exception = assertThrows(Exception.class, () -> algorithm.findShortestPath(null, null, null, null));

        assertEquals("Null element given", exception.getMessage());
    }

    @Test
    public void orderingTest() {
        PathFinder<Edges, Nodes> algorithm = new Dijkstra(nodesList.size());
        Edges edge = edgesList.remove(1);
        edgesList.add(edge);

        List<Edges> result = algorithm.findShortestPath(nodesList.get(0), nodesList.get(2), nodesList, edgesList);
        List<Edges> expected = new ArrayList<>();
        expected.add(edgesList.get(2));

        assertEquals(expected, result);
    }

    @AfterAll
    public static void endTest() {
        logger.info("Finished tests");
    }
}
