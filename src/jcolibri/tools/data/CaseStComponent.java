package jcolibri.tools.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import jcolibri.cbrcore.CBRSimilarity;
import jcolibri.cbrcore.CBRSimilarityParam;
import jcolibri.cbrcore.CBRTerm;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The structure of a case is represented using a tree. Every node of that tree
 * (i.e. every component of the case structure) must inherit from this abstract
 * class: case, description, solution, result, attributes, etc.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class CaseStComponent implements CBRTerm {

	/** Parent case component. */
	protected CaseStComponent parent;

	/** Children case component. */
	protected Vector<CaseStComponent> childrens;

	/** Separator for bulding paths. A path identifies a component of the tree. */
	private static String pathSeparator = ".";

	/** Prefix for building relation names. */
	public static final String RelationPrefix = "has-";

	/**
	 * Returns the separator for building paths.
	 * 
	 * @return separator for building paths.
	 */
	public static String getPathSeparator() {
		return pathSeparator;
	}

	/**
	 * Sets the separator used for building paths.
	 * 
	 * @param pathSeparator
	 *            new path separator.
	 */
	public static void setPathSeparator(String pathSeparator) {
		CaseStComponent.pathSeparator = pathSeparator;
	}

	/**
	 * Constructor.
	 */
	public CaseStComponent() {
		parent = null;
		childrens = new Vector<CaseStComponent>(2);
	}

	/**
	 * Returns the name of the component.
	 * 
	 * @return return the component name.
	 */
	public abstract String getName();

	/**
	 * Returns the path of this component. The path is built using the names of
	 * the components in the branch containing this component. The path
	 * identifies the component.
	 * 
	 * @return component path.
	 */
	public String getPath() {
		String path = "";

		if (parent != null)
			path = parent.getPath() + pathSeparator;
		return path + getName();
	}

	/**
	 * Returns the path of this component without the name of the case.
	 * 
	 * @return component path without the case name.
	 */
	public String getPathWithoutCase() {
		String path = getPath();
		return path.substring(path.indexOf(pathSeparator) + 1);
	}

	/**
	 * Return the relation path of this component.
	 * 
	 * @return relation path.
	 */
	public String getRelationPath() {
		return RelationPrefix + getPathWithoutCase();
	}

	/**
	 * Returns the component identified by the path. If the path does not
	 * identify a component in this brach returns null.
	 * 
	 * @param path
	 *            component path.
	 * @return case component or null.
	 */
	public CaseStComponent getComponentByPath(String path) {
		String[] tokens;
		String subPath;
		CaseStComponent comp;

		// Break the path in tokens.
		tokens = path.split(Pattern.quote(pathSeparator));
		if (tokens.length == 0)
			return null;

		// First token must be this component name.
		if (!tokens[0].equals(getName()))
			return null;

		if (tokens.length == 1)
			return this;

		// Look for the children.
		for (int i = 0; i < childrens.size(); i++) {
			comp = (CaseStComponent) childrens.elementAt(i);
			if (tokens[1].equals(comp.getName())) {
				subPath = path.substring(tokens[0].length()
						+ pathSeparator.length());
				return comp.getComponentByPath(subPath);
			}
		}
		return null;
	}

	/**
	 * Returns the parent component in the tree. If the component has not parent
	 * returns null.
	 * 
	 * @return parent component.
	 */
	public CaseStComponent getParent() {
		return parent;
	}

	/**
	 * Sets the parent component in the tree. If the component has not parent
	 * returns null.
	 * 
	 * @param parent
	 */
	public void setParent(CaseStComponent parent) {
		this.parent = parent;
	}

	/**
	 * Returns the numbers of childrens.
	 * 
	 * @return number of children components.
	 */
	public int getNumChildrens() {
		return childrens.size();
	}

	/**
	 * Returns a child case component by index .
	 * 
	 * @param i
	 *            index
	 * @return child case component.
	 */
	public CaseStComponent getChild(int i) {
		return (CaseStComponent) childrens.elementAt(i);
	}

	/**
	 * Adds a new child component.
	 * 
	 * @param comp
	 *            new child case component.
	 */
	public void addChild(CaseStComponent comp) {
		childrens.add(comp);
		comp.setParent(this);
	}

	/**
	 * Replaces a child case component by index.
	 * 
	 * @param i
	 *            index
	 * @param comp
	 *            new case component.
	 */
	public void setChild(int i, CaseStComponent comp) {
		getChild(i).setParent(null);
		childrens.setElementAt(comp, i);
		comp.setParent(this);
	}

	/**
	 * Remove a child case component by index.
	 * 
	 * @param i
	 *            index
	 */
	public void removeChild(int i) {
		CaseStComponent comp = (CaseStComponent) childrens.elementAt(i);
		comp.setParent(null);
		childrens.remove(i);
	}

	/**
	 * Remove all children case components.
	 * 
	 */
	public void removeAllChildrens() {
		int num;

		num = getNumChildrens();
		for (int i = num - 1; i >= 0; i--)
			removeChild(i);
	}

	/**
	 * Returns if this case component can have children in the tree.
	 * 
	 * @return if this case component can have children in the tree.
	 */
	public boolean canHaveChildrens() {
		return true;
	}

	/**
	 * Adds to v this component and all its subcomponents at any level of depth.
	 * 
	 * @param v
	 *            vector.
	 */
	public void getSubcomponents(Vector<CaseStComponent> v) {
		CaseStComponent comp;

		v.add(this);
		for (int i = 0; i < childrens.size(); i++) {
			comp = (CaseStComponent) childrens.elementAt(i);
			comp.getSubcomponents(v);
		}
	}

	/**
	 * Returns the name of the tag that represents this component in XML.
	 * 
	 * @return XML tag name.
	 */
	public String getXMLTagName() {
		return getName();
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
		for (int i = 0; i < childrens.size(); i++) {
			caseComp = (CaseStComponent) childrens.elementAt(i);
			elem.appendChild(caseComp.toXMLDOM(document));
		}

		return elem;
	}

	/**
	 * Unserializes this component from a XML node of a DOM tree.
	 * 
	 * @param elem
	 *            XML node.
	 */
	protected abstract void fromXMLDOM(Element elem);

	/**
	 * Serializes the parameters of a similarity as a XML node.
	 * 
	 * @param document
	 *            DOM document.
	 * @param simil
	 *            similarity function.
	 * @return DOM node.
	 */
	protected Element similParamsToXMLDOM(Document document, CBRSimilarity simil) {
		Element elemParams, elemParam;
		CBRSimilarityParam similParam;
		Node nodeAtt;
		Iterator it;

		// Add localSim parameters.
		it = simil.getParameters().iterator();
		if (it.hasNext()) {
			elemParams = document.createElement("similParams");
			while (it.hasNext()) {
				similParam = (CBRSimilarityParam) it.next();
				elemParam = document.createElement("param");

				nodeAtt = document.createAttribute("name");
				nodeAtt.setNodeValue(similParam.getName());
				elemParam.getAttributes().setNamedItem(nodeAtt);

				nodeAtt = document.createAttribute("value");
				nodeAtt.setNodeValue(similParam.getValue());
				elemParam.getAttributes().setNamedItem(nodeAtt);

				elemParams.appendChild(elemParam);
			}
		} else {
			elemParams = null;
		}

		return elemParams;
	}

	/**
	 * Unserializes the parameters of a similarity function from a DOM node.
	 * 
	 * @param elem
	 *            DOM node.
	 * @param simil
	 *            similarity function.
	 */
	protected void similParamsFromXMLDOM(Element elem, CBRSimilarity simil) {
		Element paramNode;
		List elemParams;
        List<CBRSimilarityParam> params;
		CBRSimilarityParam simParam;

		params = new ArrayList<CBRSimilarityParam>();
		elemParams = XMLUtil.getChild(elem, "param");
		for (int i = 0; i < elemParams.size(); i++) {
			paramNode = (Element) elemParams.get(i);
			simParam = new CBRSimilarityParam(paramNode.getAttribute("name"),
					paramNode.getAttribute("value"));
			params.add(simParam);
		}

		simil.setParameters(params);
	}
}
