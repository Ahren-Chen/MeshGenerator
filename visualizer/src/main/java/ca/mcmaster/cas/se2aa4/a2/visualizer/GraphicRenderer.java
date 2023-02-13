package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicRenderer {

    private static final int defaultThickness = 3;

    private Map<String, String> properties;

    private static final ParentLogger logger = new ParentLogger();

    public void render(Mesh aMesh, Graphics2D canvas, boolean debug) {
        //Set initial color and stroke size
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        //Render the vertices and the segments
        renderVertices(aMesh.getVerticesList(), canvas, debug);
        renderSegments(aMesh.getVerticesList(), aMesh.getSegmentsList(), canvas);
    }

    private void renderVertices(List<Vertex> vertexList, Graphics2D canvas, boolean debug) {
        //This method renders the vertices specifically

        //Loop through every vertex
        for (Vertex vertex : vertexList) {
            //Getting a list of properties other than color in a string to string map format
            properties = extractExtraProperties(vertex.getPropertiesList());

            //Set the old color
            Color old = canvas.getColor();

            int thickness = defaultThickness;

            try {
                if (properties.containsKey("thickness")) {
                    thickness = Integer.parseInt(properties.get("thickness"));
                }
            }
            catch (NumberFormatException ex) {
                logger.error("Parsing thickness property to int failed (Default thickness of 3 applied)");
            }

            //Position the X and Y
            double centreX = vertex.getX() - (thickness / 2.0d);
            double centreY = vertex.getY() - (thickness / 2.0d);

            //Set the new color based on properties
            if (debug) {
                if (properties.containsKey("centroid")) {
                    canvas.setColor(Color.RED);
                    //Draw the vertex
                    Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                    canvas.fill(point);
                }
                else {
                    canvas.setColor(Color.BLACK);
                    //Draw the vertex
                    Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                    canvas.fill(point);
                }
            }
            else {
                canvas.setColor(extractColor(vertex.getPropertiesList()));
                //Draw the vertex
                Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
                canvas.fill(point);
            }

            //Reset the color
            canvas.setColor(old);
        }
    }

    private void renderSegments(List<Vertex> vertexList, List<Segment> segmentList, Graphics2D canvas) {
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

            //Then I set the color of the segment, STILL NEED TO ADD ALPHA VALUE
            Color old = canvas.getColor();
            canvas.setColor(extractColor(segment.getPropertiesList()));

            //Getting a list of properties other than color in a string to string map format
            properties = extractExtraProperties(segment.getPropertiesList());

            Stroke oldStroke = canvas.getStroke();
            try {
                if (properties.containsKey("thickness")) {
                    Stroke newStroke = new BasicStroke(
                                            Integer.parseInt(properties.get("thickness")));

                    canvas.setStroke(newStroke);
                }
            }
            catch (NumberFormatException ex) {
                logger.error("Parsing stroke thickness property to int failed (Default thickness of 3 applied)");
            }

            //Then I draw the segment and reset the color
            canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));
            canvas.setColor(old);
            canvas.setStroke(oldStroke);
        }
    }

    private Map<String, String> extractExtraProperties(List<Property> propertiesList) {
        Map<String, String> properties = new HashMap<>();

        for (Property property : propertiesList) {
            if (! property.getKey().equals("rgb_color")) {
                properties.put(property.getKey(), property.getValue());
            }
        }

        return properties;
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgba_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");

        float red;
        float green;
        float blue;
        float alpha;

        try {
            red = Float.parseFloat(raw[0]);
            green = Float.parseFloat(raw[1]);
            blue = Float.parseFloat(raw[2]);
            alpha = Float.parseFloat(raw[3]);
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.error("Converting color failed (Will default to black): " + ex.getMessage());
            return Color.BLACK;
        }

        return new Color(red, green, blue, alpha);
    }
}
