package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns 1 if both String are the same despite case letters, 0
 * in the other case
 */
public class EqualsStringIgnoreCase implements SimilarityFunction {

	/**
	 * Constructor.
	 */
	public EqualsStringIgnoreCase() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#compute(jcolibri.cbrcase.Individual,
	 *      jcolibri.cbrcase.Individual)
	 */
	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(), o2.getValue());
	}

	/**
	 * Applies the similarity funciton.
	 * 
	 * @param s
	 *            String
	 * @param t
	 *            String
	 * @return the result of apply the similarity function.
	 */
	private double compute(Object s, Object t) {
		if ((s == null) || (t == null))
			return 0;
		if ((!(s instanceof java.lang.String))
				|| (!(t instanceof java.lang.String)))
			return 0;

		return (((String) s).equalsIgnoreCase(((String) t)) ? 1 : 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#getParametersInfo()
	 */
	public HashMap getParametersInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {
	}

}