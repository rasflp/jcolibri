package jcolibri.extensions.DL.cbrcase;

import jcolibri.cbrcase.Individual;

/**
 * 
 * Extension of simple IndividualRelation to support real concepts relation
 * inside JENA model.
 * 
 * @author Pablo José Beltrán Ferruz
 * @version 1.0
 */
public class IndividualRelationJena extends jcolibri.cbrcase.IndividualRelation {

	/** Creates a new instance of IndividualRelationRacer */
	public IndividualRelationJena(String relationDescription, Individual ind) {
		super(relationDescription, ind);
	}

	/**
	 * Based of the CBRCase structure will determine if this relation is
	 * important. Currently will just check if the current relation is subsumed
	 * by HAS-ATTRIBUTE relation. This approach will require furhter work to
	 * make it more general
	 * 
	 * @return
	 */
	public boolean isImportant() {
		// TODO: Aqui se deberia ver si la propiedad es hija de HAS-ATTRIBUTE
		return true;
	}
}
