package jcolibri.cbrcase;

import java.util.Collection;
import java.util.Properties;

import jcolibri.cbrcore.exception.InitializingException;

/**
 * Connector interface declares the methodd needed to be implemented by the
 * classes that will access to the cases from the phisical storage.
 */
public interface Connector {

	/**
	 * Defines the property used by the implementations of this interface to
	 * obtain the XML configuration file
	 */
	public static final String CONFIG_FILE = "configFile";

	/**
	 * Initialices the connector with the given properties
	 * 
	 * @param props
	 *            Should contain any property needed for the initialization of
	 *            the connector
	 * @throws InitializingException
	 *             Raised if the connector can not be initialezed.
	 */
	public void init(Properties props) throws InitializingException;

	/**
	 * Cleanup any resource that the connector might be using, and suspends the
	 * service
	 * 
	 */
	public void close();

	/**
	 * Stores given classes on the storage media
	 * 
	 * @param cases
	 *            List of cases
	 */
	public void storeCases(Collection<CBRCase> cases);

	/**
	 * Deletes given cases for the storage media
	 * 
	 * @param cases
	 *            List of cases
	 */
	public void deleteCases(Collection<CBRCase> cases);

	/**
	 * Returns max cases without any special consideration
	 * 
	 * @param max
	 *            Maximum number of cases that could be returned
	 * @return The list of retrieved cases
	 */
	public Collection<CBRCase> retrieveAllCases();

	/**
	 * Retrieves some cases depending on the filter. TODO.
	 */
	public Collection<CBRCase> retrieveSomeCases(String filter);

}
