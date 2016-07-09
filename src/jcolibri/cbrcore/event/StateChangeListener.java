package jcolibri.cbrcore.event;

/**
 * Interface to be implemented by atate change listeners, to react when a
 * StateChangeEvent is retrieved.
 */
public interface StateChangeListener {

	/**
	 * This method is invoked when a change on the state of the CBR system is
	 * detected.
	 * 
	 * @param event
	 *            state change event.
	 */
	public void stateChanged(StateChangeEvent event);

}
