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
 * Manager of the global similarities of a package.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class GlobalSimilPkgReg {

	/** Global similarities indexed by a hash table. */
	private HashMap<String,CBRGlobalSimilarity> similsHM;

	/** Global similarities indexed by a vector. */
	private Vector<CBRGlobalSimilarity> similsV;

	/** Package. */
	private PackageInfo pkgInfo;

	/**
	 * Constructor.
	 * 
	 * @param pkgInfo
	 *            package.
	 */
	public GlobalSimilPkgReg(PackageInfo pkgInfo) {
		similsHM = new HashMap<String,CBRGlobalSimilarity>();
		similsV = new Vector<CBRGlobalSimilarity>();
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
	 * Returns the number of global similarities.
	 * 
	 * @return the number of global similarities.
	 */
	public int getNumGlobalSimils() {
		return similsV.size();
	}

	/**
	 * Returns all the global similarities.
	 * 
	 * @return all the global similarities
	 */
	public Collection<CBRGlobalSimilarity> getGlobalSimils() {
		return similsV;
	}

	/**
	 * Returns a global similarity by name.
	 * 
	 * @param name
	 *            name of the global similarity.
	 * @return the global similarity.
	 */
	public CBRGlobalSimilarity getGlobalSimil(String name) {
		return similsHM.get(name);
	}

	/**
	 * Returns a global similarity by index.
	 * 
	 * @param idx
	 *            index.
	 * @return global similarity or null.
	 */
	public CBRGlobalSimilarity getGlobalSimil(int idx) {
		if ((idx >= 0) && (idx < similsV.size()))
			return similsV.elementAt(idx);
		else
			return null;
	}

	/**
	 * Adds a global similarity.
	 * 
	 * @param globalSim
	 *            global similarity.
	 */
	public void addGlobalSimil(CBRGlobalSimilarity globalSim) {
		similsHM.put(globalSim.getName(), globalSim);
		similsV.add(globalSim);
	}

	/**
	 * Remove a global similarity function.
	 * 
	 * @param globalSim
	 *            global similarity function.
	 */
	public void removeGlobalSimil(CBRGlobalSimilarity globalSim) {
		similsHM.remove(globalSim.getName());
		similsV.remove(globalSim);
	}

	/**
	 * Remove a global similarity function by index.
	 * 
	 * @param idx
	 *            index.
	 */
	public void removeGlobalSimil(int idx) {
		CBRGlobalSimilarity globalSim;

		globalSim = getGlobalSimil(idx);
		if (globalSim != null) {
			similsHM.remove(globalSim.getName());
			similsV.remove(idx);
		}
	}

	/**
	 * Returns the default global similarity.
	 * 
	 * @return the default global similarity.
	 */
	public CBRGlobalSimilarity getDefGlobalSimil() {
		if (similsV.size() > 0)
			return similsV.firstElement();
		else
			return null;
	}

	/**
	 * Removes all the global similarities.
	 */
	public void clear() {
		similsHM.clear();
		similsV.clear();
	}

	/**
	 * Loads the global similarities from the package.
	 */
	public void load() {
		Document document;

		if ((pkgInfo == null) || (pkgInfo.getGlobalSim() == null))
			return;

		// Load XML file.
		document = readXMLFile(pkgInfo.getGlobalSim());
		if (document == null)
			return;

		// Process DOM tree.
		processDOMTree(document);
	}

	/**
	 * Saves the global similarities.
	 */
	public void save() {
		Document document;

		if ((pkgInfo == null) || (pkgInfo.getGlobalSim() == null))
			return;

		// Create DOM tree.
		document = createDOMTree();
		if (document == null)
			return;

		// Write XML file.
		FileUtil.createFileBackup(pkgInfo.getGlobalSim());
		XMLUtil.writeDOMToFile(document, pkgInfo.getGlobalSim());
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
			CBRLogger.log("jcolibri.tools.GlobalSimilPkgReg", "readXMLFile", x);
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built.
			CBRLogger.log("jcolibri.tools.GlobalSimilPkgReg", "readXMLFile",
					pce);
		} catch (IOException ioe) {
			// I/O error.
			CBRLogger.log("jcolibri.tools.GlobalSimilPkgReg", "readXMLFile",
					ioe);
		} catch (Exception ex) {
			CBRLogger
					.log("jcolibri.tools.GlobalSimilPkgReg", "readXMLFile", ex);
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
		Element elemGSimilarities;
		Iterator it;
		CBRGlobalSimilarity globalSim;

		elemGSimilarities = (Element) document.getChildNodes().item(0);
		it = XMLUtil.getChild(elemGSimilarities, "GlobalSim").iterator();
		while (it.hasNext()) {
			globalSim = new CBRGlobalSimilarity();
			globalSim.fromXMLDOM((Element) it.next());
			addGlobalSimil(globalSim);
		}
	}

	/**
	 * Returns a DOM tree that represents the global similarities.
	 * 
	 * @return a DOM tree.
	 */
	private Document createDOMTree() {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemLocalSimilarities, elemLocalSim;
		Iterator it;
		CBRGlobalSimilarity globalSim;

		try {
			// Create document.
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument(); // Create from whole cloth

			// <LocalSimilarities>.
			elemLocalSimilarities = document
					.createElement("GlobalSimilarities");
			document.appendChild(elemLocalSimilarities);

			// <LocalSimil>.
			it = getGlobalSimils().iterator();
			while (it.hasNext()) {
				globalSim = (CBRGlobalSimilarity) it.next();
				elemLocalSim = globalSim.toXMLDOM(document);
				elemLocalSimilarities.appendChild(elemLocalSim);
			}

			return document;

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.GlobalSimilarityRegistry",
					"createDOMTree", pce);
		}

		return null;
	}
}
