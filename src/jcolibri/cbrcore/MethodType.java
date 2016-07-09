package jcolibri.cbrcore;

import java.io.Serializable;

/**
 * Enumerate to specify the kind of method
 */
public class MethodType implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Type decomposition as string. */
	private final static String STR_DECOMPOSITION = "Decomposition";

	/** Type decomposition as integer. */
	private final static int _DECOMPOSITION = 1;

	/** Type decomposition. */
	public final static MethodType DECOMPOSITION = new MethodType(
			_DECOMPOSITION);

	/** Type resolution as string. */
	private final static String STR_RESOLUTION = "Resolution";

	/** Type resolution as integer. */
	private final static int _RESOLUTION = 2;

	/** Type resolution. */
	public final static MethodType RESOLUTION = new MethodType(_RESOLUTION);

	/** Value of the method type. */
	private int value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            type as integer.
	 */
	private MethodType(int value) {
		this.value = value;
	}

	/**
	 * Constructor.
	 * 
	 * @param str
	 *            type as string.
	 */
	public MethodType(String str) {
		if (str.equalsIgnoreCase(STR_DECOMPOSITION))
			value = _DECOMPOSITION;
		else if (str.equalsIgnoreCase(STR_RESOLUTION))
			value = _RESOLUTION;
		else
			value = -1;
	}

	/**
	 * Returns the value as integer.
	 * 
	 * @return value as integer.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns if 2 MethodType are equals.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof MethodType)
			return equals((MethodType) obj);
		else
			return false;
	}

	/**
	 * Returns if 2 MethodType are equals.
	 */
	public boolean equals(MethodType data) {
		return value == data.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		switch (value) {
		case _DECOMPOSITION:
			return STR_DECOMPOSITION;
		case _RESOLUTION:
			return STR_RESOLUTION;
		default:
			return "NOT_VALID_VALUE";
		}
	}

}
