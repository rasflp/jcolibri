package jcolibri.evaluation.evaluators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.evaluation.EvaluationResult;
import jcolibri.evaluation.Evaluator;
import jcolibri.util.CBRLogger;

/**
 * This methods uses all the cases as queries. 
 * It executes the CBR cycle the same number of times like cases in the case base. 
 * In each cycle one case is used as query.  
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */
public class LeaveOneOutEvaluator extends Evaluator {

  public LeaveOneOutEvaluator() {
  }

  public Collection<String> getParamsNames() {
	  return paramsNames;
  }

  public void init() {

  }

  public EvaluationResult evaluate() {
    try {
        
        long t = (new Date()).getTime();
        int numberOfCycles = 0;
        
        // Run the precycle to load the case base
		runPrecycle();
        
		CBRCaseBase baseCasos = core.getContext().getCBRCaseBase();
		Collection<CBRCase> cases = baseCasos.getAll();
		
        //For each case in the case base
        Iterator<CBRCase> itCasos = cases.iterator();
		while (itCasos.hasNext()) {
		  CBRCase _case = itCasos.next();
          
          //Create a copy of the case base without the _case
          Collection<CBRCase> copy = new ArrayList<CBRCase>(cases);
          copy.remove(_case);
          
          //Clear the case base
          baseCasos.forgetCases(cases);
          
          //Set current cases
          baseCasos.learnCases(copy);
          
          //Set current query
          core.setQuery(_case);
          
          //Run the cycle
		  runCycle();
          
          //Evaluate the cycle
          cycleEvaluator.evaluateTraining(core.getContext());
          numberOfCycles++;
		}
        
        baseCasos.forgetCases(cases);
        
        //Run PostCycle
		//runPostcycle();
        
        t = (new Date()).getTime() - t;
        
        //Obtain and complete evaluation result
        EvaluationResult er = cycleEvaluator.getEvaluationResult();
        er.setTotalTime(t);
        er.setNumberOfCycles(numberOfCycles);
        return er;

	} catch (ExecutionException e) {
		CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
	}
    
    return new EvaluationResult();
  }

//----------------------------------------------------------------------------//



}