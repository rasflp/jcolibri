package jcolibri.tools.gui.casestruct;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.gui.CBRArmGUI;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;
import jcolibri.tools.gui.casestruct.DL.PnlPropSimpleAttConcept;
import jcolibri.tools.gui.casestruct.DL.PnlReasonerProperties;
import jcolibri.util.CBRLogger;
import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the case structures.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class FrCaseStructure extends JInternalFrame implements
		TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	// Components.
	private TreeCases treeCase;

	private JButton btnLoad, btnSave;

	private JButton btnAddAttSimple, btnAddAttComp, btnRemoveAtt;

	private JPanel pnlProps;

	private JButton btnAttApply;

	private PnlPropType pnlPropTypes[];

	private ButtonsListener btnListener;
    
	private static final String ID_NONE = "none";

    boolean showDL = false; 
	/**
	 * Constructor.
	 * 
	 */
	public FrCaseStructure() {
		super();
        showDL = PackageManager.getInstance().getPackageByName("Description Logic Extension").isActive();
		createComponents();
	}

	/**
	 * Show the properties panel of a case component.
	 * 
	 * @param caseComp
	 *            case component.
	 */
	public void showCaseStCompProps(CaseStComponent caseComp) {
		PnlPropType pnlProp = null;
		String idPnl = ID_NONE;

		// Look for the suitable properties panel.
		if (caseComp != null) {
			for (int i = 0; (i < pnlPropTypes.length) && (pnlProp == null); i++) {
				if (pnlPropTypes[i].getCaseCompClassName() == caseComp
						.getClass().getName())
                {
					pnlProp = pnlPropTypes[i];
                    idPnl = pnlProp.getCaseCompClassName();
                    if(showDL)
                        if((i==5) && pureOntological)
                        {
                            pnlProp = pnlPropTypes[7];
                            idPnl = pnlProp.getCaseCompClassName();
                        }
                    
                }
			}
		}
		if (pnlProp != null) {
			pnlProp.showCaseComp(caseComp);
		} else {
			idPnl = ID_NONE;
		}

		((CardLayout) pnlProps.getLayout()).show(pnlProps, idPnl);
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlLoadSave, pnlCaseStruct, pnlCaseProps;
		JSplitPane spPnl;

        this.setFrameIcon(jcolibri.util.ImagesContainer.CASESTRUCTURE);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Manage Case Structures");
		setLayout(new BorderLayout());

		// Create subpanels.
		btnListener = new ButtonsListener();
		pnlLoadSave = createPnlLoadSave();
		pnlCaseStruct = createPnlCaseStruct();
		pnlCaseProps = createPnlCaseProps();
		spPnl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spPnl.setLeftComponent(pnlCaseStruct);
		spPnl.setRightComponent(pnlCaseProps);
		spPnl.setDividerLocation(0.5);

		add(pnlLoadSave, BorderLayout.NORTH);
		add(spPnl, BorderLayout.CENTER);
		setBounds(0, 0, 750, 450);
	}

    
    public static boolean pureOntological = false;
    
    JComboBox cbType;
    
	/**
	 * Creates and returns the panel with locad/save buttons.
	 * 
	 * @return
	 */
	private JPanel createPnlLoadSave() {
		JPanel pnlRes;

		// Add buttons.
        pnlRes = new JPanel();
        if(showDL)
        {
            pnlRes.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
            JLabel lblType = new JLabel("Type:");
            cbType = new JComboBox();
            cbType.addItem("STANDARD");
            cbType.addItem("ONTOLOGY");
    
            cbType.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    boolean newTypeOnto = cbType.getSelectedItem().equals("ONTOLOGY");
                    if(pureOntological != newTypeOnto)
                    {
                        pureOntological = newTypeOnto;
                        //if(pureOntological)
                        //    pnlPropTypes[5] = new PnlPropCaseConcept();
                        //else
                        //    pnlPropTypes[5] = new PnlPropCase();
                        treeCase.reset();
                    }
                }
                
            });
            JPanel pnlAux = new JPanel();
            pnlAux.add(lblType);
            pnlAux.add(cbType);
            pnlRes.add(pnlAux);
        }
        
		btnLoad = new JButton("Load case structure");
        btnLoad.setIcon(ImagesContainer.LOAD);  
		btnLoad.addActionListener(btnListener);
		btnSave = new JButton("Save case structure");
        btnSave.setIcon(ImagesContainer.SAVE);  
		btnSave.addActionListener(btnListener);
        JPanel pnlAux = new JPanel();
		pnlAux.add(btnLoad);
		pnlAux.add(btnSave);
		pnlRes.add(pnlAux);
		return pnlRes;
	}

	/**
	 * Creates and return the panel with the tree that defines the structure of
	 * the case.
	 * 
	 * @return
	 */
	private JPanel createPnlCaseStruct() {
		JPanel pnlRes, pnlButtons;
		JScrollPane scPnl;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Case structure");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel();
		pnlRes.setBorder(compoundBorder);
		pnlRes.setLayout(new BorderLayout());
        
		// Set tree.
		treeCase = new TreeCases(this);
		scPnl = new JScrollPane(treeCase);
		pnlRes.add(scPnl, BorderLayout.CENTER);

		// Set buttons.
		btnAddAttSimple = new JButton("Add simple");
		btnAddAttSimple.addActionListener(btnListener);
		btnAddAttComp = new JButton("Add compound");
		btnAddAttComp.addActionListener(btnListener);
		btnRemoveAtt = new JButton("Remove");
		btnRemoveAtt.addActionListener(btnListener);
		pnlButtons = new JPanel();
		pnlButtons.add(btnAddAttSimple);
		pnlButtons.add(btnAddAttComp);
		pnlButtons.add(btnRemoveAtt);
		pnlRes.add(pnlButtons, BorderLayout.PAGE_END);

		return pnlRes;
	}

	/**
	 * Creates and return the panel with properties of the attributes.
	 * 
	 * @return
	 */
	private JPanel createPnlCaseProps() {
		JPanel pnlBtn, pnlRes;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory
				.createTitledBorder(lineBorder, "Properties");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel();
		pnlRes.setBorder(compoundBorder);
		pnlRes.setLayout(new BorderLayout());

		// Set subpanels.
        if(showDL)
        {
    		pnlPropTypes = new PnlPropType[8];
    		pnlPropTypes[0] = new PnlPropDescr();
    		pnlPropTypes[1] = new PnlPropSol();
    		pnlPropTypes[2] = new PnlPropRes();
    		pnlPropTypes[3] = new PnlPropSimpleAtt();
    		pnlPropTypes[4] = new PnlPropCompAtt();
            pnlPropTypes[5] = new PnlPropCase();
            pnlPropTypes[6] = new PnlPropSimpleAttConcept();
            pnlPropTypes[7] = new PnlPropCaseConcept();
        }else
        {
            pnlPropTypes = new PnlPropType[6];
            pnlPropTypes[0] = new PnlPropDescr();
            pnlPropTypes[1] = new PnlPropSol();
            pnlPropTypes[2] = new PnlPropRes();
            pnlPropTypes[3] = new PnlPropSimpleAtt();
            pnlPropTypes[4] = new PnlPropCompAtt();
            pnlPropTypes[5] = new PnlPropCase();       
        }

		pnlProps = new JPanel();
		pnlProps.setLayout(new CardLayout());
		for (int i = 0; i < pnlPropTypes.length; i++)
			pnlProps.add(pnlPropTypes[i], pnlPropTypes[i]
					.getCaseCompClassName());
        
        /*
        if(showDL)
            pnlProps.add(pnlPropTypes[6], pnlPropTypes[6].getCaseCompClassName()+"Concept");
        else
            pnlProps.add(pnlPropTypes[5], pnlPropTypes[5].getCaseCompClassName());
        */
		pnlProps.add(new JPanel(), ID_NONE);
		pnlRes.add(pnlProps, BorderLayout.CENTER);

		btnAttApply = new JButton("Apply changes");
        btnAttApply.setIcon(ImagesContainer.APPLY);  
		btnAttApply.addActionListener(btnListener);
		pnlBtn = new JPanel();
		pnlBtn.add(btnAttApply);
		pnlRes.add(pnlBtn, BorderLayout.PAGE_END);

		return pnlRes;
	}

	/**
	 * Listener of selection events of the tree.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		CaseStComponent caseComp;

		caseComp = ((TreeCases) e.getSource()).getSelCaseStComp();
		showCaseStCompProps(caseComp);
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
			JFrame frame;
			FileDialog fd;
			String fileName;
			CaseStructure caseSt;

			if (e.getSource() == btnLoad) {
				frame = (JFrame) btnLoad.getTopLevelAncestor();
				fd = new FileDialog(frame, "Load case structure from file...",
						FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getFile() != null) {
					fileName = fd.getDirectory() + fd.getFile();
					caseSt = new CaseStructure();
					caseSt.readFromXMLFile(fileName);
                    if(cbType != null)
                        if(caseSt.getConcept()!=null)
                            cbType.setSelectedItem("ONTOLOGY");
                        else
                            cbType.setSelectedItem("STANDARD");
                    if(caseSt.usesOntoBridge())
                        PnlReasonerProperties.getInstance().configureFromOntoBridge();
					treeCase.setCaseStructure(caseSt);
				}
			} else if (e.getSource() == btnSave) {
				frame = (JFrame) btnSave.getTopLevelAncestor();
				fd = new FileDialog(frame, "Save case structure to file...",
						FileDialog.SAVE);
				fd.setFile("caseStructure.xml");
				fd.setVisible(true);
				if (fd.getFile() != null) {
					fileName = fd.getDirectory() + fd.getFile();
					caseSt = treeCase.getCaseStructure();
					caseSt.writeToXMLFile(fileName);
				}
			} else if (e.getSource() == btnAddAttSimple) {
				// Add simple attribute.
				DefaultMutableTreeNode node = treeCase.addNewAttSimple(pureOntological);
                if(node != null)
                    treeCase.setSelectionPath(new TreePath(node.getPath()));
			} else if (e.getSource() == btnAddAttComp) {
				// Add compound attribute.
				DefaultMutableTreeNode node = treeCase.addNewAttCompound();
                if(node != null)
                    treeCase.setSelectionPath(new TreePath(node.getPath()));
			} else if (e.getSource() == btnRemoveAtt) {
				// Remove attribute.
				if ((treeCase.getSelectionCount() > 0)
						&& treeCase.getSelNodeRemovable() && getConfirmation()) {
					treeCase.removeSelNode();
				}

			} else if (e.getSource() == btnAttApply) {
				// Apply changes in property values.
				updateSelNode();
			}
		}

		/** Ask for confirmation before removing an attribute from the tree. */
		private boolean getConfirmation() {
			String msj, title;
			String[] options = { "Yes", "No" };
			int res;

			msj = "Do you want to remove the selected attribute from the tree?";
			title = "Confirmation";
			res = JOptionPane.showOptionDialog(null, msj, title,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[1]);
			return (res == 0);
		}

		/**
		 * Update the value of the selected node in the tree with the new values
		 * in the panel of properties.
		 */
		private void updateSelNode() {
			CaseStComponent caseComp;
			PnlPropType pnlProp = null;

			// Look for the suitable properties panel.
			caseComp = treeCase.getSelCaseStComp();
			for (int i = 0; i < pnlPropTypes.length; i++) {
				if (pnlPropTypes[i].getCaseCompClassName() == caseComp
						.getClass().getName())
                {
					pnlProp = pnlPropTypes[i];
                    if((i==5) && pureOntological)
                    {
                        pnlProp = pnlPropTypes[7];
                    }
                }
			}
			try{
                if (pnlProp != null) {
                    caseComp = pnlProp.getCaseComp();
                    treeCase.updateSelCaseStComp(caseComp);
                    pnlProp.showCaseComp(caseComp);
                }                
            }catch(Exception e)
            {
                JOptionPane.showInternalMessageDialog(CBRArmGUI.getReference(),"Complete all attribute fields before applying changes","Error", JOptionPane.ERROR_MESSAGE);
                CBRLogger.log(this.getClass(),CBRLogger.ERROR, "Complete all attribute fields before applying changes");
            }

		}
	}
}
