package jcolibri.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jcolibri.cbrcore.CBRCore;
import jcolibri.cbrcore.CBRTask;
import jcolibri.cbrcore.exception.DataInconsistencyException;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * This abstract class defines the methods of an evaluator.<br>
 * It includes the methods to run the Pre/Post/Cycle of an CBR application.
 * Subclasses shuld redefine init() that uses the parameters to initialize the evaluator and evaluate() that runs the evaluation
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */
public abstract class Evaluator {


  protected CBRCore core;
  protected CycleEvaluator cycleEvaluator;
  protected HashMap parameters;
  

  protected Collection<String> paramsNames = new ArrayList<String>();



  public Evaluator() 
  {
  }


  public void setCBRCore(CBRCore core) 
  {
    this.core = core;
  }

  public void setCycleEvaluator(CycleEvaluator solEv) 
  {
    cycleEvaluator = solEv;
  }

  public void setConfigParam(HashMap params) 
  {
    parameters = params;
  }
  
  public HashMap getConfigParam() 
  {
    return parameters;
  }


  public Collection<String> getParamsNames()
  {
      return paramsNames;
  }
  
  
  
  
  /**
   * Initializes the evaluator with the paramethers.
   * @throws DataInconsistencyException
   */
  public abstract void init() throws DataInconsistencyException;

  /**
   * Performs the evaluation
   * @return An EvaluationResult object
   */
  public abstract EvaluationResult evaluate();

  

  
  
 /**
  * Runs the preCycle of the CBR application
  */
 public void runPrecycle() throws ExecutionException{
   CBRTask task = core.getPreCycleTask();
   Collection plannedTasks = core.getPlannedTasks(task);
   for (Iterator iter = plannedTasks.iterator(); iter.hasNext();) {
	   CBRTask t = (CBRTask)iter.next();
	   core.executeTask(t);
   }
 }

 /**
  * Runs the Cycle of the CBR application
  */
  public void runCycle() throws ExecutionException {
   CBRTask task = core.getCycleTask();
   Collection plannedTasks = core.getPlannedTasks(task);
   for (Iterator iter = plannedTasks.iterator(); iter.hasNext();) {
       CBRTask t = (CBRTask)iter.next();
       if(!t.getName().equals("Obtain query task"))
           core.executeTask(t);
   }
  }

  /**
   * Runs the postCycle of the CBR application
   */
  public void runPostcycle() throws ExecutionException {
	CBRTask task = core.getPostCycleTask();
       Collection plannedTasks = core.getPlannedTasks(task);
       for (Iterator iter = plannedTasks.iterator(); iter.hasNext();) {
           CBRTask t = (CBRTask)iter.next();
           core.executeTask(t);
       }
  }
  
  



}
