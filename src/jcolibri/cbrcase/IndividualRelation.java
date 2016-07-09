package jcolibri.cbrcase;

/**
 * Represents the relation concept between two individuals
 */
public class IndividualRelation {

	/** Description. */
	String relationDescription;

	/** Target individual of the relation. */
	Individual target;

	/** Weight associated to the relation. */
	double weight = 1.0;

	/**
	 * Creates a new relation.
	 * 
	 * @param description
	 *            Name of the relation
	 * @param target
	 *            Individual object tarjet of this relation
	 */
	public IndividualRelation(String description, Individual target) {
		this.relationDescription = description;
		this.target = target;
	}

	/**
	 * Returns the name of this Relation
	 * 
	 * @return the name of this Relation
	 */
	public String getDescription() {
		return relationDescription;
	}

	/**
	 * Returns the tarjet individual of this relation
	 * 
	 * @return the tarjet individual of this relation
	 */
	public Individual getTarget() {
		return target;
	}

	/**
	 * Returns the current importance value associated to this relation
	 * 
	 * @return the current importance value associated to this relation
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the current importance value associated to this relation
	 * 
	 * @param value
	 */
	public void setWeight(double value) {
		this.weight = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(relationDescription);
		sb.append(": ");
		sb.append(target.toString());
		return sb.toString();
	}
}