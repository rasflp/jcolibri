package jcolibri.util;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseRecord;
import jcolibri.cbrcase.CBRGenericQuery;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStResult;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStSimpleAttConcept;
import jcolibri.tools.data.CaseStSolution;
import jcolibri.tools.data.CaseStructure;

/**
 * Utilties for creating cases.
 * 
 * @author Juan A. Recio-García
 */
public class CaseCreatorUtils {

	/** Last case structure. */
	private static CaseStructure LASTCaseStructure = null;

	/** Last case structure XML file. */
	private static String LASTcaseStructureXMLfile = null;

	/**
	 * Reads a case structure from a XML file.
	 * 
	 * @param caseStructureXMLfile
	 *            file path.
	 * @return case structure.
	 */
	private static CaseStructure getCaseStructure(String caseStructureXMLfile) {
		if (LASTcaseStructureXMLfile != null)
			if (LASTcaseStructureXMLfile.equals(caseStructureXMLfile))
				if (LASTCaseStructure != null)
					return LASTCaseStructure;
		// else
		LASTCaseStructure = new CaseStructure();
		LASTCaseStructure.readFromXMLFile(caseStructureXMLfile);
		LASTcaseStructureXMLfile = caseStructureXMLfile;
		return LASTCaseStructure;
	}

	/**
	 * Returns a copy of a case.
	 * 
	 * @param caseStructureXMLfile
	 *            file path.
	 * @param originalCase
	 *            original case to copy.
	 * @return a copy of the original case.
	 */
	public static CBRCaseRecord copyCase(String caseStructureXMLfile,
			CBRCase originalCase) {
		CBRCaseRecord copyCase = createCase(caseStructureXMLfile);
		copyIndividualValues(originalCase, copyCase);
		return copyCase;
	}

	/**
	 * Copy the value of an original individual to a target individual.
	 * 
	 * @param orig
	 *            original individual.
	 * @param target
	 *            target individual.
	 */
	public static void copyIndividualValues(Individual orig, Individual target) {
		Object value = orig.getValue();
		target.setValue(value);
		for (java.util.Iterator iter = orig.getRelations().iterator(); iter
				.hasNext();) {
			IndividualRelation origRel = (IndividualRelation) iter.next();
			IndividualRelation copyRel = target.getRelation(origRel
					.getDescription());
			copyIndividualValues(origRel.getTarget(), copyRel.getTarget());
		}
	}

	/**
	 * Returns a CBRCaseRecord from a XML file with a case structure.
	 * 
	 * @param caseStructureXMLfile
	 *            file path.
	 * @return CBRCasRecord.
	 */
	public static CBRCaseRecord createCase(String caseStructureXMLfile) {
		CBRCaseRecord _case = new CBRCaseRecord("case");

		CaseStructure caseStructure = getCaseStructure(caseStructureXMLfile);

		CaseStDescription description = caseStructure.getDescription();
		Individual descriptionInd = _case.getDescription();
		createComponent(description, descriptionInd);
        descriptionInd.registerSimilarity(description.getGlobalSim().getSimilFunction());

		CaseStSolution solution = caseStructure.getSolution();
		Individual solutionInd = _case.getSolution();
		createComponent(solution, solutionInd);

		return _case;
	}

	/**
	 * Returns a CBRQuery with the structure of the case description described
	 * in a XML file.
	 * 
	 * @param caseStructureXMLfile
	 * @return
	 */
	public static CBRQuery createQuery(String caseStructureXMLfile) {
		CBRQuery _query = new CBRGenericQuery("query");

		CaseStructure caseStructure = getCaseStructure(caseStructureXMLfile);

		CaseStDescription description = caseStructure.getDescription();
		Individual descriptionInd = _query.getDescription();
		createComponent(description, descriptionInd);
        descriptionInd.registerSimilarity(description.getGlobalSim().getSimilFunction());


		return _query;
	}

