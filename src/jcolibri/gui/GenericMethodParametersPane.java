package jcolibri.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jcolibri.cbrcase.Connector;
import jcolibri.cbrcore.MethodParameter;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.extensions.DL.gui.editor.ConceptEditor;
import jcolibri.gui.editor.WeightEditor;

/**
 * 
 * @author Juan José Bello
 */
public class GenericMethodParametersPane {

	java.util.List requestedParameters;

	HashMap parametersToReturn;
    
    boolean showWeights;

	public final static int OK = 1;

	public final static int CANCEL = 2;

	private int value = 2;

	JDialog dialog;

	/** Creates new form GenericMethodParametersPane */
	public GenericMethodParametersPane(java.util.List requestedParameters,
			HashMap parametersToReturn) {
		this.requestedParameters = requestedParameters;
		this.parametersToReturn = parametersToReturn;
        showWeights = false;
	}
    
    /** Creates new form GenericMethodParametersPane */
    public GenericMethodParametersPane(java.util.List requestedParameters,
            HashMap parametersToReturn, boolean showWeights) {
        this.requestedParameters = requestedParameters;
        this.parametersToReturn = parametersToReturn;
        this.showWeights = showWeights;
    }

	public JDialog createJDialog(java.awt.Frame parent, boolean modal,
			String title) {
		JDialog dialog = new JDialog(parent, modal);
		this.dialog = dialog;
		dialog.setTitle(title);
		initComponents();
		myInitComponents();
		return dialog;
	}

	public JPanel createJDialog(JDialog parentDialog, boolean modal,
			String title) {
		this.dialog = parentDialog;
		this.dialog.setTitle(title);
		this.dialog.setModal(modal);
		initComponents();
		myInitComponents();
		return this.mainPanel;
	}

    
    private void myInitComponents() {
        ParameterEditorFactory.parent = this.dialog;
        GridBagLayout gridbag = new GridBagLayout();
        parametersPane.setFont(new Font("Helvetica", Font.PLAIN, 14));
        parametersPane.setLayout(gridbag);
        int i = 0;
        for (Iterator it = requestedParameters.iterator(); it.hasNext();) 
        {
            MethodParameter param = (MethodParameter) it.next();
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = i++;
            c.weightx = 1;
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
            JLabel theLabel = new JLabel(param.getName());
            theLabel.setToolTipText(param.getDescription());
            parametersPane.add(theLabel, c);
            c.gridx = 1;
            parametersPane.add(editor.getJComponent(), c);
            parametersToReturn.put(param.getName(), editor);
            //Esto es una pequeña chapucilla, habría que hacerlo de una
            //forma más elegante.
            // CAMBIO_LUIS
            if(showWeights){
                        c.gridx  = 2;
                        c.weightx = 1;
                        WeightEditor tf = new WeightEditor();
                        parametersPane.add(tf,c);
                        String key = param.getName()+"_ExtraWeight";
                        parametersToReturn.put(key, tf);
            }
            this.dialog.pack();
        }
    }

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		mainPanel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
		parametersPane = new javax.swing.JPanel();
		buttonsPanel = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();

		mainPanel.setLayout(new java.awt.BorderLayout());

		mainPanel.setBorder(new javax.swing.border.TitledBorder(
				"Requested parameters"));
		scrollPane.setViewportView(parametersPane);

		mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

		dialog.getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		okButton.setText("Ok");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		buttonsPanel.add(okButton);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		buttonsPanel.add(cancelButton);

		dialog.getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

		dialog.pack();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		dialog.setSize(new java.awt.Dimension(400, 400));
		dialog.setLocation((screenSize.width - 400) / 2,
				(screenSize.height - 400) / 2);
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		value = CANCEL;
		dialog.dispose();
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Set keys = parametersToReturn.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String name = (String) it.next();
			// switching components by its values
			parametersToReturn.put(name, ((ParameterEditor) parametersToReturn
					.get(name)).getEditorValue());
		}
		value = OK;
		dialog.dispose();
	}

	public int getValue() {
		return value;
	}

	private javax.swing.JPanel buttonsPanel;

	private javax.swing.JScrollPane scrollPane;

	private javax.swing.JButton okButton;

	private javax.swing.JPanel mainPanel;

	private javax.swing.JButton cancelButton;

	private javax.swing.JPanel parametersPane;

}
