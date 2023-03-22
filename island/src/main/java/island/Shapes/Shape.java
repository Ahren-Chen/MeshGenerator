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
    protected Map<Integer, Vertex> vertexMap;
    protected Map<Integer, Segment> segmentMap;
    protected Map<Integer, Polygon> polygonMap;
    protected Map<Integer, Polygon> tileMap;

    protected abstract void affectNeighbors();
    protected abstract void setElevation(String elevationOption);
}
