package jcolibri.cbrcore;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents the concept Local Similarity Function of CBROnto.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CBRLocalSimilarity extends CBRSimilarity {

	/** Possible types of the items that are going to be compared. */
	private Set<DataType> attributes;

	/**
	 * Constructor.
	 */
	public CBRLocalSimilarity() {
		this("", "");
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the similarity.
	 * @param className
	 *            name of the java class that implements the similarity
	 *            funciton.
	 */
	public CBRLocalSimilarity(String name, String className) {
		super(name, className);
		this.attributes = new HashSet<DataType>();
	}

	/**
	 * Returns the possible types of the items that are going to be compared.
	 * 
	 * @return possible types of the items that are going to be compared.
	 */
	public Set<DataType> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the possible types of the items that are going to be compared.
	 * 
	 * @param attributes
	 *            possible types of the items that are going to be compared.
	 */
	public void setAttributes(Set<DataType> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns if this function can be applied to data of type dataType.
	 * 
	 * @param dataType
	 *            type of the data.
	 * @return true if this function can be applied to this kind of data.
	 */
	public boolean isCompatible(DataType dataType) {
		DataType dt;
		Iterator<DataType> it;
		boolean isCompatible = false;

		try {
			it = attributes.iterator();
			while (it.hasNext() && !isCompatible) {
				dt = it.next();
				if (isAntecesor(dt.getJavaClass(), dataType.getJavaClass()))
					isCompatible = true;

			}
			return isCompatible;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns a node of a DOM tree that represents this instance.
	 * 
	 * @param document
	 *            DOM document.
	 * @return node of the DOM tree.
	 */
	public Element toXMLDOM(Document document) {
		Element elemLocalSim, elemParams, elemAux, elemAttributes;
		Iterator it;
		DataType dataType;

		elemLocalSim = document.createElement("LocalSim");
		fillDOMSimilNode(document, elemLocalSim);

		// <Attributes>.
		elemAttributes = document.createElement("Attributes");
		elemParams = XMLUtil.getFirstChild(elemLocalSim, "Parameters");
		elemLocalSim.insertBefore(elemAttributes, elemParams);
		it = attributes.iterator();
		while (it.hasNext()) {
			dataType = (DataType) it.next();

			elemAux = document.createElement("Attribute");
			elemAux.setTextContent(dataType.getName());
			elemAttributes.appendChild(elemAux);
		}

		return elemLocalSim;
	}

	/**
	 * Reads an instance from a node of a DOM tree.
	 * 
	 * @param nod
	 *            node of the DOM tree.
	 */
	public void fromXMLDOM(Node node) {
		Element elemLocalSim, elemAttributes, elemAtt;
		Iterator it;
		DataTypesRegistry dataTypeReg;
		DataType dataType;

		super.fromXMLDOM(node);

		elemLocalSim = (Element) node;
		elemAttributes = XMLUtil.getFirstChild(elemLocalSim, "Attributes");
		it = XMLUtil.getChild(elemAttributes, "Attribute").iterator();
		dataTypeReg = DataTypesRegistry.getInstance();
		while (it.hasNext()) {
			elemAtt = (Element) it.next();
			dataType = dataTypeReg.getDataType(elemAtt.getTextContent());
			if (dataType != null)
				attributes.add(dataType);
		}
	}

	/**
	 * Retuns a copy of this instance.
	 * 
	 * @return a copy of this instance.
	 */
	public Object clone() {
		CBRLocalSimilarity lSim = new CBRLocalSimilarity(name, className);
		lSim.getAttributes().addAll(getAttributes());
		lSim.getParameters().addAll(getParameters());
		return lSim;
	}

	/**
	 * Retuns if father is an antecesor of child in the java class hieraechy.
	 * 
	 * @return true if father is an antecesor of child in the java class
	 *         hieraechy.
	 */
	private boolean isAntecesor(Class father, Class child) {
		Class classObj = null;

		try {
			classObj = Class.forName("java.lang.Object");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while ((child != father) && (child != classObj)) {
			child = child.getSuperclass();
		}
		return (child == father);
	}
}
