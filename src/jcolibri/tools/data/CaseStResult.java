package jcolibri.tools.data;

import org.w3c.dom.Element;

/**
 * Represents the case result.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CaseStResult extends CaseStComponent {

	/** Name of the result component. */
	public static final String NAME = "Result";

	/** Name of the result relation. */
	public static final String RELATION_NAME = CaseStComponent.RelationPrefix
			+ NAME;

	/**
	 * Constructor.
	 */
	public CaseStResult() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#canHaveChildrens()
	 */
	public boolean canHaveChildrens() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#getName()
	 */
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#fromXMLDOM(org.w3c.dom.Element)
	 */
	protected void fromXMLDOM(Element elem) {

	}
}
