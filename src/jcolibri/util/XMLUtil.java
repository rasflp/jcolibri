package jcolibri.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities to help when working with XML data.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class XMLUtil {

	/**
	 * Returns the children nodes of elem with a specific name.
	 * 
	 * @param elem
	 *            parent node.
	 * @param childName
	 *            name of the wanted children.
	 * @return list of nodes.
	 */
	public static List<Element> getChild(Element elem, String childName) {
		NodeList childNodes;
		Node node;
		ArrayList<Element> res;

		res = new ArrayList<Element>();
		childNodes = elem.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			node = childNodes.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == childName)
					res.add((Element)node);
			}
		}
		return res;
	}

	/**
	 * Returns the first child node of elem with a specific name.
	 * 
	 * @param elem
	 *            parent node.
	 * @param childName
	 *            name of the wanted child.
	 * @return the first child of elem with that name.
	 */
	public static Element getFirstChild(Element elem, String childName) {
		NodeList childNodes;
		Node node;

		childNodes = elem.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			node = childNodes.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == childName)
					return (Element) node;
			}
		}
		return null;
	}

	/**
	 * Writes a DOM document to a file.
	 * 
	 * @param document
	 *            DOM document.
	 * @param fileName
	 *            target file path.
	 */
	public static void writeDOMToFile(Document document, String fileName) {
		FileOutputStream fos;
		Source source;
		Result result;
		Transformer xformer;

		try {
			fos = new FileOutputStream(fileName);
			source = new DOMSource(document);
			result = new StreamResult(fos);
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
