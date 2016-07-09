package jcolibri.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.gui.GenericMultipleCasesEditPane;
import jcolibri.tools.data.CaseStructure;

/**
 * Builds a frame using the case structure file to display the cases in the
 * context to the user. The user can change manualy the cases values.
 * 
 * @author Juan Antonio Recio García
 */
public class ManualRevisionMethod extends jcolibri.cbrcore.CBRMethod {
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

		GenericMultipleCasesEditPane gmcep = new GenericMultipleCasesEditPane(
				context.getCases(), caseStructure);
		gmcep.setVisible(true);
		return context;
	}
}
