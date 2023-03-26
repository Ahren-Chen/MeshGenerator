package island.soilProfiles;

public class SlowSoil extends Soil{

    @Override
    protected double calculatePrecipitation(double distance) {
        return (1000/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 170);
    }
}
