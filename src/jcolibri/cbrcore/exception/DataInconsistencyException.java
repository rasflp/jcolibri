package jcolibri.cbrcore.exception;

/**
 * Exception using not consistent data.
 * 
 * @author Juan José Bello
 */
public class DataInconsistencyException extends java.lang.RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>DataInconsistencyException</code> without detail
	 * message.
	 */
	public DataInconsistencyException() {
	}

	/**
	 * Creates a new <code>DataInconsistencyException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            detail message.
	 */
	public DataInconsistencyException(String msg) {
		super(msg);
	}
}
