package island.SoilProfiles;

public class MediumSoil extends Soil{
    @Override
    protected double calculatePrecipitation(double distance) {
        if(distance == 0) {
            return 300;
        }
        return (1500/distance);
    }

    @Override
    protected boolean withinBound(double distance) {
        return (distance <= 120);
    }
}
