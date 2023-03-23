package island.SoilProfiles;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Vertex;

import java.util.List;

abstract public class Soil {
    public void calculateAbsorption(Polygon tile, List<Polygon> lakeMap) {
        Vertex mainCentroid = tile.getCentroid();
        double precipitation = tile.getPrecipitation();

        double addedPrecipitation = 0;
        for (Polygon lakeTile : lakeMap) {
            Vertex lakeCentroid = lakeTile.getCentroid();

            double distance = calculateDistance(mainCentroid, lakeCentroid);

            if (withinBound(distance)) {
                addedPrecipitation += calculatePrecipitation(distance);
            }

        }

        tile.setPrecipitation(precipitation + addedPrecipitation);
    }

    private double calculateDistance(Vertex v1, Vertex v2) {
        double x1 = v1.getX();
        double y1 = v1.getY();

        double x2 = v2.getX();
        double y2 = v2.getY();

        double xDiff = x2 - x1;
        double yDiff = y2 - y1;

        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
    }

    protected abstract boolean withinBound(double distance);

    protected abstract double calculatePrecipitation(double distance);
}
