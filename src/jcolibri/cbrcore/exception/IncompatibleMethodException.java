package jcolibri.cbrcore.exception;

/**
 * Exception attempting to add a method incompatible with the context.
 * 
 * @author Juan José Bello
 */
public class IncompatibleMethodException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>IncompatibleMethodException</code> without detail
	 * message.
	 */
	public IncompatibleMethodException() {
	}

	/**
	 * Creates a new <code>IncompatibleMethodException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            detail message.
	 */
	public IncompatibleMethodException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>IncompatibleMethodException</code> with the
	 * specified detail message.
	 * 
	 * @param th
	 *            cause of the exception.
	 */
	public IncompatibleMethodException(Throwable th) {
		super(th);
	}
}
