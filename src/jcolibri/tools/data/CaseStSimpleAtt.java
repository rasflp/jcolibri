package jcolibri.tools.data;

import jcolibri.cbrcore.CBRLocalSimilarity;
import jcolibri.cbrcore.LocalSimilarityRegistry;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * RRepresents the structure of a simple attribute of a case.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CaseStSimpleAtt extends CaseStAttribute {

	/** Type name. */
	private String type;

	/** Local similarity. */
	private CBRLocalSimilarity localSim;

	/**
	 * Constructor.
	 */
	public CaseStSimpleAtt() {
		this("", null, 1, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            simple attribute name.
	 * @param type
	 *            simple attribute type.
	 * @param weight
	 *            related weight.
	 * @param localSim
	 *            related local similarity.
	 */
	public CaseStSimpleAtt(String name, String type, float weight,
			CBRLocalSimilarity localSim) {
		super(name, weight);
		this.type = type;
		this.localSim = localSim;
	}

	/**
	 * Returns the type name.
	 * 
	 * @return type name.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type name.
	 * 
	 * @param type
	 *            type name.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the local similarity.
	 * 
	 * @return local similarity.
	 */
	public CBRLocalSimilarity getLocalSim() {
		return localSim;
	}

	/**
	 * Sets the local similarity.
	 * 
	 * @param localSim
	 *            local similarity.
	 */
	public void setLocalSim(CBRLocalSimilarity localSim) {
		this.localSim = localSim;
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
	 * @see jcolibri.tools.data.CaseStComponent#getXMLTagName()
	 */
	public String getXMLTagName() {
		return "SimpleAttribute";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#fromXMLDOM(org.w3c.dom.Element)
	 */
	protected void fromXMLDOM(Element elem) {
		Element elemSimilParams;
		String localSimName;

		super.fromXMLDOM(elem);

		// Attributes.
		type = elem.getAttribute("type");
		localSimName = elem.getAttribute("localSim");
		localSim = (CBRLocalSimilarity) LocalSimilarityRegistry.getInstance()
				.getLocalSimil(localSimName).clone();

		// Similarity parameters.
		elemSimilParams = XMLUtil.getFirstChild(elem, "similParams");
		if (elemSimilParams != null)
			similParamsFromXMLDOM(elemSimilParams, localSim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#toXMLDOM(org.w3c.dom.Document)
	 */
	protected Element toXMLDOM(Document document) {
		Element elem, elemSimilParams;
		Node nodeAtt;

		elem = super.toXMLDOM(document);

		// Add type.
		nodeAtt = document.createAttribute("type");
		nodeAtt.setNodeValue(type);
		elem.getAttributes().setNamedItem(nodeAtt);

		// Add localSim.
		nodeAtt = document.createAttribute("localSim");
		nodeAtt.setNodeValue(localSim.getName());
		elem.getAttributes().setNamedItem(nodeAtt);

		// Add localSim parameters.
		elemSimilParams = similParamsToXMLDOM(document, localSim);
		if (elemSimilParams != null)
			elem.appendChild(elemSimilParams);

		return elem;
	}

}
