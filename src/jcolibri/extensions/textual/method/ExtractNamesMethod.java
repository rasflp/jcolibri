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
import opennlp.common.xml.NLPDocument;

import org.jdom.Element;

/**
 * <p>
 * Selects the main names of the text.
 * </p>
 * <p>
 * It uses Maximum Entrophy method (Opennnlp) to identify main names
 * </p>
 * <p>
 * This method doesn't use Part-of-Speech tags.
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class ExtractNamesMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;

	public static final String NAMES_LIST = "names_list";

	opennlp.grok.preprocess.namefind.NameFinderME opennlpNF;

	/**
	 * Constructor.
	 */
	public ExtractNamesMethod() {
	}

	/**
	 * Iterates over all cases extracting their Texts and invokes nameFinder()
	 * 
	 * @throws jcolibri.cbrcore.exception.ExecutionException
	 */
	public CBRContext execute(CBRContext context)
			throws jcolibri.cbrcore.exception.ExecutionException {
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractNamesMethod BEGIN");

		opennlpNF = new opennlp.grok.preprocess.namefind.EnglishNameFinderME();

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
			namefinder(_texts);
			ProgressBar.PROGRESSBAR.step();
		}
		ProgressBar.PROGRESSBAR.setVisible(false);
		opennlpNF = null;
		jcolibri.util.CBRLogger.log(this.getClass(), 0,
				"ExtractNamesMethod END");
		return context;
	}

	/**
	 * Extracts names form this collection of texts using
	 * opennlp.grok.preprocess.namefind.NameFinderME
	 * 
	 * @param texts
	 *            Text collection
	 */
	void namefinder(Collection texts) {

		Iterator iter = texts.iterator();
		while (iter.hasNext()) {
			Text t = (Text) iter.next();
			NLPDocument opennlpDoc = (NLPDocument) t.getData(Text.NLPDOC);
			opennlpNF.process(opennlpDoc);

			List xmlTokens = opennlpDoc.getTokenElements();
			Iterator tokIter = t.getTokensList().iterator();
			for (int s = 0; s < xmlTokens.size(); s++) {
				Token token = (Token) tokIter.next();
				Element xmlTok = (Element) xmlTokens.get(s);
				List attrbs = xmlTok.getAttributes();
				token.setData(Token.IS_NAME, new Boolean(attrbs
						.contains("name")));
			}
		}
	}

}