package jcolibri.method;

import java.util.ArrayList;
import java.util.Iterator;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;

/**
 * Copies the cases in the context. It must be made before begining the
 * adaptation process.
 * 
 * @author Juan Antonio Recio García
 */
public class CopyCasesforAdaptationMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the case structure file path. */
	public static final String CASE_STRUCTURE_FILE = "Case Structure";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {

		String configFile = (String) this.getParameterValue(CASE_STRUCTURE_FILE);

		ArrayList<CBRCase> list = new ArrayList<CBRCase>();
		for (Iterator<CBRCase> iter = context.getCases().iterator(); iter.hasNext();) {
			CBRCase ce = iter.next();
			list.add(CaseCreatorUtils.copyCase(configFile, ce));
		}
		context.setCases(list);
		CBRLogger.log(this.getClass(), CBRLogger.INFO,
				"Removing evaluation info in " + list.size()
						+ " working cases.");
		return context;
	}

}
