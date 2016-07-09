package jcolibri.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.MethodParameter;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStructure;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;

/**
 * 
 * @author Juan José Bello
 */
public class GenericMultipleCasesEditPane extends JDialog {
	private static final long serialVersionUID = 1L;

	java.util.List cases;

	CaseStructure caseStructure;

	CBRCase currentCase;

	int currentCasePosition;

	HashMap editingParameters;

	/** Creates new form GenericMethodParametersPane */
	public GenericMultipleCasesEditPane(java.util.List _cases,
			CaseStructure _caseStructure) {
		super(CBRArmGUI.getReference(), "Revision", true);

		this.cases = _cases;
		this.caseStructure = _caseStructure;
		this.currentCasePosition = 0;

		this.initComponents();
		loadCase();

	}

	private void loadCase() {
		this.currentCase = (CBRCase) cases.get(currentCasePosition);
		ArrayList attributesList = new ArrayList();
		ArrayList valuesList = new ArrayList();
		extractSimpleAttributes(this.currentCase, this.caseStructure,
				attributesList, valuesList);
		this.editingParameters = myInitComponents(attributesList, valuesList);
	}

	private void saveCase() {
		for (Iterator iter = this.editingParameters.keySet().iterator(); iter
				.hasNext();) {
			String attName = (String) iter.next();
			Individual ind = CaseCreatorUtils.getAttribute(this.currentCase,
					attName);
			ParameterEditor pe = (ParameterEditor) this.editingParameters
					.get(attName);
			ind.setValue(pe.getEditorValue());
		}
		CBRLogger.log(this.getClass(), CBRLogger.INFO,
				"Saving changes to case: "
						+ this.currentCase.getValue().toString());
	}

	private void extractSimpleAttributes(CBRCase _case, CaseStComponent cc,
			java.util.List attributesList, java.util.List valuesList) {
		Vector childs = new Vector();
		cc.getSubcomponents(childs);

		for (Iterator iter = childs.iterator(); iter.hasNext();) {
			CaseStComponent child = (CaseStComponent) iter.next();
			if (child instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) child;
                
                DataType dataType = DataTypesRegistry.getInstance().getDataType(
                        sa.getType());
                MethodParameter mp = new MethodParameter(sa.getName(), sa.getPathWithoutCase()
                        , dataType);

				/* CHANGED V1.1
				 MethodParameter mp = new MethodParameter(sa.getName(), sa
						.getPathWithoutCase(), new DataType(sa.getType()));
				*/
                attributesList.add(mp);

				Individual ind = CaseCreatorUtils.getAttribute(_case, sa
						.getPathWithoutCase());
				valuesList.add(ind.getValue());
			}
		}
	}

	private HashMap myInitComponents(java.util.List attributesList,
			java.util.List defaultValuesList) {
		ParameterEditorFactory.parent = this;
		HashMap parametersToReturn = new HashMap();

		parametersPane.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		parametersPane.setFont(new Font("Helvetica", Font.PLAIN, 14));
		parametersPane.setLayout(gridbag);
		int i = 0;
		Iterator dvIter = defaultValuesList.iterator();
		for (Iterator it = attributesList.iterator(); it.hasNext();) {
			MethodParameter param = (MethodParameter) it.next();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = i++;
			c.weightx = 0.3;
			c.weighty = 0.1;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.insets = new java.awt.Insets(5, 5, 5, 5);

            ParameterEditor editor = null;
            try
            {
                if(param.getType().getJavaClass().equals(ConceptType.class))
                    editor = ParameterEditorFactory.getEditor(param.getType(), "Description."+param.getName());
                else
                    editor = ParameterEditorFactory.getEditor(param.getType());
            } catch (ClassNotFoundException e)
            {
                editor = ParameterEditorFactory.getEditor(param.getType());
            }

			editor.setDefaultValue(dvIter.next());

			JLabel theLabel = new JLabel(param.getName());
			theLabel.setToolTipText(param.getDescription());
			parametersPane.add(theLabel, c);
			c.gridx = 1;

			parametersPane.add(editor.getJComponent(), c);
			parametersToReturn.put(param.getDescription(), editor);

			this.okButton.setText("Save: "
					+ this.currentCase.getValue().toString());
			this.titledBorder.setTitle(this.currentCase.getValue().toString()
					+ " (" + (this.currentCasePosition + 1) + "/"
					+ this.cases.size() + ")");
			mainPanel.setBorder(titledBorder);
			mainPanel.repaint();
		}
		return parametersToReturn;

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		mainPanel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
		parametersPane = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		casesPanel = new javax.swing.JPanel();
		nextButton = new javax.swing.JButton();
		previousButton = new javax.swing.JButton();

		mainPanel.setLayout(new java.awt.BorderLayout());
		titledBorder = new javax.swing.border.TitledBorder("Case Attributes");
		mainPanel.setBorder(titledBorder);
		scrollPane.setViewportView(parametersPane);
		mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		okButton.setText("Save");
		okButton.setPreferredSize(new Dimension(250, 30));
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});
		nextButton.setText(">>");
		nextButton.setPreferredSize(new Dimension(60, 30));
		nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});
		previousButton.setText("<<");
		previousButton.setPreferredSize(new Dimension(60, 30));
		previousButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previousButtonActionPerformed(evt);
			}
		});
		caseIDLabel = new JLabel("CaseID");
		caseIDLabel.setPreferredSize(new Dimension(130, 30));
		caseIDLabel.setHorizontalAlignment(JLabel.CENTER);

		casesPanel.setFont(new Font("Helvetica", Font.PLAIN, 11));
		parametersPane.setLayout(new java.awt.BorderLayout());
		casesPanel.add(previousButton, java.awt.BorderLayout.WEST);
		casesPanel.add(okButton, BorderLayout.CENTER);
		// casesPanel.add(caseIDLabel, BorderLayout.SOUTH);
		casesPanel.add(nextButton, BorderLayout.EAST);

		getContentPane().add(casesPanel, java.awt.BorderLayout.SOUTH);

		pack();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setSize(new java.awt.Dimension(400, 500));
		setLocation((screenSize.width - 400) / 2, (screenSize.height - 500) / 2);

		this.previousButton.setEnabled(false);
		if (cases.size() == 1)
			this.nextButton.setEnabled(false);
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		saveCase();
	}

	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.currentCasePosition++;

		if (this.currentCasePosition + 1 == this.cases.size())
			this.nextButton.setEnabled(false);
		if (this.currentCasePosition > 0)
			this.previousButton.setEnabled(true);

		loadCase();
	}

	private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.currentCasePosition--;

		if (this.currentCasePosition + 1 < this.cases.size())
			this.nextButton.setEnabled(true);
		if (this.currentCasePosition == 0)
			this.previousButton.setEnabled(false);

		loadCase();
	}

	private javax.swing.JScrollPane scrollPane;

	private javax.swing.JButton okButton;

	private javax.swing.JPanel mainPanel;

	private javax.swing.JPanel parametersPane;

	private javax.swing.JPanel casesPanel;

	private javax.swing.JButton nextButton;

	private javax.swing.JButton previousButton;

	private JLabel caseIDLabel;

	javax.swing.border.TitledBorder titledBorder;

	public static void main(String args[]) {
		// GenericMultipleCasesEditPane ep = new
		// GenericMultipleCasesEditPane(new ArrayList());
		// ep.setVisible(true);
	}

}
