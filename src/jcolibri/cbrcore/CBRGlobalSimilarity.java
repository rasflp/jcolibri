package jcolibri.cbrcore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents the concept Global Similarity Function of CBROnto.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class CBRGlobalSimilarity extends CBRSimilarity {

	/**
	 * Constructor.
	 */
	public CBRGlobalSimilarity() {
		this("", "");
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the global similarity function.
	 * @param name
	 *            of the java class that implements the function.
	 */
	public CBRGlobalSimilarity(String name, String className) {
		super(name, className);
	}

	/**
	 * Returns a node of a DOM tree that represents this instance.
	 * 
	 * @param document
	 *            DOM document.
	 * @return node of the DOM tree.
	 */
	public Element toXMLDOM(Document document) {
		Element elemGlobalSim;

		elemGlobalSim = document.createElement("GlobalSim");
		fillDOMSimilNode(document, elemGlobalSim);

		return elemGlobalSim;
	}

	/**
	 * Retuns a copy of this instance.
	 * 
	 * @return a copy of this instance.
	 */
	public Object clone() {
		CBRGlobalSimilarity sim = new CBRGlobalSimilarity(name, className);
		sim.getParameters().addAll(getParameters());
		return sim;
	}
}
