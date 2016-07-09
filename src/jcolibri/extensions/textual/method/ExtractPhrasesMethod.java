package jcolibri.extensions.textual.method;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jcolibri.cbrcase.Individual;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.ProgressBar;

/**
 * <p>
 * Extracts Phrases using Regular Expressions.
 * </p>
 * <p>
 * This method uses rules defined in PHRASES_FILE to recognize regular
 * expressions inside text and associate a label to each one
 * </p>
 * <p>
 * Rules file format is:
 * </p>
 * <p>
 * [PhraseName]PhraseRegularExpresion
 * <ul>
 * <il>PhraseName is used to store the extracted information <il>Regular
 * Expressions are deffined following java.util.regex.Pattern syntaxis. (See API
 * for details)
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 * @see java.util.regex.Pattern
 */
public class ExtractPhrasesMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	public static final String PHRASES_FILE = "Phrases file";

	ArrayList _fList;

	/**
	 * Constructor.
	 */
	public ExtractPhrasesMethod() {
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes
	 * extractPhrases()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractPhrasesMethod BEGIN");

		loadRules();

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
			extractPhrases(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		_fList = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractPhrasesMethod END");
		return context;
	}

	/**
	 * Extracts Phrases form this collection of texts
	 * 
	 * @param texts
	 *            Text collection
	 */
	void extractPhrases(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();
			Iterator parIt = t.getParagraphs().iterator();
			while (parIt.hasNext()) {
				Paragraph par = (Paragraph) parIt.next();
				String _text = (String) par.getData(Paragraph.RAW_DATA);

				for (int r = 0; r < _fList.size(); r++) {
					Pair pair = (Pair) _fList.get(r);

					Matcher m = pair._pattern.matcher(_text);
					while (m.find()) {
						ArrayList list = (ArrayList) par
								.getData(Paragraph.PHRASES);
						if (list == null) {
							list = new ArrayList();
							par.setData(Paragraph.PHRASES, list);
						}
						list.add(new PhraseInfo(pair._feature, m.start(), m
								.end()));
						System.out.println("Phrase: " + pair._feature);
					}
				}
			}
		}
	}

	/**
	 * Load rules file located in PHRASES_FILE
	 */
	void loadRules() {
		_fList = new ArrayList();
		String filename = "phrasesRules.txt";
		try {
			String aux = (String) this
					.getParameterValue(ExtractPhrasesMethod.PHRASES_FILE);
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
					throw new Exception(line + "  Feature field not found");
				String _feature = line.substring(1, pos);
				String _rule = line.substring(pos + 1);
				_fList.add(new Pair(cleanSpaces(_feature), Pattern
						.compile(_rule)));
			}
			br.close();
		} catch (Exception e) {
			if (e instanceof PatternSyntaxException)
				System.out.println("Error parsing rule: " + line + " "
						+ e.getMessage());
			else
				System.out.println("Error parsing RegExp file: "
						+ e.getMessage());
		}
	}

	private class Pair {
		String _feature;

		Pattern _pattern;

		Pair(String _f, Pattern _p) {
			_feature = _f;
			_pattern = _p;
		}
	}

	/**
	 * <p>
	 * This class stores phrase information
	 * </p>
	 * <p>
	 * It's composed by:
	 * </p>
	 * <ul>
	 * <li> _feature: Feature name
	 * <li> _begin: Points phrase beginning in paragraph raw data.
	 * <li> _end: Points phrase end in paragraph raw data.
	 * <p>
	 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
	 * Universidad Complutense de Madrid (GAIA)
	 * </p>
	 * 
	 * @author Juan Antonio Recio García
	 * @version 1.0
	 */
	public class PhraseInfo {
		public String _feature;

		public int _begin;

		public int _end;

		public PhraseInfo(String f, int b, int e) {
			_feature = f;
			_begin = b;
			_end = e;
		}
	}

	/**
	 * This method parses arg[0] String with the regular expression defined in
	 * arg[1].
	 * <p>
	 * Example: "In the Happlewhite Inc. we use a very good ..."
	 * "((\p{Lu}\w+\s)+)(Inc\.|Corporation|Associates|Bank)"
	 * 
	 * @param args[1]
	 *            Text
	 * @param args[2]
	 *            Rule
	 * @param args[3]
	 *            Feature Position.
	 */
	public static void main(String[] args) {
		try {
			String myText = args[0];
			String rule = args[1];

			Pattern p = Pattern.compile(rule);

			Matcher m = p.matcher(myText);

			while (m.find()) {
				System.out.println("Matches: "
						+ myText.substring(m.start(), m.end()));
				System.out.println("Start:   " + m.start());
				System.out.println("End:     " + m.end());
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
		System.exit(0);
	}

	private String cleanSpaces(String w) {
		String res = "";
		StringTokenizer st = new StringTokenizer(w, " ");
		while (st.hasMoreTokens()) {
			res += st.nextToken();
			if (st.hasMoreTokens())
				res += " ";
		}
		return res;
	}

}