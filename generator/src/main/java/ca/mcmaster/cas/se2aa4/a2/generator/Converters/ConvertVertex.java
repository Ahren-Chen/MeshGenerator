package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import ca.mcmaster.cas.se2aa4.a2.generator.Converters.Converter;
import ca.mcmaster.cas.se2aa4.a2.generator.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

public class ConvertVertex implements Converter<Vertex> {

    /**
     * Converts <insert description here>
     */
    public List<Vertex> convert(List<List<Vertex>> vertices) {
        List<Vertex> result = new ArrayList<>();
        for (List<Vertex> sublist : vertices) {
            result.addAll(sublist);
        }
        return result;
    }



}
