package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns 1 if the difference between two numbers is less than a
 * threshold, o in the other case.
 */
public class Threshold implements SimilarityFunction {

	/** Threshold parameter name. */
	public final static String THRESHOLD = "THRESHOLD";

	/** Threshold. */
	double _threshold;

	/**
	 * Constructor.
	 */
	public Threshold() {
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int compare(int x, int y) {
		if (Math.abs(x - y) > _threshold)
			return 0;
		else
			return 1;
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	// ##Implemented by rasflp@gmail.com on Sep 7, 2016
	public int compare(double x, double y) {
		if (Math.abs(x - y) > _threshold)
			return 0;
		else
			return 1;
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
	 * @param o1
	 *            Integer
	 * @param o2
	 *            Integer
	 * @return result of apply the similarity function.
	 */
	private double compute(Object o1, Object o2) {
		
		if ((o1 == null) || (o2 == null))
			return 0;
		
		// ##Implemented by rasflp@gmail.com on Sep 7, 2016
		if ((o1 instanceof java.lang.Integer) && (o2 instanceof java.lang.Integer)){

			Integer i1 = (Integer) o1;
			Integer i2 = (Integer) o2;
			
			return (double) compare(i1.intValue(), i2.intValue());
		}
		else{
			
			// ##Implemented by rasflp@gmail.com on Sep 7, 2016
			if ((o1 instanceof java.lang.Double) && (o2 instanceof java.lang.Double)){

				Double i1 = (Double) o1;
				Double i2 = (Double) o2;
				
				return (double) compare(i1, i2);
			}
			else
				return 0;
		}
		
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
		Double in = null;
		Object obj;

		obj = parameters.get(THRESHOLD);

		if (obj instanceof Double)
			in = (Double) obj;
		else if (obj instanceof String)
			in = new Double((String) obj);

		if (in != null)
			_threshold = in.doubleValue();
	}
}