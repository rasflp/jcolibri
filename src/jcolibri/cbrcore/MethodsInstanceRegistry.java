package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import jcolibri.cbrcore.exception.PrototypeNotAvailableException;
import jcolibri.util.CBRLogger;

/**
 * Registry for currently created method instances. This object will hold all
 * the current configured methods, so also represents the operational status of
 * the CBR applicaction being configured. This class implemetns a Singleton
 * approach.
 */
public class MethodsInstanceRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Collection of CBRMethod. */
	private HashMap<String,HashMap<String,CBRMethod>> instancesByPrototypeHash;

	/** Unique instance of this class. */
	private static MethodsInstanceRegistry registry;

	/**
	 * Constructor.
	 */
	private MethodsInstanceRegistry() {
		instancesByPrototypeHash = new HashMap<String,HashMap<String,CBRMethod>>();
	}

	/**
	 * Return the instance of this registry.
	 * 
	 * @return MethodsInstanceRegistry reference
	 */
	public static MethodsInstanceRegistry getInstance() {
		if (registry == null) {
			registry = new MethodsInstanceRegistry();
		}
		return registry;
	}

    /**
     * Clears the registry
     */
    public void clear()
    {
        instancesByPrototypeHash.clear();
    }
    
	/**
	 * Sets the unique instance of this class.
	 * 
	 * @param registry
	 */
	protected static void setInstance(MethodsInstanceRegistry registry) {
		MethodsInstanceRegistry.registry = registry;
	}

	/**
	 * Returns the list of all method instances for a given prototype.
	 * 
	 * @param methodPrototype
	 * @return Collection
	 */
	public Collection<CBRMethod> getMethodInstances(String methodPrototype) {
		HashMap<String,CBRMethod> result = instancesByPrototypeHash
				.get(methodPrototype);
		Collection<CBRMethod> col = null;
		if (result != null)
			col = result.values();
		return (col == null ? new Vector<CBRMethod>() : col);
	}

	/**
	 * Returns a specific method instance
	 * 
	 * @param methodPrototype
	 *            Protype from where the instance was created
	 * @param methodInstance
	 *            name of the method instance
	 * @return CBRMethod
	 */
	public CBRMethod getMethodInstance(String methodPrototype,
			String methodInstance) {
		HashMap<String,CBRMethod> o = instancesByPrototypeHash.get(methodPrototype);
		if (o != null) {
			CBRMethod cbrmethod = o.get(methodInstance);
			return cbrmethod;
		} else {
			return null;
		}
	}

	/**
	 * Creates a new method instace
	 * 
	 * @param methodPrototype
	 *            Name of the Prototype to be cloned
	 * @param methodInstance
	 *            Name that the method instance will have
	 * @return CBRMethod
	 * @throws PrototypeNotAvailableException
	 */
	public CBRMethod createInstance(String methodPrototype,
			String methodInstance) throws PrototypeNotAvailableException {
		CBRMethod prototype = PrototypeMethodsRegistry.getInstance().getMethod(
				methodPrototype);
		if (prototype != null)
			return createInstance(prototype, methodInstance);
		else {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Fail to create instance " + methodInstance + " of method "
							+ prototype);
			throw new PrototypeNotAvailableException(methodPrototype);
		}
	}

	/**
	 * Creates a new method instace
	 * 
	 * @param prototype
	 *            Prototype to be cloned
	 * @param methodInstance
	 *            Name that the method instance will have
	 * @return CBRMethod
	 * @throws PrototypeNotAvailableException
	 */
	public CBRMethod createInstance(CBRMethod prototype, String methodInstance)
			throws PrototypeNotAvailableException {
		if (prototype != null) {
			CBRMethod instance = CBRMethod.copy(prototype);
			instance.setInstanceName(methodInstance);
			HashMap<String,CBRMethod> o = instancesByPrototypeHash.get(prototype.getName());
			if (o != null) {
				o.put(methodInstance, instance);
			} else {
				HashMap<String, CBRMethod> hm = new HashMap<String, CBRMethod>();
				hm.put(methodInstance, instance);
				instancesByPrototypeHash.put(prototype.getName(), hm);
			}
			return instance;
		} else {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Fail to create instance " + methodInstance + " of method "
							+ prototype);
			throw new PrototypeNotAvailableException(prototype.getName());
		}
	}

}
