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
import javax.swing.border.Border;

import jcolibri.tools.data.ConnMapping;
import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the mappings of a SQL connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlSQLMappings extends JPanel {

	private static final long serialVersionUID = 1L;

	private TbSQLMappings tbMappings;

	private JButton btnAdd, btnRemove;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefConnSQL
	 *            panel to manage the SQL connectors.
	 * @param pnlDBTable
	 *            pnel to manage the database table.
	 */
	public PnlSQLMappings(PnlConnectorSQL pnlDefConnSQL,
			PnlSQLDBTable pnlDBTable) {
		super();
		createComponents(pnlDefConnSQL, pnlDBTable);
	}

	/**
	 * Returns the actual connector mappings.
	 * 
	 * @return
	 */
	public Vector getConnMappings() {
		return tbMappings.getConnMappings();
	}

	/**
	 * Updates the list of case parameters showed.
	 */
	public void updateCaseParams() {
		tbMappings.updateCaseParams();
	}

	/**
	 * Updates the list of database columns showed.
	 */
	public void updateDBColumns() {
		tbMappings.updateDBColumns();
	}

	/**
	 * Adds a mapping.
	 * 
	 * @param mapping
	 */
	public void addConnMapping(ConnMapping mapping) {
		tbMappings.addMapping(mapping);
	}

	/**
	 * Clears the mappings.
	 */
	public void clearConnMappings() {
		tbMappings.clearData();
	}

	/**
	 * Creates the visual components.
	 * 
	 * @param pnlDefConnSQL
	 * @param pnlDBTable
	 */
	private void createComponents(PnlConnectorSQL pnlDefConnSQL,
			PnlSQLDBTable pnlDBTable) {
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		JPanel pnlTable, pnlButtons;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder, "Mappings");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);
		setLayout(new BorderLayout());

		// Create subpanels.
		pnlTable = createPnlTable(pnlDefConnSQL, pnlDBTable);
		pnlButtons = createPnlButtons();
		add(pnlTable, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the visual components.
	 * 
	 * @param pnlDefConnSQL
	 * @param pnlDBTable
	 * @return
	 */
	private JPanel createPnlTable(PnlConnectorSQL pnlDefConnSQL,
			PnlSQLDBTable pnlDBTable) {
		JScrollPane scrPnl;
		JPanel pnlRes;

		// Table of mappings.
		tbMappings = new TbSQLMappings(pnlDefConnSQL, pnlDBTable);
		scrPnl = new JScrollPane(tbMappings);
		tbMappings.setPreferredScrollableViewportSize(new Dimension(200, 100));

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
				tbMappings.addNewMapping();
			} else if (e.getSource() == btnRemove) {
				tbMappings.removeSelMapping();
			}
		}
	}
}
