package jcolibri.method;

import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcore.CBRContext;

/**
 * Adds the cases in the context to the CBRCaseBase.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class StoreCasesMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public StoreCasesMethod() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		try {
			CBRCaseBase casebase = context.getCBRCaseBase();
			casebase.learnCases(context.getCases());
			return context;

		} catch (Exception e) {
			throw new jcolibri.cbrcore.exception.ExecutionException(
					"Error executing StoreCasesMethod: " + e.getMessage());
		}
	}

}