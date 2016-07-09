package jcolibri.gui.model;

import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.CBRTask;
import jcolibri.cbrcore.PrototypeTaskPkgReg;
import jcolibri.cbrcore.PrototypeTasksRegistry;
import jcolibri.cbrcore.event.RegistryChangeEvent;
import jcolibri.cbrcore.event.RegistryChangeListener;

/**
 * 
 * @author Juan José Bello
 */
public class TasksTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of TasksTableModel */

	String[] columns = new String[] { "Name", "Description" };

	private PrototypeTaskPkgReg ptPkgReg;

	public TasksTableModel() {
		ptPkgReg = null;
		PrototypeTasksRegistry.getInstance().addRegistryChangeListener(
				new RegistryChangeListener() {
					public void registryChanged(RegistryChangeEvent ev) {
						fireTableDataChanged();
					}
				});
	}

	public void setPackage(String pkgName) {
		ptPkgReg = PrototypeTasksRegistry.getInstance().getPrototypeTaskPkg(
				pkgName);
		fireTableDataChanged();
	}

	public Object getValueAt(int row, int column) {
		CBRTask task;

		task = getTask(row);
		switch (column) {
		case 0:
			return task.getName();
		case 1:
			return task.getDescription();
		default:
			return null;
		}
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public int getRowCount() {
		return (ptPkgReg == null ? 0 : ptPkgReg.getNumTasks());
	}

	public int getColumnCount() {
		return columns.length;
	}

	public String getColumnName(int col) {
		return columns[col];
	}

	public CBRTask getTask(int row) {
		return ptPkgReg.getTask(row);
	}
}
