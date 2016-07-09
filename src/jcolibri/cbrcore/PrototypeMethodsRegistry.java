package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import jcolibri.cbrcore.event.RegistryChangeListener;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.exception.InternalException;
import jcolibri.cbrcore.packagemanager.PackageInfo;

/**
 * Registry of the managers of the package methods. It is implemented using the
 * Singleton pattern.
 */
public class PrototypeMethodsRegistry {

	/** User method package. */
	public static final String METHOD_PACKAGE = "jcolibri.extensions.user.method.";

	/** Collection of PrototypeMethodPkgReg. */
	private HashMap<String,PrototypeMethodPkgReg> methodPkgs;

	/** Unique instance of this class. */
	private static PrototypeMethodsRegistry registry;

	/**
	 * Constructor.
	 */
	private PrototypeMethodsRegistry() {
		methodPkgs = new LinkedHashMap<String,PrototypeMethodPkgReg>();
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static PrototypeMethodsRegistry getInstance() {
		if (registry == null) {
			registry = new PrototypeMethodsRegistry();
		}
		return registry;
	}

	/**
	 * Returns all the managers of the package methods.
	 * 
	 * @return collection of managers of package methods.
	 */
	public Collection<PrototypeMethodPkgReg> getPrototypeMethodPkgs() {
		return methodPkgs.values();
	}

	/**
	 * Returns the manager of a package methods by its name.
	 * 
	 * @param pkgName
	 *            name of the package.
	 * @return the manager of the package methods.
	 */
	public PrototypeMethodPkgReg getPrototypeMethodPkg(String pkgName) {
		return (PrototypeMethodPkgReg) methodPkgs.get(pkgName);
	}

	/**
	 * Clears all the methods of the managers.
	 */
	public void cleanRegistry() {
		Iterator<PrototypeMethodPkgReg> it;
		PrototypeMethodPkgReg pmPkgReg;

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = it.next();
			pmPkgReg.cleanRegistry();
		}
	}

	/**
	 * Build a package manager and loads the methods of that package.
	 * 
	 * @param pkgInfo
	 *            package
	 */
	public void loadPackage(PackageInfo pkgInfo) throws InitializingException {
		PrototypeMethodPkgReg pmPkgReg;

		pmPkgReg = new PrototypeMethodPkgReg(pkgInfo);
		pmPkgReg.load();
		methodPkgs.put(pkgInfo.getName(), pmPkgReg);
	}

	/**
	 * Save the methods.
	 * 
	 * @throws InternalException
	 */
	public void storeRegistry() throws InternalException {
		Iterator it;
		PrototypeMethodPkgReg pmPkgReg;

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = (PrototypeMethodPkgReg) it.next();
			pmPkgReg.storeRegistry();
		}
	}

	/**
	 * Return a Collection of CBRMethods currently available on the system
	 * 
	 * @return collection of currently available protype methods
	 */
	public Collection<CBRMethod> getMethods() {
		Iterator<PrototypeMethodPkgReg> it;
		PrototypeMethodPkgReg pmPkgReg;
		List<CBRMethod> methods = new ArrayList<CBRMethod>();

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = it.next();
			methods.addAll(pmPkgReg.getMethods());
		}
		return methods;
	}

	/**
	 * Return the prototype method identified by given name
	 * 
	 * @param name
	 *            String Name of the requiered method
	 * @return requiered method.
	 */
	public CBRMethod getMethod(String name) {
		Iterator it;
		PrototypeMethodPkgReg pmPkgReg;
		CBRMethod method = null;

		it = methodPkgs.values().iterator();
		while (it.hasNext() && (method == null)) {
			pmPkgReg = (PrototypeMethodPkgReg) it.next();
			method = pmPkgReg.getMethod(name);
		}
		return method;
	}

	/**
	 * Retrieves the available methods for a given competence
	 * 
	 * @param compentece
	 *            Competence that wants to be covered
	 * @return A list of valid methods
	 */
	public Collection<CBRMethod> getMethodWithCompetence(String competence) {
		Iterator it;
		PrototypeMethodPkgReg pmPkgReg;
		List<CBRMethod> methods = new ArrayList<CBRMethod>();

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = (PrototypeMethodPkgReg) it.next();
			methods.addAll(pmPkgReg.getMethodWithCompetence(competence));
		}
		return methods;
	}

	/**
	 * Adds a new listener of changes in the registry.
	 * 
	 * @param listener
	 */
	public void addRegistryChangeListener(RegistryChangeListener listener) {
		Iterator it;
		PrototypeMethodPkgReg pmPkgReg;

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = (PrototypeMethodPkgReg) it.next();
			pmPkgReg.addRegistryChangeListener(listener);
		}
	}

	/**
	 * Removes a listener of changes in the registry.
	 * 
	 * @param listener
	 */
	public void removeRegistryChangeListener(RegistryChangeListener listener) {
		Iterator it;
		PrototypeMethodPkgReg pmPkgReg;

		it = methodPkgs.values().iterator();
		while (it.hasNext()) {
			pmPkgReg = (PrototypeMethodPkgReg) it.next();
			pmPkgReg.removeRegistryChangeListener(listener);
		}
	}
}
