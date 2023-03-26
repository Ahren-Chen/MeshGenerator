package island.interfaces;

/**
 * The ConvertToStruct interface describes a converter where it can turn
 * any object into something accepted by the IO in Struct mode.
 * @author Ahren, Mike, Simon
 * @version Feb 2023
 */
public interface ConvertToStruct<T> {
    /**
     * This abstract method returns the Struct version of the object taken from the generator mesh
     * @return T
     */
    T convertToStruct();
}
