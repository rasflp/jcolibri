package jcolibri.extensions.DL.similarity.local;

import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntClass;

import jcolibri.cbrcase.Individual;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.similarity.SimilarityFunction;
/**
 * This function computes the similarity between two concept values as the distance
 * between then in the ontology tree.
 * 
 * @author A. Luis Diez Hernández
 */
public class EdgeDistance implements SimilarityFunction {

	
	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(),o2.getValue());
	}

	private double compute(Object o1, Object o2){
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof ConceptType))
			return 0;
		ConceptType cQuery = (ConceptType) o1;
		OntClass queryValue = cQuery.getConcept();
		if(queryValue == null) return 0;
		
		if (!(o2 instanceof ConceptType))
			return 0;
		ConceptType cCase = (ConceptType) o2;
		OntClass caseValue = cCase.getConcept();
		if(caseValue == null) return 0;
				
		int distance = cQuery.edgeDistance(caseValue);
		//if(cQuery.isInstace()) distance++;
		//Normalizar la distancia
		return 1 - (float)distance/(float)cQuery.maxDistance(caseValue);
		//Puede que así esté bien.
		
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
