package Interfaces;

import java.util.List;

public interface PathFinder<E, N> {
    List<E> findShortestPath(N node1, N node2);
}
