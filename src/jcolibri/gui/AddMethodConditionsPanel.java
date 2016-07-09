package jcolibri.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.DataType;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.MethodCondition;
import jcolibri.cbrcore.MethodParameter;
import jcolibri.util.ImagesContainer;
import jcolibri.util.JColibriClassHelper;

/**
 * @author Juan José Bello
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class AddMethodConditionsPanel extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private static final String UNDEFINED = "undefined";

	JPanel mainPanel = new JPanel();

	JPanel conditionsPanel = new JPanel();

	JPanel mainButtonsPanel = new JPanel();

	JButton okButton = new JButton();

	JButton cancelButton = new JButton();

	JPanel preConditionsPanel = new JPanel();

	JPanel postConditionsPanel = new JPanel();

	ArrayList preConditionsGeneralList = new ArrayList();

	JPanel preConditionsGeneralPanel = new JPanel();

	JPanel preConditionsGeneralButtonsPanel = new JPanel();

	JButton addPreconditionGeneralButton = new JButton();

	JButton deletePreconditionGeneralButton = new JButton();

	JTable preConditionsGeneralTable = new JTable();

	JScrollPane preConditionsGeneralScrollPane = new JScrollPane();

	ArrayList preConditionsParameterList = new ArrayList();

	JPanel preConditionsParameterPanel = new JPanel();

	JPanel preConditionsParameterButtonsPanel = new JPanel();

	JButton addPreconditionParameterButton = new JButton();

	JButton deletePreconditionParameterButton = new JButton();

	JTable preConditionsParameterTable = new JTable();

	JScrollPane preConditionsParameterScrollPane = new JScrollPane();

	ArrayList postConditionsGeneralList = new ArrayList();

	JPanel postConditionsGeneralPanel = new JPanel();

	JPanel postConditionsGeneralButtonsPanel = new JPanel();

	JButton addPostconditionGeneralButton = new JButton();

	JButton deletePostconditionGeneralButton = new JButton();

	JTable postConditionsGeneralTable = new JTable();

	JScrollPane postConditionsGeneralScrollPane = new JScrollPane();

	ArrayList postConditionsParameterList = new ArrayList();

	JPanel postConditionsParameterPanel = new JPanel();

	JPanel postConditionsParameterButtonsPanel = new JPanel();

	JButton addPostconditionParameterButton = new JButton();

	JButton deletePostconditionParameterButton = new JButton();

	JTable postConditionsParameterTable = new JTable();

	JScrollPane postConditionsParameterScrollPane = new JScrollPane();

	GeneralTableModel preConditionsGeneralTableModel;

	GeneralTableModel postConditionsGeneralTableModel;

	ParameterTableModel preConditionsParameterTableModel;

	ParameterTableModel postConditionsParameterTableModel;

	HashMap preConditions;

	HashMap postConditions;

	public AddMethodConditionsPanel(HashMap preConditions,
			HashMap postConditions) {
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		preConditionsGeneralList = getGeneralConditions(preConditions);
		postConditionsGeneralList = getGeneralConditions(postConditions);
		preConditionsParameterList = getParameterConditions(preConditions);
		postConditionsParameterList = getParameterConditions(postConditions);
		myInitComponents();
	}

	private ArrayList getParameterConditions(HashMap map) {
		ArrayList list = new ArrayList();
		Object aux;
		if (map != null) {
			for (Iterator it = map.values().iterator(); it.hasNext();) {
				aux = it.next();
				if (aux instanceof MethodParameter)
					list.add(aux);
			}
		}
		return list;
	}

	private ArrayList getGeneralConditions(HashMap map) {
		ArrayList list = new ArrayList();
		Object aux;
		if (map != null) {
			for (Iterator it = map.values().iterator(); it.hasNext();) {
				aux = it.next();
				if (aux instanceof MethodCondition)
					list.add(aux);
			}
		}
		return list;
	}

	public void myInitComponents() {
		setTitle("Pre/Post conditions");
		mainPanel.setLayout(new java.awt.BorderLayout());
		conditionsPanel.setLayout(new java.awt.GridLayout(2, 1));
		preConditionsPanel.setLayout(new java.awt.GridLayout(2, 1));
		postConditionsPanel.setLayout(new java.awt.GridLayout(2, 1));
		preConditionsPanel.setBorder(new javax.swing.border.TitledBorder(
				"Pre-conditions"));
		postConditionsPanel.setBorder(new javax.swing.border.TitledBorder(
				"Post-conditions"));
		conditionsPanel.add(preConditionsPanel);
		conditionsPanel.add(postConditionsPanel);

		preConditionsGeneralPanel.setLayout(new java.awt.BorderLayout());
		preConditionsGeneralPanel
				.setBorder(new javax.swing.border.TitledBorder("Method input"));
		preConditionsParameterPanel.setLayout(new java.awt.BorderLayout());
		preConditionsParameterPanel
				.setBorder(new javax.swing.border.TitledBorder(
						"Input parameters"));

		postConditionsGeneralPanel.setLayout(new java.awt.BorderLayout());
		postConditionsGeneralPanel
				.setBorder(new javax.swing.border.TitledBorder("Method output"));
		postConditionsParameterPanel.setLayout(new java.awt.BorderLayout());
		postConditionsParameterPanel
				.setBorder(new javax.swing.border.TitledBorder(
						"Output parameters"));

		preConditionsPanel.add(preConditionsGeneralPanel);
		preConditionsPanel.add(preConditionsParameterPanel);
		postConditionsPanel.add(postConditionsGeneralPanel);
		postConditionsPanel.add(postConditionsParameterPanel);

		mainPanel.add(conditionsPanel, java.awt.BorderLayout.CENTER);

		// main button panel
		okButton.setText("Ok");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		mainButtonsPanel.add(okButton);
		mainButtonsPanel.add(cancelButton);
		mainPanel.add(mainButtonsPanel, java.awt.BorderLayout.SOUTH);

		// First panel
		preConditionsGeneralButtonsPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT));

		addPreconditionGeneralButton.setIcon(ImagesContainer.NEW);
		addPreconditionGeneralButton.setPreferredSize(new java.awt.Dimension(
				20, 20));
		addPreconditionGeneralButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						addPreconditionGeneralButtonActionPerformed(evt);
					}
				});

		preConditionsGeneralButtonsPanel.add(addPreconditionGeneralButton);

		deletePreconditionGeneralButton.setIcon(ImagesContainer.DELETE);
		deletePreconditionGeneralButton
				.setPreferredSize(new java.awt.Dimension(20, 20));
		deletePreconditionGeneralButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						deletePreconditionGeneralButtonActionPerformed(evt);
					}
				});

		preConditionsGeneralButtonsPanel.add(deletePreconditionGeneralButton);
		preConditionsGeneralPanel.add(preConditionsGeneralButtonsPanel,
				java.awt.BorderLayout.NORTH);
		preConditionsGeneralTableModel = new GeneralTableModel(
				preConditionsGeneralList);
		preConditionsGeneralTable.setModel(preConditionsGeneralTableModel);
		preConditionsGeneralScrollPane
				.setViewportView(preConditionsGeneralTable);
		preConditionsGeneralPanel.add(preConditionsGeneralScrollPane,
				java.awt.BorderLayout.CENTER);

		// second panel
		preConditionsParameterButtonsPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT));

		addPreconditionParameterButton.setIcon(ImagesContainer.NEW);
		addPreconditionParameterButton.setPreferredSize(new java.awt.Dimension(
				20, 20));
		addPreconditionParameterButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						addPreconditionParameterButtonActionPerformed(evt);
					}
				});

		preConditionsParameterButtonsPanel.add(addPreconditionParameterButton);

		deletePreconditionParameterButton.setIcon(ImagesContainer.DELETE);
		deletePreconditionParameterButton
				.setPreferredSize(new java.awt.Dimension(20, 20));
		deletePreconditionParameterButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						deletePreconditionParameterButtonActionPerformed(evt);
					}
				});

		preConditionsParameterButtonsPanel
				.add(deletePreconditionParameterButton);
		preConditionsParameterPanel.add(preConditionsParameterButtonsPanel,
				java.awt.BorderLayout.NORTH);
		preConditionsParameterTableModel = new ParameterTableModel(
				preConditionsParameterList);
		preConditionsParameterTable.setModel(preConditionsParameterTableModel);
		preConditionsParameterScrollPane
				.setViewportView(preConditionsParameterTable);
		preConditionsParameterPanel.add(preConditionsParameterScrollPane,
				java.awt.BorderLayout.CENTER);

		// third panel
		postConditionsGeneralButtonsPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT));

		addPostconditionGeneralButton.setIcon(ImagesContainer.NEW);
		addPostconditionGeneralButton.setPreferredSize(new java.awt.Dimension(
				20, 20));
		addPostconditionGeneralButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						addPostconditionGeneralButtonActionPerformed(evt);
					}
				});

		postConditionsGeneralButtonsPanel.add(addPostconditionGeneralButton);

		deletePostconditionGeneralButton.setIcon(ImagesContainer.DELETE);
		deletePostconditionGeneralButton
				.setPreferredSize(new java.awt.Dimension(20, 20));
		deletePostconditionGeneralButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						deletePostconditionGeneralButtonActionPerformed(evt);
					}
				});

		postConditionsGeneralButtonsPanel.add(deletePostconditionGeneralButton);
		postConditionsGeneralPanel.add(postConditionsGeneralButtonsPanel,
				java.awt.BorderLayout.NORTH);
		postConditionsGeneralTableModel = new GeneralTableModel(
				postConditionsGeneralList);
		postConditionsGeneralTable.setModel(postConditionsGeneralTableModel);
		postConditionsGeneralScrollPane
				.setViewportView(postConditionsGeneralTable);
		postConditionsGeneralPanel.add(postConditionsGeneralScrollPane,
				java.awt.BorderLayout.CENTER);

		// fourth panel
		postConditionsParameterButtonsPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.RIGHT));

		addPostconditionParameterButton.setIcon(ImagesContainer.NEW);
		addPostconditionParameterButton
				.setPreferredSize(new java.awt.Dimension(20, 20));
		addPostconditionParameterButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						addPostconditionParameterButtonActionPerformed(evt);
					}
				});

		postConditionsParameterButtonsPanel
				.add(addPostconditionParameterButton);

		deletePostconditionParameterButton.setIcon(ImagesContainer.DELETE);
		deletePostconditionParameterButton
				.setPreferredSize(new java.awt.Dimension(20, 20));
		deletePostconditionParameterButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						deletePostconditionParameterButtonActionPerformed(evt);
					}
				});

		postConditionsParameterButtonsPanel
				.add(deletePostconditionParameterButton);
		postConditionsParameterPanel.add(postConditionsParameterButtonsPanel,
				java.awt.BorderLayout.NORTH);
		postConditionsParameterTableModel = new ParameterTableModel(
				postConditionsParameterList);
		postConditionsParameterTable
				.setModel(postConditionsParameterTableModel);
		postConditionsParameterScrollPane
				.setViewportView(postConditionsParameterTable);
		postConditionsParameterPanel.add(postConditionsParameterScrollPane,
				java.awt.BorderLayout.CENTER);

		getContentPane().add(mainPanel);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 500) / 2, (screenSize.height - 700) / 2,
				500, 600);
	}

	private void deletePreconditionGeneralButtonActionPerformed(ActionEvent e) {
		int sel = preConditionsGeneralTable.getSelectedRow();
		if (sel != -1)
			preConditionsGeneralTableModel.deleteParameter(sel);
	}

	private void addPreconditionGeneralButtonActionPerformed(ActionEvent e) {
		preConditionsGeneralTableModel.addParameter();
	}

	private void deletePreconditionParameterButtonActionPerformed(ActionEvent e) {
		int sel = preConditionsParameterTable.getSelectedRow();
		if (sel != -1)
			preConditionsParameterTableModel.deleteParameter(sel);
	}

	private void addPreconditionParameterButtonActionPerformed(ActionEvent e) {
		preConditionsParameterTableModel.addParameter();
	}

	private void deletePostconditionGeneralButtonActionPerformed(ActionEvent e) {
		int sel = postConditionsGeneralTable.getSelectedRow();
		if (sel != -1)
			postConditionsGeneralTableModel.deleteParameter(sel);
	}

	private void addPostconditionGeneralButtonActionPerformed(ActionEvent e) {
		postConditionsGeneralTableModel.addParameter();
	}

	private void deletePostconditionParameterButtonActionPerformed(ActionEvent e) {
		int sel = postConditionsParameterTable.getSelectedRow();
		if (sel != -1)
			postConditionsParameterTableModel.deleteParameter(sel);
	}

	private void addPostconditionParameterButtonActionPerformed(ActionEvent e) {
		postConditionsParameterTableModel.addParameter();

	}

	private void updateConditions(List generalList, List parameterList,
			HashMap conditions) {
		Object aux;
		for (Iterator it = generalList.iterator(); it.hasNext();) {
			aux = it.next();
			conditions.put(((MethodCondition) aux).getName(), aux);
		}
		for (Iterator it = parameterList.iterator(); it.hasNext();) {
			aux = it.next();
			conditions.put(((MethodParameter) aux).getName(), aux);
		}
	}

	private void okButtonActionPerformed(ActionEvent e) {
		updateConditions(preConditionsGeneralList, preConditionsParameterList,
				preConditions);
		updateConditions(postConditionsGeneralList,
				postConditionsParameterList, postConditions);
		this.dispose();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	/*
	 * public static void main(String[] args) { JFrame frame = new
	 * AddMethodConditionsPanel(new HashMap(), new HashMap()); frame.show(); }
	 */

	public class GeneralTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		ArrayList conditions;

		String[] columns = new String[] { "CBRTerm", "Min ocurrences",
				"Max ocurrences" };

		public GeneralTableModel(ArrayList conditions) {
			this.conditions = conditions;
		}

		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return ((MethodCondition) conditions.get(row)).getName();
			case 1:
				return ((MethodCondition) conditions.get(row))
						.getMinOcurrences();
			case 2:
				return ((MethodCondition) conditions.get(row))
						.getMaxOcurrences();
			default:
				return null;
			}
		}

		private String validOccurrences(String value) {
			try {
				new Integer(value);
			} catch (NumberFormatException nfe) {
				return UNDEFINED;
			}
			return value;
		}

		public void setValueAt(Object value, int row, int column) {
			switch (column) {
			case 0:
				if (jcolibri.util.JColibriClassHelper
						.isValidCBRTermInstance((String) value)) {
					((MethodCondition) conditions.get(row))
							.setName((String) value);
				}
				break;
			case 1:
				((MethodCondition) conditions.get(row))
						.setMinOcurrences(validOccurrences((String) value));
				break;
			case 2:
				((MethodCondition) conditions.get(row))
						.setMaxOcurrences(validOccurrences((String) value));
				break;
			}
		}

		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public int getRowCount() {
			return (conditions == null ? 0 : conditions.size());
		}

		public int getColumnCount() {
			return columns.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public List getParameters() {
			return conditions;
		}

		public void addParameter() {
			conditions.add(new MethodCondition("", UNDEFINED, UNDEFINED));
			fireTableDataChanged();
		}

		public void deleteParameter(int index) {
			conditions.remove(index);
			fireTableDataChanged();
		}
	}

	public class ParameterTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		ArrayList list;

		String[] columns = new String[] { "Name", "Description",
				"CBRTerm/DataType" };

		public ParameterTableModel(ArrayList list) {
			this.list = list;
		}

		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return ((MethodParameter) list.get(row)).getName();
			case 1:
				return ((MethodParameter) list.get(row)).getDescription();
			case 2:
				if (((MethodParameter) list.get(row)).getType().equals(
						DataTypesRegistry.getCBRTERMDataType()))
					return ((MethodParameter) list.get(row)).getType()
							.toString();// .getSubType();
				else
					return ((MethodParameter) list.get(row)).getType()
							.toString();
			default:
				return null;
			}
		}

		public void setValueAt(Object value, int row, int column) {
			switch (column) {
			case 0:
				((MethodParameter) list.get(row)).setName((String) value);
				break;
			case 1:
				((MethodParameter) list.get(row))
						.setDescription((String) value);
				break;
			case 2:
				DataType type;
				if (JColibriClassHelper.isValidCBRTermInstance((String) value)) {
					// type = new
					// DataType(DataTypesRegistry.getInstance().getCBRTERMDataType().toString());
					// type.setSubType((String) value);
					type = new DataType((String) value);
					((MethodParameter) list.get(row)).setType(type);
				} else if (DataTypesRegistry.getInstance().existsDataType(
						(String) value)) {
					type = new DataType((String) value);
					((MethodParameter) list.get(row)).setType(type);
				}
				break;
			}
		}

		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public int getRowCount() {
			return (list == null ? 0 : list.size());
		}

		public int getColumnCount() {
			return columns.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public List getParameters() {
			return list;
		}

		public void addParameter() {
			list.add(new MethodParameter("", "", DataTypesRegistry
					.getCBRTERMDataType()));
			fireTableDataChanged();
		}

		public void deleteParameter(int index) {
			list.remove(index);
			fireTableDataChanged();
		}
	}

}
