package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import jcolibri.cbrcore.event.TaskChangeEvent;
import jcolibri.cbrcore.event.TaskChangeListener;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.cbrcore.exception.PrototypeNotAvailableException;
import jcolibri.util.CBRLogger;
import jcolibri.util.JColibriClassHelper;

/**
 * Represent the task concept inside the framework. Instance of this class will
 * represent protypic until are cloned to get a new instance. This tasks
 * represent the goal that are set on the system.
 */
public class CBRTask implements Serializable, CBRTerm {
	private static final long serialVersionUID = 1L;

	/** Listeners. */
	private LinkedList<TaskChangeListener> listeners = new LinkedList<TaskChangeListener>();

	/** Name of the task. */
	private String name;

	/** Method instance that is asigned to this task instance at this moment. */
	private CBRMethod methodInstance;

	/** Description of the task. */
	private String description;

	/** Name of this task instance. */
	private String instanceName;

	/**
	 * Creates a new task with its name and description
	 * 
	 * @param taskName
	 * @param description
	 */
	public CBRTask(String taskName, String description) {
		this.name = taskName;
		this.description = description;
	}

	/**
	 * Returns the name of the task
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the task.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the name of this task instance once clones
	 * 
	 * @return name of the instance once cloned
	 */
	public final String getInstanceName() {
		return instanceName;
	}

	/**
	 * Set the instance name for a cloned task
	 * 
	 * @param name
	 */
	public final void setInstanceName(String name) {
		this.instanceName = name;
	}

	/**
	 * Return the description of this task
	 * 
	 * @return a description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the task.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * If available return the method instance currently assigned to this task
	 * instance
	 * 
	 * @return CBRMethod
	 */
	public CBRMethod getMethodInstance() {
		return methodInstance;
	}

	/**
	 * Sets the method instance that is going to solve this task instance
	 * 
	 * @param methodInstance
	 */
	public void setMethodInstance(CBRMethod methodInstance) {
		this.methodInstance = methodInstance;
		fireTaskChanged(this);
	}

	/**
	 * Solves this task, applying the method instance currently associated
	 * 
	 * @param context
	 *            Input context to achive the goal
	 * @return context updated with the result of the method execution
	 * @throws ExecutionException
	 */
	public CBRContext solve(CBRContext context) throws ExecutionException {
		if (methodInstance == null)
			return context;
		else
			return methodInstance.execute(context);
	}

	/**
	 * Register a listner that will take care of any method change on this task
	 * 
	 * @param listener
	 */
	public void addTaskChangeListener(TaskChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given lister
	 * 
	 * @param listener
	 */
	public void removeTaskChangeListener(TaskChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sends a TaskChangeEvent to all the listener.
	 * 
	 * @param source
	 *            task that was modified.
	 */
	private void fireTaskChanged(Object source) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			TaskChangeListener l = (TaskChangeListener) it.next();
			l.taskChanged(new TaskChangeEvent(source));
		}
	}

	/**
	 * Clones the given prototype task to get a new instance.
	 * 
	 * @param theTask
	 *            the task to be cloned
	 * @return new CBRTask instacen
	 * @throws PrototypeNotAvailableException
	 */
	public static CBRTask copy(CBRTask theTask)
			throws PrototypeNotAvailableException {
		try {
			String ser = JColibriClassHelper.serializeObject(theTask);
			Object o = JColibriClassHelper.deserializeObject(ser);
			return (CBRTask) o;
		} catch (java.io.IOException ioe) {
			CBRLogger.log("jcolibri.core.CBRTask", "copy", ioe);
			throw new PrototypeNotAvailableException(ioe);
		} catch (java.lang.ClassNotFoundException cnfe) {
			CBRLogger.log("jcolibri.core.CBRTask", "copy", cnfe);
			throw new PrototypeNotAvailableException(cnfe);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

}