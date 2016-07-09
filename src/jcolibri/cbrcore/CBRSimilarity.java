package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jcolibri.similarity.SimilarityFunction;
import jcolibri.util.CBRLogger;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents the concept Similarity function of CBROnto.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class CBRSimilarity implements CBRTerm {

	/** Name of the similarity function. */
	protected String name;

	/** Name of the java class that implements the similarity function. */
	protected String className;

	/** Parameters of the function. */
	protected List<CBRSimilarityParam> parameters;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the similarity function.
	 * @param className
	 *            name of the java class that implements the function.
	 */
	public CBRSimilarity(String name, String className) {
		this.name = name;
		this.className = className;
		this.parameters = new ArrayList<CBRSimilarityParam>();
	}

	/**
	 * Returns the name of the similarity.
	 * 
	 * @return the name of the similarity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the similarity.
	 * 
	 * @param name
	 *            new name of the similarity.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the qualified name of the java class that implements the
	 * function.
	 * 
	 * @return the qualified name of the java class that implements the
	 *         function.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the qualified name of the class that implements the function.
	 * 
	 * @param className
	 *            qualified name of the class that implements the function.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the parameters of the similarity function.
	 * 
	 * @return parameters.
	 */
	public List<CBRSimilarityParam> getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters of the similarity function.
	 * 
	 * @param parameters
	 */
	public void setParameters(List<CBRSimilarityParam> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns a DOM node that represents this instance.
	 * 
	 * @param document
	 *            DOM document.
	 * @return DOM node thet represents this instance.
	 */
	public abstract Element toXMLDOM(Document document);

	/**
	 * Reads an instance from a DOM node.
	 * 
	 * @param node
	 *            DOM node.
	 */
	public void fromXMLDOM(Node node) {
		Element elemSim, elemParams, elemParam;
		Iterator it;
		String paramName, paramValue;

		elemSim = (Element) node;
		name = XMLUtil.getFirstChild(elemSim, "Name").getTextContent();
		className = XMLUtil.getFirstChild(elemSim, "ClassName")
				.getTextContent();

		// Parameters.
		parameters.clear();
		elemParams = XMLUtil.getFirstChild(elemSim, "Parameters");
		if (elemParams != null) {
			it = XMLUtil.getChild(elemParams, "Param").iterator();
			while (it.hasNext()) {
				elemParam = (Element) it.next();
				paramName = XMLUtil.getFirstChild(elemParam, "Name")
						.getTextContent();
				paramValue = XMLUtil.getFirstChild(elemParam, "Value")
						.getTextContent();
				parameters.add(new CBRSimilarityParam(paramName, paramValue));
			}
		}
	}

	/**
	 * Returns an instance of the java class that implements the function.
	 * 
	 * @return the SimilarityFunction that computes the similarity.
	 */
	public SimilarityFunction getSimilFunction() {
		Class cl;
		SimilarityFunction similFun;
		Iterator it;
		HashMap<String, Object> map;
		CBRSimilarityParam param;

		try {
			cl = Class.forName(className);
			similFun = (SimilarityFunction) cl.newInstance();

			// Add parameters.
			it = parameters.iterator();
			map = new HashMap<String, Object>();
			while (it.hasNext()) {
				param = (CBRSimilarityParam) it.next();
				map.put(param.getName(), param.getValue());
			}
			similFun.setParameters(map);

			return similFun;
		} catch (java.lang.ClassNotFoundException cnfe) {
			CBRLogger.log("jcolibri.tools.CBRSimilarity", "getFunctionClass",
					cnfe);
		} catch (java.lang.InstantiationException ine) {
			CBRLogger.log("jcolibri.core.CBRSimilarity", "getFunctionClass",
					ine);
		} catch (java.lang.IllegalAccessException ile) {
			CBRLogger.log("jcolibri.core.CBRSimilarity", "getFunctionClass",
					ile);
		}

		return null;
	}

	/**
	 * Fills a DOM node with the common data of all similarities: name,
	 * javaclass, parameters, ...
	 * 
	 * @param document
	 *            DOM document.
	 * @param elemSimil
	 *            similarity DOM node.
	 */
	protected void fillDOMSimilNode(Document document, Element elemSimil) {
		Element elemParameters, elemParam, elemAux;
		CBRSimilarityParam param;
		Iterator it;

		// <Name>.
		elemAux = document.createElement("Name");
		elemAux.setTextContent(getName());
		elemSimil.appendChild(elemAux);

		// <ClassName>.
		elemAux = document.createElement("ClassName");
		elemAux.setTextContent(getClassName());
		elemSimil.appendChild(elemAux);

		// <Parameters>
		elemParameters = document.createElement("Parameters");
		elemSimil.appendChild(elemParameters);
		it = parameters.iterator();
		while (it.hasNext()) {
			param = (CBRSimilarityParam) it.next();

			// <Parameters><Param>
			elemParam = document.createElement("Param");
			elemParameters.appendChild(elemParam);

			// <Parameters><Param><Name>
			elemAux = document.createElement("Name");
			elemAux.setTextContent(param.getName());
			elemParam.appendChild(elemAux);

			// <Parameters><Param><Name>
			elemAux = document.createElement("Value");
			elemAux.setTextContent(param.getValue());
			elemParam.appendChild(elemAux);
		}
	}
}
