package jcolibri.tools.gui.casestruct;

import javax.swing.JPanel;

import jcolibri.tools.data.CaseStComponent;

/**
 * All the panels that show case component properties must extend this class.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class PnlPropType extends JPanel {

	/**
	 * Returns the case component class name that the panel can manage.
	 * 
	 * @return
	 */
	public abstract String getCaseCompClassName();

	/**
	 * Shows the values of a case component.
	 * 
	 * @param comp
	 */
	public abstract void showCaseComp(CaseStComponent comp);

	/**
	 * Returns a case component using the values introduced by the user.
	 * 
	 * @return
	 */
	public abstract CaseStComponent getCaseComp();
}
