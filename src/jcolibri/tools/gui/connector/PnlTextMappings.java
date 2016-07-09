package jcolibri.tools.gui.connector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import jcolibri.util.ImagesContainer;

/**
 * Panel to manage the mappings of a plain text connector.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlTextMappings extends JPanel {
	private static final long serialVersionUID = 1L;

	private TbTextMappings tbMappings;

	private JButton btnAdd, btnRemove, btnUp, btnDown;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefConnText
	 *            panel to manage plain text connectors.
	 */
	public PnlTextMappings(PnlConnectorText pnlDefConnText) {
		super();
		createComponents(pnlDefConnText);
	}

	/**
	 * Updates the list of case parameters showed.
	 */
	public void updateCaseParams() {
		tbMappings.updateCaseParams();
	}

	/**
	 * Clears the mappings.
	 */
	public void clearTable() {
		tbMappings.clearTable();
	}

	/**
	 * Adds a mapping.
	 * 
	 * @param param
	 */
	public void addMapping(String param) {
		tbMappings.addMapping(param);
	}

	/**
	 * Returns the actual connector mappings.
	 * 
	 * @return
	 */
	public Vector getConnMappings() {
		return tbMappings.getConnMappings();
	}

	/**
	 * Creates the visual components.
	 * 
	 * @param pnlDefConnText
	 */
	private void createComponents(PnlConnectorText pnlDefConnText) {
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		JPanel pnlTable, pnlButtons;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder, "Mappings");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);
		setLayout(new BorderLayout());

		// Create subpanels.
		pnlTable = createPnlTable(pnlDefConnText);
		pnlButtons = createPnlButtons();
		add(pnlTable, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the visual components.
	 * 
	 * @param pnlDefConnText
	 * @return
	 */
	private JPanel createPnlTable(PnlConnectorText pnlDefConnText) {
		JScrollPane scrPnl;
		JPanel pnlRes;

		// Table of mappings.
		tbMappings = new TbTextMappings(pnlDefConnText);
		scrPnl = new JScrollPane(tbMappings);
		tbMappings.setPreferredScrollableViewportSize(new Dimension(200, 100));

		pnlRes = new JPanel(new BorderLayout());
		pnlRes.add(scrPnl, BorderLayout.CENTER);
		return pnlRes;
	}

	/**
	 * Creates the visual components.
	 */
	private JPanel createPnlButtons() {
		JPanel pnlRes;
		ButtonsListener btnListener;

		btnListener = new ButtonsListener();
		btnAdd = new JButton("Add");
        btnAdd.setIcon(ImagesContainer.ADD);
		btnAdd.addActionListener(btnListener);
		btnRemove = new JButton("Remove");
        btnRemove.setIcon(ImagesContainer.REMOVE);
		btnRemove.addActionListener(btnListener);
		btnUp = new JButton("Up");
        btnUp.setIcon(ImagesContainer.UP);
		btnUp.addActionListener(btnListener);
		btnDown = new JButton("Down");
        btnDown.setIcon(ImagesContainer.DOWN);
		btnDown.addActionListener(btnListener);

		pnlRes = new JPanel();
		pnlRes.add(btnAdd);
		pnlRes.add(btnRemove);
		pnlRes.add(btnUp);
		pnlRes.add(btnDown);
		return pnlRes;
	}

	/**
	 * Listener of button events.
	 * 
	 * @author Antonio
	 */
	private class ButtonsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAdd) {
				tbMappings.addNewMapping();
			} else if (e.getSource() == btnRemove) {
				tbMappings.removeSelMapping();
			} else if (e.getSource() == btnUp) {
				tbMappings.upSelMapping();
			} else if (e.getSource() == btnDown) {
				tbMappings.downSelMapping();
			}
		}
	}
}
