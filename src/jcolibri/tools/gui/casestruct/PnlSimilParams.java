package jcolibri.tools.gui.casestruct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.CBRSimilarityParam;

/**
 * Panel to manage the parameters of a similarity function.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlSimilParams extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable tbSimParams;

	/**
	 * Constructor.
	 */
	public PnlSimilParams() {
		super();
		createComponents();
	}

	/**
	 * Clear the parameters.
	 */
	public void clearSimParams() {
		DefaultTableModel defTbMdl = (DefaultTableModel) tbSimParams.getModel();
		while (defTbMdl.getRowCount() > 0) {
			defTbMdl.removeRow(0);
		}
	}

	/**
	 * Adds a parameter.
	 * 
	 * @param param
	 */
	public void addSimParam(CBRSimilarityParam param) {
		DefaultTableModel defTbMdl = (DefaultTableModel) tbSimParams.getModel();
		Object data[] = { param.getName(), param.getValue() };
		defTbMdl.addRow(data);
	}

	/**
	 * Returns the parameters.
	 * 
	 * @return
	 */
	public List getSimParams() {
		DefaultTableModel defTbMdl = (DefaultTableModel) tbSimParams.getModel();
		List params = new ArrayList();
		CBRSimilarityParam lSimParam;
		String name, value;

		for (int i = 0; i < defTbMdl.getRowCount(); i++) {
			name = (String) defTbMdl.getValueAt(i, 0);
			value = (String) defTbMdl.getValueAt(i, 1);
			lSimParam = new CBRSimilarityParam(name, value);
			params.add(lSimParam);
		}
		return params;
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		JScrollPane scrPnl;
		JTextField tfName;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Similarity parameters");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);

		// Create table.
		String columnNames[] = { "Name", "Value" };
		Object data[][] = {};
		tfName = new JTextField();
		tfName.setEditable(false);
		tbSimParams = new JTable(new DefaultTableModel(data, columnNames));
		tbSimParams.getColumnModel().getColumn(0).setCellEditor(
				new DefaultCellEditor(tfName));
		scrPnl = new JScrollPane(tbSimParams);
		tbSimParams.setPreferredScrollableViewportSize(new Dimension(100, 100));

		setLayout(new BorderLayout());
		setBorder(compoundBorder);
		add(scrPnl, BorderLayout.CENTER);
	}

}
