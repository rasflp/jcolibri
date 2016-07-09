package jcolibri.cbrcore;

import java.io.Serializable;

/**
 * Restriction of a parameter of a method.
 * 
 * @author Juan José Bello
 */
public class MethodCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Name of the description. */
	String name;

	/** Max number of ocurrences. */
	String maxOcurrences;

	/** Min number of ocurrences. */
	String minOcurrences;

	/** Parameter restricted. */
	MethodParameter parameter;

	/**
	 * Constructor.
	 * 
	 * @param parameter
	 *            parameter restricted.
	 */
	public MethodCondition(MethodParameter parameter) {
		super();
		this.parameter = parameter;
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the condition.
	 * @param maxOcurrences
	 *            max ocurrences of the parameter.
	 * @param minOcurrences
	 *            min ocurrences of the parameter.
	 */
	public MethodCondition(String name, String maxOcurrences,
			String minOcurrences) {
		super();
		this.name = name;
		this.maxOcurrences = maxOcurrences;
		this.minOcurrences = minOcurrences;
	}

	/**
	 * Returns the max number of ocurrences of the parameter.
	 * 
	 * @return the max number of ocurrences of the parameter.
	 */
	public String getMaxOcurrences() {
		return maxOcurrences;
	}

	/**
	 * Sets the max number of ocurrences of the parameter
	 * 
	 * @param maxOcurrences
	 *            max number of ocurrences of the parameter.
	 */
	public void setMaxOcurrences(String maxOcurrences) {
		this.maxOcurrences = maxOcurrences;
	}

	/**
	 * Returns the min number of ocurrences of the parameter.
	 * 
	 * @return the min number of ocurrences of the parameter.
	 */
	public String getMinOcurrences() {
		return minOcurrences;
	}

	/**
	 * Returns the min number of ocurrences of the parameter
	 * 
	 * @return min number of ocurrences of the parameter.
	 */
	public void setMinOcurrences(String minOcurrences) {
		this.minOcurrences = minOcurrences;
	}

	/**
	 * Returns the name of the condition.
	 * 
	 * @return the name of the condition.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the condition.
	 * 
	 * @param name
	 *            new name of the condition.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the parameter restricted.
	 * 
	 * @return the parameter restricted.
	 */
	public MethodParameter getParameter() {
		return parameter;
	}

	/**
	 * Sets the parameter restricted.
	 * 
	 * @param parameter
	 *            the parameter restricted.
	 */
	public void setParameter(MethodParameter parameter) {
		this.parameter = parameter;
	}
}
