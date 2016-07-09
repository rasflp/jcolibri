package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.datatypes.StringEnum;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function computes the similarity between two enum values as their cyclic
 * distance.
 * 
 * @author Juan A. Recio-García
 */
public class EnumCyclicDistance implements SimilarityFunction {

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
	 * Applies the similarity.
	 * 
	 * @param o1
	 *            StringEnum.
	 * @param o2
	 *            StringEnum or String.
	 * @return the result to apply the ismilarity.
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

		int normalDistance = Math.abs(queryOrd - caseOrd);
		int cyclicDistance = sEquery.getType().getNumPossibleValues()
				- normalDistance;
		if (normalDistance < cyclicDistance)
			return 1 - ((double) normalDistance / (double) sEquery.getType()
					.getNumPossibleValues());
		else
			return 1 - ((double) cyclicDistance / (double) sEquery.getType()
					.getNumPossibleValues());

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
