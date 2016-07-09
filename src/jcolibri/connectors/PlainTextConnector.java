package jcolibri.connectors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
 * Implements a generic PlainText Connector.
 * </p>
 * It manages the persistence of the cases automatically, so user does not need
 * to understand the implementation. It has several limitations:
 * <ul>
 * <li>Only works with CBRCaseRecord.
 * <li>By default only can manage a few data types, although developers can add
 * their own ones. Supported types and the type extension mechanism is explained
 * in PlainTextTypeConverter.
 * <li>Only works with one file
 * <li>In the text file, the first column must be the case name and it is used
 * as a primary key.
 * </ul>
 * <p>
 * This connector uses the property in the init() parameter to obtain the
 * configuration file. This file is a xml that follows the Schema defined in
 * connector.xsd.
 * <p>
 * This class does not implement any cache mechanims, so cases are read and
 * written directly. This can be very inefficient in some operations (mainly in
 * reading)
 * <p>
 * The main() method tests this class using the PlayTennis CaseBase.
 * <p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see jcolibri.connectors.PlainTextTypeConverter
 */
public class PlainTextConnector implements Connector {

	/** Text file path. */
	protected String PROP_FILEPATH = "";

	/** Columns separator. */
	protected String PROP_DELIM = "";

	/** Mapping between the columns of the file and the attributes of the case. */
	protected Mapping PROP_MAPPING = null;

	/** Name of the case id column. */
	protected String PROP_CASE_ID_COLUMN = "";
    
    protected String CaseStructureXMLfile;

	/**
	 * Constructor.
	 */
	public PlainTextConnector() {
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
		parsePlainTextProperties(configFile);
	}

	/**
	 * Parses the xml file with the configuration.
	 * 
	 * @param config
	 */
	protected void parsePlainTextProperties(String config) {
		this.PROP_MAPPING = new Mapping();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(config);
            
            Node caseStructureNode = doc.getElementsByTagName("CaseStructure").item(0);
            this.CaseStructureXMLfile = caseStructureNode.getFirstChild().getNodeValue();
            
			Node mainNode = doc.getElementsByTagName("PlainText").item(0);
			NodeList props = mainNode.getChildNodes();
			Node mappings = null;
			for (int n = 0; n < props.getLength(); n++) {
				Node _node = props.item(n);
				if (_node.getNodeType() == Node.TEXT_NODE)
					continue;
				if (_node.getNodeName().equals("FilePath"))
					this.PROP_FILEPATH = _node.getFirstChild().getNodeValue();
				else if (_node.getNodeName().equals("Delimiters"))
					this.PROP_DELIM = _node.getFirstChild().getNodeValue();
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
		try {
			jcolibri.cbrcase.CBRCaseRecord _case;

			BufferedWriter br = null;
			br = new BufferedWriter(new FileWriter(this.PROP_FILEPATH, true));
			if (br == null)
				throw new Exception("Error opening file for writing: "
						+ this.PROP_FILEPATH);

			for (Iterator iter = cases.iterator(); iter.hasNext();) {
				_case = (CBRCaseRecord) iter.next();

				String line = _case.getValue() + this.PROP_DELIM;
				Iterator cIter = this.PROP_MAPPING.getColumns().iterator();
				while (cIter.hasNext()) {
					String column = (String) cIter.next();
					String relation = this.PROP_MAPPING.getRelation(column);
					Object value = _case.getAttributeValue(relation);
					line += value + this.PROP_DELIM;
				}

				br.write(line);
				br.newLine();
			}
			br.close();
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error storing cases " + e.getMessage());
		}
	}

	/**
	 * Deletes cases from the case base. It only uses the case name (primary
	 * key) to remove the row. Note that this method is very inefficient because
	 * it reads all the database, removes the rows in memory, and writes it
	 * again into the text file.
	 * 
	 * @param cases
	 *            Cases to delete
	 */
	public void deleteCases(Collection cases) {
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(this.PROP_FILEPATH));
			if (br == null)
				throw new Exception("Error opening file for reading: "
						+ this.PROP_FILEPATH);

			ArrayList<String> lines = new ArrayList<String>();
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || (line.length() == 0)) {
					lines.add(line);
					continue;
				}

