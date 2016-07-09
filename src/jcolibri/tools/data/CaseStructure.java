package jcolibri.tools.data;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.extensions.DL.util.OntoBridgeSingleton;
import jcolibri.util.CBRLogger;
import jcolibri.util.FileUtil;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Represents the structure of a case (it's a tree).
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 * 
 */
public class CaseStructure extends CaseStComponent {

    private String concept;
    public String getConcept()
    {
        return concept;
    }
    public void setConcept(String concept)
    {
        this.concept = concept;
    }
    
    
	/**
	 * Constructor.
	 */
	public CaseStructure() {
		super();

		CBRGlobalSimilarity globalSim;

		globalSim = GlobalSimilarityRegistry.getInstance().getDefGlobalSimil();
		addChild(new CaseStDescription(globalSim));
		addChild(new CaseStSolution(globalSim));
		addChild(new CaseStResult());
	}

	/**
	 * Returns the case description.
	 * 
	 * @return case description.
	 */
	public CaseStDescription getDescription() {
		return (CaseStDescription) getChild(0);
	}

	/**
	 * Sets the case description.
	 * 
	 * @param descr
	 *            case description.
	 */
	public void setDescription(CaseStDescription descr) {
		setChild(0, descr);
	}

	/**
	 * Returns the case solution.
	 * 
	 * @return case solution.
	 */
	public CaseStSolution getSolution() {
		return (CaseStSolution) getChild(1);
	}

	/**
	 * Sets the case solution.
	 * 
	 * @param sol
	 *            case solution.
	 */
	public void setSolution(CaseStSolution sol) {
		setChild(1, sol);
	}

	/**
	 * Returns the case result.
	 * 
	 * @return case result.
	 */
	public CaseStResult getResult() {
		return (CaseStResult) getChild(2);
	}

	/**
	 * Sets the case result.
	 * 
	 * @param result
	 *            case result.
	 */
	public void setResult(CaseStResult result) {
		setChild(2, result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#getName()
	 */
	public String getName() {
		return "Case";
	}

	/**
	 * Serializes this case structure to a XML file.
	 * 
	 * @param fileName
	 *            file path.
	 */
	public void writeToXMLFile(String fileName) {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;

		try {
			// Create document.
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// Create DOM tree.
			document.appendChild(toXMLDOM(document));


			// Write to file.
			FileUtil.createFileBackup(fileName);
			XMLUtil.writeDOMToFile(document, fileName);

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.PnlDefConnectorSQL", "createDOMTree",
					pce);
		}
	}
    
    /**
     * Serializes this component as a XML node of a DOM tree.
     * 
     * @param document
     *            CML document.
     * @return XML node.
     */
    protected Element toXMLDOM(Document document) {

        Element elem;
        CaseStComponent caseComp;

        elem = document.createElement(getXMLTagName());
        if(concept!=null)
        {
            Node nodeAtt = document.createAttribute("concept");
            nodeAtt.setNodeValue(concept);
            elem.getAttributes().setNamedItem(nodeAtt);
        }
        for (int i = 0; i < childrens.size(); i++) {
            caseComp = (CaseStComponent) childrens.elementAt(i);
            elem.appendChild(caseComp.toXMLDOM(document));
        }
        if(usesOntoBridge())
            elem.appendChild(OntoBridgeSingleton.toXMLDOM(document));
        return elem;
    }

	/**
	 * Unserializes a case structure from a XML file.
	 * 
	 * @param fileName
	 *            file path.
	 */
	public void readFromXMLFile(String fileName) {
		DocumentBuilder db;
		Document doc;

		try {
			// Read and parse the file.
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(fileName);

			// Build the case structure.
			fromXMLDOM((Element) doc.getElementsByTagName("Case").item(0));

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.data.CaseStComponent#fromXMLDOM(org.w3c.dom.Element)
	 */
	protected void fromXMLDOM(Element elem) {
        this.concept = elem.getAttribute("concept");
        if(this.concept != null)
            if(this.concept.length()==0)
                this.concept = null;
		getDescription().fromXMLDOM((Element) elem.getChildNodes().item(0));
		getSolution().fromXMLDOM((Element) elem.getChildNodes().item(1));
		getResult().fromXMLDOM((Element) elem.getChildNodes().item(2));
        NodeList nl = elem.getElementsByTagName("Reasoner");
        if(nl.getLength()>0)
            OntoBridgeSingleton.fromXMLDOM((Element)elem.getElementsByTagName("Reasoner").item(0));
    }
    
    public boolean usesOntoBridge()
    {
        if(concept != null)
            return true;
        else if  (usesOntoBridge(this.getDescription()))
            return true;
        else if  (usesOntoBridge(this.getSolution()))
            return true;
        else if  (usesOntoBridge(this.getResult()))
            return true;
        else
            return false;
    }
    private boolean usesOntoBridge(CaseStComponent comp)
    {
        if(comp instanceof CaseStSimpleAttConcept)
            return true;
        else if(comp instanceof CaseStSimpleAtt)
        {
            CaseStSimpleAtt simple = (CaseStSimpleAtt) comp;
            if(simple.getType().equals("Concept"))
                return true;
        }
        else
        {
            for(int i = 0; i<comp.getNumChildrens(); i++)
                if(usesOntoBridge(comp.getChild(i)))
                    return true;
        }
        return false;
    }
    
}
