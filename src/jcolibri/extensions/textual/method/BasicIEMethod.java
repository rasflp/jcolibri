package jcolibri.extensions.textual.method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import jcolibri.cbrcase.CBRCaseRecord;
import jcolibri.cbrcase.Individual;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.util.TextualObjectUtils;
import jcolibri.util.CBRLogger;
import jcolibri.util.ProgressBar;

/**
 * <p>
 * Extracts texts information and stores it in case individuals (if defined).
 * <p>
 * For example: if we have identified a feature with label "person" and our case
 * has an attribute with the same label, this method stores the feature
 * information into the case attribute.
 * <p>
 * Phrases are also extracting storing a Boolean(true) value if an attribute
 * with the same label exists.
 * <p>
 * It also stores the "TOPIC" information extracted by
 * "DomainTopicClassifierMethod" in the attribute named as defined in
 * Paragraph.TOPIC (only if this attribute is defined in the case). Topic info
 * is stored in a String where different values are sepparated by white-spaces.
 * <p>
 * Important: This method only works with CBRCaseRecord instances.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class BasicIEMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public BasicIEMethod() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0, "BasicIEMethod BEGIN");

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
			extractTopicInfo(_case, _texts);
			extractFeaturesInfo(_case, _texts);
			extractPhrasesInfo(_case, _texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);

		jcolibri.util.CBRLogger.log(this.getClass(), 0, "BasicIEMethod END");
		return context;
	}

	/**
	 * Extracts the topic information from _text to _case
	 * 
	 * @param _case
	 *            Case that contains the _texts
	 * @param _texts
	 *            _case is composed by this _texts
	 */
	public static void extractTopicInfo(Individual _case, Collection _texts) {
		if (!(_case instanceof CBRCaseRecord))
			return;
		String topicsCad = "";
		for (Iterator tIter = _texts.iterator(); tIter.hasNext();) {
			Text t = (Text) tIter.next();
			for (Iterator pIter = t.getParagraphs().iterator(); pIter.hasNext();) {
				Paragraph par = (Paragraph) pIter.next();
				Collection topics = (Collection) par.getData(Paragraph.TOPICS);
				if (topics != null) {
					for (Iterator topicIter = topics.iterator(); topicIter
							.hasNext();) {
						String topic = (String) topicIter.next();
						topicsCad += topic + " ";
					}
				}
			}
		}
		IndividualRelation ir = findRelation(_case, Paragraph.TOPICS);
		if (ir != null) {
			String value = (String) ir.getTarget().getValue();
			if (value != null)
				topicsCad = value + " " + topicsCad;
			ir.getTarget().setValue(topicsCad);
			CBRLogger.log("BasicIEMethod", CBRLogger.INFO, "Extracting topic: "
					+ topicsCad + " to case: " + _case.getValue());
		}
	}

	/**
	 * Extracts the topic information from _text to _case
	 * 
	 * @param _case
	 *            Case that contains the _texts
	 * @param _texts
	 *            _case is composed by this _texts
	 */
	public static void extractFeaturesInfo(Individual _case, Collection _texts) {
		if (!(_case instanceof CBRCaseRecord))
			return;
		Hashtable featuresTable = new Hashtable();
		for (Iterator tIter = _texts.iterator(); tIter.hasNext();) {
			Text t = (Text) tIter.next();
			for (Iterator pIter = t.getParagraphs().iterator(); pIter.hasNext();) {
				Paragraph par = (Paragraph) pIter.next();
				Collection features = (Collection) par
						.getData(Paragraph.FEATURES);
				if (features != null)
					for (Iterator fIter = features.iterator(); fIter.hasNext();) {
						ExtractFeaturesMethod.FeatureInfo fi = (ExtractFeaturesMethod.FeatureInfo) fIter
								.next();
						Set set = (Set) featuresTable.get(fi._feature);
						if (set == null) {
							set = new HashSet();
							set.add(fi._value);
							featuresTable.put(fi._feature, set);
						} else
							set.add(fi._value);
					}
			}
		}
		for (Enumeration fiter = featuresTable.keys(); fiter.hasMoreElements();) {
			String feature = (String) fiter.nextElement();
			IndividualRelation ir = findRelation(_case, feature);
			if (ir == null)
				continue;
			Set set = (Set) featuresTable.get(feature);
			String featuresCad = "";
			for (Iterator iterator = set.iterator(); iterator.hasNext();)
				featuresCad += (String) iterator.next() + " ";
			String value = (String) ir.getTarget().getValue();
			if (value != null)
				featuresCad = value + " " + featuresCad;
			ir.getTarget().setValue(featuresCad);
			CBRLogger.log("BasicIEMethod", CBRLogger.INFO,
					"Extracting feature: " + feature + "<-" + featuresCad
							+ " to case: " + _case.getValue());
		}
	}

	/**
	 * Extracts the phrase information from _text to _case
	 * 
	 * @param _case
	 *            Case that contains the _texts
	 * @param _texts
	 *            _case is composed by this _texts
	 */
	public static void extractPhrasesInfo(Individual _case, Collection _texts) {
		if (!(_case instanceof CBRCaseRecord))
			return;
		Set phrases = new HashSet();
		for (Iterator tIter = _texts.iterator(); tIter.hasNext();) {
			Text t = (Text) tIter.next();
			for (Iterator pIter = t.getParagraphs().iterator(); pIter.hasNext();) {
				Paragraph par = (Paragraph) pIter.next();
				Collection parPhrases = (Collection) par
						.getData(Paragraph.PHRASES);
				if (parPhrases != null)
					phrases.addAll(parPhrases);
			}
		}
		for (Iterator iter = phrases.iterator(); iter.hasNext();) {
			ExtractPhrasesMethod.PhraseInfo pi = (ExtractPhrasesMethod.PhraseInfo) iter
					.next();
			String phrase = pi._feature;
			IndividualRelation ir = findRelation(_case, phrase);
			if (ir == null)
				continue;
			ir.getTarget().setValue(new Boolean(true));

			CBRLogger.log("BasicIEMethod", CBRLogger.INFO,
					"Extracting phrase: " + phrase + " to case: "
							+ _case.getValue());
		}

	}

	public static IndividualRelation findRelation(Individual _case,
			String _relsuffix) {
		for (Iterator iter = _case.getRelations().iterator(); iter.hasNext();) {
			IndividualRelation ir = (IndividualRelation) iter.next();
			if (ir.getDescription().endsWith(_relsuffix))
				return ir;
			else {
				IndividualRelation ir2 = findRelation(ir.getTarget(),
						_relsuffix);
				if (ir2 != null)
					return ir2;
			}

		}
		return null;
	}
}