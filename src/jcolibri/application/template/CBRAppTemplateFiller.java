package jcolibri.application.template;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import jcolibri.cbrcore.CBRCore;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.cbrcore.CBRTask;
import jcolibri.cbrcore.MethodParameter;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.util.CBRLogger;
import jcolibri.util.JColibriClassHelper;

/**
 * @author Juan José Bello
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class CBRAppTemplateFiller {

	private static final String templateFile = "/jcolibri/application/template/CBRApplicationTemplate.template";

	private static final String CBRAPPLICATION = "%CBRAPPLICATION%";

	private static final String CBRAPPLICATIONNAME = "%CBRAPPLICATIONNAME%";

	private static final String CBRPACKAGESCONFIG = "%CBRPACKAGESCONFIG%";

	private static final String PRECYCLE_CONFIGURATION = "%PRECYCLE_TASKS_AND_METHODS%";

	private static final String CYCLE_CONFIGURATION = "%CYCLE_TASKS_AND_METHODS%";

	private static final String POSTCYCLE_CONFIGURATION = "%POSTCYCLE_TASKS_AND_METHODS%";

	// TO DO actualmente con lo de create task instance no se mantiene la
	// estructura
	// de jerarquica de tareas. Esto habrá que revisarlo y ver como mejorarlo.
	private static final String CONFIG_TEMPLATE = "\n"
			+ "			task = TasksInstanceRegistry.getInstance().\n"
			+ "			  createInstance(\"%TASK_PROTOTYPE%\",\"%TASK_INSTANCE%\");\n"
			+ "			taskList.add(task);\n"
			+ "			method = MethodsInstanceRegistry.getInstance().\n"
			+ "			  createInstance(\"%METHOD_PROTOTYPE%\",\"%METHOD_INSTANCE%\");\n"
			+ "			core.resolveTaskWithMethod(task, method);\n";

	private static final String PARAMETERS_INI = "\n"
			+ "			//!!!NOTE: Final version should maybe modified these values\n"
			+ "			//These are serialized version of the values\n"
			+ "			//assigned during application configuration.\n"
			+ "			parametersMap = new HashMap<String,Object>();\n";

	private static final String PARAMETERS_END = "\n"
			+ "			method.setParameters(parametersMap);\n";

	private static final String TASK_PROTOTYPE = "%TASK_PROTOTYPE%";

	private static final String TASK_INSTANCE = "%TASK_INSTANCE%";

	private static final String METHOD_PROTOTYPE = "%METHOD_PROTOTYPE%";

	private static final String METHOD_INSTANCE = "%METHOD_INSTANCE%";

	public static String escape(String name) {
		return Pattern.compile(" ").matcher(name).replaceAll("_");
	}

	private static String generateConfiguration(CBRCore core, CBRTask cbrtask) {
		StringBuffer sb = new StringBuffer();
		// CBRTask cbrtask = core.getRootTask();
		LinkedList list = core.getPlannedTasks(cbrtask);
		Pattern tpp = Pattern.compile(TASK_PROTOTYPE);
		Pattern tip = Pattern.compile(TASK_INSTANCE);
		Pattern mpp = Pattern.compile(METHOD_PROTOTYPE);
		Pattern mip = Pattern.compile(METHOD_INSTANCE);
		String temp = "";
		CBRMethod method;
		CBRTask task;
		for (Iterator it = list.iterator(); it.hasNext();) {
			task = (CBRTask) it.next();
			method = task.getMethodInstance();
			if (method != null) {
				temp = tpp.matcher(CONFIG_TEMPLATE).replaceAll(task.getName());
				temp = tip.matcher(temp).replaceAll(task.getInstanceName());
				temp = mpp.matcher(temp).replaceAll(method.getName());
				temp = mip.matcher(temp).replaceAll(method.getInstanceName());
				sb.append(temp);
				List parameters = method.getParametersInfo();
				if (method.getParametersInfo() != null
						&& method.getParametersInfo().size() > 0) {
					sb.append(PARAMETERS_INI);
					for (Iterator it2 = parameters.iterator(); it2.hasNext();) {
						try {
							MethodParameter param = (MethodParameter) it2
									.next();
							String paramName = param.getName();
							Object value = method.getParameterValue(paramName);
							sb.append("			//parameter:" + paramName + "="
									+ value.toString() + "\n");
							sb.append("			parametersMap.put(\"");
							sb.append(paramName);
							sb.append("\",");
							sb
									.append("jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode(");
							sb.append("\"");
							sb.append(URLEncoder.encode(JColibriClassHelper
									.serializeObject((Serializable) value),
									"UTF-8"));
							sb.append("\",\"UTF-8\")));");
						} catch (IOException ioe) {
							CBRLogger
									.log(
											"jcolibri.application.template.CBRAppTemplateFiller",
											"generateConfiguration", ioe);
						}
					}
					sb.append(PARAMETERS_END);

				}

			}
		}
		return sb.toString();
	}

	public static String generatePackagesConfig(CBRCore core) {
		String res = "";
		for (Iterator iter = PackageManager.getInstance().getActivePackages()
				.iterator(); iter.hasNext();) {
			PackageInfo pi = (PackageInfo) iter.next();

			res += "        PackageManager.getInstance().getPackageByName(\""
					+ pi.getName() + "\").setActive(true);\n";
		}
		return res;
	}

	public static void generateApplication(CBRCore core, File file)
			throws IOException {
		InputStream is = CBRAppTemplateFiller.class
				.getResourceAsStream(templateFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = in.readLine();
		while (line != null) {
			sb.append(line);
			sb.append("\n");
			line = in.readLine();
		}
		is.close();
		String content = sb.toString();
		String className = escape(core.getName());
		content = Pattern.compile(CBRAPPLICATION).matcher(content).replaceAll(
				className);
		content = Pattern.compile(CBRAPPLICATIONNAME).matcher(content)
				.replaceAll(core.getName());
		content = Pattern
				.compile(PRECYCLE_CONFIGURATION)
				.matcher(content)
				.replaceAll(generateConfiguration(core, core.getPreCycleTask()));
		content = Pattern.compile(CYCLE_CONFIGURATION).matcher(content)
				.replaceAll(generateConfiguration(core, core.getCycleTask()));
		content = Pattern.compile(POSTCYCLE_CONFIGURATION).matcher(content)
				.replaceAll(
						generateConfiguration(core, core.getPostCycleTask()));
		content = Pattern.compile(CBRPACKAGESCONFIG).matcher(content)
				.replaceAll(generatePackagesConfig(core));
		// System.out.println(content);
		FileOutputStream osf = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(osf);
		dos.writeBytes(content);
		dos.flush();
		dos.close();
		osf.close();
		CBRLogger.log("jcolibri.application.template.CBRAppTemplateFiller",
				CBRLogger.INFO, "Application generated: "
						+ file.getAbsolutePath());

	}

	public static void main(String[] args) throws IOException {
		CBRAppTemplateFiller.generateApplication(new CBRCore("Test"), new File(
				"."));
	}

}
