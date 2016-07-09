package jcolibri.tools.gui.datatype;

import java.util.Collections;

import javax.swing.AbstractListModel;
import javax.swing.JList;

import jcolibri.datatypes.StringEnumType;

/**
 * Visual list to manage the enumeration values.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class LstEnumValues extends JList {
	private static final long serialVersionUID = 1L;

	ListModel lstMdl;

	/**
	 * Constructor.
	 */
	public LstEnumValues() {
		super();
		lstMdl = new ListModel();
		setModel(lstMdl);
	}

	/**
	 * Sets the working enum type.
	 * 
	 * @param enumType
	 */
	public void setEnumType(StringEnumType enumType) {
		lstMdl.setEnumType(enumType);
	}

	/**
	 * Adds an enum value.
	 * 
	 * @param value
	 */
	public void addValue(String value) {
		lstMdl.addValue(value);
	}

	/**
	 * Removes the selected enum value.
	 */
	public void removeSelValue() {
		if (getSelectedIndex() >= 0)
			lstMdl.removeValue(getSelectedIndex());
	}

	/**
	 * Moves up 1 position the selected enum value.
	 */
	public void moveUpSelValue() {
		int idxSel = getSelectedIndex();

		if (idxSel >= 1) {
			lstMdl.moveUpValue(idxSel);
			setSelectedIndex(idxSel - 1);
		}
	}

	/**
	 * Moves down 1 position the selected enum value.
	 */
	public void moveDownSelValue() {
		int idxSel = getSelectedIndex();

		if ((idxSel >= 0) && (idxSel < lstMdl.getSize() - 1)) {
			lstMdl.moveDownValue(idxSel);
			setSelectedIndex(idxSel + 1);
		}
	}

	/**
	 * List mode.
	 * 
	 * @author Antonio
	 */
	private class ListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;

		private StringEnumType enumType;

		/**
		 * Constructor.
		 */
		public ListModel() {
			enumType = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getSize()
		 */
		public int getSize() {
			if (enumType != null)
				return enumType.getNumPossibleValues();
			else
				return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int arg0) {
			return enumType.getPossibleValues().get(arg0);
		}

		/**
		 * Sets the working enum type.
		 * 
		 * @param enumType
		 */
		public void setEnumType(StringEnumType enumType) {
			this.enumType = enumType;
			fireContentsChanged(this, 0, getSize() - 1);
		}

		/**
		 * Adds an enum value.
		 * 
		 * @param value
		 */
		public void addValue(String value) {
			enumType.getPossibleValues().add(value);
			fireIntervalAdded(this, getSize() - 1, getSize() - 1);
		}

		/**
		 * Removes an enum value by index.
		 * 
		 * @param idx
		 *            index.
		 */
		public void removeValue(int idx) {
			enumType.getPossibleValues().remove(idx);
			fireIntervalRemoved(this, idx, idx);
		}

		/**
		 * Moves up 1 position the value at index idx.
		 * 
		 * @param idx
		 */
		public void moveUpValue(int idx) {
			Collections.swap(enumType.getPossibleValues(), idx, idx - 1);
			fireContentsChanged(this, idx - 1, idx);
		}

		/**
		 * Moves down 1 position the value at index idx.
		 * 
		 * @param idx
		 */
		public void moveDownValue(int idx) {
			Collections.swap(enumType.getPossibleValues(), idx, idx + 1);
			fireContentsChanged(this, idx, idx + 1);
		}
	}
}
