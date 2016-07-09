package jcolibri.extensions.textual.similarity.local;

import java.util.HashMap;
import java.util.StringTokenizer;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns a similarity value depending on the tokens (words) that
 * appear in the query attribute and also appear in the case attribute.
 */
public class TokensContained implements SimilarityFunction {

	/** Creates a new instance of MaxString */
	public TokensContained() {
	}

	/** Applies the similarity function */
	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(), o2.getValue());
	}

	private double compute(Object s, Object t) {
		if ((s == null) || (t == null))
			return 0;
		if ((!(s instanceof java.lang.String))
				|| (!(t instanceof java.lang.String)))
			return 0;

		String queryS = (String) s;
		String caseS = (String) t;
		StringTokenizer ct = new StringTokenizer(caseS);
		int totalTokens = ct.countTokens();
		if (totalTokens == 0)
			return 0;
		StringTokenizer qt = new StringTokenizer(queryS);
		int foundTokens = 0;
		while (qt.hasMoreTokens()) {
			String tok = qt.nextToken();
			if (caseS.contains(tok))
				foundTokens++;
		}
		return ((double) foundTokens / (double) totalTokens);
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