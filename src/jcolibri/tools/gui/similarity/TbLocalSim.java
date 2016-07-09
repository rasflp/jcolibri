package jcolibri.tools.gui.similarity;

import java.util.Iterator;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jcolibri.cbrcore.CBRLocalSimilarity;
import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.LocalSimilPkgReg;
import jcolibri.cbrcore.LocalSimilarityRegistry;

/**
 * Visual table to manage local similarities.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbLocalSim extends JTable {

	private static final long serialVersionUID = 1L;

	private TbModel tableModel;

	private int contSim;

	/**
	 * Constructor.
	 */
	public TbLocalSim() {
		super();

		tableModel = new TbModel();
		setModel(tableModel);
		contSim = 1;

		JComboBox cbParameters = new JComboBox();
		cbParameters.setEditable(true);
		getColumnModel().getColumn(2).setCellEditor(
				new DefaultCellEditor(cbParameters));
		updateDataTypes();
	}

	/**
	 * Sets the working package.
	 * 
	 * @param pkgName
	 */
	public void setActivePackage(String pkgName) {
		tableModel.setActivePackage(pkgName);
	}

	/**
	 * Adds a new local similarity function.
	 */
	public void addNewLocalSimilarity() {
		CBRLocalSimilarity localSim;
		String name, className;

		name = "local sim " + contSim;
		contSim++;
		className = "";
		localSim = new CBRLocalSimilarity(name, className);

		tableModel.addLocalSimilarity(localSim);
	}

	/**
	 * Removes the selected local similarity.
	 */
	public void removeSelLocalSimilarity() {
		int row;

		row = getSelectedRow();
		if (row >= 0)
			tableModel.removeLocalSimilarity(row);
	}

	/**
	 * Reloads the similarities.
	 */
	public void reloadSimilarities() {
		tableModel.reloadSimilarities();
	}

	/**
	 * Saves the similarities.
	 */
	public void saveSimilarities() {
		tableModel.saveSimilarities();
	}

	/**
	 * Returns a local similarity by index.
	 * 
	 * @param row
	 *            index
	 * @return
	 */
	public CBRLocalSimilarity getLocalSim(int row) {
		return tableModel.getLocalSimilarity(row);
	}

	/**
	 * Updates the list of data types.
	 */
	private void updateDataTypes() {
		DefaultCellEditor defCellEd;
		JComboBox cbAttributes;
		Iterator it;

		defCellEd = (DefaultCellEditor) getColumnModel().getColumn(2)
				.getCellEditor();
		cbAttributes = (JComboBox) defCellEd.getComponent();
		cbAttributes.removeAllItems();
		it = DataTypesRegistry.getInstance().getDataTypesNames();
		while (it.hasNext()) {
			cbAttributes.addItem(it.next());
		}
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TbModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		String columnNames[] = { "Name", "ClassName", "Attributes" };

		LocalSimilPkgReg lsPkgReg;

		/**
		 * Constructor.
		 */
		public TbModel() {
			lsPkgReg = null;
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
			if (lsPkgReg != null) {
				return lsPkgReg.getNumLocalSimils();
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
			CBRLocalSimilarity localSim;
			Object res = null;

			localSim = getLocalSimilarity(row);
			switch (col) {
			case 0:
				res = localSim.getName();
				break;
			case 1:
				res = localSim.getClassName();
				break;
			case 2:
				res = attributesToStr(localSim.getAttributes());
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
			CBRLocalSimilarity localSim;

			localSim = getLocalSimilarity(row);
			;
			switch (col) {
			case 0:
				localSim.setName((String) value);
				break;
			case 1:
				localSim.setClassName((String) value);
				break;
			case 2:
				strToDataTypes((String) value, localSim.getAttributes());
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
			lsPkgReg = LocalSimilarityRegistry.getInstance().getLocalSimilPkg(
					pkgName);
			fireTableDataChanged();
		}

		/**
		 * Adds a new local similarity.
		 * 
		 * @param localSim
		 */
		public void addLocalSimilarity(CBRLocalSimilarity localSim) {
			int row;

			lsPkgReg.addLocalSimil(localSim);
			row = lsPkgReg.getNumLocalSimils() - 1;
			fireTableRowsInserted(row, row);
		}

		/**
		 * Removes a local similarity by index.
		 * 
		 * @param row
		 *            index.
		 */
		public void removeLocalSimilarity(int row) {
			lsPkgReg.removeLocalSimil(row);
			fireTableRowsDeleted(row, row);
		}

		/**
		 * Returns a local similarity by index.
		 * 
		 * @param row
		 *            index.
		 * @return
		 */
		public CBRLocalSimilarity getLocalSimilarity(int row) {
			return lsPkgReg.getLocalSimil(row);
		}

		/**
		 * Reloads the similarities.
		 */
		public void reloadSimilarities() {
			LocalSimilarityRegistry.getInstance().reload();
			fireTableDataChanged();
		}

		/**
		 * Saves the similarities.
		 */
		public void saveSimilarities() {
			LocalSimilarityRegistry.getInstance().save();
		}

		/**
		 * Returns a string representing the datatypes.
		 * 
		 * @param dataTypes
		 * @return
		 */
		private String attributesToStr(Set dataTypes) {
			String str = new String("");
			Iterator it;

			it = dataTypes.iterator();
			while (it.hasNext()) {
				str += ((DataType) it.next()).getName() + ", ";
			}
			if (str.length() > 2)
				str = str.substring(0, str.length() - 2);

			return str;
		}

		/**
		 * Returns the datatypes repreted by a string.
		 * 
		 * @param str
		 * @param dataTypes
		 */
		private void strToDataTypes(String str, Set dataTypes) {
			DataTypesRegistry dataTypeReg;
			DataType dataType;
			String tokens[];

			tokens = str.split(",");
			dataTypeReg = DataTypesRegistry.getInstance();
			dataTypes.clear();
			for (int i = 0; i < tokens.length; i++) {
				dataType = dataTypeReg.getDataType(tokens[i].trim());
				if (dataType != null)
					dataTypes.add(dataType);
			}
		}
	}
}
