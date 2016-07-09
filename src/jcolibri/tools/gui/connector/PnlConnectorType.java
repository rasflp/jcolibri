package jcolibri.tools.gui.connector;

import javax.swing.JPanel;

import jcolibri.tools.data.CaseStructure;

/**
 * The panels to manage a type of connector must extends this class.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class PnlConnectorType extends JPanel {

	/**
	 * Informs about the actual case structure and the file where is defined.
	 * 
	 * @param caseSt
	 *            case structure.
	 * @param caseStFile
	 *            case structure file path.
	 */
	public abstract void setCaseStructure(CaseStructure caseSt,
			String caseStFile);

	/**
	 * Writes the defined connector to a file.
	 * 
	 * @param fileName
	 *            file path.
	 * @param caseBase
	 *            java class name that implements the CBRCaseBase.
	 */
	public abstract void writeConnectorToFile(String fileName, String caseBase);

	/**
	 * Reads the defines connector from a file.
	 * 
	 * @param fileName
	 *            connector file path.
	 */
	public abstract void readConnectorFromFile(String fileName);
}
