package jcolibri.extensions.textual.similarity.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.representation.WeightedRelation;

/**
 * <p>
 * This class implements several algorithms that are used by the similiarity
 * functions.
 * </p>
 * <p>
 * These algorithms take two tokens collections and compute Intersection and
 * Union using the Weighted Relations between tokens.
 * </p>
 * <p>
 * Comparision between tokens can be made using several attributes
 * (COMPLETTEWORD, STEMMEDWORD, ...). It's indicated using the _data parameter.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class TextualSimilarityUtils {

	/**
	 * Intersection.
	 * 
	 * @param c1
	 *            Tokens Collection 1
	 * @param c2
	 *            Tokens Collection 2
	 * @param _data
	 *            Tokens comparision attribute
	 * @return Intersection length.
	 */
	public static double Intersection(Collection c1, Collection c2, String _data) {
		double res = 0;

		for (Iterator Iter1 = c1.iterator(); Iter1.hasNext();) {
			Token tok1 = (Token) Iter1.next();
			String d1 = (String) tok1.getData(_data);

			for (Iterator Iter2 = c2.iterator(); Iter2.hasNext();) {
				Token tok2 = (Token) Iter2.next();
				String d2 = (String) tok2.getData(_data);
				if (d1.equals(d2))
					res += 1;
			}
		}
		res += computeRelations(c1, c2);

		return res;
	}

	/**
	 * Union.
	 * 
	 * @param c1
	 *            Tokens Collection 1
	 * @param c2
	 *            Tokens Collection 2
	 * @param _data
	 *            Tokens comparision attribute
	 * @return Union lenght
	 */
	public static double Union(Collection c1, Collection c2, String _data) {
		double res = c1.size();

		for (Iterator Iter1 = c1.iterator(); Iter1.hasNext();) {
			Token tok1 = (Token) Iter1.next();
			String d1 = (String) tok1.getData(_data);

			boolean found = false;
			for (Iterator Iter2 = c2.iterator(); Iter2.hasNext() && (!found);) {
				Token tok2 = (Token) Iter2.next();
				String d2 = (String) tok2.getData(_data);
				if (d1.equals(d2))
					found = true;
			}
			if (!found)
				res += 1;
		}
		res += computeRelations(c1, c2);

		return res;
	}

	/**
	 * Returns the length of a collection. It's not the size() because it must
	 * compute relations between tokens.
	 */
	public static double size(Collection c) {
		double res = c.size();
		for (Iterator Iter1 = c.iterator(); Iter1.hasNext();) {
			Token tok1 = (Token) Iter1.next();
			Hashtable ht = (Hashtable) tok1.getData(Token.RELATEDTOKENS);
			if (ht != null)
				res += ht.size();
		}
		return res;
	}

	private static double computeRelations(Collection c1, Collection c2) {
		double res = 0;
		// Here we have a problem, because we know that query tokens has a list
		// with
		// the related tokens, BUT we don't know if our query is c1 or c2.
		// Due to, case tokens hasn't related tokens information, we can try
		// both
		// directions. One of this bucles, never makes nothing.

		for (Iterator Iter1 = c1.iterator(); Iter1.hasNext();) {
			Token tok1 = (Token) Iter1.next();
			Hashtable ht = (Hashtable) tok1.getData(Token.RELATEDTOKENS);
			if (ht == null)
				continue;
			ArrayList related = new ArrayList(ht.values());
			for (int r = 0; r < related.size(); r++) {
				WeightedRelation wr = (WeightedRelation) related.get(r);
				Object destination = wr.getDestination();
				if (c2.contains(destination)) {
					res += wr.getWeight();
					related.remove(r);
					r--;
				}
			}
		}

		for (Iterator Iter2 = c2.iterator(); Iter2.hasNext();) {
			Token tok2 = (Token) Iter2.next();
			Hashtable ht = (Hashtable) tok2.getData(Token.RELATEDTOKENS);
			if (ht == null)
				continue;
			ArrayList related = new ArrayList(ht.values());
			for (int r = 0; r < related.size(); r++) {
				WeightedRelation wr = (WeightedRelation) related.get(r);
				Object destination = wr.getDestination();
				if (c1.contains(destination)) {
					res += wr.getWeight();
					related.remove(r);
					r--;
				}
			}
		}
		return res;
	}

}