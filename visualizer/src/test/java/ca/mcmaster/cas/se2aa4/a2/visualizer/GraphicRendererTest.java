package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphicRendererTest {
    /*@Test
    public void producesMesh() throws IOException {
        File input = new File("sample.mesh");
        File output = new File("sample.svg");
        Structs.Mesh aMesh = new MeshFactory().read("sample.mesh");

        Graphics2D canvas = SVGCanvas.build(500, 500);
        GraphicRenderer renderer = new GraphicRenderer();
        // Painting the mesh on the canvas
        renderer.render(aMesh, canvas, false);
        // Storing the result in an SVG file
        SVGCanvas.write(canvas, "test.svg");

        boolean isTwoEqual = FileUtils.contentEquals(input, output);
        assertTrue(isTwoEqual);
    }*/
}
