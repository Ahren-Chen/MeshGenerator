package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class encapsulates the methods that are responsible for converting anything to do with the Vertex class
 *  it implements Converter2DTo1D and ObjectConverter and utilizes the ConvertColor class implemented as ObjectConverter
 * @author Ahren, Mike, Simon
 * @version February 2023
 */
public class ConvertVertex implements Converter2DTo1D<Vertex, Structs.Vertex>, ObjectConverter<Structs.Vertex, Vertex> {
    private final ObjectConverter<String, float[]> colorConverter = new ConvertColor();

    private static final ParentLogger logger = new ParentLogger();

    /**
     *  This method takes in a 2D List of Vertices,
     *  it converts the input into a 1D List of type Vertex and returns it
     * @param vertices  a 2D List of vertices
     * @return          a 1D list of vertices
     */
    public List<Vertex> convert(List<List<Vertex>> vertices) {
        List<Vertex> result = new ArrayList<>();
        for (List<Vertex> sublist : vertices) {
            result.addAll(sublist);
        }
        return result;
    }

    /**
     *  This method takes in a 2D array of Vertices,
     *  it converts the input into a 1D List of type Structs.Vertex and returns it
     * @param vertices  a 2D array of vertices
     * @return          a 1D List of vertices
     */
    public List<Structs.Vertex> convert(Vertex[][] vertices) {
        List<Structs.Vertex> result = new ArrayList<>();
        for (Vertex[] vertexRow : vertices) {
            for (Vertex vertex : vertexRow) {
                double[] coord = vertex.getCoordinate();

                Structs.Vertex v = Structs.Vertex.newBuilder()
                        .setX(coord[0])
                        .setY(coord[1])
                        .build();
                String colorCode = colorConverter.convert(vertex.getColor());
                Structs.Property color = Structs.Property.newBuilder()
                        .setKey("rgba_color")
                        .setValue(colorCode)
                        .build();

                Structs.Vertex vertexIO = Structs.Vertex.newBuilder(v).addProperties(color).build();

                result.add(vertexIO);
            }
        }
        return result;
    }

    /**
     *  This method takes in a 1D array of Vertices,
     *  it converts the input into a 1D List of type Structs.Vertex and returns it
     * @param vertices  a 1D array of vertices
     * @return          a 1D List of Structs.Vertex
     */
    public List<Structs.Vertex> convert(Vertex[] vertices) {
        List<Structs.Vertex> result = new ArrayList<>();
        for (Vertex v0 : vertices) {
            double[] coord = v0.getCoordinate();

            Structs.Vertex v = Structs.Vertex.newBuilder()
                    .setX(coord[0])
                    .setY(coord[1])
                    .build();

            String colorCode = colorConverter.convert(v0.getColor());
            Structs.Property color = Structs.Property.newBuilder()
                    .setKey("rgba_color")
                    .setValue(colorCode)
                    .build();

            Structs.Vertex vertex = Structs.Vertex.newBuilder(v).addProperties(color).build();
            result.add(vertex);

        }
        return result;
    }

    /**
     *  This method takes in a single Vertex type and
     *  converts the input into a vertex of type Structs.Vertex
     * @param v0        a Vertex type
     * @return          a Vertex of type Structs.Vertex
     */
    public Structs.Vertex convert(Vertex v0) {

        double[] coord=v0.getCoordinate();
        Structs.Vertex v= Structs.Vertex.newBuilder()
                .setX(coord[0])
                .setY(coord[1])
                .build();
        String colorCode= colorConverter.convert(v0.getColor());
        Structs.Property color=Structs.Property.newBuilder()
                .setKey("rgba_color")
                .setValue(colorCode)
                .build();

        Structs.Property centroid = Structs.Property.newBuilder()
                .setKey("centroid")
                .setValue(v0.isCentroid() + "")
                .build();

        Structs.Property thickness = Structs.Property.newBuilder()
                .setKey("thickness")
                .setValue(v0.getThickness() + "")
                .build();

        return Structs.Vertex.newBuilder(v).addProperties(color).addProperties(centroid).addProperties(thickness).build();
    }


}
