package jcolibri.extensions.DL.cbrcase;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStResult;
import jcolibri.tools.data.CaseStSolution;

/**
 * 
 * CBRCase that supports JENA cases.
 * 
 * @author Pablo José Beltrán Ferruz
 * @version 1.0
 */
public class CBRCaseJena extends JenaIndividual implements CBRCase {

	/**
	 * Creates a new instance of CBRCaseJena
	 * 
	 * @param obj
	 *            Object
	 */
	public CBRCaseJena(Object obj) {
		super(obj);
		descriptionInd = new SimpleIndividual(CaseStDescription.NAME);
		this.addRelation(new IndividualRelation(
				CaseStDescription.RELATION_NAME, descriptionInd));

		solutionInd = new SimpleIndividual(CaseStSolution.NAME);
		this.addRelation(new IndividualRelation(CaseStSolution.RELATION_NAME,
				solutionInd));

		resultInd = new SimpleIndividual(CaseStResult.NAME);
		this.addRelation(new IndividualRelation(CaseStResult.RELATION_NAME,
				resultInd));

	}

	SimpleIndividual solutionInd;

	SimpleIndividual resultInd;

	SimpleIndividual descriptionInd;

	public Individual getDescription() {
		return descriptionInd;
	}

	public Individual getSolution() {
		return solutionInd;
	}

	public Individual getResult() {
		return resultInd;
	}

}
