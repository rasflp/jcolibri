package jcolibri.extensions.DL.cbrcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcase.SimpleIndividual;
import jcolibri.util.CBRLogger;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

/**
 * 
 * Extension of SimpleIndividual to support cases retrieved from Jena Model.
 * Instances of this class will represent real instances on a DL reasoner
 * 
 * @author Pablo José Beltrán Ferruz
 * @version 1.0
 */
public class JenaIndividual extends SimpleIndividual {

	Resource res;

	/**
	 * 
	 * Constructor of the object must receive an Individual of Jena
	 * 
	 * @param obj
	 *            Object
	 */
	public JenaIndividual(Object obj) {
		super(obj.toString());
		res = (ResourceImpl) obj;
	}

	/**
	 * 
	 * This method is not implemented
	 * 
	 */
	public void addRelation(IndividualRelation relation) {
		CBRLogger.log("JenaIndividual", CBRLogger.ERROR,
				"Method addRelation is not implemented in JenaIndividual");
	}

	/**
	 * 
	 * Returns all parents of this JenaIndividual
	 * 
	 * @return Individual[]
	 */
	public Individual[] getParents() {
		// TODO Auto-generated method stub

		Collection<Individual> list = new LinkedList<Individual>();
		int n = 0;

		for (Iterator i = res.listProperties(); i.hasNext();) {
			Statement state = (Statement) i.next();

			if (state.getPredicate().getLocalName().equals("type")) {
				list.add(new JenaIndividual(state.getResource()));
				n++;
			}
		}

		if (n >= 0) {
			Individual[] result = new Individual[n];
			int t = 0;
			for (Iterator i = list.iterator(); i.hasNext();) {
				result[t++] = (Individual) i.next();
			}

			super.setParents(result);
			return result;
		} else {
			CBRLogger.log("JenaIndividual", CBRLogger.ERROR, "Error");
			return null;
		}

	}

	/**
	 * 
	 * Retrieves all parents of this case
	 * 
	 * @return Collection
	 */
	public Collection<String> getAllParents() {
		com.hp.hpl.jena.ontology.Individual qind = (com.hp.hpl.jena.ontology.Individual) res;
		ArrayList<String> list = new ArrayList<String>();

		for (Iterator it = qind.listRDFTypes(false); it.hasNext();) {
			String parent = ((Resource) it.next()).toString();
			list.add(parent);
		}

		return list;
	}

	/**
	 * 
	 * Retrieve only direct Parents of this case
	 * 
	 * @return Collection
	 */
	public Collection getDirectParents() {
		com.hp.hpl.jena.ontology.Individual qind = (com.hp.hpl.jena.ontology.Individual) res;
		ArrayList<String> list = new ArrayList<String>();

		for (Iterator it = qind.listRDFTypes(true); it.hasNext();) {
			String parent = ((Resource) it.next()).toString();
			list.add(parent);
		}

		return list;
	}

	/**
	 * 
	 * This method is not implemented.
	 * 
	 * @param description
	 *            String
	 * @return IndividualRelation
	 */
	public IndividualRelation getRelation(String description) {
		CBRLogger.log("JenaIndividual", CBRLogger.ERROR,
				"Method getRelation() is not implemented in JenaIndividual");
		return null;

	}

	/**
	 * 
	 * This method returns all relations of the JenaIndividual
	 * 
	 * @return Collection
	 */
	public Collection<IndividualRelation> getRelations() {

		Collection<IndividualRelation> result = new LinkedList<IndividualRelation>();

		for (Iterator i = res.listProperties(); i.hasNext();) {
			Statement state = (Statement) i.next();
			result.add(new IndividualRelationJena(state.getPredicate()
					.getLocalName(), new JenaIndividual(state.getResource())));

		}

		return result;
	}

	/**
	 * 
	 * This method returns the name of the JenaIndividual
	 * 
	 * @return Object
	 */
	public Object getValue() {
		return res.getLocalName();
	}

	/**
	 * 
	 * This method returns the name of the JenaIndividual with namespace
	 * 
	 * @return Object
	 */
	public Object getName() {
		return this.getValue();
	}

	/**
	 * 
	 * This methos is not implemented
	 * 
	 * @param parents
	 *            Individual[]
	 */
	public void setParents(Individual[] parents) {
		CBRLogger.log("JenaIndividual", CBRLogger.ERROR,
				"Method setParents() is not implemented in JenaIndividual");
	}

	/**
	 * 
	 * This method is not implemented
	 * 
	 * @param relationsWithIndividuals
	 *            Collection
	 */
	public void setRelations(Collection relationsWithIndividuals) {
		CBRLogger.log("JenaIndividual", CBRLogger.ERROR,
				"Method setRelations() is not implemented in JenaIndividual");
	}

	/**
	 * 
	 * This method is not implemented
	 * 
	 * @param value
	 *            Object
	 */
	public void setValue(Object value) {
		CBRLogger.log("JenaIndividual", CBRLogger.ERROR,
				"Method setValue() is not implemented in JenaIndividual");
	}

	/**
	 * 
	 * This method writes all information about the case
	 * 
	 * @return String
	 */
	public String toString() {
		return super.toString();
	}
}
