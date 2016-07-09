package jcolibri.cbrcore.event;

/**
 * Event to be launched when a change on a registry is detected.
 */
public class RegistryChangeEvent extends java.util.EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>RegistryChangeEvent</code>.
	 * 
	 * @param source
	 *            producer of the event.
	 */
	public RegistryChangeEvent(Object source) {
		super(source);
	}

}
