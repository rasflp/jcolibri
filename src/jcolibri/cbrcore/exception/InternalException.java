package jcolibri.cbrcore.exception;

/**
 * Exception reading o writing the configutation data files.
 * 
 * @author Juan José Bello
 */
public class InternalException extends java.lang.Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>InternalException</code> without detail message.
	 */
	public InternalException() {
	}

	/**
	 * Creates a new <code>InternalException</code> with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            detail message.
	 */
	public InternalException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new <code>InternalException</code> with the specified detail
	 * message.
	 * 
	 * @param th
	 *            cause of the exception.
	 */
	public InternalException(Throwable th) {
		super(th);
	}
}
