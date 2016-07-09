package jcolibri.cbrcase;

import jcolibri.cbrcore.CBRTerm;

/**
 * Data structure containing a case and a object that represents its evaluation
 */
public class CaseEvaluation implements Comparable, CBRTerm {

	/** Case to evaluate. */
	CBRCase cbrcase;

	/** Evualtion of the case. */
	Object evaluation;

	/**
	 * Create a new CaseEvaluation instance.
	 * 
	 * @param cbrcase
	 *            The CBRCase that has been evaluated
	 * @param evaluation
	 *            The evaluation object for the case, it must implement the
	 *            java.util.Comparable interface.
	 */
	public CaseEvaluation(CBRCase cbrcase, Comparable evaluation) {
		this.cbrcase = cbrcase;
		this.evaluation = evaluation;
	}

	/**
	 * Returns The evaluation object associated to this CaseEvaluation instance
	 * 
	 * @return The evaluation object associated to this CaseEvaluation instance
	 */
	public Object getEvaluation() {
		return this.evaluation;
	}

	/**
	 * Returns The CBRCase associated to this CaseEvaluation instance
	 * 
	 * @return The CBRCase associated to this CaseEvaluation instance
	 */
	public CBRCase getCase() {
		return cbrcase;
	}

	/**
	 * Method needed to establish an order between two different instances of
	 * CBRCaseEvulation. Note that the comparation is done between the
	 * evaluation objects, this method will never return that two instances of
	 * CaseEvaluation are equal. This is done to make possible the insertion of
	 * two equaly evaluated instances in the same ordered structure.
	 * 
	 * @param Object
	 *            Object against this instance will be compared with
	 * @return 1 if both instance have an eual evaluation and the result of the
	 *         invokation of the compare method between the evaluation objects
	 *         otherwiseare.
	 */
	@SuppressWarnings("unchecked")
    public int compareTo(Object o) {
		Comparable thiscom = (Comparable) evaluation;
		Comparable othercom = (Comparable) ((CaseEvaluation) o).getEvaluation();
		int result = -thiscom.compareTo(othercom);
		return (result == 0 ? 1 : result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(cbrcase.toString());
		sb.append(" with value:");
		sb.append(evaluation.toString());
		return sb.toString();
	}
}
