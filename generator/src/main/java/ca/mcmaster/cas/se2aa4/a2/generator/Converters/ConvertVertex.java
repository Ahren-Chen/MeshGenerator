package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

public class ConvertVertex implements Converter2DTo1D<Vertex, Structs.Vertex>, ObjectConverter<Structs.Vertex, Vertex> {

    /**
     * Converts <insert description here>
     */

    private final ConvertColor colorConverter = new ConvertColor();

    private static final ParentLogger logger=new ParentLogger();

    public List<Vertex> convert(List<List<Vertex>> vertices) {
        List<Vertex> result = new ArrayList<>();
        for (List<Vertex> sublist : vertices) {
            result.addAll(sublist);
        }
        return result;
    }

    public List<Structs.Vertex> convert(Vertex[][] vertices) {
        List<Structs.Vertex> result = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices[i].length; j++) {
                Vertex v0= vertices[i][j];
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
                Structs.Vertex vertex= Structs.Vertex.newBuilder(v).addProperties(color).build();

                result.add(vertex);
            }
        }
        return result;
    }

    public Structs.Vertex convert(Vertex v0) {

        double[] coord=v0.getCoordinate();
        Structs.Vertex v= Structs.Vertex.newBuilder()
                .setX(coord[0])
                .setY(coord[1])
                .build();
        String colorCode= colorConverter.convert(v0.getColor());

        Structs.Property color = Structs.Property.newBuilder()
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

    public List<Structs.Vertex> convert(Vertex[] vertices) {
        List<Structs.Vertex> result = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
                Vertex v0= vertices[i];
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

                Structs.Vertex vertex= Structs.Vertex.newBuilder(v).addProperties(color).build();
                result.add(vertex);

        }
        return result;
    }
}
