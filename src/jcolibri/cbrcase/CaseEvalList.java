package jcolibri.cbrcase;

import java.util.TreeSet;

import jcolibri.cbrcore.CBRTerm;

/**
 * Data structure containing a set of CaseEvaluation objects, that holds a case
 * and a object that represents an evaluation of the case. The structure will
 * keep a ascending order of the cases according their evaluation.
 * 
 * @see CaseEvaluation
 */

public class CaseEvalList extends TreeSet<CaseEvaluation> implements CBRTerm {
	private static final long serialVersionUID = 1L;

}
