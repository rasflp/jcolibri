package jcolibri.extensions.textual.representation;

import java.util.Hashtable;

/**
 * <p>
 * This class represents a Token.
 * </p>
 * <p>
 * Texts are composed by Paragraphs, Paragraphs by Sentences, and Sentences by
 * Tokens.
 * </p>
 * <p>
 * Tokens stores final data about words.
 * </p>
 * <p>
 * It extends DataFilter so new attributes can be added easily.
 * </p>
 * <p>
 * In this basic implementation, the current attributes are:
 * </p>
 * <ul>
 * <li>COMPLETEWORD: Original string.
 * <li>STEMMEDWORD: Stemmed string. It's stored here by StemmerMethod.
 * <li>POSTAG: Part-of-Speech tag. It's stored here by PartofSpeechMethod.
 * <li>TOKENINDEX: Number of token inside the paragraph.
 * <li>WORDPOSITION: Word position inside the paragraph raw data.
 * <li>RELATEDTOKENS: Collection of WeightedRelation objects that relate
 * similar tokens.
 * <li>IS_NAME: Boolean value that indicates if this token is name. It's stored
 * by ExtractNamesMethod.
 * <li>ISNOT_STOPWORD: Boolean value that indicates if this token isn't a stop
 * word. It's stored by WordsFilterMethod.
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * 
 */
public class Token extends DataFilter {
	public static final String COMPLETEWORD = "CompleteWord";

	public static final String STEMMEDWORD = "StemmedWord";

	public static final String POSTAG = "POSTag";

	public static final String TOKENINDEX = "Token Index";

	public static final String WORDPOSITION = "Word Position";

	public static final String RELATEDTOKENS = "relatedTokens";

	public static final String IS_NAME = "is name";

	public static final String ISNOT_STOPWORD = "IsNotStopWord";

	/**
	 * Sets Tokens filter.
	 */
	public static void setFilter(Hashtable[] filter) {
		DataFilter.setFilter(
				"jcolibri.extensions.textual.representation.Token", filter);
	}

	/**
	 * Returns Tokens filter.
	 */
	public static Hashtable[] getFilter() {
		return DataFilter
				.getFilter("jcolibri.extensions.textual.representation.Token");
	}

	/**
	 * Returns a string representation of this token. It tries to return the
	 * COMPLETEWORD attribute. If it isn't found it returns object.toString()
	 * value.
	 */
	public String toString() {
		String res = (String) _data.get(Token.COMPLETEWORD);
		if (res == null)
			res = super.toString();
		return res;
	}

}