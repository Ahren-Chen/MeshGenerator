package island.SoilProfiles;

public class MediumSoil extends Soil{
    @Override
    protected double calculatePrecipitation(double distance) {
        return (600/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 120);
    }
}
