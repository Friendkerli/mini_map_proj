package trip;

/** Initial class for the 'trip' program.
 *  @author Yang Li
 */
final class EdgInfo implements graph.Weighted {
	
	public EdgInfo(double d, String direction, String roadname) {
		_distance = d;
		_direction = direction;
		_roadname = roadname;
	}

	private String _direction, _roadname;

	private double _distance;

	double getDistance() {
		return _distance;
	}

	String getDirection() { return _direction; }

	String getRoadName() { return _roadname; }

	@Override
	public double weight() {
		return _distance;
	}
}