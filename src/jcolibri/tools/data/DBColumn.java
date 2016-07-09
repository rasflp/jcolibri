package jcolibri.tools.data;

/**
 * Represents the column type in a database table.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class DBColumn {

	/** Column name. */
	private String name;

	/** Column type. */
	private String type;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            column name.
	 * @param type
	 *            column type.
	 */
	public DBColumn(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Returns the column name.
	 * 
	 * @return column name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the column name.
	 * 
	 * @param name
	 *            new column name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the column type.
	 * 
	 * @return column type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the column type.
	 * 
	 * @param type
	 *            new column type.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
