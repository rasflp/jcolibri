package jcolibri.extensions.textual.method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;

/**
 * <p>
 * This method can perform several stemmer algorithms for diferent languages.
 * <p>
 * Default language is English, but developers can choose another language
 * configuration using the LANGUAGE_ATTRIBUTE parameter
 * </p>
 * <p>
 * Although other languages are providen, actually you must use English because
 * the remainder layers only works with this language
 * </p>
 * <p>
 * This method uses the SnowBall package: <a
 * href="http://snowball.tartarus.org">http://snowball.tartarus.org</a>
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class StemmerMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	public static final int DANISH = 1;

	public static final int DUTCH = 2;

	public static final int ENGLISH = 3;

	public static final int FINNISH = 4;

	public static final int FRENCH = 5;

	public static final int GERMAN = 6;

	public static final int ITALIAN = 7;

	public static final int NORWEGIAN = 8;

	public static final int PORTUGUESE = 9;

	public static final int RUSSIAN = 10;

	public static final int SPANISH = 11;

	public static final int SWEDISH = 12;

	public static final String LANGUAGE_ATTRIBUTE = "language";

	static int language;

	static net.sf.snowball.SnowballProgram _stemmer;

	/**
	 * Static method used to stemm words after the execution of this method.
	 * <p>
	 * So you can only call this method after the call to execute()
	 * <p>
	 * Exactly, this method is used by the glossary method.
	 * 
	 * @param word
	 *            Word to stemm
	 * @return Stemmed word
	 */
	static String Stemm(String word) {
		if (_stemmer == null)
			return word;
		_stemmer.setCurrent(word.toLowerCase());
		_stemmer.stem();
		return _stemmer.getCurrent();
	}

	/**
	 * Constructor.
	 */
	public StemmerMethod() {
		language = StemmerMethod.ENGLISH;
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes stemmer()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0, "StemmerMethod BEGIN");
		_stemmer = chooseStemmer();

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
			stemmer(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);

		_stemmer = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0, "StemmerMethod END");
		return context;
	}

	private net.sf.snowball.SnowballProgram chooseStemmer() {
		try {
			Integer newLanguage = (Integer) this
					.getParameterValue(StemmerMethod.LANGUAGE_ATTRIBUTE);
			if (newLanguage != null)
				language = newLanguage.intValue();
		} catch (Exception e) {
		}

		net.sf.snowball.SnowballProgram _stemmer;
		if (language == StemmerMethod.DANISH)
			_stemmer = new net.sf.snowball.ext.danishStemmer();
		else if (language == StemmerMethod.DUTCH)
			_stemmer = new net.sf.snowball.ext.dutchStemmer();
		else if (language == StemmerMethod.ENGLISH)
			_stemmer = new net.sf.snowball.ext.englishStemmer();
		else if (language == StemmerMethod.FINNISH)
			_stemmer = new net.sf.snowball.ext.finnishStemmer();
		else if (language == StemmerMethod.FRENCH)
			_stemmer = new net.sf.snowball.ext.frenchStemmer();
		else if (language == StemmerMethod.GERMAN)
			_stemmer = new net.sf.snowball.ext.germanStemmer();
		else if (language == StemmerMethod.ITALIAN)
			_stemmer = new net.sf.snowball.ext.italianStemmer();
		else if (language == StemmerMethod.NORWEGIAN)
			_stemmer = new net.sf.snowball.ext.norwegianStemmer();
		else if (language == StemmerMethod.PORTUGUESE)
			_stemmer = new net.sf.snowball.ext.portugueseStemmer();
		else if (language == StemmerMethod.RUSSIAN)
			_stemmer = new net.sf.snowball.ext.russianStemmer();
		else if (language == StemmerMethod.SPANISH)
			_stemmer = new net.sf.snowball.ext.spanishStemmer();
		else if (language == StemmerMethod.SWEDISH)
			_stemmer = new net.sf.snowball.ext.swedishStemmer();
		else
			_stemmer = new net.sf.snowball.ext.englishStemmer();

		return _stemmer;
	}

	/**
	 * Stemms the words contained in the texts and stores the stemmed word in
	 * each token using the Tokens.STEMMEDWORD attribute
	 * 
	 * @param texts
	 *            Collection of Texts objects
	 */
	void stemmer(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();

			Iterator wIt = t.getTokensList().iterator();

			while (wIt.hasNext()) {
				Token tok = (Token) wIt.next();
				String word = (String) tok.getData(Token.COMPLETEWORD);
				_stemmer.setCurrent(word.toLowerCase());
				_stemmer.stem();
				tok.setData(Token.STEMMEDWORD, _stemmer.getCurrent());
			}
		}
	}

}