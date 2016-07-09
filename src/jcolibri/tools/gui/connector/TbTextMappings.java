package jcolibri.tools.gui.connector;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;

/**
 * Visual table of mappings for the plain text connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TbTextMappings extends JTable {

	private static final long serialVersionUID = 1L;

	private PnlConnectorText pnlDefConnText;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefConnText
	 *            container panel.
	 */
	public TbTextMappings(PnlConnectorText pnlDefConnText) {
		super();

		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		String[] columnNames = { "Column", "Parameter" };
		JComboBox cbParam;
		JTextField tfCNumber;

		this.pnlDefConnText = pnlDefConnText;
		defTbMdl.setColumnIdentifiers(columnNames);

		tfCNumber = new JTextField();
		tfCNumber.setEditable(false);
		getColumnModel().getColumn(0).setCellEditor(
				new DefaultCellEditor(tfCNumber));

		cbParam = new JComboBox();
		getColumnModel().getColumn(1).setCellEditor(
				new DefaultCellEditor(cbParam));
	}

	/**
	 * Updates the list of case parameters showed.
	 */
	public void updateCaseParams() {
		CaseStructure caseSt;
		JComboBox cbParams;
		Vector caseComps, validPaths;
		CaseStComponent caseComp;
		String path;
		DefaultCellEditor cellEd;

		// Get paths to case components that can be mapped to columns.
		caseSt = pnlDefConnText.getCaseSt();
		caseComps = new Vector();
		caseSt.getSubcomponents(caseComps);
		validPaths = new Vector();
		for (int i = 0; i < caseComps.size(); i++) {
			caseComp = (CaseStComponent) caseComps.elementAt(i);
			if (!caseComp.canHaveChildrens()) {
				path = caseComp.getPathWithoutCase();
				validPaths.add(path);
			}
		}

		// Update comboBox.
		cellEd = (DefaultCellEditor) getColumnModel().getColumn(1)
				.getCellEditor();
		cbParams = (JComboBox) cellEd.getComponent();
		cbParams.removeAllItems();
		for (int i = 0; i < validPaths.size(); i++) {
			path = (String) validPaths.elementAt(i);
			cbParams.addItem(path);
		}
	}

	/**
	 * Adds a mapping.
	 * 
	 * @param param
	 */
	public void addMapping(String param) {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		Object data[] = { new Integer(getRowCount()), param };

		defTbMdl.addRow(data);
	}

	/**
	 * Adds a new mapping.
	 */
	public void addNewMapping() {
		addMapping("");
	}

	/**
	 * Removes the selected mapping.
	 * 
	 */
	public void removeSelMapping() {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		int idxSelRow;

		idxSelRow = getSelectedRow();
		if (idxSelRow >= 0) {
			defTbMdl.removeRow(idxSelRow);
			for (int i = idxSelRow; i < getRowCount(); i++)
				setValueAt(new Integer(i), i, 0);
		}
	}

	/**
	 * Clears the mappings.
	 * 
	 */
	public void clearTable() {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();

		while (getRowCount() > 0)
			defTbMdl.removeRow(0);
	}

	/**
	 * Return the actual connector mappings.
	 * 
	 * @return
	 */
	public Vector getConnMappings() {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		Vector data, mappings;

		mappings = new Vector();
		data = defTbMdl.getDataVector();
		for (int i = 0; i < data.size(); i++) {
			mappings.add(((Vector) data.get(i)).get(1));
		}

		return mappings;
	}

	/**
	 * Moves down 1 position the selected mapping.
	 */
	public void downSelMapping() {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		int idxSelRow;
		String param1, param2;

		idxSelRow = getSelectedRow();
		if ((idxSelRow >= 0) && (idxSelRow < getRowCount() - 1)) {
			param1 = (String) defTbMdl.getValueAt(idxSelRow, 1);
			param2 = (String) defTbMdl.getValueAt(idxSelRow + 1, 1);
			defTbMdl.setValueAt(param1, idxSelRow + 1, 1);
			defTbMdl.setValueAt(param2, idxSelRow, 1);
			getSelectionModel().setSelectionInterval(idxSelRow + 1,
					idxSelRow + 1);
		}
	}

	/**
	 * Moves up 1 position the selected mapping.
	 */
	public void upSelMapping() {
		DefaultTableModel defTbMdl = (DefaultTableModel) getModel();
		int idxSelRow;
		String param1, param2;

		idxSelRow = getSelectedRow();
		if ((idxSelRow >= 0) && (idxSelRow > 0)) {
			param1 = (String) defTbMdl.getValueAt(idxSelRow, 1);
			param2 = (String) defTbMdl.getValueAt(idxSelRow - 1, 1);
			defTbMdl.setValueAt(param1, idxSelRow - 1, 1);
			defTbMdl.setValueAt(param2, idxSelRow, 1);
			getSelectionModel().setSelectionInterval(idxSelRow - 1,
					idxSelRow - 1);
		}
	}
}
