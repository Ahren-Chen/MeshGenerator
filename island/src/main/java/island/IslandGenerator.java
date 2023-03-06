package island;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Interfaces.ShapeGen;
import island.Shapes.Lagoon;

public class IslandGenerator {
    private static final ParentLogger logger = new ParentLogger();

    private final Mesh mesh;

    public IslandGenerator(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh generate(String shape) {
        if (shape.equals("lagoon")) {
            ShapeGen lagoon = new Lagoon();

            return lagoon.generate(mesh);
        }

        else {
            logger.error("No valid mesh shape given in IslandGenerator, assuming lagoon default");
            ShapeGen lagoon = new Lagoon();

            return lagoon.generate(mesh);
        }
    }
}
