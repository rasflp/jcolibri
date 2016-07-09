/**
 * 
 */
package jcolibri.extensions.DL.similarity.local;

import java.util.HashMap;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import jcolibri.cbrcase.Individual;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.similarity.SimilarityFunction;

/**
 * @author A. Luis Diez Hernández
 *
 */
public class Detail implements SimilarityFunction {

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
		
							
			
		int common;
		common = 0;
		Vector<Resource> superQuery = new Vector<Resource>();
		for(ExtendedIterator it= cQuery.getConcept().listSuperClasses();it.hasNext();){
			superQuery.add((Resource)it.next());
					}
		
		for(ExtendedIterator it2 = cCase.getConcept().listSuperClasses();it2.hasNext();){
			Resource nxt = (Resource)it2.next();
			if(superQuery.contains(nxt)) common++;
		}		
		double divider = 2*common;
		
		if(divider==0) divider=0.000001;
		
		double deep = 1/divider;
		
							
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
