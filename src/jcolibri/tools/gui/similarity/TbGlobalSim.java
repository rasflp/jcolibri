package jcolibri.tools.gui.similarity;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.GlobalSimilPkgReg;
import jcolibri.cbrcore.GlobalSimilarityRegistry;

/**
 * Visual table to manage the global similarities.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbGlobalSim extends JTable {

	private static final long serialVersionUID = 1L;

	private TbModel tableModel;

	private int contSim;

	/**
	 * Constructor.
	 */
	public TbGlobalSim() {
		super();

		tableModel = new TbModel();
		setModel(tableModel);
		contSim = 1;
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
	 * Adds a new global similarity function.
	 */
	public void addNewGlobalSimilarity() {
		CBRGlobalSimilarity GlobalSim;
		String name, className;

		// !! verificar que el nombre no existe.
		name = "Global sim " + contSim;
		contSim++;
		className = "";
		GlobalSim = new CBRGlobalSimilarity(name, className);

		tableModel.addGlobalSimilarity(GlobalSim);
	}

	/**
	 * Removes the selected global similarity of the table.
	 */
	public void removeSelGlobalSimilarity() {
		int row;

		row = getSelectedRow();
		if (row >= 0)
			tableModel.removeGlobalSimilarity(row);
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
	 * Returns a global similarity bu index.
	 * 
	 * @param row
	 *            index.
	 * @return
	 */
	public CBRGlobalSimilarity getGlobalSim(int row) {
		return tableModel.getGlobalSimilarity(row);
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TbModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		String columnNames[] = { "Name", "ClassName" };

		GlobalSimilPkgReg lsPkgReg;

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
			if (lsPkgReg != null)
				return lsPkgReg.getNumGlobalSimils();
			else
				return 0;
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
			CBRGlobalSimilarity globalSim;
			Object res = null;

			globalSim = getGlobalSimilarity(row);
			switch (col) {
			case 0:
				res = globalSim.getName();
				break;
			case 1:
				res = globalSim.getClassName();
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
			CBRGlobalSimilarity globalSim;

			globalSim = getGlobalSimilarity(row);
			switch (col) {
			case 0:
				globalSim.setName((String) value);
				break;
			case 1:
				globalSim.setClassName((String) value);
				break;
			}
			fireTableCellUpdated(row, col);
		}

		/**
		 * Sets the working package.
		 * 
		 * @param pkgName
		 */
		public void setActivePackage(String pkgName) {
			lsPkgReg = GlobalSimilarityRegistry.getInstance()
					.getGlobalSimilPkg(pkgName);
			fireTableDataChanged();
		}

		/**
		 * Adds a new Global similarity.
		 * 
		 * @param globalSim
		 */
		public void addGlobalSimilarity(CBRGlobalSimilarity globalSim) {
			int row;

			lsPkgReg.addGlobalSimil(globalSim);
			row = lsPkgReg.getNumGlobalSimils() - 1;
			fireTableRowsInserted(row, row);
		}

		/**
		 * Removes a Global similarity by index.
		 * 
		 * @param row
		 *            index.
		 */
		public void removeGlobalSimilarity(int row) {
			lsPkgReg.removeGlobalSimil(row);
			fireTableRowsDeleted(row, row);
		}

		/**
		 * Returns a global similarity by index.
		 * 
		 * @param row
		 *            index.
		 * @return
		 */
		public CBRGlobalSimilarity getGlobalSimilarity(int row) {
			return lsPkgReg.getGlobalSimil(row);
		}

		/**
		 * Reloads the similarities.
		 */
		public void reloadSimilarities() {
			GlobalSimilarityRegistry.getInstance().reload();
			fireTableDataChanged();
		}

		/**
		 * Saves the similarities.
		 */
		public void saveSimilarities() {
			GlobalSimilarityRegistry.getInstance().save();
		}
	}
}
