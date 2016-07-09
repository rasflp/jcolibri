package jcolibri.gui.editor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * 
 * @author Juan José Bello
 */
public class DateEditor extends JFormattedTextField implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of DateEditor */
	public DateEditor() {
		setValue(new Date());
	}

	public Object getEditorValue() {
		try {
			return DateFormat.getDateInstance().parse(getText());
		} catch (ParseException pe) {
			return null; // To do, check what to do
		}
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof Date))
			return;
		Date value = (Date) defaultValue;
		this.setValue(value);
	}

}
