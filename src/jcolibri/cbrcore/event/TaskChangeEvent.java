package jcolibri.cbrcore.event;

/**
 * Event to be launched when a change on the state of the CBR system is
 * detected.
 */
public class TaskChangeEvent extends java.util.EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>TaskChangeEvent</code>.
	 * 
	 * @param source
	 *            producer of the event.
	 */
	public TaskChangeEvent(Object source) {
		super(source);
	}

}
