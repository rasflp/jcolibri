package jcolibri.extensions.textual.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import jcolibri.extensions.textual.method.ExtractFeaturesMethod.FeatureInfo;
import jcolibri.extensions.textual.method.ExtractPhrasesMethod.PhraseInfo;

/**
 * <p>
 * This class represents a Paragraph.
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
 * <li>SENTENCES: Collection of sentences (Sentence class instances) that
 * compose this paragraph.
 * <li>FEATURES: Features extracted by ExtractFeaturesMethod. It is a
 * Collection of ExtractFeaturesMethod.FeatureInfo objects.
 * <li>PHRASES: Phrases extracted by ExtractPhrasesMethod. It is a Collection
 * of ExtracPhrasesMethod.PhraseInfo objects.
 * <li>TOPICS: Topics obtained by DomainTopicClassifierMethod. It is a
 * Collection of Strings.
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class Paragraph extends DataFilter {
	public static final String RAW_DATA = "raw_data";

	public static final String SENTENCES = "sentences";

	public static final String FEATURES = "features";

	public static final String PHRASES = "phrases";

	public static final String TOPICS = "domain topics";

	/**
	 * Default constructor. Raw data is empty.
	 */
	public Paragraph() {
		this("");
	}

	/**
	 * Constructor
	 * 
	 * @param raw
	 *            Original text
	 */
	public Paragraph(String raw) {
		this.setData(Paragraph.RAW_DATA, raw);
		this.setData(Paragraph.SENTENCES, new ArrayList());
	}

	/**
	 * Constructor
	 * 
	 * @param orig
	 *            Original text
	 * @param sentences
	 *            Sentences that compose this paragraph
	 */
	public Paragraph(Paragraph orig, Collection sentences) {
		_data = new Hashtable(orig._data);
		this.setData(Paragraph.SENTENCES, sentences);
	}

	/**
	 * Adds a new sentence.
	 * 
	 * @param sen
	 *            Sentence to be added
	 */
	public void addSentence(Sentence sen) {
		Collection col = (Collection) _data.get(SENTENCES);
		col.add(sen);
	}

	/**
	 * Returns a collection with all the sentences.
	 * 
	 * @return Collection of Sentences
	 */
	public Collection getSentences() {
		return (Collection) _data.get(SENTENCES);
	}

	/**
	 * Returns a filtered copy of this paragraph
	 */
	public Paragraph getFilteredParagraph() {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(SENTENCES);
		if (col == null)
			return new Paragraph();
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Sentence sent = (Sentence) colIter.next();
			if (sent.checkConditions())
				l.add(sent.getFilteredSentence());
		}
		return new Paragraph(this, l);
	}

	/**
	 * Returns a filtered paragraph composed by sentences that contains tokens
	 * data (that comply token filter) instead of Tokens.
	 * 
	 * @param keyString
	 *            contained tokens data
	 */
	public Paragraph getFilteredParagraphData(String keyString) {
		ArrayList l = new ArrayList();
		Collection col = (Collection) getData(SENTENCES);
		if (col == null)
			return new Paragraph();
		Iterator colIter = col.iterator();
		while (colIter.hasNext()) {
			Sentence sent = (Sentence) colIter.next();
			if (sent.checkConditions())
				l.add(sent.getFilteredSentenceData(keyString));
		}
		return new Paragraph(this, l);
	}

	/**
	 * Sets Paragraph filter
	 */
	public static void setFilter(Hashtable[] filter) {
		DataFilter.setFilter(
				"jcolibri.extensions.textual.representation.Paragraph", filter);
	}

	/**
	 * Returns Paragraph filter
	 * 
	 * @return
	 */
	public static Hashtable[] getFilter() {
		return DataFilter
				.getFilter("jcolibri.extensions.textual.representation.Paragraph");
	}

	public String printAttributes() {
		String res = new String();
		Collection phrases = (Collection) this.getData(Paragraph.PHRASES);
		if (phrases != null) {
			res += "PHRASES:\n";
			for (Iterator iter = phrases.iterator(); iter.hasNext();) {
				PhraseInfo pi = (PhraseInfo) iter.next();
				res += pi._feature + "\n";
			}
		}

		Collection features = (Collection) this.getData(Paragraph.FEATURES);
		if (features != null) {
			res += "FEATURES:\n";
			for (Iterator iter = features.iterator(); iter.hasNext();) {
				FeatureInfo fi = (FeatureInfo) iter.next();
				res += fi._feature + ": " + fi._value + "\n";
			}
		}

		Collection topics = (Collection) this.getData(Paragraph.TOPICS);
		if (topics != null) {
			res += "TOPICS:\n";
			for (Iterator iter = topics.iterator(); iter.hasNext();) {
				res += (String) iter.next() + "\n";
			}
		}
		return res;
	}

}