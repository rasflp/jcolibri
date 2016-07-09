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
 * Registry of the managers of the package tasks. It is implemented using the
 * Singleton pattern.
 */
public class PrototypeTasksRegistry {

	/** Unique instance of this class. */
	private static PrototypeTasksRegistry registry;

	/** Collection of PrototypeTaskPkgReg. */
	private HashMap<String,PrototypeTaskPkgReg> taskPkgs;

	/**
	 * Constructor.
	 */
	private PrototypeTasksRegistry() {
		taskPkgs = new LinkedHashMap<String,PrototypeTaskPkgReg>();
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static PrototypeTasksRegistry getInstance() {
		if (registry == null) {
			registry = new PrototypeTasksRegistry();
		}
		return registry;
	}

	/**
	 * Returns all the managers of the package tasks.
	 * 
	 * @return collection of managers of package tasks.
	 */
	public Collection<PrototypeTaskPkgReg> getPrototypeTaskPkgs() {
		return taskPkgs.values();
	}

	/**
	 * Returns the manager of a package tasks by its name.
	 * 
	 * @param pkgName
	 *            name of the package.
	 * @return the manager of the package tasks.
	 */
	public PrototypeTaskPkgReg getPrototypeTaskPkg(String pkgName) {
		return (PrototypeTaskPkgReg) taskPkgs.get(pkgName);
	}

	/**
	 * Clears all the tasks of the managers.
	 */
	public void cleanRegistry() {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg =  it.next();
			ptPkgReg.cleanRegistry();
		}
	}

	/**
	 * Build a package manager and loads the tasks of that package.
	 * 
	 * @param pkgInfo
	 *            package
	 */
	public void loadPackage(PackageInfo pkgInfo) throws InitializingException {
		PrototypeTaskPkgReg ptPkgReg;

		ptPkgReg = new PrototypeTaskPkgReg(pkgInfo);
		ptPkgReg.load();
		taskPkgs.put(pkgInfo.getName(), ptPkgReg);
	}

	/**
	 * Save the tasks.
	 * 
	 * @throws InternalException
	 */
	public void storeRegistry() throws InternalException {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg =  it.next();
			ptPkgReg.storeRegistry();
		}
	}

	/**
	 * Return a Collection of CBRTasks currently available on the system
	 * 
	 * @return Collection
	 */
	public Collection getTasks() {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;
		List<CBRTask> tasks = new ArrayList<CBRTask>();

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg = it.next();
			tasks.addAll(ptPkgReg.getTasks());
		}
		return tasks;
	}

	/**
	 * Return a specific task
	 * 
	 * @param taskName
	 * @return CBRTask
	 */
	public CBRTask getTask(String taskName) {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;
		CBRTask task = null;

		it = taskPkgs.values().iterator();
		while (it.hasNext() && (task == null)) {
			ptPkgReg = it.next();
			task = ptPkgReg.getTask(taskName);
		}
		return task;
	}

	/**
	 * Adds a new listener of changes in the registry.
	 * 
	 * @param listener
	 */
	public void addRegistryChangeListener(RegistryChangeListener listener) {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg = it.next();
			ptPkgReg.addRegistryChangeListener(listener);
		}
	}

	/**
	 * Removes a listener of changes in the registry.
	 * 
	 * @param listener
	 */
	public void removeRegistryChangeListener(RegistryChangeListener listener) {
		Iterator<PrototypeTaskPkgReg> it;
		PrototypeTaskPkgReg ptPkgReg;

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg = it.next();
			ptPkgReg.removeRegistryChangeListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Iterator<PrototypeTaskPkgReg> it;
		StringBuffer sb = new StringBuffer();
		PrototypeTaskPkgReg ptPkgReg;

		it = taskPkgs.values().iterator();
		while (it.hasNext()) {
			ptPkgReg = it.next();
			sb.append(ptPkgReg.toString());
		}
		return sb.toString();
	}
}
