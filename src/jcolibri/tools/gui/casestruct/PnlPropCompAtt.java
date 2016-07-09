package jcolibri.tools.gui.casestruct;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.gui.SpringUtilities;

/**
 * Panel to manage the properties of a compound attribute.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlPropCompAtt extends PnlPropType {

	private static final long serialVersionUID = 1L;

	private JLabel lblName, lblWeight, lblGlobalSim;

	private JTextField tfName, tfWeight;

	private JComboBox cbGlobalSim;

	private PnlSimilParams pnlSimilParams;

	private CaseStCompoundAtt compAtt;

	/**
	 * Constructor.
	 */
	public PnlPropCompAtt() {
		super();
		createComponents();
		compAtt = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#showCaseComp(jcolibri.tools.data.CaseStComponent)
	 */
	public void showCaseComp(CaseStComponent comp) {
		if (!(comp instanceof CaseStCompoundAtt))
			return;

		compAtt = (CaseStCompoundAtt) comp;
		tfName.setText(compAtt.getName());
		tfWeight.setText(String.valueOf(compAtt.getWeight()));
		cbGlobalSim.setSelectedItem(compAtt.getGlobalSim().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseComp()
	 */
	public CaseStComponent getCaseComp() {
		String name, simName;
		float weight;
		CBRGlobalSimilarity globalSim;

		name = tfName.getText();
		weight = Float.valueOf(tfWeight.getText()).floatValue();
		simName = (String) cbGlobalSim.getSelectedItem();
		globalSim = (CBRGlobalSimilarity) GlobalSimilarityRegistry
				.getInstance().getGlobalSimil(simName).clone();
		globalSim.setParameters(pnlSimilParams.getSimParams());
		return new CaseStCompoundAtt(name, weight, globalSim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseCompClassName()
	 */
	public String getCaseCompClassName() {
		return CaseStCompoundAtt.class.getName();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlProps, pnlAux;

		// Create components.
		lblName = new JLabel("Name:");
		lblWeight = new JLabel("Weight:");
		lblGlobalSim = new JLabel("Global similarity:");
		tfName = new JTextField(15);
		tfWeight = new JTextField();
		cbGlobalSim = new JComboBox();
		cbGlobalSim.addActionListener(new ComboBoxListener());
		pnlSimilParams = new PnlSimilParams();

		// Add global similarities.
		updateGlobalSim();

		// Set form.
		pnlProps = new JPanel();
		pnlProps.setLayout(new SpringLayout());
		pnlProps.add(lblName);
		pnlProps.add(tfName);
		pnlProps.add(lblWeight);
		pnlProps.add(tfWeight);
		pnlProps.add(lblGlobalSim);
		pnlProps.add(cbGlobalSim);
		SpringUtilities.makeCompactGrid(pnlProps, 3, 2, 5, 5, 5, 5);

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
				if (compAtt != null) {
					if (compAtt.getGlobalSim().getName() == simName) {
						globalSim = compAtt.getGlobalSim();
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
