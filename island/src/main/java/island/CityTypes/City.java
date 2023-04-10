package island.CityTypes;

import island.IOEncapsulation.Vertex;

public abstract class City {
    protected double size;
    protected Vertex vertex;

    public double getSize() {
        return size;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
