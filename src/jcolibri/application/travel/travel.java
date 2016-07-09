package jcolibri.application.travel;

import jcolibri.cbrcore.*;
import jcolibri.cbrcore.packagemanager.*;
import java.util.*;
import java.net.*;

/**
 * @author jcolibri
 *
 * Automatic generated class by jCOLIBRI
 */
public class travel {

	CBRCore core;
	ArrayList<CBRTask> preCycleTaskList  = new ArrayList<CBRTask>();
	ArrayList<CBRTask> cycleTaskList     = new ArrayList<CBRTask>();
	ArrayList<CBRTask> postCycleTaskList = new ArrayList<CBRTask>();


	/**
	* Init method of the application. Will invoke several methods
	* to leave ready the application.
	*/
	public void init() {
		core = new CBRCore("travel");
		
        PackageManager.getInstance().getPackageByName("Core").setActive(true);
        PackageManager.getInstance().getPackageByName("User Components").setActive(true);

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
			  createInstance("PreCycle","PreCycle8249");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.PreCycleMethod","jcolibri.method.PreCycleMethod5825");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain cases task","Obtain cases task5913");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.LoadCaseBaseMethod","jcolibri.method.LoadCaseBaseMethod4182");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Connector=examplestravelconnector.xml
			parametersMap.put("Connector",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%1Dexamples%5Ctravel%5Cconnector.xml","UTF-8")));
			method.setParameters(parametersMap);
 
			
			/****************************/
			/* Cycle Tasks & Methods */
			/****************************/			
			taskList = cycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("CBR Cycle","CBR Cycle2347");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CBRMethod","jcolibri.method.CBRMethod5870");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain query task","Obtain query task9517");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.ConfigureQueryMethod","jcolibri.method.ConfigureQueryMethod902");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=examplestravelcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%21examples%5Ctravel%5CcaseStructure.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Retrieve Task","Retrieve Task2943");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.RetrieveComputationalMethod","jcolibri.method.RetrieveComputationalMethod9414");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select working cases task","Select working cases task5396");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectAllCasesMethod","jcolibri.method.SelectAllCasesMethod5674");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Compute similarity task","Compute similarity task6392");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.NumericSimComputationMethod","jcolibri.method.NumericSimComputationMethod57");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select best task","Select best task4895");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectSomeCasesMethod","jcolibri.method.SelectSomeCasesMethod9091");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Number of cases=3
			parametersMap.put("Number of cases",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Integer%12%EF%BF%A2%EF%BE%A0%EF%BE%A4%EF%BF%B7%EF%BE%81%EF%BE%878%02%00%01I%00%05valuexr%00%10java.lang.Number%EF%BE%86%EF%BE%AC%EF%BE%95%1D%0B%EF%BE%94%EF%BF%A0%EF%BE%8B%02%00%00xp%00%00%00%03","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Reuse Task","Reuse Task5938");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.ReuseComputationalMethod","jcolibri.method.ReuseComputationalMethod4894");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Prepare Cases for Adaptation Task","Prepare Cases for Adaptation Task6025");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CopyCasesforAdaptationMethod","jcolibri.method.CopyCasesforAdaptationMethod884");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=examplestravelcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%21examples%5Ctravel%5CcaseStructure.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Atomic Reuse Task","Atomic Reuse Task7507");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.NumericDirectProportionMethod","jcolibri.method.NumericDirectProportionMethod2827");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Description Attribute=Description.Duration
			parametersMap.put("Description Attribute",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%14Description.Duration","UTF-8")));			//parameter:Solution Attribute=Description.Price
			parametersMap.put("Solution Attribute",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%11Description.Price","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Atomic Reuse Task","Atomic Reuse Task9149");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.NumericDirectProportionMethod","jcolibri.method.NumericDirectProportionMethod4534");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Description Attribute=Description.NumberOfPersons
			parametersMap.put("Description Attribute",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%1BDescription.NumberOfPersons","UTF-8")));			//parameter:Solution Attribute=Description.Price
			parametersMap.put("Solution Attribute",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%11Description.Price","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Revise Task","Revise Task5210");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.ManualRevisionMethod","jcolibri.method.ManualRevisionMethod4648");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=examplestravelcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%21examples%5Ctravel%5CcaseStructure.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Retain Task","Retain Task629");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.RetainComputationalMethod","jcolibri.method.RetainComputationalMethod5795");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select cases to store task","Select cases to store task8651");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.RetainChooserMethod","jcolibri.method.RetainChooserMethod5422");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=examplestravelcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%21examples%5Ctravel%5CcaseStructure.xml","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Store cases task","Store cases task6650");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.StoreCasesMethod","jcolibri.method.StoreCasesMethod7007");
			core.resolveTaskWithMethod(task, method);
 

			/****************************/
			/* PostCycle Tasks & Methods */
			/****************************/			
			taskList = postCycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("PostCycle","PostCycle1594");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.PostCycleMethod","jcolibri.method.PostCycleMethod8952");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Close connector task","Close connector task6049");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CloseConnectorMethod","jcolibri.method.CloseConnectorMethod3760");
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
		travel test = new travel();		
		//Create standard GUI
		test.new travelGUI();
		//Init CBR application
		test.init();
	}
	
	/**
	* Default GUI for generated applications. It is completely independent of the CBR application.
	*/	
	private class travelGUI implements jcolibri.util.LogListener
	{
	    javax.swing.JTextArea _log; 
	    public travelGUI()
	    {
			javax.swing.JFrame _frame = new javax.swing.JFrame();
			_frame.setTitle("travel [jCOLIBRI]");
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
