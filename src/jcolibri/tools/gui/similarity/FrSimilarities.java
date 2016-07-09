package jcolibri.tools.gui.similarity;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.util.ImagesContainer;

/**
 * Frame to manage similarities.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class FrSimilarities extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblType;

	private JComboBox cbType;

	private JLabel lblPkg;

	private JComboBox cbPkg;

	private JButton btnLoadSim, btnSaveSim;

	private JPanel pnlSimilarities;

	private PnlSimType pnlLocalSim, pnlGlobalSim;

	private final static String SIM_LOCAL = "local similarities";

	private final static String SIM_GLOBAL = "global similarities";

	/**
	 * Constructor.
	 */
	public FrSimilarities() {
		super();
		createComponents();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlCommon;

        this.setFrameIcon(ImagesContainer.SIMILARITY);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Manage Similarities");
		setLayout(new BorderLayout());

		// Types of connector.
		pnlLocalSim = new PnlLocalSim();
		pnlGlobalSim = new PnlGlobalSim();
		pnlSimilarities = new JPanel();
		pnlSimilarities.setLayout(new CardLayout());
		pnlSimilarities.add(pnlLocalSim, SIM_LOCAL);
		pnlSimilarities.add(pnlGlobalSim, SIM_GLOBAL);
		add(pnlSimilarities, BorderLayout.CENTER);

		// Select type of connector.
		pnlCommon = createPnlCommon();
		add(pnlCommon, BorderLayout.PAGE_START);
		setBounds(0, 0, 750, 450);
	}

	/**
	 * Creates the panel common for all similarities.
	 * 
	 * @return
	 */
	private JPanel createPnlCommon() {
		JPanel pnlType, pnlPkg, pnlLoadSave, pnlAux, pnlRes;
		ButtonListener btnLst;
		ComboBoxListener cbLst;

		// Combobox types.
		cbLst = new ComboBoxListener();
		lblType = new JLabel("Type:");
		cbType = new JComboBox();
		cbType.addItem(SIM_LOCAL);
		cbType.addItem(SIM_GLOBAL);
		cbType.addActionListener(cbLst);
		pnlType = new JPanel();
		pnlType.add(lblType);
		pnlType.add(cbType);

		// Combobox packages.
		lblPkg = new JLabel("Package:");
		cbPkg = new JComboBox();
		cbPkg.addActionListener(cbLst);
		updatePackages();
		pnlPkg = new JPanel();
		pnlPkg.add(lblPkg);
		pnlPkg.add(cbPkg);

		// Buttons of load and save.
		btnLst = new ButtonListener();
		btnLoadSim = new JButton("Load similarities");
        btnLoadSim.setIcon(ImagesContainer.LOAD);
		btnLoadSim.addActionListener(btnLst);
		btnSaveSim = new JButton("Save similarities");
        btnSaveSim.setIcon(ImagesContainer.SAVE);
		btnSaveSim.addActionListener(btnLst);
		pnlLoadSave = new JPanel();
		pnlLoadSave.add(btnLoadSim);
		pnlLoadSave.add(btnSaveSim);

		pnlAux = new JPanel();
		pnlAux.add(pnlType);
		pnlAux.add(pnlPkg);
		pnlAux.add(pnlLoadSave);

		pnlRes = new JPanel(new BorderLayout());
		pnlRes.add(pnlAux, BorderLayout.LINE_START);

		return pnlRes;
	}

	/**
	 * Updates the list of packages.
	 */
	private void updatePackages() {
		PackageInfo pkgInfo;
		Iterator it;

		it = PackageManager.getInstance().getActivePackages().iterator();
		cbPkg.removeAllItems();
		while (it.hasNext()) {
			pkgInfo = (PackageInfo) it.next();
			cbPkg.addItem(pkgInfo.getName());
		}
	}

	/**
	 * Listener of the combobox types of similarities.
	 * 
	 * @author Antonio
	 */
	class ComboBoxListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CardLayout clSimilarities;
			String type;

			if (e.getSource() == cbType) {
				setActivePackage();
				clSimilarities = (CardLayout) pnlSimilarities.getLayout();
				type = (String) cbType.getSelectedItem();
				clSimilarities.show(pnlSimilarities, type);
			} else if (e.getSource() == cbPkg) {
				setActivePackage();
			}

		}

		/**
		 * Informs about the working package.
		 * 
		 */
		private void setActivePackage() {
			String pkgName;
			PnlSimType pnlSim = null;

			if (cbType.getSelectedItem() == SIM_LOCAL)
				pnlSim = pnlLocalSim;
			else if (cbType.getSelectedItem() == SIM_GLOBAL)
				pnlSim = pnlGlobalSim;

			if (pnlSim != null) {
				pkgName = (String) cbPkg.getSelectedItem();
				pnlSim.setActivePackage(pkgName);
			}
		}
	}

	/**
	 * Listener of the combobox types of similarities.
	 * 
	 * @author Antonio
	 */
	class ButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			PnlSimType pnlSim = null;

			if (cbType.getSelectedItem() == SIM_LOCAL)
				pnlSim = pnlLocalSim;
			else if (cbType.getSelectedItem() == SIM_GLOBAL)
				pnlSim = pnlGlobalSim;

			if (e.getSource() == btnLoadSim) {
				pnlSim.loadSimilarities();
			} else if (e.getSource() == btnSaveSim) {
				pnlSim.saveSimilarities();
			}
		}
	}
}
