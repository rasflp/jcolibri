package jcolibri.method;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CaseEvaluation;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * Select the first case in the context and discard the remainders.
 * 
 * @author
 * @version 1.0
 */

public class SelectBestCaseMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		Collection<CaseEvaluation> col = context.getEvaluatedCases();

		LinkedList<CBRCase> result = new LinkedList<CBRCase>();
		Iterator<CaseEvaluation> it = col.iterator();
		if (it.hasNext())
			result.add(it.next().getCase());
		context.setCases(result);
		return context;
	}

}