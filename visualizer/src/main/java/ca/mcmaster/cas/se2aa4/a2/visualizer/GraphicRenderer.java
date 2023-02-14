package ca.mcmaster.cas.se2aa4.a2.visualizer;

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

    public void render(Mesh aMesh, Graphics2D canvas, boolean debug) {
        //Set initial color and stroke size
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        //Render the vertices and the segments
        renderVertices(aMesh.getVerticesList(), canvas, debug);
        renderSegments(aMesh.getVerticesList(), aMesh.getSegmentsList(), canvas, debug);

        if (debug) {

        }
    }

    private void renderVertices(List<Vertex> vertexList, Graphics2D canvas, boolean debug) {
        //This method renders the vertices specifically

        //Loop through every vertex
        for (Vertex vertex : vertexList) {
            //Getting a list of properties other than color in a string to string map format
            PropertyExtractor properties = new PropertyExtractor(vertex.getPropertiesList());

            //Set the old color
            Color old = canvas.getColor();

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
            canvas.setColor(old);
        }
    }

    private void renderSegments(List<Vertex> vertexList, List<Segment> segmentList, Graphics2D canvas, boolean debug) {
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

            //Getting a list of properties other than color in a string to string map format
            PropertyExtractor properties = new PropertyExtractor(segment.getPropertiesList());

            //Then I set the color of the segment
            Color old = canvas.getColor();

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

            canvas.setColor(old);
            canvas.setStroke(oldStroke);
        }
    }

    private void renderPolygonNeighbours(List<Vertex> vertexList, List<Polygon> polygonList, Graphics2D canvas) {

    }
}