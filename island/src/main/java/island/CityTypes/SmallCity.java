package island.CityTypes;

import island.IOEncapsulation.Vertex;

public class SmallCity extends City {
    public SmallCity(Vertex vertex) {
        this.size = 5;
        this.vertex = vertex;
    }
    @Override
    public double getSize() {
        return this.size;
    }
}
