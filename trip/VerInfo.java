package trip;

/** VerInfo represent VLABEL.
 *  @author Yang Li
 */
final class VerInfo implements graph.Weightable {

    /** Constructor use NAME X Y. */
    public VerInfo(String name, Double x, Double y) {
        _name = name;
        _x = x;
        _y = y;
    }

    /** A String matches name. */
    private String _name;

    /** double X. */
    private double _x, _y;

    /** A double w. */
    private double _w;

    /** RETURN string. */
    String getName() {
        return _name;
    }

    /** RETURN double. */
    double getX() {
        return _x;
    }

    /** RETURN string. */
    double getY() {
        return _y;
    }

    @Override
    public void setWeight(double w) {
        _w = w;
    }

    @Override
    public double weight() {
        return _w;
    }
}
