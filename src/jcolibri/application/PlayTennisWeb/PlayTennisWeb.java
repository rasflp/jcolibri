package jcolibri.application.PlayTennisWeb;

import jcolibri.cbrcore.*;
import jcolibri.cbrcore.packagemanager.*;
import java.util.*;
import java.net.*;

/**
 * @author jcolibri
 *
 * Automatic generated class by jCOLIBRI
 */
public class PlayTennisWeb {

	CBRCore core;
	ArrayList<CBRTask> preCycleTaskList  = new ArrayList<CBRTask>();
	ArrayList<CBRTask> cycleTaskList     = new ArrayList<CBRTask>();
	ArrayList<CBRTask> postCycleTaskList = new ArrayList<CBRTask>();


	/**
	* Init method of the application. Will invoke several methods
	* to leave ready the application.
	*/
	public void init() {
		core = new CBRCore("PlayTennisWeb");
		
        PackageManager.getInstance().getPackageByName("Core").setActive(true);
        PackageManager.getInstance().getPackageByName("Web Interface Support").setActive(true);

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
			  createInstance("PreCycle","PreCycle2315");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.WebPreCycleMethod","jcolibri.extensions.web.method.WebPreCycleMethod5569");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Start Web Server task","Start Web Server task7733");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.StartTomcatMethod","jcolibri.extensions.web.method.StartTomcatMethod1712");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain cases task","Obtain cases task565");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.LoadCaseBaseMethod","jcolibri.method.LoadCaseBaseMethod4109");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Connector=examplesPlayTennisWebconnector.xml
			parametersMap.put("Connector",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%24examples%5CPlayTennisWeb%5Cconnector.xml","UTF-8")));
			method.setParameters(parametersMap);
 
			
			/****************************/
			/* Cycle Tasks & Methods */
			/****************************/			
			taskList = cycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("CBR Cycle","CBR Cycle647");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.CBRWebMethod","jcolibri.extensions.web.method.CBRWebMethod9227");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Start Web Cycle task","Start Web Cycle task3262");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.StartWebCycleMethod","jcolibri.extensions.web.method.StartWebCycleMethod4631");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Obtain query task","Obtain query task2030");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.ObtainWebQueryMethod","jcolibri.extensions.web.method.ObtainWebQueryMethod2253");
			core.resolveTaskWithMethod(task, method);

			//!!!NOTE: Final version should maybe modified these values
			//These are serialized version of the values
			//assigned during application configuration.
			parametersMap = new HashMap<String,Object>();
			//parameter:Case Structure=examplesPlayTennisWebcaseStructure.xml
			parametersMap.put("Case Structure",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05t%00%28examples%5CPlayTennisWeb%5CcaseStructure.xml","UTF-8")));			//parameter:Time out=60
			parametersMap.put("Time out",jcolibri.util.JColibriClassHelper.deserializeObject(URLDecoder.decode("%EF%BE%AC%EF%BF%AD%00%05sr%00%11java.lang.Integer%12%EF%BF%A2%EF%BE%A0%EF%BE%A4%EF%BF%B7%EF%BE%81%EF%BE%878%02%00%01I%00%05valuexr%00%10java.lang.Number%EF%BE%86%EF%BE%AC%EF%BE%95%1D%0B%EF%BE%94%EF%BF%A0%EF%BE%8B%02%00%00xp%00%00%00%3C","UTF-8")));
			method.setParameters(parametersMap);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Retrieve Task","Retrieve Task5458");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.RetrieveComputationalForWebServerMethod","jcolibri.extensions.web.method.RetrieveComputationalForWebServerMethod314");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select working cases task","Select working cases task8786");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectAllCasesMethod","jcolibri.method.SelectAllCasesMethod3986");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Compute similarity task","Compute similarity task4570");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.NumericSimComputationMethod","jcolibri.method.NumericSimComputationMethod2718");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Select best task","Select best task9364");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.SelectBestCaseMethod","jcolibri.method.SelectBestCaseMethod5590");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Send Cases to Web Server task","Send Cases to Web Server task320");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.SendCasesToWebServerMethod","jcolibri.extensions.web.method.SendCasesToWebServerMethod1701");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Stop Web Cycle task","Stop Web Cycle task6167");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.StopWebCycleMethod","jcolibri.extensions.web.method.StopWebCycleMethod3554");
			core.resolveTaskWithMethod(task, method);
 

			/****************************/
			/* PostCycle Tasks & Methods */
			/****************************/			
			taskList = postCycleTaskList;
			
			task = TasksInstanceRegistry.getInstance().
			  createInstance("PostCycle","PostCycle1622");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.WebPostCycleMethod","jcolibri.extensions.web.method.WebPostCycleMethod3675");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Stop Web Server task","Stop Web Server task2040");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.extensions.web.method.StopTomcatMethod","jcolibri.extensions.web.method.StopTomcatMethod9339");
			core.resolveTaskWithMethod(task, method);

			task = TasksInstanceRegistry.getInstance().
			  createInstance("Close connector task","Close connector task3785");
			taskList.add(task);
			method = MethodsInstanceRegistry.getInstance().
			  createInstance("jcolibri.method.CloseConnectorMethod","jcolibri.method.CloseConnectorMethod959");
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
		PlayTennisWeb test = new PlayTennisWeb();		
		//Create standard GUI
		test.new PlayTennisWebGUI();
		//Init CBR application
		test.init();
	}
	
	/**
	* Default GUI for generated applications. It is completely independent of the CBR application.
	*/	
	private class PlayTennisWebGUI implements jcolibri.util.LogListener
	{
	    javax.swing.JTextArea _log; 
	    public PlayTennisWebGUI()
	    {
			javax.swing.JFrame _frame = new javax.swing.JFrame();
			_frame.setTitle("PlayTennisWeb [jCOLIBRI]");
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
