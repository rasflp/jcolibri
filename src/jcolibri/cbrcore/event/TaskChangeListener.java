package jcolibri.cbrcore.event;

/**
 * Interface to be implemented by task change listeners, to react when a
 * TaskChangeEvent is retrieved.
 */
public interface TaskChangeListener {

	/**
	 * This method is invoked when a change on the state of the CBR system is
	 * detected.
	 * 
	 * @param event
	 *            task change event.
	 */
	public void taskChanged(TaskChangeEvent event);
}
