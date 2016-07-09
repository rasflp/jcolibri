package jcolibri.method;

import java.util.Iterator;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcase.CaseEvalList;
import jcolibri.cbrcase.CaseEvaluation;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.similarity.SimilarityFunction;
import jcolibri.util.CBRLogger;
import jcolibri.util.ProgressBar;

/**
 * Computes the similarities between the query and each case in the context.
 * Sets the result of these evaluations in the context.
 * 
 * @author
 * @version 1.0
 */

public class NumericSimComputationMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		CBRQuery query = context.getQuery();

		// IndividualRelation rel =
		// query.getRelation(CaseStDescription.RELATION_NAME);
		Individual queryDescription = query.getDescription();// rel.getTarget();
		SimilarityFunction simFunction = queryDescription
				.getSimilarityFunction();

		CaseEvalList evalList = new CaseEvalList();
		ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
				.getCases().size());
		for (Iterator iter = context.getCases().iterator(); iter.hasNext();) {
			CBRCase cbrcase = (CBRCase) iter.next();
			Individual caseDescription = cbrcase.getDescription();// getRelation(CaseStDescription.RELATION_NAME).getTarget();
			double res = simFunction.compute(queryDescription, caseDescription);
			CaseEvaluation caseEval = new CaseEvaluation(cbrcase, new Double(
					res));
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Similarity with case " + cbrcase.getValue() + " :" + res);
			evalList.add(caseEval);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		context.setEvaluatedCases(evalList);
		return context;
	}

}