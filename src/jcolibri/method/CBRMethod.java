package jcolibri.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * Method that solves the CBR cycle task by decomposition.
 * 
 * @author Juan José Bello
 */
public class CBRMethod extends jcolibri.cbrcore.CBRMethod {
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
