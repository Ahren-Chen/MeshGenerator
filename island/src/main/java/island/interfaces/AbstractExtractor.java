package island.interfaces;

/**
 * The AbstractExtractor interface describes an extractor that is defined by color, thickness, and centroids.
 * This class is only the interface for all objects which store a mesh and has properties color, thickness, and centroids.
 * The actual storage representation of the properties is left to the subclass.
 * @author Ahren
 * @version Feb 2023
 */
public interface AbstractExtractor<T, E> {

    /**
     * This is the abstract method to return the color after extracting the properties from a mesh
     * @return the color of the object
     */
    T color();

    /**
     * This is the abstract method to return the thickness after extracting the properties from a mesh
     * @return the thickness of the object
     */
    E thickness();

    /**
     * This is the abstract method to return whether the object is a centroid after extracting the properties from a mesh
     * @return whether the current object is a centroid
     */
    Boolean isCentroid();
}
