package ca.mcmaster.cas.se2aa4.a2.visualizer;

import java.awt.*;

abstract public class Extractor<T> {
    public abstract T color();
    public abstract T thickness();
    public abstract T centroid();
}
