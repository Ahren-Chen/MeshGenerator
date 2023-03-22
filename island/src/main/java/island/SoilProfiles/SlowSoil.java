package island.SoilProfiles;

public class SlowSoil extends Soil{

    @Override
    protected double calculatePrecipitation(double distance) {
        //175 is fast
        return (125/distance);
    }
}
