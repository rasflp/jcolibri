package jcolibri.tools.gui.similarity;

import javax.swing.JPanel;

/**
 * Panels to manage diferent types of similarities (local or global) must extend
 * this class.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public abstract class PnlSimType extends JPanel {

	/**
	 * Loads the similarities of the working package.
	 */
	public abstract void loadSimilarities();

	/**
	 * Saves the similarities of the working package.
	 */
	public abstract void saveSimilarities();

	/**
	 * Sets the working package.
	 * 
	 * @param pkgName
	 *            package name.
	 */
	public abstract void setActivePackage(String pkgName);
}
