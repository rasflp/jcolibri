package jcolibri.tools.gui.casestruct.DL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import jcolibri.cbrcore.CBRLocalSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;
import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.LocalSimilarityRegistry;
import jcolibri.extensions.DL.util.OntoBridge;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStSimpleAttConcept;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.tools.gui.casestruct.PnlPropType;
import jcolibri.tools.gui.casestruct.PnlSimilParams;
import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the simple attributes properties.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlPropSimpleAttConcept extends PnlPropType {

	private static final long serialVersionUID = 1L;

	private JLabel lblName, lblWeight, lblLocalSim, lblRelation;

	private JTextField tfName, tfWeight;

	private JComboBox cbLocalSim, cbRelations;

	private PnlSimilParams pnlSimilParams;

	private CaseStSimpleAttConcept simpleAttConcept;

    private JButton connectToOntology;
    
	/**
	 * Constructor.
	 */
	public PnlPropSimpleAttConcept() {
		super();
		createComponents();
		simpleAttConcept = null;
        updateLocalSim("Concept");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#showCaseComp(jcolibri.tools.data.CaseStComponent)
	 */
	public void showCaseComp(CaseStComponent comp) {
		if (!(comp instanceof CaseStSimpleAttConcept))
			return;
		updateRelations();
		simpleAttConcept = (CaseStSimpleAttConcept) comp;
		tfName.setText(simpleAttConcept.getName());
        cbRelations.setSelectedItem(simpleAttConcept.getRelation());
		tfWeight.setText(String.valueOf(simpleAttConcept.getWeight()));
		cbLocalSim.setSelectedItem(simpleAttConcept.getLocalSim().getName());
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
		type = "Concept";
		weight = Float.valueOf(tfWeight.getText()).floatValue();
        String relation = (String)cbRelations.getSelectedItem();
		simName = (String) cbLocalSim.getSelectedItem();
		localSim = (CBRLocalSimilarity) LocalSimilarityRegistry.getInstance()
				.getLocalSimil(simName).clone();
		localSim.setParameters(pnlSimilParams.getSimParams());
		return new CaseStSimpleAttConcept(name, type, weight, localSim, relation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseCompClassName()
	 */
	public String getCaseCompClassName() {
		return CaseStSimpleAttConcept.class.getName();
	}

    
    private void updateRelations()
    {
        OntoBridge ob= PnlReasonerProperties.getInstance().getJenaConnectionData();
        if(ob != null)
        {
            cbRelations.removeAllItems();
            Iterator pIt = ob.listProperties();                    
            while(pIt.hasNext())
            {
                ObjectProperty op = (ObjectProperty) pIt.next();
                cbRelations.addItem(op.getLocalName());
            }
            /*
            Vector<String> propertyNames = new Vector<String>();
            while(pIt.hasNext()){
                Statement stm = (Statement)pIt.next();
                String prName = stm.getPredicate().getLocalName();
                if(!propertyNames.contains(prName)){
                    propertyNames.add(prName);
                    cbRelations.addItem(prName);
                }
            }
            */
        }
    }
    
	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlAux, pnlProps;
		ComboBoxListener cbListener;

		// Create components.
        connectToOntology = new JButton("Select Concept",ImagesContainer.CONCEPT);
        connectToOntology.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                SelectConceptDialog ctod = new SelectConceptDialog(true);
                ctod.setVisible(true);
                tfName.setText(ctod.getSelectedConcept());
                if(cbRelations.getItemCount()== 0)
                {
                    updateRelations();
                }
            }
        });

        cbListener = new ComboBoxListener();
		lblName = new JLabel("Concept:");
        lblRelation = new JLabel("Relation:");
		lblWeight = new JLabel("Weight:");
		lblLocalSim = new JLabel("Local similarity:");
		tfName = new JTextField(15);
        cbRelations = new JComboBox();
		tfWeight = new JTextField();
		cbLocalSim = new JComboBox();
		cbLocalSim.addActionListener(cbListener);
		pnlSimilParams = new PnlSimilParams();


		// Set form.
		pnlProps = new JPanel();
		pnlProps.setLayout(new SpringLayout());
		pnlProps.add(lblName);
		pnlProps.add(tfName);
        pnlProps.add(lblRelation);
        pnlProps.add(cbRelations);
		pnlProps.add(lblWeight);
		pnlProps.add(tfWeight);
		pnlProps.add(lblLocalSim);
		pnlProps.add(cbLocalSim);
        
        OntoBridge ob= PnlReasonerProperties.getInstance().getJenaConnectionData();
        if(ob != null)
        {
            Iterator pIt = ob.listProperties();                    
            while(pIt.hasNext())
            {
                ObjectProperty op = (ObjectProperty) pIt.next();
                cbRelations.addItem(op.getLocalName());
            }
        }
        
		SpringUtilities.makeCompactGrid(pnlProps, 4, 2, 5, 5, 5, 5);

		pnlAux = new JPanel();
		pnlAux.setLayout(new BoxLayout(pnlAux, BoxLayout.Y_AXIS));
        pnlAux.add(connectToOntology);
        pnlAux.add(pnlProps);
		pnlAux.add(pnlSimilParams);



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
			if (simpleAttConcept != null) {
				if (simpleAttConcept.getLocalSim().getName() == lSimName) {
					localSim = simpleAttConcept.getLocalSim();
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
