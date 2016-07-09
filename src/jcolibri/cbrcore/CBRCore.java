package jcolibri.cbrcore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcore.exception.IncompatibleMethodException;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.exception.InternalException;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.util.CBRLogger;

/**
 * This class will manage the framework. A new instance of this class must be
 * created for any CBR application that wants to be run.
 */
public class CBRCore implements Serializable {
	private static final long serialVersionUID = 1L;

	private CBRContext context;

	private CBRState state;

	private String name;

	/**
	 * Creates a new CBR application
	 * 
	 * @param name
	 *            of the apliaction
	 */
	public CBRCore(String name) {
		this.name = name;
	}

	/**
	 * Creates a new CBR application
	 */
	public CBRCore() {
		this.name = "";
	}

	/**
	 * Returns the name of the application
	 * 
	 * @return the name of the application
	 */
	public String getName() {
		return name;
	}

	private void loadPackages() {
		Iterator it;

		try {
			PrototypeMethodsRegistry.getInstance().cleanRegistry();
			PrototypeTasksRegistry.getInstance().cleanRegistry();
			LocalSimilarityRegistry.getInstance().clear();
			GlobalSimilarityRegistry.getInstance().clear();
			DataTypesRegistry.getInstance().clearRegistry();

			it = PackageManager.getInstance().getActivePackages().iterator();
			while (it.hasNext()) {
				PackageInfo pi = (PackageInfo) it.next();
				DataTypesRegistry.getInstance().loadPackage(pi);
			}

			it = PackageManager.getInstance().getActivePackages().iterator();
			while (it.hasNext()) {
				PackageInfo pi = (PackageInfo) it.next();

				CBRLogger.log(this.getClass(), CBRLogger.INFO,
						"Loading package: " + pi.getName());
				PrototypeMethodsRegistry.getInstance().loadPackage(pi);
				PrototypeTasksRegistry.getInstance().loadPackage(pi);
				// DataTypesRegistry.getInstance().loadPackage(pi);
				LocalSimilarityRegistry.getInstance().loadPackage(pi);
				GlobalSimilarityRegistry.getInstance().loadPackage(pi);
			}
		} catch (InitializingException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			loadPackages();
			state = new CBRState();
			context = new CBRContext();

			CBRMethod method = MethodsInstanceRegistry.getInstance()
					.createInstance(
							"jcolibri.method.CBRSystemMethod",
							"jcolibri.method.CBRSystemMethod"
									+ new Double(Math.random() * 10000)
											.intValue());
			CBRTask task = (CBRTask) TasksInstanceRegistry.getInstance()
					.getTaskInstances(TasksInstanceRegistry.ROOT_TASK)
					.iterator().next();
			this.resolveTaskWithMethod(task, method);
			CBRTask[] _tasks = state.getChildren(task);
			this.preCycleTask = _tasks[0];
			this.cycleTask = _tasks[1];
			this.postCycleTask = _tasks[2];

			CBRLogger.log(this.getClass(), CBRLogger.INFO, "CBR System Ready");

		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Stores on disk current tasks and methods registries
	 * 
	 */
	public void persistPrototypeRegistries() {
		javax.swing.JOptionPane.showMessageDialog(null,
				"Registry store is unavailable");
		/*
		 * try { PrototypeMethodsRegistry.getInstance().storeRegistry(methods);
		 * PrototypeTasksRegistry.getInstance().storeRegistry(tasks); } catch
		 * (InternalException ie) { ie.printStackTrace(); System.exit(1); }
		 */
	}

	/**
	 * This method stores the configuration currently set for this application
	 * 
	 * @param filename
	 *            Name of the file where the application data is going to be
	 *            stored
	 * @throws InternalException
	 *             In case any error happens when trying to store the
	 *             application
	 */
	public void persistApplication(String filename) throws InternalException {
		try {

			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.writeObject(MethodsInstanceRegistry.getInstance());
			oos.writeObject(TasksInstanceRegistry.getInstance());
			oos.writeObject(PackageManager.getInstance());
			oos.flush();
			oos.close();
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Application saved to: " + filename);
		} catch (java.io.IOException ioe) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Failure storing the application");
			throw new InternalException(ioe);
		}
	}

	public void loadApplication(String filename) throws InternalException {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			CBRCore theCore = (CBRCore) ois.readObject();
			MethodsInstanceRegistry mreg = (MethodsInstanceRegistry) ois
					.readObject();
			TasksInstanceRegistry treg = (TasksInstanceRegistry) ois
					.readObject();
			PackageManager pkgMngr = (PackageManager) ois.readObject();
			this.name = theCore.getName();
			this.state = theCore.getCBRState();
			this.state.restoreTransient();
			this.context = theCore.getContext();
			PackageManager.setInstance(pkgMngr);
			this.loadPackages();
			// this.resetContext();
			MethodsInstanceRegistry.setInstance(mreg);
			TasksInstanceRegistry.setInstance(treg);
			this.preCycleTask = theCore.preCycleTask;
			this.cycleTask = theCore.cycleTask;
			this.postCycleTask = theCore.postCycleTask;
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Application loaded from: " + filename);
		} catch (java.io.IOException ioe) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Failure reading the application");
			throw new InternalException(ioe);
		} catch (Exception cnfe) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Failure reading the application");
			throw new InternalException(cnfe);
		}
	}

	/**
	 * Sets the query to be resoved by the CBR appliaction
	 * 
	 * @param query
	 */
	public void setQuery(CBRQuery query) {
		//context = new CBRContext(); REMOVED 1.1
		context.setQuery(query);
	}

	/**
	 * Returns the actual context of the appliaction
	 * 
	 * @return CBRContext instance
	 */
	public CBRContext getContext() {
		return context;
	}

	/**
	 * Execute the given task inside this CBR application
	 * 
	 * @param task
	 *            The name of the task instance that wants to be solved
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public void executeTask(CBRTask task)
			throws jcolibri.cbrcore.exception.ExecutionException {
		context = task.solve(context);
	}

	/**
	 * Execute the list given tasks in secuence inside this CBR application
	 * 
	 * @param tasks
	 *            Names of the tasks that wants to be solved
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public void executeTasks(CBRTask[] tasks)
			throws jcolibri.cbrcore.exception.ExecutionException {
		for (int i = 0; i < tasks.length; i++) {
			context = tasks[i].solve(context);
		}
	}

	private void checkAgainstTask(CBRTask task, CBRMethod method)
			throws IncompatibleMethodException {
		List tasks = method.getCompetencies();
		for (Iterator it = tasks.iterator(); it.hasNext();) {
			if (it.next().equals(task.getName()))
				return;
		}
		throw new IncompatibleMethodException("Method:" + method.getName()
				+ " can not be used to solve given task: Competence failure:"
				+ task.getName());
	}

	/**
	 * Assigns one task with one method instance on this appliaction
	 * 
	 * @param task
	 *            Name of the task
	 * @param methodInstance
	 *            CBRMethod instance that will be used to solve this task
	 * @return List of Task instance that may result from the execution of this
	 *         method. This will only occur if er are applying a descomposition
	 *         method
	 */
	public CBRTask[] resolveTaskWithMethod(CBRTask task,
			CBRMethod methodInstance) throws IncompatibleMethodException {
		checkAgainstTask(task, methodInstance);
		context.addMethodToExecution(methodInstance);
		task.setMethodInstance(methodInstance);
		return state.getChildren(task);
	}

	private LinkedList<CBRTask> taskTreeOrder(LinkedList<CBRTask> list, CBRTask task) {
		CBRTask[] children = getCBRState().getChildren(task);
		if (children == null || children.length == 0) {
			list.add(task);
		} else {
			list.add(task);
			for (int i = 0; i < children.length; i++) {
				taskTreeOrder(list, children[i]);
			}
		}
		return list;
	}

	/**
	 * Returns the list of current planned tasks on the applicaiton
	 * 
	 * @param task
	 *            from which the children tasks are discovered
	 * @return
	 */
	public LinkedList<CBRTask> getPlannedTasks(CBRTask task) {
		return taskTreeOrder(new LinkedList<CBRTask>(), task);
	}

	public void resetContext() throws IncompatibleMethodException {
		context = new CBRContext();
		LinkedList list = getPlannedTasks(getRootTask());
		CBRTask task;
		CBRMethod method;
		for (Iterator it = list.iterator(); it.hasNext();) {
			task = (CBRTask) it.next();
			method = task.getMethodInstance();
			if (method != null) {
				context.addMethodToExecution(method);
			}
		}
		CBRLogger.log(this.getClass(), CBRLogger.INFO, "Context Reset");

	}

	/**
	 * Method to deassign a method instance from resolving a task
	 * 
	 * @param task
	 *            Name of the task
	 */
	public void resetTaskWithMethod(CBRTask task)
			throws IncompatibleMethodException {
		CBRMethod method = task.getMethodInstance();
		if (method != null) {
			task.setMethodInstance(null);
			resetContext();
		}
	}

	/**
	 * Returns the current configuration state of the appliaction
	 * 
	 * @return CBRState
	 */
	public CBRState getCBRState() {
		return state;
	}

	/**
	 * Returns the ROOT task for this CBR application
	 * 
	 * @return CBRTask
	 */
	public CBRTask getRootTask() {
		return state.getRootTask().getTask();
	}

	private CBRTask preCycleTask;

	private CBRTask cycleTask;

	private CBRTask postCycleTask;

	public CBRTask getCycleTask() {
		return cycleTask;
	}

	public CBRTask getPostCycleTask() {
		return postCycleTask;
	}

	public CBRTask getPreCycleTask() {
		return preCycleTask;
	}

}
