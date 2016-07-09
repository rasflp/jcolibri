package jcolibri.cbrcase;

import java.util.Collection;

import jcolibri.similarity.SimilarityFunction;

/**
 * Represents the invidual concept. CBRCase are individuals, their attributes
 * are also individuals.
 */
public interface Individual {

	/**
	 * Returns the value object for this instance
	 * 
	 * @return the value object for this instance
	 */
	public Object getValue();

	/**
	 * Sets the value object for this instance
	 * 
	 * @param value
	 */
	public void setValue(Object value);

	/**
	 * Returns the parents of this individual
	 * 
	 * @return Array with its parents, in a conceptual hierarchy
	 */
	public Individual[] getParents();

	/**
	 * Set the parents for this instance
	 * 
	 * @param parents
	 */
	public void setParents(Individual[] parents);

	/**
	 * Returns the relations of this instance with others individuals
	 * 
	 * @return a collection of IndividualRelation objects
	 */
	public Collection<IndividualRelation> getRelations();

	/**
	 * Add a new IndividualRelation to current ones
	 * 
	 * @param relation
	 */
	public void addRelation(IndividualRelation relation);

	/**
	 * Sets the relations of this instance with others individuals
	 * 
	 * @param relationsWithIndividuals
	 */
	public void setRelations(Collection relationsWithIndividuals);

	/**
	 * Returns a specific relation of this individual
	 * 
	 * @param description
	 *            Name of the relation that is going to be retrieved
	 * @return The invidualRelation identified with the description
	 */
	public IndividualRelation getRelation(String description);

	/**
	 * Registers the similarity function of this individual
	 * 
	 * @param function
	 *            Similarity function
	 */
	public void registerSimilarity(SimilarityFunction function);

	/**
	 * Returns the SimilarityFunction associated to this individual
	 * 
	 * @return The SimilarityFunction associated to this individual
	 */
	public SimilarityFunction getSimilarityFunction();

}