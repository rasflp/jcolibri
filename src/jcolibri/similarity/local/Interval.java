package jcolibri.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns the similarity of two number inside an interval.
 * sim(x,y)=1-(|x-y|/interval)
 * 
 * Now it only works with Integer values, else it returns 0.
 */
public class Interval implements SimilarityFunction {

	/** Interval parameter name. */
	public final static String INTERVAL = "INTERVAL";

	/** Interval. */
	double _interval;

	/**
	 * Constructor.
	 */
	public Interval() {
		_interval = 1;
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double compare(int x, int y) {
		return 1 - ((double) Math.abs(x - y) / _interval);
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double compare(double x, double y) {
		return 1 - (Math.abs(x - y) / _interval);
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
		
		// ##Fixed by rasflp@gmail.com on Sep 7, 2016
		if ((o1 instanceof java.lang.Integer) && (o2 instanceof java.lang.Integer)){
			Integer i1 = (Integer) o1;
			Integer i2 = (Integer) o2;
			double res = compare(i1.intValue(), i2.intValue());
			return res;
		}

		// ##Fixed by rasflp@gmail.com on Sep 7, 2016
		else {
			
			if ((o1 instanceof java.lang.Double) && (o2 instanceof java.lang.Double)){
				Double i1 = (Double) o1;
				Double i2 = (Double) o2;
				double res = compare(i1.doubleValue(), i2.doubleValue());
				return res;
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

		obj = parameters.get(Interval.INTERVAL);

		if (obj instanceof Double)
			in = (Double) obj;
		else if (obj instanceof String)
			in = new Double((String) obj);

		if (in != null)
			_interval = in.doubleValue();
	}
}
