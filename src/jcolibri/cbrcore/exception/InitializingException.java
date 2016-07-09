package jcolibri.cbrcore.exception;

/**
 * Exception in the initialization of an object.
 * 
 * @author Juan José Bello
 */
public class InitializingException extends java.lang.Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>InitializingException</code> without detail
	 * message.
	 */
	public InitializingException() {
	}

	/**
	 * Creates a new <code>InitializingException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            detail message.
	 */
	public InitializingException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new <code>InitializingException</code> with the specified
	 * detail message.
	 * 
	 * @param th
	 *            cause of the exception.
	 */
	public InitializingException(Throwable th) {
		super(th);
	}
}