	/**
	 * Creates an individual representing a case component.
	 * 
	 * @param component
	 *            case component.
	 * @param componentInd
	 *            individual.
	 */
	private static void createComponent(CaseStComponent component,
			Individual componentInd) {
		for (int i = 0; i < component.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) component.getChild(i);
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
				createSimpleAttribute(sa, componentInd);
            } else if (att instanceof CaseStSimpleAttConcept) {
                CaseStSimpleAttConcept sa = (CaseStSimpleAttConcept) att;
                createSimpleAttConcept(sa, componentInd);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				createCompoundAttribute(sa, componentInd);
			}
		}

	}

    /**
     * Creates an individual representing a simple attribute concept.
     * 
     * @param simpleAtt
     *            simple attribute.
     * @param ind
     *            individual.
     */
    private static void createSimpleAttConcept(CaseStSimpleAttConcept simpleAtt,
            Individual ind) {
        String relationName = simpleAtt.getRelation();
        SimpleIndividual target = new SimpleIndividual(null);
        IndividualRelation relation = new IndividualRelation(relationName,
                target);
        relation.setWeight(simpleAtt.getWeight());
        ind.addRelation(relation);
        target.registerSimilarity(simpleAtt.getLocalSim().getSimilFunction());
        target.setParents(new Individual[] { ind });
    }
    
    
	/**
	 * Creates an individual representing a simple attribute.
	 * 
	 * @param simpleAtt
	 *            simple attribute.
	 * @param ind
	 *            individual.
	 */
	private static void createSimpleAttribute(CaseStSimpleAtt simpleAtt,
			Individual ind) {
		String relationName = simpleAtt.getRelationPath();
		SimpleIndividual target = new SimpleIndividual(null);
		IndividualRelation relation = new IndividualRelation(relationName,
				target);
		relation.setWeight(simpleAtt.getWeight());
		ind.addRelation(relation);
		target.registerSimilarity(simpleAtt.getLocalSim().getSimilFunction());
		target.setParents(new Individual[] { ind });
	}

	/**
	 * Creates an individual representing a compound attribute.
	 * 
	 * @param compAtt
	 *            compound attribute.
	 * @param ind
	 *            individual.
	 */
	private static void createCompoundAttribute(CaseStCompoundAtt compAtt,
			Individual ind) {
		String individualName = compAtt.getPathWithoutCase();
		String relationName = compAtt.getRelationPath();
		SimpleIndividual target = new SimpleIndividual(individualName);
		IndividualRelation relation = new IndividualRelation(relationName,
				target);
		relation.setWeight(compAtt.getWeight());
		ind.addRelation(relation);
		target.registerSimilarity(compAtt.getGlobalSim().getSimilFunction());
		target.setParents(new Individual[] { ind });

		for (int i = 0; i < compAtt.getNumChildrens(); i++) {
			CaseStAttribute att = (CaseStAttribute) compAtt.getChild(i);
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
				createSimpleAttribute(sa, target);
            } else if (att instanceof CaseStSimpleAttConcept) {
                CaseStSimpleAttConcept sa = (CaseStSimpleAttConcept) att;
                createSimpleAttConcept(sa, target);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				createCompoundAttribute(sa, target);
			}
		}
	}

	/**
	 * Sets the name of a case.
	 * 
	 * @param _case
	 *            case.
	 * @param name
	 *            new name.
	 */
	public static void setCaseName(CBRCase _case, String name) {
		_case.setValue(name);
	}

	/**
	 * Returns a individual of a case by its name.
	 * 
	 * @param _case
	 *            case.
	 * @param individualName
	 *            individual name.
	 * @return individual.
	 */
	public static Individual getAttribute(CBRCase _case, String individualName) {
		Individual description = _case.getDescription();
		if (individualName.equals(CaseStDescription.NAME))
			return description;
		if (individualName.startsWith(CaseStDescription.NAME))
			return findIndividual(description, individualName);

		Individual solution = _case.getSolution();
		if (individualName.equals(CaseStSolution.NAME))
			return solution;
		if (individualName.startsWith(CaseStSolution.NAME))
			return findIndividual(solution, individualName);

		if (individualName.equals(CaseStResult.NAME))
			return _case.getResult();

		return null;
	}

	/**
	 * Returns an individual of a query by its name.
	 * 
	 * @param _query
	 * @param individualName
	 * @return
	 */
	public static Individual getAttribute(CBRQuery _query, String individualName) {
		Individual description = _query.getDescription();
		if (individualName.equals(CaseStDescription.NAME))
			return description;
		if (individualName.startsWith(CaseStDescription.NAME))
			return findIndividual(description, individualName);

		return null;
	}

	/**
	 * Returns an individual child of another individual by its name.
	 * 
	 * @param parent
	 * @param individualName
	 * @return
	 */
	private static Individual findIndividual(Individual parent,
			String individualName) {
		for (java.util.Iterator iter = parent.getRelations().iterator(); iter
				.hasNext();) {
			IndividualRelation rel = (IndividualRelation) iter.next();
			if (rel.getDescription().equals(
					CaseStComponent.RelationPrefix + individualName))
				return rel.getTarget();
			else
				findIndividual(rel.getTarget(), individualName);
		}
		return null; // Not found
	}

}
