package jcolibri.similarity.global;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.RelationEvaluation;
import jcolibri.similarity.SimilarityFunction;
import jcolibri.util.CBRLogger;

/**
 * This similarity receives 2 compound attributes and computes the similarities
 * between their sub-attributes, returning these values as a list. This list can
 * be used by other global similarities to compute the similarity associated to
 * the compound attribute.
 * 
 * @author Juan Antonio Recio García
 */
public abstract class CBRCaseNumericSim implements SimilarityFunction {

	// ##Implemented by rasflp@gmail.com on Sep 7, 2016
	public double weightSummary = 0;
	
	
	/**
	 * Computes the similarities between the children individuals of o1 and o2.
	 * 
	 * @param o1
	 *            individual representing a compound attribute.
	 * @param o2
	 *            individual representing a compound attribute.
	 * @return list of similarities between the childrens of o1 and o2.
	 */
	protected List<RelationEvaluation> compareCompoundAttributes(Individual o1, Individual o2) {

		LinkedList<RelationEvaluation> relationEvalList = new LinkedList<RelationEvaluation>();
		
		// ##Implemented by rasflp@gmail.com on Sep 7, 2016
		weightSummary = 0;
		
		for (Iterator it = o1.getRelations().iterator(); it.hasNext();) {
			IndividualRelation o1rel = (IndividualRelation) it.next();
			IndividualRelation o2rel = o2.getRelation(o1rel.getDescription());
			if (o2rel == null) // doesn't exists in the case
			{
				CBRLogger
						.log(
								this.getClass(),
								CBRLogger.ERROR,
								"Query structure doesn't match with case structure. Query-Description attribute: "
										+ o1rel.getDescription()
										+ " not found.");
				continue;
			}

			if (o1rel.getWeight() == 0.0) // we don't have to compute this
			// attribute
			{
				relationEvalList.add(new RelationEvaluation(o1rel
						.getDescription(), new Double(0)));
				continue;
			}

			SimilarityFunction similfun = o1rel.getTarget()
					.getSimilarityFunction();
			double sim = similfun.compute(o1rel.getTarget(), o2rel.getTarget());

			// ##Implemented by rasflp@gmail.com on Sep 7, 2016
			sim = sim * o2rel.getWeight();	// o2rel
			weightSummary = weightSummary + o2rel.getWeight();
			
			relationEvalList.add(new RelationEvaluation(o1rel.getDescription(),
					new Double(sim)));
		}
		return relationEvalList;
	}
}
