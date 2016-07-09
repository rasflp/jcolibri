package jcolibri.util;

/**
 * Classes that want to receive the log data must implement this interface.
 * 
 * @author Juan A. Recio-García
 */
public interface LogListener {
	public abstract void receivedData(String data);
}
