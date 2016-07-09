package jcolibri.tools.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jcolibri.util.CBRLogger;

/**
 * Necesary data to establish a connection to a SQL database.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class SQLConnectionData {
	/** JDBC driver. */
	private String driver;

	/** JDBC subprotocol. */
	private String subprotocol;

	/** Database host. */
	private String host;

	/** Database port. */
	private int port;

	/** Database name. */
	private String database;

	/** Logging name. */
	private String user;

	/** Logging password. */
	private String password;

	/**
	 * Constructor.
	 * 
	 * @param driver
	 *            JDBC driver.
	 * @param subprotocol
	 *            JDBC subprotocol.
	 * @param host
	 *            database host.
	 * @param port
	 *            database port.
	 * @param database
	 *            database name.
	 * @param user
	 *            logging user.
	 * @param password
	 *            logging password.
	 */
	public SQLConnectionData(String driver, String subprotocol, String host,
			int port, String database, String user, String password) {
		this.database = database;
		this.driver = driver;
		this.host = host;
		this.password = password;
		this.port = port;
		this.subprotocol = subprotocol;
		this.user = user;
	}

	/**
	 * Constructor.
	 */
	public SQLConnectionData() {

	}

	/**
	 * Returns the database name.
	 * 
	 * @return tha database name.
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Sets the database name.
	 * 
	 * @param database
	 *            database name.
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Returns the JDBC driver.
	 * 
	 * @return the JDBC driver.
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * Sets the JDBC driver.
	 * 
	 * @param driver
	 *            JDBC driver.
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * Returns the database host.
	 * 
	 * @return the database host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the database host.
	 * 
	 * @param host
	 *            the database host.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns the logginf password.
	 * 
	 * @return the logging password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the logging password.
	 * 
	 * @param password
	 *            the logging password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the database port.
	 * 
	 * @return the database port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the database port.
	 * 
	 * @param port
	 *            database port.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Returns the JDBC subprotocol.
	 * 
	 * @return JDBC subprotocol.
	 */
	public String getSubprotocol() {
		return subprotocol;
	}

	/**
	 * Sets the JDBC subprotocol.
	 * 
	 * @param subprotocol
	 *            JDBC subprotocol.
	 */
	public void setSubprotocol(String subprotocol) {
		this.subprotocol = subprotocol;
	}

	/**
	 * Returns the logging user.
	 * 
	 * @return logging user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the logging user.
	 * 
	 * @param user
	 *            logging user.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Creates a new connection to the database using the actual connection
	 * information.
	 * 
	 * @return a new connection to the database.
	 */
	public Connection createConnection() {
		Connection connection = null;

		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(getURL(), user, password);
		} catch (SQLException e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error connecting to database " + e.getMessage());
		} catch (InstantiationException e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error connecting to database " + e.getMessage());
		} catch (IllegalAccessException e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error connecting to database " + e.getMessage());
		} catch (ClassNotFoundException e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error connecting to database " + e.getMessage());
		}

		return connection;
	}

	/**
	 * Returns the connection URL to the database.
	 * 
	 * @return connection URL to the database.
	 */
	private String getURL() {
		return "jdbc:" + subprotocol + "://" + host + ":" + port + "/"
				+ database;
	}
}
