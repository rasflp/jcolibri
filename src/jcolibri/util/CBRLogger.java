package jcolibri.util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for creating an application run time log.
 * 
 * @author Juan José Bello
 */
public class CBRLogger {

	/** Information type. */
	public static final int INFO = 0;

	/** Error type. */
	public static final int ERROR = 1;

	/**
	 * Constructor.
	 */
	private CBRLogger() {
	}

	/**
	 * Creates a log message.
	 * 
	 * @param clase
	 *            source class.
	 * @param level
	 *            information or erro.
	 * @param message
	 *            user message.
	 */
	public static void log(Class clase, int level, String message) {
		disperseData(clase.getName(), getLevel(level), message);
		Logger l = Logger.getLogger(clase.getName());
		l.log(getLevel(level), message);
	}

	/**
	 * Creates a log message.
	 * 
	 * @param clase
	 *            source class name.
	 * @param level
	 *            information or erro.
	 * @param message
	 *            user message.
	 */
	public static void log(String clase, int level, String message) {
		disperseData(clase, getLevel(level), message);
		Logger l = Logger.getLogger(clase);
		l.log(getLevel(level), message);
	}

	/**
	 * Creates a log message of a exception.
	 * 
	 * @param clase
	 *            source class.
	 * @param message
	 *            user message.
	 * @param t
	 *            exception.
	 */
	public static void log(Class clase, String message, Throwable t) {
		disperseData(clase.getName(), Level.SEVERE, message + "\n"
				+ t.toString());
		Logger l = Logger.getLogger(clase.getName());
		l.log(Level.SEVERE, message, t);
	}

	/**
	 * Creates a log message of a exception.
	 * 
	 * @param clase
	 *            source class name.
	 * @param message
	 *            user message.
	 * @param t
	 *            exception.
	 */
	public static void log(String clase, String message, Throwable t) {
		disperseData(clase, Level.SEVERE, message + "\n" + t.toString());
		Logger l = Logger.getLogger(clase);
		l.log(Level.SEVERE, message, t);
	}

	/**
	 * Returns the java.util.logging.Level constant related to a level.
	 * 
	 * @param level
	 * @return java.util.logging.Level constant
	 */
	private static Level getLevel(int level) {
		switch (level) {
		case INFO:
			return Level.INFO;
		case ERROR:
			return Level.SEVERE;
		default:
			return Level.ALL;
		}
	}

	/** Listener. */
	private static ArrayList<LogListener> _listeners = new ArrayList<LogListener>();

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 */
	public static void addListener(LogListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener
	 */
	public static void removeListener(LogListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * Sends a log message to the listener.
	 * 
	 * @param clase
	 *            source class name.
	 * @param level
	 *            information or error.
	 * @param message
	 *            user message.
	 */
	private static void disperseData(String clase, Level level, String message) {
		String reducedClassName = clase.substring(clase.lastIndexOf(".") + 1);
		String data = '[' + reducedClassName + "] " + level.getLocalizedName()
				+ ": " + message + '\n';
		for (java.util.Iterator iter = _listeners.iterator(); iter.hasNext();) {
			LogListener listener = (LogListener) iter.next();
			listener.receivedData(data);
		}
	}

}
