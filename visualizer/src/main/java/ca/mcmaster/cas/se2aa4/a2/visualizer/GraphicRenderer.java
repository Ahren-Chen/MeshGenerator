package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
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
import java.util.List;

public class GraphicRenderer {

    private static final int defaultThickness = 3;

    public void render(Mesh aMesh, Graphics2D canvas) {
        //Set initial color and stroke size
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        //Render the vertices and the segments
        renderVertices(aMesh.getVerticesList(), canvas);
        renderSegments(aMesh.getVerticesList(), aMesh.getSegmentsList(), canvas);
    }

    private void renderVertices(List<Vertex> vertexList, Graphics2D canvas) {
        //This method renders the vertices specifically

        //Loop through every vertex
        for (Vertex vertex : vertexList) {

            //Set the color
            Color old = canvas.getColor();
            canvas.setColor(extractColor(vertex.getPropertiesList()));

            //Position the X and Y
            int thickness = getThickness(vertex.getPropertiesList());
            double centreX = vertex.getX() - (thickness / 2.0d);
            double centreY = vertex.getY() - (thickness / 2.0d);

            //Draw the vertex
            Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
            canvas.fill(point);

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

            //Then I set the color of the segment
            Color old = canvas.getColor();
            canvas.setColor(extractColor(segment.getPropertiesList()));

            //Then I draw the segment and reset the color
            canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));
            canvas.setColor(old);
        }
    }

    private int getThickness(List<Property> properties) {
        return defaultThickness;
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }
}
