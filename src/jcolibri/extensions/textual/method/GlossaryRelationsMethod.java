package jcolibri.extensions.textual.method;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.representation.WeightedRelation;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;

/**
 * <p>
 * Relates query words to cases words using a domain specific glossary.
 * </p>
 * <p>
 * Tokens are related using a list of WeightedRelation objects (Collection).
 * This Collection is stored in each Token instance using the
 * Token.RELATEDTOKENS parameter.
 * <p>
 * Glossary file is specified by GLOSSARY_FILE
 * </p>
 * <p>
 * Glossary Format:
 * <p>
 * [Part-of-Speech Tag]{Similarity} word1 word2 ... wordn
 * <ul>
 * <li>Part-of-Speech Tag: Sometimes words can have different POS tags, this
 * parameter marks that the following words are only related when they appear in
 * a sentence with that tag.
 * <p>
 * Possible values: NOUN, VERB, ADJECTIVE, ADVERB
 * <li>Similarity: Indicates the similarity relation.
 * <p>
 * Possible values: 1, 2, 3. (1 - very similar, 2 - similar, 3 - not very
 * similar)
 * <li>Words must be separated with spaces.
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see jcolibri.extensions.textual.representation.WeightedRelation
 * @see jcolibri.extensions.textual.representation.Token
 */
