package island.IOEncapsulation;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;
import island.Interfaces.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements ConvertToStruct<Structs.Polygon>, Tile<Polygon> {
    private final List<Segment> segments;
    private final Vertex centroid;
    private List<Polygon> neighbours;
    private final int ID;
    private Color color;
    protected double temperature = 0;
    protected double precipitation = 0;
    private boolean hasAquifer=false;
    protected double elevation=0;

    private final ParentLogger logger = new ParentLogger();

    public Polygon(List<Segment> segments, Vertex centroid, int ID) {
        this.segments = segments;
        this.centroid = centroid;
        this.ID = ID;
        this.color = Color.BLACK;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public Vertex getCentroid() {
        return centroid;
    }

    public List<Polygon> getNeighbours() {

        return neighbours;
    }

    public int getID() {
        return ID;
    }
    public Color getColor() {
        return color;
    }
    public double getElevation() { return this.elevation; }
    public double getTemperature(){ return temperature; }
    public double getPrecipitation(){ return precipitation; }
    public void setNeighbours(List<Polygon> polygons) {
        this.neighbours = polygons;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void calculateWhittakerColor() {    }

    public void affectTile(Polygon tile) {

    }

    public boolean hasAquifer() {
        return hasAquifer;
    }

    public void setAquifer(boolean hasAquifer) {
        this.hasAquifer = hasAquifer;
    }

    /**
     * This method takes will convert the polygon object to Structs.Polygon and keep the same attributes
     * @return Structs.Polygon
     */
    public Structs.Polygon convertToStruct() {
        //Convert the color and create a Structs.Property for it
        String polygonColor = ConvertColor.convert(this.color);
        Structs.Property colorProperty = Structs.Property.newBuilder().setKey("rgba_color").setValue(polygonColor).build();

        List<Integer> segmentIndexList = new ArrayList<>();

        for (Segment s: this.segments) {
            int segmentIdx = s.getID();
            segmentIndexList.add(segmentIdx);
        }

        List<Integer> neighborID = new ArrayList<>();

        for (Polygon p: this.neighbours) {
            neighborID.add(p.getID());
        }

        int centroidIdx = this.centroid.getID();

        return Structs.Polygon.newBuilder()
                .setCentroidIdx(centroidIdx)
                .addAllSegmentIdxs(segmentIndexList)
                .addAllNeighborIdxs(neighborID)
                .addProperties(colorProperty)
                .build();
    }

    public void updateElevation() {
        double elevation = 0;
        for (Segment s: this.segments) {
            elevation += s.getElevation();
        }
        this.elevation = elevation/this.segments.size();
    }
    public List<Segment> sort_base_elevation(){
        List<Segment> segments =this.getSegments();

        // One by one move boundary of unsorted sub-array
        for (int i = 0; i < segments.size(); i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < this.getSegments().size(); j++)
                if (segments.get(j).getElevation() < segments.get(min_idx).getElevation())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Segment temp = segments.get(min_idx);
            segments.set(min_idx,segments.get(i));
            segments.set(i,temp);

        }
        return segments;

    }
}


