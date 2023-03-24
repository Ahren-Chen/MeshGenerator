package island.SoilProfiles;

public class SlowSoil extends Soil{

    @Override
    protected double calculatePrecipitation(double distance) {
        return (500/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 170);
    }
}
