package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns a similarity value depending of the biggest substring
 * that belong to both strings.
 */
public class MaxString implements SimilarityFunction {

	/**
	 * Constructor.
	 */
	public MaxString() {
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
	 * Applies the similarity function.
	 * 
	 * @param s
	 *            String.
	 * @param t
	 *            String.
	 * @return result of apply the similarity funciton.
	 */
	private double compute(Object s, Object t) {
		if ((s == null) || (t == null))
			return 0;
		if ((!(s instanceof java.lang.String))
				&& (!(t instanceof java.lang.String)))
			return 0;

		String news = (String) s;
		String newt = (String) t;
		if (news.equals(newt))
			return 1.0;
		else
			return ((double) MaxSubString(news, newt) / (double) Math.max(news
					.length(), newt.length()));
	}

	/**
	 * Returns the length of the biggest substring that belong to both strings.
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	private int MaxSubString(String s, String t) {
		String shorter = (s.length() > t.length()) ? t : s;
		String longer = (shorter.equals(s)) ? t : s;
		int best = 0;
		for (int i = 0; i < shorter.length(); i++) {
			for (int j = shorter.length(); j > i; j--) {
				if (longer.indexOf(shorter.substring(i, j)) != -1)
					best = Math.max(best, j - i);
			}
		}
		return best;
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