package jcolibri.cbrcore.exception;

/**
 * Exception in the execution of a method.
 */
public class ExecutionException extends java.lang.Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>ExecutionException</code> without a detail message.
	 */
	public ExecutionException() {
	}

	/**
	 * Creates a new <code>ExecutionException</code> with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            description message.
	 */
	public ExecutionException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>ExecutionException</code> with the specified
	 * detail message.
	 * 
	 * @param th
	 *            cause of the exception.
	 */
	public ExecutionException(Throwable th) {
		super(th);
	}

}
