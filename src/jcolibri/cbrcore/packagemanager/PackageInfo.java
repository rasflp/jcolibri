package jcolibri.cbrcore.packagemanager;

import java.io.Serializable;

/**
 * Information about a jCOLIBRI package.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class PackageInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Name of the package. */
	String name;

	/** Description of the package. */
	String description;

	/** Path of the tasks file. */
	String tasks;

	/** Path of the methods file. */
	String methods;

	/** Path of the local similarities file. */
	String localSim;

	/** Path of the global similarities file. */
	String globalSim;

	/** Path of the package folder. */
	String extensionPath;

	/** Path of the data types file. */
	String dataTypes;

	/** Indicates if the package is being used. */
	boolean active;

	/**
	 * Creates a new instance of this class.
	 */
	public PackageInfo() {
		active = false;
	}

	/**
	 * Returns the description of the package.
	 * 
	 * @return description of the package.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the path of the package folder.
	 * 
	 * @return path of the package folder.
	 */
	public String getExtensionPath() {
		return extensionPath;
	}

	/**
	 * Returns the path of the global similarities file.
	 * 
	 * @return path of the global similarities file.
	 */
	public String getGlobalSim() {
		return globalSim;
	}

	/**
	 * Returns the path of the local similarities file.
	 * 
	 * @return path of the local similarities file.
	 */
	public String getLocalSim() {
		return localSim;
	}

	/**
	 * Returns the path of the methods file.
	 * 
	 * @return path of the local similarity file.
	 */
	public String getMethods() {
		return methods;
	}

	/**
	 * Returns the name of package.
	 * 
	 * @return name of package.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the path of the tasks file.
	 * 
	 * @return path of the tasks file.
	 */
	public String getTasks() {
		return tasks;
	}

	/**
	 * Sets the description of the package.
	 * 
	 * @param description
	 *            description of the package.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the path of the package folder.
	 * 
	 * @param extensionPath
	 *            path of the package folder.
	 */
	public void setExtensionPath(String extensionPath) {
		this.extensionPath = extensionPath;
	}

	/**
	 * Sets the path of the global similarities file.
	 * 
	 * @param globalSim
	 *            path of the global similarities file.
	 */
	public void setGlobalSim(String globalSim) {
		this.globalSim = globalSim;
	}

	/**
	 * Sets the path of the local similarities file.
	 * 
	 * @param localSim
	 *            path of the local similarities file.
	 */
	public void setLocalSim(String localSim) {
		this.localSim = localSim;
	}

	/**
	 * Sets the path of the methods file.
	 * 
	 * @param methods
	 *            path of the methods file.
	 */
	public void setMethods(String methods) {
		this.methods = methods;
	}

	/**
	 * Sets the name of the package.
	 * 
	 * @param name
	 *            name of the package.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the path of the tasks file.
	 * 
	 * @param tasks
	 *            path of the tasks file.
	 */
	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

	/**
	 * Returns the path of the data types file.
	 * 
	 * @return path of the data types file.
	 */
	public String getDataTypes() {
		return dataTypes;
	}

	/**
	 * Sets the path of the data types file.
	 * 
	 * @param dataTypes
	 *            path of the data types file.
	 */
	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
	}

	/**
	 * Returns if the package is active. The data types, similarity functions...
	 * of a package only will be available if the package is active.
	 * 
	 * @return if the package is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets if the package is active.
	 * 
	 * @param active
	 *            if the oacjage is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public static void main(String[] args) {

	}
}