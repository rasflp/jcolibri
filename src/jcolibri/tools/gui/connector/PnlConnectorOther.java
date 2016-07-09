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

import jcolibri.tools.data.CaseStructure;
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
 * Panel to manage connectors of type "Other".
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlConnectorOther extends PnlConnectorType {

	private static final long serialVersionUID = 1L;

	private JLabel lblConnClass;

	private JTextField tfConnClass;

	private PnlOtherParams pnlOtherParams;

	private String caseStFile;

	/**
	 * Constructor.
	 */
	public PnlConnectorOther() {
		super();
		createComponents();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlProps;

		pnlProps = createPnlProps();
		pnlOtherParams = new PnlOtherParams();

		setLayout(new BorderLayout());
		add(pnlProps, BorderLayout.PAGE_START);
		add(pnlOtherParams, BorderLayout.CENTER);
	}

	/**
	 * Creates the panel of properties.
	 * 
	 * @return
	 */
	private JPanel createPnlProps() {
		JPanel pnlRes, pnlAux1;
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
		lblConnClass = new JLabel("Connector class:");
		tfConnClass = new JTextField(10);

		// Column 1 of components.
		pnlAux1 = new JPanel(new SpringLayout());
		pnlAux1.add(lblConnClass);
		pnlAux1.add(tfConnClass);
		SpringUtilities.makeCompactGrid(pnlAux1, 1, 2, 5, 5, 5, 5);

		pnlRes.add(pnlAux1);
		return pnlRes;
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
	 * @see jcolibri.tools.gui.connector.PnlConnectorType#setCaseStructure(jcolibri.tools.data.CaseStructure,
	 *      java.lang.String)
	 */
	public void setCaseStructure(CaseStructure caseSt, String caseStFile) {
		this.caseStFile = caseStFile;
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
	 * Reads the connector data from a DOM tree.
	 * 
	 * @param node
	 *            DOM node.
	 */
	private void fromXMLDOM(Node node) {
		Element elem = (Element) node;
		NodeList paramNames, paramValues;
		String name, value;

		tfConnClass.setText(elem.getElementsByTagName("ConnectorClass").item(0)
				.getTextContent());

		paramNames = elem.getElementsByTagName("ParameterName");
		paramValues = elem.getElementsByTagName("ParameterValue");
		for (int i = 0; i < paramNames.getLength(); i++) {
			name = paramNames.item(i).getTextContent();
			value = paramValues.item(i).getTextContent();
			pnlOtherParams.addParam(name, value);
		}
	}

	/**
	 * Writes the connector data to a DOM tree.
	 * 
	 * @param caseBase
	 *            java class name used for CBRCaseBase.
	 * @return DOM tree.
	 */
	private Document createDOMTree(String caseBase) {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemConnector, elemOTHER, elemParameter, elemAux;
		Vector params;
		String paramData[];

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

			// <Connector><OTHER>.
			elemOTHER = document.createElement("OTHER");
			elemConnector.appendChild(elemOTHER);

			// <Connector><RACER><ConnectorClass>.
			elemAux = document.createElement("ConnectorClass");
			elemAux.setTextContent(tfConnClass.getText());
			elemOTHER.appendChild(elemAux);

			params = pnlOtherParams.getParams();
			for (int i = 0; i < params.size(); i++) {

				// <Connector><RACER><Parameter>.
				elemParameter = document.createElement("Parameter");
				elemOTHER.appendChild(elemParameter);

				// <Connector><RACER><Parameter><ParameterName>
				paramData = (String[]) params.get(i);
				elemAux = document.createElement("ParameterName");
				elemAux.setTextContent(paramData[0]);
				elemParameter.appendChild(elemAux);

				// <Connector><RACER><Parameter><ParameterValue>
				elemAux = document.createElement("ParameterValue");
				elemAux.setTextContent(paramData[1]);
				elemParameter.appendChild(elemAux);
			}

			return document;

		} catch (ParserConfigurationException pce) {

			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.PnlConnectorOther", "createDOMTree",
					pce);
		}

		return null;
	}
}
