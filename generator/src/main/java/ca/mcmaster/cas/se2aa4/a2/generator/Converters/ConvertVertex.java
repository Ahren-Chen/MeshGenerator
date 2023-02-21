package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import ca.mcmaster.cas.se2aa4.a2.generator.Converters.Converter;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ConvertVertex implements Converter<Vertex> {

    /**
     * Converts the 2D Vertex List into a 1D Vertex List
     */
    public List<Vertex> convert2DTo1D(List<List<Vertex>> array) {
        List<Vertex> newArray = new ArrayList<>();

        //For every row in the 2D List, add every item in that list to the 1D list.
        for (List<Vertex> vertices : array) {
            newArray.addAll(vertices);
        }

        return newArray;
    }
}
