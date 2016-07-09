package jcolibri.cbrcore;

/**
 * A parameter of a similarity funciton.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CBRSimilarityParam {

	/** Name of the parameter. */
	private String name;

	/** Value of the parameter as string. */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the parameter.
	 * @param value
	 *            the value of the parameter as string.
	 */
	public CBRSimilarityParam(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of the parameter.
	 * 
	 * @return name of the parameter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the parameter.
	 * 
	 * @param name
	 *            name of the parameter.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the parameters as string.
	 * 
	 * @return value of the parameter as string.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the parameter as string.
	 * 
	 * @param value
	 *            value of the parameter as string.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns a copy of this instance.
	 * 
	 * @return a copy of this instance.
	 */
	public Object clone() {
		return new CBRSimilarityParam(name, value);
	}
}
