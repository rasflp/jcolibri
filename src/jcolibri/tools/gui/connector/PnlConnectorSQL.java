package jcolibri.tools.gui.connector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;
import jcolibri.tools.data.ConnMapping;
import jcolibri.tools.data.DBColumn;
import jcolibri.tools.data.DBTable;
import jcolibri.tools.data.SQLConnectionData;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.util.CBRLogger;
import jcolibri.util.FileUtil;
import jcolibri.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Panel to manage the SQL connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlConnectorSQL extends PnlConnectorType {

	private static final long serialVersionUID = 1L;

	private JLabel lblDriver, lblSubProtocol, lblHost, lblPort, lblDatabase,
			lblTable, lblUser, lblPassword;

	private JTextField tfDriver, tfSubProtocol, tfHost, tfPort, tfDatabase,
			tfTable, tfUser, tfPassword;

	private PnlSQLDBTable pnlDBTable;

	private PnlSQLMappings pnlMappings;

	private CaseStructure caseSt;

	private String caseStFile;

	/**
	 * Constructor.
	 */
	public PnlConnectorSQL() {
		super();
		createComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.connector.PnlConnectorType#writeConnectorToFile(java.lang.String,
	 *      java.lang.String)
	 */
	public void writeConnectorToFile(String fileName, String caseBase) {
		// TODO mover a la clase connector correspondiete. Aqui solo se debería
		// crear esa clase.
		// Cambiar este método por otro que devuelva el connector
		// correspondiente
		// y en el panel superior se completen los datos comunes -> CaseBase
		Document document;

		// Create DOM tree.
		document = createDOMTree(caseBase);
		if (document == null)
			return;

		// Write XML file.
		FileUtil.createFileBackup(fileName);
		XMLUtil.writeDOMToFile(document, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.connector.PnlConnectorType#readConnectorFromFile(java.lang.String)
	 */
	public void readConnectorFromFile(String fileName) {
		// TODO mover a la clase connector correspondiete. Aqui solo se debería
		// crear esa clase.
		// Cambiar este método por otro que devuelva el connector
		// correspondiente
		// y en el panel superior se visualicen los datos comunes -> CaseBase
		DocumentBuilder db;
		Document doc;

		try {
			// Read and parse the file.
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(fileName);

			// Build the case structure.
			fromXMLDOM(doc.getElementsByTagName("Connector").item(0));

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns the form data necesary to create a SQL connection.
	 * 
	 * @return
	 */
	public SQLConnectionData getSQLConnectionData() {
		SQLConnectionData data;

		data = new SQLConnectionData();
		data.setDriver(tfDriver.getText().trim());
		data.setSubprotocol(tfSubProtocol.getText().trim());
		data.setHost(tfHost.getText().trim());
		data.setPort(Integer.valueOf(tfPort.getText()).intValue());
		data.setDatabase(tfDatabase.getText().trim());
		data.setUser(tfUser.getText().trim());
		data.setPassword(tfPassword.getText().trim());

		return data;
	}

	/**
	 * Returns the table of the database.
	 * 
	 * @return
	 */
	public String getSQLTable() {
		return tfTable.getText();
	}

	/**
	 * Returns the case structure.
	 * 
	 * @return
	 */
	public CaseStructure getCaseStructure() {
		return caseSt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.tools.gui.connector.PnlConnectorType#setCaseStructure(jcolibri.tools.data.CaseStructure,
	 *      java.lang.String)
	 */
	public void setCaseStructure(CaseStructure caseSt, String caseStFile) {
		this.caseSt = caseSt;
		this.caseStFile = caseStFile;
		pnlMappings.updateCaseParams();
	}

	/**
	 * Returns a DOM document that represents the connector.
	 * 
	 * @param caseBase
	 *            java class name used for CBRCaseBase.
	 * @return DOM document.
	 */
	private Document createDOMTree(String caseBase) {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemConnector, elemJDBC, elemMappings, elemMap, elemAux;
		Vector mappings;
		DBTable dbTable;
		DBColumn dbColumn;
		String[] mapping;
		float weight;
		CaseStAttribute att;
		CaseStComponent caseComp;

		try {
			// Create document.
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// <Connector>.
			elemConnector = document.createElement("Connector");
			document.appendChild(elemConnector);

			// <Connector><CaseBase>.
			elemAux = document.createElement("CaseBase");
			elemAux.setTextContent(caseBase);
			elemConnector.appendChild(elemAux);

			// <Connector><CaseStructure>
			elemAux = document.createElement("CaseStructure");
			elemAux.setTextContent(caseStFile);
			elemConnector.appendChild(elemAux);

			// <Connector><Type>.
			elemAux = document.createElement("Type");
			elemAux.setTextContent("JDBC");
			elemConnector.appendChild(elemAux);

			// <Connector><JDBC>.
			elemJDBC = document.createElement("JDBC");
			elemConnector.appendChild(elemJDBC);

			// <Connector><JDBC><Driver>.
			elemAux = document.createElement("Driver");
			elemAux.setTextContent(tfDriver.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><SubProtocol>.
			elemAux = document.createElement("SubProtocol");
			elemAux.setTextContent(tfSubProtocol.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><Host>.
			elemAux = document.createElement("Host");
			elemAux.setTextContent(tfHost.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><Port>.
			elemAux = document.createElement("Port");
			elemAux.setTextContent(tfPort.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><DataBase>.
			elemAux = document.createElement("DataBase");
			elemAux.setTextContent(tfDatabase.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><Table>.
			elemAux = document.createElement("Table");
			elemAux.setTextContent(tfTable.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><User>.
			elemAux = document.createElement("User");
			elemAux.setTextContent(tfUser.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><PassWord>.
			elemAux = document.createElement("PassWord");
			elemAux.setTextContent(tfPassword.getText());
			elemJDBC.appendChild(elemAux);

			// <Connector><JDBC><Mappings>.
			elemMappings = document.createElement("Mappings");
			elemJDBC.appendChild(elemMappings);

			// <Connector><JDBC><Mappings><CaseIdColumn>.
			elemAux = document.createElement("CaseIdColumn");
			elemAux.setTextContent("caseId");
			elemMappings.appendChild(elemAux);

			mappings = pnlMappings.getConnMappings();
			dbTable = pnlDBTable.getDBTable();

			// <Connector><JDBC><Mappings><Map>.
			for (int i = 0; i < mappings.size(); i++) {
				elemMap = document.createElement("Map");
				elemMappings.appendChild(elemMap);

				mapping = (String[]) mappings.elementAt(i);
				dbColumn = dbTable.getByName(mapping[1]);
				caseComp = (CaseStComponent) caseSt.getComponentByPath(caseSt
						.getName()
						+ CaseStComponent.getPathSeparator() + mapping[0]);
				if (caseComp instanceof CaseStAttribute) {
					att = (CaseStAttribute) caseComp;
					weight = att.getWeight();
				} else
					weight = 0;

				// <Connector><JDBC><Mappings><Map><ColumnName>.
				elemAux = document.createElement("ColumnName");
				elemAux.setTextContent(mapping[1]);
				elemMap.appendChild(elemAux);

				// <Connector><JDBC><Mappings><Map><Relation>.
				elemAux = document.createElement("Relation");
				elemAux.setTextContent(caseComp.getRelationPath());
				elemMap.appendChild(elemAux);

				// <Connector><JDBC><Mappings><Map><Weight>.
				elemAux = document.createElement("Weight");
				elemAux.setTextContent(Float.valueOf(weight).toString());
				elemMap.appendChild(elemAux);

				// <Connector><JDBC><Mappings><Map><Individual>.
				elemAux = document.createElement("Individual");
				elemAux.setTextContent(caseComp.getPathWithoutCase());
				elemMap.appendChild(elemAux);

				// <Connector><JDBC><Mappings><Map><ColumnType>.
				elemAux = document.createElement("ColumnType");
				elemAux.setTextContent(dbColumn.getType());
				elemMap.appendChild(elemAux);
			}

			return document;

		} catch (ParserConfigurationException pce) {

			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.PnlConnectorSQL", "createDOMTree",
					pce);
		}

		return null;
	}

	/**
	 * Reads the connector data from a DOM tree.
	 * 
	 * @param node
	 *            DOM node.
	 */
	private void fromXMLDOM(Node node) {
		Element elem = (Element) node;
		NodeList columnNames, individuals, columnTypes;
		String columnName, columnType, individual;

		tfDriver.setText(elem.getElementsByTagName("Driver").item(0)
				.getTextContent());
		tfSubProtocol.setText(elem.getElementsByTagName("SubProtocol").item(0)
				.getTextContent());
		tfHost.setText(elem.getElementsByTagName("Host").item(0)
				.getTextContent());
		tfPort.setText(elem.getElementsByTagName("Port").item(0)
				.getTextContent());
		tfDatabase.setText(elem.getElementsByTagName("DataBase").item(0)
				.getTextContent());
		tfTable.setText(elem.getElementsByTagName("Table").item(0)
				.getTextContent());
		tfUser.setText(elem.getElementsByTagName("User").item(0)
				.getTextContent());
		tfPassword.setText(elem.getElementsByTagName("PassWord").item(0)
				.getTextContent());

		columnNames = elem.getElementsByTagName("ColumnName");
		individuals = elem.getElementsByTagName("Individual");
		columnTypes = elem.getElementsByTagName("ColumnType");

		pnlDBTable.clearDBTable();
		pnlMappings.clearConnMappings();
		for (int i = 0; i < columnNames.getLength(); i++) {
			columnName = columnNames.item(i).getTextContent();
			columnType = columnTypes.item(i).getTextContent();
			pnlDBTable.addDBColumn(new DBColumn(columnName, columnType));

			individual = individuals.item(i).getTextContent();
			individual = individual.replace('_', '.');
			pnlMappings.addConnMapping(new ConnMapping(individual, columnName));
		}
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlProps, pnlAux;

		pnlProps = createPnlProps();
		pnlDBTable = new PnlSQLDBTable(this);
		pnlMappings = new PnlSQLMappings(this, pnlDBTable);
		pnlDBTable.setPnlConnMappings(pnlMappings);

		pnlAux = new JPanel(new GridLayout(1, 2));
		pnlAux.add(pnlDBTable);
		pnlAux.add(pnlMappings);

		setLayout(new BorderLayout());
		add(pnlProps, BorderLayout.PAGE_START);
		add(pnlAux, BorderLayout.CENTER);
	}

	/**
	 * Creates the panel of properties.
	 * 
	 * @return
	 */
	private JPanel createPnlProps() {
		JPanel pnlRes, pnlAux1, pnlAux2;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory
				.createTitledBorder(lineBorder, "Properties");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel();
		pnlRes.setBorder(compoundBorder);
		pnlRes.setLayout(new GridLayout(1, 2, 10, 0));

		// Set components for properties.
		lblDriver = new JLabel("Driver:");
		tfDriver = new JTextField(10);
		tfDriver.setText("com.mysql.jdbc.Driver");
		lblSubProtocol = new JLabel("Subprotocol:");
		tfSubProtocol = new JTextField(10);
		tfSubProtocol.setText("mysql");
		lblHost = new JLabel("Host:");
		tfHost = new JTextField(10);
		tfHost.setText("localhost");
		lblPort = new JLabel("Port:");
		tfPort = new JTextField(10);
		tfPort.setText("3306");
		lblDatabase = new JLabel("Database:");
		tfDatabase = new JTextField(10);
		lblTable = new JLabel("Table:");
		tfTable = new JTextField(10);
		lblUser = new JLabel("User:");
		tfUser = new JTextField(10);
		lblPassword = new JLabel("Password:");
		tfPassword = new JTextField(10);

		// Column 1 of components.
		pnlAux1 = new JPanel(new SpringLayout());
		pnlAux1.add(lblDriver);
		pnlAux1.add(tfDriver);
		pnlAux1.add(lblSubProtocol);
		pnlAux1.add(tfSubProtocol);
		pnlAux1.add(lblHost);
		pnlAux1.add(tfHost);
		pnlAux1.add(lblPort);
		pnlAux1.add(tfPort);
		SpringUtilities.makeCompactGrid(pnlAux1, 4, 2, 5, 5, 5, 5);

		// Column 2 of components.
		pnlAux2 = new JPanel(new SpringLayout());
		pnlAux2.add(lblDatabase);
		pnlAux2.add(tfDatabase);
		pnlAux2.add(lblTable);
		pnlAux2.add(tfTable);
		pnlAux2.add(lblUser);
		pnlAux2.add(tfUser);
		pnlAux2.add(lblPassword);
		pnlAux2.add(tfPassword);
		SpringUtilities.makeCompactGrid(pnlAux2, 4, 2, 5, 5, 5, 5);

		pnlRes.add(pnlAux1);
		pnlRes.add(pnlAux2);
		return pnlRes;
	}

}
