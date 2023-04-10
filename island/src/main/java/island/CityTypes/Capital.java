package island.CityTypes;

import island.CityTypes.City;
import island.IOEncapsulation.Vertex;

public class Capital extends City {
    public Capital(Vertex vertex) {
        this.size = 15;
        this.vertex = vertex;
    }

    @Override
    public double getSize() {
        return this.size;
    }
}
