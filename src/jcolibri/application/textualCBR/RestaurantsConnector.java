package jcolibri.application.textualCBR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseRecord;
import jcolibri.cbrcase.Connector;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * <p>
 * Description: This is an example connector used by "textExample.java".
 * <p>
 * It reads restaurants data from a text file that is indicated in
 * "FILE_PARAMETER" method paramether.
 * <p>
 * Important: This method depends on textExample.java, so cannot be used in
 * other applications. This is only an example
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class RestaurantsConnector implements Connector {
	public RestaurantsConnector() {
	}

	java.util.LinkedList<CBRCase> caselist;

	String textAttribute;

	public final static String FILE_PARAMETER = "File";

	public void init(Properties props) throws InitializingException {
		caselist = new java.util.LinkedList<CBRCase>();

		String filename = (String) props.getProperty(FILE_PARAMETER);
		if (filename == null)
			throw new InitializingException(
					"Error initializing connector. Parameter: "
							+ FILE_PARAMETER + " not found.");

		Document doc = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = db.parse((String) props.getProperty(Connector.CONFIG_FILE));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Node caseStructureNode = doc.getElementsByTagName("CaseStructure")
				.item(0);
		String caseStructureFile = caseStructureNode.getFirstChild()
				.getNodeValue();

		CaseStructure caseStructure = new CaseStructure();
		caseStructure.readFromXMLFile(caseStructureFile);

		File f = new File(filename);
		try {
			if (!f.canRead())
				return;
			BufferedReader br = new BufferedReader(new FileReader(f));
			String _abstract = "";
			@SuppressWarnings("unused") String _phone = "";
			String _title = "";
			String _address = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				_title = line;
				_address = br.readLine();
				_phone = br.readLine();
				_abstract = br.readLine();
				do {
					line = br.readLine();
				} while (!line.startsWith("-"));
				CBRCaseRecord _case = createCase(caseStructure, _title,
						_abstract, _address);
				caselist.add(_case);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * private CBRCaseRecord createCase( CaseStructure caseStructure, String
	 * _title, String _address, String _phone, String _abstract) {
	 * 
	 * jcolibri.extensions.textual.representation.Text _text = new
	 * jcolibri.extensions.textual.representation.Text("Description."+textAttribute,
	 * _title); _text.addParagraph(new Paragraph(_abstract));
	 * 
	 * 
	 * CBRCaseRecord _case = new jcolibri.cbrcase.CBRCaseRecord(_title);
	 * _case.addAttribute(CaseStComponent.RelationPrefix+"Description."+textAttribute,
	 * _text, 1.0, "Description."+textAttribute); return _case; }
	 */

	private CBRCaseRecord createCase(CaseStructure caseStructure,
			String _title, String _abstract, String _address) {
		CBRCaseRecord _case = new CBRCaseRecord(_title);
		CaseStDescription description = caseStructure.getDescription();
		Individual descriptionInd = _case.getDescription();
		descriptionInd.registerSimilarity(description.getGlobalSim()
				.getSimilFunction());
		// query.addRelation(new
		// IndividualRelation(description.getRelationPath(), descriptionInd));

		for (int i = 0; i < description.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) description.getChild(i);
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
				addSimpleAttribute(sa, descriptionInd, _title, _abstract,
						_address);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				addCompoundAttribute(sa, descriptionInd, _title, _abstract,
						_address);
			}
		}

		return _case;

	}

	private void addSimpleAttribute(CaseStSimpleAtt simpleAtt, Individual ind,
			String _title, String _abstract, String _address) {
		String individualName = simpleAtt.getPathWithoutCase();
		String relationName = simpleAtt.getRelationPath();
		Object value = null;
		if (simpleAtt.getType().equals("Text")) {
			Text text = new Text(individualName, _title);
			text.addParagraph(new Paragraph(_address));
			text.addParagraph(new Paragraph(_abstract));
			value = text;
		}
		SimpleIndividual target = new SimpleIndividual(value);
		ind.addRelation(new IndividualRelation(relationName, target));
		target.registerSimilarity(simpleAtt.getLocalSim().getSimilFunction());
		target.setParents(new Individual[] { ind });
	}

	private void addCompoundAttribute(CaseStCompoundAtt compAtt,
			Individual ind, String _title, String _abstract, String _address) {
		String individualName = compAtt.getPathWithoutCase();
		String relationName = compAtt.getRelationPath();
		SimpleIndividual target = new SimpleIndividual(individualName);
		ind.addRelation(new IndividualRelation(relationName, target));
		target.setParents(new Individual[] { ind });

		target.registerSimilarity(compAtt.getGlobalSim().getSimilFunction());

		for (int i = 0; i < compAtt.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) compAtt.getChild(i);
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
				addSimpleAttribute(sa, target, _title, _abstract, _address);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				addCompoundAttribute(sa, target, _title, _abstract, _address);
			}
		}

	}

	public void close() {
	}

	public void storeCases(Collection cases) {
	}

	public void deleteCases(Collection cases) {
	}

	/**
	 * Returns max cases without any special consideration
	 * 
	 * @param max
	 *            Maximum number of cases that could be returned
	 * @return The list of retrieved cases
	 */
	public Collection<CBRCase> retrieveAllCases() {
		return caselist;
	}

	public Collection retrieveNCases(int max) {
		if (max < caselist.size())
			return caselist.subList(0, max);
		else
			return caselist;
	}

	public Collection retrieveNCasesAprox(CBRCase query, int maxCases,
			int importanceThreshold) {
		return retrieveNCases(maxCases);
	}

	/**
	 * Filters are not implemented so this method returns all cases.
	 */
	public Collection<CBRCase> retrieveSomeCases(String filter) {
		return retrieveAllCases();
	}

}