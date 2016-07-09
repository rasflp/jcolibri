package jcolibri.tools.gui.connector;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;
import jcolibri.tools.data.ConnMapping;
import jcolibri.tools.data.DBTable;

/**
 * Visual table to manage the mappings of a SQL connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbSQLMappings extends JTable {

	private static final long serialVersionUID = 1L;

	private TbModelMappings tableModel;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefConnSQL
	 *            container panel.
	 * @param pnlDBTable
	 *            panel that manage the database table.
	 */
	public TbSQLMappings(PnlConnectorSQL pnlDefConnSQL, PnlSQLDBTable pnlDBTable) {
		super();
		tableModel = new TbModelMappings(pnlDefConnSQL, pnlDBTable);
		setModel(tableModel);

		TableColumn tbColumn;
		tbColumn = getColumnModel().getColumn(0);
		tbColumn.setCellEditor(new DefaultCellEditor(new JComboBox()));
		tbColumn = getColumnModel().getColumn(1);
		tbColumn.setCellEditor(new DefaultCellEditor(new JComboBox()));
	}

	/**
	 * Adds a new mapping.
	 */
	public void addNewMapping() {
		tableModel.addNewMapping();
	}

	/**
	 * Adds a mapping.
	 * 
	 * @param mapping
	 */
	public void addMapping(ConnMapping mapping) {
		tableModel.addMapping(mapping);
	}

	/**
	 * Removes the selected mapping of the table.
	 */
	public void removeSelMapping() {
		int row;

		row = getSelectedRow();
		if (row >= 0)
			tableModel.removeMapping(row);
	}

	/**
	 * Return the actual connector mappings.
	 * 
	 * @return
	 */
	public Vector getConnMappings() {
		return tableModel.getConnMappings();
	}

	/**
	 * Updates the list of case parameters showed.
	 */
	public void updateCaseParams() {
		tableModel.updateCaseParams();
	}

	/**
	 * Updates the list of database columns showed.
	 */
	public void updateDBColumns() {
		tableModel.updateDBColumns();
	}

	/**
	 * Clears all mappings.
	 */
	public void clearData() {
		while (tableModel.getRowCount() > 0)
			tableModel.removeMapping(0);
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TbModelMappings extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private String columnNames[] = { "Params", "Columns" };

		private Vector data;

		private PnlConnectorSQL pnlDefConnSQL;

		private PnlSQLDBTable pnlDBTable;

		/**
		 * Constructor.
		 * 
		 * @param pnlDefConnSQL
		 *            panel to manage SQL connectors.
		 * @param pnlDBTable
		 *            panel to manage the database table.
		 */
		public TbModelMappings(PnlConnectorSQL pnlDefConnSQL,
				PnlSQLDBTable pnlDBTable) {
			super();

			data = new Vector();
			this.pnlDefConnSQL = pnlDefConnSQL;
			this.pnlDBTable = pnlDBTable;
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
			return data.size();
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
			String[] mapping;
			mapping = (String[]) data.elementAt(row);
			return mapping[col];
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
			String[] mapping;

			mapping = (String[]) data.elementAt(row);
			mapping[col] = (String) value;
			fireTableCellUpdated(row, col);
		}

		/**
		 * Adds a new mapping to the table.
		 */
		public void addNewMapping() {
			addMapping(new ConnMapping("", ""));
		}

		/**
		 * Adds a mapping.
		 * 
		 * @param mapping
		 */
		public void addMapping(ConnMapping mapping) {
			String values[] = { mapping.getAttribute(), mapping.getColumn() };
			data.add(values);
			fireTableRowsInserted(data.size() - 1, data.size() - 1);
		}

		/**
		 * Removes a mapping by index.
		 * 
		 * @param row
		 *            index.
		 */
		public void removeMapping(int row) {
			data.remove(row);
			fireTableRowsDeleted(row, row);
		}

		/**
		 * Returns the actual connection mappings.
		 * 
		 * @return
		 */
		public Vector getConnMappings() {
			return data;
		}

		/**
		 * Updates the list of case parameters showed.
		 */
		public void updateCaseParams() {
			CaseStructure caseSt;
			CaseStComponent caseComp;
			Vector caseComps, validPaths;
			String path;
			DefaultCellEditor cellEd;
			JComboBox cbParams;
			String[] rowData;

			// Get paths to case components that can be mapped to columns.
			validPaths = new Vector();
			caseSt = pnlDefConnSQL.getCaseStructure();
			caseComps = new Vector();
			caseSt.getSubcomponents(caseComps);
			for (int i = 0; i < caseComps.size(); i++) {
				caseComp = (CaseStComponent) caseComps.elementAt(i);
				if (!caseComp.canHaveChildrens()) {
					path = caseComp.getPathWithoutCase();
					validPaths.add(path);
				}
			}

			// Update comboBox.
			cellEd = (DefaultCellEditor) getColumnModel().getColumn(0)
					.getCellEditor();
			cbParams = (JComboBox) cellEd.getComponent();
			cbParams.removeAllItems();
			for (int i = 0; i < validPaths.size(); i++) {
				path = (String) validPaths.elementAt(i);
				cbParams.addItem(path);
			}

			// Update column data.
			for (int i = 0; i < data.size(); i++) {
				rowData = (String[]) data.elementAt(i);
				if (!validPaths.contains(rowData[0])) {
					setValueAt("", i, 0);
				}
			}
		}

		/**
		 * Updates the list of database columns showed.
		 */
		public void updateDBColumns() {
			DefaultCellEditor cellEd;
			JComboBox cbColumns;
			Vector columnNames;
			String columnName;
			String[] rowData;
			DBTable dbTable;

			// Get the names of the columns of the database table.
			dbTable = pnlDBTable.getDBTable();
			columnNames = new Vector(dbTable.getCount());
			for (int i = 0; i < dbTable.getCount(); i++) {
				columnNames.add(dbTable.get(i).getName());
			}

			// Update comboBox.
			cellEd = (DefaultCellEditor) getColumnModel().getColumn(1)
					.getCellEditor();
			cbColumns = (JComboBox) cellEd.getComponent();
			cbColumns.removeAllItems();
			for (int i = 0; i < columnNames.size(); i++) {
				columnName = (String) columnNames.elementAt(i);
				if (columnName != null)
					cbColumns.addItem(columnName);
			}

			// Update column data.
			for (int i = 0; i < data.size(); i++) {
				rowData = (String[]) data.elementAt(i);
				if (!columnNames.contains(rowData[1])) {
					setValueAt("", i, 1);
				}
			}
		}
	}
}
