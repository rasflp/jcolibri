package jcolibri.cbrcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jcolibri.similarity.SimilarityFunction;
import jcolibri.util.CBRLogger;

/**
 * BasicCBRCaseBase is a simple linear implementation of the interface
 * CBRCaseBase. It will keep all the retrieved cases in a list structure for
 * further return to requesting methods.
 */
public class BasicCBRCaseBase implements CBRCaseBase, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/** Conector. */
	Connector connector;

	/** List of cases retrieved using the conector. */
	Collection<CBRCase> cases = new ArrayList<CBRCase>();

	/** Cases learned in this session. */
	ArrayList<CBRCase> sessionCases = new ArrayList<CBRCase>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#setConnector(jcolibri.cbrcase.Connector)
	 */
	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#learnCases(java.util.Collection)
	 */
	public void learnCases(Collection<CBRCase> cases) {
		sessionCases.addAll(cases);
		CBRLogger.log(this.getClass(), CBRLogger.INFO, cases.size()
				+ " cases learned. (Not stored into persistence layer)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#loadAll()
	 */
	public void loadAll() {
		cases = connector.retrieveAllCases();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#close()
	 */
	public void close() {
		connector.close();
		CBRLogger
				.log(this.getClass(), CBRLogger.INFO,
						"Connector closed. Learned cases has not been stored into persistence layer.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#loadFor(java.lang.String)
	 */
	public void loadFor(String filter) {
		connector.retrieveSomeCases(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#closeAndUpdate()
	 */
	public void closeAndUpdate() {
		connector.storeCases(sessionCases);
		CBRLogger.log(this.getClass(), CBRLogger.INFO, "Connector closed. "
				+ sessionCases.size()
				+ " learned cases has been stored into persistence layer.");
		connector.close();;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#knn(jcolibri.cbrcase.CBRCase, int,
	 *      jcolibri.similarity.SimilarityFunction)
	 */
	public CaseEvalList knn(CBRCase cbrcase, int numCases,
			SimilarityFunction function) {
		CBRLogger.log(this.getClass(), CBRLogger.INFO, "Query: "
				+ cbrcase.toString());
		CaseEvalList result = new CaseEvalList();
		Collection<CBRCase> currentCases = this.getAll();
		for (Iterator it = currentCases.iterator(); it.hasNext();) {
			CBRCase cbrcaseaux = (CBRCase) it.next();
			double value = function.compute(cbrcase, cbrcaseaux);
			CBRLogger.log(this.getClass(), CBRLogger.INFO, "Result: " + value);
			result.add(new CaseEvaluation(cbrcaseaux, new Double(value)));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#getAll()
	 */
	public Collection<CBRCase> getAll() {
		ArrayList<CBRCase> aux = new ArrayList<CBRCase>();
		aux.addAll(cases);
		aux.addAll(sessionCases);
		return aux;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCaseBase#forgetCases(java.util.Collection)
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		this.sessionCases.removeAll(cases);
        this.cases.removeAll(cases);
	}
}
