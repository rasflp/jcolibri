package jcolibri.tools.gui.casestruct;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.CBRLocalSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;
import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.LocalSimilarityRegistry;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.tools.gui.casestruct.DL.SelectConceptDialog;
import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the simple attributes properties.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlPropSimpleAtt extends PnlPropType {

	private static final long serialVersionUID = 1L;

	private JLabel lblName, lblType, lblWeight, lblLocalSim;

	private JTextField tfName, tfWeight;

	private JComboBox cbType, cbLocalSim;

	private PnlSimilParams pnlSimilParams;

	private CaseStSimpleAtt simpleAtt;

    private JButton connectToOntology;
    
	/**
	 * Constructor.
	 */
	public PnlPropSimpleAtt() {
		super();
		createComponents();
		simpleAtt = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#showCaseComp(jcolibri.tools.data.CaseStComponent)
	 */
	public void showCaseComp(CaseStComponent comp) {
		if (!(comp instanceof CaseStSimpleAtt))
			return;

		simpleAtt = (CaseStSimpleAtt) comp;
		tfName.setText(simpleAtt.getName());
		cbType.setSelectedItem(simpleAtt.getType());
		tfWeight.setText(String.valueOf(simpleAtt.getWeight()));
		cbLocalSim.setSelectedItem(simpleAtt.getLocalSim().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseComp()
	 */
	public CaseStComponent getCaseComp() {
		String name, type, simName;
		float weight;
		CBRLocalSimilarity localSim;

		name = tfName.getText();
		type = (String) cbType.getSelectedItem();
		weight = Float.valueOf(tfWeight.getText()).floatValue();
		simName = (String) cbLocalSim.getSelectedItem();
		localSim = (CBRLocalSimilarity) LocalSimilarityRegistry.getInstance()
				.getLocalSimil(simName).clone();
		localSim.setParameters(pnlSimilParams.getSimParams());
		return new CaseStSimpleAtt(name, type, weight, localSim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseCompClassName()
	 */
	public String getCaseCompClassName() {
		return CaseStSimpleAtt.class.getName();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlAux, pnlProps;
		ComboBoxListener cbListener;

		// Create components.
        connectToOntology = new JButton("Configure Ontology",ImagesContainer.CONCEPT);
        connectToOntology.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                SelectConceptDialog ctod = new SelectConceptDialog(true);
                ctod.setVisible(true);
                tfName.setText(ctod.getSelectedConcept());
            }
        });

        cbListener = new ComboBoxListener();
		lblName = new JLabel("Name:");
		lblType = new JLabel("Type:");
		lblWeight = new JLabel("Weight:");
		lblLocalSim = new JLabel("Local similarity:");
		tfName = new JTextField(15);
		cbType = new JComboBox();
		cbType.addActionListener(cbListener);
		tfWeight = new JTextField();
		cbLocalSim = new JComboBox();
		cbLocalSim.addActionListener(cbListener);
		pnlSimilParams = new PnlSimilParams();

		// Add types.
		updateSimpleAttTypes();

		// Set form.
		pnlProps = new JPanel();
		pnlProps.setLayout(new SpringLayout());
		pnlProps.add(lblName);
		pnlProps.add(tfName);
		pnlProps.add(lblType);
		pnlProps.add(cbType);
		pnlProps.add(lblWeight);
		pnlProps.add(tfWeight);
		pnlProps.add(lblLocalSim);
		pnlProps.add(cbLocalSim);
        
        
		SpringUtilities.makeCompactGrid(pnlProps, 4, 2, 5, 5, 5, 5);

		pnlAux = new JPanel();
		pnlAux.setLayout(new BoxLayout(pnlAux, BoxLayout.Y_AXIS));
		pnlAux.add(pnlProps);
		pnlAux.add(pnlSimilParams);

        JPanel p = new JPanel();
        p.add(connectToOntology);
        pnlAux.add(p);

		setLayout(new BorderLayout());
		add(pnlAux, BorderLayout.PAGE_START);
	}

	/**
	 * Updates the list of local similarities showed.
	 * 
	 * @param dataTypeName
	 */
	private void updateLocalSim(String dataTypeName) {
		CBRLocalSimilarity localSim;
		Object selItem;
		Iterator it;
		DataType dataType;

		// Get selected item.
		selItem = cbLocalSim.getSelectedItem();

		// Update items.
		cbLocalSim.removeAllItems();
		it = LocalSimilarityRegistry.getInstance().getLocalSimils().iterator();
		dataType = DataTypesRegistry.getInstance().getDataType(dataTypeName);
		if (dataType != null) {
			while (it.hasNext()) {
				localSim = (CBRLocalSimilarity) it.next();
				if (localSim.isCompatible(dataType)) {
					cbLocalSim.addItem(localSim.getName());
				}
			}
		}

		// Restore the selected items if exists.
		cbLocalSim.setSelectedItem(selItem);
	}

	/**
	 * Updates the list of simple attribute types showed.
	 */
	private void updateSimpleAttTypes() {
		Iterator it;

		cbType.removeAllItems();
		it = DataTypesRegistry.getInstance().getDataTypesNames();
		while (it.hasNext()) {
			String type = (String) it.next();
			if (!type.equals("StringEnumeration"))
				cbType.addItem(type);
		}
	}

	/**
	 * Combobox listener.
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
			if (e.getSource() == cbLocalSim) {
				localSimAction();
			} else if (e.getSource() == cbType) {
				updateLocalSim((String) cbType.getSelectedItem());
                connectToOntology.setVisible(cbType.getSelectedItem().equals("Concept"));
			}

		}

		/**
		 * 
		 */
		private void localSimAction() {
			Iterator itParams;
			String lSimName;
			CBRLocalSimilarity localSim;

			// If the simple attribute has the same similarity function that
			// is selected in the combobox show its parameters values. If it's
			// diferent, show the default values.
			lSimName = (String) cbLocalSim.getSelectedItem();
			if (simpleAtt != null) {
				if (simpleAtt.getLocalSim().getName() == lSimName) {
					localSim = simpleAtt.getLocalSim();
				} else {
					localSim = LocalSimilarityRegistry.getInstance()
							.getLocalSimil(lSimName);
				}
			} else {
				localSim = LocalSimilarityRegistry.getInstance().getLocalSimil(
						lSimName);
			}

			// Show parameters of selected local similarity.
			if (localSim != null) {
				pnlSimilParams.clearSimParams();
				itParams = localSim.getParameters().iterator();
				while (itParams.hasNext()) {
					pnlSimilParams.addSimParam((CBRSimilarityParam) itParams
							.next());
				}
			}
		}
	}
}
