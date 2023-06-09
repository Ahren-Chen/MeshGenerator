package island.IOEncapsulation;

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Converters.ConvertColor;
import island.Interfaces.ConvertToStruct;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements ConvertToStruct<Structs.Polygon>{
    private final List<Segment> segments;
    private Vertex centroid;
    private List<Polygon> neighbours;
    private final int ID;
    private Color color;
    protected double temperature = 0;
    protected double precipitation = 0;
    private boolean hasAquifer=false;
    private double elevation=0;

    private final ParentLogger logger = new ParentLogger();

    private boolean nextToOcean = false;

    private boolean isWater = false;

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
    public boolean getIsWater(){return isWater;}

    public int getID() {
        return ID;
    }
    public Color getColor() {
        return color;
    }
    public double getElevation() { return this.elevation; }
    public double getTemperature(){ return temperature; }
    public double getPrecipitation(){ return precipitation; }
    public boolean getNextToOcean(){return nextToOcean; }

    public boolean getHasAquifer(){return this.hasAquifer;}
    public void setNeighbours(List<Polygon> polygons) {
        this.neighbours = polygons;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public void setIsWater(boolean isWater){this.isWater = isWater;}

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public void setNextToOcean(boolean ifNextToOcean){this.nextToOcean = ifNextToOcean;}


    public void calculateWhittakerColor() {  }

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
        temperature=40-elevation*0.045;
        setTemperature(temperature);
    }

    public List<Polygon> sort_base_elevation() {
        List<Polygon> neighbors = this.getNeighbours();

        // One by one move boundary of unsorted sub-array
        for (int i = 0; i < neighbors.size(); i++) {
            // Find the maximum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < neighbors.size(); j++) {
                if (neighbors.get(j).getElevation() < neighbors.get( min_idx).getElevation())
                    min_idx = j;

                // Swap the found maximum element with the first element
                Polygon temp = neighbors.get( min_idx);
                neighbors.set( min_idx, neighbors.get(i));
                neighbors.set(i, temp);

            }

        }
        return neighbors;
    }
    public void affectTemperatue(){
        temperature = temperature - elevation*0.065;
    }
}


