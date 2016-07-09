package jcolibri.extensions.textual.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;

/**
 * <p>
 * Empty method that can be used when user doesn't want to include a layer.
 * </p>
 * <p>
 * Developers should be carefull because there are dependencies between layers.
 * For example, you need the POS tags in the Glossary layer
 * <p>
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
public class SkipThisTaskMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public SkipThisTaskMethod() {
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