package jcolibri.tools.data;

/**
 * Mapping between a simple attribute of a case and a concept of the media
 * storage.
 * 
 * @author A. Luis Diez Hernández -- Antonio A. Sánchez Ruiz-Granados
 */
public class ConnOwlMapping {

	/** Simple attribute name (path). */
	private String attribute;

	/** Property local name*/
	private String property;
	
	/** Concept local name. */
	private String concept;

	/**
	 * Constructor.
	 * 
	 * @param attribute
	 *            simple attribute name (path).
	 * @param concept
	 *            concept name.
	 */
	public ConnOwlMapping(String attribute, String property, String concept) {
		super();
		this.attribute = attribute;
		this.property = property;
		this.concept = concept;
	}

	/**
	 * Returns the simple attribute name (path).
	 * 
	 * @return simple attribute name (path).
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Sets the simple attribute name (path).
	 * 
	 * @param attribute
	 *            new simple attribute name.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns the Property name.
	 * 
	 * @return property name.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Sets the property name.
	 * 
	 * @param property
	 *            property name.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	
	/**
	 * Returns the concept name.
	 * 
	 * @return concept name.
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * Sets the concept name.
	 * 
	 * @param concept
	 *            concept name.
	 */
	public void setColumn(String concept) {
		this.concept = concept;
	}
}

