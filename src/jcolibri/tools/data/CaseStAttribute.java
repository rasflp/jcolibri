package jcolibri.tools.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents the structure of a case attribute.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class CaseStAttribute extends CaseStComponent {

	/** Attribute name. */
	protected String name;

	/** Attribute weight. */
	protected float weight;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            attribute name.
	 * @param weight
	 *            attribute weight.
	 */
	public CaseStAttribute(String name, float weight) {
		super();
		this.name = name;
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the attribute name.
	 * 
	 * @param name
	 *            new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the attribute weight.
	 * 
	 * @return attribute weight.
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Sets the attribute weight.
	 * 
	 * @param weight
	 *            attribute weight.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
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
	 * @see jcolibri.tools.data.CaseStComponent#toXMLDOM(org.w3c.dom.Document)
	 */
	protected Element toXMLDOM(Document document) {
		Element elem;
		Node nodeAtt;

		elem = super.toXMLDOM(document);

		// Add name.
		nodeAtt = document.createAttribute("name");
		nodeAtt.setNodeValue(getName());
		elem.getAttributes().setNamedItem(nodeAtt);

		// Add weight.
		nodeAtt = document.createAttribute("weight");
		nodeAtt.setNodeValue(String.valueOf(weight));
		elem.getAttributes().setNamedItem(nodeAtt);

		return elem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#fromXMLDOM(org.w3c.dom.Element)
	 */
	protected void fromXMLDOM(Element elem) {
		name = elem.getAttribute("name");
		weight = Float.valueOf(elem.getAttribute("weight")).floatValue();
	}
}
