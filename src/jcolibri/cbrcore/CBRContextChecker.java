package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jcolibri.cbrcore.exception.IncompatibleMethodException;
import jcolibri.util.CBRLogger;

/**
 * Instance of this class will track the changed on the context because of a CBR
 * application configuration. Also will check that new methods are compatible
 * with context situation at every moment.
 * 
 * @author Juan José Bello
 */
public class CBRContextChecker implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Actual conditions of the context. */
	HashMap<String, Object> actualConditions = new HashMap<String, Object>();

	/**
	 * Refresh the conditions od the context as necesary when a new method is
	 * added.
	 * 
	 * @param method
	 *            new method to add.
	 * @throws IncompatibleMethodException
	 */
	public void addMethod(CBRMethod method) throws IncompatibleMethodException {
		if (!isMethodApplicable(method))
			throw new IncompatibleMethodException();
		else {
			addConditions(method.getPostConditionsInfo());
		}
	}

	/**
	 * Returns if a method is applicable in the actual context.
	 * 
	 * @param method
	 *            method to check.
	 * @return true if is applicable, false otherwise.
	 */
	public boolean isMethodApplicable(CBRMethod method) {
		return checkPreConditions(method.getPreConditionsInfo());
	}

	/**
	 * Adds new conditions to the actual conditions of the context.
	 * 
	 * @param conditions
	 *            new conditions.
	 */
	private void addConditions(HashMap<String, Object> conditions) {
		if (conditions != null)
			actualConditions.putAll(conditions);
	}

	/**
	 * Checks if the preconditions of a method are compatible with the actual
	 * state of the context.
	 * 
	 * @param conditions
	 *            preconditions of the method.
	 * @return true if the method is applicable.
	 */
	private boolean checkPreConditions(HashMap<String, Object> conditions) {
		boolean aux = false;
		if (conditions == null || conditions.size() == 0)
			return true;
		Set actualValues = actualConditions.keySet();
		for (Iterator it = conditions.keySet().iterator(); it.hasNext();) {
			String name = (String) it.next();
			if (!actualValues.contains(name)) {
				CBRLogger.log(this.getClass(), CBRLogger.ERROR,
						"Method general precondition:" + name
								+ " does not exist in current context");
				return false;
			} else {
				Object value = conditions.get(name);
				Object actualValue = actualConditions.get(name);
				if (value instanceof MethodCondition
						&& actualValue instanceof MethodCondition) {
					aux = isInRange(((MethodCondition) value)
							.getMaxOcurrences(), ((MethodCondition) value)
							.getMinOcurrences(),
							((MethodCondition) actualValue).getMaxOcurrences(),
							((MethodCondition) actualValue).getMinOcurrences());
					if (!aux) {
						CBRLogger
								.log(
										this.getClass(),
										CBRLogger.ERROR,
										"Method general precondition (CBRTerm:"
												+ name
												+ ") instances are not guarantee in current context");
					}
					return aux;
				} else if (value instanceof MethodParameter
						&& actualValue instanceof MethodParameter) {
					MethodParameter aux1 = (MethodParameter) value;
					MethodParameter aux2 = (MethodParameter) actualValue;
					aux = aux1.getType().equals(aux2.getType());
					if (!aux) {
						CBRLogger
								.log(
										this.getClass(),
										CBRLogger.ERROR,
										"Method precondition (parameter DataType:"
												+ name
												+ ") is not guarantee in current context");
						return aux;
					}
					if (aux1.getType().equals(
							DataTypesRegistry.getCBRTERMDataType())) {
						aux = true;
						// aux1.getType().getSubType().equals(
						// aux2.getType().getSubType());
						if (!aux) {
							CBRLogger
									.log(
											this.getClass(),
											CBRLogger.ERROR,
											"Method precondition (parameter CBRTerm:"
													+ name
													+ ") is not guarantee in current context");
						}
						return aux;
					} else
						return true;
				}
			}
		}
		return true;
	}

	/**
	 * Parse a int from a string.
	 * 
	 * @param x
	 *            string
	 * @return if x == "undefined" Integer.MAX_VALUE, the int value of the
	 *         string otherwise.
	 */
	private int parseInt(String x) {
		if (x.equalsIgnoreCase("undefined"))
			return Integer.MAX_VALUE;
		else
			return new Integer(x).intValue();
	}

	/**
	 * Returns if (yi, yo) is a subrange of (xi, xo), where xi, xo, yi and yo
	 * are integers represented as strings.
	 * 
	 * @param xo
	 *            first integer of (xi, x0) interval.
	 * @param xi
	 *            second integer of (xi, x0) interval.
	 * @param yo
	 *            first integer of (yi, y0) interval.
	 * @param yi
	 *            second integer of (yi, y0) interval.
	 * @return true if xi <= y1 and xo >= yo, and false in otherwise.
	 */
	private boolean isInRange(String xo, String xi, String yo, String yi) {
		int xo_int = parseInt(xo);
		int xi_int = parseInt(xi);
		int yo_int = parseInt(yo);
		int yi_int = parseInt(yi);

		if (xi_int > yi_int)
			return false;
		if (xo_int < yo_int)
			return false;
		return true;

	}

}
