package trip;

/** Initial class for the 'trip' program.
 *  @author Yang Li
 */
final class EdgInfo implements graph.Weighted {

    /** Constructor use D DIRECTION ROADNAME. */
    public EdgInfo(double d, String direction, String roadname) {
        _distance = d;
        _direction = direction;
        _roadname = roadname;
    }

    /** A String matches roadname. */
    private String _direction, _roadname;

    /** A DOUBLE matches DISTANCE. */
    private double _distance;

    /** RETURN double. */
    double getDistance() {
        return _distance;
    }

    /** RETURN string. */
    String getDirection() { return _direction; }

    /** RETURN string. */
    String getRoadName() { return _roadname; }

    @Override
    public double weight() {
        return _distance;
    }
}
