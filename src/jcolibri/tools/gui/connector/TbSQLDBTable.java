package jcolibri.tools.gui.connector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import jcolibri.tools.data.DBColumn;
import jcolibri.tools.data.DBTable;

/**
 * Visual table that displays the structure of a database table.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbSQLDBTable extends JTable {

	private static final long serialVersionUID = 1L;

	private TableModelDBTable tableModel;

	/**
	 * Constructor.
	 */
	public TbSQLDBTable() {
		super();
		tableModel = new TableModelDBTable();
		setModel(tableModel);

		TableColumn tbColumn;
		JComboBox cbTypes;
		cbTypes = new JComboBox();
		tbColumn = getColumnModel().getColumn(1);
		tbColumn.setCellEditor(new DefaultCellEditor(cbTypes));
		cbTypes.addItem("BigDecimal");
		cbTypes.addItem("Boolean");
		cbTypes.addItem("Byte");
		cbTypes.addItem("Date");
		cbTypes.addItem("Double");
		cbTypes.addItem("Float");
		cbTypes.addItem("Integer");
		cbTypes.addItem("Long");
		cbTypes.addItem("Object");
		cbTypes.addItem("Short");
		cbTypes.addItem("String");
		cbTypes.addItem("URL");
        cbTypes.addItem("Concept"); 
	}

	/**
	 * Sets the panel of connector mappings. This panel will be notificated if
	 * the table structure of the database changes.
	 * 
	 * @param pnlConnMappings
	 */
	public void setPnlConnMappings(PnlSQLMappings pnlConnMappings) {
		tableModel.setPnlConnMappings(pnlConnMappings);
	}

	/**
	 * Adds a new DBColumn.
	 */
	public void addNewDBColumn() {
		addDBColumn(new DBColumn("", ""));
	}

	/**
	 * Adds a DBColumn.
	 * 
	 * @param column
	 */
	public void addDBColumn(DBColumn column) {
		tableModel.addDBColumn(column);
	}

	/**
	 * Removes the selected DBColumn of the table. s
	 */
	public void removeSelDBColumn() {
		int row;

		row = getSelectedRow();
		if (row >= 0)
			tableModel.removeDBColumn(row);
	}

	/**
	 * Returns the actual structure of the database table.
	 * 
	 * @return
	 */
	public DBTable getDBTable() {
		return tableModel.getDBTable();
	}

	/**
	 * Clears all DBColumns.
	 * 
	 */
	public void clearData() {
		while (tableModel.getRowCount() > 0)
			tableModel.removeDBColumn(0);
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TableModelDBTable extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		String columnNames[] = { "Column", "Type" };

		DBTable dbTable;

		private PnlSQLMappings pnlConnMappings;

		/**
		 * Constructor.
		 */
		public TableModelDBTable() {
			dbTable = new DBTable();
			this.pnlConnMappings = null;
		}

		/**
		 * Sets the panel of connection mappings.
		 * 
		 * @param pnlConnMappings
		 */
		public void setPnlConnMappings(PnlSQLMappings pnlConnMappings) {
			this.pnlConnMappings = pnlConnMappings;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
		public String getColumnName(int col) {
			return columnNames[col].toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return dbTable.getCount();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			DBColumn dbColumn;
			Object res = null;

			dbColumn = (DBColumn) dbTable.get(row);
			switch (col) {
			case 0:
				res = dbColumn.getName();
				break;
			case 1:
				res = dbColumn.getType();
				break;
			}
			return res;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int,
		 *      int)
		 */
		public void setValueAt(Object value, int row, int col) {
			DBColumn dbColumn;

			dbColumn = (DBColumn) dbTable.get(row);
			switch (col) {
			case 0:
				dbColumn.setName((String) value);
				pnlConnMappings.updateDBColumns();
				break;
			case 1:
				dbColumn.setType((String) value);
				break;
			}
			fireTableCellUpdated(row, col);
		}

		/**
		 * Adds a DBColumn.
		 * 
		 * @param column
		 */
		public void addDBColumn(DBColumn column) {
			dbTable.add(column);
			fireTableRowsInserted(dbTable.getCount() - 1,
					dbTable.getCount() - 1);
			pnlConnMappings.updateDBColumns();
		}

		/**
		 * Removes a DBColumn by index.
		 * 
		 * @param row
		 *            index.
		 */
		public void removeDBColumn(int row) {
			dbTable.remove(row);
			fireTableRowsDeleted(row, row);
			pnlConnMappings.updateDBColumns();
		}

		/**
		 * Returns the structure of the database table.
		 * 
		 * @return
		 */
		public DBTable getDBTable() {
			return dbTable;
		}
	}
}
