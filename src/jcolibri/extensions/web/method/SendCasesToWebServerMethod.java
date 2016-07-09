package jcolibri.extensions.web.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.extensions.web.bridge.Synchronizer;
import jcolibri.extensions.web.bridge.WebBridge;

/**
 * Method that solves the CBR System task by decomposition in Precycle, Cycle
 * and Postcycle.
 * 
 * @author Juan Antonio Recio García
 */
public class SendCasesToWebServerMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Method execution code.
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
        WebBridge.putData("RETRIEVED_CASES", context.getCases());
        Synchronizer.cycleReleasesTurn();
		return context;
	}

}
