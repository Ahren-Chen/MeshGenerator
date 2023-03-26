package island.SoilProfiles;

public class FastSoil extends Soil{
    @Override
    protected double calculatePrecipitation(double distance) {
        return (2000/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 70);
    }
}
