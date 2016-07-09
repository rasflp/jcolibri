package jcolibri.extensions.textual.method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;

/**
 * <p>
 * This method uses a Maximum Entropy tagger (Opennlp) to assign Part-of-Speech
 * tags.
 * </p>
 * <p>
 * Tags are stored in Tokens using the Tokens.POSSTAG parameter
 * </p>
 * <p>
 * Possible values are:
 * </p>
 * <ul>
 * <li> 1. CC Coordinating conjunction
 * <li> 2. CD Cardinal number
 * <li> 3. DT Determiner
 * <li> 4. EX Existential there
 * <li> 5. FW Foreign word
 * <li> 6. IN Preposition or subordinating conjunction
 * <li> 7. JJ Adjective
 * <li> 8. JJR Adjective, comparative
 * <li> 9. JJS Adjective, superlative
 * <li> 10. LS List item marker
 * <li> 11. MD Modal
 * <li> 12. NN Noun, singular or mass
 * <li> 13. NNS Noun, plural
 * <li> 14. NNP Proper noun, singular
 * <li> 15. NNPS Proper noun, plural
 * <li> 16. PDT Predeterminer
 * <li> 17. POS Possessive ending
 * <li> 18. PRP Personal pronoun
 * <li> 19. PRP$ Possessive pronoun
 * <li> 20. RB Adverb
 * <li> 21. RBR Adverb, comparative
 * <li> 22. RBS Adverb, superlative
 * <li> 23. RP Particle
 * <li> 24. SYM Symbol
 * <li> 25. TO to
 * <li> 26. UH Interjection
 * <li> 27. VB Verb, base form
 * <li> 28. VBD Verb, past tense
 * <li> 29. VBG Verb, gerund or present participle
 * <li> 30. VBN Verb, past participle
 * <li> 31. VBP Verb, non-3rd person singular present
 * <li> 32. VBZ Verb, 3rd person singular present
 * <li> 33. WDT Wh-determiner
 * <li> 34. WP Wh-pronoun
 * <li> 35. WP$ Possessive wh-pronoun
 * <li> 36. WRB Wh-adverb
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class PartofSpeechMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	opennlp.grok.preprocess.postag.EnglishPOSTaggerME _tagger;

	/**
	 * Constructor.
	 */
	public PartofSpeechMethod() {
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes tagger()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {

		jcolibri.util.CBRLogger.log(this.getClass(), 0, "POSMethod BEGIN");

		_tagger = new opennlp.grok.preprocess.postag.EnglishPOSTaggerME();

		Boolean processQuery = (Boolean) this
				.getParameterValue("Process Query");
		Boolean processCases = (Boolean) this
				.getParameterValue("Process Cases");
		ArrayList cases = new ArrayList();
		if (processQuery.booleanValue())
			cases.add(context.getQuery());
		if (processCases.booleanValue())
			cases.addAll(context.getCases());

		ProgressBar.PROGRESSBAR.init(this.getClass().getName(), cases.size());
		Iterator iter = cases.iterator();
		while (iter.hasNext()) {
			Individual _case = (Individual) iter.next();
			ArrayList _texts = new ArrayList();
			TextualObjectUtils.extractText(_case, _texts);
			tagger(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		_tagger = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0, "POSMethod END");
		return context;
	}

	/**
	 * Performs POS tag algorithm using
	 * opennlp.grok.preprocess.postag.EnglishPOSTaggerME
	 * 
	 * @param texts
	 */
	void tagger(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();

			opennlp.common.xml.NLPDocument doc = (opennlp.common.xml.NLPDocument) t
					.getData(Text.NLPDOC);
			_tagger.process(doc);

			List elements = doc.getWordElements();
			Collection tokens = t.getTokensList();
			Iterator tokenIter = tokens.iterator();
			if (elements.size() != tokens.size())
				throw new jcolibri.cbrcore.exception.DataInconsistencyException(
						"Part-of-Speech error: Insconsistency between opennlpDoc & native representation");
			for (int e = 0; e < elements.size(); e++) {
				org.jdom.Element tag = (org.jdom.Element) elements.get(e);
				String posTag = tag.getAttributeValue("pos");
				Token tok = (Token) tokenIter.next();
				tok.setData(Token.POSTAG, posTag);
			}
		}
	}

}