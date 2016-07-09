package jcolibri.cbrcase;

import jcolibri.cbrcore.CBRTerm;

/**
 * Represents a CBR query over the system
 */
public interface CBRQuery extends Individual, CBRTerm {

	/**
	 * Returns the description of the query.
	 * 
	 * @return the description of the query.
	 */
	public Individual getDescription();
}
