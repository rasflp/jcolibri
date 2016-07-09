package jcolibri.tools.gui.connector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the parameters of a connector of type "Other".
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlOtherParams extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable tbParams;

	private JButton btnAdd, btnRemove;

	/**
	 * Constructor.
	 */
	public PnlOtherParams() {
		super();
		createComponents();
	}

	/**
	 * Clears the parameters.
	 */
	public void clearTable() {
		while (tbParams.getRowCount() > 0) {
			tbParams.remove(0);
		}
	}

	/**
	 * Adds a parameter.
	 * 
	 * @param name
	 *            parameter name.
	 * @param value
	 *            parameter value.
	 */
	public void addParam(String name, String value) {
		DefaultTableModel defTbMdl = (DefaultTableModel) tbParams.getModel();
		Object data[] = { name, value };

		defTbMdl.addRow(data);
	}

	/**
	 * Returns the parameters.
	 * 
	 * @return
	 */
	public Vector getParams() {
		DefaultTableModel defTbMdl;
		Vector data, params = null;
		String aux[];

		defTbMdl = (DefaultTableModel) tbParams.getModel();
		data = defTbMdl.getDataVector();
		params = new Vector();
		for (int i = 0; i < data.size(); i++) {
			aux = new String[2];
			aux[0] = (String) ((Vector) data.get(i)).get(0);
			aux[1] = (String) ((Vector) data.get(i)).get(1);
			params.add(aux);
		}

		return params;
	}

	/**
	 * Removes the selected parameter.
	 */
	private void removeSelParam() {
		DefaultTableModel defTbMdl = (DefaultTableModel) tbParams.getModel();
		int idxSelRow;

		idxSelRow = tbParams.getSelectedRow();
		if (idxSelRow >= 0) {
			defTbMdl.removeRow(idxSelRow);
		}
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		JPanel pnlTable, pnlButtons;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory
				.createTitledBorder(lineBorder, "Parameters");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);
		setLayout(new BorderLayout());

		// Create subpanels.
		pnlTable = createPnlTable();
		pnlButtons = createPnlButtons();
		add(pnlTable, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlTable() {
		JScrollPane scrPnl;
		JPanel pnlRes;
		String columnNames[] = { "Name", "Value" };
		Object data[][] = {};

		// Table of parameters.
		tbParams = new JTable(new DefaultTableModel(data, columnNames));
		scrPnl = new JScrollPane(tbParams);
		tbParams.setPreferredScrollableViewportSize(new Dimension(200, 100));

		pnlRes = new JPanel(new BorderLayout());
		pnlRes.add(scrPnl, BorderLayout.CENTER);
		return pnlRes;
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlButtons() {
		JPanel pnlRes;
		ButtonsListener btnListener;

		btnListener = new ButtonsListener();
		btnAdd = new JButton("Add");
        btnAdd.setIcon(ImagesContainer.ADD);
		btnAdd.addActionListener(btnListener);
		btnRemove = new JButton("Remove");
        btnRemove.setIcon(ImagesContainer.REMOVE);
		btnRemove.addActionListener(btnListener);

		pnlRes = new JPanel();
		pnlRes.add(btnAdd);
		pnlRes.add(btnRemove);
		return pnlRes;
	}

	/**
	 * Listener of button events.
	 * 
	 * @author Antonio
	 */
	private class ButtonsListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAdd) {
				addParam("", "");
			} else if (e.getSource() == btnRemove) {
				removeSelParam();
			}
		}
	}
}
