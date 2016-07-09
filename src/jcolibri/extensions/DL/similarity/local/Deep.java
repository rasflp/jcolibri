/**
 * 
 */
package jcolibri.extensions.DL.similarity.local;

import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntClass;

import jcolibri.cbrcase.Individual;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.similarity.SimilarityFunction;

/**
 * @author A. Luis Diez Hernández
 *
 */
public class Deep implements SimilarityFunction {

	/* (non-Javadoc)
	 * @see jcolibri.similarity.SimilarityFunction#compute(jcolibri.cbrcase.Individual, jcolibri.cbrcase.Individual)
	 */
	public double compute(Individual o1, Individual o2) {
		return compute(o1.getValue(),o2.getValue());
	}
	
	private double compute(Object o1, Object o2){
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof ConceptType))
			return 0;
		ConceptType cQuery = (ConceptType) o1;
		
		
		if (!(o2 instanceof ConceptType))
			return 0;
		ConceptType cCase = (ConceptType) o2;
		
		OntClass commonParent = cQuery.commonParent(cCase.getConcept());
					
		//Nota para mi mismo
		//Pensar en una forma de normalizar esto para que devuelva
		//un numero entre 0 y 1 en función del grado de parentesco.
		int level = cQuery.getLevel(commonParent);
		
		double divider = cQuery.getLevel();
		
		int lvl2 = cCase.getLevel();
		
		if(lvl2>divider) divider = lvl2;
		
		if(divider==0) divider=0.000001;
		
		double deep = level/divider;
		
							
		return deep;
	}
	
	/** Function parameters. */
	private HashMap params;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {
		params = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#getParametersInfo()
	 */
	public HashMap getParametersInfo() {
		return params;
	}

}
