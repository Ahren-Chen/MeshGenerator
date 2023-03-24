package island.Interfaces;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IOEncapsulation.Polygon;
import island.SoilProfiles.Soil;
import island.Utility.RandomGen;

import java.util.*;
import java.util.Map;

public interface ShapeGen {
    Structs.Mesh generate(Structs.Mesh mesh, double width, double height, int lakes, RandomGen bag, int aquifer, int river, String elevation, Soil soil, Biomes biomes);

}
