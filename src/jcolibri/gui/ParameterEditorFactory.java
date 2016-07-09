package jcolibri.gui;

import java.util.HashMap;

import javax.swing.JDialog;

import jcolibri.cbrcore.DataType;

/**
 * 
 * @author Juan José Bello
 */
public class ParameterEditorFactory {

	private static HashMap<String, String> table = new HashMap<String, String>();

	public static JDialog parent = null;

    /**
     * Creates the editor and configures it with its data-type name
     */
	public static ParameterEditor getEditor(String type) {
	    return getEditor(type, type);
	}

    /**
     * Creates the editor and configures it with its data-type name
     */
	public static ParameterEditor getEditor(DataType dtype) {
		return getEditor(dtype.getName(), dtype.getName());
	}
    
    /**
     * Creates the editor and configures it with the specified config object
     */
    public static ParameterEditor getEditor(String type, Object config) {
        String _class = null;
        ParameterEditor pe = null;
        try {
            _class = table.get(type);
            pe = (ParameterEditor) Class.forName(_class).newInstance();
            pe.setConfig(config);
            return pe;
        } catch (Exception e) {
            // CBRLogger.log("jcolibri.gui.ParameterEditorFactory","Error
            // creating parameterEditor instance: "+ _class+ "for type: "+ type,
            // e);
        }
        return pe;
    }

    /**
     * Creates the editor and configures it with the specified config object
     */
    public static ParameterEditor getEditor(DataType dtype, Object config) {
        return getEditor(dtype.getName(), config);
    }
    
    
	public static void registerEditor(String type, String _class) {
		table.put(type, _class);
	}

	public static void unregisterEditor(String type) {
		table.remove(type);
	}

	public static void clear() {
		table.clear();
	}
}
