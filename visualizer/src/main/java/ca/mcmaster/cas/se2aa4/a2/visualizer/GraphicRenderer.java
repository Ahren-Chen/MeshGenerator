package ca.mcmaster.cas.se2aa4.a2.visualizer;

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
            properties = extractProperties(vertex.getPropertiesList());

            //Set the old color
            Color old = canvas.getColor();

            int thickness = defaultThickness;

            try {
                if (properties.containsKey("thickness")) {
                    thickness = Integer.parseInt(properties.get("thickness"));
                }
            }
            catch (NumberFormatException ignored) {}

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
                float alpha;
                try {
                    if (properties.containsKey("alpha")) {
                        alpha = Float.parseFloat(properties.get("alpha"));
                    }
                    else {
                        alpha = 1;
                    }
                }
                catch (NumberFormatException ex) {
                    alpha = 1;
                }
                canvas.setColor(extractColor(vertex.getPropertiesList(), alpha));
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
            canvas.setColor(extractColor(segment.getPropertiesList(), 1));

            //Getting a list of properties other than color in a string to string map format
            properties = extractProperties(segment.getPropertiesList());

            Stroke oldStroke = canvas.getStroke();
            try {
                if (properties.containsKey("thickness")) {
                    Stroke newStroke = new BasicStroke(
                                            Integer.parseInt(properties.get("thickness")));

                    canvas.setStroke(newStroke);
                }
            }
            catch (NumberFormatException ignore) {}

            //Then I draw the segment and reset the color
            canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));
            canvas.setColor(old);
            canvas.setStroke(oldStroke);
        }
    }

    private Map<String, String> extractProperties(List<Property> propertiesList) {
        Map<String, String> properties = new HashMap<>();

        for (Property property : propertiesList) {
            if (! property.getKey().equals("rgb_color")) {
                properties.put(property.getKey(), property.getValue());
            }
        }

        return properties;
    }

    private Color extractColor(List<Property> properties, float alpha) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        float red = Float.parseFloat(raw[0]);
        float green = Float.parseFloat(raw[1]);
        float blue = Float.parseFloat(raw[2]);
        return new Color(red, green, blue, alpha);
    }
}
