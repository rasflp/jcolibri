package jcolibri.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JWindow;

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

public class LogoFrame extends JWindow {
	private static final long serialVersionUID = 1L;

	public LogoFrame() throws HeadlessException {
		try {
			JLabel jLabel1 = new JLabel();
			jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
					"/jcolibri/resources/jcolibri.jpg")));
			this.getContentPane().add(jLabel1, BorderLayout.CENTER);
			this.pack();

			java.awt.Dimension screenSize = java.awt.Toolkit
					.getDefaultToolkit().getScreenSize();
			setBounds((screenSize.width - this.getWidth()) / 2,
					(screenSize.height - this.getHeight()) / 2, getWidth(),
					getHeight());

			this.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws HeadlessException {

	}

}