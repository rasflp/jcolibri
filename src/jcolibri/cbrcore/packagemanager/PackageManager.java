package jcolibri.cbrcore.packagemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Manager of the jCOLIBRI packages. It is built using the singleton pattern.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class PackageManager implements Serializable {
	private static final long serialVersionUID = 1L;

	// Unique instance of this class.
	private static PackageManager _instance = null;

	// Avaible packages.
	private Collection<PackageInfo> packages;

	/**
	 * Sets the unique instance of this class.
	 * 
	 * @param pkgMngr
	 *            unique instance of this class.
	 */
	public static void setInstance(PackageManager pkgMngr) {
		_instance = pkgMngr;
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return unique instance of this class.
	 */
	public static PackageManager getInstance() {
		if (_instance == null)
			_instance = new PackageManager();
		return _instance;
	}

	/**
	 * Returns all the available packages in jCOLIBRI.
	 * 
	 * @return all the packages.
	 */
	public Collection<PackageInfo> getPackages() {
		return packages;
	}

	/**
	 * Returns the active packages. A package is active if it has been selected
	 * to be used in the actual application.
	 * 
	 * @return the active packages.
	 */
	public Collection<PackageInfo> getActivePackages() {
		List<PackageInfo> pkgs = new ArrayList<PackageInfo>();
		Iterator<PackageInfo> it;
		PackageInfo pkgInfo;

		it = packages.iterator();
		while (it.hasNext()) {
			pkgInfo = it.next();
			if (pkgInfo.isActive())
				pkgs.add(pkgInfo);
		}
		return pkgs;
	}

	/**
	 * Returns a specific package by its name. If the package does not exist,
	 * returns null.
	 * 
	 * @param name
	 *            name of the wanted package.
	 * @return the specified package or null.
	 */
	public PackageInfo getPackageByName(String name) {
		PackageInfo pi = null;
		for (Iterator<PackageInfo> iter = packages.iterator(); iter.hasNext();) {
			pi = iter.next();
			if (pi.getName().equals(name))
				return pi;
		}
		return pi;
	}

	/**
	 * Creates a new instance of this class.
	 */
	private PackageManager() {
		PackageFinder pf = new PackageFinder();
		packages = pf.findPackages();
	}
}