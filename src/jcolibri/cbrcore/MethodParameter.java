package jcolibri.cbrcore;

import java.io.Serializable;

/**
 * Parameter object that can declared for any method.
 */
public class MethodParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter. */
	String name;

	/** Description of the parameter. */
	String description;

	/** Value of the parameter. */
	Object value;

	/** Type of the parameter. */
	DataType type;

	/**
	 * Creates a new parameter
	 * 
	 * @param name
	 *            Name of the parameter
	 * @param description
	 *            Description of the parameter
	 * @param type
	 *            DataType for the parameter
	 */
	public MethodParameter(String name, String description, DataType type) {
		this.name = name;
		this.description = description;
		this.type = type;
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
	 *            new name of the parameter.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the parameter.
	 * 
	 * @return the descripiton of the parameter.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the parameter.
	 * 
	 * @param description
	 *            the description of the parameter.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the value of the parameter.
	 * 
	 * @param value
	 *            new value of the parameter.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns the value of the parameter.
	 * 
	 * @return the value of the parameter.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the type of the parameter.
	 * 
	 * @return type of the parameter.
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * Sets the type of the parameter.
	 * 
	 * @param type
	 *            new type of the parameter.
	 */
	public void setType(DataType type) {
		this.type = type;
	}

}
