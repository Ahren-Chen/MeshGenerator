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
import java.util.List;
import java.util.Map;

public class GraphicRenderer {

    private static final int defaultThickness = 3;

    private Map<String, String> properties;
    private static final int THICKNESS = 3;

    public void render(Mesh aMesh, Graphics2D canvas, boolean debug) {
        //Set initial color and stroke size

        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        //Create 2 lists, one for the vertices and one for the segments
        List<Vertex> vertexList = aMesh.getVerticesList();
        List<Segment> segmentList = aMesh.getSegmentsList();

        for (int vertex = 0, segment = 0; vertex < aMesh.getVerticesList().size(); vertex++) {
            //For every vertex in the list, create a Vertex
            Vertex v = vertexList.get(vertex);

            double centre_x = v.getX() - (THICKNESS/2.0d);
            double centre_y = v.getY() - (THICKNESS/2.0d);
            Color old = canvas.getColor();
            canvas.setColor(extractColor(v.getPropertiesList()));
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
            canvas.fill(point);
            canvas.setColor(old);

            if (vertex % 100 != 0) {

                //To draw the segment, the list of vertices is bigger than the list of segments by 1, so I have to do vertex - 1
                //For every segment, I need to get 2 points and their x,y
                double leftX = vertexList.get(
                                segmentList.get(segment).getV1Idx())
                        .getX();

                double rightX = vertexList.get(
                                segmentList.get(segment).getV2Idx())
                        .getX();

                double topY = vertexList.get(
                                segmentList.get(segment).getV1Idx())
                        .getY();

                double bottomY = vertexList.get(
                                segmentList.get(segment).getV2Idx())
                        .getY();

                canvas.setColor(extractColor(segmentList.get(segment).getPropertiesList()));
                canvas.draw(new Line2D.Double(leftX, topY, rightX, bottomY));
                canvas.setColor(old);
                segment++;
            }
        }
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                System.out.println(p.getValue());
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
