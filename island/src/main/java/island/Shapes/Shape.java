package island.Shapes;

import island.Interfaces.ShapeGen;
import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Tiles.BiomesTile;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;
import java.util.*;

public abstract class Shape implements ShapeGen {
    protected abstract Map<Integer, Vertex> vertexMap();
    protected abstract Map<Integer, Segment> segmentMap();
    protected abstract Map<Integer, Polygon> polygonMap();
    protected abstract Map<Integer, Polygon> tileMap();

    protected abstract void affectNeighbors();
    protected abstract void setElevation(String elevationOption);
}
