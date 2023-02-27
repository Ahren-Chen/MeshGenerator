package ca.mcmaster.cas.se2aa4.a2.generator.Converters;

public interface ObjectConverter<T, E> {
    T convert (E object);
}
