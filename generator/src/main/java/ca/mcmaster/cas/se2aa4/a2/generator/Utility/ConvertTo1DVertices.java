package ca.mcmaster.cas.se2aa4.a2.generator.Utility;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Converters.ConvertColor;
import ca.mcmaster.cas.se2aa4.a2.generator.Interfaces.Converter2DTo1D;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class encapsulates the methods that are responsible for converting anything to do with the Vertex class
 *  it implements Converter2DTo1D and SelfConverter and utilizes the ConvertColor class implemented as SelfConverter
 * @author Ahren, Mike, Simon
 * @version February 2023
 */
public class ConvertTo1DVertices implements Converter2DTo1D<Vertex, Vertex> {
    private final ConvertColor colorConverter = new ConvertColor();
    private static final ParentLogger logger = new ParentLogger();

    /**
     *  This method takes in a 2D List of Vertices,
     *  it converts the input into a 1D List of type Vertex and returns it
     * @param vertices  a 2D List of vertices
     * @return          a 1D list of vertices
     */
    public List<Vertex> convert(List<List<Vertex>> vertices) {
        logger.trace("Converting 2D List to 1D List");
        List<Vertex> result = new ArrayList<>();
        for (List<Vertex> sublist : vertices) {
            result.addAll(sublist);
        }
        return result;
    }

    /**
     *  This method takes in a 2D array of Vertices,
     *  it converts the input into a 1D List of type Structs.Vertex and returns it
     * @param vertices a 2D array of vertices
     * @return {@code List<Vertex>} a 1D List of vertices
     */
    public List<Vertex> convert(Vertex[][] vertices) {
        logger.trace("Converting a ");
        int count=0;
        List<Vertex> result = new ArrayList<>();
        for (Vertex[] vertexRow : vertices) {
            for (Vertex vertex : vertexRow) {
                vertex.setID(count++);
                result.add(vertex);
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

            result.add(v0.convertToStruct());
        }
        return result;
    }
}
