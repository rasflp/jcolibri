package jcolibri.cbrcase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jcolibri.similarity.SimilarityFunction;

/**
 * Basic implemenation of Individual interface
 * 
 * @author Juan José Bello
 */
public class SimpleIndividual implements jcolibri.cbrcase.Individual {

	/** Value of this individual. */
	Object value;

	/** Parents in the individual hierarchy. */
	Individual[] parents;

	/** Relations with other individuals. */
	HashMap<String, IndividualRelation> relationsWithIndividuals;

	/**
	 * Creates a new instance of SimpleIndividual
	 * 
	 * @param The
	 *            value for this individual
	 */
	public SimpleIndividual(Object value) {
		this.value = value;
		relationsWithIndividuals = new HashMap<String, IndividualRelation>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#setParents(jcolibri.cbrcase.Individual[])
	 */
	public void setParents(Individual[] parents) {
		this.parents = parents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#getParents()
	 */
	public Individual[] getParents() {
		return parents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#getRelation(java.lang.String)
	 */
	public IndividualRelation getRelation(String description) {
		return (IndividualRelation) relationsWithIndividuals.get(description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#getRelations()
	 */
	public Collection<IndividualRelation> getRelations() {
		return relationsWithIndividuals.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#addRelation(jcolibri.cbrcase.IndividualRelation)
	 */
	public void addRelation(IndividualRelation relation) {
		relationsWithIndividuals.put(relation.getDescription(), relation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.Individual#setRelations(java.util.Collection)
	 */
	public void setRelations(Collection relationsWithIndividuals) {
		this.relationsWithIndividuals = new HashMap<String, IndividualRelation>();
		IndividualRelation relation;
		for (Iterator it = relationsWithIndividuals.iterator(); it.hasNext();) {
			relation = (IndividualRelation) it.next();
			this.relationsWithIndividuals.put(relation.getDescription(),
					relation);
		}
	}

	private SimilarityFunction similarity = null;

	/**
	 * Registers the similarity function of this individual
	 * 
	 * @param function
	 *            Similarity function
	 */
	public void registerSimilarity(SimilarityFunction function) {
		similarity = function;
	}

	/**
	 * Returns the SimilarityFunction associated to this individual
	 * 
	 * @return The SimilarityFunction associated to this individual
	 */
	public SimilarityFunction getSimilarityFunction() {
		return similarity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(value);
		sb.append("\n");
		Collection rels = getRelations();
		for (Iterator it = rels.iterator(); it.hasNext();) {
			sb.append(((IndividualRelation) it.next()).toString());
		}
		return sb.toString();
	}
}
