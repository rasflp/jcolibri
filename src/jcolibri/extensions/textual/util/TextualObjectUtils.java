package jcolibri.extensions.textual.util;

import jcolibri.extensions.textual.representation.Text;

/**
 * This class implements general methods to manage textual cases.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class TextualObjectUtils {

	/**
	 * Extracts Text objects contained by the Individual and stores them in the
	 * Collection.
	 */
	public static void extractText(jcolibri.cbrcase.Individual ind,
			java.util.Collection col) {
		Object o = ind.getValue();
		if (o instanceof Text)
			col.add(o);
		java.util.Iterator rels = ind.getRelations().iterator();
		while (rels.hasNext()) {
			jcolibri.cbrcase.IndividualRelation irel = (jcolibri.cbrcase.IndividualRelation) rels
					.next();
			jcolibri.cbrcase.Individual child = irel.getTarget();
			extractText(child, col);
		}
	}

	/**
	 * Extracts the name of the individuals (contained by "ind") that store Text
	 * objects
	 */
	public static void getTextualIndividualNames(
			jcolibri.cbrcase.Individual ind, java.util.Collection col) {
		Object o = ind.getValue();
		if (o instanceof Text)
			col.add(((Text) o).getIndividual());
		java.util.Iterator rels = ind.getRelations().iterator();
		while (rels.hasNext()) {
			jcolibri.cbrcase.IndividualRelation irel = (jcolibri.cbrcase.IndividualRelation) rels
					.next();
			jcolibri.cbrcase.Individual child = irel.getTarget();
			getTextualIndividualNames(child, col);
		}
	}

	/**
	 * Returns the Text stored inside an individual.
	 */
	public static Text getTextbyIndividualName(jcolibri.cbrcase.Individual ind,
			String name) {
		Object o = ind.getValue();
		if (o instanceof Text)
			if (((Text) o).getIndividual().equals(name))
				return (Text) o;

		Text _text = null;
		java.util.Iterator rels = ind.getRelations().iterator();
		while (rels.hasNext() && (_text == null)) {
			jcolibri.cbrcase.IndividualRelation irel = (jcolibri.cbrcase.IndividualRelation) rels
					.next();
			jcolibri.cbrcase.Individual child = irel.getTarget();
			_text = getTextbyIndividualName(child, name);
		}
		return _text;
	}
}