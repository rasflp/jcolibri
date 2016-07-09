package jcolibri.tools.gui.datatype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;
import jcolibri.datatypes.StringEnumType;
import jcolibri.util.ImagesContainer;

/**
 * Frame to manage data types.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class FrDataTypes extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblPkg;

	private JComboBox cbPkg;

	private JButton btnLoadDT, btnSaveDT;

	private TbDataTypes tbDataTypes;

	private JButton btnAddDT, btnRemoveDT;

	private LstEnumValues lstEnumValues;

	private JButton btnAddValue, btnRemoveValue, btnUpValue, btnDownValue;

	private ButtonListener btnListener;

	/**
	 * Constructor.
	 */
	public FrDataTypes() {
		super();
		createComponents();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {
		JPanel pnlNorth, pnlDataTypes, pnlEnumValues;

        this.setFrameIcon(jcolibri.util.ImagesContainer.DATA_TYPE);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Manage DataTypes");
		setLayout(new BorderLayout());

		btnListener = new ButtonListener();
		pnlDataTypes = createPnlDataTypes();
		pnlEnumValues = createPnlEnumValues();
		pnlNorth = createPnlNorth();
		add(pnlNorth, BorderLayout.PAGE_START);
		add(pnlDataTypes, BorderLayout.CENTER);
		add(pnlEnumValues, BorderLayout.PAGE_END);

		setBounds(0, 0, 750, 450);
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlNorth() {
		JPanel pnlPkg, pnlLoadSave, pnlAux, pnlRes;
		ComboBoxListener cbLst;

		// Combobox packages.
		cbLst = new ComboBoxListener();
		lblPkg = new JLabel("Package:");
		cbPkg = new JComboBox();
		cbPkg.addActionListener(cbLst);
		updatePackages();
		pnlPkg = new JPanel();
		pnlPkg.add(lblPkg);
		pnlPkg.add(cbPkg);

		// Buttons of load and save.
		btnLoadDT = new JButton("Load data types");
        btnLoadDT.setIcon(ImagesContainer.LOAD);
		btnLoadDT.addActionListener(btnListener);
		btnSaveDT = new JButton("Save data types");
        btnSaveDT.setIcon(ImagesContainer.SAVE);
		btnSaveDT.addActionListener(btnListener);
		pnlLoadSave = new JPanel();
		pnlLoadSave.add(btnLoadDT);
		pnlLoadSave.add(btnSaveDT);

		pnlAux = new JPanel();
		pnlAux.add(pnlPkg);
		pnlAux.add(pnlLoadSave);

		pnlRes = new JPanel(new BorderLayout());
		pnlRes.add(pnlAux, BorderLayout.LINE_START);

		return pnlRes;
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlDataTypes() {
		JPanel pnlButtons, pnlRes;
		JScrollPane scPnl;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder, "DataTypes");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		pnlRes = new JPanel(new BorderLayout());
		pnlRes.setBorder(compoundBorder);

		// Set table of local similarities.
		tbDataTypes = new TbDataTypes();
		// tbDataTypes.getSelectionModel().addListSelectionListener(new
		// TableListener());
		scPnl = new JScrollPane(tbDataTypes);
		tbDataTypes.setPreferredScrollableViewportSize(new Dimension(200, 100));
		tbDataTypes.getSelectionModel().addListSelectionListener(
				new TableListener());

		// Panel with buttons.
		btnAddDT = new JButton("Add datatype");
        btnAddDT.setIcon(ImagesContainer.ADD);
		btnAddDT.addActionListener(btnListener);
		btnRemoveDT = new JButton("Remove datatype");
        btnRemoveDT.setIcon(ImagesContainer.REMOVE);
		btnRemoveDT.addActionListener(btnListener);
		pnlButtons = new JPanel();
		pnlButtons.add(btnAddDT);
		pnlButtons.add(btnRemoveDT);

		pnlRes.add(scPnl, BorderLayout.CENTER);
		pnlRes.add(pnlButtons, BorderLayout.PAGE_END);
		return pnlRes;
	}

	/**
	 * Creates the visual components.
	 * 
	 * @return
	 */
	private JPanel createPnlEnumValues() {
		JPanel pnlButtons, pnlRes, pnlAux;
		JScrollPane scrPnl;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Enumeration values");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);

		// Set list of enum values.
		lstEnumValues = new LstEnumValues();
		scrPnl = new JScrollPane(lstEnumValues);
		scrPnl.setPreferredSize(new Dimension(150, 80));
		scrPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Panel with buttons.
		btnAddValue = new JButton("Add");
        btnAddValue.setIcon(ImagesContainer.ADD);
		btnAddValue.addActionListener(btnListener);
		btnRemoveValue = new JButton("Remove");
        btnRemoveValue.setIcon(ImagesContainer.REMOVE);
		btnRemoveValue.addActionListener(btnListener);
		btnUpValue = new JButton("Up");
        btnUpValue.setIcon(ImagesContainer.UP);
		btnUpValue.addActionListener(btnListener);
		btnDownValue = new JButton("Down");
        btnDownValue.setIcon(ImagesContainer.DOWN);
		btnDownValue.addActionListener(btnListener);
		pnlButtons = new JPanel();
		pnlButtons.add(btnAddValue);
		pnlButtons.add(btnRemoveValue);
		pnlButtons.add(btnUpValue);
		pnlButtons.add(btnDownValue);
		setEnablePnlEnumValues(false);

		pnlAux = new JPanel(new BorderLayout());
		pnlAux.add(scrPnl, BorderLayout.CENTER);
		pnlAux.add(pnlButtons, BorderLayout.PAGE_END);

		pnlRes = new JPanel();
		pnlRes.setBorder(compoundBorder);
		pnlRes.add(pnlAux);
		return pnlRes;
	}

	/**
	 * Updates the list of packages.
	 */
	private void updatePackages() {
		PackageInfo pkgInfo;
		Iterator it;

		it = PackageManager.getInstance().getActivePackages().iterator();
		cbPkg.removeAllItems();
		while (it.hasNext()) {
			pkgInfo = (PackageInfo) it.next();
			cbPkg.addItem(pkgInfo.getName());
		}
	}

	/**
	 * Enables or disables the enum values panel.
	 * 
	 * @param enable
	 */
	private void setEnablePnlEnumValues(boolean enable) {
		btnAddValue.setEnabled(enable);
		btnRemoveValue.setEnabled(enable);
		btnUpValue.setEnabled(enable);
		btnDownValue.setEnabled(enable);
	}

	/**
	 * Listener of the combobox types of similarities.
	 * 
	 * @author Antonio
	 */
	class ComboBoxListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbPkg) {
				setActivePackage();
			}

		}

		/**
		 * Notifies about the working package.
		 */
		private void setActivePackage() {
			String pkgName;

			pkgName = (String) cbPkg.getSelectedItem();
			tbDataTypes.setActivePackage(pkgName);
			tbDataTypes.getSelectionModel().setSelectionInterval(-1, -1);
		}
	}

	/**
	 * Listener of the combobox types of similarities.
	 * 
	 * @author Antonio
	 */
	class ButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnLoadDT) {
				tbDataTypes.reloadDataTypes();
			} else if (e.getSource() == btnSaveDT) {
				tbDataTypes.saveDataTypes();
			} else if (e.getSource() == btnAddDT) {
				tbDataTypes.addNewDataType();
			} else if (e.getSource() == btnRemoveDT) {
				tbDataTypes.removeSelDataType();
			} else if (e.getSource() == btnAddValue) {
				String value = JOptionPane.showInputDialog("New value:");
				if (value != null)
					lstEnumValues.addValue(value);
			} else if (e.getSource() == btnRemoveValue) {
				lstEnumValues.removeSelValue();
			} else if (e.getSource() == btnUpValue) {
				lstEnumValues.moveUpSelValue();
			} else if (e.getSource() == btnDownValue) {
				lstEnumValues.moveDownSelValue();
			}
		}
	}

	/**
	 * Data types table listener.
	 * 
	 * @author Antonio
	 */
	private class TableListener implements ListSelectionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent arg0) {
			DataType dataType;
			StringEnumType enumType;

			if (!arg0.getValueIsAdjusting()) {
				if (tbDataTypes.getSelectedRow() >= 0) {
					dataType = tbDataTypes.getDataType(tbDataTypes
							.getSelectedRow());
					if (dataType instanceof StringEnumType)
						enumType = (StringEnumType) dataType;
					else
						enumType = null;
				} else {
					enumType = null;
				}
				setEnablePnlEnumValues(enumType != null);
				lstEnumValues.setEnumType(enumType);
			}
		}
	}
}
