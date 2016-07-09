package jcolibri.tools.data;

import java.util.List;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents the structure of the case solution.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CaseStSolution extends CaseStComponent {

	/** Name of the solution component. */
	public static final String NAME = "Solution";

	/** Name of the solution relation. */
	public static final String RELATION_NAME = CaseStComponent.RelationPrefix
			+ NAME;

	/** Global similarity. */
	private CBRGlobalSimilarity globalSim;

	/**
	 * Constructor.
	 */
	public CaseStSolution() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param globalSim
	 *            global similarity function.
	 */
	public CaseStSolution(CBRGlobalSimilarity globalSim) {
		super();
		this.globalSim = globalSim;
	}

	/**
	 * Returns the global similarity function.
	 * 
	 * @return global similarity function.
	 */
	public CBRGlobalSimilarity getGlobalSim() {
		return globalSim;
	}

	/**
	 * Sets the global similarity function.
	 * 
	 * @param globalSim
	 *            new global similarity function.
	 */
	public void setGlobalSim(CBRGlobalSimilarity globalSim) {
		this.globalSim = globalSim;
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
	 * @see jcolibri.tools.data.CaseStComponent#toXMLDOM(org.w3c.dom.Document)
	 */
	protected Element toXMLDOM(Document document) {
		Element elem, elemSimilParams;
		Node nodeAtt;

		elem = super.toXMLDOM(document);

		// Add globalSim.
		nodeAtt = document.createAttribute("globalSim");
		nodeAtt.setNodeValue(globalSim.getName());
		elem.getAttributes().setNamedItem(nodeAtt);

		// Add global similarity parameters.
		elemSimilParams = similParamsToXMLDOM(document, globalSim);
		if (elemSimilParams != null)
			elem.appendChild(elemSimilParams);

		return elem;
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
}
