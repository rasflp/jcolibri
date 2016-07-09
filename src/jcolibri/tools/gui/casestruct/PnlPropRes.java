package jcolibri.tools.gui.casestruct;

import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStResult;

/**
 * Panel to manage the properties of the result.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlPropRes extends PnlPropType {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PnlPropRes() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#showCaseComp(jcolibri.tools.data.CaseStComponent)
	 */
	public void showCaseComp(CaseStComponent comp) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseComp()
	 */
	public CaseStComponent getCaseComp() {
		return new CaseStResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.casestruct.PnlPropType#getCaseCompClassName()
	 */
	public String getCaseCompClassName() {
		return CaseStResult.class.getName();
	}
}
