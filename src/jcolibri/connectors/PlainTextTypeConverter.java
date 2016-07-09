package jcolibri.connectors;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import jcolibri.util.CBRLogger;

/**
 * Converts data types between its textual representation and Java types. By
 * default it only manages a few data types:
 * <ul>
 * <li>BigDecimal
 * <li>Boolean
 * <li>Byte
 * <li>Date
 * <li>Double
 * <li>Float
 * <li>Int
 * <li>Long
 * <li>Object
 * <li>Short
 * <li>String
 * <li>URL
 * </ul>
 * Even so, developers can store any class in the data base if the class
 * implements the jcolibri.connectors.TypeAdaptor interface.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see jcolibri.connectors.TypeAdaptor
 */
public class PlainTextTypeConverter {

	/**
	 * Coverts a string representation of the data in an object.
	 * 
	 * @param data
	 *            string representation of the data.
	 * @param type
	 *            type of the data.
	 * @return the object trepresented by data and type.
	 */
	protected static Object convert(String data, String type) {
		try {
			if (type.equals("BigDecimal"))
				return new BigDecimal(data);
			else if (type.equals("Boolean"))
				return new Boolean(data);
			else if (type.equals("Byte"))
				return new Byte(data);
			else if (type.equals("Date"))
				return new SimpleDateFormat().parse(data);
			else if (type.equals("Double"))
				return new Double(data);
			else if (type.equals("Float"))
				return new Float(data);
			else if (type.equals("Integer"))
				return new Integer(data);
			else if (type.equals("Long"))
				return new Long(data);
			else if (type.equals("Object"))
				return data;
			else if (type.equals("Short"))
				return new Short(data);
			else if (type.equals("String"))
				return data;
			else if (type.equals("URL"))
				return data;
			else {
				
				// ##Fixed by rasflp@gmail.com on Sep 7, 2016
				
				if (type.toLowerCase().indexOf("enum") != -1)
					return data;
				else {
					TypeAdaptor adaptor = (TypeAdaptor) Class.forName(type)
							.newInstance();
					adaptor.fromString(data);
					return adaptor;
				}
			}
		} catch (Exception e) {
			CBRLogger.log("PlainTextTypeConverter", CBRLogger.ERROR,
					"Error converting types: " + e.getMessage());
		}

		return null;
	}
}