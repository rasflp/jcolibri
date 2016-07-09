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
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jcolibri.cbrcore.event.RegistryChangeEvent;
import jcolibri.cbrcore.event.RegistryChangeListener;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.cbrcore.exception.InternalException;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.util.CBRLogger;
import jcolibri.xml.methods.Competencies;
import jcolibri.xml.methods.ContextConditionType;
import jcolibri.xml.methods.ContextInputPrecondition;
import jcolibri.xml.methods.ContextInputPreconditionType;
import jcolibri.xml.methods.ContextOutputPostcondition;
import jcolibri.xml.methods.ContextOutputPostconditionType;
import jcolibri.xml.methods.Methods;
import jcolibri.xml.methods.ObjectFactory;
import jcolibri.xml.methods.ParameterType;
import jcolibri.xml.methods.Parameters;
import jcolibri.xml.methods.ParametersType;
import jcolibri.xml.methods.SubTasks;
import jcolibri.xml.methods.SubTasksType;

/**
 * Manager of the methods of a package.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PrototypeMethodPkgReg {

	/** Methods indexed as a hash table. */
	private HashMap<String,CBRMethod> methodsHM;

	/** Methods indexed as a list. */
	private List<CBRMethod> methodsL;

	/** Listener of changes in this manager. */
	private LinkedList<RegistryChangeListener> listeners = new LinkedList<RegistryChangeListener>();

	/** Package. */
	private PackageInfo pkgInfo;

	/**
	 * Constructor.
	 * 
	 * @param pkgInfo
	 *            package.
	 */
	public PrototypeMethodPkgReg(PackageInfo pkgInfo) {
		methodsHM = new HashMap<String,CBRMethod>();
		methodsL = new ArrayList<CBRMethod>();
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
	 * Clears all the methods.
	 */
	public void cleanRegistry() {
		methodsHM.clear();
		methodsL.clear();
		fireRegistryChanged(this);
	}

	/**
	 * 
	 * @param methodXML
	 * @return
	 */
	@SuppressWarnings("unchecked")
    private CBRMethod buildCBRMethod(jcolibri.xml.methods.MethodType methodXML) {
		String name = methodXML.getName();
		CBRMethod method = null;
		try {
			method = (CBRMethod) Class.forName(name).newInstance();
			method.setImplementationAvailable(true);
		} catch (java.lang.ClassNotFoundException cnfe) {
			// CBRLogger.log("jcolibri.core.PrototypeMethodsRegistry","loadRegistry",cnfe);
		} catch (java.lang.InstantiationException ine) {
			CBRLogger.log("jcolibri.core.MethodsRegistry", "loadRegistry", ine);
		} catch (java.lang.IllegalAccessException ile) {
			CBRLogger.log("jcolibri.core.MethodsRegistry", "loadRegistry", ile);
		}
		if (method == null) {
			method = new CBRMethod() {
				private static final long serialVersionUID = 1L;

				public CBRContext execute(CBRContext context) {
					return null;
				}
			};
			method.setImplementationAvailable(false);
		}
		method.setName(name);
		method.setInformalDescription(methodXML.getDescription());
		method.setMethodType(new MethodType(methodXML.getType()));
		List<String> competencies = methodXML.getCompetencies().getCompetence();
		method.setCompetencies(competencies);
		SubTasksType subtaskstype = methodXML.getSubTasks();
		if (subtaskstype != null) {
			List<String> subtasks = subtaskstype.getSubTask();
			method.setSubTasks(subtasks);
		}
		ParametersType parameterstype = methodXML.getParameters();
		if (parameterstype != null) {
			List<ParameterType> parametersInfoXML = methodXML.getParameters().getParameter();
			List<MethodParameter> parametersInfo = new ArrayList<MethodParameter>();
			for (Iterator<ParameterType> it = parametersInfoXML.iterator(); it.hasNext();) {
				ParameterType paramXML =  it.next();
				MethodParameter param = readMethodParameter(paramXML);
				parametersInfo.add(param);
			}
			method.setParametersInfo(parametersInfo);
		}

		// Pre conditions
		ContextInputPreconditionType precondType = methodXML
				.getContextInputPrecondition();
		if (precondType != null) {
			method.setPreConditionsInfo(readMethodConditions(precondType.getContextCondition()));
		}

		// Post conditions
		ContextOutputPostconditionType postcondType = methodXML
				.getContextOutputPostcondition();
		if (postcondType != null) {
			method.setPostConditionsInfo(readMethodConditions(postcondType.getContextCondition()));
		}
		return method;
	}

	/**
	 * 
	 * @param precondsInfoXML
	 * @return
	 */
	private HashMap<String,Object> readMethodConditions(List<ContextConditionType> precondsInfoXML) {
		HashMap<String, Object> precondsInfo = new HashMap<String,Object>();
		for (Iterator<ContextConditionType> it = precondsInfoXML.iterator(); it.hasNext();) {
			ContextConditionType condXML =  it.next();
			if (condXML.getCBRTerm() != null) {
				String termName = condXML.getCBRTerm();
				String termMax = condXML.getMaxOccurrences();
				String termMin = condXML.getMinOccurrences();
				MethodCondition conditionInfo = new MethodCondition(termName,
						termMax, termMin);
				precondsInfo.put(termName, conditionInfo);
			} else if (condXML.getParameter() != null) {
				MethodParameter param = readMethodParameter(condXML
						.getParameter());
				precondsInfo.put(param.getName(), param);
			}
		}
		return precondsInfo;
	}

	/**
	 * 
	 * @param paramXML
	 * @return
	 */
	private MethodParameter readMethodParameter(ParameterType paramXML) {
		String paramName = paramXML.getName();
		String paramDesc = paramXML.getDescription();
		String dataType = paramXML.getDataType();
		DataType dt;
		if (dataType == null) {
			dataType = paramXML.getCBRTerm();
		}
		dt = DataTypesRegistry.getInstance().getDataType(dataType);
		return new MethodParameter(paramName, paramDesc, dt);
	}

	/**
	 * 
	 * @param name
	 * @param method
	 * @param of
	 * @return
	 * @throws javax.xml.bind.JAXBException
	 */
	@SuppressWarnings("unchecked")
    private jcolibri.xml.methods.MethodType buildXMLMethod(String name,
			CBRMethod method, ObjectFactory of)
			throws javax.xml.bind.JAXBException {
		jcolibri.xml.methods.MethodType methodXML = of.createMethod();
		methodXML.setName(name);
		methodXML.setDescription(method.getInformalDescription());
		methodXML.setType(method.getMethodType().toString());

		Competencies competenciesXML = of.createCompetencies();
		List<String> compList = competenciesXML.getCompetence();
		List methodCompetenciesList = method.getCompetencies();
		for (Iterator it = methodCompetenciesList.iterator(); it.hasNext();) {
			compList.add(it.next().toString());
		}
		methodXML.setCompetencies(competenciesXML);

		SubTasks subTasksXML = of.createSubTasks();
		List<String> subTasksList = subTasksXML.getSubTask();
		List methodSubtasksList = method.getSubTasks();
		if (methodSubtasksList != null && methodSubtasksList.size() > 0) {
			for (Iterator it = methodSubtasksList.iterator(); it.hasNext();) {
				subTasksList.add(it.next().toString());
			}
			methodXML.setSubTasks(subTasksXML);
		}

		Parameters paramsXML = of.createParameters();
		List<ParameterType> paramsList = paramsXML.getParameter();
		List parametersInfo = method.getParametersInfo();
		if (parametersInfo != null && parametersInfo.size() > 0) {
			for (Iterator it = parametersInfo.iterator(); it.hasNext();) {
				MethodParameter m = (MethodParameter) it.next();
				String paramName = m.getName();
				String paramDescrip = m.getDescription();
				ParameterType paramXML = of.createParameter();
				paramXML.setName(paramName);
				paramXML.setDescription(paramDescrip);
				DataType dataType = ((MethodParameter) m).getType();
				if (dataType.equals(DataTypesRegistry.getCBRTERMDataType())) {
					// paramXML.setCBRTerm(dataType.getSubType().toString());
					paramXML.setDataType(DataTypesRegistry.getCBRTERMDataType()
							.getName());
				} else
					paramXML.setDataType(dataType.toString());
				paramsList.add(paramXML);
			}
			methodXML.setParameters(paramsXML);
		}

		ContextInputPrecondition preXML = of.createContextInputPrecondition();
		List<ContextConditionType> preList = preXML.getContextCondition();
		HashMap conditionsInfo = method.getPreConditionsInfo();
		buildXMLConditions(preList, conditionsInfo, of);
		if (preList.size() > 0)
			methodXML.setContextInputPrecondition(preXML);

		ContextOutputPostcondition postXML = of
				.createContextOutputPostcondition();
		List<ContextConditionType> postList = postXML.getContextCondition();
		conditionsInfo = method.getPostConditionsInfo();
		buildXMLConditions(postList, conditionsInfo, of);
		if (postList.size() > 0)
			methodXML.setContextOutputPostcondition(postXML);

		return methodXML;
	}

	/**
	 * 
	 * @param list
	 * @param conditionsInfo
	 * @param of
	 * @throws javax.xml.bind.JAXBException
	 */
	private void buildXMLConditions(List<ContextConditionType> list, HashMap conditionsInfo,
			ObjectFactory of) throws javax.xml.bind.JAXBException {
		if (conditionsInfo != null && conditionsInfo.size() > 0) {
			for (Iterator it = conditionsInfo.values().iterator(); it.hasNext();) {
				Object aux = it.next();
				ContextConditionType condTypeXML = of.createContextCondition();
				if (aux instanceof MethodCondition) {
					String condName = ((MethodCondition) aux).getName();
					String max = ((MethodCondition) aux).getMaxOcurrences();
					String min = ((MethodCondition) aux).getMinOcurrences();
					condTypeXML.setCBRTerm(condName);
					condTypeXML.setMaxOccurrences(max);
					condTypeXML.setMinOccurrences(min);

				} else if (aux instanceof MethodParameter) {
					String paramName = ((MethodParameter) aux).getName();
					String paramDescrip = ((MethodParameter) aux)
							.getDescription();
					ParameterType paramXML = of.createParameter();
					paramXML.setName(paramName);
					paramXML.setDescription(paramDescrip);
					DataType dataType = ((MethodParameter) ((MethodParameter) aux))
							.getType();
					if (dataType.equals(DataTypesRegistry.getCBRTERMDataType())) {
						// paramXML.setCBRTerm(dataType.getSubType().toString());
						paramXML.setCBRTerm(dataType.toString());
					} else {
						paramXML.setDataType(dataType.toString());
					}
					condTypeXML.setParameter(paramXML);
				}
				list.add(condTypeXML);
			}
		}
	}

	/**
	 * Loads the method of the current package.
	 * 
	 * @throws InitializingException
	 */
	public void load() throws InitializingException {
		if ((pkgInfo == null) || (pkgInfo.getMethods() == ""))
			return;

		loadRegistry(pkgInfo.getMethods());
	}

	/**
	 * Loads de methods from a file.
	 * 
	 * @param fileName
	 *            Name of the xml file satisfying methods.xsd xml schema that
	 *            has the information about current available methods
	 * @throws InitializingException
	 *             Some error has occured while loading the registry
	 */
	private void loadRegistry(String fileName) throws InitializingException {
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
			// ClassLoader.getSystemResourceAsStream(fileName);
			// if (is == null) {
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeMethodsRegistry",
					CBRLogger.ERROR, "Resource " + fileName + " not found.");
			throw new jcolibri.cbrcore.exception.InitializingException();
		}
		// If the file exists then we try to recover the tasks.
		try {
			// CHANGE r4// methodsHash = new HashMap();
			// current = fileName;
			JAXBContext jc = JAXBContext.newInstance("jcolibri.xml.methods");
			Unmarshaller u = jc.createUnmarshaller();
			Methods methodsXML = (Methods) u.unmarshal(is);
			List methodsListXML = methodsXML.getMethod();
			CBRMethod cbrMethod = null;
			for (Iterator it = methodsListXML.iterator(); it.hasNext();) {
				jcolibri.xml.methods.MethodType methodXML = (jcolibri.xml.methods.MethodType) it
						.next();
				cbrMethod = buildCBRMethod(methodXML);
				methodsHM.put(cbrMethod.getName(), cbrMethod);
				methodsL.add(cbrMethod);
			} // end for

			fireRegistryChanged(this);
		} catch (javax.xml.bind.JAXBException je) {
			CBRLogger.log("jcolibri.core.PrototypeMethodsRegistry",
					"loadRegistry()" + je.getMessage(), je);
			// throw new jcolibri.cbrcore.exception.InitializingException(je);
		}
	}

	/**
	 * Saves the methods.
	 * 
	 * @throws InternalException
	 *             Some error has occured while storing the registry
	 */
	public void storeRegistry() throws InternalException {
		storeRegistry(pkgInfo.getMethods(), methodsL);
	}

	/**
	 * Save the methods in a file.
	 * 
	 * @param fileName
	 *            Name of the file where the registry will be stored
	 * @param table
	 *            Methods table to store.
	 * @throws InternalException
	 *             Some error has occured while storing the registry
	 */
    @SuppressWarnings("unchecked")
    private void storeRegistry(String fileName, Collection table)
			throws InternalException {
		File file = new File(fileName + "update");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeMethodPkgReg",
					"storeRegistry", ioe);
			throw new jcolibri.cbrcore.exception.InternalException(ioe);
		}
		// If the file exists then we try to recover the tasks.
		try {
			JAXBContext jc = JAXBContext.newInstance("jcolibri.xml.methods");
			ObjectFactory of = new ObjectFactory();
			Methods methodsXML = of.createMethods();
			List<jcolibri.xml.methods.MethodType> list = methodsXML.getMethod();
			for (Iterator it = table.iterator(); it.hasNext();) {
				CBRMethod method = (CBRMethod) it.next();
				jcolibri.xml.methods.MethodType methodXML = buildXMLMethod(
						method.getName(), method, of);
				list.add(methodXML);
			}
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// m.setProperty( Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
			// "xml");
			m.marshal(methodsXML, fos);
		} catch (javax.xml.bind.JAXBException je) {
			CBRLogger.log("jcolibri.core.PrototypeMethodPkgReg",
					"storeRegistry", je);
			throw new jcolibri.cbrcore.exception.InternalException(je);
		}
		try {
			fos.close();
			File oldFile = new File(fileName);
			oldFile.delete();
			file.renameTo(oldFile);
			CBRLogger.log("jcolibri.core.PrototypeMethodPkgReg",
					CBRLogger.INFO, "writting method registry at:"
							+ oldFile.getAbsolutePath());
		} catch (IOException ioe) {
			CBRLogger.log("jcolibri.core.PrototypeMethodPkgReg",
					"storeRegistry", ioe);
			throw new jcolibri.cbrcore.exception.InternalException(ioe);
		}
	}

	/**
	 * Removes a method from the manager by its index
	 * 
	 * @param idx
	 *            index
	 */
	public void removeMethod(int idx) {
		if ((idx >= 0) && (idx < methodsL.size())) {
			methodsHM.remove(getMethod(idx).getName());
			methodsL.remove(idx);
			fireRegistryChanged(this);
		}
	}

	/**
	 * Returns a method by its index
	 * 
	 * @param idx
	 *            index
	 * @return the method or null.
	 */
	public CBRMethod getMethod(int idx) {
		if ((idx >= 0) && (idx < methodsL.size())) {
			return (CBRMethod) methodsL.get(idx);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param methodName
	 * @param description
	 * @param type
	 * @param competencies
	 * @param subtasks
	 * @param params
	 * @param preConditions
	 * @param postConditions
	 */
	private void generateMethodPatternFile(String methodName,
			String description, MethodType type, List competencies,
			List subtasks, List params, HashMap preConditions,
			HashMap postConditions) {

	}

	/**
	 * A new prototype method is added to the manager.
	 * 
	 * @param methodName
	 *            Name of the new method
	 * @param description
	 *            Brief description of new method
	 * @param type
	 *            Type of new method
	 * @param competencies
	 *            Competencies what this method is useful for
	 * @param subtasks
	 *            List of subtasks that generate this method in case of being a
	 *            DECOMPOSITION method
	 * @param params
	 *            IN parameters needed by this method
	 */
	public void addMethod(String methodName, String description,
			MethodType type, List<String> competencies, List<String> subtasks, List<MethodParameter> params,
			HashMap<String,Object> preConditions, HashMap<String,Object> postConditions) {

		CBRMethod newMethod = new CBRMethod() {
			private static final long serialVersionUID = 1L;

			public CBRContext execute(CBRContext context) {
				return null;
			}
		};
		newMethod.setName(methodName);
		newMethod.setInformalDescription(description);
		newMethod.setCompetencies(competencies);
		newMethod.setMethodType(type);
		newMethod.setParametersInfo(params);
		newMethod.setSubTasks(subtasks);
		newMethod.setPreConditionsInfo(preConditions);
		newMethod.setPostConditionsInfo(postConditions);

		addMethod(newMethod);
	}

	/**
	 * Adds a method to the manager.
	 * 
	 * @param method
	 *            new method.
	 */
	public void addMethod(CBRMethod method) {
		generateMethodPatternFile(method.getName(), method
				.getInformalDescription(), method.getMethodType(), method
				.getCompetencies(), method.getSubTasks(), method
				.getParametersInfo(), method.getPreConditionsInfo(), method
				.getPostConditionsInfo());

		methodsHM.put(method.getName(), method);
		methodsL.add(method);
		fireRegistryChanged(this);
	}

	/**
	 * Return a Collection of CBRMethods
	 * 
	 * @return collection of methods
	 */
	public Collection<CBRMethod> getMethods() {
		return methodsL;
	}

	/**
	 * Return the prototype method identified by given name
	 * 
	 * @param name
	 *            String Name of the requiered method
	 * @return requiered method.
	 */
	public CBRMethod getMethod(String name) {
		return (CBRMethod) methodsHM.get(name);
	}

	/**
	 * Retrieves the available methods for a given competence
	 * 
	 * @param compentece
	 *            Competence that wants to be covered
	 * @return A list of valid methods
	 */
	public Collection<CBRMethod> getMethodWithCompetence(String compentece) {
		Vector<CBRMethod> v = new Vector<CBRMethod>();
		Collection<CBRMethod> col = getMethods();
		for (Iterator<CBRMethod> it = col.iterator(); it.hasNext();) {
			CBRMethod method = it.next();
			List<String> comp = method.getCompetencies();
			for (Iterator<String> it2 = comp.iterator(); it2.hasNext();) {
				if ((it2.next()).equals(compentece)) {
					v.add(method);
					continue;
				}
			}
		}
		return v;
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
}
