package jcolibri.extensions.web.method;

import java.util.HashMap;

import jcolibri.cbrcase.CBRGenericQuery;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.extensions.web.bridge.SessionOutException;
import jcolibri.extensions.web.bridge.Synchronizer;
import jcolibri.extensions.web.bridge.WebBridge;
import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStructure;
import jcolibri.util.CBRLogger;

/**
 * Reads the structure of the case from a file and build a frame dynamically
 * that can be used to introduce the values of the query.
 * 
 * @author Juan Antonio Recio García
 */
public class ObtainWebQueryMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

	/** Parameter name containing the case structure file path. */
	public static final String CASE_STRUCTURE_FILE = "Case Structure";
    
    public static final String TIME_OUT = "Time out";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {

		String configFile = (String) this
				.getParameterValue(CASE_STRUCTURE_FILE);
        
        int timeout = (Integer)this.getParameterValue(TIME_OUT);

		CaseStructure caseStructure = new CaseStructure();
		caseStructure.readFromXMLFile(configFile);
        
       
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Waiting for query from Web Server. Sleeping current thread");

        try
        {
            Synchronizer.cycleWaitsTurn(timeout);
        } catch (SessionOutException e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, "Web server time out. Aborting CBR cycle");
            throw new ExecutionException();
        }
        
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Recieved query from Web Server. Waking up current thread");
        
		HashMap queryValues = (HashMap) WebBridge.getData("QUERY_VALUES");

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
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
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
	private void addSimpleAttribute(CaseStSimpleAtt simpleAtt, Individual ind,
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
			if (att instanceof CaseStSimpleAtt) {
				CaseStSimpleAtt sa = (CaseStSimpleAtt) att;
				addSimpleAttribute(sa, target, queryValues);
			} else if (att instanceof CaseStCompoundAtt) {
				CaseStCompoundAtt sa = (CaseStCompoundAtt) att;
				addCompoundAttribute(sa, target, queryValues);
			}
		}

	}

}
