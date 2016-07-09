package jcolibri.similarity;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;

/**
 * 
 * @author Usuario
 */
public interface SimilarityFunction {

	public double compute(Individual o1, Individual o2);

	public void setParameters(HashMap parameters);

	public HashMap getParametersInfo();

}
