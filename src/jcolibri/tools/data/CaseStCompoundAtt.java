package jcolibri.tools.data;

import java.util.List;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents the structure of a case compound attribute.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CaseStCompoundAtt extends CaseStAttribute {

	/** Global similarity. */
	private CBRGlobalSimilarity globalSim;

	/**
	 * Constructor.
	 */
	public CaseStCompoundAtt() {
		this("", 1, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            compound attribute name.
	 * @param weight
	 *            related weight.
	 * @param globalSim
	 *            related global similarity.
	 */
	public CaseStCompoundAtt(String name, float weight,
			CBRGlobalSimilarity globalSim) {
		super(name, weight);
		this.globalSim = globalSim;
	}

	/**
	 * Returns the global similarity function.
	 * 
	 * @return global similarity.
	 */
	public CBRGlobalSimilarity getGlobalSim() {
		return globalSim;
	}

	/**
	 * Sets the global similarity function.
	 * 
	 * @param globalSim
	 *            new global similarity.
	 */
	public void setGlobalSim(CBRGlobalSimilarity globalSim) {
		this.globalSim = globalSim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#getXMLTagName()
	 */
	public String getXMLTagName() {
		return "CompoundAttribute";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#fromXMLDOM(org.w3c.dom.Element)
	 */
	protected void fromXMLDOM(Element elem) {
		Element elemSimilParams, elemAtt;
		CaseStAttribute att;
		String globalSimName;
		List attElements;

		super.fromXMLDOM(elem);

		// Global similarity.
		globalSimName = elem.getAttribute("globalSim");
		globalSim = (CBRGlobalSimilarity) GlobalSimilarityRegistry
				.getInstance().getGlobalSimil(globalSimName).clone();

		// Similarity parameters.
		elemSimilParams = XMLUtil.getFirstChild(elem, "similParams");
		if (elemSimilParams != null)
			similParamsFromXMLDOM(elemSimilParams, globalSim);

		// Simple attributes.
		attElements = XMLUtil.getChild(elem, "SimpleAttribute");
		for (int i = 0; i < attElements.size(); i++) {
			elemAtt = (Element) attElements.get(i);
			att = new CaseStSimpleAtt();
			att.fromXMLDOM(elemAtt);
			addChild(att);
		}

        // Simple Concept attributes.
        attElements = XMLUtil.getChild(elem, "SimpleAttributeConcept");
        for (int i = 0; i < attElements.size(); i++) {
            elemAtt = (Element) attElements.get(i);
            att = new CaseStSimpleAttConcept();
            att.fromXMLDOM(elemAtt);
            addChild(att);
        }
        
		// Compound attributes.
		attElements = XMLUtil.getChild(elem, "CompoundAttribute");
		for (int i = 0; i < attElements.size(); i++) {
			elemAtt = (Element) attElements.get(i);
			att = new CaseStCompoundAtt();
			att.fromXMLDOM(elemAtt);
			addChild(att);
		}
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

		// Add localSim.
		nodeAtt = document.createAttribute("globalSim");
		nodeAtt.setNodeValue(globalSim.getName());
		elem.getAttributes().setNamedItem(nodeAtt);

		// Add globalSim parameters.
		elemSimilParams = similParamsToXMLDOM(document, globalSim);
		if (elemSimilParams != null)
			elem.appendChild(elemSimilParams);

		return elem;
	}
}