				StringTokenizer st = new StringTokenizer(line, this.PROP_DELIM);
				String caseId = st.nextToken();
				for (Iterator cIter = cases.iterator(); cIter.hasNext();) {
					CBRCaseRecord _case = (CBRCaseRecord) cIter.next();
					if (!caseId.equals((String) _case.getValue()))
						lines.add(line);
				}
			}
			br.close();

			BufferedWriter bw = null;
			bw = new BufferedWriter(new FileWriter(this.PROP_FILEPATH, false));
			if (bw == null)
				throw new Exception("Error opening file for writing: "
						+ this.PROP_FILEPATH);
			for (ListIterator lIter = lines.listIterator(); lIter.hasNext();) {
				line = (String) lIter.next();
				bw.write(line);
				bw.newLine();
			}
			bw.close();

		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error deleting cases " + e.getMessage());
		}
	}

	/**
	 * Retrieves all cases from the text file. It maps data types using the
	 * PlainTextTypeConverter class.
	 * 
	 * @return Retrieved cases.
	 */
	public Collection<CBRCase> retrieveAllCases() {
		LinkedList<CBRCase> cases = new LinkedList<CBRCase>();
		try {
			jcolibri.cbrcase.CBRCaseRecord _case;

			BufferedReader br = null;
			br = new BufferedReader(new FileReader(this.PROP_FILEPATH));
			if (br == null)
				throw new Exception("Error opening file: " + this.PROP_FILEPATH);

			String line = "";
			while ((line = br.readLine()) != null) {

				if (line.startsWith("#") || (line.length() == 0))
					continue;
				StringTokenizer st = new StringTokenizer(line, this.PROP_DELIM);

				//_case = new jcolibri.cbrcase.CBRCaseRecord(st.nextToken());
                _case = CaseCreatorUtils.createCase(this.CaseStructureXMLfile);// new
                CaseCreatorUtils.setCaseName(_case, st.nextToken());

				Iterator columns = this.PROP_MAPPING.getColumns()
						.listIterator();
				while (columns.hasNext()) {
					String column = (String) columns.next();

					String relation = this.PROP_MAPPING.getRelation(column);
					String attribute = this.PROP_MAPPING.getAttribute(column);
					double weight = this.PROP_MAPPING.getWeight(column);
					String type = this.PROP_MAPPING.getType(column);
					Object value = PlainTextTypeConverter.convert(st
							.nextToken(), type);

                    if(value instanceof ConceptType)
                    {
                        ConceptType ct = (ConceptType)value;
                        String att = null;
                        StringTokenizer stok = new StringTokenizer(attribute, ".");
                        while(stok.hasMoreTokens())
                            att = stok.nextToken();   
                        ct.setConcept(att);
                    }
                    
					_case.addAttribute(relation, value, weight, attribute);
				}

				cases.add(_case);
			}
			br.close();
		} catch (Exception e) {
			CBRLogger.log(this.getClass(), CBRLogger.ERROR,
					"Error retrieving cases " + e.getMessage());
		}
		return cases;
	}

	/** TODO */
	public Collection<CBRCase> retrieveSomeCases(String filter) {
		/** @todo Implementar este método jcolibri.cbrcase.Connector */
		throw new java.lang.UnsupportedOperationException(
				"El método retrieveSomeCases() aún no está implementado.");
	}

	/**
	 * Test method. It runs the connector with the PlayTennis case base. The
	 * casebase must be stored in a text file defined in a configuration file
	 * named PTConnectorPlainText.xml
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PlainTextConnector Connector1 = new PlainTextConnector();
		Properties props = new Properties();
		props.put(PlainTextConnector.CONFIG_FILE, "PTconnectorPlainText.xml");
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