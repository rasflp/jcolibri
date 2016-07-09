package jcolibri.evaluation.evaluators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;
import jcolibri.evaluation.EvaluationResult;
import jcolibri.evaluation.Evaluator;

/**
 * This method splits the case base in two sets: one used for testing where each case is used as query, and another that acts as normal case base.
 * This process is performed serveral times.
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */

public class HoldOutEvaluator extends Evaluator {

    /** Number of repetitions */
  public static String PARAM_REPETITIONS  = "Repetitions";
    /** Percentage of cases used for testing (as queries) */
  public static String PARAM_TESTPERCENT  = "%Cases used for testing";
  

  public HoldOutEvaluator() {
    paramsNames.add(HoldOutEvaluator.PARAM_REPETITIONS);
    paramsNames.add(HoldOutEvaluator.PARAM_TESTPERCENT);      
  }

  
  public Collection<String> getParamsNames() {
	  return paramsNames;
  }

  public void init() {
  }
  

  public EvaluationResult evaluate() {
    try {
        
        //Obtain the time
      long t = (new Date()).getTime();
      int numberOfCycles=0;
            
        //Obtain the parameters
      int testPercent = Integer.parseInt((String)parameters.get(HoldOutEvaluator.PARAM_TESTPERCENT));
      int repetitions = Integer.parseInt((String)parameters.get(HoldOutEvaluator.PARAM_REPETITIONS));

        // Run the precycle to load the case base
      runPrecycle();

      
      CBRCaseBase caseBase = core.getContext().getCBRCaseBase();
      Collection<CBRCase> cases = new ArrayList<CBRCase>(caseBase.getAll());
      
        // For each repetition
      for(int rep=0; rep<repetitions; rep++)
      {
          ArrayList<CBRCase> querySet = new ArrayList<CBRCase>();
          ArrayList<CBRCase> caseBaseSet = new ArrayList<CBRCase>();
          //Split the case base
          splitCaseBase(cases, querySet, caseBaseSet, testPercent);
          
          //Clear the caseBase
          caseBase.forgetCases(cases);
          
          //Set the cases that acts as case base in this repetition
          caseBase.learnCases(caseBaseSet);
          
          //Run cycle for each case in querySet
          for(CBRCase c: querySet)
          {
              core.setQuery( c );
              runCycle();
              
              //Evaluate the cycle
              cycleEvaluator.evaluateTraining(core.getContext());
              numberOfCycles++;
          }
      }
      
      // Run the poscycle to finish the application
      //runPostcycle();

      t = (new Date()).getTime() - t;
      
      // Obtain and complete the evaluation result
      EvaluationResult er = cycleEvaluator.getEvaluationResult();
      er.setTotalTime(t);
      er.setNumberOfCycles(numberOfCycles);
      return er;

    }
    catch (NumberFormatException e) {
    	CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
    }
    catch (ExecutionException e) {
    	CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
    }
    return new EvaluationResult();
    
  }

  /**
   * Splits the case base in two sets: queries and case base
   * @param holeCaseBase Complete original case base
   * @param querySet Output param where queries are stored
   * @param casebaseSet Output param where case base is stored
   * @param testPercent Percentage of cases used as queries
   */
  private void splitCaseBase(Collection<CBRCase> holeCaseBase, List<CBRCase> querySet, List<CBRCase> casebaseSet, int testPercent)
  {
      querySet.clear();
      casebaseSet.clear();
      
      int querySetSize = (holeCaseBase.size() * testPercent) / 100;
      casebaseSet.addAll(holeCaseBase);
   
      
      for(int i=0; i<querySetSize; i++)
      {
          int random = (int) (Math.random() * casebaseSet.size());
          CBRCase _case = casebaseSet.get( random );
          casebaseSet.remove(random);
          querySet.add(_case);
      }
  }


}