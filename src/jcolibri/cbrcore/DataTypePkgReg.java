package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.datatypes.StringEnumType;
import jcolibri.gui.ParameterEditorFactory;
import jcolibri.util.CBRLogger;
import jcolibri.util.FileUtil;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Manager of the datatypes of a package.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class DataTypePkgReg {

	/** Package. */
	PackageInfo pkgInfo;

	/** Data types. */
	List<DataType> types;

	/**
	 * Constructor.
	 * 
	 * @param pkgInfo
	 *            package.
	 */
	public DataTypePkgReg(PackageInfo pkgInfo) {
		this.pkgInfo = pkgInfo;
		types = new ArrayList<DataType>();
	}

	/**
	 * Returns all the datatypes.
	 * 
	 * @return DataType
	 */
	public Collection<DataType> getDataTypes() {
		return types;
	}

	/**
	 * Returns the name of all the datatypes.
	 * 
	 * @return String Collection
	 */
	public Collection<String> getDataTypesNames() {
		ArrayList<String> al = new ArrayList<String>();
		for (Iterator iter = getDataTypes().iterator(); iter.hasNext();) {
			DataType dt = (DataType) iter.next();
			al.add(dt.getName());
		}
		return al;
	}

	/**
	 * Clear all the datatypes.
	 */
	public void clearRegistry() {
		DataType dt;
		Iterator it;

		it = types.iterator();
		while (it.hasNext()) {
			dt = (DataType) it.next();
			ParameterEditorFactory.unregisterEditor(dt.getName());
		}
		types.clear();
	}

	/**
	 * Adds a new datatype.
	 * 
	 * @param dataType
	 *            new datatype.
	 */
	public void addDataType(DataType dataType) {
		if (!existsDataType(dataType.getName())) {
			types.add(dataType);
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Loading Data Type: " + dataType.getName());
		}
	}

	/**
	 * Replaces a datatype.
	 * 
	 * @param oldName
	 *            name of the old datatype.
	 * @param dataType
	 *            new datatype.
	 */
	public void replaceDataType(String oldName, DataType dataType) {
		int idx;

		idx = getIdxDataType(oldName);
		if (idx >= 0) {
			types.remove(idx);
			types.add(idx, dataType);
		}
	}

	/**
	 * Removes a datatype by its name.
	 * 
	 * @param name
	 *            name of the datatype.
	 */
	public void removeDataType(String name) {
		DataType dt = getDataType(name);
		types.remove(dt);
		ParameterEditorFactory.unregisterEditor(name);
	}

	/**
	 * Removes a datatype by its index.
	 * 
	 * @param idx
	 *            index of the datatype.
	 */
	public void removeDataType(int idx) {
		DataType dt = getDataType(idx);
		ParameterEditorFactory.unregisterEditor(dt.getName());
		types.remove(idx);
	}

	/**
	 * Returns if exists a datatype with this name.
	 * 
	 * @param name
	 *            name of the datatype.
	 * @return true if exists, false in otherwise.
	 */
	public boolean existsDataType(String name) {
		return (getDataType(name) != null);
	}

	/**
	 * Returns a datatype by its name.
	 * 
	 * @param datatype
	 *            name of the datatype.
	 * @return the datatype or null if it does not exist.
	 */
	public DataType getDataType(String datatype) {
		DataType dt, dtRes = null;

		for (Iterator iter = types.iterator(); iter.hasNext()
				&& (dtRes == null);) {
			dt = (DataType) iter.next();
			if (dt.getName().equals(datatype))
				dtRes = dt;
		}
		return dtRes;
	}

	/**
	 * Returns the index of a datatype.
	 * 
	 * @param datatype
	 *            name of the datatype.
	 * @return index of the datatype or -1 if it daoes not exist.
	 */
	private int getIdxDataType(String datatype) {
		int idx;
		DataType dt;

		idx = 0;
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			dt = (DataType) iter.next();
			if (dt.getName().equals(datatype))
				return idx;
			idx++;
		}
		return -1;
	}

	/**
	 * Returns a datatype by ots index.
	 * 
	 * @param idx
	 *            index of the wanted datatype.
	 * @return the datatype.
	 */
	public DataType getDataType(int idx) {
		return (DataType) types.get(idx);
	}

	/**
	 * Returns the names of the datatypes.
	 * 
	 * @return names of the datatypes.
	 */
	public String[] getSupportedTypes() {
		String[] res = new String[this.types.size()];
		int i = 0;
		for (Iterator iter = types.iterator(); iter.hasNext();)
			res[i++] = ((DataType) iter.next()).getName();
		return res;
	}

	/**
	 * Loads the datatypes of the package.
	 * 
	 * @throws InitializingException
	 */
	public void load() throws InitializingException {
		if ((pkgInfo == null) || (pkgInfo.getDataTypes() == ""))
			return;

		clearRegistry();
		loadRegistry(pkgInfo.getDataTypes());
	}

	/**
	 * Saves the datatypes of the package.
	 */
	public void save() {
		Document document;

		if ((pkgInfo == null) || (pkgInfo.getDataTypes() == ""))
			return;

		// Create DOM tree.
		document = createDOMTree();
		if (document == null)
			return;

		// Write XML file.
		FileUtil.createFileBackup(pkgInfo.getDataTypes());
		XMLUtil.writeDOMToFile(document, pkgInfo.getDataTypes());
	}

	/**
	 * Creates a DOM document that represents the datatypes.
	 * 
	 * @return DOM document.
	 */
	private Document createDOMTree() {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemDataTypes, elemDataType;
		Iterator it;
		DataType dataType;

		try {
			// Create document.
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// <DataTypes>.
			elemDataTypes = document.createElement("DataTypes");
			document.appendChild(elemDataTypes);

			// <DataType>.
			it = getDataTypes().iterator();
			while (it.hasNext()) {
				dataType = (DataType) it.next();
				elemDataType = dataType.toXMLDOM(document);
				elemDataTypes.appendChild(elemDataType);
			}

			return document;

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.LocalSimilFileReg", "createDOMTree",
					pce);
		}

		return null;
	}

	/**
	 * Check if a datatype DOM node is a enumerator datatype node.
	 * 
	 * @param elem
	 *            DOM node
	 * @return true if is a enumerator node.
	 */
	private boolean isEnumeratorElement(Element elem) {
		if (elem != null) {
			return (XMLUtil.getFirstChild(elem, "Enumeration") != null);
		}
		return false;
	}

	/**
	 * Loads the datatype from a file.
	 * 
	 * @param fileName
	 * @throws InitializingException
	 */
	private void loadRegistry(String fileName) throws InitializingException {
		if (fileName == null)
			return;

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = db.parse(fileName);
			Element elemDataTypes, elemDataType;
			Iterator it;
			DataType dataType;

			elemDataTypes = (Element) doc.getElementsByTagName("DataTypes")
					.item(0);

			// Read DataType.
			it = XMLUtil.getChild(elemDataTypes, "DataType").iterator();
			while (it.hasNext()) {
				elemDataType = (Element) it.next();

				if (isEnumeratorElement(elemDataType))
					dataType = new StringEnumType();
				else
					dataType = new DataType();
				dataType.fromXMLDOM(elemDataType);
				addDataType(dataType);
			}
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), "Error parsing data types xml: "
					+ fileName, e);
			throw new jcolibri.cbrcore.exception.InitializingException(e);
		}
	}
}
