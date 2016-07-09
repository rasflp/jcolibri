package jcolibri.datatypes;

/**
 * Represents a variable of type StringEnumType.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class StringEnum {

	/** Type. */
	private StringEnumType type;

	/** Actual value. */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            type of the variable.
	 * @param value
	 *            value of the variable.
	 */
	public StringEnum(StringEnumType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the type of the variable.
	 * 
	 * @return the type of the variable.
	 */
	public StringEnumType getType() {
		return type;
	}

	/**
	 * Returns the actual value of the variable.
	 * 
	 * @return the actual value of the variable.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the new value of the variable.
	 * 
	 * @param value
	 *            new value of the variable.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean equals(Object arg0) {
		if (arg0 instanceof StringEnum) {
			StringEnum se2 = (StringEnum) arg0;
			if (type.equals(se2.type))
				return value.equals(se2.value);
			else
				return false;
		} else if (arg0 instanceof String) {
			return value.equals(arg0);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.value;
	}
}

