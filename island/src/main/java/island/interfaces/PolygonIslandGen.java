package island.interfaces;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.soilProfiles.Soil;
import island.utility.RandomGen;

public interface PolygonIslandGen {
    Structs.Mesh generate(Structs.Mesh mesh, java.awt.Polygon shape, double width, double height, int lakes, RandomGen bag, int aquifer, int river, String elevation, Soil soil, Biomes biomes);
}
