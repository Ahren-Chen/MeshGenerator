package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import org.locationtech.jts.geom.Coordinate;

import java.net.CookieHandler;
import java.util.List;
import java.util.Map;

public class Lagoon implements ShapeGen {

    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh) {
        logger.trace("Generating lagoon");

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();

        Map<Integer, Vertex> vertexMap = ConvertFromStructs.convert(structsVertexList);
        Map<Integer, Segment> segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        return mesh;
    }
}
