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
 * Extracts features using Regular Expressions.
 * </p>
 * <p>
 * This method uses rules defined in FEATURES_FILE to recognize regular
 * expressions inside text and extract features
 * </p>
 * <p>
 * Rules files format is:
 * </p>
 * <p>
 * [FeatureName]{FeaturePosition}FeatureRegularExpresion
 * <ul>
 * <li>FeatureName is used to store the extracted information
 * <li>FeaturePosition indicates the position of the information that we want
 * to extract inside the regular expression. The feature is indicated by
 * counting the opening parentheses from left to right.
 * <p>
 * In the expression ((A)(B(C))), for example, there are four such groups:
 * <ol>
 * <li> ((A)(B(C)))
 * <li> (A)
 * <li> (B(C))4(C)
 * </ol>
 * <p>
 * Group zero always stands for the entire expression
 * <li>Regular Expressions are deffined following java.util.regex.Pattern
 * syntaxis. (See API for details)
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
public class ExtractFeaturesMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	public static final String FEATURES_FILE = "Features file";

	ArrayList _fList;

	/**
	 * Constructor.
	 */
	public ExtractFeaturesMethod() {
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes
	 * extractFeatures()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractFeaturesMethod BEGIN");

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
			extractFeatures(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		_fList = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractFeaturesMethod END");
		return context;
	}

	/**
	 * Extracts Features form this collection of texts
	 * 
	 * @param texts
	 *            Text collection
	 */
	void extractFeatures(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();
			Iterator parIt = t.getParagraphs().iterator();
			while (parIt.hasNext()) {
				Paragraph par = (Paragraph) parIt.next();
				String _text = (String) par.getData(Paragraph.RAW_DATA);

				for (int r = 0; r < _fList.size(); r++) {
					Triple trip = (Triple) _fList.get(r);

					Matcher m = trip._pattern.matcher(_text);
					while (m.find()) {
						String group = m.group(trip._group);
						group = cleanSpaces(group);
						ArrayList list = (ArrayList) par
								.getData(Paragraph.FEATURES);
						if (list == null) {
							list = new ArrayList();
							par.setData(Paragraph.FEATURES, list);
						}
						list.add(new FeatureInfo(trip._feature, group, m
								.start(), m.end()));
						System.out.println("Feature: " + trip._feature + " -> "
								+ group);
					}
				}
			}
		}
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

	/**
	 * Load rules file located in FEATURES_FILE
	 */
	void loadRules() {
		_fList = new ArrayList();
		String filename = "featuresRules.txt";
		try {
			String aux = (String) this
					.getParameterValue(ExtractFeaturesMethod.FEATURES_FILE);
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
				String _rest = line.substring(pos + 1);
				pos = _rest.indexOf('}');
				if (pos == -1)
					throw new Exception(line
							+ "  FeaturePostion field not found");
				String _group = _rest.substring(1, pos);
				String _rule = _rest.substring(pos + 1);
				int g = Integer.parseInt(_group);
				_fList.add(new Triple(_feature, Pattern.compile(_rule), g));
			}
			br.close();
		} catch (Exception e) {
			if (e instanceof PatternSyntaxException)
				System.out.println("Error features rule: " + line + " "
						+ e.getMessage());
			else
				System.out.println("Error parsing features rules file: "
						+ e.getMessage());
		}
	}

	private class Triple {
		String _feature;

		Pattern _pattern;

		int _group;

		Triple(String _f, Pattern _p, int _g) {
			_feature = _f;
			_pattern = _p;
			_group = _g;
		}
	}

	/**
	 * <p>
	 * This class stores feature informations
	 * </p>
	 * <p>
	 * It's composed by:
	 * </p>
	 * <ul>
	 * <li> _feature: Feature name
	 * <li> _value: Feature value
	 * <li> _begin: Points feature beginning in paragraph raw data.
	 * <li> _end: Points feature end in paragraph raw data.
	 * <p>
	 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
	 * Universidad Complutense de Madrid (GAIA)
	 * </p>
	 * 
	 * @author Juan Antonio Recio García
	 * @version 1.0
	 */
	public class FeatureInfo {
		public String _feature;

		public String _value;

		public int _begin;

		public int _end;

		public FeatureInfo(String f, String v, int b, int e) {
			_feature = f;
			_value = v;
			_begin = b;
			_end = e;
		}
	}

	/**
	 * This method parses arg[0] String with the regular expression defined in
	 * arg[1].
	 * <p>
	 * It extracts the feature indicated in arg[2].
	 * <p>
	 * <p>
	 * Example: "In the Happlewhite Inc. we use a very good ..."
	 * "((\p{Lu}\w+\s)+)(Inc\.|Corporation|Associates|Bank)" 1
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
			int f = Integer.parseInt(args[2]);

			Pattern p = Pattern.compile(rule);

			Matcher m = p.matcher(myText);

			while (m.find()) {
				String group = m.group(f);
				System.out.println("Found: " + group);
				System.out.println("Start: " + m.start(f));
				System.out.println("End:   " + m.end(f));
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
		System.exit(0);
	}
}