package jcolibri.tools.data;

import java.util.Vector;

/**
 * Represents the structure of a table in a database.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class DBTable {

	/** Columns of the table. */
	private Vector<DBColumn> dbColumns;

	/**
	 * Constructor.
	 */
	public DBTable() {
		dbColumns = new Vector<DBColumn>();
	}

	/**
	 * Adds a new column.
	 * 
	 * @param dbColumn
	 *            column.
	 */
	public void add(DBColumn dbColumn) {
		dbColumns.add(dbColumn);
	}

	/**
	 * Returns the number of columns.
	 * 
	 * @return number of columns.
	 */
	public int getCount() {
		return dbColumns.size();
	}

	/**
	 * Returns a column by index.
	 * 
	 * @param idx
	 *            index.
	 * @return DBColumn.
	 */
	public DBColumn get(int idx) {
		return (DBColumn) dbColumns.get(idx);
	}

	/**
	 * Returns the column by name.
	 * 
	 * @param name
	 *            name of the column.
	 * @return DBColumn.
	 */
	public DBColumn getByName(String name) {
		DBColumn dbColumn;

		for (int i = 0; i < dbColumns.size(); i++) {
			dbColumn = (DBColumn) dbColumns.elementAt(i);
			if (dbColumn.getName().equals(name))
				return dbColumn;
		}
		return null;
	}

	/**
	 * Sets the column of index idx.
	 * 
	 * @param idx
	 *            index.
	 * @param dbColumn
	 *            new column.
	 */
	public void set(int idx, DBColumn dbColumn) {
		dbColumns.setElementAt(dbColumn, idx);
	}

	/**
	 * Removes a column by index.
	 * 
	 * @param idx
	 *            index.
	 */
	public void remove(int idx) {
		dbColumns.removeElementAt(idx);
	}
}
