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
public class CaseStSimpleAttConcept extends CaseStAttribute {

	/** Type name. */
	private String type;
    
    private String relation;

	/** Local similarity. */
	private CBRLocalSimilarity localSim;

	/**
	 * Constructor.
	 */
	public CaseStSimpleAttConcept() {
		this("", null, 1, null, "");
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
     * @param relation
     *            relation atrribute name
	 */
	public CaseStSimpleAttConcept(String name, String type, float weight,
			CBRLocalSimilarity localSim, String relation) {
		super(name, weight);
		this.type = type;
		this.localSim = localSim;
        this.relation = relation;
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
		return "SimpleAttributeConcept";
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
        relation = elem.getAttribute("relation");
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

        // Add relation 
        nodeAtt = document.createAttribute("relation");
        nodeAtt.setNodeValue(relation);
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

    public String getRelation()
    {
        return relation;
    }
    
    /**
     * Return the relation path of this component.
     * 
     * @return relation path.
     */
    public String getRelationPath() {
        return relation;
    }

    public void setRelation(String relation)
    {
        this.relation = relation;
    }

}
