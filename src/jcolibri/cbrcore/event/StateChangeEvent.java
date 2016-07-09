package jcolibri.cbrcore.event;

/**
 * Event to be launched when a change on the state of the CBR system is
 * detected.
 */
public class StateChangeEvent extends java.util.EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>StateChangeEvent</code>.
	 * 
	 * @param source
	 *            producer of the event.
	 */
	public StateChangeEvent(Object source) {
		super(source);
	}
}
