package jcolibri.evaluation;

import jcolibri.evaluation.util.LoadFromXML;

import java.util.HashMap;

import javax.swing.JOptionPane;

import jcolibri.cbrcore.CBRCore;
import jcolibri.util.CBRLogger;


/**
 * This class is a Factory of Evaluators. 
 * It includes methods to configure an evaluator form a XML file or indicating all its components.
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */
public class EvaluatorsFactory {


  /**
   * Creates an evaluator from a XML file.
   * @param XMLfile Path to the XML file
   * @param core jCOLIBRI CBR core
   */
  public static Evaluator getEvaluator(String XMLfile, CBRCore core) {
    LoadFromXML.load(XMLfile);
    
    String evaluatorClassName = LoadFromXML.getEvaluatorClassName();
    HashMap params = LoadFromXML.getParametros();
    String solTypeClassName = LoadFromXML.getCycleEvaluatorClassName();
    
    Evaluator eval;
    CycleEvaluator solType;
    try {
      Class c = Class.forName(evaluatorClassName);
      eval = (Evaluator) c.newInstance();
      Class sol = Class.forName(solTypeClassName);
      solType = (CycleEvaluator) sol.newInstance();
      eval.setCycleEvaluator(solType);
    }
    catch (Exception e) {
    	JOptionPane.showMessageDialog(null,e);
      CBRLogger.log(EvaluatorsFactory.class, CBRLogger.ERROR, e.getMessage());
      return null;
    }

    eval.setCBRCore(core);
    eval.setConfigParam(params);
    eval.init();
    return eval;
  }
  
  
  /**
   * Creates an evaluator and configures it with all its parameters
   * @param evaluatorClassName Class name of the evaluator to load and configure
   * @param cycleEvaluatorClassName Cycle evaluator class name
   * @param params Params of the evaluator
   * @param core jCOLIBRI CBR core
   * @return
   */
  public static Evaluator getEvaluator(String evaluatorClassName, String cycleEvaluatorClassName, HashMap params, CBRCore core)
  {
    Evaluator eval;
    try {
      Class c = Class.forName(evaluatorClassName);
      eval = (Evaluator) c.newInstance();
      
      c = Class.forName(cycleEvaluatorClassName);
      CycleEvaluator ceval = (CycleEvaluator)c.newInstance();
      eval.setCycleEvaluator(ceval);

      eval.setCBRCore(core);
      eval.setConfigParam(params);
      eval.init();

    }
    catch (Exception e) {
      CBRLogger.log(EvaluatorsFactory.class, CBRLogger.ERROR, e.getMessage());
      return null;
    }

    return eval;
  }

}