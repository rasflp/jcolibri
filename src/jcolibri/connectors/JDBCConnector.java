package jcolibri.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseRecord;
import jcolibri.cbrcase.Connector;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Implements a generic JDBC Connector.
 * </p>
 * It manages the persistence of the cases automatically, so user does not need
 * to understand the implementation. It has several limitations:
 * <ul>
 * <li>Only works with CBRCaseRecord.
 * <li>By default only can manage a few data types, although developers can add
 * their own ones. Supported types and the type extension mechanism is explained
 * in JDBCTypeConverter.
 * <li>Only works with one table.
 * <li>In the database table, the first column must be the case name and it has
 * to be the primary key. Also, its type must be mapped to a Java String type
 * (CHAR(n), VARCHAR(n), ...).
 * </ul>
 * <p>
 * This connector uses the property in the init() parameter to obtain the
 * configuration file. This file is a xml that follows the Schema defined in
 * connector.xsd.
 * <p>
 * This class does not implement any cache mechanims, so cases are read and
 * written directly.
 * <p>
 * You must add the JDBC library in the CLASSPATH to run this class.
 * <p>
 * The main() method tests this class using the PlayTennis CaseBase.
 * <p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see jcolibri.connectors.JDBCTypeConverter
 */

public class JDBCConnector implements Connector {
	/** JDBC driver. */
	protected String PROP_JDBC_DRIVER = "";

	/** JDBC subprotocol. */
	protected String PROP_JDBC_SUBPROTOCOL = "";

	/** Port of the database server. */
	protected String PROP_PORT = "";

	/** Host of the database server. */
	protected String PROP_HOST = "";

	/** Name of the database. */
	protected String PROP_DATABASE = "";

	/** name of the table. */
	protected String PROP_TABLE = "";

	/** User for loggin. */
	protected String PROP_USER = "";

	/** Password ofr logging. */
	protected String PROP_PASSWORD = "";

	/** Name of the case id column. */
	protected String PROP_CASE_ID_COLUMN = "";

	/** Mappings between the columns of the table and the attributes of the case. */
	protected Mapping PROP_MAPPING = null;

	/** Database connection private attribute */
	private Connection connection = null;

	/** Database connection private attribute */
	private Statement stmt = null;

	/** Database connection private attribute */
	private ResultSet rs = null;

	/** Case Structure file */
	private String CaseStructureXMLfile = null;

	/**
	 * Empty constructor.
	 */
	public JDBCConnector() {
	}

