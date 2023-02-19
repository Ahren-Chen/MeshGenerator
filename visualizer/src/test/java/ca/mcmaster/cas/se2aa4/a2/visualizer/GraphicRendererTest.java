package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.apache.batik.svggen.SVGGraphics2D;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphicRendererTest {
    private static final ParentLogger logger = new ParentLogger();

    @BeforeAll
    public static void initGraphicRenderer() {
        logger.info("\n Initializing GraphicRenderer testing \n");
    }
    /*@Test
    public void emptyMesh() throws IOException {
        Mesh emptyMesh = Mesh.newBuilder().build();

        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: emptyMesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        // Creating the Canvas to draw the mesh
        Graphics2D canvas = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));
        Graphics2D canvasTester = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));

        GraphicRenderer renderer = new GraphicRenderer();

        // Painting the mesh on the canvas
        renderer.render(emptyMesh, canvas, true);
        Color old = canvas.getColor();
        canvas.setColor(new Color(100));
        Ellipse2D point = new Ellipse2D.Double(5, 5, 3, 3);
        canvas.fill(point);
        canvas.setColor(old);

        assertEquals(canvas.toString(), canvasTester.toString());
    }*/

    @AfterAll
    public static void doneTesting() {
        logger.info("\n GraphicRenderer done testing \n");
    }
}
