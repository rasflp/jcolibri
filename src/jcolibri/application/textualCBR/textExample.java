package jcolibri.application.textualCBR;

import java.util.Iterator;

import javax.swing.JOptionPane;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcore.CBRContext;
import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.util.CaseCreatorUtils;

/**
 * Textual CBR example application.
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class textExample {

	public textExample() {
	}

	public static void main(String[] args) {

		// Create CBR application
		textualCBR test = new textualCBR();
		// Init CBR application
		test.init();
		test.executePreCycle();

		CBRContext context;

		boolean _continue = false;
		do {
			context = test.executeCycle();
			showFrame(context);
			_continue = (JOptionPane.showConfirmDialog(null,
					"Try another query?") == JOptionPane.YES_OPTION);
		} while (_continue);

		test.executePostCycle();
		System.exit(0);
	}

	private static void showFrame(CBRContext context) {
		//CaseEvaluation ce = (CaseEvaluation) context.getCases().get(0);
		CBRCase caso = context.getCases().get(0);//ce.getCase();

		Text text = (Text) CaseCreatorUtils.getAttribute(caso,
				"Description.Restaurant").getValue();
		String restaurant = (String) caso.getValue();
		Iterator parIter = text.getParagraphs().iterator();
		Paragraph par = (Paragraph) parIter.next();
		String address = (String) par.getData(Paragraph.RAW_DATA);
		par = (Paragraph) parIter.next();
		String description = (String) par.getData(Paragraph.RAW_DATA);

		CBRQuery query = context.getQuery();
		text = (Text) CaseCreatorUtils.getAttribute(query,
				"Description.Restaurant").getValue();
		par = (Paragraph) text.getParagraphs().iterator().next();
		String text_cad = (String) par.getData(Paragraph.RAW_DATA);

		ResultFrame rf = new ResultFrame(text_cad, restaurant, address,
				description);
		rf.setModal(true);
		rf.setVisible(true);

	}

}