package jcolibri.cbrcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jcolibri.cbrcore.event.RegistryChangeEvent;
import jcolibri.cbrcore.event.RegistryChangeListener;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.exception.InternalException;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.util.CBRLogger;
import jcolibri.xml.tasks.ObjectFactory;
import jcolibri.xml.tasks.Task;
import jcolibri.xml.tasks.TaskType;
import jcolibri.xml.tasks.Tasks;

/**
 * Manager of the tasks of a package.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PrototypeTaskPkgReg {

	/** Tasks indexed as a hash table. */
	private HashMap<String,CBRTask> tasksHM;

	/** Tasks indexed as a list. */
	private List<CBRTask> tasksL;

	/** Listener of changes in this manager. */
	private List<RegistryChangeListener> listeners;

	/** Package. */
	private PackageInfo pkgInfo;

	/**
	 * Constructor.
	 * 
	 * @param pkgInfo
	 *            package.
	 */
	public PrototypeTaskPkgReg(PackageInfo pkgInfo) {
		tasksHM = new HashMap<String,CBRTask>();
		tasksL = new ArrayList<CBRTask>();
		listeners = new LinkedList<RegistryChangeListener>();
		this.pkgInfo = pkgInfo;
	}

	/**
	 * Returns the associated package.
	 * 
	 * @return the associated package.
	 */
	public PackageInfo getPackageInfo() {
		return pkgInfo;
	}

	/**
	 * Returns the number of tasks.
	 * 
	 * @return the number of tasks.
	 */
	public int getNumTasks() {
		return tasksL.size();
	}

	/**
	 * Clears all the tasks.
	 */
	public void cleanRegistry() {
		tasksHM.clear();
		tasksL.clear();
		fireRegistryChanged(this);
	}

	/**
	 * Loads the tasks of the current package.
	 * 
	 * @throws InitializingException
	 */
	public void load() throws InitializingException {
		if ((pkgInfo == null) || (pkgInfo.getTasks() == null))
			return;

		loadRegistry(pkgInfo.getTasks());
	}

	/**
	 * Loads de tasks from a file.
	 * 
	 * @param fileName
	 */
	private void loadRegistry(String fileName) throws InitializingException {
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);// ClassLoader.getSystemResourceAsStream(fileName);
			// if (is == null) {
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeTasksPkgReg",
					CBRLogger.ERROR, "Resource " + fileName + " not found.");
			throw new jcolibri.cbrcore.exception.InitializingException();
		}
		// If the file exists then we try to recover the tasks.
		try {
			// CHANGE r4// tasksHash = new HashMap();
			JAXBContext jc = JAXBContext.newInstance("jcolibri.xml.tasks");
			Unmarshaller u = jc.createUnmarshaller();
			Tasks tasksXML = (Tasks) u.unmarshal(is);
			List tasksListXML = tasksXML.getTask();
			for (Iterator it = tasksListXML.iterator(); it.hasNext();) {
				TaskType taskXML = (TaskType) it.next();
				CBRTask task = new CBRTask(taskXML.getName(), taskXML
						.getDescription());
				tasksHM.put(task.getName(), task);
				tasksL.add(task);
			}
			// current = fileName;
			fireRegistryChanged(this);
		} catch (javax.xml.bind.JAXBException je) {
			CBRLogger.log("jcolibri.core.PrototypeTasksRegistry",
					"loadRegistry", je);
			throw new jcolibri.cbrcore.exception.InitializingException(je);
		}
	}

	/**
	 * Adds a new task to the manager
	 * 
	 * @param name
	 * @param description
	 */
	public void addTask(String name, String description) {
		addTask(new CBRTask(name, description));
	}

	/**
	 * Adds a new task to the manager.
	 * 
	 * @param task
	 */
	public void addTask(CBRTask task) {
		tasksHM.put(task.getName(), task);
		tasksL.add(task);
		fireRegistryChanged(this);
	}

	/**
	 * Persists the manager in disk
	 * 
	 * @throws InternalException
	 */
	public void storeRegistry() throws InternalException {
		storeRegistry(pkgInfo.getTasks(), tasksL);
	}

	/**
	 * Persists the manager in disk on an specific file
	 * 
	 * @param fileName
	 * @throws InternalException
	 */
	@SuppressWarnings("unchecked")
    private void storeRegistry(String fileName, Collection table)
			throws InternalException {
		File file = new File(fileName + "update");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeTasksPkgReg",
					"storeRegistry", ioe);
			throw new jcolibri.cbrcore.exception.InternalException(ioe);
		}
		// If the file exists then we try to recover the tasks.
		try {
			JAXBContext jc = JAXBContext.newInstance("jcolibri.xml.tasks");
			ObjectFactory of = new ObjectFactory();
			Tasks tasksXML = of.createTasks();
			List<Task> list = tasksXML.getTask();
			for (Iterator it = table.iterator(); it.hasNext();) {
				CBRTask task = (CBRTask) it.next();
				Task taskXML = of.createTask();
				taskXML.setName(task.getName());
				taskXML.setDescription(task.getDescription());
				list.add(taskXML);
			}
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(tasksXML, fos);
		} catch (javax.xml.bind.JAXBException je) {
			CBRLogger.log("jcolibri.core.PrototypeTasksRegistry",
					"storeRegistry", je);
			throw new jcolibri.cbrcore.exception.InternalException(je);
		}
		try {
			fos.close();
			File oldFile = new File(fileName);
			oldFile.delete();
			file.renameTo(oldFile);
			CBRLogger.log("jcolibri.core.PrototypeTasksRegistry",
					CBRLogger.INFO, "writting task registry at:"
							+ oldFile.getAbsolutePath());
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeTasksRegistry",
					"storeRegistry", ioe);
			throw new jcolibri.cbrcore.exception.InternalException(ioe);
		}
	}

	/**
	 * Return a Collection of CBRTasks
	 * 
	 * @return Collection
	 */
	public Collection<CBRTask> getTasks() {
		return tasksL;
	}

	/**
	 * Return a specific task by its name.
	 * 
	 * @param taskName
	 * @return CBRTask
	 */
	public CBRTask getTask(String taskName) {
		return (CBRTask) tasksHM.get(taskName);
	}

	/**
	 * Returns a specific task by its index.
	 * 
	 * @param idx
	 * @return
	 */
	public CBRTask getTask(int idx) {
		if ((idx >= 0) && (idx < tasksL.size()))
			return (CBRTask) tasksL.get(idx);
		else
			return null;
	}

	/**
	 * Remove a task by its index.
	 * 
	 * @param idx
	 *            index.
	 */
	public void removeTask(int idx) {
		if ((idx >= 0) && (idx < tasksL.size())) {
			tasksHM.remove(getTask(idx).getName());
			tasksL.remove(idx);
			fireRegistryChanged(this);
		}
	}

	/**
	 * Adds a new listener of changes in the manager.
	 * 
	 * @param listener
	 */
	public void addRegistryChangeListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listner of changes in the manager.
	 * 
	 * @param listener
	 */
	public void removeRegistryChangeListener(RegistryChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify changes to all the listeners.
	 */
	public void notifyChanges() {
		fireRegistryChanged(this);
	}

	/**
	 * Send a RegistryChangeEvent to all the listeners.
	 * 
	 * @param source
	 */
	private void fireRegistryChanged(Object source) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			RegistryChangeListener l = (RegistryChangeListener) it.next();
			l.registryChanged(new RegistryChangeEvent(source));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator it = tasksL.iterator(); it.hasNext();) {
			CBRTask task = (CBRTask) it.next();
			sb.append("Name=");
			sb.append(task.getName());
			sb.append(" Description=");
			sb.append(task.getDescription());
			sb.append("\n");
		}
		return sb.toString();
	}
}
