package jcolibri.similarity.global;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.RelationEvaluation;

/**
 * This function returns aritmethic average of a list of values.
 */
public class Average extends CBRCaseNumericSim {

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {

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
	 * @see jcolibri.similarity.SimilarityFunction#compute(jcolibri.cbrcase.Individual,
	 *      jcolibri.cbrcase.Individual)
	 */
	public double compute(Individual cbrcase1, Individual cbrcase2) {
		List list = this.compareCompoundAttributes(cbrcase1, cbrcase2);
		double aux[] = new double[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			RelationEvaluation re = (RelationEvaluation) it.next();
			aux[i] = ((Double) re.getEvaluation()).doubleValue();
		}
		return execute(aux);
	}

	/**
	 * Applies the function.
	 * 
	 * @param xs
	 *            values.
	 * @return the result of apply the similarity.
	 */
	protected double execute(int[] xs) {
		double aux = 0.0;
		for (int i = 0; i < xs.length; i++) {
			aux = aux + xs[i];
		}
		return aux / (double) xs.length;
	}

	/**
	 * Applies the function.
	 * 
	 * @param xs
	 *            values.
	 * @return the result of apply the similarity.
	 */
	protected double execute(double[] xs) {
		double aux = 0.0;
		for (int i = 0; i < xs.length; i++) {
			aux = aux + xs[i];
		}

		// ##Fixed by rasflp@gmail.com on Sep 7, 2016
		return aux / this.weightSummary;	// (double) xs.length;
	}

	/**
	 * Applies the function.
	 * 
	 * @param xs
	 *            values.
	 * @return the result of apply the similarity.
	 */
	protected double execute(Collection xs) {
		double aux = 0.0;
		for (Iterator it = xs.iterator(); it.hasNext();) {
			aux = aux + ((Double) it.next()).doubleValue();
		}
		return aux / (double) xs.size();
	}
}