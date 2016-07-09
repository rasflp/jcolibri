package jcolibri.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.gui.model.TasksTableModel;

/**
 * 
 * @author Juan José Bello
 */
public class ChooseableTaskListPane {

	/** Creates a new instance of ChooseableTaskListPane */

	ArrayList selectedTasks;

	public final static int OK = 1;

	public final static int CANCEL = 2;

	JDialog dialog;

	// Variables declaration - do not modify
	private javax.swing.JPanel buttonsPanel;

	private javax.swing.JButton cancelButton;

	private javax.swing.JButton okButton;

	private javax.swing.JScrollPane tasksScrollPane;

	private javax.swing.JTable tasksTable;

	private JPanel packagesPanel;

	private JLabel packagesLabel;

	private JComboBox packagesComboBox;

	// End of variables declaration

	/** Creates new form ChooseableTaskListPane */
	public ChooseableTaskListPane() {
		selectedTasks = new ArrayList();
	}

	public JDialog createJDialog(java.awt.Frame parent, boolean modal,
			String title) {
		JDialog dialog = new JDialog(parent, modal);
		this.dialog = dialog;
		dialog.setTitle(title);
		initComponents();
		myInitComponents();
		return dialog;
	}

	private void myInitComponents() {
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		tasksScrollPane = new javax.swing.JScrollPane();
		tasksTable = new javax.swing.JTable();
		buttonsPanel = new javax.swing.JPanel();
		cancelButton = new javax.swing.JButton();
		okButton = new javax.swing.JButton();
		packagesPanel = new JPanel();
		packagesLabel = new JLabel();
		packagesComboBox = new JComboBox();

		dialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				cancelButtonActionPerformed(null);
			}
		});

		tasksTable.setModel(new TasksTableModel());
		tasksScrollPane.setViewportView(tasksTable);

		dialog.getContentPane().add(tasksScrollPane,
				java.awt.BorderLayout.CENTER);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		okButton.setText("Ok");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		dialog.getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

		packagesLabel.setText("Package:");
		packagesComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pkgName = (String) packagesComboBox.getSelectedItem();
				((TasksTableModel) tasksTable.getModel()).setPackage(pkgName);
			}
		});
		updatePackages();
		packagesPanel.add(packagesLabel);
		packagesPanel.add(packagesComboBox);
		dialog.getContentPane().add(packagesPanel, java.awt.BorderLayout.NORTH);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		dialog.setBounds((screenSize.width - 350) / 2,
				(screenSize.height - 358) / 2, 350, 358);
	}

	private void updatePackages() {
		Iterator it;
		PackageInfo pkgInfo;

		packagesComboBox.removeAllItems();
		it = PackageManager.getInstance().getActivePackages().iterator();
		while (it.hasNext()) {
			pkgInfo = (PackageInfo) it.next();
			packagesComboBox.addItem(pkgInfo.getName());
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dialog.dispose();
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int[] selectedRows = tasksTable.getSelectedRows();
		TasksTableModel model = (TasksTableModel) tasksTable.getModel();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedTasks.add(model.getTask(selectedRows[i]));
		}
		dialog.dispose();
	}

	public ArrayList getValue() {
		return selectedTasks;
	}
}
