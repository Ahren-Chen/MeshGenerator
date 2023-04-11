package island.CityTypes;

import island.IOEncapsulation.Vertex;

public class MediumCity extends City {
    public MediumCity(Vertex vertex) {
        this.size = 7;
        this.vertex = vertex;
    }
    @Override
    public double getSize() {
        return this.size;
    }
}
