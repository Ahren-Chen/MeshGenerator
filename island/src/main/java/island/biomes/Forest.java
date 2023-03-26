package island.biomes;

import island.interfaces.Biomes;

public class Forest implements Biomes {
    public double getPrecipitation() {
        return 200;
    }

    public double getTemp() {
        return 15;
    }
}
