package island;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Biomes.Arctic;
import island.Biomes.Desert;
import island.Biomes.Forest;
import island.Biomes.Grassland;
import island.Interfaces.Biomes;
import island.Interfaces.LagoonGen;
import island.Interfaces.PolygonIslandGen;
import island.Shapes.Lagoon;
import island.Shapes.Star;
import island.SoilProfiles.FastSoil;
import island.SoilProfiles.MediumSoil;
import island.SoilProfiles.SlowSoil;
import island.SoilProfiles.Soil;
import island.Utility.RandomGen;

import java.awt.*;

public class IslandGenerator {
    private static final ParentLogger logger = new ParentLogger();
    private final Mesh mesh;
    private final double max_x;
    private final double max_y;
    private final RandomGen bag;

    public IslandGenerator(Mesh mesh, double max_x, double max_y, int seed) {
        this.mesh = mesh;
        this.max_x = max_x;
        this.max_y = max_y;

        if (seed != -1) {
            bag = new RandomGen(seed);
        }
        else {
            bag = new RandomGen();
        }
    }

    public Mesh generate(String shape, int lakes, int aquifer,  int river , String elevation, String soil, String biomes) {
        LagoonGen lagoon = new Lagoon();
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

        switch (shape) {
            case "lagoon" -> {


                return lagoon.generate(mesh, max_x, max_y, lakes, bag, aquifer, river, elevation, soilProfile, biomesProfile);
            }
            case "star" -> {
                PolygonIslandGen star = new Star();
                Polygon starShape = new Polygon();
                double outerCircleRadius = Math.min(max_x, max_y) / 2;
                double innerCircleRadius = outerCircleRadius / 2;
                double centerX = max_x / 2;
                double centerY = max_y / 2;
                starShape.addPoint((int) centerX, (int) (centerY + outerCircleRadius));
                starShape.addPoint((int) (centerX + innerCircleRadius * Math.cos(Math.toRadians(54))), (int) (centerY + innerCircleRadius * Math.sin(Math.toRadians(54))));
                starShape.addPoint((int) (centerX + outerCircleRadius * Math.cos(Math.toRadians(18))), (int) (centerY + outerCircleRadius * Math.sin(Math.toRadians(18))));
                starShape.addPoint((int) (centerX + innerCircleRadius * Math.cos(Math.toRadians(342))), (int) (centerY + innerCircleRadius * Math.sin(Math.toRadians(342))));
                starShape.addPoint((int) (centerX + outerCircleRadius * Math.cos(Math.toRadians(306))), (int) (centerY + outerCircleRadius * Math.sin(Math.toRadians(306))));
                starShape.addPoint((int) centerX, (int) (centerY - innerCircleRadius));
                starShape.addPoint((int) (centerX + outerCircleRadius * Math.cos(Math.toRadians(234))), (int) (centerY + outerCircleRadius * Math.sin(Math.toRadians(234))));
                starShape.addPoint((int) (centerX + innerCircleRadius * Math.cos(Math.toRadians(198))), (int) (centerY + innerCircleRadius * Math.sin(Math.toRadians(198))));
                starShape.addPoint((int) (centerX + outerCircleRadius * Math.cos(Math.toRadians(162))), (int) (centerY + outerCircleRadius * Math.sin(Math.toRadians(162))));
                starShape.addPoint((int) (centerX + innerCircleRadius * Math.cos(Math.toRadians(126))), (int) (centerY + innerCircleRadius * Math.sin(Math.toRadians(126))));
                starShape.addPoint((int) centerX, (int) (centerY + outerCircleRadius));
                return star.generate(mesh, starShape, max_x, max_y, lakes, bag, aquifer, river, elevation, soilProfile, biomesProfile);
            }
            case "bridge" -> {
                logger.trace("Generating bridge shape");
            }
            default -> {
                logger.error("No valid mesh shape given in IslandGenerator, assuming lagoon default");
                LagoonGen lagoon1 = new Lagoon();

                return lagoon1.generate(mesh, max_x, max_y, lakes, bag, aquifer, river, elevation, soilProfile, biomesProfile);
            }
        }
        return lagoon.generate(mesh, max_x, max_y, lakes, bag, aquifer, river, elevation, soilProfile, biomesProfile);

    }


}
