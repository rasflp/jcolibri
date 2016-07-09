package jcolibri.extensions.textual.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;

/**
 * <p>
 * Decomposition method assigned to "Retrieve Task".
 * </p>
 * <p>
 * This method doesn't perform any action
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class TextualCBRRetrieveMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public TextualCBRRetrieveMethod() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		return context;
	}

}