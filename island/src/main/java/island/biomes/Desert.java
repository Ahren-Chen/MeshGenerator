package island.biomes;

import island.interfaces.Biomes;

public class Desert implements Biomes {

    public double getPrecipitation() {
        return 30;
    }

    public double getTemp() {
        return 25;
    }
}
