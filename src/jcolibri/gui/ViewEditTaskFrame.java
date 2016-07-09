package jcolibri.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.CBRCore;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.cbrcore.CBRTask;
import jcolibri.cbrcore.MethodType;
import jcolibri.cbrcore.MethodsInstanceRegistry;
import jcolibri.cbrcore.PrototypeMethodsRegistry;
import jcolibri.cbrcore.exception.IncompatibleMethodException;
import jcolibri.util.ImagesContainer;

/**
 * 
 * @author Juan José Bello
 */
public class ViewEditTaskFrame extends javax.swing.JInternalFrame {
	private static final long serialVersionUID = 1L;

	private CBRTask task;

	private CBRCore core;

	private String taskName;

	/** Creates new form ViewEditTaskFrame */
	public ViewEditTaskFrame(CBRTask task, CBRCore core) {
		this.task = task;
		this.taskName = task.getInstanceName();
		this.core = core;
		initComponents();
		myInitComponents();
	}

	/**
	 * This method is called from within the constructor and after
	 * initComponents method to initialize the form.
	 */
	private void myInitComponents() {
		methodsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		instancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setTitle("Task - " + taskName);
		/*
		 * methodsTable.addMouseListener(new MouseAdapter() { public void
		 * mouseClicked(MouseEvent e) { if ( (e.getClickCount()>=1) &&
		 * (methodsTable.getSelectedRow()!=-1) ) {
		 * loadInstancesTable(getMethodNameFromTable(methodsTable.getSelectedRow())); }
		 * if (methodsTable.getSelectedRow()==-1) emptyInstancesTable(); } });
		 */
		methodsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							if (methodsTable.getSelectedRow() != -1)
								loadInstancesTable(getMethodNameFromTable(methodsTable
										.getSelectedRow()));
							else
								emptyInstancesTable();
						}
						reviewComponentes();
					}
				});
		instancesTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						reviewComponentes();
					}
				});
		reloadData();
		reviewComponentes();
		if (methodsTable.getSelectedRow() != -1)
			loadInstancesTable(getMethodNameFromTable(methodsTable
					.getSelectedRow()));

	}

	private void reviewComponentes() {
		disableComponents();
		enableComponents();
	}

	private void disableComponents() {
		newInstanceButton.setEnabled(false);
		deleteInstanceButton.setEnabled(false);
	}

	private boolean isImplementationAvailable(int index) {
		String method = getMethodNameFromTable(index);
		return PrototypeMethodsRegistry.getInstance().getMethod(method)
				.isImplementationAvailable();
	}

	private boolean isMethodApplicable(int row) {
		return ((Boolean) methodsTable.getValueAt(
				methodsTable.getSelectedRow(), methodsTable.getColumn(
						"Applicable").getModelIndex())).booleanValue();
	}

	private void enableComponents() {
		int mrow = methodsTable.getSelectedRow();
		if (mrow != -1 && isImplementationAvailable(mrow)
				&& isMethodApplicable(mrow)) {
			newInstanceButton.setEnabled(true);
		}
		if (instancesTable.getSelectedRow() != -1)
			deleteInstanceButton.setEnabled(true);
	}

	private void emptyInstancesTable() {
		instancesTable.setModel(new InstancesTableModel(task, new Vector()));
	}

	private String getMethodNameFromTable(int selectedRow) {
		int columnIndex = methodsTable.getColumn("Method name").getModelIndex();
		return (String) methodsTable.getValueAt(selectedRow, columnIndex);
	}

	private void loadInstancesTable(String methodName) {
		Collection col = MethodsInstanceRegistry.getInstance()
				.getMethodInstances(methodName);
		InstancesTableModel instancemodel = new InstancesTableModel(task, col);
		instancesTable.setModel(instancemodel);
		if (col.size() == 1)
			instancemodel.setValueAt(new Boolean(true), 0, 2);
		instancesTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		instancesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		instancesTable.getColumnModel().getColumn(2).setPreferredWidth(40);
	}

	private void reloadData() {
		taskNameField.setText(taskName);
		taskNameField.setEditable(false);
		taskDescriptionArea.setText(task.getDescription());
		taskDescriptionArea.setEditable(false);

		Collection col = PrototypeMethodsRegistry.getInstance()
				.getMethodWithCompetence(task.getName());

		MethodsTableModel model = new MethodsTableModel(task, col);
		methodsTable.setModel(model);
		if (col.size() == 1)
			methodsTable.selectAll();

		methodsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		methodsTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		methodsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		methodsTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		methodsTable.getColumnModel().getColumn(4).setPreferredWidth(40);

		InstancesTableModel instancemodel = new InstancesTableModel(task,
				new Vector());
		instancesTable.setModel(instancemodel);
		instancesTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		instancesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		instancesTable.getColumnModel().getColumn(2).setPreferredWidth(40);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() { // GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;

		mainPanel = new javax.swing.JPanel();
		textPanel = new javax.swing.JPanel();
		taskNamelabel = new javax.swing.JLabel();
		taskNameField = new javax.swing.JTextField();
		taskDescriptionLabel = new javax.swing.JLabel();
		taskDescriptionScrollPane = new javax.swing.JScrollPane();
		taskDescriptionArea = new javax.swing.JTextArea();
		methodsPanel = new javax.swing.JPanel();
		methodsTablePanel = new javax.swing.JPanel();
		tableScrollPane = new javax.swing.JScrollPane();
		methodsTable = new javax.swing.JTable();
		methodsButtonPanel = new javax.swing.JPanel();
		newInstanceButton = new javax.swing.JButton();
		instancesTablePanel = new javax.swing.JPanel();
		instanceTableScrollPane = new javax.swing.JScrollPane();
		instancesTable = new javax.swing.JTable();
		instancesButtonPanel = new javax.swing.JPanel();
		deleteInstanceButton = new javax.swing.JButton();
		mainButtonsPanel = new javax.swing.JPanel();
		cancelButton = new javax.swing.JButton();

		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Task instance");
		mainPanel.setLayout(new java.awt.GridBagLayout());

		mainPanel.setBorder(new javax.swing.border.TitledBorder("Task"));
		textPanel.setLayout(new java.awt.GridBagLayout());

		taskNamelabel.setText("Task name:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		textPanel.add(taskNamelabel, gridBagConstraints);

		taskNameField.setColumns(15);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		textPanel.add(taskNameField, gridBagConstraints);

		taskDescriptionLabel.setText("Task description:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		textPanel.add(taskDescriptionLabel, gridBagConstraints);

		taskDescriptionScrollPane.setViewportView(taskDescriptionArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.7;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		textPanel.add(taskDescriptionScrollPane, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.4;
		mainPanel.add(textPanel, gridBagConstraints);

		methodsPanel.setLayout(new java.awt.GridBagLayout());

		methodsPanel.setBorder(new javax.swing.border.TitledBorder("Methods"));
		methodsTablePanel.setLayout(new java.awt.BorderLayout());

		methodsTablePanel.setBorder(new javax.swing.border.EmptyBorder(
				new java.awt.Insets(0, 5, 5, 5)));
		tableScrollPane.setViewportView(methodsTable);

		methodsTablePanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);

		methodsButtonPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT, 0, 0));

		methodsButtonPanel.setBorder(new javax.swing.border.EmptyBorder(
				new java.awt.Insets(1, 1, 3, 1)));
		newInstanceButton.setFont(new java.awt.Font("Dialog", 0, 10));
		newInstanceButton.setIcon(ImagesContainer.NEW);
		newInstanceButton.setText("Instance");
		newInstanceButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		newInstanceButton.setToolTipText("Create new instance");
		newInstanceButton.setFocusPainted(false);
		newInstanceButton.setPreferredSize(new java.awt.Dimension(150, 25));
		newInstanceButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						newInstanceButtonActionPerformed(evt);
					}
				});

		methodsButtonPanel.add(newInstanceButton);

		methodsTablePanel.add(methodsButtonPanel, java.awt.BorderLayout.NORTH);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.5;
		methodsPanel.add(methodsTablePanel, gridBagConstraints);

		instancesTablePanel.setLayout(new java.awt.BorderLayout());

		instancesTablePanel.setBorder(new javax.swing.border.TitledBorder(
				new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1,
						1)), "Available method instances"));
		instanceTableScrollPane.setViewportView(instancesTable);

		instancesTablePanel.add(instanceTableScrollPane,
				java.awt.BorderLayout.CENTER);

		instancesButtonPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT, 0, 0));

		instancesButtonPanel.setBorder(new javax.swing.border.EmptyBorder(
				new java.awt.Insets(1, 1, 3, 1)));
		deleteInstanceButton.setFont(new java.awt.Font("Dialog", 0, 10));
		deleteInstanceButton.setIcon(ImagesContainer.DELETE);
		deleteInstanceButton.setToolTipText("Delete instance");
		deleteInstanceButton.setPreferredSize(new java.awt.Dimension(20, 20));
		deleteInstanceButton.setRolloverEnabled(true);
		deleteInstanceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteInstanceButtonActionPerformed(arg0);
			}
		});

		instancesButtonPanel.add(deleteInstanceButton);

		instancesTablePanel.add(instancesButtonPanel,
				java.awt.BorderLayout.NORTH);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.5;
		methodsPanel.add(instancesTablePanel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.6;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		mainPanel.add(methodsPanel, gridBagConstraints);

		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		cancelButton.setText("Close");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		mainButtonsPanel.add(cancelButton);

		getContentPane().add(mainButtonsPanel, java.awt.BorderLayout.SOUTH);

		pack();
	} // GEN-END:initComponents

	private void newInstanceButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_newInstanceButtonActionPerformed

		CBRMethod method = ((MethodsTableModel) methodsTable.getModel())
				.getMethodAt(methodsTable.getSelectedRow());

		HashMap parametersToReturn = new HashMap();
		List parametersIn = method.getParametersInfo();
		boolean toAdd = true;
		if (parametersIn != null && parametersIn.size() != 0) {
			String title = "Parameters for a new instance of "
					+ method.getClass().getName();
			GenericMethodParametersPane pane = new GenericMethodParametersPane(
					parametersIn, parametersToReturn);
			JDialog dialog = pane.createJDialog(CBRArmGUI.getReference(), true,
					title);
			dialog.setVisible(true);
			toAdd = (pane.getValue() == GenericMethodParametersPane.OK);
		}
		if (toAdd) {
			CBRMethod newInstance = MethodsInstanceRegistry.getInstance()
					.createInstance(
							method,
							method.getName()
									+ new Double(Math.random() * 10000)
											.intValue());
			newInstance.setParameters(parametersToReturn);
			try {
				core.resolveTaskWithMethod(task, newInstance);
			} catch (IncompatibleMethodException e) {
				e.printStackTrace();
			}
			loadInstancesTable(method.getName());
		}

	} // GEN-LAST:event_newInstanceButtonActionPerformed

	private void deleteInstanceButtonActionPerformed(
			java.awt.event.ActionEvent evt) {

		if (instancesTable.getSelectedRow() >= 0) {
			InstancesTableModel insTbMdl = (InstancesTableModel) instancesTable
					.getModel();
			insTbMdl.removeInstance(instancesTable.getSelectedRow());
		}
	}

	/** Exit the window */
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_cancelButtonActionPerformed
		this.dispose();
	} // GEN-LAST:event_cancelButtonActionPerformed

	class InstancesTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		ArrayList instances;

		String[] columns = new String[] { "Instance name", "Method name",
				"Chosen" };

		CBRTask task;

		public InstancesTableModel(CBRTask task, Collection col) {
			this.task = task;
			instances = new ArrayList(col);
		}

		public Object getValueAt(int row, int column) {
			CBRMethod instance = (CBRMethod) instances.get(row);
			switch (column) {
			case 0:
				return instance.getInstanceName();
			case 1:
				return instance.getName();
			case 2:
				return (task.getMethodInstance() == null ? new Boolean(false)
						: new Boolean(task.getMethodInstance().equals(
								instances.get(row))));
			default:
				return null;
			}
		}

		public Class getColumnClass(int columnIndex) {
			if (columnIndex == 2)
				return Boolean.class;
			else
				return String.class;
		}

		public boolean isCellEditable(int row, int column) {
			return (column == 2);
		}

		public void setValueAt(Object aValue, int row, int column) {
			try {
				if (((Boolean) aValue).booleanValue()) {
					core.resolveTaskWithMethod(task, (CBRMethod) instances
							.get(row));
				} else {
					core.resetTaskWithMethod(task);
				}
				this.fireTableRowsUpdated(0, instances.size());
			} catch (IncompatibleMethodException ime) {
				JOptionPane
						.showMessageDialog(
								ViewEditTaskFrame.this,
								"Current context in not consistent, please review current method assignation",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		public int getRowCount() {
			if (instances != null)
				return instances.size();
			else
				return 0;
		}

		public int getColumnCount() {
			return columns.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public void removeInstance(int idx) {
			instances.remove(idx);
			fireTableDataChanged();
		}
	}

	class MethodsTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		CBRMethod[] methods = new CBRMethod[0];

		String[] columns = new String[] { "Method name", "Method type",
				"Method description", "Available", "Applicable" };

		CBRTask task;

		public MethodsTableModel(CBRTask task, Collection col) {
			this.task = task;
			methods = (CBRMethod[]) col.toArray(new CBRMethod[col.size()]);
		}

		public CBRMethod getMethodAt(int row) {
			return methods[row];
		}

		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return methods[row].getName();
			case 1:
				return methods[row].getMethodType().equals(
						MethodType.DECOMPOSITION) ? "Decomposition"
						: "Execution";
			case 2:
				return methods[row].getInformalDescription();
			case 3:
				return new Boolean(methods[row].isImplementationAvailable());
			case 4:
				return new Boolean(core.getContext().isMethodApplicable(
						methods[row]));
			default:
				return null;
			}
		}

		public Class getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return Boolean.class;
			case 4:
				return Boolean.class;
			default:
				return String.class;
			}
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public int getRowCount() {
			if (methods != null)
				return methods.length;
			else
				return 0;
		}

		public int getColumnCount() {
			return columns.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelButton;

	private javax.swing.JButton deleteInstanceButton;

	private javax.swing.JScrollPane instanceTableScrollPane;

	private javax.swing.JPanel instancesButtonPanel;

	private javax.swing.JTable instancesTable;

	private javax.swing.JPanel instancesTablePanel;

	private javax.swing.JPanel mainButtonsPanel;

	private javax.swing.JPanel mainPanel;

	private javax.swing.JPanel methodsButtonPanel;

	private javax.swing.JPanel methodsPanel;

	private javax.swing.JTable methodsTable;

	private javax.swing.JPanel methodsTablePanel;

	private javax.swing.JButton newInstanceButton;

	private javax.swing.JScrollPane tableScrollPane;

	private javax.swing.JTextArea taskDescriptionArea;

	private javax.swing.JLabel taskDescriptionLabel;

	private javax.swing.JScrollPane taskDescriptionScrollPane;

	private javax.swing.JTextField taskNameField;

	private javax.swing.JLabel taskNamelabel;

	private javax.swing.JPanel textPanel;
	// End of variables declaration//GEN-END:variables

}
