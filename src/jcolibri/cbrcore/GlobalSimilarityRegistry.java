package jcolibri.cbrcore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import jcolibri.cbrcore.packagemanager.PackageInfo;

/**
 * Registry of the managers of the package global similarities. It is
 * implemented using the Singleton pattern.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class GlobalSimilarityRegistry {

	/** Unique instance of this class. */
	private static GlobalSimilarityRegistry globalSimReg = null;

	/** Collection of GlobalSimilPkgReg. */
	private HashMap<String, GlobalSimilPkgReg> similPkgs;

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static GlobalSimilarityRegistry getInstance() {
		if (globalSimReg == null) {
			globalSimReg = new GlobalSimilarityRegistry();
		}
		return globalSimReg;
	}

	/**
	 * Returns the number of managers of the package global similarities.
	 * 
	 * @return the number of managers of the package global similarities.
	 */
	public int getNumGlobalSimilPkgs() {
		return similPkgs.size();
	}

	/**
	 * Returns all the managers of the package global similarities.
	 * 
	 * @return collection of managers of package global similarities.
	 */
	public Collection<GlobalSimilPkgReg> getGlobalSimilPkgs() {
		return similPkgs.values();
	}

	/**
	 * Returns the manager of a package global similarities by its name.
	 * 
	 * @param pkgName
	 *            name of the package.
	 * @return the manager of the package global similarities.
	 */
	public GlobalSimilPkgReg getGlobalSimilPkg(String pkgName) {
		return (GlobalSimilPkgReg) similPkgs.get(pkgName);
	}

	/**
	 * Returns a global similarity by its name.
	 * 
	 * @param name
	 *            name of the global similarity.
	 * @return the global similarity or null.
	 */
	public CBRGlobalSimilarity getGlobalSimil(String name) {
		GlobalSimilPkgReg lsPkgReg;
		CBRGlobalSimilarity gSim = null;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext() && (gSim == null)) {
			lsPkgReg = (GlobalSimilPkgReg) it.next();
			gSim = lsPkgReg.getGlobalSimil(name);
		}
		return gSim;
	}

	/**
	 * Returns the default Global similarity.
	 * 
	 * @return the default Global similarity.
	 */
	public CBRGlobalSimilarity getDefGlobalSimil() {
		GlobalSimilPkgReg lsPkgReg;
		CBRGlobalSimilarity gSim = null;

		if (similPkgs.size() > 0) {
			lsPkgReg = (GlobalSimilPkgReg) similPkgs.values().iterator().next();
			gSim = lsPkgReg.getDefGlobalSimil();
		}
		return gSim;
	}

	/**
	 * Returns all the Global similarities of all package managers.
	 * 
	 * @return all the Global similarities.
	 */
	public Collection getGlobalSimils() {
		GlobalSimilPkgReg lsPkgReg;
		Iterator<GlobalSimilPkgReg> it;
		List<CBRGlobalSimilarity> lst;

		lst = new ArrayList<CBRGlobalSimilarity>();
		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = it.next();
			lst.addAll(lsPkgReg.getGlobalSimils());
		}
		return lst;
	}

	/**
	 * Clears all the global similarities of the managers.
	 */
	public void clear() {
		GlobalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (GlobalSimilPkgReg) it.next();
			lsPkgReg.clear();
		}
	}

	/**
	 * Build a package manager and loads the global similarities of that
	 * package.
	 * 
	 * @param pkgInfo
	 *            package
	 */
	public void loadPackage(PackageInfo pkgInfo) {
		GlobalSimilPkgReg lsPkgReg;

		lsPkgReg = new GlobalSimilPkgReg(pkgInfo);
		lsPkgReg.load();
		similPkgs.put(pkgInfo.getName(), lsPkgReg);
	}

	/**
	 * Reload the global similarities of all current managers.
	 */
	public void reload() {
		GlobalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (GlobalSimilPkgReg) it.next();
			lsPkgReg.clear();
			lsPkgReg.load();
		}
	}

	/**
	 * Save the global similaties.
	 */
	public void save() {
		GlobalSimilPkgReg lsPkgReg;
		Iterator it;

		it = similPkgs.values().iterator();
		while (it.hasNext()) {
			lsPkgReg = (GlobalSimilPkgReg) it.next();
			lsPkgReg.save();
		}
	}

	/**
	 * Constructor.
	 */
	private GlobalSimilarityRegistry() {
		similPkgs = new LinkedHashMap<String, GlobalSimilPkgReg>();
	}
}
