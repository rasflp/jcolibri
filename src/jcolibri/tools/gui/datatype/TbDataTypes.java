package jcolibri.tools.gui.datatype;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypePkgReg;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.datatypes.StringEnumType;
import jcolibri.gui.ParameterEditor;
import jcolibri.gui.ParameterEditorFactory;
import jcolibri.gui.editor.StringEditor;

/**
 * Visual table to manage data types.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbDataTypes extends JTable {

	private static final long serialVersionUID = 1L;

	private TbModel tableModel;

	int contName;

	/**
	 * Constructor.
	 */
	public TbDataTypes() {
		super();

		tableModel = new TbModel();
		setModel(tableModel);
		contName = 1;
	}

	/**
	 * Sets the working package.
	 * 
	 * @param pkgName
	 *            package name.
	 */
	public void setActivePackage(String pkgName) {
		tableModel.setActivePackage(pkgName);
	}

	/**
	 * Gets a datatype by index.
	 * 
	 * @param idx
	 *            index.
	 * @return
	 */
	public DataType getDataType(int idx) {
		return tableModel.getDataType(idx);
	}

	/**
	 * Adds a new data type.
	 */
	public void addNewDataType() {
		DataType dataType = new DataType("Name" + contName);
		tableModel.addDataType(dataType, StringEditor.class.getName());
		contName++;
	}

	/**
	 * Removes the selected data type.
	 */
	public void removeSelDataType() {
		int row;

		row = getSelectedRow();
		if (row >= 0)
			tableModel.removeDataType(row);
	}

	/**
	 * Reloads all the data types.
	 */
	public void reloadDataTypes() {
		tableModel.reloadDataTypes();
	}

	/**
	 * Saves the data types.
	 */
	public void saveDataTypes() {
		tableModel.saveDataTypes();
	}

	/**
	 * Returns the selected row of the table.
	 */
	private void reselectRow() {
		int idxSel;

		idxSel = getSelectedRow();
		if (idxSel >= 0) {
			getSelectionModel().clearSelection();
			getSelectionModel().setSelectionInterval(idxSel, idxSel);
		}
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TbModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		String columnNames[] = { "Name", "Class", "GUI editor" };

		DataTypePkgReg dtPkgReg;

		/**
		 * Constructor.
		 */
		public TbModel() {
			dtPkgReg = null;
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
			if (dtPkgReg != null) {
				return dtPkgReg.getDataTypes().size();
			} else {
				return 0;
			}
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
			DataType dataType;
			Object res = null;
			ParameterEditor paramEd;

			dataType = getDataType(row);
			switch (col) {
			case 0:
				res = dataType.getName();
				break;
			case 1:
				res = dataType.getJavaClassName();
				break;
			case 2:
				paramEd = ParameterEditorFactory.getEditor(dataType);
				if (paramEd != null)
					res = paramEd.getClass().getName();
				else
					res = "";
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
			DataType dataType, dtAux;
			ParameterEditor paramEd;

			dataType = getDataType(row);

			switch (col) {
			case 0:
				paramEd = ParameterEditorFactory.getEditor(dataType.getName());
				if (paramEd != null)
					ParameterEditorFactory.unregisterEditor(dataType.getName());
				dataType.setName((String) value);
				if (paramEd != null)
					ParameterEditorFactory.registerEditor(dataType.getName(),
							paramEd.getClass().getName());
				break;
			case 1:
				if (isEnumeration(dataType.getJavaClassName())) {
					if (isEnumeration((String) value)) {
						dataType.setJavaClassName((String) value);
					} else {
						dtAux = new DataType(dataType.getName(), (String) value);
						dtPkgReg.replaceDataType(dataType.getName(), dtAux);
						reselectRow();
					}
				} else {
					if (isEnumeration((String) value)) {
						dtAux = new StringEnumType(dataType.getName());
						dtPkgReg.replaceDataType(dataType.getName(), dtAux);
						reselectRow();
					} else {
						dataType.setJavaClassName((String) value);
					}
				}
				break;
			case 2:
				ParameterEditorFactory.registerEditor(dataType.getName(),
						(String) value);
				break;
			}
			fireTableCellUpdated(row, col);
		}

		/**
		 * Sets the working package.
		 * 
		 * @param pkgName
		 *            package name.
		 */
		public void setActivePackage(String pkgName) {
			dtPkgReg = DataTypesRegistry.getInstance().getDataTypePkg(pkgName);
			fireTableDataChanged();
		}

		/**
		 * Adds a data type.
		 * 
		 * @param dataType
		 *            data type.
		 * @param guiEditor
		 *            java class to edit the data type.
		 */
		public void addDataType(DataType dataType, String guiEditor) {
			int row;

			dtPkgReg.addDataType(dataType);
			ParameterEditorFactory
					.registerEditor(dataType.getName(), guiEditor);

			row = getRowCount() - 1;
			fireTableRowsInserted(row, row);
		}

		/**
		 * Removes a data type by index.
		 * 
		 * @param row
		 *            index.
		 */
		public void removeDataType(int row) {
			dtPkgReg.removeDataType(row);
			fireTableRowsDeleted(row, row);
		}

		/**
		 * Returns a data type by index.
		 * 
		 * @param row
		 *            index
		 * @return
		 */
		public DataType getDataType(int row) {
			return dtPkgReg.getDataType(row);
		}

		/**
		 * Reloads the data types from the working package.
		 */
		public void reloadDataTypes() {
			try {
				DataTypesRegistry.getInstance().reload();
			} catch (InitializingException e) {
				e.printStackTrace();
			}
			fireTableDataChanged();
		}

		/**
		 * Saves the data types.
		 */
		public void saveDataTypes() {
			DataTypesRegistry.getInstance().save();
		}

		/**
		 * Check if a class is an enumeration class.
		 * 
		 * @param className
		 * @return
		 */
		private boolean isEnumeration(String className) {
			return className.equals(StringEnumType.class.getName());
		}
	}
}
