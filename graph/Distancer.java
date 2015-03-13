package graph;

/** An object representing a function that computes the distance between
 *  two objects of type TYPE. */

/** Assorted graph algorithms.
 *  @author Yang Li
 */
public interface Distancer<Type> {

    /** Returns the distance from V0 to V1. */
    double dist(Type v0, Type v1);

}
