package trip;

/** Distance give h.
 *  @author Yang Li
 */
final class Distance implements graph.Distancer {

    @Override
    public double dist(Object v0, Object v1) {
        double xV0 = ((VerInfo) v0).getX();
        double xV1 = ((VerInfo) v1).getX();
        double yV0 = ((VerInfo) v0).getY();
        double yV1 = ((VerInfo) v1).getY();
        return Math.sqrt((xV0 - xV1) * (xV0 - xV1) + (yV0 - yV1) * (yV0 - yV1));
    }

}
