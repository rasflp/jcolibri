package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.datatypes.StringEnum;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns the similarity of two enum values as the their distance
 * sim(x,y)=|ord(x) - ord(y)|
 * 
 * @author Juan A. Recio-García
 */
public class EnumDistance implements SimilarityFunction {

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
	 * @param o1
	 *            StringEnum or String
	 * @param o2
	 *            StringEnum or String
	 * @return the result of apply the similarity function.
	 */
	private double compute(Object o1, Object o2) {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof StringEnum))
			return 0;
		StringEnum sEquery = (StringEnum) o1;
		String queryValue = sEquery.getValue();
		if (queryValue == null)
			return 0;

		String caseValue;
		if (o2 instanceof StringEnum) {
			StringEnum sEcase = (StringEnum) o2;
			caseValue = sEcase.getValue();
			if (caseValue == null)
				return 0;
		} else if (o2 instanceof String) {
			caseValue = (String) o2;
		} else
			return 0;

		int queryOrd = sEquery.getType().getOrd(queryValue);
		int caseOrd = sEquery.getType().getOrd(caseValue);
		if (queryOrd == -1)
			return 0;
		if (caseOrd == -1)
			return 0;

		return 1 - ((double) Math.abs(queryOrd - caseOrd) / (double) sEquery
				.getType().getNumPossibleValues());

	}

	/** Function parameters. */
	private HashMap params;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {
		params = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#getParametersInfo()
	 */
	public HashMap getParametersInfo() {
		return params;
	}

}
