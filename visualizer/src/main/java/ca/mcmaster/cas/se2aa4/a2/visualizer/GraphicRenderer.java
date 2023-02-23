package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

public class GraphicRenderer {

    private static boolean debug;

    private static final float defaultStroke = 0.5f;

    private static Graphics2D canvas;

    private static List<Vertex> vertexList;

    private static List<Polygon> polygonList;

    private static List<Segment> segmentList;

    private static PropertyExtractor properties;

    private static final ParentLogger logger = new ParentLogger();

    public GraphicRenderer(boolean debugMode) {
        debug = debugMode;
    }

    public void render(Mesh aMesh, Graphics2D canvas2D) {

        canvas = canvas2D;
        vertexList = aMesh.getVerticesList();
        segmentList = aMesh.getSegmentsList();
        polygonList = aMesh.getPolygonsList();

        logger.trace("Setting initial color and stroke of the canvas");
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(defaultStroke);
        canvas.setStroke(stroke);

        //Render the vertices and the segments
        renderVertices();
        renderSegments();

        if (debug) {
            renderPolygonNeighbours();
        }
    }

    private void renderVertices() {
        logger.trace("Rendering vertices");

        //Loop through every vertex
        for (Vertex vertex : vertexList) {
            //Extracting properties
            properties = new PropertyExtractor(vertex.getPropertiesList());
            logger.error(vertex.getPropertiesList().size()+"");

            //Set the old color
            Color oldColor = canvas.getColor();

            //Setting the thickness
            int thickness = properties.thickness();

            //Position the X and Y
            double centreX = vertex.getX() - (thickness / 2.0d);
            double centreY = vertex.getY() - (thickness / 2.0d);

            //Set the new color based on properties
            if (debug) {

                //If I am in debug mode, then I check for centroids, if it is a centroid then I make it RED regardless of color property
                if (properties.centroid()) {
                    canvas.setColor(Color.RED);
                    //Draw the vertex
                    Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                    canvas.fill(point);
                }

                //If it is not a centroid, then I make the vertex black
                else {
                    canvas.setColor(Color.BLACK);
                    //Draw the vertex
                    Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                    canvas.fill(point);
                }
            }

            //If I am not in debug mode, then I draw it based on the given color property
            else {
                canvas.setColor(properties.color());
                //Draw the vertex
                Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                canvas.fill(point);
            }

            //Reset the color
            canvas.setColor(oldColor);
        }
    }

    private void renderSegments() {
        logger.trace("Rendering Segments");

        for (Segment segment : segmentList) {
            //To draw the segment, I need the X and Y values of my 2 vertices
            double v1X = vertexList.get(
                            segment.getV1Idx())
                    .getX();

            double v2X = vertexList.get(
                            segment.getV2Idx())
                    .getX();

            double v1Y = vertexList.get(
                            segment.getV1Idx())
                    .getY();

            double v2Y = vertexList.get(
                            segment.getV2Idx())
                    .getY();

            //Remember the old stroke size
            Stroke oldStroke = canvas.getStroke();

            //Extracting properties
            properties = new PropertyExtractor(segment.getPropertiesList());

            //Then I set the color of the segment
            Color oldColor = canvas.getColor();

            if (debug) {
                canvas.setColor(Color.BLACK);
            }
            else {
                canvas.setColor(properties.color());
            }

            Stroke newStroke = new BasicStroke(properties.thickness());
            canvas.setStroke(newStroke);

            //Then I draw the segment and reset the color
            canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));

            canvas.setColor(oldColor);
            canvas.setStroke(oldStroke);
        }
    }

    private void renderPolygonNeighbours() {
        logger.trace("Rendering polygons and their neighbours");

        for (Polygon polygon : polygonList) {
            Vertex centroidMain = vertexList.get(
                                    polygon.getCentroidIdx());

            double v1X = centroidMain.getX();
            double v1Y = centroidMain.getY();

            for (int index : polygon.getNeighborIdxsList()) {
                Polygon polygonNeighbour = polygonList.get(index);

                Vertex centroidToConnect = vertexList.get(
                                            polygonNeighbour.getCentroidIdx());

                double v2X = centroidToConnect.getX();
                double v2Y = centroidToConnect.getY();

                Color oldColor = canvas.getColor();
                Stroke oldStroke = canvas.getStroke();

                canvas.setColor(Color.lightGray);

                canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));

                canvas.setColor(oldColor);
                canvas.setStroke(oldStroke);
            }
        }
    }
}