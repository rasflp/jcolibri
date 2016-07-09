package jcolibri.gui;

import javax.swing.JScrollPane;

import jcolibri.util.CBRLogger;

/**
 * @author Juan A. Recio-García
 * 
 */
public class LogPanel extends JScrollPane implements jcolibri.util.LogListener {
	private static final long serialVersionUID = 1L;

	javax.swing.JTextArea textArea = new javax.swing.JTextArea();

	public LogPanel() {
		super();
		setViewportView(textArea);
		// JScrollPane textAreaScrollPane = new
		// javax.swing.JScrollPane(textArea);
		// add(textAreaScrollPane);
		// this.setPreferredSize(new java.awt.Dimension(800,50));
		this.setMinimumSize(new java.awt.Dimension(0, 0));
		// textArea.setPreferredSize(new java.awt.Dimension(800,50));
		// textArea.setMinimumSize(new java.awt.Dimension(800,50));
		textArea.setEditable(false);
		java.awt.Font f = textArea.getFont();
		textArea.setFont(f.deriveFont(1, (float) 10));

		CBRLogger.addListener(this);
	}

	public void receivedData(String data) {
		textArea.append(data);
	}

	public static void main(String[] args) {
	}
}
