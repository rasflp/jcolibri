package jcolibri.gui.editor;

import java.awt.FileDialog;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jcolibri.gui.ParameterEditor;
import jcolibri.gui.ParameterEditorFactory;

/**
 * @author Juan A. Recio-García
 * 
 */
public class FileEditor extends JPanel implements ParameterEditor {
	private static final long serialVersionUID = 1L;

	JTextField text;

	JButton button;

	public FileEditor() {
		text = new JTextField("");
		button = new JButton("...");
		this.add(text);
		this.add(button);
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonPressed();
			}
		});
		text.setMinimumSize(new java.awt.Dimension(150, 25));
		text.setPreferredSize(new java.awt.Dimension(150, 25));

	}

	private void buttonPressed() {
		FileDialog fd = new FileDialog(ParameterEditorFactory.parent,
				"Load file...", FileDialog.LOAD);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			text.setText(fd.getDirectory() + fd.getFile());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.gui.ParameterEditor#getEditorValue()
	 */
	public Object getEditorValue() {
		return text.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.gui.ParameterEditor#getJComponent()
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof String))
			return;
		String value = (String) defaultValue;
		text.setText(value);
	}

}
