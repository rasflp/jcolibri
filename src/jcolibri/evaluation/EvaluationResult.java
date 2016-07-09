package jcolibri.evaluation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

/**
 * This class stores the result of an evaluation. It is configured and filled by a cycleEvaluator.
 * This info is used to represent graphically the result of an evaluation. 
 * Developers can store several evaluations identified by a label.
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * 
 */
public class EvaluationResult
{
    private long totalTime;
    private int numberOfCycles;
    
    /** Stores the evaluation info */
    private Hashtable<String, Vector<Double>> data;
    
    /** Stores textual info to be presented to user */
    private String textualInfo;
    
    /** Default constructor */
    public EvaluationResult()
    {
        data = new Hashtable<String, Vector<Double>>();
    }

    /**
     * Gets the hashTable with the info about the evaluation
     */
    public Hashtable<String, Vector<Double>> getCyclesEvaluation()
    {
        return data;
    }

    /**
     * Sets the evaluation data
     */
    public void setData(Hashtable<String, Vector<Double>> data)
    {
        this.data = data;
    }
    
    /**
     * Gets the evaluation info identified by the label
     * @param label Identifies the evaluation serie.
     */
    public Vector<Double> getCycleEvaluation(String label)
    {
        return data.get(label);
    }
    
    /**
     * Stes the evaluation info
     * @param label Identifier of the info
     * @param evaluation Evaluation result
     */
    public void setCycleEvaluation(String label, Vector<Double> evaluation)
    {
   
        data.put(label, evaluation);
    }
    
    /** 
     * Returns the name of the contained evaluation series
     * @return
     */
    public String[] getLabels()
    {
        Set<String> set = data.keySet();
        String[] res = new String[set.size()];
        int i=0;
        for( String e : set)
            res[i++] = e;
        return res;
    }

    public int getNumberOfCycles()
    {
        return numberOfCycles;
    }

    public void setNumberOfCycles(int numberOfCycles)
    {
        this.numberOfCycles = numberOfCycles;
    }

    public long getTotalTime()
    {
        return totalTime;
    }

    public void setTotalTime(long totalTime)
    {
        this.totalTime = totalTime;
    }
    
    public double getTimePerCycle()
    {
        return (double)this.totalTime / (double)numberOfCycles;
    }
    
    /**
     * Checks if the evaluation series are correct. This is: all them must have the same length
     * @return
     */
    public boolean checkData()
    {
        boolean ok = true;
        int l = -1;
        for(Enumeration<Vector<Double>> iter = data.elements(); iter.hasMoreElements() && ok ;)
        {
            Vector<Double> v = iter.nextElement();
            if(l == -1)
                l = v.size();
            else
                ok = (l == v.size());      
        }
        return ok;      
    }
    
    public String getTextualInfo()
    {
        return this.textualInfo;
    }
    
    public void setTextualInfo(String texinfo)
    {
        this.textualInfo = texinfo;
    }
    
}
