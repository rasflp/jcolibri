package jcolibri.extensions.DL.method;

import java.util.Properties;

import jcolibri.cbrcase.BasicCBRCaseBase;
import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcase.Connector;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.DL.connectors.JenaConnector;

/**
 * Opens the connection to the CBRCase base and loads all the cases in the
 * context.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class LoadDLCaseBaseMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the Case Structure file path. */
	public static final String CONFIG_FILE = "Case Structure";

	/** CBRCaseBase. */
	private CBRCaseBase caseBase = null;

	/** Connector. */
	private Connector connector = null;

	/**
	 * Constructor.
	 */
	public LoadDLCaseBaseMethod() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		try {
			String configFile = (String) this.getParameterValue(CONFIG_FILE);
            Properties props = new Properties();
            props.setProperty(Connector.CONFIG_FILE, configFile);
            connector = JenaConnector.getJenaConnector();
			connector.init(props);
            
            caseBase = new BasicCBRCaseBase();
			caseBase.setConnector(connector);
			caseBase.loadAll();
			context.setCBRCaseBase(caseBase);

			return context;

		} catch (Exception e) {
			throw new jcolibri.cbrcore.exception.ExecutionException(
					"Error executing LoadDLCaseBase: " + e.getMessage());
		}
	}

}