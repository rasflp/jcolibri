package jcolibri.gui;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 * <p>
 * Título: ClassName
 * </p>
 * <p>
 * Descripción:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Empresa:
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class HelpFrame extends JDialog {
	private static final long serialVersionUID = 1L;

	public HelpFrame() {
		try {
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);

			java.io.BufferedReader fr = new java.io.BufferedReader(
					new java.io.FileReader("doc/help.html"));
			String text = "";
			String cad = null;
			while ((cad = fr.readLine()) != null)
				text += cad;
			fr.close();

			editorPane.setContentType("text/html");
			editorPane.setText(text);

			// Put the editor pane in a scroll pane.
			JScrollPane editorScrollPane = new JScrollPane(editorPane);
			editorScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			editorScrollPane.setPreferredSize(new Dimension(600, 600));
			editorScrollPane.setMinimumSize(new Dimension(10, 10));
			this.getContentPane().add(editorScrollPane);
			this.setSize(600, 600);
			this.pack();
			java.awt.Dimension screenSize = java.awt.Toolkit
					.getDefaultToolkit().getScreenSize();
			setBounds((screenSize.width - this.getWidth()) / 2,
					(screenSize.height - this.getHeight()) / 2, getWidth(),
					getHeight());

		} catch (Exception e) {
			System.err.println("Error showing help: " + e.getMessage());
		}

	}

	public static void main(String[] args) throws HeadlessException {
		HelpFrame helpFrame1 = new HelpFrame();
		helpFrame1.setVisible(true);
	}

}