package jcolibri.cbrcore;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.util.CBRLogger;
import jcolibri.util.FileUtil;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Manager of the local similarities of a package.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class LocalSimilPkgReg {

	/** Local similarities indexed by a hash table. */
	private HashMap<String,CBRLocalSimilarity> similsHM;

	/** Local similarities indexed by a vector. */
	private Vector<CBRLocalSimilarity> similsV;

	/** Package. */
	private PackageInfo pkgInfo;

	/**
	 * Constructor.
	 * 
	 * @param pkgInfo
	 *            package.
	 */
	public LocalSimilPkgReg(PackageInfo pkgInfo) {
		similsHM = new HashMap<String,CBRLocalSimilarity>();
		similsV = new Vector<CBRLocalSimilarity>();
		this.pkgInfo = pkgInfo;
	}

	/**
	 * Returns the package.
	 * 
	 * @return the package.
	 */
	public PackageInfo getPackageInfo() {
		return pkgInfo;
	}

	/**
	 * Returns the number of local similarities.
	 * 
	 * @return the number of local similarities.
	 */
	public int getNumLocalSimils() {
		return similsV.size();
	}

	/**
	 * Returns all the local similarities.
	 * 
	 * @return all the local similarities
	 */
	public Collection<CBRLocalSimilarity> getLocalSimils() {
		return similsV;
	}

	/**
	 * Returns a local similarity by name.
	 * 
	 * @param name
	 *            name of the local similarity.
	 * @return the local similarity.
	 */
	public CBRLocalSimilarity getLocalSimil(String name) {
		return similsHM.get(name);
	}

	/**
	 * Returns a local similarity by index.
	 * 
	 * @param idx
	 *            index.
	 * @return local similarity or null.
	 */
	public CBRLocalSimilarity getLocalSimil(int idx) {
		if ((idx >= 0) && (idx < similsV.size()))
			return similsV.elementAt(idx);
		else
			return null;
	}

	/**
	 * Adds a local similarity.
	 * 
	 * @param localSim
	 *            local similarity.
	 */
	public void addLocalSimil(CBRLocalSimilarity localSim) {
		similsHM.put(localSim.getName(), localSim);
		similsV.add(localSim);
	}

	/**
	 * Remove a local similarity function.
	 * 
	 * @param localSim
	 *            local similarity function.
	 */
	public void removeLocalSimil(CBRLocalSimilarity localSim) {
		similsHM.remove(localSim.getName());
		similsV.remove(localSim);
	}

	/**
	 * Remove a local similarity function by index.
	 * 
	 * @param idx
	 *            index.
	 */
	public void removeLocalSimil(int idx) {
		CBRLocalSimilarity localSim;

		localSim = getLocalSimil(idx);
		if (localSim != null) {
			similsHM.remove(localSim.getName());
			similsV.remove(idx);
		}
	}

	/**
	 * Returns the default local similarity.
	 * 
	 * @return the default local similarity.
	 */
	public CBRLocalSimilarity getDefLocalSimil() {
		if (similsV.size() > 0)
			return similsV.firstElement();
		else
			return null;
	}

	/**
	 * Removes all the local similarities.
	 */
	public void clear() {
		similsHM.clear();
		similsV.clear();
	}

	/**
	 * Loads the local similarities from the package.
	 */
	public void load() {
		Document document;

		if ((pkgInfo == null) || (pkgInfo.getLocalSim() == null))
			return;

		// Load XML file.
		document = readXMLFile(pkgInfo.getLocalSim());
		if (document == null)
			return;

		// Process DOM tree.
		processDOMTree(document);
	}

	/**
	 * Saves the local similarities.
	 */
	public void save() {
		Document document;

		if ((pkgInfo == null) || (pkgInfo.getLocalSim() == null))
			return;

		// Create DOM tree.
		document = createDOMTree();
		if (document == null)
			return;

		// Write XML file.
		FileUtil.createFileBackup(pkgInfo.getLocalSim());
		XMLUtil.writeDOMToFile(document, pkgInfo.getLocalSim());
	}

	/**
	 * Loads the data from a XML file and returns a DOM tree.
	 * 
	 * @param fileName
	 * @return DOM document.
	 */
	private Document readXMLFile(String fileName) {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;

		try {
			// Get DOM factory.
			factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // !! validar.

			// Load XML file.
			builder = factory.newDocumentBuilder();
			document = builder.parse(fileName);
			return document;
		} catch (SAXException sxe) {
			// Error generated during parsing.
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			CBRLogger.log("jcolibri.tools.LocalSimilFileReg", "readXMLFile", x);
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built.
			CBRLogger.log("jcolibri.tools.LocalSimilFileReg", "readXMLFile",
					pce);
		} catch (IOException ioe) {
			// I/O error.
			CBRLogger.log("jcolibri.tools.LocalSimilFileReg", "readXMLFile",
					ioe);
		}

		return null;
	}

	/**
	 * Process a DOM tree and adds the similarity functions.
	 * 
	 * @param document
	 *            DOM tree.
	 */
	private void processDOMTree(Document document) {
		Element elemLSimilarities;
		Iterator it;
		CBRLocalSimilarity localSim;

		elemLSimilarities = (Element) document.getChildNodes().item(0);
		it = XMLUtil.getChild(elemLSimilarities, "LocalSim").iterator();
		while (it.hasNext()) {
			localSim = new CBRLocalSimilarity();
			localSim.fromXMLDOM((Element) it.next());
			addLocalSimil(localSim);
		}
	}

	/**
	 * Returns a DOM tree that represents the local similarities.
	 * 
	 * @return a DOM tree.
	 */
	private Document createDOMTree() {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemLocalSimilarities, elemLocalSim;
		Iterator it;
		CBRLocalSimilarity localSim;

		try {
			// Create document.
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument(); // Create from whole cloth

			// <LocalSimilarities>.
			elemLocalSimilarities = document.createElement("LocalSimilarities");
			document.appendChild(elemLocalSimilarities);

			// <LocalSimil>.
			it = getLocalSimils().iterator();
			while (it.hasNext()) {
				localSim = (CBRLocalSimilarity) it.next();
				elemLocalSim = localSim.toXMLDOM(document);
				elemLocalSimilarities.appendChild(elemLocalSim);
			}

			return document;

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.LocalSimilFileReg", "createDOMTree",
					pce);
		}

		return null;
	}
}
