package island.soilProfiles;

public class MediumSoil extends Soil{
    @Override
    protected double calculatePrecipitation(double distance) {
        return (1500/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 120);
    }
}
