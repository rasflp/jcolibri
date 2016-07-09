package jcolibri.evaluation.cycleEvaluators;

import java.util.Vector;

import jcolibri.cbrcase.CaseEvalList;
import jcolibri.cbrcase.CaseEvaluation;
import jcolibri.cbrcore.CBRContext;
import jcolibri.evaluation.CycleEvaluator;
import jcolibri.evaluation.EvaluationResult;

/**
 * This class evaluates each cycle with the similarity value of the most similar case. 
 * Its evaluateTraining() method is called by the evaluator in each cycle.
 * Each cycle evaluation is stored internally, when the evaluation finishes they are stored 
 * in the EvaluationResult object returned by getEvaluationResult()
 * 
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */

public class SolutionSimilEvaluator implements CycleEvaluator {

      EvaluationResult er;
      
      Vector<Double> similVector;
      
      public SolutionSimilEvaluator() 
      {
         er = new EvaluationResult(); 
         similVector = new Vector<Double>();
      }	
    	
    
      public void evaluateTraining(CBRContext context) {
          CaseEvalList l = context.getEvaluatedCases();
          if(l==null)
              similVector.add(new Double(0));
          if(l.size()==0)
              similVector.add(new Double(0));
          CaseEvaluation ce = (CaseEvaluation) l.first();
          Object o = ce.getEvaluation();
          if(! (o instanceof Double))
              similVector.add(new Double(0));
          Double d = (Double)o;
          similVector.add(d);
      }
    
    
    public EvaluationResult getEvaluationResult()
    {
        er.setCycleEvaluation("Similarity", similVector);
        double acum = 0;
        for(Double d: similVector)
            acum += d;
        
        er.setTextualInfo("Similarity average: "+ acum/similVector.size());
        return er;
    }

}