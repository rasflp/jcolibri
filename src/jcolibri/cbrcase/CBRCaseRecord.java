package jcolibri.cbrcase;

import java.util.Collection;

import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStResult;
import jcolibri.tools.data.CaseStSolution;

/**
 * Simple attribute-value CBRCase strucutre. Although a simple structure the
 * values for the attribute will remain trated as Individual.
 */
public class CBRCaseRecord extends SimpleIndividual implements CBRCase {

	/** Description. */
	SimpleIndividual descriptionInd;

	/** Solution. */
	SimpleIndividual solutionInd;

	/** Result. */
	SimpleIndividual resultInd;

	/**
	 * Creates a new instance of this class
	 * 
	 * @param name
	 *            of the case that is created
	 */
	public CBRCaseRecord(String name) {
		super(name);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRQuery#getDescription()
	 */
	public Individual getDescription() {
		return descriptionInd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCase#getSolution()
	 */
	public Individual getSolution() {
		return solutionInd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRCase#getResult()
	 */
	public Individual getResult() {
		return resultInd;
	}

	/**
	 * Adds a new attribute to this case if doesn't exist yet.
	 * 
	 * @param description
	 *            Name/description of the new attribute
	 * @param value
	 *            Value for the new attribute
	 * @param weight
	 *            Importance indication of this attribute in the whole case.
	 * @param attributeType
	 *            Type of attribute. Not used, mantained here for backwards
	 *            compatibility.
	 */
	public void addAttribute(String description, Object value, double weight,
			String attributeType) {
        SimpleIndividual ind = descriptionInd;
        if(description.startsWith(CaseStSolution.RELATION_NAME))
            ind = this.solutionInd;
        if(description.startsWith(CaseStResult.RELATION_NAME))
            ind = this.resultInd;
        
		if (ind.getRelation(description) == null) {
			SimpleIndividual invidual = new SimpleIndividual(value);
			invidual.setParents(new Individual[] { ind });
			IndividualRelation relation = new IndividualRelation(description,
					invidual);
			relation.setWeight(weight);
			ind.addRelation(relation);
		} else
			ind.getRelation(description).getTarget().setValue(value);
	}

	/**
	 * Returns the value object associated to a specific attribute in this
	 * object
	 * 
	 * @param description
	 *            Name of the attribute
	 * @return Returns the attribute value object
	 */
	public Object getAttributeValue(String description) {
        SimpleIndividual ind = descriptionInd;
        if(description.startsWith(CaseStSolution.RELATION_NAME))
            ind = this.solutionInd;
        if(description.startsWith(CaseStResult.RELATION_NAME))
            ind = this.resultInd;
		return ind.getRelation(description).getTarget().getValue();
	}

	/**
	 * Returns all the attributes of a case
	 * 
	 * @return all the attributes of a case
	 */
	public String[] getAttributes() {
		Collection<IndividualRelation> relations = descriptionInd.getRelations();
        relations.addAll(solutionInd.getRelations());
        relations.addAll(resultInd.getRelations());
		return (String[]) relations.toArray(new String[relations.size()]);
	}

}
