package jcolibri.connectors;

import java.sql.ResultSet;
import java.sql.Types;

import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.util.CBRLogger;

/**
 * Converts data types between SQL and Java. By default it only manages a few
 * data types:
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
 * Even so, developers can store any class in the data base if that class
 * implements the jcolibri.connectors.TypeAdaptor interface.
 * <p>
 * The connector discovers the type using the xml configuration file. It is
 * stored in the ColumnType Node, so if you want to include a complex type, you
 * must write the class name there.
 * 
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see jcolibri.connectors.TypeAdaptor
 */
public class JDBCTypeConverter {
	/**
	 * Convert from jdbc.Types enum to string.
	 * 
	 * @param type
	 *            jdbc.Types
	 * @return string representation of the type.
	 */
	public static String typeToString(int type) {
		String str;

		switch (type) {
		case Types.BOOLEAN:
			str = "Boolean";
			break;
		case Types.FLOAT:
			str = "Float";
			break;
		case Types.DECIMAL:
		case Types.SMALLINT:
		case Types.INTEGER:
			str = "Integer";
			break;
		case Types.DOUBLE:
		case Types.REAL:
			str = "Double";
			break;
		case Types.VARCHAR:
		case Types.CHAR:
			str = "String";
			break;

		case Types.ARRAY:
		case Types.BIGINT:
		case Types.BINARY:
		case Types.BIT:
		case Types.BLOB:
		case Types.CLOB:
		case Types.DATALINK:
		case Types.DATE:
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
		case Types.LONGVARBINARY:
		case Types.LONGVARCHAR:
		case Types.NULL:
		case Types.NUMERIC:
		case Types.OTHER:
		case Types.REF:
		case Types.STRUCT:
		case Types.TIME:
		case Types.TIMESTAMP:
		case Types.TINYINT:
		case Types.VARBINARY:
		default:
			str = "";
			break;
		}

		return str;
	}

	/**
	 * Retrieves data from a JDBC ResultSet and transforms it to an apropiate
	 * Java type.
	 * <p>
	 * If the data type cannot be converted, it is supposed to implement
	 * TypeAdaptor. Its type is stored in the xml configuration file under the
	 * ColumnType node.
	 * 
	 * @param res
	 *            JDBC ResultSet
	 * @param column
	 *            Column of the object in the ResultSet. Read from the xml
	 *            config file.
	 * @param type
	 *            Object type. Read from the xml config file.
	 * @return Appropiate Java type
	 */
	protected static Object convert(ResultSet res, String column, String type) {
		try {
			if (type.equals("BigDecimal"))
				return res.getBigDecimal(column);
			else if (type.equals("Boolean"))
				return new Boolean(res.getBoolean(column));
			else if (type.equals("Byte"))
				return new Byte(res.getByte(column));
			else if (type.equals("Date"))
				return res.getDate(column);
			else if (type.equals("Double"))
				return new Double(res.getDouble(column));
			else if (type.equals("Float"))
				return new Float(res.getFloat(column));
			else if (type.equals("Integer"))
				return new Integer(res.getInt(column));
			else if (type.equals("Long"))
				return new Long(res.getLong(column));
			else if (type.equals("Object"))
				return res.getObject(column);
			else if (type.equals("Short"))
				return new Short(res.getShort(column));
			else if (type.equals("String"))
				return res.getString(column);
			else if (type.equals("URL"))
				return res.getURL(column);
			else {
				String content = res.getObject(column).toString();
				TypeAdaptor adaptor = (TypeAdaptor) DataTypesRegistry.getInstance().getDataType(type).getInstance();
				adaptor.fromString(content);
				return adaptor;
			}
		} catch (Exception e) {
			CBRLogger.log("JDBCTypeConverter", CBRLogger.ERROR,
					"Error converting types: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Transforms java objects to be stored in the database. This step is
	 * required because of SQL syntaxis rules. For example, Strings must be
	 * stored with quotes.
	 * 
	 * @param o
	 *            Java object to transform
	 * @param type
	 *            Object type. Read from the xml configuration file
	 * @return Appropiate SQL object.
	 */
	protected static Object java2sql(Object o, String type) {
		try {
			if (type.equals("Boolean")) {
				boolean b = ((Boolean) o).booleanValue();
				if (b)
					return new Integer(1);
				else
					return new Integer(0);
			} else if (type.equals("URL"))
				return ((java.net.URL) o).toString();
			else if (o instanceof TypeAdaptor) {
				TypeAdaptor adaptor = (TypeAdaptor) Class.forName(type)
						.newInstance();
				return adaptor.toString();
			} else
				return o;
		} catch (Exception e) {
			CBRLogger.log("JDBCTypeConverter", CBRLogger.ERROR,
					"Error converting types [JDBCTypeConverter.convert()]: "
							+ e.getMessage());
		}

		return null;
	}

}