package jcolibri.method;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;

/**
 * Sets all the cases from the CBRCaseBase in the context.
 * 
 * @author Juan Antonio Recio García
 */
public class SelectAllCasesMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		context.setCases(new java.util.ArrayList<CBRCase>(context.getCBRCaseBase()
				.getAll()));
		CBRLogger.log(this.getClass(), CBRLogger.INFO, "Selecting all cases.");
		return context;
	}

}
