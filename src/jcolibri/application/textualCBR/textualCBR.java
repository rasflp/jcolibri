package jcolibri.application.textualCBR;

import jcolibri.cbrcore.*;
import jcolibri.cbrcore.packagemanager.*;
import java.util.*;
import java.net.*;

/**
 * @author jcolibri
 *
 * Automatic generated class by jCOLIBRI
 */
public class textualCBR {

	CBRCore core;
	ArrayList<CBRTask> preCycleTaskList  = new ArrayList<CBRTask>();
	ArrayList<CBRTask> cycleTaskList     = new ArrayList<CBRTask>();
	ArrayList<CBRTask> postCycleTaskList = new ArrayList<CBRTask>();


	/**
	* Init method of the application. Will invoke several methods
	* to leave ready the application.
	*/
	public void init() {
		core = new CBRCore("textualCBR");
		
        PackageManager.getInstance().getPackageByName("Core").setActive(true);
        PackageManager.getInstance().getPackageByName("Textual Extension").setActive(true);

        core.init();

		setCBRApplicationConfiguration();
	}
	
	/**
	 * Resets the CBR application
	 */
	public void reset()
	{
	    try
        {
            core.resetContext();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	* Describes the context (current state) of the CBR Application
	* @returns String describing current context.
	*/
	public String getContext()
	{
	    return core.getContext().toString();
	}
	

	/**
	* This method will be invoked to run some tasks of the CBR Application
	* @return CBRContext, result of the CBR application execution
	*/
	protected CBRContext executeTasks(List<CBRTask> taskList) {
		try {
			core.executeTasks(
				(CBRTask[]) taskList.toArray(new CBRTask[taskList.size()]));
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return core.getContext();
	}
	
	/**
	* This method will be invoked to run the pre-cycle of the CBR Application
	* @return CBRContext, result of the pre-cycle execution
	*/
	public CBRContext executePreCycle()
	{
		return this.executeTasks(preCycleTaskList);
	}

	/**
	* This method will be invoked to run the main cycle of the CBR Application
	* @return CBRContext, result of the main cycle execution
	*/

	public CBRContext executeCycle()
	{
		return this.executeTasks(cycleTaskList);
	}

	/**
	* This method will be invoked to run the post-cycle of the CBR Application
	* @return CBRContext, result of the post-cycle execution
	*/
	public CBRContext executePostCycle()
	{
		return this.executeTasks(postCycleTaskList);
	}


	/**
	* This method holds the automatic code generated for your application
	*/
	public final void setCBRApplicationConfiguration() {
		CBRTask task;
		CBRMethod method;
		HashMap<String,Object> parametersMap;
		ArrayList<CBRTask> taskList;
		try {
			/****************************/
			/* PreCycle Tasks & Methods */
			/****************************/			
			taskList = preCycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("PreCycle","PreCycle9143");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.TextualPreCycleMethod","jcolibri.extensions.textual.method.TextualPreCycleMethod6318");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain cases task","Obtain cases task5868");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.LoadCaseBaseMethod","jcolibri.method.LoadCaseBaseMethod9292");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Connector=examplesTextualCBRconnector.xml
			parametersMap.put("Connector",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%21examples%5CTextualCBR%5Cconnector.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select working cases task","Select working cases task7666");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectAllCasesMethod","jcolibri.method.SelectAllCasesMethod6002");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Cases Textual Process task","Cases Textual Process task7946");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.TextualCBRPreProcessMethod","jcolibri.extensions.textual.method.TextualCBRPreProcessMethod5885");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Keyword Layer","Keyword Layer3661");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.KeywordLayerMethod","jcolibri.extensions.textual.method.KeywordLayerMethod3202");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Words Filter","Words Filter9185");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.WordsFilterMethod","jcolibri.extensions.textual.method.WordsFilterMethod9591");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Stemmer","Stemmer5226");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.StemmerMethod","jcolibri.extensions.textual.method.StemmerMethod646");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Part-of-Speech","Part-of-Speech6472");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.PartofSpeechMethod","jcolibri.extensions.textual.method.PartofSpeechMethod1140");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Extract Names","Extract Names4864");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractNamesMethod","jcolibri.extensions.textual.method.ExtractNamesMethod831");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Phrase Layer","Phrase Layer6905");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractPhrasesMethod","jcolibri.extensions.textual.method.ExtractPhrasesMethod1341");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Phrases file=examplesTextualCBRphrasesRules.txt
			parametersMap.put("Phrases file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%24examples%5CTextualCBR%5CphrasesRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Feature Value Layer","Feature Value Layer1382");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractFeaturesMethod","jcolibri.extensions.textual.method.ExtractFeaturesMethod2618");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Features file=examplesTextualCBRfeaturesRules.txt
			parametersMap.put("Features file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%25examples%5CTextualCBR%5CfeaturesRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Domain Structure Layer","Domain Structure Layer5564");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.DomainTopicClassifierMethod","jcolibri.extensions.textual.method.DomainTopicClassifierMethod55");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Topics file=examplesTextualCBRDomainRules.txt
			parametersMap.put("Topics file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%23examples%5CTextualCBR%5CDomainRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Information Extraction Layer","Information Extraction Layer360");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.BasicIEMethod","jcolibri.extensions.textual.method.BasicIEMethod3572");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=false
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Process Cases=true
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));
			method.setParameters(parametersMap);
 
			
			/****************************/
			/* Cycle Tasks & Methods */
			/****************************/			
			taskList = cycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("CBR Cycle","CBR Cycle2007");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CBRMethod","jcolibri.method.CBRMethod8910");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain query task","Obtain query task5222");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.ConfigureQueryMethod","jcolibri.method.ConfigureQueryMethod2389");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=C:doctoradoJColibrijCOLIBRI_1.1jCOLIBRIexamplesTextualCBRcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00QC%3A%5Cdoctorado%5CJColibri%5CjCOLIBRI_1.1%5CjCOLIBRI%5Cexamples%5CTextualCBR%5CcaseStructure.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Retrieve Task","Retrieve Task8972");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.TextualCBRRetrieveMethod","jcolibri.extensions.textual.method.TextualCBRRetrieveMethod6990");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Query Textual Process task","Query Textual Process task5703");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.TextualCBRPreProcessMethod","jcolibri.extensions.textual.method.TextualCBRPreProcessMethod8016");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Keyword Layer","Keyword Layer2137");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.KeywordLayerMethod","jcolibri.extensions.textual.method.KeywordLayerMethod6529");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Words Filter","Words Filter7977");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.WordsFilterMethod","jcolibri.extensions.textual.method.WordsFilterMethod4834");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Stemmer","Stemmer8633");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.StemmerMethod","jcolibri.extensions.textual.method.StemmerMethod1296");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Part-of-Speech","Part-of-Speech3298");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.PartofSpeechMethod","jcolibri.extensions.textual.method.PartofSpeechMethod9004");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Extract Names","Extract Names9841");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractNamesMethod","jcolibri.extensions.textual.method.ExtractNamesMethod6221");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Phrase Layer","Phrase Layer7176");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractPhrasesMethod","jcolibri.extensions.textual.method.ExtractPhrasesMethod7823");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Phrases file=examplesTextualCBRphrasesRules.txt
			parametersMap.put("Phrases file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%24examples%5CTextualCBR%5CphrasesRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Feature Value Layer","Feature Value Layer7509");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.ExtractFeaturesMethod","jcolibri.extensions.textual.method.ExtractFeaturesMethod6175");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Features file=examplesTextualCBRfeaturesRules.txt
			parametersMap.put("Features file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%25examples%5CTextualCBR%5CfeaturesRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Domain Structure Layer","Domain Structure Layer9734");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.DomainTopicClassifierMethod","jcolibri.extensions.textual.method.DomainTopicClassifierMethod8739");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));			//parameter:Topics file=examplesTextualCBRDomainRules.txt
			parametersMap.put("Topics file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%23examples%5CTextualCBR%5CDomainRules.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Information Extraction Layer","Information Extraction Layer1576");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.BasicIEMethod","jcolibri.extensions.textual.method.BasicIEMethod1064");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Process Query=true
			parametersMap.put("Process Query",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%01","UTF-8")));			//parameter:Process Cases=false
			parametersMap.put("Process Cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Boolean%EF%BF%8D+r%EF%BE%80%EF%BF%95%EF%BE%9C%EF%BF%BA%EF%BF%AE%02%00%01Z%00%05valuexp%00","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select working cases task","Select working cases task4417");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectAllCasesMethod","jcolibri.method.SelectAllCasesMethod3971");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Text Relation Process task","Text Relation Process task4526");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.TextRelationsMethod","jcolibri.extensions.textual.method.TextRelationsMethod898");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Thesaurus Layer","Thesaurus Layer8354");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.WordNetRelationsMethod","jcolibri.extensions.textual.method.WordNetRelationsMethod1439");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Glossary Layer","Glossary Layer8461");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.textual.method.GlossaryRelationsMethod","jcolibri.extensions.textual.method.GlossaryRelationsMethod8590");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Glossary file=examplesTextualCBRglossary.txt
			parametersMap.put("Glossary file",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00+examples%5CTextualCBR%5Cglossary.txt","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Compute similarity task","Compute similarity task2205");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.NumericSimComputationMethod","jcolibri.method.NumericSimComputationMethod8310");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select best task","Select best task7594");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectBestCaseMethod","jcolibri.method.SelectBestCaseMethod2288");
			core.resolveTaskWithMethod(task, method);
 

			/****************************/
			/* PostCycle Tasks & Methods */
			/****************************/			
			taskList = postCycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("PostCycle","PostCycle1303");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.PostCycleMethod","jcolibri.method.PostCycleMethod9946");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Close connector task","Close connector task573");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CloseConnectorMethod","jcolibri.method.CloseConnectorMethod8973");
			core.resolveTaskWithMethod(task, method);
 
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Main method. Assings a default GUI to the generated CBR application for allowing testing
	*/
	public static void main(String[] args) {
		//Create CBR application
		textualCBR test = new textualCBR();		
		//Create standard GUI
		test.new textualCBRGUI();
		//Init CBR application
		test.init();
	}
	
	/**
	* Default GUI for generated applications. It is completely independent of the CBR application.
	*/	
	private class textualCBRGUI implements jcolibri.util.LogListener
	{
	    javax.swing.JTextArea _log; 
	    public textualCBRGUI()
	    {
			javax.swing.JFrame _frame = new javax.swing.JFrame();
			_frame.setTitle("textualCBR [jCOLIBRI]");
			java.awt.Dimension ss = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			_frame.setBounds((ss.width-800)/2, (ss.height-580)/2, 800, 580);
			_frame.getContentPane().setLayout(null);
			
			javax.swing.JButton _preButton = new javax.swing.JButton();
			_preButton.setIcon(	new javax.swing.ImageIcon(
						_preButton.getClass().getResource("/jcolibri/resources/precycle.gif")));
			_preButton.setText("PreCycle");
			_preButton.setBounds(20,20,220,110);
			_preButton.setIconTextGap(20);
			_frame.getContentPane().add(_preButton);
			_preButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					executePreCycle();
				}});

			
			javax.swing.JButton _cycleButton = new javax.swing.JButton();
			_cycleButton.setIcon(	new javax.swing.ImageIcon(
			        _cycleButton.getClass().getResource("/jcolibri/resources/cycle.gif")));
			_cycleButton.setText("CBR Cycle");
			_cycleButton.setBounds(20,150,220,110);
			_cycleButton.setIconTextGap(20);
			_frame.getContentPane().add(_cycleButton);
			_cycleButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					executeCycle();
				}});

			javax.swing.JButton _postButton = new javax.swing.JButton();
			_postButton.setIcon(	new javax.swing.ImageIcon(
			        _postButton.getClass().getResource("/jcolibri/resources/postcycle.gif")));
			_postButton.setText("PostCycle");
			_postButton.setBounds(20,280,220,110);
			_postButton.setIconTextGap(20);
			_frame.getContentPane().add(_postButton);
			_postButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					executePostCycle();
				}});

			
			javax.swing.JButton _showContext = new javax.swing.JButton("Show CBR Context");
			_showContext.setBounds(20,420,220,30);
			_frame.getContentPane().add(_showContext);
			_showContext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					_log.append(getContext());
				}});


			javax.swing.JButton _resetContext = new javax.swing.JButton("Reset CBR Context");
			_resetContext.setBounds(20,460,220,30);
			_frame.getContentPane().add(_resetContext);
			_resetContext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					reset();
				}});
			
			javax.swing.JButton _exit = new javax.swing.JButton("Exit");
			_exit.setBounds(20,500,220,30);
			_frame.getContentPane().add(_exit);
			_exit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					System.exit(0);
				}});


			_log = new javax.swing.JTextArea();
			_log.setEditable(false);
			javax.swing.JScrollPane _scrollPane = new javax.swing.JScrollPane();
			_scrollPane.setViewportView(_log);
			_scrollPane.setBounds(260, 20, 520, 510);
			_frame.getContentPane().add(_scrollPane);
			
			jcolibri.util.CBRLogger.addListener(this);

			_frame.addWindowListener(new java.awt.event.WindowAdapter(){
		    	public void windowClosing(java.awt.event.WindowEvent e) 
		    	{ System.exit(0); }});
		   

			_frame.setVisible(true);
	        
	    }
	    
	    public void receivedData(String data)
	    {
	        _log.append(data);
	        _log.setCaretPosition(_log.getText().length());       
	    }
	}
}
