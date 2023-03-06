package island.Converters;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IOEncapsulation.Vertex;
import island.PropertyExtractor;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class ConvertFromStructs {
    private static PropertyExtractor properties;

    public static List<Vertex>  convert (List<Structs.Vertex> structsVertexList) {
        List<Vertex> vertices = new ArrayList<>();
        for (int vertexIdx = 0; vertexIdx < structsVertexList.size(); vertexIdx++) {
            Structs.Vertex vertex = structsVertexList.get(vertexIdx);
            double x = vertex.getX();
            double y = vertex.getY();
            Coordinate cords = new Coordinate(x, y);

            List<Structs.Property> structsProperties = vertex.getPropertiesList();

            properties = new PropertyExtractor(structsProperties);

            Vertex newVertex = new Vertex(
                    cords,
                    properties.isCentroid(),
                    properties.thickness(),
                    properties.color(),
                    vertexIdx);

            vertices.add(newVertex);
        }

        return vertices;
    }
}
