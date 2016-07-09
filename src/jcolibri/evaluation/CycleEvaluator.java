package jcolibri.evaluation;

import jcolibri.cbrcore.CBRContext;

/**
 * Interface for the CycleEvaluators.<br>
 * CycleEvaluators shuold be call in each evaluation step (i.e. cycle). 
 * They store internally the evaluation of each cycle and returns the hole
 * information when evaluation has finished with the method getEvaluationResult() 
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */


public interface CycleEvaluator {

  /**
   * This method must be called in each cycle of the evaluation.
   */
  public void evaluateTraining(CBRContext context);
  
  /**
   * Returns the EvaluationResult when the evaluation has finished
   * @return
   */
  public EvaluationResult getEvaluationResult();

}