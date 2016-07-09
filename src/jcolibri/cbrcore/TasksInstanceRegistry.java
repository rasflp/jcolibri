package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import jcolibri.cbrcore.exception.PrototypeNotAvailableException;
import jcolibri.util.CBRLogger;

/**
 * Registry for currently created tasks instances. This object will hold all the
 * current configured tasks, so also represents the operational status of the
 * CBR applicaction being configured. This class implemetns a Singleton
 * approach.
 */
public class TasksInstanceRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String, HashMap<String, CBRTask>> instancesByPrototypeHash;

	/** Name of the root task. */
	public static final String ROOT_TASK = "CBR System";

	/** Name of the precycle task. */
	public static final String PRECYCLE_TASK = "PreCycle";

	/** Name of the cycle task. */
	public static final String CYCLE_TASK = "CBR Cycle";

	/** Name of the postcycle task. */
	public static final String POSTCYCLE_TASK = "PostCycle";

	/** Unique instance of this class. */
	private static TasksInstanceRegistry registry;

	/**
	 * Constructor.
	 */
	private TasksInstanceRegistry() {
		instancesByPrototypeHash = new HashMap<String, HashMap<String, CBRTask>>();
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static TasksInstanceRegistry getInstance() {
		if (registry == null) {
			registry = new TasksInstanceRegistry();
		}
		return registry;
	}

    /**
     * Clears the registry
     */
    public void clean()
    {
        instancesByPrototypeHash.clear();
    }
    
	/**
	 * Sets the unique instance of this class.
	 * 
	 * @param registry
	 *            unique instance of this class.
	 */
	protected static void setInstance(TasksInstanceRegistry registry) {
		TasksInstanceRegistry.registry = registry;
	}

	/**
	 * Returns all available task instances for an specific prototype
	 * 
	 * @param taskPrototype
	 * @return Collection
	 */
	public Collection<CBRTask> getTaskInstances(String taskPrototype) {
		HashMap<String,CBRTask> result = instancesByPrototypeHash.get(taskPrototype);
		Collection<CBRTask> col = null;
		if (result != null)
			col = result.values();
		return (col == null ? new Vector<CBRTask>() : col);
	}

	/**
	 * Get a task instance
	 * 
	 * @param taskPrototype
	 *            Protoype cloned to get the instance
	 * @param taskInstance
	 *            Name of the instance class
	 * @return
	 */
	public CBRTask getTaskInstance(String taskPrototype, String taskInstance) {
		HashMap<String,CBRTask> o = instancesByPrototypeHash.get(taskPrototype);
		if (o != null) {
			CBRTask cbrTask = o.get(taskInstance);
			return cbrTask;
		} else {
			return null;
		}
	}

	/**
	 * Create a new task instance.
	 * 
	 * @param taskPrototype
	 *            Prototype that is going to be cloned
	 * @param taskInstance
	 *            Name for the future instance
	 * @return new CBRTask instance
	 * @throws PrototypeNotAvailableException
	 */
	public CBRTask createInstance(String taskPrototype, String taskInstance)
			throws PrototypeNotAvailableException {
		CBRTask prototype = PrototypeTasksRegistry.getInstance().getTask(
				taskPrototype);
		if (prototype != null) {
			CBRTask instance = CBRTask.copy(prototype);
			instance.setInstanceName(taskInstance);
			HashMap<String,CBRTask> o = instancesByPrototypeHash.get(taskPrototype);
			if (o != null) {
				o.put(taskInstance, instance);
			} else {
				HashMap<String,CBRTask> hm = new HashMap<String,CBRTask>();
				hm.put(taskInstance, instance);
				instancesByPrototypeHash.put(taskPrototype, hm);
			}
			return instance;
		} else {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Fail to create instance " + taskInstance + " of task "
							+ taskPrototype);
			throw new PrototypeNotAvailableException(taskPrototype);
		}
	}

}
