package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;

import java.util.List;

public class Lagoon implements ShapeGen {

    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh) {
        logger.trace("Generating lagoon");

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();

        List<Vertex> vertices = ConvertFromStructs.convert(structsVertexList);
        return mesh;
    }
}
