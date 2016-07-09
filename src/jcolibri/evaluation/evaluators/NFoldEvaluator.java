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
 * This evaluation method divides the case base into several folds (indicated by the user). 
 * For each fold, their cases are used as queries and the remaining folds are used together as case base. 
 * This process is performed several times.
 * <br>
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */

public class NFoldEvaluator extends Evaluator
{

    public static String PARAM_FOLDS = "Folds";
    public static String PARAM_REPETITIONS = "Repetitions";

    public NFoldEvaluator()
    {
        paramsNames.add(NFoldEvaluator.PARAM_FOLDS);
        paramsNames.add(NFoldEvaluator.PARAM_REPETITIONS);
    }


    public Collection<String> getParamsNames()
    {
        return paramsNames;
    }

    public void init()
    {
    }

    public EvaluationResult evaluate()
    {   
        try
        {
            //Get the time
            long t = (new Date()).getTime();
            int numberOfCycles = 0;

            //Get the user parameters
            int folds = Integer.parseInt((String) parameters.get(PARAM_FOLDS));
            int repetitions = Integer.parseInt((String) parameters.get(PARAM_REPETITIONS));

            //Run the precycle to load the cases
            runPrecycle();

            //Obtain the case base
            CBRCaseBase caseBase = core.getContext().getCBRCaseBase();
            Collection<CBRCase> cases = new ArrayList<CBRCase>(caseBase.getAll());
            
            //For each repetition
            for(int r=0; r<repetitions; r++)
            {
                //Create the folds
                createFolds(cases, folds);
                
                //For each fold
                for(int f=0; f<folds; f++)
                {
                    ArrayList<CBRCase> querySet = new ArrayList<CBRCase>();
                    ArrayList<CBRCase> caseBaseSet = new ArrayList<CBRCase>();
                    //Obtain the query and casebase sets
                    getFolds(f, querySet, caseBaseSet);
                    
                    //Clear the caseBase
                    caseBase.forgetCases(cases);
                    
                    //Set the cases that acts as casebase in this cycle
                    caseBase.learnCases(caseBaseSet);
                    
                    //Run cycle for each case in querySet (current fold)
                    for(CBRCase c: querySet)
                    {
                        core.setQuery( c );
                        runCycle();
                        
                        //Evaluate the cycle
                        cycleEvaluator.evaluateTraining(core.getContext());
                        numberOfCycles++;
                    }          
                } 
                
            }

            //Run the post cycle to finalize the application
            //runPostcycle();
            
            //Obtain and complete the evaluation result
            EvaluationResult er = cycleEvaluator.getEvaluationResult();
            er.setTotalTime(t);
            er.setNumberOfCycles(numberOfCycles);
            return er;
            

        } catch (NumberFormatException e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
        } catch (ExecutionException e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
        }
        return new EvaluationResult();
    }

    
    private ArrayList<ArrayList<CBRCase>> _folds;
    private void createFolds(Collection<CBRCase> cases, int folds)
    {
        _folds = new  ArrayList<ArrayList<CBRCase>>();
        int foldsize = cases.size() / folds;
        ArrayList<CBRCase> copy = new ArrayList<CBRCase>(cases);
        
        for(int f=0; f<folds; f++)
        {
            ArrayList<CBRCase> fold = new ArrayList<CBRCase>();
            for(int i=0; (i<foldsize)&&(copy.size()>0); i++)
            {
                int random = (int) (Math.random() * copy.size());
                CBRCase _case = copy.get( random );
                copy.remove(random);
                fold.add(_case);
            }
            _folds.add(fold);
        }
    }
    
    private void getFolds(int f, List<CBRCase> querySet, List<CBRCase> caseBaseSet)
    {
        querySet.clear();
        caseBaseSet.clear();
        
        querySet.addAll(_folds.get(f));
        
        for(int i=0; i<_folds.size(); i++)
            if(i!=f)
                caseBaseSet.addAll(_folds.get(i));
    }
    
}