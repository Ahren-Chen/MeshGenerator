package island.Shapes;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.Converters.ConvertFromStructs;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Segment;
import island.IOEncapsulation.Vertex;
import island.Interfaces.ShapeGen;
import island.Tiles.OceanTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lagoon implements ShapeGen {
    private final ParentLogger logger = new ParentLogger();
    public Mesh generate(Mesh mesh) {
        logger.trace("Generating lagoon");

        List<Structs.Vertex> structsVertexList = mesh.getVerticesList();
        List<Structs.Segment> structsSegmentList = mesh.getSegmentsList();
        List<Structs.Polygon> structsPolygonList = mesh.getPolygonsList();

        Map<Integer, Vertex> vertexMap = ConvertFromStructs.convert(structsVertexList);
        Map<Integer, Segment> segmentMap = ConvertFromStructs.convert(structsSegmentList, vertexMap);
        Map<Integer, Polygon> polygonMap = ConvertFromStructs.convert(structsPolygonList, vertexMap, segmentMap);

        List<OceanTile> oceanTileList = new ArrayList<>();
        for (Polygon polygon : polygonMap.values()) {
            OceanTile poly = new OceanTile(polygon);
            oceanTileList.add(poly);
        }

        List<Structs.Polygon> tileList = new ArrayList<>();
        for (OceanTile tile : oceanTileList) {
            tileList.add(tile.convertToStruct());
        }

        return Mesh
                .newBuilder()
                .addAllVertices(structsVertexList)
                .addAllSegments(structsSegmentList)
                .addAllPolygons(tileList)
                .build();
    }
}
