package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.cbrcore.exception.PrototypeNotAvailableException;
import jcolibri.util.CBRLogger;
import jcolibri.util.JColibriClassHelper;

/**
 * Represnts any method that will solve a CBRTask on this system. Dinamically
 * created intances of this class will be assogned to CBRTask to finally achieve
 * the desired goal of the configured CBR application.
 */
public abstract class CBRMethod implements Serializable, CBRTerm {

	/** Parameters of the method. */
	HashMap<String, Object> parameters;

	/** Name of the instance. */
	String instanceName;

	/** If the java class that implements this method is available. */
	boolean implementationAvailable = false;

	/** Name of the method. */
	String name;

	/** Tasks that this method solve. */
	List<String> competencies;

	/** Description of the method. */
	String informalDescription;

	/** Type of the method: descomposition or resolution. */
	MethodType type;

	/** Information about the parameters of the method */
	List<MethodParameter> parametersInfo;

	/**
	 * If this method is a descomposition method, list of subtasks on which this
	 * method will decompose the original one.
	 */
	List<String> subtasks;

	/** PostConditions of the method. */
	HashMap<String, Object> postConditionsInfo;

	/** Preconditions of the method. */
	HashMap<String, Object> preConditionsInfo;

	/**
	 * Instance of this class will implement this abstract method with all the
	 * needed logic to achieve the goal identified by the task for which this
	 * method has been assigned for. Instance of this class will be filled with
	 * data coming from the XML declarations for this methods, and will remain
	 * as prototypes until a the clone method is created.
	 * 
	 * @param context
	 *            context prior to method execution.
	 * @return context updated after method execution.
	 */
	public abstract CBRContext execute(CBRContext context)
			throws ExecutionException;

	/**
	 * Sets the name of this protype method.
	 * 
	 * @param name
	 *            name of this protype method.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this protype method
	 * 
	 * @return name of this protype method.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method will return a list of tasks for which this method can be
	 * used.
	 * 
	 * @return LinkedList, the list of competencies
	 */
	public final List<String> getCompetencies() {
		return competencies;
	}

	/**
	 * Sets the list of subtasks on which this method will decompose the
	 * original one
	 * 
	 * @param competencies
	 */
	public final void setCompetencies(List<String> competencies) {
		this.competencies = competencies;
	}

	/**
	 * Returns a description of the method functionality
	 * 
	 * @return String
	 */
	public final String getInformalDescription() {
		return informalDescription;
	}

	/**
	 * Must specify a description of the method functionality.
	 * 
	 * @return String
	 */
	public final void setInformalDescription(String informalDescription) {
		this.informalDescription = informalDescription;
	}

	/**
	 * Specifies what kind of method is this one.
	 * 
	 * @return MethodType, the method can be a Resolution or a Descomposition
	 *         one.
	 */
	public final MethodType getMethodType() {
		return type;
	}

	/**
	 * Sets the method type for this method, execution or deccomposition
	 * 
	 * @param type
	 * @see MethodType
	 */
	public final void setMethodType(MethodType type) {
		this.type = type;
	}

	/**
	 * Must return the name of the CBR method implementation.
	 * 
	 * @return String, name of the method.
	 */
	public final String getInstanceName() {
		return instanceName;
	}

	/**
	 * Sets the name of the method instance.
	 */
	public final void setInstanceName(String name) {
		this.instanceName = name;
	}

	/**
	 * This method will return the parameters that this method needs for
	 * execution Generally this parameters will be introduced by the user before
	 * this method at configuration time.
	 * 
	 * @return List, List of MethodParameter instances
	 */
	public final List<MethodParameter> getParametersInfo() {
		return parametersInfo;
	}

	/**
	 * Sets the parameter info for this method
	 * 
	 * @param parametersInfo
	 *            List of MethodParameter instances
	 */
	public final void setParametersInfo(List<MethodParameter> parametersInfo) {
		this.parametersInfo = parametersInfo;
	}

	/**
	 * This method will return the pre conditions that this method needs for
	 * execution
	 * 
	 * @return HashMap, Map of conditions, identified by classname or parameter
	 *         name
	 */
	public final HashMap<String, Object> getPreConditionsInfo() {
		return preConditionsInfo;
	}

	/**
	 * Sets the pre conditions info for this method
	 * 
	 * @param preConditionsInfo
	 *            Map of conditions
	 */
	public final void setPreConditionsInfo(HashMap<String, Object> preConditionsInfo) {
		this.preConditionsInfo = preConditionsInfo;
	}

	/**
	 * This method will return the post conditions that this add to the context
	 * after its execution
	 * 
	 * @return HashMap, Map of conditions, identified by classname or parameter
	 *         name
	 */
	public final HashMap<String, Object> getPostConditionsInfo() {
		return postConditionsInfo;
	}

	/**
	 * Sets the post conditions info for this method
	 * 
	 * @param postConditionsInfo
	 *            Map of conditions
	 */
	public final void setPostConditionsInfo(HashMap<String, Object> postConditionsInfo) {
		this.postConditionsInfo = postConditionsInfo;
	}

	/**
	 * This method will be used to set to the method the parameters filled by
	 * the user at configuration time.
	 * 
	 * @param parameters
	 *            HashMap with parameters value, following a name-value
	 *            strategy.
	 */
	public final void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * This method will be used return the filled parameters values to CBRMethod
	 * instances.
	 * 
	 * @return the value object for that parameter
	 */
	public final Object getParameterValue(String name) {
		if (parameters != null)
			return parameters.get(name);
		else
			return null;
	}

	/**
	 * This can be remade later. Now this method needs only to be implemented by
	 * DECOMPOSITION type methods
	 */
	public final List<String> getSubTasks() {
		return subtasks;
	}

	/**
	 * Set the list of substaks for this decomposition method
	 * 
	 * @param subtasks
	 */
	public final void setSubTasks(List<String> subtasks) {
		this.subtasks = subtasks;
	}

	/**
	 * Clone method implementation. It will create instances of the prototype
	 * method
	 * 
	 * @param theMethod
	 *            method to be cloned
	 * @return a new intance equal to the protype method
	 * @throws PrototypeNotAvailableException
	 */
	public static CBRMethod copy(CBRMethod theMethod)
			throws PrototypeNotAvailableException {
		try {
			String ser = JColibriClassHelper.serializeObject(theMethod);
			Object o = JColibriClassHelper.deserializeObject(ser);
			return (CBRMethod) o;
		} catch (java.io.IOException ioe) {
			CBRLogger.log("jcolibri.core.CBRMethod", "copy", ioe);
			throw new PrototypeNotAvailableException(ioe);
		} catch (java.lang.ClassNotFoundException cnfe) {
			CBRLogger.log("jcolibri.core.CBRMethod", "copy", cnfe);
			throw new PrototypeNotAvailableException(cnfe);
		}
	}

	/**
	 * Returns true if there is an implementation ready for this method
	 * declaration
	 * 
	 * @return true if there is an implementation ready for this method
	 *         declaration
	 */
	public boolean isImplementationAvailable() {
		return implementationAvailable;
	}

	/**
	 * Sets if there is an implementation ready for this method declaration
	 * 
	 * @param value
	 *            true if there is an implementation ready for this method
	 *            declaration
	 */
	public void setImplementationAvailable(boolean value) {
		implementationAvailable = value;
	}

}
