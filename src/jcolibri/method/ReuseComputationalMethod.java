package jcolibri.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * Solves the Reuse Task by decomposition.
 * 
 * @author
 * @version 1.0
 */

public class ReuseComputationalMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		return context;
	}

}
