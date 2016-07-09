package jcolibri.gui;

import javax.swing.JComponent;

/**
 * 
 * @author Juan José Bello
 */
public interface ParameterEditor {

    /*
     * Gets the Editor value
     */
	public Object getEditorValue();

    /*
     * Gets the Editor as a JComponent to be added to the panel
     */
	public JComponent getJComponent();

    /*
     * Sets configuration options for the editor. This method is called from ParameterEditorFactory
     */
	public void setConfig(Object config);

    /*
     * Sets the default value of the editor
     */
	public void setDefaultValue(Object defalutValue);
}
