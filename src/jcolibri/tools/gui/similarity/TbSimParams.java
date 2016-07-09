package jcolibri.tools.gui.similarity;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jcolibri.cbrcore.CBRSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;

/**
 * Visual table to manage the similarity parameters.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 * 
 */
public class TbSimParams extends JTable {

	private static final long serialVersionUID = 1L;

	private TbModel tbMdl;

	/**
	 * Constructor.
	 */
	public TbSimParams() {
		super();
		tbMdl = new TbModel();
		setModel(tbMdl);
	}

	/**
	 * Ses the working similarity.
	 * 
	 * @param sim
	 */
	public void setActualSim(CBRSimilarity sim) {
		tbMdl.setActualSim(sim);
	}

	/**
	 * Adds a new parameter.
	 */
	public void addNewParam() {
		tbMdl.addParam(new CBRSimilarityParam("", ""));
	}

	/**
	 * Removes the selected parameter.
	 */
	public void removeSelParam() {
		int idxSel;

		idxSel = getSelectedRow();
		if (idxSel >= 0) {
			tbMdl.removeParam(idxSel);
		}
	}

	/**
	 * Table model.
	 * 
	 * @author Antonio
	 */
	private class TbModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		String columnNames[] = { "Name", "value" };

		CBRSimilarity sim;

		/**
		 * Constructor.
		 */
		public TbModel() {
			sim = null;
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
			if (sim != null)
				return sim.getParameters().size();
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
			CBRSimilarityParam param;
			Object res = null;

			if (sim != null) {
				param = (CBRSimilarityParam) sim.getParameters().get(row);
				switch (col) {
				case 0:
					res = param.getName();
					break;
				case 1:
					res = param.getValue();
					break;
				}
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
			CBRSimilarityParam param;

			if (sim != null) {
				param = (CBRSimilarityParam) sim.getParameters().get(row);
				switch (col) {
				case 0:
					param.setName((String) value);
					break;
				case 1:
					param.setValue((String) value);
					break;
				}
				fireTableCellUpdated(row, col);
			}
		}

		/**
		 * Sets the working similarity.
		 * 
		 * @param sim
		 */
		public void setActualSim(CBRSimilarity sim) {
			this.sim = sim;
			fireTableDataChanged();
		}

		/**
		 * Adds a parameter.
		 * 
		 * @param param
		 */
		public void addParam(CBRSimilarityParam param) {
			int row;

			if (sim != null) {
				sim.getParameters().add(param);
				row = sim.getParameters().size() - 1;
				fireTableRowsInserted(row, row);
			}
		}

		/**
		 * Removes a parameter by index
		 * 
		 * @param idx
		 *            index.
		 */
		public void removeParam(int idx) {
			if (sim != null) {
				sim.getParameters().remove(idx);
				fireTableRowsDeleted(idx, idx);
			}
		}
	}

}
