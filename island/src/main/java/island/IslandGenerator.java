package island;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Biomes.Arctic;
import island.Biomes.Desert;
import island.Biomes.Forest;
import island.Biomes.Grassland;
import island.Interfaces.Biomes;
import island.Interfaces.ShapeGen;
import island.Shapes.Lagoon;
import island.SoilProfiles.FastSoil;
import island.SoilProfiles.MediumSoil;
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;

import java.util.Random;

public class IslandGenerator {
    private static final ParentLogger logger = new ParentLogger();
    private final Mesh mesh;
    private final double max_x;
    private final double max_y;
    private final int seed;

    public IslandGenerator(Mesh mesh, double max_x, double max_y, int seed) {
        this.mesh = mesh;
        this.max_x = max_x;
        this.max_y = max_y;

        if (seed != -1) {
            this.seed = seed;
        }
        else {
            Random bag = new Random();

            //To avoid getting 0 as the seed
            this.seed = bag.nextInt(Integer.MAX_VALUE - 1) + 1;
        }
    }

    public Mesh generate(String shape, int lakes, int aquifer,  int river , String elevation, String soil, String biomes) {
        Soil soilProfile;
        switch (soil) {
            case ("slow") -> soilProfile = new SlowSoil();
            case ("medium") -> soilProfile = new MediumSoil();
            default -> soilProfile = new FastSoil();
        }

        Biomes biomesProfile;
        switch (biomes) {
            case ("arctic") -> biomesProfile = new Arctic();
            case ("forest") -> biomesProfile = new Forest();
            case ("grassland") -> biomesProfile = new Grassland();
            default -> biomesProfile = new Desert();
        }

        if (shape.equals("lagoon")) {
            ShapeGen lagoon = new Lagoon();

            return lagoon.generate(mesh, max_x, max_y, lakes, seed, aquifer, river, elevation, soilProfile, biomesProfile);
        }

        else {
            logger.error("No valid mesh shape given in IslandGenerator, assuming lagoon default");
            ShapeGen lagoon = new Lagoon();

            return lagoon.generate(mesh, max_x, max_y, lakes, seed, aquifer, river, elevation, soilProfile, biomesProfile);
        }
    }


}
