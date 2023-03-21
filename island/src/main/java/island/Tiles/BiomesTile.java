package island.Tiles;

import island.IOEncapsulation.Polygon;
import island.Interfaces.Tile;

import java.awt.*;

public class BiomesTile extends Polygon implements Tile<Polygon> {

    private double temperature;
    private double Humidity;
    private double precipitation;
    private double elevation;
    public BiomesTile(Polygon polygon) {
        super(polygon.getSegments(), polygon.getCentroid(), polygon.getID());
        super.setNeighbours(polygon.getNeighbours());
        super.setColor(Color.WHITE);
        this.temperature = 20;
        this.precipitation = 200;
    }

    public void affectTile(Polygon polygon) {
        setHumidity();
    }

    public double getHumidity(){
        return Humidity;
    }
    public double getTemperature(){
        return temperature;
    }

    public double getElevation() {
        return elevation;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    @Override
    public void calculateWhittakerColor() {
        Color tropicalRainForest = Color.GREEN;
        Color tropicalSeasonalForest = new Color(76, 187, 23);
        Color temperateRainForest = new Color(80, 200, 120);
        Color temperateDeciduousForest = new Color(34, 139, 34);
        Color temperateGrasslandAndDesert = Color.YELLOW;
        Color taiga = new Color(21, 71, 52);
        Color tundra = new Color(51, 204, 255);
        Color desert = Color.ORANGE;

        if (this.temperature >= 20 && this.precipitation >= 250) {
            this.setColor(tropicalRainForest);
        }

        else if (this.temperature >= 20 && this.precipitation >= 50) {
            this.setColor(tropicalSeasonalForest);
        }

        else if (this.temperature >= 5 && this.precipitation >= 200) {
            this.setColor(temperateRainForest);
        }

        else if (this.temperature >= 5 && this.precipitation >= 90) {
            this.setColor(temperateDeciduousForest);
        }

        else if (this.temperature >= -5 && this.precipitation >= 50) {
            this.setColor(taiga);
        }

        else if (this.temperature < -5) {
            this.setColor(tundra);
        }

        else if (this.precipitation >= 50) {
            this.setColor(temperateGrasslandAndDesert);
        }

        else {
            this.setColor(desert);
        }
    }
    public boolean hasAquifer() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setHumidity(){
        double precipitation = this.getPrecipitation();
        double temperature = this.getTemperature();
        double humidity = 0;
        if (temperature >= 20) {
            humidity = precipitation * 0.8;
        }
        else if (temperature >= 5) {
            humidity = precipitation * 0.6;
        }
        else if (temperature >= -5) {
            humidity = precipitation * 0.4;
        }
        else {
            humidity = precipitation * 0.2;
        }
        this.Humidity=humidity;
    }
}
