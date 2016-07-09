package jcolibri.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcore.DataType;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Enumeration type of strings.
 * 
 * @author Juan A. Recio-García
 */
public class StringEnumType extends DataType {
	private static final long serialVersionUID = 1L;

	/** Possible values of the enumeration. */
	List<String> possibleValues;

	/**
	 * Constructor.
	 */
	public StringEnumType() {
		this("");
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the type.
	 */
	public StringEnumType(String name) {
		this(name, new ArrayList<String>());
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the type.
	 * @param possibleValues
	 *            possible values of the enumeration.
	 */
	public StringEnumType(String name, List<String> possibleValues) {
		super(name, StringEnumType.class.getName());
		this.possibleValues = possibleValues;
	}

	/**
	 * Returns the possible values of the enumeration.
	 * 
	 * @return possible values of the enumeration.
	 */
	public List getPossibleValues() {
		return possibleValues;
	}

	/**
	 * Sets the possible values of the enumeration.
	 * 
	 * @param values
	 *            the possible values of the enumeration.
	 */
	public void setPossibleVales(List<String> values) {
		this.possibleValues = values;
	}

	/**
	 * Returns the number of possible values.
	 * 
	 * @return the number of possible values.
	 */
	public int getNumPossibleValues() {
		return possibleValues.size();
	}

	/**
	 * Returns the ordinal of a specific value.
	 * 
	 * @param val
	 *            value.
	 * @return the ordinal of the value.
	 */
	public int getOrd(String val) {
		return possibleValues.indexOf(val);
	}

	/**
	 * Returns a variable of this type.
	 */
	public Object getInstance() {
		String value = "";

		if ((possibleValues != null) && (possibleValues.size() > 0))
			value = (String) possibleValues.get(0);
		return new StringEnum(this, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof StringEnumType) {
			StringEnumType other = (StringEnumType) o;
			return name.equals(other.name);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.DataType#fromXMLDOM(org.w3c.dom.Element)
	 */
	public void fromXMLDOM(Element elem) {
		Element elemEnumeration, elemValue;
		List list;

		super.fromXMLDOM(elem);

		elemEnumeration = XMLUtil.getFirstChild(elem, "Enumeration");
		list = XMLUtil.getChild(elemEnumeration, "Value");
		possibleValues = new LinkedList<String>();
		for (int i = 0; i < list.size(); i++) {
			elemValue = (Element) list.get(i);
			possibleValues.add(elemValue.getTextContent());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.DataType#toXMLDOM(org.w3c.dom.Document)
	 */
	public Element toXMLDOM(Document document) {
		Element elemDataType, elemEnumeration, elemAux;

		elemDataType = super.toXMLDOM(document);

		// <Enumeration>.
		elemEnumeration = document.createElement("Enumeration");
		elemDataType.appendChild(elemEnumeration);

		// <Enumeration><Values><Value>.
		for (Iterator it = possibleValues.iterator(); it.hasNext();) {
			elemAux = document.createElement("Value");
			elemAux.setTextContent((String) it.next());
			elemEnumeration.appendChild(elemAux);
		}

		return elemDataType;
	}
}
