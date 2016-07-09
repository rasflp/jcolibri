package jcolibri.method;

import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcase.Connector;
import jcolibri.cbrcore.CBRContext;
import jcolibri.util.CBRLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Opens the connection to the CBRCase base and loads all the cases in the
 * context.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class LoadCaseBaseMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Name of the parameter having the connector file path. */
	public static final String CONFIG_FILE = "Connector";

	/** CBRCaseBase. */
	private CBRCaseBase caseBase = null;

	/** Connector. */
	private Connector connector = null;

	/**
	 * Constructor.
	 */
	public LoadCaseBaseMethod() {
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
			Properties props = chooseConnector(configFile);
			connector.init(props);
			caseBase.setConnector(connector);
			caseBase.loadAll();
			context.setCBRCaseBase(caseBase);
			// ArrayList list = new ArrayList(caseBase.getAll());
			// context.setCases(list);
			this.caseBase = null;
			this.connector = null;
			return context;

		} catch (Exception e) {
			throw new jcolibri.cbrcore.exception.ExecutionException(
					"Error executing LoadCaseBase: " + e.getMessage());
		}
	}

	/**
	 * Parses the connector file and returns the necesary properties to
	 * initialize the connector.
	 * 
	 * @param config
	 *            connector file path.
	 * @return the necesary properties.
	 */
	private Properties chooseConnector(String config) {
		Properties props = new Properties();
		props.setProperty(Connector.CONFIG_FILE, config);

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = db.parse(config);

			Node caseBaseNode = doc.getElementsByTagName("CaseBase").item(0);
			String caseBaseName = caseBaseNode.getFirstChild().getNodeValue();

			Class caseBaseClass = Class.forName(caseBaseName);
			this.caseBase = (CBRCaseBase) caseBaseClass.newInstance();

			if (doc.getElementsByTagName("OTHER").getLength() != 0) {
				CBRLogger.log(this.getClass(), CBRLogger.INFO,
						"Using External Connector");
				String connectorClassName = doc.getElementsByTagName(
						"ConnectorClass").item(0).getFirstChild()
						.getNodeValue();
				this.connector = (Connector) Class.forName(connectorClassName)
						.newInstance();

				NodeList nl = doc.getElementsByTagName("Parameter");
				for (int i = 0; i < nl.getLength(); i++) {
					Node node = nl.item(i);
					String parName = null;
					String parValue = null;
					for (int j = 0; j < node.getChildNodes().getLength(); j++) {
						Node parNode = node.getChildNodes().item(j);
						if (parNode.getNodeName().equals("ParameterName"))
							parName = parNode.getFirstChild().getNodeValue();
						if (parNode.getNodeName().equals("ParameterValue"))
							parValue = parNode.getFirstChild().getNodeValue();
					}
					props.setProperty(parName, parValue);
				}
			} else if (doc.getElementsByTagName("JDBC").getLength() != 0) {
				CBRLogger.log(this.getClass(), CBRLogger.INFO,
						"Using JDBC Connector");
				this.connector = new jcolibri.connectors.JDBCConnector();
			} else if (doc.getElementsByTagName("PlainText").getLength() != 0) {
				CBRLogger.log(this.getClass(), CBRLogger.INFO,
						"Using PlainText Connector");
				this.connector = new jcolibri.connectors.PlainTextConnector();
			} else if(doc.getElementsByTagName("Jena").getLength() != 0){
                CBRLogger.log(this.getClass(), CBRLogger.INFO,
                "Using Jena Connector");
                this.connector = new jcolibri.extensions.DL.connectors.JenaConnector();
            }
	    } catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error parsing config file: " + config + '\r'
							+ e.getMessage());
		}
		return props;
	}

	public static void main(String[] args) {

	}

}