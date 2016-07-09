package jcolibri.gui.editor;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * 
 * @author Juan José Bello
 */
public class BooleanEditor extends JCheckBox implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of BooleanEditor */
	public BooleanEditor() {
		addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BooleanEditor be = (BooleanEditor) evt.getSource();
				if (be.isSelected())
					be.setText(" true ");
				else
					be.setText(" false");
			}
		});
		this.setText("false");

	}

	public Object getEditorValue() {
		return new Boolean(isSelected());
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof Boolean))
			return;
		Boolean value = (Boolean) defaultValue;
		this.setSelected(value.booleanValue());
	}
}
