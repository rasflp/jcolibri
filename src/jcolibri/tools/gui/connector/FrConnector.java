package jcolibri.tools.gui.connector;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jcolibri.tools.data.CaseStructure;
import jcolibri.tools.gui.casestruct.FrCaseStructure;
import jcolibri.util.ImagesContainer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Frame to manage connectors.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class FrConnector extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblType, lblCaseBase, lblCaseStFile;

	private JComboBox cbType;

	private JTextField tfCaseBase, tfCaseStFile;

	private JButton btnSaveConnector, btnLoadConnector, btnLoadCaseStFile;

	private PnlConnectorType pnlConnSQL, pnlConnText, //pnlConnJena,
			pnlConnOther;

	private JPanel pnlConnectors;

	private final static String CONN_TEXT = "connector text";

	private final static String CONN_SQL = "connector sql";

	//private final static String CONN_JENA = "connector jena";
	
	private final static String CONN_OTHER = "connector other";

	private CaseStructure caseSt;

	/**
	 * Constructor.
	 * 
	 * @param pnlCase
	 *            frame to manage case structures.
	 */
	public FrConnector(FrCaseStructure pnlCase) {
		super();
		createComponents(pnlCase);
	}

	/**
	 * Creates the visual components.
	 * 
	 * @param pnlCase
	 */
	private void createComponents(FrCaseStructure pnlCase) {
		JPanel pnlCommon;

        setFrameIcon(jcolibri.util.ImagesContainer.CONNECTOR);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Manage Connectors");
		setLayout(new BorderLayout());

		// Types of connector.
		pnlConnSQL = new PnlConnectorSQL();
		pnlConnText = new PnlConnectorText();
		//pnlConnJena = new PnlConnectorJena();
		pnlConnOther = new PnlConnectorOther();
		pnlConnectors = new JPanel();
		pnlConnectors.setLayout(new CardLayout());
		pnlConnectors.add(pnlConnSQL, CONN_SQL);
		pnlConnectors.add(pnlConnText, CONN_TEXT);
		//pnlConnectors.add(pnlConnJena, CONN_JENA);
		pnlConnectors.add(pnlConnOther, CONN_OTHER);
		add(pnlConnectors, BorderLayout.CENTER);

		// Select type of connector.
		pnlCommon = createPnlCommon();
		add(pnlCommon, BorderLayout.PAGE_START);
		setBounds(0, 0, 750, 450);
	}

	/**
	 * Creates the panel common for all connectors.
	 * 
	 * @return
	 */
	private JPanel createPnlCommon() {
		JPanel pnlRes;
		ButtonsListener btnListener;

		btnListener = new ButtonsListener();
		pnlRes = new JPanel();
		pnlRes.setLayout(new BoxLayout(pnlRes, BoxLayout.PAGE_AXIS));
		pnlRes.add(createPnlType(btnListener));
		pnlRes.add(createPnlCaseSt(btnListener));

		return pnlRes;
	}

	/**
	 * Creates panel to select the type of connector.
	 * 
	 * @param btnListener
	 * @return
	 */
	private JPanel createPnlType(ButtonsListener btnListener) {
		JPanel pnlAux, pnlRes;

		pnlRes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

		lblType = new JLabel("Type:");
		cbType = new JComboBox();
		cbType.addItem(new ConnTypeItem("SQL Database", pnlConnSQL, CONN_SQL));
		cbType.addItem(new ConnTypeItem("Plain text file", pnlConnText,
				CONN_TEXT));
		//cbType.addItem(new ConnTypeItem("DL Reasoner",pnlConnJena,CONN_JENA));
		cbType.addItem(new ConnTypeItem("Other", pnlConnOther, CONN_OTHER));
		cbType.addActionListener(new TypeConnectorListener());
		pnlAux = new JPanel();
		pnlAux.add(lblType);
		pnlAux.add(cbType);
		pnlRes.add(pnlAux);

		lblCaseBase = new JLabel("Case base:");
		tfCaseBase = new JTextField(15);
		tfCaseBase.setText("jcolibri.cbrcase.BasicCBRCaseBase");
		pnlAux = new JPanel();
		pnlAux.add(lblCaseBase);
		pnlAux.add(tfCaseBase);
		pnlRes.add(pnlAux);

		btnLoadConnector = new JButton("Load connector");
        btnLoadConnector.setIcon(jcolibri.util.ImagesContainer.LOAD);  
		btnLoadConnector.addActionListener(btnListener);
		btnSaveConnector = new JButton("Save connector");
        btnSaveConnector.setIcon(ImagesContainer.SAVE);  
        btnSaveConnector.addActionListener(btnListener);
		pnlAux = new JPanel();
		pnlAux.add(btnLoadConnector);
		pnlAux.add(btnSaveConnector);
		pnlRes.add(pnlAux);

		return pnlRes;
	}

	/**
	 * Creates panel to select the file that describes the case structure.
	 * 
	 * @param btnListener
	 * @return
	 */
	private JPanel createPnlCaseSt(ButtonsListener btnListener) {
		JPanel pnlRes;

		pnlRes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

		lblCaseStFile = new JLabel("Case structure file:");
		tfCaseStFile = new JTextField(20);
		btnLoadCaseStFile = new JButton("Load");
        btnLoadCaseStFile.setIcon(ImagesContainer.LOAD_ARROW);  
		btnLoadCaseStFile.addActionListener(btnListener);
		pnlRes.add(lblCaseStFile);
		pnlRes.add(tfCaseStFile);
		pnlRes.add(btnLoadCaseStFile);

		return pnlRes;
	}

	/**
	 * Listener of the combobox types of connectors.
	 * 
	 * @author Antonio
	 */
	class TypeConnectorListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CardLayout clConnectors;
			ConnTypeItem type;

			clConnectors = (CardLayout) pnlConnectors.getLayout();
			type = (ConnTypeItem) cbType.getSelectedItem();
			clConnectors.show(pnlConnectors, type.getIdPnlConn());
		}

	}

	/**
	 * Items of the combobox type of connector.
	 * 
	 * @author Antonio
	 */
	private class ConnTypeItem {
		private String text;

		private PnlConnectorType pnlConn;

		private String idPnlConn;

		/**
		 * Constructor.
		 * 
		 * @param text
		 *            connector name.
		 * @param pnlConn
		 *            panel to manage the connector.
		 * @param idPnlConn
		 *            id used in the combobox.
		 */
		public ConnTypeItem(String text, PnlConnectorType pnlConn,
				String idPnlConn) {
			super();

			this.text = text;
			this.pnlConn = pnlConn;
			this.idPnlConn = idPnlConn;
		}

		/**
		 * Returns the connector name.
		 * 
		 * @return
		 */
		public String getText() {
			return text;
		}

		/**
		 * Sets the connector name.
		 * 
		 * @param text
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * Returns the panel to manage the connector.
		 * 
		 * @return
		 */
		public PnlConnectorType getPnlConn() {
			return pnlConn;
		}

		/**
		 * Sets the panel to manage the connector.
		 * 
		 * @param pnlConn
		 */
		public void setPnlConn(PnlConnectorType pnlConn) {
			this.pnlConn = pnlConn;
		}

		/**
		 * Returns the id of the connector.
		 * 
		 * @return
		 */
		public String getIdPnlConn() {
			return idPnlConn;
		}

		/**
		 * Sets the id of the connector.
		 * 
		 * @param idPnlConn
		 */
		public void setIdPnlConn(String idPnlConn) {
			this.idPnlConn = idPnlConn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return text;
		}
	}

	/**
	 * Listener of the events of buttons.
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
			FileDialog fd;
			JFrame frame;

			if (e.getSource() == btnLoadConnector) {
				frame = (JFrame) btnLoadConnector.getTopLevelAncestor();
				fd = new FileDialog(frame, "Load connector from file...",
						FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getFile() != null) {
					loadConnFromFile(fd.getDirectory() + fd.getFile());
				}
			} else if (e.getSource() == btnSaveConnector) {
				ConnTypeItem type;

				frame = (JFrame) btnSaveConnector.getTopLevelAncestor();
				fd = new FileDialog(frame, "Save connector to file...",
						FileDialog.SAVE);
				fd.setFile("connector.xml");
				fd.setVisible(true);
				if (fd.getFile() != null) {
					type = (ConnTypeItem) cbType.getSelectedItem();
					type.getPnlConn().writeConnectorToFile(
							fd.getDirectory() + fd.getFile(),
							tfCaseBase.getText());
				}
			} else if (e.getSource() == btnLoadCaseStFile) {
				frame = (JFrame) btnLoadCaseStFile.getTopLevelAncestor();
				fd = new FileDialog(frame, "Load case structure file...",
						FileDialog.LOAD);
				fd.setFile(tfCaseStFile.getText());
				fd.setVisible(true);
				if (fd.getFile() != null) {
					loadCaseSt(fd.getDirectory() + fd.getFile());
				}
			}
		}

		/**
		 * Loads the case structure from a file.
		 * 
		 * @param file
		 */
		private void loadCaseSt(String file) {
			ConnTypeItem type;

			tfCaseStFile.setText(file);
			caseSt = new CaseStructure();
			caseSt.readFromXMLFile(tfCaseStFile.getText());

			type = (ConnTypeItem) cbType.getSelectedItem();
			type.getPnlConn().setCaseStructure(caseSt, file);
		}

		/**
		 * Loads the connector from a file.
		 * 
		 * @param fileName
		 */
		private void loadConnFromFile(String fileName) {
			DocumentBuilder db;
			Document doc;
			Element elem;
			ConnTypeItem type;

			try {
				// Read and parse the file.
				db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = db.parse(fileName);

				// Build the case structure.
				elem = (Element) doc.getElementsByTagName("CaseBase").item(0);
				tfCaseBase.setText(elem.getTextContent());

				if (doc.getElementsByTagName("JDBC").getLength() > 0) {
					cbType.setSelectedIndex(0);
				} else if (doc.getElementsByTagName("PlainText").getLength() > 0) {
					cbType.setSelectedIndex(1);
				} else if (doc.getElementsByTagName("Jena").getLength() > 0) {
					cbType.setSelectedIndex(2);
				} else if (doc.getElementsByTagName("OTHER").getLength() > 0) {
					cbType.setSelectedIndex(3);
				}

				try {
					elem = (Element) doc.getElementsByTagName("CaseStructure")
							.item(0);
					loadCaseSt(elem.getTextContent());
				} catch (Exception e) {

				}

				type = (ConnTypeItem) cbType.getSelectedItem();
				type.getPnlConn().readConnectorFromFile(fileName);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
