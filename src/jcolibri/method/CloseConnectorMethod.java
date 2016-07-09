package jcolibri.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * Close the connection to the CBRCaseBase.
 * 
 * @author Juan Antonio Recio García
 */
public class CloseConnectorMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		context.getCBRCaseBase().closeAndUpdate();
		return context;
	}

}
