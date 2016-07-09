package jcolibri.extensions.textual.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * <p>
 * This class represents a Sentence.
 * </p>
 * <p>
 * Texts are composed by Paragraphs, Paragraphs by Sentences, and Sentences by
 * Tokens.
 * </p>
 * <p>
 * It extends DataFilter so new attributes can be added easily.
 * </p>
 * <p>
 * In this basic implementation, the current attributes are:
 * </p>
 * <ul>
 * <li>RAW_DATA: String with the original text.
 * <li>SENTENCE_POSITION: This is the index of this sentence inside the
 * Paragraph raw data.
 * <li>TOKENS: Tokens contained by this Sentence.
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class Sentence extends DataFilter {
	public static final String RAW_DATA = "raw_data";

	public static final String SENTENCE_POSITION = "Sentence Position";

	public static final String TOKENS = "tokens";

	/**
	 * Default constructor. Raw data is empty.
	 */
	public Sentence() {
		this("");
	}

	/**
	 * Constructor
	 * 
	 * @param raw
	 *            Original sentence text.
	 */
	public Sentence(String raw) {
		this.setData(Sentence.RAW_DATA, raw);
		this.setData(Sentence.TOKENS, new ArrayList());
	}

	/**
	 * Constructor
	 * 
	 * @param orig
	 *            Original sentence text.
	 * @param tokens
	 *            Tokens contained by this sentence
	 */
	public Sentence(Sentence orig, Collection tokens) {
		_data = new Hashtable(orig._data);
		this.setData(Sentence.TOKENS, new ArrayList(tokens));
	}

	/**
	 * Adds a new token
	 */
	public void addToken(Token tok) {
		ArrayList tokens = (ArrayList) _data.get(Sentence.TOKENS);
		tokens.add(tok);
	}

	/**
	 * Returns the tokens collection
	 * 
	 * @return Collection of Tokens
	 */
	public Collection getTokens() {
		return (Collection) _data.get(Sentence.TOKENS);
	}

	/**
	 * Returns a new Sentences with the tokens that satisfies the Token filter.
	 * 
	 * @return New sentence with the filtered tokens
	 */
	public Sentence getFilteredSentence() {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(TOKENS);
		if (col == null)
			return new Sentence();
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Token tok = (Token) colIter.next();
			if (tok.checkConditions())
				l.add(tok);
		}
		return new Sentence(this, l);
	}

	/**
	 * Returns a new Sentence containing the token data that satisfies the Token
	 * filter.
	 * 
	 * @return New sentence with the filtered tokens
	 */
	public Sentence getFilteredSentenceData(String datakey) {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(TOKENS);
		if (col == null)
			return new Sentence();
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Token tok = (Token) colIter.next();
			Object data = tok.getData(datakey);
			if (data != null)
				l.add(tok);
		}
		return new Sentence(this, l);
	}

	/**
	 * Sets Sentence filter
	 */
	public static void setFilter(Hashtable[] filter) {
		DataFilter.setFilter(
				"jcolibri.extensions.textual.representation.Sentence", filter);
	}

	/**
	 * Returns Sentence filter
	 */
	public static Hashtable[] getFilter() {
		return DataFilter
				.getFilter("jcolibri.extensions.textual.representation.Sentence");
	}

}