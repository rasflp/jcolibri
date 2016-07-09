package jcolibri.cbrcase;

import java.util.Collection;

import jcolibri.cbrcore.CBRTerm;
import jcolibri.similarity.SimilarityFunction;

/**
 * This interface defines the methods that at least any Case Base must implement
 * to be transparently used by the methods on the framework
 */
public interface CBRCaseBase extends CBRTerm {

	/**
	 * Get the connector instance that will use to access the media to retrieve
	 * the CBR Cases
	 * 
	 * @param connector
	 */
	public void setConnector(Connector connector);

	/**
	 * Loads all the Case reachable for the connector into memory.
	 * 
	 */
	public void loadAll();

	/**
	 * Loads the cases the match the restrictions given by the filter.
	 */
	public void loadFor(String filter);

	/**
	 * Returns all the cases available on this case base
	 * 
	 * @return all the cases available on this case base
	 */
	public Collection<CBRCase> getAll();

	/**
	 * Returns the k nearest cases to the one given
	 * 
	 * @param cbrcase
	 *            Case that will guide the searh. Retrieved cases will be
	 *            similar to this one
	 * @param numCases
	 *            Number of cases to be returned
	 * @param function
	 *            Similarity function that is used to get the similarity between
	 *            two cases
	 * @return Returns a collection containing CaseEvaluation objects
	 */
	public CaseEvalList knn(CBRCase cbrcase, int numCases,
			SimilarityFunction function);

	/**
	 * Finalizes the work with Case Base, the changed because of cases learned
	 * or forgoten will be discarded.
	 * 
	 */
	public void close();

	/**
	 * Add a collection of new CBRCase objects to the current work session
	 * 
	 * @param cases
	 *            to be added
	 */
	public void learnCases(Collection<CBRCase> cases);

	/**
	 * Remove a collection of new CBRCase objects to the current work session
	 * 
	 * @param cases
	 *            to be removed
	 */
	public void forgetCases(Collection<CBRCase> cases);

	/**
	 * Finalizes the work with Case Base but commiting the changes done because
	 * of learned or forgottern cases.
	 * 
	 */
	public void closeAndUpdate();

}
