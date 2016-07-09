package jcolibri.gui.editor;

import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * 
 * @author Juan José Bello
 */
public class DoubleEditor extends JFormattedTextField implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of DoubleEditor */
	public DoubleEditor() {
		setValue(new Double(0));
    	
		// ##Fixed by rasflp@gmail.com on Sep 7, 2016
		DecimalFormat dFormat = new DecimalFormat("#,###,###.00") ;
        NumberFormatter formatter = new NumberFormatter(dFormat) ;
        formatter.setFormat(dFormat) ;
        formatter.setAllowsInvalid(false) ; 

        setFormatterFactory ( new DefaultFormatterFactory ( formatter ) ) ;		
	}

	public Object getEditorValue() {

		// ##Fixed by rasflp@gmail.com on Sep 7, 2016
		return Double.parseDouble(getText().replaceAll("\\.","").replace(",","."));
		
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof Double))
			return;
		Double value = (Double) defaultValue;
		this.setValue(value);
	}
}
