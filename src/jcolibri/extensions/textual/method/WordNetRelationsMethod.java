package jcolibri.extensions.textual.method;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.representation.WeightedRelation;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.relationship.RelationshipFinder;

/**
 * <p>
 * Relates query words to cases words using WordNet. (So you should have WordNet
 * installed in your computer).
 * </p>
 * <p>
 * Tokens are related using a list of WeightedRelation objects (Collection).
 * This Collection is stored in each Token instance using the
 * Token.RELATEDTOKENS parameter.
 * <p>
 * This method uses Java WordNet Library <a
 * href="http://sourceforge.net/projects/jwordnet">http://sourceforge.net/projects/jwordnet</a>
 * to query WordNet. This package should read a xml configuration file where you
 * have to customize your WordNet instalation. You must select your config file
 * with the config_path parameter.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class WordNetRelationsMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	// private static final String config_path =
	// "lib\\jwnl\\file_properties.xml";
	private static final String config_path = "examples\\TextualCBR\\jwnl\\file_properties.xml";

	int VERTICALWEIGHT = 3;

	int HORIZONTALWEIGHT = 2;

	/**
	 * Constructor.
	 */
	public WordNetRelationsMethod() {
	}

	/**
	 * If two words exceed this limit they won't be considered similar.
	 */
	public static final String SIMILARITY_LIMIT = "similarity limit";

	/**
	 * Iterates over all cases extracting their Texts and invokes
	 * findWordNetRelations() with the query and a case
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		try {
			// initialize JWNL (this must be done before JWNL can be used)
			JWNL.initialize(new FileInputStream(config_path));
		} catch (Exception e) {
			jcolibri.util.CBRLogger.log(this.getClass(),
					jcolibri.util.CBRLogger.ERROR,
					"WordNet Configuration Error");
			e.printStackTrace();
		}

		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"WordNetRelationsMethod BEGIN");
		ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
				.getCases().size());

		ArrayList _queryTexts = new ArrayList();
		TextualObjectUtils.extractText(context.getQuery(), _queryTexts);

		Iterator iter = context.getCases().iterator();
		while (iter.hasNext()) {
			Individual _case = (Individual) iter.next();
			ArrayList _caseTexts = new ArrayList();
			TextualObjectUtils.extractText(_case, _caseTexts);
			if (_queryTexts.size() != _caseTexts.size())
				throw new jcolibri.cbrcore.exception.DataInconsistencyException(
						"Query & Case Textual Attributes are different");
			for (int t = 0; t < _queryTexts.size(); t++)
				findWordNetRelations((Text) _queryTexts.get(t),
						(Text) _caseTexts.get(t));
			ProgressBar.PROGRESSBAR.step();
		}
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"WordNetRelationsMethod END");
		ProgressBar.PROGRESSBAR.setVisible(false);
		return context;
	}

	/**
	 * Finds WordNet relations between query tokens and case tokens
	 * 
	 * @param qText
	 *            Query text
	 * @param cText
	 *            Case text
	 */
	void findWordNetRelations(Text qText, Text cText) {
		int limit = 3;
		try {
			Integer Ilimit = (Integer) this
					.getParameterValue(WordNetRelationsMethod.SIMILARITY_LIMIT);
			limit = Ilimit.intValue();
		} catch (Exception e) {
		}

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
			POS pos = lookupJWNLPos(((String) qTok.getData(Token.POSTAG)));
			if (pos == null)
				continue;
			String qWord = ((String) qTok.getData(Token.STEMMEDWORD));
			for (Iterator cTIter = cTokens.iterator(); cTIter.hasNext();) {
				Token cTok = (Token) cTIter.next();
				String cWord = (String) cTok.getData(Token.STEMMEDWORD);

				if (qWord.equals(cWord))
					continue;

				int res = WordNetQuery(qTok, cTok);
				if (res < limit) {
					Hashtable related = (Hashtable) qTok
							.getData(Token.RELATEDTOKENS);
					if (related == null) {
						related = new Hashtable();
						qTok.setData(Token.RELATEDTOKENS, related);
					}
					System.out.println("Adding Synonymous Relation: "
							+ (String) qTok.getData(Token.COMPLETEWORD)
							+ " <--> "
							+ (String) cTok.getData(Token.COMPLETEWORD));
					related.put(qTok, new WeightedRelation(qTok, cTok,
							1 - (res / 10)));
				}

			}

		}
	}

	/**
	 * Look up relations between both tokens.
	 * 
	 * @return An integer between {1,2,3} if both tokens are related or 10 if
	 *         not.
	 */
	int WordNetQuery(Token tok1, Token tok2) {
		try {
			String completeWord1 = ((String) tok1.getData(Token.COMPLETEWORD))
					.toLowerCase();
			String completeWord2 = ((String) tok2.getData(Token.COMPLETEWORD))
					.toLowerCase();
			POS pos1 = lookupJWNLPos((String) tok1.getData(Token.POSTAG));
			POS pos2 = lookupJWNLPos((String) tok2.getData(Token.POSTAG));
			if (pos1 == null)
				return 10;
			if (pos2 == null)
				return 10;
			if (!pos1.getKey().equals(pos2.getKey()))
				return 10;

			IndexWord start = net.didion.jwnl.dictionary.Dictionary
					.getInstance().lookupIndexWord(pos1, completeWord1);
			IndexWord end = net.didion.jwnl.dictionary.Dictionary.getInstance()
					.lookupIndexWord(pos2, completeWord2);

			if (start == null)
				return 10;
			if (end == null)
				return 10;

			int INMEDIATEdistance = RelationshipFinder.getInstance()
					.getImmediateRelationship(start, end);

			int SIMdistance = Integer.MAX_VALUE;
			int HYPdistance = Integer.MAX_VALUE;
			/*
			 * Synset[] w1 = start.getSenses(); Synset[] w2 = end.getSenses();
			 * for(int i1 = 0; i1<w1.length; i1++) for(int i2 = 0; i2<w2.length;
			 * i2++) { Synset s1 = w1[i1]; Synset s2 = w2[i2];
			 * 
			 * RelationshipList list =
			 * RelationshipFinder.getInstance().findRelationships(s1, s2,
			 * PointerType.SIMILAR_TO); if(!list.isEmpty()) { for (Iterator itr =
			 * list.iterator(); itr.hasNext();) { Relationship rel =
			 * (Relationship)itr.next(); int depth = rel.getDepth(); if(depth<SIMdistance) {
			 * SIMdistance = depth; // System.out.println("Relacion Encontrada:
			 * SIMILARTO"); // rel.getNodeList().print(); } } }
			 * 
			 * list = RelationshipFinder.getInstance().findRelationships(s1, s2,
			 * PointerType.HYPERNYM); if(!list.isEmpty()) { for (Iterator itr =
			 * list.iterator(); itr.hasNext();) { Relationship rel =
			 * (Relationship)itr.next(); int depth = rel.getDepth(); if(depth<HYPdistance) {
			 * HYPdistance = depth; // System.out.println("Relacion Encontrada:
			 * HYPERNYM"); // rel.getNodeList().print(); } } } }
			 */
			// System.out.println("InmediateRelationship " + start.getLemma() +
			// " and " + end.getLemma()+ ": "+INMEDIATEdistance);
			// System.out.println("SIMILARTO MinDistance " + start.getLemma() +
			// " and " + end.getLemma()+ ": "+SIMdistance);
			// System.out.println("HYPERNYM MinDistance " + start.getLemma() + "
			// and " + end.getLemma()+ ": "+HYPdistance);
			if (INMEDIATEdistance > -1)
				return INMEDIATEdistance;
			else if (SIMdistance < 3)
				return 2 * SIMdistance;
			else if (HYPdistance < 3)
				return 3 * HYPdistance;
			else
				return 10;
		} catch (Exception e) {
			jcolibri.util.CBRLogger.log(this.getClass(),
					jcolibri.util.CBRLogger.ERROR, "WordNet Query Error: "
							+ e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return 10;
	}

	/**
	 * This method transforms POS tags defined in PartofSpeechMethod to the tags
	 * used by WordNet
	 * 
	 * @param tag
	 *            POS tag
	 * @return POS object defined in JWNL
	 */
	POS lookupJWNLPos(String tag) {
		/*
		 * 12. NN Noun, singular or mass 13. NNS Noun, plural
		 */
		if (tag.equals("NN") || tag.equals("NNS"))
			return POS.NOUN;
		/*
		 * 27. VB Verb, base form 28. VBD Verb, past tense 29. VBG Verb, gerund
		 * or present participle 30. VBN Verb, past participle 31. VBP Verb,
		 * non-3rd person singular present 32. VBZ Verb, 3rd person singular
		 * present
		 */
		if (tag.startsWith("V"))
			return POS.VERB;

		/*
		 * 7. JJ Adjective 8. JJR Adjective, comparative 9. JJS Adjective,
		 * superlative
		 */
		if (tag.startsWith("J"))
			return POS.ADJECTIVE;

		/*
		 * 20. RB Adverb 21. RBR Adverb, comparative 22. RBS Adverb, superlative
		 */
		if (tag.startsWith("RB"))
			return POS.ADVERB;

		return null;
	}
}