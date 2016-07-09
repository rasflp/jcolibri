package jcolibri.method;

import java.util.Iterator;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;
import jcolibri.util.ProgressBar;

/**
 * Simple adaptation method. Computes the value of a simple solution attribute
 * related to a simple description attribute as proportional to the actual
 * values of these attributes in a retrieved case.
 * 
 * @author
 * @version 1.0
 */
public class NumericDirectProportionMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the name of the description attribute. */
	public static String DESCRIPTION_ATTRIBUTE = "Description Attribute";

	/** Name of the parameter having the name of the solution attribute. */
	public static String SOLUTION_ATTRIBUTE = "Solution Attribute";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		String descriptionAtt = (String) this
				.getParameterValue(DESCRIPTION_ATTRIBUTE);
		String solutionAtt = (String) this
				.getParameterValue(SOLUTION_ATTRIBUTE);

		try {
			Individual querySource = CaseCreatorUtils.getAttribute(context
					.getQuery(), descriptionAtt);
			Number number = (Number) querySource.getValue();
			double queryDescrValue = number.doubleValue();

			ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
					.getCases().size());
			for (Iterator iter = context.getCases().iterator(); iter.hasNext();) {
				CBRCase _case = (CBRCase) iter.next();
				Individual source = CaseCreatorUtils.getAttribute(_case,
						descriptionAtt);
				Individual target = CaseCreatorUtils.getAttribute(_case,
						solutionAtt);
				ProgressBar.PROGRESSBAR.step();

				number = (Number) source.getValue();
				double caseDescrValue = number.doubleValue();
				number = (Number) target.getValue();
				double caseSolValue = number.doubleValue();

				double res = (caseSolValue / caseDescrValue) * queryDescrValue;

				Object targetValue = target.getValue();

				if (targetValue instanceof Integer)
					target.setValue(new Integer((int) res));
				else if (targetValue instanceof Double)
					target.setValue(new Double(res));
				else
					throw new Exception(
							"Unrecognoized number class when storing result.");

				source.setValue(querySource.getValue());

			}
		} catch (Exception e) {
			CBRLogger
					.log(
							this.getClass(),
							CBRLogger.ERROR,
							"Error computing direct proportion. Be sure that description and solution attributes are subclases of Integer or Double. Error message: "
									+ e.getMessage());
			e.printStackTrace();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		return context;
	}

}
