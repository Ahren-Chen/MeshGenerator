package ca.mcmaster.cas.se2aa4.a2.generator.Interfaces;

import java.util.List;

public interface Converter2DTo1D<T, E> {
    List<T>  convert(List<List<T>> array);
    List<T>  convert(T[][] array);

}
