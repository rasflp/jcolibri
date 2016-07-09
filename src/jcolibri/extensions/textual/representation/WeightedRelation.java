package jcolibri.extensions.textual.representation;

/**
 * <p>
 * This class represents a weighted relation between two tokens.
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class WeightedRelation {

	Object _origin;

	Object _destination;

	double _weight;

	/**
	 * Constructor
	 * 
	 * @param origin
	 *            Origin Token
	 * @param destination
	 *            Destination Token
	 * @param weight
	 *            Relation weight (0..1)
	 */
	public WeightedRelation(Object origin, Object destination, double weight) {
		_origin = origin;
		_destination = destination;
		_weight = weight;
	}

	/**
	 * Returns the origin token.
	 * 
	 * @return
	 */
	public Object getOrigin() {
		return _origin;
	}

	/**
	 * Returns the destination token.
	 * 
	 * @return
	 */
	public Object getDestination() {
		return _destination;
	}

	/**
	 * Returns the relation weight.
	 * 
	 * @return
	 */
	public double getWeight() {
		return _weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String res = "Relation: ";
		res += _origin + " --" + _weight + "--> " + _destination;
		return res;
	}
}