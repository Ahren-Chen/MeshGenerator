package island.CityTypes;

import island.IOEncapsulation.Vertex;

public abstract class City {
    protected double size;
    protected Vertex vertex;

    public abstract double getSize();

    public Vertex getVertex() {
        return vertex;
    }
}
