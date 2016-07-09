package jcolibri.method;

import java.util.Iterator;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;
import jcolibri.util.JColibriClassHelper;
import jcolibri.util.ProgressBar;

/**
 * Simple adaptation method. Copies the value of an attribute in the description
 * of the case to the solution.
 * 
 * @author
 * @version 1.0
 */

public class DirectCopyMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the name os the description attribute. */
	public static String DESCRIPTION_ATTRIBUTE = "Description Attribute";

	/** Name of the parameter having the name os the solution attribute. */
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

		ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
				.getCases().size());
		for (Iterator iter = context.getCases().iterator(); iter.hasNext();) {
			CBRCase _case = (CBRCase) iter.next();
			Individual source = CaseCreatorUtils.getAttribute(_case,
					descriptionAtt);
			Individual target = CaseCreatorUtils.getAttribute(_case,
					solutionAtt);
			ProgressBar.PROGRESSBAR.step();
			if ((source != null) && (target != null)) {
				// This is a trick because we don't know the class of the value.
				// We need a copy, so we serialize the object and obtain it.
				try {
					String objseralizated = JColibriClassHelper
							.serializeObject((java.io.Serializable) source
									.getValue());
					Object newObject = JColibriClassHelper
							.deserializeObject(objseralizated);
					target.setValue(newObject);
				} catch (Exception e) {
					CBRLogger.log(this.getClass(), CBRLogger.ERROR,
							"Error coping value from: " + descriptionAtt
									+ " to: " + solutionAtt
									+ "\nValue to copy is: "
									+ source.getValue() + " class: "
									+ source.getValue().getClass().getName());
				}

			}

		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		return context;
	}

}
