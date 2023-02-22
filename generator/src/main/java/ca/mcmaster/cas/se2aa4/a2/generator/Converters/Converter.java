package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

import java.util.List;

public interface Converter<T, E> {
    List<T>  convert(List<List<T>> array);
    List<E>  convert(T[][] array);
    E convert (T object);


}
