package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcore.event.StateChangeEvent;
import jcolibri.cbrcore.event.StateChangeListener;
import jcolibri.cbrcore.event.TaskChangeEvent;
import jcolibri.cbrcore.event.TaskChangeListener;
import jcolibri.cbrcore.exception.DataInconsistencyException;
import jcolibri.util.CBRLogger;

/**
 * Instance of this class will hold the current configuration status of the CBR
 * appliaction being developed.
 */
public class CBRState implements TaskChangeListener, Serializable {
	private static final long serialVersionUID = 1L;

	/** Tree of tasks. */
	HashMap<String,CBRTaskNode> tree = new HashMap<String,CBRTaskNode>();

	/** Listeners. */
	transient List<StateChangeListener> listeners = Collections.synchronizedList(new ArrayList<StateChangeListener>());

	/**
	 * Creates a new instance of CBRState.
	 */
	public CBRState() {
		CBRTask rootTask = TasksInstanceRegistry.getInstance().createInstance(
				TasksInstanceRegistry.ROOT_TASK,
				TasksInstanceRegistry.ROOT_TASK);
		rootTask.addTaskChangeListener(this);
		addRootTask(rootTask);
	}

	/**
	 * Clear the listeners list.
	 */
	protected void restoreTransient() {
		listeners = Collections.synchronizedList(new ArrayList<StateChangeListener>());
	}

	/**
	 * Returns the ROOT task on this application
	 * 
	 * @return the ROOT task on this application
	 */
	public CBRTaskNode getRootTask() {
		return (CBRTaskNode) tree.get(TasksInstanceRegistry.ROOT_TASK);
	}

	/**
	 * Register one more listener to the changes produced during the
	 * configuration of the CBR application
	 * 
	 * @param listener
	 *            Callback object
	 */
	public synchronized void addStateChangeListener(StateChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Deregister one listener from the changes produced during the
	 * configuration of the CBR application
	 * 
	 * @param listener
	 *            Callback object
	 */
	public synchronized void removeStateChangeListener(
			StateChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sends a StateChageEvent to all the listeners.
	 * 
	 * @param source
	 *            modified task.
	 */
	private synchronized void fireStateChanged(Object source) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			StateChangeListener l = (StateChangeListener) it.next();
			l.stateChanged(new StateChangeEvent(source));
		}
	}

	/**
	 * Adds a tasks as the root of the tasks tree.
	 * 
	 * @param task
	 */
	private void addRootTask(CBRTask task) {
		tree.put(task.getInstanceName(),
				new CBRTaskNode(task, new LinkedList()));
	}

	/**
	 * Sends a StateChageEvent setting a task as source.
	 * 
	 * @param task
	 *            tasks that was modified.
	 */
	private void updateTask(CBRTask task) {
		CBRTaskNode node = (CBRTaskNode) tree.get(task.getInstanceName());
		fireStateChanged(node);
	}

	/**
	 * Sets a list of tasks as children of another task of the tree.
	 * 
	 * @param parent
	 *            task of the tree.
	 * @param newTaskNodes
	 *            new children tasks of parent task.
	 */
	private void addTasksBecauseOf(CBRTask parent, LinkedList newTaskNodes) {
		CBRTaskNode node = (CBRTaskNode) tree.get(parent.getInstanceName());
		node.setChildren(newTaskNodes);
		for (Iterator it = newTaskNodes.iterator(); it.hasNext();) {
			CBRTaskNode newNode = (CBRTaskNode) it.next();
			newNode.setParentRef(node);
			CBRLogger.log(this.getClass(), CBRLogger.INFO, "Adding:"
					+ newNode.getTask().getName());
			tree.put(newNode.getTask().getInstanceName(), newNode);
		}
		fireStateChanged(node);
	}

	/**
	 * Remove all the children of a task from the tree.
	 * 
	 * @param task
	 */
	private void removeChildren(CBRTaskNode task) {
		LinkedList list = task.getChildren();
		for (Iterator it = list.iterator(); it.hasNext();) {
			CBRTaskNode newTask = (CBRTaskNode) it.next();
			removeChildren(newTask);
			tree.remove(newTask.getTask().getInstanceName());
		}
		task.setChildren(new LinkedList());
		fireStateChanged(task);
	}

	/**
	 * Remove all the children of a task from the tree.
	 * 
	 * @param parent
	 */
	private void removeChildren(CBRTask parent) {
		CBRTaskNode task = (CBRTaskNode) tree.get(parent.getInstanceName());
		removeChildren(task);
	}

	/**
	 * Process a change produced in any task instance. If the change it is
	 * because of an association of a execution method then the task will be
	 * just marked as ready to be executed, but if it was a decomposition method
	 * the information from method will be get and the task will be decomposed
	 * on the subtasks indicated by the method, modifying current CBR state.
	 */
	public void taskChanged(TaskChangeEvent event) {
		CBRTask task = (CBRTask) event.getSource();
		CBRMethod methodInstance = task.getMethodInstance();
		if (methodInstance != null) {
			CBRLogger.log(this.getClass(), CBRLogger.INFO, "Task changed:"
					+ task.getName() + " Setting method instance: "
					+ methodInstance);
			if (methodInstance.getMethodType().equals(MethodType.DECOMPOSITION)) {
				List<String> subtasks = methodInstance.getSubTasks();
				LinkedList<CBRTaskNode> list = new LinkedList<CBRTaskNode>();
				for (Iterator<String> it = subtasks.iterator(); it.hasNext();) {
					String taskName =  it.next();
					CBRTask newTask = TasksInstanceRegistry.getInstance()
							.createInstance(
									taskName,
									taskName
											+ new Double(Math.random() * 10000)
													.intValue());
					if (newTask == null) {
						DataInconsistencyException die = new DataInconsistencyException(
								"A DECOMPOSITION method is returning a task not defined on the system");
						CBRLogger.log(this.getClass(), "taskChanged", die);
						throw die;
					}
					// to do chapuza por ahora me suscribo a todas las tareas
					// para siempre
					newTask.addTaskChangeListener(this);
					list.addLast(new CBRTaskNode(newTask));
				}
				addTasksBecauseOf(task, list);
			} else {
				updateTask(task);
			}
		} else {
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Task changed (removed):" + task.getName());
			removeChildren(task);
		}
	}

	/**
	 * Returns the task instances that are children of a given task on this CBR
	 * task configuration hierarchy
	 * 
	 * @param task
	 * @return
	 */
	public CBRTask[] getChildren(CBRTask task) {
		CBRTaskNode node = (CBRTaskNode) tree.get(task.getInstanceName());
		if (node != null) {
			LinkedList list = node.getChildren();
			CBRTask[] result = new CBRTask[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext();) {
				CBRTaskNode newTask = (CBRTaskNode) it.next();
				result[i++] = newTask.getTask();
			}
			return result;
		} else
			return new CBRTask[0];
	}
}
