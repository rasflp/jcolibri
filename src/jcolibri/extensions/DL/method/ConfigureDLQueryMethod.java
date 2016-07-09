package jcolibri.extensions.DL.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;

import jcolibri.cbrcase.CBRGenericQuery;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.MethodParameter;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.gui.CBRArmGUI;
import jcolibri.gui.GenericMethodParametersPane;
import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStSimpleAttConcept;
import jcolibri.tools.data.CaseStructure;

/**
 * Reads the structure of the case from a file and build a frame dynamically
 * that can be used to introduce the values of the query.
 * 
 * @author Juan Antonio Recio García
 */
public class ConfigureDLQueryMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Parameter name containing the case structure file path. */
	public static final String CASE_STRUCTURE_FILE = "Case Structure";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {

		String configFile = (String) this
				.getParameterValue(CASE_STRUCTURE_FILE);

		CaseStructure caseStructure = new CaseStructure();
		caseStructure.readFromXMLFile(configFile);
		List requestedAttributes = extractSimpleAttributes(caseStructure
				.getDescription());
		HashMap queryValues = new HashMap();
		GenericMethodParametersPane pane = new GenericMethodParametersPane(
				requestedAttributes, queryValues, true);
		JDialog dialog = pane.createJDialog(CBRArmGUI.getReference(), true,
				"Query");
		dialog.setVisible(true);
		boolean _continue = (pane.getValue() == GenericMethodParametersPane.OK);

		if (!_continue)
			throw new ExecutionException("Query not configured by user. CBRCycle aborted.");

		CBRQuery query = createQuery(caseStructure, queryValues);
		context.setQuery(query);
		return context;
	}

	/**
	 * Retuns a CBRQuery having the same structure of the CBRCase description
	 * and the values of QueryValues.
	 * 
	 * @param caseStructure
	 *            case structure.
	 * @param QueryValues
	 *            values of the new query.
	 * @return CBRQuery.
	 */
	private CBRQuery createQuery(CaseStructure caseStructure,
			HashMap QueryValues) {
		CBRQuery query = new CBRGenericQuery("Query");
		CaseStDescription description = caseStructure.getDescription();
		Individual descriptionInd = query.getDescription();
		descriptionInd.registerSimilarity(description.getGlobalSim()
				.getSimilFunction());
		// query.addRelation(new
		// IndividualRelation(description.getRelationPath(), descriptionInd));

		for (int i = 0; i < description.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) description.getChild(i);
			if (att instanceof CaseStSimpleAttConcept) {
                CaseStSimpleAttConcept sa = (CaseStSimpleAttConcept) att;
				addSimpleAttribute(sa, descriptionInd, QueryValues);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				addCompoundAttribute(sa, descriptionInd, QueryValues);
			}
		}

		return (CBRQuery) query;

	}

	/**
	 * Builds an individual that represents a simple case attribute.
	 * 
	 * @param simpleAtt
	 *            simple case attribute.
	 * @param ind
	 *            individual.
	 * @param queryValues
	 *            value of the new individual.
	 */
	private void addSimpleAttribute(CaseStSimpleAttConcept simpleAtt, Individual ind,
			HashMap queryValues) {
		String relationName = simpleAtt.getRelationPath();
		SimpleIndividual target = new SimpleIndividual(queryValues
				.get(simpleAtt.getName()));
		ind.addRelation(new IndividualRelation(relationName, target));
		target.registerSimilarity(simpleAtt.getLocalSim().getSimilFunction());
		target.setParents(new Individual[] { ind });
	}

	/**
	 * Builds an individual that represents a compound attribute.
	 * 
	 * @param compAtt
	 *            compound case attribute.
	 * @param ind
	 *            individual.
	 * @param queryValues
	 *            values of the new individual.
	 */
	private void addCompoundAttribute(CaseStCompoundAtt compAtt,
			Individual ind, HashMap queryValues) {
		String individualName = compAtt.getPathWithoutCase();
		String relationName = compAtt.getRelationPath();
		SimpleIndividual target = new SimpleIndividual(individualName);
		ind.addRelation(new IndividualRelation(relationName, target));
		target.setParents(new Individual[] { ind });

		target.registerSimilarity(compAtt.getGlobalSim().getSimilFunction());

		for (int i = 0; i < compAtt.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) compAtt.getChild(i);
			if (att instanceof CaseStSimpleAttConcept) {
                CaseStSimpleAttConcept sa = (CaseStSimpleAttConcept) att;
				addSimpleAttribute(sa, target, queryValues);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				addCompoundAttribute(sa, target, queryValues);
			}
		}

	}

	/**
	 * Returns a list of method parameters associated to the simple case
	 * attributes childrens of a compound attribute.
	 * 
	 * @param cc
	 *            compound attribute.
	 * @return list of method parametes.
	 */
	private List<MethodParameter> extractSimpleAttributes(CaseStComponent cc) {
		ArrayList<MethodParameter> atts = new ArrayList<MethodParameter>();
		DataType dataType;

		Vector<CaseStComponent> childs = new Vector<CaseStComponent>();
		cc.getSubcomponents(childs);

		for (Iterator iter = childs.iterator(); iter.hasNext();) {
			CaseStComponent child = (CaseStComponent) iter.next();
			if (child instanceof CaseStSimpleAttConcept) {
                CaseStSimpleAttConcept sa = (CaseStSimpleAttConcept) child;
				dataType = DataTypesRegistry.getInstance().getDataType(
						sa.getType());
				MethodParameter mp = new MethodParameter(sa.getName(), sa
						.getName(), dataType);
				atts.add(mp);
			}
		}

		return atts;
	}

}
