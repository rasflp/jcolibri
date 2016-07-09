package jcolibri.gui.model;

import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.CBRMethod;
import jcolibri.cbrcore.PrototypeMethodPkgReg;
import jcolibri.cbrcore.PrototypeMethodsRegistry;
import jcolibri.cbrcore.event.RegistryChangeEvent;
import jcolibri.cbrcore.event.RegistryChangeListener;

/**
 * 
 * @author Juan José Bello
 */
public class MethodsTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	private PrototypeMethodPkgReg pmPkgReg;

	String[] columns = new String[] { "Name", "Description", "Available" };

	public MethodsTableModel() {
		pmPkgReg = null;
		PrototypeMethodsRegistry.getInstance().addRegistryChangeListener(
				new RegistryChangeListener() {
					public void registryChanged(RegistryChangeEvent ev) {
						fireTableDataChanged();
					}
				});
	}

	public void setPackage(String pkgName) {
		pmPkgReg = PrototypeMethodsRegistry.getInstance()
				.getPrototypeMethodPkg(pkgName);
		fireTableDataChanged();
	}

	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Boolean.class;
		default:
			return String.class;
		}
	}

	public Object getValueAt(int row, int column) {
		CBRMethod method;
		method = getMethod(row);
		switch (column) {
		case 0:
			return method.getName();
		case 1:
			return method.getInformalDescription();
		case 2:
			return new Boolean(method.isImplementationAvailable());
		default:
			return null;
		}
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public int getRowCount() {
		return (pmPkgReg == null ? 0 : pmPkgReg.getMethods().size());
	}

	public int getColumnCount() {
		return columns.length;
	}

	public String getColumnName(int col) {
		return columns[col];
	}

	public CBRMethod getMethod(int idx) {
		return pmPkgReg.getMethod(idx);
	}
}