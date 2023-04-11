package island.Interfaces;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.SoilProfiles.Soil;
import island.Utility.RandomGen;

public interface PolygonIslandGen {
    Structs.Mesh generate(Structs.Mesh mesh, java.awt.Polygon shape, double width, double height, int lakes, RandomGen bag, int aquifer, int river, String elevation, Soil soil, Biomes biomes, String heatMapOption, int cities);

}
