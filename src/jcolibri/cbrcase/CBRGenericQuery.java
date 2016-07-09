package jcolibri.cbrcase;

import jcolibri.tools.data.CaseStDescription;

/**
 * Generic query for a CBR Application.
 * 
 * @author Juan A. Recio-García
 */
public class CBRGenericQuery extends SimpleIndividual implements CBRQuery {

	/** The description. */
	SimpleIndividual descriptionInd;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            name of the query.
	 */
	public CBRGenericQuery(Object value) {
		super(value);
		descriptionInd = new SimpleIndividual(CaseStDescription.NAME);
		this.addRelation(new IndividualRelation(
				CaseStDescription.RELATION_NAME, descriptionInd));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcase.CBRQuery#getDescription()
	 */
	public Individual getDescription() {
		return descriptionInd;
	}

}
