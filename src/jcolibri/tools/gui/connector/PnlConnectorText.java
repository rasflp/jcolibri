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
import jcolibri.tools.data.CaseStResult;
import jcolibri.tools.data.CaseStSimpleAtt;
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
 * Panel to manage the plain text connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlConnectorText extends PnlConnectorType {

	private static final long serialVersionUID = 1L;

	private JLabel lblFilePath, lblDelimiter;

	private JTextField tfFilePath, tfDelimiter;

	private PnlTextMappings pnlMappings;

	private CaseStructure caseSt;

	private String caseStFile;

	/**
	 * Constructor.
	 */
	public PnlConnectorText() {
		super();
		createComponents();
	}

	/**
	 * Returns the case structure.
	 * 
	 * @return
	 */
	public CaseStructure getCaseSt() {
		return caseSt;
	}

	/**
	 * Creates the visual components.
	 * 
	 */
	private void createComponents() {
		JPanel pnlProps;

		pnlProps = createPnlProps();
		pnlMappings = new PnlTextMappings(this);

		setLayout(new BorderLayout());
		add(pnlProps, BorderLayout.PAGE_START);
		add(pnlMappings, BorderLayout.CENTER);
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
		lblFilePath = new JLabel("File path:");
		tfFilePath = new JTextField(10);
		lblDelimiter = new JLabel("Delimiter:");
		tfDelimiter = new JTextField(10);

		// Column 1 of components.
		pnlAux1 = new JPanel(new SpringLayout());
		pnlAux1.add(lblFilePath);
		pnlAux1.add(tfFilePath);
		SpringUtilities.makeCompactGrid(pnlAux1, 1, 2, 5, 5, 5, 5);

		// Column 2 of components.
		pnlAux2 = new JPanel(new SpringLayout());
		pnlAux2.add(lblDelimiter);
		pnlAux2.add(tfDelimiter);
		SpringUtilities.makeCompactGrid(pnlAux2, 1, 2, 5, 5, 5, 5);

		pnlRes.add(pnlAux1);
		pnlRes.add(pnlAux2);
		return pnlRes;
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
		// y en el panel superior se completen los datos comunes -> CaseBase */
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
	 * Create a DOM document that represents the connector.
	 * 
	 * @param caseBase
	 *            java class name used for the CBRCaseBase.
	 * @return DOM document.
	 */
	private Document createDOMTree(String caseBase) {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Element elemConnector, elemPlainText, elemMappings, elemMap, elemAux;
		Vector mappings;
		float weight;
		String type;
		CaseStComponent caseComp;
		String caseCompName;

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

			// <Connector><PlainText>.
			elemPlainText = document.createElement("PlainText");
			elemConnector.appendChild(elemPlainText);

			// <Connector><PlainText><FilePath>.
			elemAux = document.createElement("FilePath");
			elemAux.setTextContent(tfFilePath.getText());
			elemPlainText.appendChild(elemAux);

			// <Connector><PlainText><Delimiters>.
			elemAux = document.createElement("Delimiters");
			elemAux.setTextContent(tfDelimiter.getText());
			elemPlainText.appendChild(elemAux);

			// <Connector><PlainText><Mappings>.
			elemMappings = document.createElement("Mappings");
			elemPlainText.appendChild(elemMappings);

			// <Connector><PlainText><Mappings><CaseIdColumn>.
			elemAux = document.createElement("CaseIdColumn");
			elemAux.setTextContent("caseId");
			elemMappings.appendChild(elemAux);

			mappings = pnlMappings.getConnMappings();

			// <Connector><PlainText><Mappings><Map>.
			for (int i = 0; i < mappings.size(); i++) {
				elemMap = document.createElement("Map");
				elemMappings.appendChild(elemMap);

				caseCompName = (String) mappings.elementAt(i);
				caseCompName = caseSt.getName()
						+ CaseStComponent.getPathSeparator() + caseCompName;
				caseComp = (CaseStComponent) caseSt
						.getComponentByPath(caseCompName);
				if (caseComp instanceof CaseStAttribute)
					weight = ((CaseStAttribute) caseComp).getWeight();
				else
					weight = 0;

				if (caseComp instanceof CaseStSimpleAtt) {
					type = ((CaseStSimpleAtt) caseComp).getType();
				} else if (caseComp instanceof CaseStResult) {
					type = "Boolean";
				} else {
					type = "";
				}

				// <Connector><PlainText><Mappings><Map><ColumnName>.
				elemAux = document.createElement("ColumnName");
				elemAux.setTextContent(caseComp.getPathWithoutCase());
				elemMap.appendChild(elemAux);

				// <Connector><PlainText><Mappings><Map><Relation>.
				elemAux = document.createElement("Relation");
				elemAux.setTextContent(caseComp.getRelationPath());
				elemMap.appendChild(elemAux);

				// <Connector><PlainText><Mappings><Map><Weight>.
				elemAux = document.createElement("Weight");
				elemAux.setTextContent(Float.valueOf(weight).toString());
				elemMap.appendChild(elemAux);

				// <Connector><PlainText><Mappings><Map><Individual>.
				elemAux = document.createElement("Individual");
				elemAux.setTextContent(caseComp.getPathWithoutCase());
				elemMap.appendChild(elemAux);

				// <Connector><PlainText><Mappings><Map><ColumnType>.
				elemAux = document.createElement("ColumnType");
				elemAux.setTextContent(type);
				elemMap.appendChild(elemAux);
			}

			return document;

		} catch (ParserConfigurationException pce) {

			// Parser with specified options can't be built
			CBRLogger.log("jcolibri.tools.PnlConnectorText", "createDOMTree",
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
		NodeList individuals;
		String individual;

		tfFilePath.setText(elem.getElementsByTagName("FilePath").item(0)
				.getTextContent());
		tfDelimiter.setText(elem.getElementsByTagName("Delimiters").item(0)
				.getTextContent());

		individuals = elem.getElementsByTagName("Individual");

		pnlMappings.clearTable();
		for (int i = 0; i < individuals.getLength(); i++) {
			individual = individuals.item(i).getTextContent();
			individual = individual.replace('_', '.');
			pnlMappings.addMapping(individual);
		}
	}
}
