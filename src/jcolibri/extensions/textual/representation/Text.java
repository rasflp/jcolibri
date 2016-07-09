package jcolibri.extensions.textual.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * <p>
 * This class represents a Text.
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
 * <li>NLPDOC: Opennlp representation of this text. It's used internally.
 * <li>PARAGRAPHS: Collection of Paragraphs that compose this text.
 * </ul>
 * This class also stores information about the case and individual that
 * contains this text.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class Text extends DataFilter {
	public static final String NLPDOC = "nlpdoc";

	public static final String PARAGRAPHS = "paragraphs";

	String _case;

	String _ind;

	/**
	 * Default constructor. Case name, Individual name and Paragraph list are
	 * empty.
	 */
	public Text() {
		this("", "", new ArrayList());
	}

	/**
	 * Constructor. Paragraph list is empty.
	 * 
	 * @param c
	 *            Case name
	 * @param i
	 *            Individual name
	 */
	public Text(String c, String i) {
		this(c, i, new ArrayList());
	}

	/**
	 * Constructor.
	 * 
	 * @param c
	 *            Case Name.
	 * @param i
	 *            Individual Name.
	 * @param paragraphs
	 *            Paragraph list.
	 */
	public Text(String c, String i, Collection paragraphs) {
		this.setData(Text.PARAGRAPHS, new ArrayList(paragraphs));
		_case = new String(c);
		_ind = new String(i);
	}

	/**
	 * Adds a new paragraph.
	 */
	public void addParagraph(Paragraph par) {
		ArrayList pars = (ArrayList) _data.get(Text.PARAGRAPHS);
		pars.add(par);
	}

	/**
	 * Returns the paragraph collection.
	 */
	public Collection getParagraphs() {
		return (ArrayList) _data.get(Text.PARAGRAPHS);
	}

	/**
	 * Returns a filtered copy of this Text object.
	 */
	public Text getFilteredText() {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(Text.PARAGRAPHS);
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Paragraph par = (Paragraph) colIter.next();
			if (par.checkConditions())
				l.add(par.getFilteredParagraph());
		}
		return new Text(this._case, this._ind, l);
	}

	/**
	 * Returns a filtered text that contains tokens data (that comply token
	 * filter) instead of Tokens.
	 * 
	 * @param keyString
	 *            contained tokens data
	 */
	public Text getFilteredTextData(String keyString) {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(Text.PARAGRAPHS);
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Paragraph par = (Paragraph) colIter.next();
			if (par.checkConditions())
				l.add(par.getFilteredParagraphData(keyString));
		}
		return new Text(this._case, this._ind, l);
	}

	/**
	 * Returns a filtered list of tokens
	 */
	public Collection getTokensList() {
		Text t = this.getFilteredText();
		ArrayList result = new ArrayList();
		Collection parColl = t.getParagraphs();
		Iterator parIter = parColl.iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			Iterator sentIter = par.getSentences().iterator();
			while (sentIter.hasNext()) {
				Sentence sent = (Sentence) sentIter.next();
				Iterator tokensIter = sent.getTokens().iterator();
				while (tokensIter.hasNext()) {
					Token tok = (Token) tokensIter.next();
					result.add(tok);
				}
			}
		}
		return result;
	}

	/**
	 * Returns a filtered list of data (extracted from Tokens)
	 * 
	 * @param key
	 *            data attribute
	 */
	public Collection getDataList(String key) {
		Text t = this.getFilteredTextData(key);
		ArrayList result = new ArrayList();
		Iterator parIter = t.getParagraphs().iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			Iterator sentIter = par.getSentences().iterator();
			while (sentIter.hasNext()) {
				Sentence sent = (Sentence) sentIter.next();
				Iterator tokensIter = sent.getTokens().iterator();
				while (tokensIter.hasNext()) {
					Token tok = (Token) tokensIter.next();
					result.add(tok.getData(key));
				}
			}
		}
		return result;
	}

	/**
	 * Returns a collection with all the sentences, without filtering
	 * 
	 * @return
	 */
	public Collection getSentences() {
		ArrayList result = new ArrayList();
		Iterator parIter = this.getParagraphs().iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			result.addAll(par.getSentences());
		}
		return result;
	}

	/**
	 * Sets individual name.
	 */
	public void setIndividual(String ind) {
		_ind = ind;
	}

	/**
	 * Returns individual name.
	 * 
	 * @return
	 */
	public String getIndividual() {
		return _ind;
	}

	/**
	 * Sets case name.
	 */
	public void setCase(String c) {
		_case = c;
	}

	/**
	 * Returns case name.
	 */
	public String getCase() {
		return _case;
	}

	/**
	 * Returns a textual representation of this Text.
	 */
	public String toString() {
		int p = 0;
		String result = "";
		Iterator parIter = this.getParagraphs().iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			result += ("Paragraph " + p++ + ": "
					+ par.getData(Paragraph.RAW_DATA) + "\n");
			// result+= par.printAttributes();
			/*
			 * Iterator sentIter = par.getSentences().iterator();
			 * while(sentIter.hasNext()) { Sentence sent =
			 * (Sentence)sentIter.next(); result +=(" Sencence "+ s++ +".
			 * Position: "+ sent.getData(Sentence.SENTENCE_POSITION)+ " Data: "
			 * +sent.getData(sent.RAW_DATA) +"\n"); Iterator tokensIter =
			 * sent.getTokens().iterator(); while(tokensIter.hasNext()) { result += ("
			 * Token:\n"); Token tok = (Token)tokensIter.next(); Enumeration
			 * keys = tok._data.keys(); while(keys.hasMoreElements()) { String
			 * key = (String)keys.nextElement(); result+=(" "+key+":
			 * "+tok._data.get(key)+"\n"); } } }
			 */
		}
		return result;
	}

	public String printAttributes() {
		String res = new String();
		Iterator parIter = this.getParagraphs().iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			res += par.printAttributes() + "\n";
		}
		return res;
	}

	/**
	 * Sets Text filter.
	 */
	public static void setFilter(Hashtable[] filter) {
		DataFilter.setFilter("jcolibri.extensions.textual.representation.Text",
				filter);
	}

	/**
	 * Returns Text filter.
	 */
	public static Hashtable[] getFilter() {
		return DataFilter
				.getFilter("jcolibri.extensions.textual.representation.Text");
	}

}