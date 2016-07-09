package jcolibri.gui.editor;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * 
 * @author Juan José Bello
 */
public class StringEditor extends JTextField implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of StringEditor */
	public StringEditor() {
        this.setMaximumSize(new Dimension(50,20));
	}

	public Object getEditorValue() {
		return getText();
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof String))
			return;
		String value = (String) defaultValue;
		this.setText(value);
	}

}
