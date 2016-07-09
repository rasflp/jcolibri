package jcolibri.extensions.textual.gui.editor;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jcolibri.extensions.textual.representation.Paragraph;
import jcolibri.extensions.textual.representation.Text;

/**
 * 
 * @author Juan José Bello
 */
public class TextEditor extends JScrollPane implements
		jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID = 1L;

	JTextArea ta;

	/** Creates a new instance of StringEditor */
	public TextEditor() {
		ta = new JTextArea();
		this.setViewportView(ta);
		this.setMinimumSize(new Dimension(60, 50));
		this.setPreferredSize(new Dimension(60, 50));
	}

	public Object getEditorValue() {
		Text t = new Text("query", "");

		String raw = ta.getText();
		String[] pars = raw.split("\n");
		for (int i = 0; i < pars.length; i++)
			t.addParagraph(new Paragraph(pars[i]));
		return t;

	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
	}

	public void setDefaultValue(Object defaultValue) {
		if (!(defaultValue instanceof String))
			return;
		String value = (String) defaultValue;
		ta.setText(value);
	}

}
