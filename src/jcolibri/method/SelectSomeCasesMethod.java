package jcolibri.method;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CaseEvaluation;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;

/**
 * Select the first n cases from the context and discard the remainders.
 * 
 * @author
 * @version 1.0
 */

public class SelectSomeCasesMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		int i = 0;
		int num = ((Integer) this.getParameterValue("Number of cases")).intValue();
        
		Collection<CaseEvaluation> col = context.getEvaluatedCases();
		
		LinkedList<CBRCase> result = new LinkedList<CBRCase>();
		for (Iterator<CaseEvaluation> it = col.iterator(); it.hasNext() && i < num;) {
			result.add(it.next().getCase());
			i++;
		}
		context.setCases(result);
		return context;
	}

}