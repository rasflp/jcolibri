package jcolibri.evaluation.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jcolibri.util.CBRLogger;
import jcolibri.util.ClassDiscover;
import jcolibri.evaluation.CycleEvaluator;
import jcolibri.evaluation.Evaluator;

/**
 * Clase encargada de llevar toda la información del sistema de evaluación.
 * Posee todos los métodos necesarios para acceder a la información.
 */
public class EvaluatorsInfo
{

    // Única instancia de la clase
    private static EvaluatorsInfo _instance = null;

    private ArrayList<Class> evaluatorsList = new ArrayList<Class>();
    
    private ArrayList<Class> cycleEvaluatorsList = new ArrayList<Class>();

    // ----------------------------CONSTRUCTORA------------------------------------//
    private EvaluatorsInfo()
    {
        this.addEvaluatorsFromDir("jcolibri.evaluation.evaluators");
        this.addCycleEvaluatorsFromDir("jcolibri.evaluation.cycleEvaluators");
    }

    public static EvaluatorsInfo getInstance()
    {
        if (_instance == null)
            _instance = new EvaluatorsInfo();
        return _instance;
    }

    public Collection<String> getCycleEvaluators()
    {
        ArrayList<String> nameList = new ArrayList<String>();
        for (Class c : cycleEvaluatorsList)
            nameList.add(c.getName());
        return nameList;
    }

    public Collection<String> getEvaluators()
    {
        ArrayList<String> nameList = new ArrayList<String>();
        for (Class c : evaluatorsList)
            nameList.add(c.getName());
        return nameList;
    }

    public Collection<String> getEvaluatorParams(String evalName)
    {
        Class eval = null;
        Evaluator evalI = null;
        for (Class c : evaluatorsList)
            if (c.getName().equals(evalName))
                eval = c;
        try
        {
            evalI = (Evaluator) eval.newInstance();
            return evalI.getParamsNames();
        } catch (Exception e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------------------
    // Adds to evaluatrosNameList all the classes found in dir that extend
    // evaluator.Evaluator
    public void addEvaluatorsFromDir(String dir)
    {
        try
        {
            List<String> classNames = ClassDiscover.find(dir, Evaluator.class);
            for (String className:classNames)
            {
                Class c = Class.forName(dir+"."+className);
                this.addEvaluatorClass(c);
            }
        } catch (ClassNotFoundException e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------
    // Adds to typesSolutionEvaluatorsList all the classes found in dir that
    // extend evaluator.CycleEvaluator
    public void addCycleEvaluatorsFromDir(String dir)
    {
        try
        {
            List<String> classNames = ClassDiscover.find(dir, CycleEvaluator.class);
            for (String className:classNames)
            {
                Class c = Class.forName(dir+"."+className);
                this.addcycleEvaluator(c);
            }
        } catch (ClassNotFoundException e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------
    public void addEvaluatorClass(Class c)
    {
        if (!evaluatorsList.contains(c))
        {
            this.evaluatorsList.add(c);
            this.notifyChange();
        }
    }

    // ------------------------------------------------------------------------------
    public void addcycleEvaluator(Class c)
    {
        if (!cycleEvaluatorsList.contains(c))
        {
            this.cycleEvaluatorsList.add(c);
            this.notifyChange();
        }
    }

    // ------------------------------------------------------------------------------
   
    private ArrayList<EvaluatorsInfoListener> listeners = new ArrayList<EvaluatorsInfoListener>();
    
    public void addListener(EvaluatorsInfoListener eil)
    {
        listeners.add(eil);
    }
    
    public void clearListeners()
    {
        listeners.clear();
    }
    
    protected void notifyChange()
    {
        for(EvaluatorsInfoListener eil:listeners)
            eil.change();
    }
    
}