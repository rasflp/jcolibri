package jcolibri.extensions.textual.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Sentence;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;
import opennlp.common.xml.NLPDocument;

import org.jdom.Element;

/**
 * Filters the text removing stop-words and special characters.
 * <p>
 * This method is divided in 4 steps:
 * <ul>
 * <li>Sentence Detector. It uses Opennlp MaxEnt Model:
 * opennlp.grok.preprocess.sentdetect.EnglishSentenceDetectorME
 * <li>Tokenizer. First it uses a standard java StringTokenizer and after that
 * uses again the Opennlp package through
 * opennlp.grok.preprocess.tokenize.EnglishTokenizerME
 * <li>StopWordsFilter. This step uses a simple stopword list to perform the
 * classification
 * <li>CreateIndexes. It links each token with its corresponding word stored in
 * the paragraph raw data.
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class WordsFilterMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	opennlp.grok.preprocess.tokenize.TokenizerME opennlpTok;

	opennlp.grok.preprocess.sentdetect.EnglishSentenceDetectorME englishSentenceDetector;

	java.util.HashSet stopwordsSet;

	/**
	 * Constructor.
	 */
	public WordsFilterMethod() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {

		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"WordsFilterMethod BEGIN");

		opennlpTok = new opennlp.grok.preprocess.tokenize.EnglishTokenizerME();
		englishSentenceDetector = new opennlp.grok.preprocess.sentdetect.EnglishSentenceDetectorME();

		stopwordsSet = new java.util.HashSet(java.util.Arrays.asList(stopWords));

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
			tokenizer(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);

		opennlpTok = null;
		englishSentenceDetector = null;
		stopwordsSet = null;

		jcolibri.util.CBRLogger
				.log(this.getClass(), 0, "WordsFilterMethod END");
		return context;
	}

	void tokenizer(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();
			sentenceDetector(t);
			Tokenizer(t);
			StopWordsFilter(t);
			createIndexes(t);

			NLPDocument opennlpDoc = createOpennlpDoc(t);
			t.setData(Text.NLPDOC, opennlpDoc);
		}
	}

	protected NLPDocument createOpennlpDoc(Text t) {
		opennlp.common.xml.NLPDocument opennlpDoc = new opennlp.common.xml.NLPDocument();
		Element root = opennlpDoc.getRootElement();

		Iterator parIter = t.getParagraphs().iterator();
		while (parIter.hasNext()) {
			Paragraph par = (Paragraph) parIter.next();
			Element xmlpar = new Element(NLPDocument.PARAGRAPH_LABEL);
			root.addContent(xmlpar);

			Iterator sentIter = par.getSentences().iterator();
			while (sentIter.hasNext()) {
				Sentence sent = (Sentence) sentIter.next();
				Element xmlsent = new Element(NLPDocument.SENTENCE_LABEL);
				xmlpar.addContent(xmlsent);

				Iterator tokIter = sent.getTokens().iterator();
				while (tokIter.hasNext()) {
					Token tok = (Token) tokIter.next();
					Element xmltok = NLPDocument.createTOK((String) tok
							.getData(Token.COMPLETEWORD));
					xmlsent.addContent(xmltok);
				}
			}
		}

		// System.out.println(opennlpDoc.toXml());
		return opennlpDoc;
	}

	protected void createIndexes(
			jcolibri.extensions.textual.representation.Text t) {
		Iterator it = t.getParagraphs().iterator();
		while (it.hasNext()) {
			Paragraph par = (Paragraph) it.next();
			String _p = (String) par.getData(Paragraph.RAW_DATA);
			Iterator itS = par.getSentences().iterator();
			while (itS.hasNext()) {
				Sentence sent = (Sentence) itS.next();
				String _s = (String) sent.getData(Sentence.RAW_DATA);
				int sentIndex = _p.indexOf(_s);
				sent
						.setData(Sentence.SENTENCE_POSITION, new Integer(
								sentIndex));

				Iterator itT = sent.getTokens().iterator();
				while (itT.hasNext()) {
					Token tok = (Token) itT.next();
					String _w = (String) tok.getData(Token.COMPLETEWORD);
					tok.setData(Token.WORDPOSITION, new Integer(_p.indexOf(_w,
							sentIndex)));
					sentIndex += _w.length() + 1;
				}
			}
		}
	}

	protected void sentenceDetector(
			jcolibri.extensions.textual.representation.Text t) {
		Iterator it = t.getParagraphs().iterator();
		while (it.hasNext()) {
			Paragraph par = (Paragraph) it.next();
			String raw = (String) par.getData(Paragraph.RAW_DATA);
			opennlp.common.xml.NLPDocument opennlpDoc = new opennlp.common.xml.NLPDocument(
					raw);
			englishSentenceDetector.process(opennlpDoc);
			ArrayList sentences = new ArrayList(Arrays.asList(opennlpDoc
					.getSentences()));
			for (int s = 0; s < sentences.size(); s++) {
				String sent = (String) sentences.get(s);
				if (sent.endsWith(" ."))
					sent = sent.substring(0, sent.length() - 2);
				if (sent.endsWith("."))
					sent = sent.substring(0, sent.length() - 1);
				par.addSentence(new Sentence(sent));
			}
		}
	}

	protected void Tokenizer(Text t) {
		Iterator iter = t.getSentences().iterator();
		int index = 0;
		while (iter.hasNext()) {
			Sentence _sentence = (Sentence) iter.next();
			String sent = (String) _sentence.getData(Sentence.RAW_DATA);
			String firstFilter = "";
			StringTokenizer st = new StringTokenizer(sent, signs);
			while (st.hasMoreTokens())
				firstFilter += st.nextToken() + " ";

			String[] tokenSA = opennlpTok.tokenize(firstFilter);
			for (int i = 0; i < tokenSA.length; i++) {
				Token tok = new Token();
				tok.setData(Token.COMPLETEWORD, tokenSA[i]);
				tok.setData(Token.TOKENINDEX, new Integer(index++));
				_sentence.addToken(tok);
			}
		}
	}

	protected void StopWordsFilter(Text t) {
		Collection col = t.getTokensList();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			Token tok = (Token) iter.next();
			String word = (String) tok.getData(Token.COMPLETEWORD);
			word = word.toLowerCase();
			if (this.stopwordsSet.contains(word))
				tok.setData(Token.ISNOT_STOPWORD, new Boolean(false));
			else
				tok.setData(Token.ISNOT_STOPWORD, new Boolean(true));
		}
	}

	public static void main(String[] args) {
		try {
			java.io.BufferedReader reader = new java.io.BufferedReader(
					new java.io.FileReader(args[0]));
			java.io.BufferedWriter writer = new java.io.BufferedWriter(
					new java.io.FileWriter(args[0] + ".out"));

			java.util.HashSet set = new java.util.HashSet();

			writer.write("String[] stopWords = {\n");
			String cad = reader.readLine();
			while (cad != null) {
				if (cad.length() > 0)
					if (!set.contains(cad)) {
						writer.write('"' + cad + '"' + ",\n");
						set.add(cad);
					}
				cad = reader.readLine();
			}
			writer.write("}\n");
			reader.close();
			writer.close();
		} catch (Exception e) {
			System.out.println("Error" + e.getMessage());
		}
	}

	static String signs = ",;.:_{}[]+*¡¿?=)(/&%$· ";

	static String[] stopWords = { "a", "a's", "able", "about", "above",
			"according", "accordingly", "across", "actually", "after",
			"afterwards", "again", "against", "ain't", "all", "allow",
			"allows", "almost", "alone", "along", "already", "also",
			"although", "always", "am", "among", "amongst", "an", "and",
			"another", "any", "anybody", "anyhow", "anyone", "anything",
			"anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
			"appropriate", "are", "aren't", "around", "as", "aside", "ask",
			"asking", "associated", "at", "available", "away", "awfully", "b",
			"be", "became", "because", "become", "becomes", "becoming", "been",
			"before", "beforehand", "behind", "being", "believe", "below",
			"beside", "besides", "best", "better", "between", "beyond", "both",
			"brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't",
			"cannot", "cant", "cause", "causes", "certain", "certainly",
			"changes", "clearly", "co", "com", "come", "comes", "concerning",
			"consequently", "consider", "considering", "contain", "containing",
			"contains", "corresponding", "could", "couldn't", "course",
			"currently", "d", "definitely", "described", "despite", "did",
			"didn't", "different", "do", "does", "doesn't", "doing", "don't",
			"done", "down", "downwards", "during", "e", "each", "edu", "eg",
			"eight", "either", "else", "elsewhere", "enough", "entirely",
			"especially", "et", "etc", "even", "ever", "every", "everybody",
			"everyone", "everything", "everywhere", "ex", "exactly", "example",
			"except", "f", "far", "few", "fifth", "first", "five", "followed",
			"following", "follows", "for", "former", "formerly", "forth",
			"four", "from", "further", "furthermore", "g", "get", "gets",
			"getting", "given", "gives", "go", "goes", "going", "gone", "got",
			"gotten", "greetings", "h", "had", "hadn't", "happens", "hardly",
			"has", "hasn't", "have", "haven't", "having", "he", "he's",
			"hello", "help", "hence", "her", "here", "here's", "hereafter",
			"hereby", "herein", "hereupon", "hers", "herself", "hi", "him",
			"himself", "his", "hither", "hopefully", "how", "howbeit",
			"however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if",
			"ignored", "immediate", "in", "inasmuch", "inc", "indeed",
			"indicate", "indicated", "indicates", "inner", "insofar",
			"instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll",
			"it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept",
			"know", "knows", "known", "l", "last", "lately", "later", "latter",
			"latterly", "least", "less", "lest", "let", "let's", "like",
			"liked", "likely", "little", "look", "looking", "looks", "ltd",
			"m", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile",
			"merely", "might", "more", "moreover", "most", "mostly", "much",
			"must", "my", "myself", "n", "name", "namely", "nd", "near",
			"nearly", "necessary", "need", "needs", "neither", "never",
			"nevertheless", "new", "next", "nine", "no", "nobody", "non",
			"none", "noone", "nor", "normally", "not", "nothing", "novel",
			"now", "nowhere", "o", "obviously", "of", "off", "often", "oh",
			"ok", "okay", "old", "on", "once", "one", "ones", "only", "onto",
			"or", "other", "others", "otherwise", "ought", "our", "ours",
			"ourselves", "out", "outside", "over", "overall", "own", "p",
			"particular", "particularly", "per", "perhaps", "placed", "please",
			"plus", "possible", "presumably", "probably", "provides", "q",
			"que", "quite", "qv", "r", "rather", "rd", "re", "really",
			"reasonably", "regarding", "regardless", "regards", "relatively",
			"respectively", "right", "s", "said", "same", "saw", "say",
			"saying", "says", "second", "secondly", "see", "seeing", "seem",
			"seemed", "seeming", "seems", "seen", "self", "selves", "sensible",
			"sent", "serious", "seriously", "seven", "several", "shall", "she",
			"should", "shouldn't", "since", "six", "so", "some", "somebody",
			"somehow", "someone", "something", "sometime", "sometimes",
			"somewhat", "somewhere", "soon", "sorry", "specified", "specify",
			"specifying", "still", "sub", "such", "sup", "sure", "t", "t's",
			"take", "taken", "tell", "tends", "th", "than", "thank", "thanks",
			"thanx", "that", "that's", "thats", "the", "their", "theirs",
			"them", "themselves", "then", "thence", "there", "there's",
			"thereafter", "thereby", "therefore", "therein", "theres",
			"thereupon", "these", "they", "they'd", "they'll", "they're",
			"they've", "think", "third", "this", "thorough", "thoroughly",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "to", "together", "too", "took", "toward", "towards",
			"tried", "tries", "truly", "try", "trying", "twice", "two", "u",
			"un", "under", "unfortunately", "unless", "unlikely", "until",
			"unto", "up", "upon", "us", "use", "used", "useful", "uses",
			"using", "usually", "uucp", "v", "value", "various", "very", "via",
			"viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we",
			"we'd", "we'll", "we're", "we've", "welcome", "well", "went",
			"were", "weren't", "what", "what's", "whatever", "when", "whence",
			"whenever", "where", "where's", "whereafter", "whereas", "whereby",
			"wherein", "whereupon", "wherever", "whether", "which", "while",
			"whither", "who", "who's", "whoever", "whole", "whom", "whose",
			"why", "will", "willing", "wish", "with", "within", "without",
			"won't", "wonder", "would", "wouldn't", "x", "y", "yes", "yet",
			"you", "you'd", "you'll", "you're", "you've", "your", "yours",
			"yourself", "yourselves", "z", "zero", "albeit", "author", "av",
			"canst", "cf", "cfrd", "choose", "conducted", "considered",
			"contrariwise", "cos", "crd", "cu", "day", "describes", "designed",
			"determine", "determined", "discussed", "dost", "doth", "double",
			"dual", "due", "excepted", "excepting", "exception", "exclude",
			"excluding", "exclusive", "farther", "farthest", "ff", "forward",
			"found", "front", "furthest", "general", "halves", "hast", "hath",
			"henceforth", "hereabouts", "hereto", "hindmost", "hitherto",
			"howsoever", "I", "include", "included", "including", "indoors",
			"inside", "insomuch", "investigated", "inwards", "kind", "kg",
			"km", "made", "meantime", "mr", "mrs", "ms", "nonetheless", "nope",
			"notwithstandi", "ng", "nowadays", "obtained", "performance",
			"performed", "plenty", "present", "presented", "presents",
			"provide", "provided", "related", "report", "required", "results",
			"round", "sake", "sang", "save", "seldom", "selected", "sfrd",
			"shalt", "shown", "sideways", "significant", "slept", "slew",
			"slung", "slunk", "smote", "spake", "spat", "spoke", "spoken",
			"sprang", "sprung", "srd", "stave", "staves", "studies",
			"supposing", "tested", "thee", "thenceforth", "thereabout",
			"thereabouts", "thereof", "thereon", "thereto", "thou", "thrice",
			"thy", "thyself", "till", "types", "unable", "underneath",
			"unlike", "upward", "upwards", "week", "whatsoever", "whensoever",
			"whereabouts", "whereat", "wherefore", "wherefrom", "whereinto",
			"whereof", "whereon", "wheresoever", "whereto", "whereunto",
			"wherewith", "whew", "whichever", "whichsoevr", "whilst", "whoa",
			"whomever", "whomsoever", "whosoever", "wilt", "worse", "worst",
			"wow", "ye", "year", "yippee" };

}