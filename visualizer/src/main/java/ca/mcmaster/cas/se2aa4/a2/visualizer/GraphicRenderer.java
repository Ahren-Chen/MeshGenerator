package ca.mcmaster.cas.se2aa4.a2.visualizer;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 *  This class is responsible for rendering a Mesh data type given to it.
 *  It utilizes the information stored within the mesh to display vertices, segments, and polygons.
 *  This class utilizes the PropertyExtractor class to retrieve the properties of each component of the mesh.
 * @author Ahren, Mike, Simon
 * @version February 2023
 */
public class GraphicRenderer {
    private static boolean debug;
    private static final float defaultStroke = 0.5f;
    private static Graphics2D canvas;
    private static List<Vertex> vertexList;
    private static List<Structs.Polygon> polygonList;
    private static List<Segment> segmentList;
    private static final List<Vertex> listOfAllPolygonVertices = new ArrayList<>();
    private static PropertyExtractor properties;
    private static final ParentLogger logger = new ParentLogger();

    /**
     *  Creates a GraphicRenderer a mode (debug or normal) based on the given boolean argument with true being debug mode
     */
    public GraphicRenderer(boolean debugMode) {
        debug = debugMode;
    }

    /**
     *  This method takes in a Mesh and a Graphics2D canvas to render the information stored in the Mesh
     *  onto the canvas.
     * @param aMesh     a Mesh data type from the given I/O
     * @param canvas2D  a Graphics2D canvas
     */
    public void render(Mesh aMesh, Graphics2D canvas2D) {

        //Set the private static variables taken from the Mesh and canvas
        canvas = canvas2D;
        vertexList = aMesh.getVerticesList();
        segmentList = aMesh.getSegmentsList();
        polygonList = aMesh.getPolygonsList();

        logger.trace("Setting initial color and stroke of the canvas");
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(defaultStroke);
        canvas.setStroke(stroke);

        //Render polygon (responsible only for filling in the color) if it is not in debug more for clarity
        if (! debug) {
            renderPolygons();
        }

        //Render the segments and vertices
        renderSegments();
        renderVertices();

        //If I am in debug mode, then render the neighbouring attributes
        if (debug) {
            renderPolygonNeighbours();
        }
    }

    /**
     *  This method renders all vertices (including centroids).
     *  Information is taken from the Vertex list in the Mesh
     */
    private void renderVertices() {
        logger.trace("Rendering vertices");

        //Loop through every vertex
        for (Vertex vertex : vertexList) {
            //Extracting properties
            properties = new PropertyExtractor(vertex.getPropertiesList());

            //Set the old canvas color
            Color oldCanvasColor = canvas.getColor();

            //Setting the thickness
            int thickness = properties.thickness();

            //Position the X and Y
            double centreX = vertex.getX() - (thickness / 2.0d);
            double centreY = vertex.getY() - (thickness / 2.0d);

            //Set the color of the canvas based on the color of the Vertex (taken from properties list)
            if (debug) {

                //If I am in debug mode, then I check for centroids, if it is a centroid then I make it Color.RED
                // regardless of original vertex color
                if (properties.centroid()) {
                    canvas.setColor(Color.RED);
                }

                //If it is not a centroid, then I make the vertex black
                else {
                    canvas.setColor(Color.BLACK);
                }
            }

            //If I am not in debug mode, then I set the color based on the given color property
            else {
                canvas.setColor(properties.color());
            }

            //Draw the vertex
            Ellipse2D point = new Ellipse2D.Double(centreX, centreY, thickness, thickness);
            canvas.fill(point);

            //Reset the color
            canvas.setColor(oldCanvasColor);
        }
    }

