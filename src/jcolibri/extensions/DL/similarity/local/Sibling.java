package jcolibri.extensions.DL.similarity.local;

import java.util.HashMap;

import jcolibri.cbrcase.Individual;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.similarity.SimilarityFunction;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * This function computes the similarity between two concept values as if their
 * common subsummer is their parent.
 * 
 * @author A. Luis Diez Hernández
 */
public class Sibling implements SimilarityFunction {
	public double compute(Individual o1, Individual o2){
		return this.compute(o1.getValue(), o2.getValue());
	}

	private double compute(Object o1, Object o2){
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof ConceptType))
			return 0;
		ConceptType cQuery, cCase;
		OntClass queryValue, caseValue, queryParent, caseParent;
		cQuery = (ConceptType) o1;
		
		queryValue = cQuery.getConcept();
		if(queryValue == null) return 0;
		if (!(o2 instanceof ConceptType))
			return 0;
		cCase = (ConceptType) o2;
		caseValue = cCase.getConcept();
		if(caseValue == null) return 0;

		com.hp.hpl.jena.ontology.Individual indQ = cQuery.getOntIndividual();
		com.hp.hpl.jena.ontology.Individual indC = cCase.getOntIndividual();
		
		if(indQ.getLocalName().equalsIgnoreCase(
				indC.getLocalName())) return 1;
		
		queryParent = cQuery.getParent(0);
		caseParent = cCase.getParent(0);
	
		if(queryParent.equals(caseParent)) return 0.5;


		return 0;
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
