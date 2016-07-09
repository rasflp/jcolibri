package jcolibri.method;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.gui.GenericMultipleCasesChooserPane;
import jcolibri.tools.data.CaseStructure;
import jcolibri.util.CBRLogger;

/**
 * Builds a frame using the case structure file and shows the cases in the
 * context to the user. The user can select the cases that must be store in the
 * CBRCaseBase.
 * 
 * @author Juan Antonio Recio García
 */
public class RetainChooserMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the case structure file path. */
	public static final String CASE_STRUCTURE_FILE = "Case Structure";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		String configFile = (String) this
				.getParameterValue(CASE_STRUCTURE_FILE);
		CaseStructure caseStructure = new CaseStructure();
		caseStructure.readFromXMLFile(configFile);

		GenericMultipleCasesChooserPane gmccp = new GenericMultipleCasesChooserPane(
				context.getCases(), caseStructure, context.getCBRCaseBase()
						.getAll().size() + 1);
		gmccp.setVisible(true);
		java.util.List<CBRCase> list = gmccp.getCasesToStore();
		context.setCases(list);
		CBRLogger.log(this.getClass(), CBRLogger.INFO, "User has selected "
				+ list.size() + " cases to store");
		return context;
	}

}
