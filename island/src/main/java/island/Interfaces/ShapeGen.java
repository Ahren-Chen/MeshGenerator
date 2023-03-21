package island.Interfaces;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IOEncapsulation.Polygon;
import java.util.*;
import java.util.Map;

public interface ShapeGen {
    Structs.Mesh generate(Structs.Mesh mesh, double width, double height, int lakes, int seed, int aquifier);

}
