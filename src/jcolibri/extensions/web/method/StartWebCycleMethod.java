package jcolibri.extensions.web.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.extensions.web.bridge.Synchronizer;
import jcolibri.util.CBRLogger;

/**
 * Method that solves the CBR cycle task by decomposition.
 * 
 * @author Juan José Bello
 */
public class StartWebCycleMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Waiting for Web Session");
        Synchronizer.cycleWaitsForWebSession();
		return context;
	}

}
