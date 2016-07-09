package jcolibri.extensions.textual.method;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
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
 * Associates a Topic using extracted Features and Phrases as conditions.
 * </p>
 * <p>
 * This method uses rules defined in "TOPICS_FILE" to associate one or more
 * topics to paragraphs
 * </p>
 * <p>
 * Topic rules file format is:
 * <p>
 * <p>
 * [Topic] &lt FeatureName,value &gt &lt FeatureName,value &gt ... &lt Phrase
 * &gt &lt Phrase &gt
 * </p>
 * <ul>
 * <li>Topic: Topic classification
 * <li>FeatureName: FeatureName defined in the features extraction Layer
 * <li>value: FeatureName value. It also can be '?', meaning any value.
 * <li>Phrase: You can use the pharses detected in the phrase Layer
 * </ul>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class DomainTopicClassifierMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public DomainTopicClassifierMethod() {
	}

	public static final String TOPICS_FILE = "Topics file";

	ArrayList _tList;

	/**
	 * Iterates over all cases extracting their Texts and invokes
	 * extractTopics()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"DomainTopicClassifierMethod BEGIN");

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
			extractTopics(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);

		_tList = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"DomainTopicClassifierMethod END");
		return context;
	}

	/**
	 * Extracts topics form this collection of texts
	 * 
	 * @param texts
	 *            Text collection
	 */
	void extractTopics(Collection texts) {
		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();
			Iterator parIt = t.getParagraphs().iterator();
			// For each paragraph
			while (parIt.hasNext()) {
				Paragraph par = (Paragraph) parIt.next();
				Collection _features = (Collection) par
						.getData(Paragraph.FEATURES);
				Collection _phrases = (Collection) par
						.getData(Paragraph.PHRASES);
				if ((_features == null) && (_phrases == null))
					continue;
				// For each rule
				for (int r = 0; r < _tList.size(); r++) {
					TopicRule rule = (TopicRule) _tList.get(r);

					// Chech rule conditions
					boolean valid = true;
					HashMap conditions = rule._data;
					Iterator fOpIter = conditions.keySet().iterator();
					// For each condition
					while (fOpIter.hasNext() && valid) {
						String featureOrPhrase = (String) fOpIter.next();
						String value = (String) conditions.get(featureOrPhrase);
						// It's a phrase condition
						if (value == null) {
							if (_phrases == null)
								valid = false; // _phrases.contains(featureOrPhrase);
							else {
								boolean found = false;
								for (Iterator it = _phrases.iterator(); it
										.hasNext()
										&& !found;) {
									ExtractPhrasesMethod.PhraseInfo pi = (ExtractPhrasesMethod.PhraseInfo) it
											.next();
									if (pi._feature.equals(featureOrPhrase))
										found = true;
								}
								valid = found;
							}
						}
						// It's a feature condition
						else {
							if (_features == null)
								valid = false;
							else {
								boolean found = false;
								for (Iterator it = _features.iterator(); it
										.hasNext()
										&& !found;) {
									ExtractFeaturesMethod.FeatureInfo fi = (ExtractFeaturesMethod.FeatureInfo) it
											.next();
									if (!value.equals("?"))
										found = (fi._feature
												.equals(featureOrPhrase) && fi._value
												.equals(value));
									else
										found = fi._feature
												.equals(featureOrPhrase);
								}
								valid = found;
							}
						}
					}
					// If rule conditions are true -> include rule name in
					// Topics
					if (valid) {
						Collection col = (Collection) par
								.getData(Paragraph.TOPICS);
						if (col == null) {
							col = new ArrayList();
							par.setData(Paragraph.TOPICS, col);
						}
						col.add(rule._name);
						System.out.println("Topic: " + rule._name);
					}
				}
			}
		}
	}

	/**
	 * Load rules file located in TOPICS_FILE
	 */
	void loadRules() {
		_tList = new ArrayList();
		String filename = "DomainRules.txt";
		try {
			String aux = (String) this
					.getParameterValue(DomainTopicClassifierMethod.TOPICS_FILE);
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
					throw new Exception(line + "  Topic field not found");
				String _feature = line.substring(1, pos);
				String _rest = line.substring(pos + 1);

				HashMap data = new HashMap();
				int indexOpen;
				int indexClose;
				while (((indexOpen = _rest.indexOf("<")) != -1)
						&& ((indexClose = _rest.indexOf(">")) != -1)) {
					String content = _rest.substring(indexOpen, indexClose);
					StringTokenizer st = new StringTokenizer(content, "<,>");
					if (!st.hasMoreTokens())
						continue;
					String featureOrPhrase = st.nextToken();
					String value = null;
					if (st.hasMoreTokens())
						value = st.nextToken();
					// If its a Phrase condition -> value == null
					data.put(cleanSpaces(featureOrPhrase), cleanSpaces(value));
					_rest = _rest.substring(indexClose + 1, _rest.length());
				}

				TopicRule rule = new TopicRule(_feature, data);
				_tList.add(rule);
			}
			br.close();
		} catch (Exception e) {
			if (e instanceof PatternSyntaxException)
				System.out.println("Error parsing domain rule: " + line + " "
						+ e.getMessage());
			else
				System.out.println("Error parsing domain rules file: "
						+ e.getMessage());
		}
	}

	private class TopicRule {
		String _name;

		HashMap _data;

		TopicRule(String n, HashMap d) {
			_name = n;
			_data = d;
		}
	}

	private String cleanSpaces(String w) {
		if (w == null)
			return null;
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