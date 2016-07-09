package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import jcolibri.cbrcore.packagemanager.PackageInfo;

/**
 * Registry of the managers of the package local similarities. It is implemented
 * using the Singleton pattern.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class LocalSimilarityRegistry {

	/** Unique instance of this class. */
	private static LocalSimilarityRegistry localSimReg = null;

	/** Collection of LocalSimilPkgReg. */
	private HashMap<String,LocalSimilPkgReg> similPkgs;

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static LocalSimilarityRegistry getInstance() {
		if (localSimReg == null) {
			localSimReg = new LocalSimilarityRegistry();
		}
		return localSimReg;
	}

	/**
	 * Returns the number of managers of the package local similarities.
	 * 
	 * @return the number of managers of the package local similarities.
	 */
	public int getNumLocalSimilPkgs() {
		return similPkgs.size();
	}

	/**
	 * Returns all the managers of the package local similarities.
	 * 
	 * @return collection of managers of package local similarities.
	 */
	public Collection<LocalSimilPkgReg> getLocalSimilPkgs() {
		return similPkgs.values();
	}

	/**
	 * Returns the manager of a package local similarities by its name.
	 * 
	 * @param pkgName
	 *            name of the package.
	 * @return the manager of the package local similarities.
	 */
	public LocalSimilPkgReg getLocalSimilPkg(String pkgName) {
		return similPkgs.get(pkgName);
	}

	/**
	 * Returns a local similarity by its name.
	 * 
	 * @param name
	 *            name of the local similarity.
	 * @return the local similarity or null.
	 */
	public CBRLocalSimilarity getLocalSimil(String name) {
		LocalSimilPkgReg lsPkgReg;
		CBRLocalSimilarity lSim = null;
		Iterator<LocalSimilPkgReg> it;

		it = similPkgs.values().iterator();
		while (it.hasNext() && (lSim == null)) {
			lsPkgReg = it.next();
			lSim = lsPkgReg.getLocalSimil(name);
		}
		return lSim;
	}

	/**
	 * Returns the default local similarity.
	 * 
	 * @return the default local similarity.
	 */
	public CBRLocalSimilarity getDefLocalSimil() {
		LocalSimilPkgReg lsPkgReg;
		CBRLocalSimilarity lSim = null;

		if (similPkgs.size() > 0) {
			lsPkgReg = (LocalSimilPkgReg) similPkgs.values().iterator().next();
			lSim = lsPkgReg.getDefLocalSimil();
		}
		return lSim;
	}

	/**
	 * Returns all the local similarities of all package managers.
	 * 
	 * @return all the local similarities.
	 */
	public Collection getLocalSimils() {
		LocalSimilPkgReg lsPkgReg;
		Iterator<LocalSimilPkgReg> it;
		List<CBRLocalSimilarity> lst;

		lst = new ArrayList<CBRLocalSimilarity>();
		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = it.next();
			lst.addAll(lsPkgReg.getLocalSimils());
		}
		return lst;
	}

	/**
	 * Clears all the local similarities of the managers.
	 */
	public void clear() {
		LocalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (LocalSimilPkgReg) it.next();
			lsPkgReg.clear();
		}
	}

	/**
	 * Build a package manager and loads the local similarities of that package.
	 * 
	 * @param pkgInfo
	 *            package
	 */
	public void loadPackage(PackageInfo pkgInfo) {
		LocalSimilPkgReg lsPkgReg;

		lsPkgReg = new LocalSimilPkgReg(pkgInfo);
		lsPkgReg.load();
		similPkgs.put(pkgInfo.getName(), lsPkgReg);
	}

	/**
	 * Reload the local similarities of all current managers.
	 */
	public void reload() {
		LocalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (LocalSimilPkgReg) it.next();
			lsPkgReg.clear();
			lsPkgReg.load();
		}
	}

	/**
	 * Save the local similaties.
	 */
	public void save() {
		LocalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (LocalSimilPkgReg) it.next();
			lsPkgReg.save();
		}
	}

	/**
	 * Constructor.
	 */
	private LocalSimilarityRegistry() {
		similPkgs = new LinkedHashMap<String,LocalSimilPkgReg>();
	}
}
