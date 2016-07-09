package jcolibri.cbrcore;

import java.io.Serializable;

import jcolibri.gui.ParameterEditorFactory;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents the type of a simple attribute of a case.
 * 
 * @author Juan José Bello
 */
public class DataType implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The name of the type. */
	protected String name;

	/** The name of the java class associated with the type. */
	protected String javaClassName;

	/**
	 * Constructor.
	 */
	public DataType() {
		this("");
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the type.
	 */
	public DataType(String name) {
		this(name, "java.lang.object");
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the type.
	 * @param javaClassName
	 *            name of the java class associated with this type.
	 */
	public DataType(String name, String javaClassName) {
		this.name = name;
		this.javaClassName = javaClassName;
	}

	/**
	 * Returns the name of the type.
	 * 
	 * @return name of the type
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the type.
	 * 
	 * @param name
	 *            new name of the type.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the java class associated with this type.
	 * 
	 * @return name of the java class associated with this type.
	 */
	public String getJavaClassName() {
		return javaClassName;
	}

	/**
	 * Sets the name of the java class associated with this type
	 * 
	 * @param javaClassName
	 *            name of the java class associated with this type.
	 */
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	/**
	 * Returns the java class associate with this type.
	 * 
	 * @return the java class associate with this type.
	 * @throws ClassNotFoundException
	 */
	public Class getJavaClass() throws ClassNotFoundException {
		return Class.forName(javaClassName);
	}

	/**
	 * Returns an instance of the java class associated with this class.
	 * 
	 * @return an instance of the java class associated with this class.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Object getInstance() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return Class.forName(javaClassName).newInstance();
	}

	/**
	 * Checks if 2 DataTypes are equal.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof DataType))
			return false;
		DataType dto = (DataType) o;
		return dto.name.equals(this.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

	/**
	 * Returns a DOM node thet represents this instance.
	 * 
	 * @param document
	 *            DOM document.
	 * @return a DOM node thet represents this instance.
	 */
	public Element toXMLDOM(Document document) {
		Element elemDataType, elemAux;

		// <DataType>.
		elemDataType = document.createElement("DataType");

		// <DataType><Name>.
		elemAux = document.createElement("Name");
		elemAux.setTextContent(name);
		elemDataType.appendChild(elemAux);

		// <DataType><Class>.
		elemAux = document.createElement("Class");
		elemAux.setTextContent(javaClassName);
		elemDataType.appendChild(elemAux);

		// <DataType><GUIEditor>.
		elemAux = document.createElement("GUIEditor");
		elemAux.setTextContent(ParameterEditorFactory.getEditor(this)
				.getClass().getName());
		elemDataType.appendChild(elemAux);
        
		return elemDataType;
	}

	/**
	 * Reads an instance from a DOM node.
	 * 
	 * @param elem
	 *            DOM node.
	 */
	public void fromXMLDOM(Element elem) {

		String guiEditor;

		name = XMLUtil.getFirstChild(elem, "Name").getTextContent();
		javaClassName = XMLUtil.getFirstChild(elem, "Class").getTextContent();
		guiEditor = XMLUtil.getFirstChild(elem, "GUIEditor").getTextContent();
		ParameterEditorFactory.registerEditor(name, guiEditor);
        
	}
}
