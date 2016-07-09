package jcolibri.cbrcase;

/**
 * Data structure containing a relation and a object that represents its
 * evaluation
 */
public class RelationEvaluation {

	/** Name of the relation. */
	String relation;

	/** Evaluation of the relation. */
	Object evaluation;

	/**
	 * Creates a new instance
	 * 
	 * @param relation
	 *            The name of the relation
	 * @param evaluation
	 *            The value that represents the evaluation
	 */
	public RelationEvaluation(String relation, Object evaluation) {
		this.relation = relation;
		this.evaluation = evaluation;
	}

	/**
	 * Return the object associated to the evaluation
	 * 
	 * @return Object
	 */
	public Object getEvaluation() {
		return this.evaluation;
	}
}
