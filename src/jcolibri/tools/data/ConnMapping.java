package jcolibri.tools.data;

/**
 * Mapping between a simple attribute of a case and a column of the media
 * storage.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class ConnMapping {

	/** Simple attribute name (path). */
	private String attribute;

	/** Column name. */
	private String column;

	/**
	 * Constructor.
	 * 
	 * @param attribute
	 *            simple attribute name (path).
	 * @param column
	 *            column name.
	 */
	public ConnMapping(String attribute, String column) {
		super();
		this.attribute = attribute;
		this.column = column;
	}

	/**
	 * Returns the simple attribute name (path).
	 * 
	 * @return simple attribute name (path).
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Sets the simple attribute name (path).
	 * 
	 * @param attribute
	 *            new simple attribute name.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns the column name.
	 * 
	 * @return column name.
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * Sets the column name.
	 * 
	 * @param column
	 *            column name.
	 */
	public void setColumn(String column) {
		this.column = column;
	}
}
