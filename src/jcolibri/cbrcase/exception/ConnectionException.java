package jcolibri.cbrcase.exception;

/**
 * Connection exception will be thrown whenever there is a connection problem
 * between JColibrí and any external process like RACER
 */
public class ConnectionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConnectionException(Throwable th) {
		super(th);
	}

}
