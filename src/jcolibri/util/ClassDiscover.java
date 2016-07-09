package jcolibri.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for look for classes in run time.
 * 
 * @author Juan José Bello
 */
public class ClassDiscover {

	/**
	 * Return all the classes in a package that extends directy or indirecty a
	 * class.
	 * 
	 * @param pckgname
	 *            name of the package.
	 * @param superclass
	 *            class to be extended.
	 * @return list of class names.
	 */
	public static List<String> find(String pckgname, Class superclass) {
		ArrayList<String> list = new ArrayList<String>();
		// Code from JWhich
		// ======
		// Translate the package name into an absolute path
		String name = new String(pckgname);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}

		name = name.replace('.', '/');

		// Get a File object for the package
		URL url = ClassDiscover.class.getResource(name);
		File directory = null;
		if (url != null)
			directory = new File(url.getFile());

		// New code
		// ======
		if (directory != null && directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {

				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					String classname = files[i].substring(0,
							files[i].length() - 6);
					try {
						// Try to create an instance of the object
						Object o = Class.forName(pckgname + "." + classname)
								.newInstance();
						if (superclass.isInstance(o)) {
							list.add(classname);
						}
					} catch (ClassNotFoundException cnfex) {
						System.err.println(cnfex);
					} catch (InstantiationException iex) {
						// We try to instantiate an interface
						// or an object that does not have a
						// default constructor
					} catch (IllegalAccessException iaex) {
						// The class is not public
					}
				}
			}
		}
		return list;
	}

	/**
	 * Returns all the loaded classes that implements an interface.
	 * 
	 * @param inter
	 *            interface name.
	 * @return list of class names.
	 * @throws ClassNotFoundException
	 */
	public static List<String> findClassByInterface(String inter)
			throws ClassNotFoundException {
		ArrayList<String> list = new ArrayList<String>();
		Class tosubclass = Class.forName(inter);
		Package[] pcks = Package.getPackages();
		for (int i = 0; i < pcks.length; i++) {
			list.addAll(find(pcks[i].getName(), tosubclass));
		}
		return list;
	}

	public static void main(String[] args) {
		try {
			findClassByInterface("jcolibri.cbrcore.CBRTerm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
