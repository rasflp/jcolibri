package jcolibri.gui.editor;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * 
 * @author Juan José Bello
 */
public class IntegerEditor extends JFormattedTextField implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of IntegerEditor */
	public IntegerEditor() {
		setValue(new Integer(0));
	}

	public Object getEditorValue() {
		Object o = this.getValue();
		return o;
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof Integer))
			return;
		Integer value = (Integer) defaultValue;
		this.setValue(value);
	}

}
