package jcolibri.extensions.textual.similarity.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function converts the text value of an attribute into several numeric
 * values and calculates its average. Then it computes the interval difference
 * between query and case.
 * <p>
 * Example:
 * <p>
 * <p>
 * Query attribute: 2.0, 4.0 -> Average = 3.0
 * <p>
 * Case attribute: 5.0, 9.0, 4.0 -> Average = 6.0
 * <p>
 * If interval param is 9.0 computed similarity equals: 0.33
 */
public class AverageMultipleTextValues implements SimilarityFunction {

	public final static String INTERVAL = "INTERVAL";

	double _interval;

	/** Creates a new instance of MaxString */
	public AverageMultipleTextValues() {
		_interval = 1;
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
		if (queryS.length() == 0)
			return 0;
		if (caseS.length() == 0)
			return 0;

		double qV = extractStringAverage(queryS);
		double cV = extractStringAverage(caseS);
		return 1 - (Math.abs(qV - cV) / _interval);

	}

	private double extractStringAverage(String s) {
		ArrayList list = new ArrayList();
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			String num = st.nextToken();
			double dnum = 0;
			try {
				dnum = Double.parseDouble(num);
			} catch (Exception e) {
			}
			if (dnum == 0) {
				try {
					dnum = (double) Integer.parseInt(num);
				} catch (Exception e) {
				}
			}
			list.add(new Double(dnum));
		}
		double total = list.size();
		if (total == 0)
			return 0;
		double acum = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Double Dnum = (Double) iter.next();
			acum += Dnum.doubleValue();
		}
		return acum / total;
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

		obj = parameters.get(INTERVAL);

		if (obj instanceof Double)
			in = (Double) obj;
		else if (obj instanceof String)
			in = new Double((String) obj);

		if (in != null)
			_interval = in.doubleValue();
	}

}