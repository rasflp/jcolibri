package jcolibri.tools.gui.similarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the global similarity functions.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlGlobalSim extends PnlSimType {

	private static final long serialVersionUID = 1L;

	private TbGlobalSim tbGlobalSim;

	private TbSimParams tbSimParams;

	private JButton btnAddSim, btnRemoveSim, btnAddParam, btnRemoveParam;

	private ButtonsListener btnListener;

	/**
	 * Constructor.
	 */
	public PnlGlobalSim() {
		super();
		createComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.similarity.PnlSimType#loadSimilarities()
	 */
	public void loadSimilarities() {
		tbGlobalSim.reloadSimilarities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.similarity.PnlSimType#saveSimilarities()
	 */
	public void saveSimilarities() {
		tbGlobalSim.saveSimilarities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.similarity.PnlSimType#setActivePackage(java.lang.String)
	 */
	public void setActivePackage(String pkgName) {
		tbGlobalSim.setActivePackage(pkgName);
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlTbGS, pnlTbSParams;

		// Table of local similarities.
		btnListener = new ButtonsListener();
		pnlTbGS = createPnlGSim();
		pnlTbSParams = createPnlSParams();
		setLayout(new BorderLayout());
		add(pnlTbGS, BorderLayout.CENTER);
		add(pnlTbSParams, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the panel that list global similarity fuctions.
	 * 
	 * @return
	 */
	private JPanel createPnlGSim() {
		JPanel pnlButtons, pnlRes;
		JScrollPane scPnl;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Global similarities");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel(new BorderLayout());
		pnlRes.setBorder(compoundBorder);

		// Set table of local similarities.
		tbGlobalSim = new TbGlobalSim();
		tbGlobalSim.getSelectionModel().addListSelectionListener(
				new TableListener());
		scPnl = new JScrollPane(tbGlobalSim);
		tbGlobalSim.setPreferredScrollableViewportSize(new Dimension(200, 100));

		// Panel with buttons.
		btnAddSim = new JButton("Add similarity");
        btnAddSim.setIcon(ImagesContainer.ADD);
		btnAddSim.addActionListener(btnListener);
		btnRemoveSim = new JButton("Remove similarity");
        btnRemoveSim.setIcon(ImagesContainer.REMOVE);
		btnRemoveSim.addActionListener(btnListener);
		pnlButtons = new JPanel();
		pnlButtons.add(btnAddSim);
		pnlButtons.add(btnRemoveSim);

		pnlRes.add(scPnl, BorderLayout.CENTER);
		pnlRes.add(pnlButtons, BorderLayout.PAGE_END);
		return pnlRes;
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlSParams() {
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		JPanel pnlRes, pnlButtons;
		JScrollPane scPnl;

		// Border.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory
				.createTitledBorder(lineBorder, "Parameters");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel(new BorderLayout());
		pnlRes.setBorder(compoundBorder);

		// Table of parameters.
		tbSimParams = new TbSimParams();
		scPnl = new JScrollPane(tbSimParams);
		tbSimParams.setPreferredScrollableViewportSize(new Dimension(200, 70));
		pnlRes.add(scPnl, BorderLayout.CENTER);

		// Panel with buttons.
		btnAddParam = new JButton("Add parameter");
        btnAddParam.setIcon(ImagesContainer.ADD);
		btnAddParam.addActionListener(btnListener);
		btnRemoveParam = new JButton("Remove parameter");
        btnRemoveParam.setIcon(ImagesContainer.REMOVE);
		btnRemoveParam.addActionListener(btnListener);
		pnlButtons = new JPanel();
		pnlButtons.add(btnAddParam);
		pnlButtons.add(btnRemoveParam);
		pnlRes.add(pnlButtons, BorderLayout.PAGE_END);

		return pnlRes;
	}

	/**
	 * Listener of the events of buttons.
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
			if (e.getSource() == btnAddSim) {
				// Add local similarity.
				tbGlobalSim.addNewGlobalSimilarity();
			} else if (e.getSource() == btnRemoveSim) {
				// Remove the selected local similarity.
				tbGlobalSim.removeSelGlobalSimilarity();
			} else if (e.getSource() == btnAddParam) {
				// Add parameter to selected similarity.
				tbSimParams.addNewParam();
			} else if (e.getSource() == btnRemoveParam) {
				// Remove selected parameter from selected similarity.
				tbSimParams.removeSelParam();
			}
		}
	}

	/**
	 * Listener of table events.
	 * 
	 * @author Antonio
	 */
	private class TableListener implements ListSelectionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {
			// Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty()) {
				tbSimParams.setActualSim(null);

			} else {
				int selectedRow = lsm.getMinSelectionIndex();
				tbSimParams.setActualSim(tbGlobalSim.getGlobalSim(selectedRow));
			}
		}
	}
}
