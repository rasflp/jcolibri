package jcolibri.cbrcore.packagemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.util.CBRLogger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Discoverer of the available packages in jCOLIBRI.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class PackageFinder {

	/** Name of the configuration file of each package. */
	public static final String CONFIG_FILE = "config.xml";

	/**
	 * Creates a new instance of this class.
	 */
	public PackageFinder() {
	}

	/**
	 * Finds and returns the available packages.
	 * 
	 * @return all the packages.
	 */
	public Collection<PackageInfo> findPackages() {
		ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();
		try {
			File workDir = new File("config");
			System.out.println(workDir.getCanonicalPath());
			ArrayList<File> extensionConfigs = new ArrayList<File>();
			findPackagesConfigFile(workDir, extensionConfigs);
			for (Iterator<File> iter = extensionConfigs.iterator(); iter.hasNext();) {
				File f =  iter.next();
				CBRLogger.log(this.getClass(), CBRLogger.INFO,
						"Loading Extension: " + f.getCanonicalPath());
				try {
					PackageInfo pi = loadPackageInfo(f);
					packages.add(pi);
					CBRLogger.log(this.getClass(), CBRLogger.INFO, pi.getName()
							+ " loaded correctly");
				} catch (Exception e) {
					CBRLogger.log(this.getClass(), CBRLogger.ERROR,
							"Error loading Package: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packages;
	}

	public static void main(String[] args) {

	}

	/**
	 * Reads the main configuration file of a package.
	 * 
	 * @param f
	 *            configuration file.
	 * @return a description of the package configuration.
	 */
	private PackageInfo loadPackageInfo(File f) throws Exception {
		PackageInfo pi = new PackageInfo();
		String base = f.getParent();
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.parse(f);

		pi.setName(doc.getElementsByTagName("name").item(0).getFirstChild()
				.getNodeValue());
		pi.setDescription(doc.getElementsByTagName("description").item(0)
				.getFirstChild().getNodeValue());

		if (!pi.getName().equals("Core"))
			pi.setExtensionPath(doc.getElementsByTagName("path").item(0)
					.getFirstChild().getNodeValue());

		NodeList nl;
		nl = doc.getElementsByTagName("tasks");
		if (nl.getLength() > 0)
			pi.setTasks(base + File.separator
					+ nl.item(0).getFirstChild().getNodeValue());

		nl = doc.getElementsByTagName("methods");
		if (nl.getLength() > 0)
			pi.setMethods(base + File.separator
					+ nl.item(0).getFirstChild().getNodeValue());

		nl = doc.getElementsByTagName("localSimilarityFunctions");
		if (nl.getLength() > 0)
			pi.setLocalSim(base + File.separator
					+ nl.item(0).getFirstChild().getNodeValue());

		nl = doc.getElementsByTagName("globalSimilarityFunctions");
		if (nl.getLength() > 0)
			pi.setGlobalSim(base + File.separator
					+ nl.item(0).getFirstChild().getNodeValue());

		nl = doc.getElementsByTagName("datatypes");
		if (nl.getLength() > 0)
			pi.setDataTypes(base + File.separator
					+ nl.item(0).getFirstChild().getNodeValue());

		return pi;
	}

	/**
	 * Looks for the packages configuration files in a directory and its
	 * subdirectories.
	 * 
	 * @param dir
	 *            root directory of the search.
	 * @param list
	 *            located configuration files .
	 */
	private void findPackagesConfigFile(File dir, ArrayList<File> list) {
		File[] childs = dir.listFiles();
		for (int i = 0; i < childs.length; i++) {
			if (childs[i].isFile()) {
				if (childs[i].getName().equals(CONFIG_FILE))
					list.add(childs[i]);
			} else if (childs[i].isDirectory()) {
				findPackagesConfigFile(childs[i], list);
			}
		}
	}

}