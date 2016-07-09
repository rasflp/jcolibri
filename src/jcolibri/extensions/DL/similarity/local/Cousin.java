package jcolibri.extensions.DL.similarity.local;

import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntClass;
import jcolibri.cbrcase.Individual;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function computes the similarity between two concept values as if they have
 * a common subsummer two levels above.
 * 
 * @author A. Luis Diez Hernández
 */
public class Cousin implements SimilarityFunction {

	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(),o2.getValue());
	}

	private double compute(Object o1, Object o2){
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof ConceptType))
			return 0;
		ConceptType cQuery = (ConceptType) o1;
		double dif;
		com.hp.hpl.jena.ontology.Individual queryValue = cQuery.getOntIndividual();
		if(queryValue == null) return 0;
		
		if (!(o2 instanceof ConceptType))
			return 0;
		ConceptType cCase = (ConceptType) o2;
		com.hp.hpl.jena.ontology.Individual caseValue = cCase.getOntIndividual();
		if(caseValue == null) return 0;
		
		OntClass commonParent = cQuery.commonParent(cCase.getConcept());
					
		//Nota para mi mismo
		//Pensar en una forma de normalizar esto para que devuelva
		//un numero entre 0 y 1 en función del grado de parentesco.
		int level = cQuery.getLevel(commonParent);
		
		dif = (level - cQuery.getLevel())/cQuery.maxDistance(cCase.getConcept());
		
			double retval = 1 - dif;
				
		return retval;
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
