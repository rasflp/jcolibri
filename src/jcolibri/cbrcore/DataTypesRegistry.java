package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.packagemanager.PackageInfo;

/**
 * Registry of the managers of the package datatypes. It is implemented using
 * the Singleton pattern.
 * 
 * @author Juan A. Recio-García
 */
public class DataTypesRegistry {
	/** Root of the datatypes hierarchy. */
	private static DataType CBRTERM_DataType = null;

	/** Unique instance of this class. */
	private static DataTypesRegistry _instance = null;

	/** Collection of DataTypePkgReg. */
	private HashMap<String,DataTypePkgReg> typesPkg;

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static DataTypesRegistry getInstance() {
		if (_instance == null)
			_instance = new DataTypesRegistry();
		return _instance;
	}

	/**
	 * Returns the number of managers of the package datatypes.
	 * 
	 * @return the number of managers of the package datatypes.
	 */
	public int getNumDataTypePkgs() {
		return typesPkg.size();
	}

	/**
	 * Returns all the managers of the packages datatypes.
	 * 
	 * @return collection of managers of packages datatypes.
	 */
	public Collection getDataTypePkgs() {
		return typesPkg.values();
	}

	/**
	 * Returns the manager of a package datatypes by its name.
	 * 
	 * @param pkgName
	 *            name of the package.
	 * @return the manager of the package datatypes.
	 */
	public DataTypePkgReg getDataTypePkg(String pkgName) {
		return (DataTypePkgReg) typesPkg.get(pkgName);
	}

	/**
	 * Clears all the datatypes of the managers.
	 */
	public void clearRegistry() {
		DataTypePkgReg dtPkgReg;
		Iterator it;

		it = typesPkg.values().iterator();
		while (it.hasNext()) {
			dtPkgReg = (DataTypePkgReg) it.next();
			dtPkgReg.clearRegistry();
		}
	}

	/**
	 * Constructor.
	 */
	private DataTypesRegistry() {
		typesPkg = new LinkedHashMap<String,DataTypePkgReg>();

		// Create CBRTERM_DataType if necessary.
		getCBRTERMDataType();
	}

	/**
	 * Returns all the datatypes of the managers.
	 * 
	 * @return all the datatypes of the managers.
	 */
	public Iterator<DataType> getDataTypes() {
		DataTypePkgReg dtPkgReg;
		Iterator it;
		List<DataType> types = new ArrayList<DataType>();

		types.add(CBRTERM_DataType);
		it = typesPkg.values().iterator();
		while (it.hasNext()) {
			dtPkgReg = (DataTypePkgReg) it.next();
			types.addAll(dtPkgReg.getDataTypes());
		}

		return types.iterator();
	}

	/**
	 * Returns the names of the datatypes of all the managers.
	 * 
	 * @return the names of the datatypes of all the managers.
	 */
	public Iterator<String> getDataTypesNames() {
		DataType dt;
		Iterator<DataType> it;
		List<String> types = new ArrayList<String>();

		it = getDataTypes();
		while (it.hasNext()) {
			dt = it.next();
			types.add(dt.getName());
		}

		return types.iterator();
	}

	/**
	 * Returns the root of the datatype hierarchy.
	 * 
	 * @return the root of the datatype hierarchy.
	 */
	public static DataType getCBRTERMDataType() {
		if (CBRTERM_DataType == null)
			initCBRTERMDataType();
		return CBRTERM_DataType;
	}

	/**
	 * Checks if a datatype exists.
	 * 
	 * @param datatype
	 *            name of the dtatype.
	 * @return true if the dtaype exists, false in otherwise.
	 */
	public boolean existsDataType(String datatype) {
		return (getDataType(datatype) != null);
	}

	/**
	 * Returns a datatype by its name.
	 * 
	 * @param datatype
	 *            name of the datatype.
	 * @return the datatype or null.
	 */
	public DataType getDataType(String datatype) {
		DataTypePkgReg dtPkgReg;
		Iterator it;
		DataType dt = null;

		if (datatype.equals(getCBRTERMDataType().getName()))
			return getCBRTERMDataType();

		it = typesPkg.values().iterator();
		while (it.hasNext() && (dt == null)) {
			dtPkgReg = (DataTypePkgReg) it.next();
			dt = dtPkgReg.getDataType(datatype);
		}

		return dt;
	}

	/**
	 * Returns the names of all the datatypes.
	 * 
	 * @return names of the datatypes.
	 */
	public String[] getSupportedTypes() {
		DataTypePkgReg dtPkgReg;
		Iterator<DataTypePkgReg> it;
		ArrayList<String> suppTypes = new ArrayList<String>();

		it = typesPkg.values().iterator();
		suppTypes.add(getCBRTERMDataType().getName());
		while (it.hasNext()) {
			dtPkgReg = it.next();
			suppTypes.addAll(Arrays.asList(dtPkgReg.getSupportedTypes()));
		}

		return (String[]) suppTypes.toArray(new String[0]);
	}

	/**
	 * Build a package manager and loads the datatypes of that package.
	 * 
	 * @param pkgInfo
	 *            package.
	 * @throws InitializingException
	 */
	public void loadPackage(PackageInfo pkgInfo) throws InitializingException {
		DataTypePkgReg dtPkgReg;

		dtPkgReg = new DataTypePkgReg(pkgInfo);
		dtPkgReg.load();
		typesPkg.put(pkgInfo.getName(), dtPkgReg);
	}

	/**
	 * Reload the datatypes of all current managers.
	 * 
	 * @throws InitializingException
	 */
	public void reload() throws InitializingException {
		DataTypePkgReg dtPkgReg;
		Iterator it;

		it = typesPkg.values().iterator();
		while (it.hasNext()) {
			dtPkgReg = (DataTypePkgReg) it.next();
			dtPkgReg.clearRegistry();
			dtPkgReg.load();
		}
	}

	/**
	 * Save the datatypes.
	 */
	public void save() {
		DataTypePkgReg dtPkgReg;
		Iterator it;

		it = typesPkg.values().iterator();
		while (it.hasNext()) {
			dtPkgReg = (DataTypePkgReg) it.next();
			dtPkgReg.save();
		}
	}

	/**
	 * Builds the CBRTERM datatype.
	 */
	private static void initCBRTERMDataType() {
		try {
			if (CBRTERM_DataType == null)
				CBRTERM_DataType = new DataType("CBRTERM", "java.lang.Object");
		} catch (Exception e) {
		}
	}

}