public class GlossaryRelationsMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	public static final String GLOSSARY_FILE = "Glossary file";

	ArrayList _gList;

	/**
	 * Constructor.
	 */
	public GlossaryRelationsMethod() {
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes
	 * findGlossaryRelations() with the query and a case
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {

		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"GlossaryRelationsMethod BEGIN");
		ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
				.getCases().size());

		ArrayList _queryTexts = new ArrayList();
		TextualObjectUtils.extractText(context.getQuery(), _queryTexts);

		loadGlossary();

		Iterator iter = context.getCases().iterator();
		while (iter.hasNext()) {
			Individual _case = (Individual) iter.next();
			ArrayList _caseTexts = new ArrayList();
			TextualObjectUtils.extractText(_case, _caseTexts);
			if (_queryTexts.size() != _caseTexts.size())
				throw new jcolibri.cbrcore.exception.DataInconsistencyException(
						"Query & Case Textual Attributes are different");
			for (int t = 0; t < _queryTexts.size(); t++)
				findGlossaryRelations((Text) _queryTexts.get(t),
						(Text) _caseTexts.get(t));
			ProgressBar.PROGRESSBAR.step();
		}
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"GlossaryRelationsMethod END");
		_gList = null;
		ProgressBar.PROGRESSBAR.setVisible(false);
		return context;
	}

	/**
	 * Finds glossary relations between query tokens and case tokens
	 * 
	 * @param qText
	 *            Query text
	 * @param cText
	 *            Case text
	 */
	void findGlossaryRelations(Text qText, Text cText) {
		Hashtable[] f = new Hashtable[1];
		Hashtable filter = new Hashtable();
		filter.put(Token.ISNOT_STOPWORD, new Boolean(true));
		f[0] = filter;
		Token.setFilter(f);
		Collection qTokens = qText.getTokensList();
		Collection cTokens = cText.getTokensList();
		Token.setFilter(null);

		for (Iterator qTIter = qTokens.iterator(); qTIter.hasNext();) {
			Token qTok = (Token) qTIter.next();

			// this, tries to skip the next bucle
			String pos = lookupGlossaryPos(((String) qTok.getData(Token.POSTAG)));
			if (pos == null)
				continue;
			boolean skip = true;
			for (int i = 0; i < _gList.size(); i++) {
				GlossaryTriple gt = (GlossaryTriple) _gList.get(i);
				if (gt._words.contains((String) qTok
						.getData(Token.COMPLETEWORD)))
					skip = false;
			}
			if (skip)
				continue;

			String qWord = ((String) qTok.getData(Token.STEMMEDWORD));
			for (Iterator cTIter = cTokens.iterator(); cTIter.hasNext();) {
				Token cTok = (Token) cTIter.next();
				String cWord = (String) cTok.getData(Token.STEMMEDWORD);

				if (qWord.equals(cWord))
					continue;

				int res = GlossaryQuery(qTok, cTok);

				if (res > 3)
					continue;

				Hashtable related = (Hashtable) qTok
						.getData(Token.RELATEDTOKENS);
				if (related == null) {
					related = new Hashtable();
					qTok.setData(Token.RELATEDTOKENS, related);
				}
				System.out.println("Adding Synonymous Relation: "
						+ (String) qTok.getData(Token.COMPLETEWORD) + " <--> "
						+ (String) cTok.getData(Token.COMPLETEWORD));
				related.put(qTok, new WeightedRelation(qTok, cTok,
						1 - ((res - 1) / 2)));
			}
		}
	}

	/**
	 * Look up relations between both tokens using the loaded glossary
	 * 
	 * @return An integer between {1,2,3} if both tokens are related or 10 if
	 *         not.
	 */
	int GlossaryQuery(Token tok1, Token tok2) {
		try {
			String completeWord1 = ((String) tok1.getData(Token.STEMMEDWORD))
					.toLowerCase();
			String completeWord2 = ((String) tok2.getData(Token.STEMMEDWORD))
					.toLowerCase();
			String pos1 = lookupGlossaryPos((String) tok1.getData(Token.POSTAG));
			String pos2 = lookupGlossaryPos((String) tok2.getData(Token.POSTAG));
			if (pos1 == null)
				return 10;
			if (pos2 == null)
				return 10;
			if (!pos1.equals(pos2))
				return 10;

			for (int i = 0; i < _gList.size(); i++) {
				GlossaryTriple gt = (GlossaryTriple) _gList.get(i);
				if (!gt._posTag.equals(pos1))
					continue;
				if (gt._words.contains(completeWord1))
					if (gt._words.contains(completeWord2))
						return gt._weight;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 10;
	}

	/**
	 * Load glossary reations stored in GLOSSARY_FILE
	 */
	void loadGlossary() {
		_gList = new ArrayList();
		String filename = "glossary.txt";
		try {
			String aux = (String) this
					.getParameterValue(GlossaryRelationsMethod.GLOSSARY_FILE);
			if (aux != null)
				filename = aux;
		} catch (Exception e) {
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (Exception e) {
			System.out.println("File not found: " + filename + " "
					+ e.getMessage());
		}
		if (br == null)
			return;

		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				int pos = line.indexOf(']');
				if (pos == -1)
					throw new Exception(line + "  POSTag field not found");
				String _posTag = line.substring(1, pos);
				String _rest = line.substring(pos + 1);
				pos = _rest.indexOf('}');
				if (pos == -1)
					throw new Exception(line + "  Weight field not found");
				String _weight = _rest.substring(1, pos);
				int w = Integer.parseInt(_weight);
				String _words = _rest.substring(pos + 1);
				StringTokenizer st = new StringTokenizer(_words, " ");
				ArrayList words = new ArrayList();
				while (st.hasMoreTokens()) {
					String sw = st.nextToken();
					words.add(StemmerMethod.Stemm(sw));
				}

				_gList.add(new GlossaryTriple(_posTag, words, w));
			}
			br.close();
		} catch (Exception e) {
			System.out
					.println("Error parsing Glossary file: " + e.getMessage());
		}

	}

	/**
	 * This method transforms POS tags defined in PartofSpeechMethod to the tags
	 * used in the glossary file
	 * 
	 * @param tag
	 *            POS tag
	 * @return NOUN, VERB, ADJECTIVE or ADVERB
	 */
	String lookupGlossaryPos(String tag) {
		/*
		 * 12. NN Noun, singular or mass 13. NNS Noun, plural
		 */
		if (tag.equals("NN") || tag.equals("NNS"))
			return "NOUN";
		/*
		 * 27. VB Verb, base form 28. VBD Verb, past tense 29. VBG Verb, gerund
		 * or present participle 30. VBN Verb, past participle 31. VBP Verb,
		 * non-3rd person singular present 32. VBZ Verb, 3rd person singular
		 * present
		 */
		if (tag.startsWith("V"))
			return "VERB";

		/*
		 * 7. JJ Adjective 8. JJR Adjective, comparative 9. JJS Adjective,
		 * superlative
		 */
		if (tag.startsWith("J"))
			return "ADJECTIVE";

		/*
		 * 20. RB Adverb 21. RBR Adverb, comparative 22. RBS Adverb, superlative
		 */
		if (tag.startsWith("RB"))
			return "ADVERB";

		return null;
	}

	private class GlossaryTriple {
		String _posTag;

		Collection _words;

		int _weight;

		GlossaryTriple(String p, Collection wor, int w) {
			_posTag = p;
			_words = wor;
			_weight = w;
		}
	}
}