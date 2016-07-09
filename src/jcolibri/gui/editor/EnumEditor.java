package jcolibri.gui.editor;

import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.datatypes.StringEnum;
import jcolibri.datatypes.StringEnumType;
import jcolibri.util.CBRLogger;

/**
 * @author Juan A. Recio-García
 * 
 */
public class EnumEditor extends JComboBox implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	private final String EMPTY = "<empty>";

	private StringEnumType enumType;

    /** 
     * Sets the configuration. The parameter is the name of the Enum datatype.
     */
	public void setConfig(Object config) {
		try {
            Object o = DataTypesRegistry.getInstance()
            .getDataType((String) config);
            if(o instanceof StringEnumType)
            {
    			enumType = (StringEnumType) o;
    			this.addItem(EMPTY);
    
    			for (Iterator it = enumType.getPossibleValues().iterator(); it
    					.hasNext();)
    				this.addItem(it.next());
            }
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Error setting EnumEditorConfig: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.gui.ParameterEditor#getEditorValue()
	 */
	public Object getEditorValue() {
		String value = (String) this.getSelectedItem();
		if (value.equals(EMPTY))
			return null;
		return new StringEnum(enumType, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.gui.ParameterEditor#getJComponent()
	 */
	public JComponent getJComponent() {
		return this;
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof String))
			return;
		String value = (String) defaultValue;
		this.setSelectedItem(value);
	}

}