    /**
     *  This method renders all segments (not including neighbouring relations).
     *  Information is taken from the Segment list in the Mesh
     */
    private void renderSegments() {
        logger.trace("Rendering Segments");

        for (Segment segment : segmentList) {
            //To draw the segment, I need the X and Y values of my 2 vertices

            //First I get the indices of the 2 vertices in the Vertex list
            int v1Idx = segment.getV1Idx();
            int v2Idx = segment.getV2Idx();

            //I get the vertices that the segment is built upon
            Vertex v1 = vertexList.get(v1Idx);
            Vertex v2 = vertexList.get(v2Idx);

            //I get the X and Y values
            double v1X = v1.getX();
            double v2X = v2.getX();

            double v1Y = v1.getY();
            double v2Y = v2.getY();

            //Remember the old stroke size
            Stroke oldStrokeSize = canvas.getStroke();

            //Extracting properties
            properties = new PropertyExtractor(segment.getPropertiesList());

            //Then I store the color of the current/old canvas color
            Color oldCanvasColor = canvas.getColor();

            //If I am in debug mode, I set the segment color to be black
            if (debug) {
                canvas.setColor(Color.BLACK);
            }

            //Otherwise, I set the segment color to be the one given in its properties
            else {
                canvas.setColor(properties.color());
            }

            //I set a new stroke size based on the thickness property of the segment
            Stroke newStrokeSize = new BasicStroke(properties.thickness());
            canvas.setStroke(newStrokeSize);

            //Then I draw the segment
            canvas.draw(new Line2D.Double(v1X, v1Y, v2X, v2Y));

            //Resetting the color and stroke size
            canvas.setColor(oldCanvasColor);
            canvas.setStroke(oldStrokeSize);
        }
    }

    /**
     *  This method renders all polygons (Is only responsible for filling in the color of the polygon).
     *  Information is taken from the Polygon list in the Mesh
     */
    private void renderPolygons() {
        logger.trace("Rendering Polygons");

        //Loop through every polygon that needs to be rendered
        for (Structs.Polygon polygon : polygonList) {

            //For each polygon, I loop through the segment indices list
            for (int segmentIdx : polygon.getSegmentIdxsList()) {

                //Get the consecutive segments that create the polygon
                Segment segment = segmentList.get(segmentIdx);

                //Get the indices of the vertices that the segment is built upon
                int v1Idx = segment.getV1Idx();
                int v2Idx = segment.getV2Idx();

                //Get the Vertices that the segment is built upon from the Vertex List
                Vertex v1 = vertexList.get(v1Idx);
                Vertex v2 = vertexList.get(v2Idx);

                //If the list of polygon vertices does not contain any of the vertices,
                //then add both of them in order
                if (! (listOfAllPolygonVertices.contains(v1) || listOfAllPolygonVertices.contains(v2))) {
                    listOfAllPolygonVertices.add(v1);
                    listOfAllPolygonVertices.add(v2);
                }

                //If the list of recognized polygon vertices only contain v1, then we add v2 as the new vertex
                //In this case, v1 is the connecting vertex between 2 segments
                else if (listOfAllPolygonVertices.contains(v1)) {
                    listOfAllPolygonVertices.add(v2);
                }

                //Or add v1 as the new vertex
                //In this case, v2 is the connecting vertex between 2 segments
                else if (listOfAllPolygonVertices.contains(v2)) {
                    listOfAllPolygonVertices.add(v1);
                }

                //If none of the if statements are true, that means that the segment is connecting the most recently
                //added vertex and the initial vertex (the last segment)
            }

            //Create an empty polygon
            Polygon poly = new Polygon();

            //Add the necessary points to the polygon and cast the X and Y coordinates to integer
            //Note: The thickness of the segments and vertices will cover and canvas space lost due to type casting
            for (Vertex v : listOfAllPolygonVertices) {
                poly.addPoint((int) v.getX(), (int) v.getY());
            }

            //Store the old canvas color
            Color oldCanvasColor = canvas.getColor();

            //Extract the properties of the Polygon
            properties = new PropertyExtractor(polygon.getPropertiesList());

            //Set the color to fill as the one designated by the polygon color property
            canvas.setColor(properties.color());

            canvas.fillPolygon(poly);

            //Reset the canvas color
            canvas.setColor(oldCanvasColor);

            //Clear the list of vertices to make room for the next polygon
            listOfAllPolygonVertices.clear();
        }
    }

    /**
     *  This method renders all polygon neighbouring relationships.
     *  Information is taken from the Polygon list in the Mesh
     */
    private void renderPolygonNeighbours() {
        logger.trace("Rendering polygon neighbours");

        for (Structs.Polygon polygon : polygonList) {

            int centroidIdx = polygon.getCentroidIdx();

            Vertex centroidMain = vertexList.get(centroidIdx);

            double v1X = centroidMain.getX();
            double v1Y = centroidMain.getY();

            for (int index : polygon.getNeighborIdxsList()) {
                Structs.Polygon polygonNeighbour = polygonList.get(index);

                centroidIdx = polygonNeighbour.getCentroidIdx();
                Vertex centroidToConnect = vertexList.get(centroidIdx);

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