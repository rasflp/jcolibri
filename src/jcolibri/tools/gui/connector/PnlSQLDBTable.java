package jcolibri.tools.gui.connector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import jcolibri.connectors.JDBCTypeConverter;
import jcolibri.tools.data.DBColumn;
import jcolibri.tools.data.DBTable;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.util.CBRLogger;

/**
 * Panel to manage the database table.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class PnlSQLDBTable extends JPanel {

	private static final long serialVersionUID = 1L;

	private TbSQLDBTable tbDBTable;

	private JButton btnAddColumn, btnRemoveColumn, btnCreateTable,
			btnDropTable, btnReadTable;

	private PnlConnectorSQL pnlDefConnSQL;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefConnSQL
	 *            panel to manage the SQL connectors.
	 */
	public PnlSQLDBTable(PnlConnectorSQL pnlDefConnSQL) {
		super();
		this.pnlDefConnSQL = pnlDefConnSQL;
		createComponents();
	}

	/**
	 * Sets the panel of connector mappings. This panel will be noticated if the
	 * table structure of the database changes.
	 * 
	 * @param pnlConnMappings
	 */
	public void setPnlConnMappings(PnlSQLMappings pnlConnMappings) {
		tbDBTable.setPnlConnMappings(pnlConnMappings);
	}

	/**
	 * Returns the actual structure of the database table.
	 * 
	 * @return
	 */
	public DBTable getDBTable() {
		return tbDBTable.getDBTable();
	}

	/**
	 * Adds a DBColumn.
	 * 
	 * @param dbColumn
	 */
	public void addDBColumn(DBColumn dbColumn) {
		tbDBTable.addDBColumn(dbColumn);
	}

	/**
	 * Clears the DBColumns.
	 */
	public void clearDBTable() {
		tbDBTable.clearData();
	}

	/**
	 * Creates the visual components.
	 */
	private void createComponents() {

		JScrollPane scrPnl;
		ButtonsListener btnListener;
		JPanel pnlButtons, pnlAux;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;

		// Set border and layout.
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Table structure");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);

		// Set table.
		tbDBTable = new TbSQLDBTable();
		scrPnl = new JScrollPane(tbDBTable);
		tbDBTable.setPreferredScrollableViewportSize(new Dimension(200, 100));

		// Panel of buttons.
		btnListener = new ButtonsListener();
		btnAddColumn = new JButton("Add column");
		btnAddColumn.addActionListener(btnListener);
		btnRemoveColumn = new JButton("Remove column");
		btnRemoveColumn.addActionListener(btnListener);
		btnCreateTable = new JButton("Create table");
		btnCreateTable.addActionListener(btnListener);
		btnCreateTable.setEnabled(false);
		btnDropTable = new JButton("Drop table");
		btnDropTable.addActionListener(btnListener);
		btnReadTable = new JButton("Read table");
		btnReadTable.addActionListener(btnListener);
		pnlButtons = new JPanel(new SpringLayout());
		pnlButtons.add(btnAddColumn);
		pnlButtons.add(btnRemoveColumn);
		pnlButtons.add(new JPanel());
		pnlButtons.add(btnCreateTable);
		pnlButtons.add(btnDropTable);
		pnlButtons.add(btnReadTable);
		SpringUtilities.makeCompactGrid(pnlButtons, 2, 3, 5, 5, 5, 5);
		pnlAux = new JPanel();
		pnlAux.add(pnlButtons);

		setLayout(new BorderLayout());
		add(scrPnl, BorderLayout.CENTER);
		add(pnlAux, BorderLayout.PAGE_END);
	}

	/**
	 * Listener of button events.
	 * 
	 * @author Antonio
	 */
	private class ButtonsListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddColumn) {
				tbDBTable.addNewDBColumn();
			} else if (e.getSource() == btnRemoveColumn) {
				tbDBTable.removeSelDBColumn();
			} else if (e.getSource() == btnCreateTable) {
				createTable();
			} else if (e.getSource() == btnDropTable) {
				Connection connection;
				String tableName;

				connection = pnlDefConnSQL.getSQLConnectionData()
						.createConnection();
				tableName = pnlDefConnSQL.getSQLTable();
				dropTable(connection, tableName);
			} else if (e.getSource() == btnReadTable) {
				Connection connection;
				String tableName;

				connection = pnlDefConnSQL.getSQLConnectionData()
						.createConnection();
				tableName = pnlDefConnSQL.getSQLTable();
				readTable(connection, tableName);
			}
		}

		/**
		 * Read the table structure of the database.
		 * 
		 * @param connection
		 *            JDBC connection.
		 * @param tableName
		 *            database table name.
		 */
		private void readTable(Connection connection, String tableName) {
			DatabaseMetaData dbMetaData;
			ResultSet rs;
			String name, type;

			if ((connection == null) || (tableName == ""))
				return;

			// Read columns.
			tbDBTable.removeAll();
			try {
				dbMetaData = connection.getMetaData();
				rs = dbMetaData.getColumns("", "", tableName, "");
				while (rs.next()) {
					name = rs.getString("COLUMN_NAME");
					type = JDBCTypeConverter.typeToString(rs
							.getInt("DATA_TYPE"));
					tbDBTable.addDBColumn(new DBColumn(name, type));
				}

			} catch (SQLException e) {
				CBRLogger.log(getClass(), CBRLogger.ERROR,
						"Error reading table structure " + e.getMessage());
			}
		}

		/**
		 * Creates the table in the database.
		 */
		private void createTable() {
			CBRLogger.log(getClass(), CBRLogger.ERROR, "Not implemented yet");
		}

		/**
		 * Drops the table in the database.
		 * 
		 * @param connection
		 *            JDBC connection.
		 * @param tableName
		 *            database table name.
		 */
		private void dropTable(Connection connection, String tableName) {
			Statement st;

			if ((connection == null) || (tableName == ""))
				return;

			// Read columns.
			try {
				st = connection.createStatement();
				st.execute("DROP TABLE " + tableName);
			} catch (SQLException e) {
				CBRLogger.log(getClass(), CBRLogger.ERROR,
						"Error reading table structure " + e.getMessage());
			}
		}
	}
}
