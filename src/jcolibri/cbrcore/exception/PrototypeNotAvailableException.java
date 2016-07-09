package jcolibri.cbrcore.exception;

/**
 * Exception looking for the prototype of a method or task.
 * 
 * @author Juan José Bello
 */
public class PrototypeNotAvailableException extends java.lang.RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>PrototypeNotAvailableException</code> without
	 * detail message.
	 */
	public PrototypeNotAvailableException() {
	}

	/**
	 * Creates a new <code>PrototypeNotAvailableException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public PrototypeNotAvailableException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new <code>EPrototypeNotAvailableException</code> with the
	 * specified detail message.
	 * 
	 * @param th
	 *            the cause of the exception.
	 */
	public PrototypeNotAvailableException(Throwable th) {
		super(th);
	}
}