	/**
	 * Initializes the connector using the xml configuration file indicated in
	 * in the properties parameter with key LoadCaseBase.CONFIG_FILE.
	 * 
	 * @param props
	 *            Properties parameter.
	 * @throws InitializingException
	 */
	public void init(Properties props) throws InitializingException {
		String configFile = props.getProperty(CONFIG_FILE);
		parseJDBCProperties(configFile);
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName(PROP_JDBC_DRIVER).newInstance();

			String url = "jdbc:" + PROP_JDBC_SUBPROTOCOL + "://" + PROP_HOST
					+ ":" + PROP_PORT + "/" + PROP_DATABASE;

			connection = DriverManager.getConnection(url, PROP_USER,
					PROP_PASSWORD);
		} catch (SQLException e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error connecting to Data Base: " + e.getMessage() + '\r'
							+ "SQLState: " + e.getSQLState());
		} catch (Exception ex) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error loading JDBC driver:" + ex.getMessage());
		}
	}

	/**
	 * Parses the xml file with the configuration.
	 * 
	 * @param config
	 */
	protected void parseJDBCProperties(String config) {
		this.PROP_MAPPING = new Mapping();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(config);

			Node caseStructureNode = doc.getElementsByTagName("CaseStructure")
					.item(0);
			this.CaseStructureXMLfile = caseStructureNode.getFirstChild()
					.getNodeValue();

			Node mainNode = doc.getElementsByTagName("JDBC").item(0);
			NodeList props = mainNode.getChildNodes();
			Node mappings = null;
			for (int n = 0; n < props.getLength(); n++) {
				Node _node = props.item(n);
				if (_node.getNodeType() == Node.TEXT_NODE)
					continue;
				if (_node.getNodeName().equals("Driver"))
					this.PROP_JDBC_DRIVER = _node.getFirstChild()
							.getNodeValue();
				else if (_node.getNodeName().equals("SubProtocol"))
					this.PROP_JDBC_SUBPROTOCOL = _node.getFirstChild()
							.getNodeValue();
				else if (_node.getNodeName().equals("Host"))
					this.PROP_HOST = _node.getFirstChild().getNodeValue();
				else if (_node.getNodeName().equals("Port"))
					this.PROP_PORT = _node.getFirstChild().getNodeValue();
				else if (_node.getNodeName().equals("DataBase"))
					this.PROP_DATABASE = _node.getFirstChild().getNodeValue();
				else if (_node.getNodeName().equals("Table"))
					this.PROP_TABLE = _node.getFirstChild().getNodeValue();
				else if (_node.getNodeName().equals("User"))
					try {
						this.PROP_USER = _node.getFirstChild().getNodeValue();
					} catch (Exception e) {
					}
				else if (_node.getNodeName().equals("PassWord"))
					try {
						this.PROP_PASSWORD = _node.getFirstChild()
								.getNodeValue();
					} catch (Exception e) {
					}

				else if (_node.getNodeName().equals("Mappings"))
					mappings = props.item(n);
			}

			NodeList _mapps = mappings.getChildNodes();
			ArrayList<String> columns = new ArrayList<String>();
			for (int n = 0; n < _mapps.getLength(); n++) {
				Node _node = _mapps.item(n);
				if (_node.getNodeType() == Node.TEXT_NODE)
					continue;
				if (_node.getNodeName().equals("CaseIdColumn")) {
					this.PROP_CASE_ID_COLUMN = _node.getFirstChild()
							.getNodeValue();
				} else if (_node.getNodeName().equals("Map")) {
					String column = "";
					String individual = "";
					String relation = "";
					double weight = 1.0;
					String cType = "";
					NodeList _mapValue = _node.getChildNodes();
					for (int nm = 0; nm < _mapValue.getLength(); nm++) {
						Node _map = _mapValue.item(nm);
						if (_map.getNodeType() == Node.TEXT_NODE)
							continue;
						if (_map.getNodeName().equals("ColumnName"))
							column = _map.getFirstChild().getNodeValue();
						else if (_map.getNodeName().equals("Individual"))
							individual = _map.getFirstChild().getNodeValue();
						else if (_map.getNodeName().equals("Relation"))
							relation = _map.getFirstChild().getNodeValue();
						else if (_map.getNodeName().equals("Weight"))
							weight = Double.parseDouble(_map.getFirstChild()
									.getNodeValue());
						else if (_map.getNodeName().equals("ColumnType"))
							cType = _map.getFirstChild().getNodeValue();
					}
					this.PROP_MAPPING.map(column, individual, relation, weight,
							cType);
					columns.add(column);
				}
			}
			this.PROP_MAPPING.setColumns(columns);
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error parsing config file: " + config + '\r'
							+ e.getMessage());
		}
	}

	/**
	 * Closes the connection with the data base and relases all resources
	 */
	public void close() {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
			connection.close();
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error closing connection: " + e.getMessage());
		}
	}

	/**
	 * Stores the cases in the data base. Note that this method does not control
	 * that the case name (== primary key) is repeated, so developers must be
	 * careful with this.
	 * 
	 * @param cases
	 *            Cases to store.
	 */
	public void storeCases(Collection cases) {
		for (Iterator iter = cases.iterator(); iter.hasNext();) {
			try {
				CBRCaseRecord _case = (CBRCaseRecord) iter.next();
				String SQLquery = "INSERT INTO " + this.PROP_TABLE + " VALUES(";
				SQLquery += "\"" + _case.getValue() + "\", ";
				for (ListIterator liter = this.PROP_MAPPING.getColumns()
						.listIterator(); liter.hasNext();) {
					String column = (String) liter.next();
					String relation = this.PROP_MAPPING.getRelation(column);
					Object value = _case.getAttributeValue(relation);
					value = JDBCTypeConverter.java2sql(value, this.PROP_MAPPING
							.getType(column));
					if ((value instanceof String)
							|| (value instanceof jcolibri.datatypes.StringEnum))
						SQLquery += "\"" + value + "\"";
					else
						SQLquery += value;

					if (liter.hasNext())
						SQLquery += ", ";
				}
				SQLquery += ");";

				SQLsentence(SQLquery);
			} catch (Exception e) {
				CBRLogger.log(this.getClass(), CBRLogger.ERROR,
						"Error storing cases: " + e.getMessage());
			}

		}
	}

	/**
	 * Deletes cases from the case base. It only uses the case name (primary
	 * key) to remove the row. It executes the SQL sentence: DELETE FROM table
	 * WHERE caseIdColumn = case.primaryKey;
	 * 
	 * @param cases
	 *            Cases to delete
	 */
	public void deleteCases(Collection cases) {
		for (Iterator iter = cases.iterator(); iter.hasNext();) {
			try {
				CBRCaseRecord _case = (CBRCaseRecord) iter.next();
				Object primaryKey = _case.getValue();
				String SQLquery = "DELETE FROM " + this.PROP_TABLE + " WHERE "
						+ this.PROP_CASE_ID_COLUMN + " = \"" + primaryKey
						+ "\";";
				SQLsentence(SQLquery);
			} catch (Exception e) {
				CBRLogger.log(this.getClass(), CBRLogger.ERROR,
						"Error deleting cases: " + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves all cases from the case base. It maps data types using the
	 * JDBCTypeConverter class.
	 * 
	 * @return Retrieved cases.
	 */
	public Collection<CBRCase> retrieveAllCases() {
		java.util.Vector<CBRCase> cases = new java.util.Vector<CBRCase>();
		jcolibri.cbrcase.CBRCaseRecord _case = null;
		try {
			ResultSet res = SQLsentence("SELECT * FROM " + this.PROP_TABLE
					+ " ;");
			if (!res.first())
				throw new SQLException("Data Base is empty");

			res.beforeFirst();
			while (!res.isLast()) {
				res.next();

				String caseId = res.getString(this.PROP_CASE_ID_COLUMN);
				_case = CaseCreatorUtils.createCase(this.CaseStructureXMLfile);// new
				// jcolibri.cbrcase.CBRCaseRecord(caseId);
				CaseCreatorUtils.setCaseName(_case, caseId);
				Iterator columns = this.PROP_MAPPING.getColumns()
						.listIterator();
				while (columns.hasNext()) {
					String column = (String) columns.next();

					String relation = this.PROP_MAPPING.getRelation(column);
					String attribute = this.PROP_MAPPING.getAttribute(column);
					double weight = this.PROP_MAPPING.getWeight(column);
					String type = this.PROP_MAPPING.getType(column);
					Object value = JDBCTypeConverter.convert(res, column, type);
					if(value instanceof ConceptType)
                    {
					    ConceptType ct = (ConceptType)value;
                        String att = null;
                        StringTokenizer st = new StringTokenizer(attribute, ".");
                        while(st.hasMoreTokens())
                            att = st.nextToken();
                        
                        ct.setConcept(att);
                    }
                    
					_case.addAttribute(relation, value, weight, attribute);
				}
				cases.add(_case);
			}
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error retrieving cases: " + e.getMessage());
			e.printStackTrace();
		}

		return cases;
	}

	/** TODO */
	public Collection<CBRCase> retrieveSomeCases(String filter) {
		/** @todo Implementar este método jcolibri.cbrcase.Connector */
		throw new java.lang.UnsupportedOperationException(
				"RetrieveSomeCases() is not implemented");
	}

	/**
	 * Utility method that executes a SQL sentences
	 * 
	 * @param sentence
	 *            SQL sentence
	 * @return Database answer if suitable.
	 */
	protected ResultSet SQLsentence(String sentence) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}

			stmt = connection.createStatement();

			if (stmt.execute(sentence))
				rs = stmt.getResultSet();
		} catch (SQLException e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error running SQL sentence: " + sentence + ". Message: "
							+ e.getMessage() + ". SQL sentence: " + sentence
							+ ". SQLState: " + e.getSQLState());
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sqlEx) { // ignore }
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sqlEx) { // ignore }
					stmt = null;
				}
			}
		}
		return rs;
	}

	/**
	 * Test method. It runs the connector with the PlayTennis case base. The
	 * casebase must be stored in a database, the JDBC driver in the CLASSPATH,
	 * and the connector configuration file must be PTConnectorJDBC.xml
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JDBCConnector Connector1 = new JDBCConnector();
		Properties props = new Properties();
		props.put(JDBCConnector.CONFIG_FILE, "PTconnectorJDBC.xml");
		try {
			Connector1.init(props);
			Collection cases = Connector1.retrieveAllCases();
			CBRCaseRecord acase = (CBRCaseRecord) cases.iterator().next();
			acase.setValue("case6");
			ArrayList<CBRCase> list = new ArrayList<CBRCase>();
			list.add(acase);
			Connector1.storeCases(list);
			Connector1.deleteCases(list);
		} catch (Exception e) {
		}
	}
}