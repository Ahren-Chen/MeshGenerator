package ca.mcmaster.cas.se2aa4.a2.generator.Interfaces;

import java.util.List;

/**
 * The Converter2DTo1D interface describes a converter where it can turn 2D arrays or {@code List<List<>>} into a 1D {@code List}
 * @author Ahren, Mike, Simon
 * @version Feb 2023
 */
public interface Converter2DTo1D<T, E> {
    /**
     * This abstract method converts a 2D {@code List} into 1D
     * @param array A {@code List<List<>>} to convert
     * @return {@code List<T>} a 1D {@code List}
     */
    List<E>  convert(List<List<T>> array);

    /**
     * This abstract method converts a 2D array into 1D
     * @param array A {@code T[][]} to convert
     * @return {@code List<T>} a 1D {@code List}
     */
    List<E>  convert(T[][] array);

}
