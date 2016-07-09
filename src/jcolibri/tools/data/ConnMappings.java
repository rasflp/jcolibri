package jcolibri.tools.data;

import java.util.Vector;

/**
 * Mapping between the simple attributes of a case and the columns of the media
 * storage.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class ConnMappings {

	/** Collection of ConnMapping. */
	private Vector<ConnMapping> mappings;

	/**
	 * Constructor.
	 */
	public ConnMappings() {
		mappings = new Vector<ConnMapping>();
	}

	/**
	 * Adds a new mapping.
	 * 
	 * @param mapping
	 *            new mapping.
	 */
	public void addMapping(ConnMapping mapping) {
		mappings.add(mapping);
	}

	/**
	 * Returns the number of mappings.
	 * 
	 * @return number of mappings.
	 */
	public int getMappingsCount() {
		return mappings.size();
	}

	/**
	 * Returns a mapping by index.
	 * 
	 * @param idx
	 *            index.
	 * @return ConnMapping.
	 */
	public ConnMapping getMapping(int idx) {
		return (ConnMapping) mappings.get(idx);
	}

	/**
	 * Sets the mapping of index idx.
	 * 
	 * @param idx
	 *            index.
	 * @param mapping
	 *            new mapping.
	 */
	public void setMapping(int idx, ConnMapping mapping) {
		mappings.setElementAt(mapping, idx);
	}

	/**
	 * Remove a mapping by index.
	 * 
	 * @param idx
	 *            index.
	 */
	public void removeMapping(int idx) {
		mappings.removeElementAt(idx);
	}
}
