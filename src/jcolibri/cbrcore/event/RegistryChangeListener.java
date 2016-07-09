package jcolibri.cbrcore.event;

/**
 * Interface to be implemented by registry change listeners, to react when a
 * RegistryChangeEvent is retrieved.
 */
public interface RegistryChangeListener {

	/**
	 * This method is invoked when a change on the registry is detected.
	 * 
	 * @param event
	 *            registry change event.
	 */
	public void registryChanged(RegistryChangeEvent event);

}
