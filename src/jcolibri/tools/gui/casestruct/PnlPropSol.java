package jcolibri.tools.gui.casestruct;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStSolution;
import jcolibri.tools.gui.SpringUtilities;

/**
 * Panel to manage solution properties.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlPropSol extends PnlPropType {

	private static final long serialVersionUID = 1L;

	private JLabel lblGlobalSim;

	private JComboBox cbGlobalSim;

	private PnlSimilParams pnlSimilParams;

	private CaseStSolution solution;

	/**
	 * Constructor.
	 */
	public PnlPropSol() {
		super();
		createComponents();
		solution = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#showCaseComp(jcolibri.tools.data.CaseStComponent)
	 */
	public void showCaseComp(CaseStComponent comp) {
		if (!(comp instanceof CaseStSolution))
			return;

		solution = (CaseStSolution) comp;
		cbGlobalSim.setSelectedItem(solution.getGlobalSim().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseComp()
	 */
	public CaseStComponent getCaseComp() {
		String simName;
		CBRGlobalSimilarity globalSim;

		simName = (String) cbGlobalSim.getSelectedItem();
		globalSim = (CBRGlobalSimilarity) GlobalSimilarityRegistry
				.getInstance().getGlobalSimil(simName).clone();
		globalSim.setParameters(pnlSimilParams.getSimParams());
		return new CaseStSolution(globalSim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseCompClassName()
	 */
	public String getCaseCompClassName() {
		return CaseStSolution.class.getName();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlProps, pnlAux;

		// Create components.
		lblGlobalSim = new JLabel("Global similarity:");
		cbGlobalSim = new JComboBox();
		cbGlobalSim.addActionListener(new ComboBoxListener());
		pnlSimilParams = new PnlSimilParams();

		// Add global similarities.
		updateGlobalSim();

		// Set form.
		pnlProps = new JPanel();
		pnlProps.setLayout(new SpringLayout());
		pnlProps.add(lblGlobalSim);
		pnlProps.add(cbGlobalSim);
		SpringUtilities.makeCompactGrid(pnlProps, 1, 2, 5, 5, 5, 5);

		pnlAux = new JPanel();
		pnlAux.setLayout(new BoxLayout(pnlAux, BoxLayout.Y_AXIS));
		pnlAux.add(pnlProps);
		pnlAux.add(pnlSimilParams);

		setLayout(new BorderLayout());
		add(pnlAux, BorderLayout.PAGE_START);
	}

	/**
	 * Updates the list of global similarities showed.
	 */
	private void updateGlobalSim() {
		CBRGlobalSimilarity globalSim;
		Object selItem;
		Iterator it;

		// Get selected item.
		selItem = cbGlobalSim.getSelectedItem();

		// Update items.
		cbGlobalSim.removeAllItems();
		it = GlobalSimilarityRegistry.getInstance().getGlobalSimils()
				.iterator();
		while (it.hasNext()) {
			globalSim = (CBRGlobalSimilarity) it.next();
			cbGlobalSim.addItem(globalSim.getName());
		}

		// Restore the selected items.
		cbGlobalSim.setSelectedItem(selItem);
	}

	/**
	 * Combo box listener.
	 * 
	 * @author Antonio
	 */
	private class ComboBoxListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Iterator itParams;
			String simName;
			CBRGlobalSimilarity globalSim;

			if (e.getSource() == cbGlobalSim) {
				// If the simple attribute has the same similarity function that
				// is selected in the combobox show its parameters values. If
				// it's
				// diferent, show the default values.
				simName = (String) cbGlobalSim.getSelectedItem();
				if (solution != null) {
					if (solution.getGlobalSim().getName() == simName) {
						globalSim = solution.getGlobalSim();
					} else {
						globalSim = GlobalSimilarityRegistry.getInstance()
								.getGlobalSimil(simName);
					}
				} else {
					globalSim = GlobalSimilarityRegistry.getInstance()
							.getGlobalSimil(simName);
				}

				// Show parameters of selected local similarity.
				if (globalSim != null) {
					pnlSimilParams.clearSimParams();
					itParams = globalSim.getParameters().iterator();
					while (itParams.hasNext()) {
						pnlSimilParams
								.addSimParam((CBRSimilarityParam) itParams
										.next());
					}
				}
			}
		}
	}
}
