package jcolibri.cbrcore;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseBase;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcase.CaseEvalList;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcore.exception.IncompatibleMethodException;

/**
 * Class that serves as communication between methods execution. In fact will
 * hold the state of the CBR system at any moment.
 * 
 * @author Juan José Bello
 */
public class CBRContext implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Checks the consistency of the context. */
	CBRContextChecker contextChecker = new CBRContextChecker();

	/** Case base. */
	CBRCaseBase _caseBase = null;

	/** Query. */
	CBRQuery _query = null;

	/** Evaluation of the cases. */
	CaseEvalList _caseEvalList = null;

	/** Cases for a which a solution is currently being searched. */
	List<CBRCase> _cases = null;

	/**
	 * Returns the CBRCaseBase currently being used on the application.
	 * 
	 * @return the CBRCaseBase.
	 */
	public CBRCaseBase getCBRCaseBase() {
		return _caseBase;
	}

	/**
	 * Sets the CBRCaseBase that is goint to be used on the application.
	 * 
	 * @param caseBase
	 */
	public void setCBRCaseBase(CBRCaseBase caseBase) {
		_caseBase = caseBase;
	}

	/**
	 * Returns the query for a which a solution is currently being searched.
	 * 
	 * @return CBRQuery
	 */
	public CBRQuery getQuery() {
		return _query;
	}

	/**
	 * Sets the query for a which a solution must be searched.
	 * 
	 * @param c
	 *            CBRQuery.
	 */
	public void setQuery(CBRQuery c) {
		_query = c;
	}

	/**
	 * Returns the cases for a which a solution is currently being searched.
	 * 
	 * @return list of cases.
	 */
	public List<CBRCase> getCases() {
		return _cases;
	}

	/**
	 * Sets the cases for a which a solution must be searched.
	 * 
	 * @param list
	 *            list of cases.
	 */
	public void setCases(List<CBRCase> list) {
		_cases = list;
	}

	/**
	 * Returns the list of evaluated cases.
	 * 
	 * @return CaseEvalList
	 */
	public CaseEvalList getEvaluatedCases() {
		return _caseEvalList;
	}

	/**
	 * Sets the list of evaluated cases.
	 * 
	 * @param list
	 *            list of evaluated cases.
	 */
	public void setEvaluatedCases(CaseEvalList list) {
		_caseEvalList = list;
	}

	/**
	 * Adds a new method to the current application execution context.
	 * 
	 * @param method
	 *            Method that is added.
	 * @throws IncompatibleMethodException
	 *             thrown when the method is not applicable over current
	 *             context.
	 */
	public void addMethodToExecution(CBRMethod method)
			throws IncompatibleMethodException {
		contextChecker.addMethod(method);
	}

	/**
	 * Checks if the a method is compatible with the current context.
	 * 
	 * @param method
	 *            Given method to be checked.
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isMethodApplicable(CBRMethod method) {
		return contextChecker.isMethodApplicable(method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("WORKING CASES:\n");
		if (getCases() == null)
			sb.append("empty\n");
		else {
			for (Iterator it = getCases().iterator(); it.hasNext();) {
				sb.append(it.next().toString());
				sb.append("\n");
			}
		}
		if (this._caseBase != null)
			sb.append("\nCASE BASE: " + this._caseBase.getAll().size()
					+ " cases\n");
		else
			sb.append("\nCASE BASE: empty\n");
		sb.append("QUERY:\n");
		if (getQuery() == null)
			sb.append("empty\n");
		else {
			Collection rels = getQuery().getRelations();
			for (Iterator it = rels.iterator(); it.hasNext();) {
				sb.append(((IndividualRelation) it.next()).toString());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 * Write this instance in a output stream.
	 * 
	 * @param out
	 *            output stream.
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		System.err.println("Writing CONTEXT");
		out.writeObject(this.contextChecker);
	}

	/**
	 * Read an instance from a input stream.
	 * 
	 * @param in
	 *            input stream.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.contextChecker = (CBRContextChecker) in.readObject();
		this._caseBase = null;
		this._caseEvalList = null;
		this._cases = null;
		this._query = null;
	}
}
