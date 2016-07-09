/**
 * 
 */
package jcolibri.tools.gui.casestruct.DL;


import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import jcolibri.extensions.DL.util.OntoBridge;
import jcolibri.extensions.DL.util.OntoBridgeSingleton;
import jcolibri.extensions.DL.util.OntoBridge.ReasonerType;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.util.CBRLogger;
import jcolibri.util.ImagesContainer;

/**
 * @author Juanan
 *
 */
public class PnlReasonerProperties extends JPanel
{

    private static final long serialVersionUID = 1L;
    
    private JLabel lblSourceCaseBase, lblHost, lblPort, lblType;

    private JButton btSourceCaseBase/*, btConnector*/;
    
    private JTextField tfSourceCaseBase, tfHost, tfPort/*, tfConnector*/;
    
    private JComboBox cbType;

    
    private static PnlReasonerProperties _instance = null;
    public static PnlReasonerProperties getInstance()
    {
        if(_instance == null)
            _instance = new PnlReasonerProperties();
        return _instance;
    }
    
    private PnlReasonerProperties()
    {
        createComponents();
    }
    
    public void configureOntoBridge()
    {
        String pth = this.tfSourceCaseBase.getText().trim(); 
        String host = this.tfHost.getText().trim();
        int port = Integer.valueOf(tfPort.getText()).intValue();
        ReasonerType type = (ReasonerType) cbType.getSelectedItem();
        
        OntoBridgeSingleton.setFile(pth);
        OntoBridgeSingleton.setHost(host);
        OntoBridgeSingleton.setPort(port);
        OntoBridgeSingleton.setType(type);
    }
    
    public void configureFromOntoBridge()
    {
        this.tfSourceCaseBase.setText(OntoBridgeSingleton.getFile());
        this.tfHost.setText(OntoBridgeSingleton.getHost());
        this.tfPort.setText(Integer.toString(OntoBridgeSingleton.getPort()));
        cbType.setSelectedItem(OntoBridgeSingleton.getType());
        OntoBridgeSingleton.getOntoBridge();
    }
    
    /**
     * Returns de form data necessary to create a connection to a Reasoner.
     * 
     * @return a JenaConnectionData object with the necessary data.
     * */
    public OntoBridge getJenaConnectionData(){
        try{

        configureOntoBridge();
        OntoBridge data = OntoBridgeSingleton.getOntoBridge();
        if(data == null)
            CBRLogger.log(this.getClass(),CBRLogger.INFO,"Cannot obtain OntoBridge connection with reasoner");
        return data;
        }
        catch(Exception e){
            CBRLogger.log(this.getClass(),CBRLogger.INFO,"Cannot obtain OntoBridge connection with reasoner");
            return null;
        }
        
    }
    
    public String getOWLFile(){
        return tfSourceCaseBase.getText();
    }
    
    public ReasonerType getReasonerType()
    {
        return (ReasonerType)cbType.getSelectedItem();
    }
    
    public String getHost()
    {
        return tfHost.getText();
    }
    
    public int getPort()
    {
        return Integer.parseInt(tfPort.getText());
    }
    
    /**
     * Creates the panel of properties.
     * */
    private void createComponents(){
        JPanel pnlAux1,pnlAux2;
        Border lineBorder, titleBorder, emptyBorder, compoundBorder;
        
//       Set border and layout.
        emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        titleBorder = BorderFactory
                .createTitledBorder(lineBorder, "Properties");
        compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
                emptyBorder);
        JPanel pnlRes = new JPanel();
        pnlRes.setBorder(compoundBorder);
        pnlRes.setLayout(new GridLayout(1, 2, 10, 0));
                
        //Set components for properties.
        lblSourceCaseBase = new JLabel("OWL File:");
        tfSourceCaseBase = new JTextField(10);
        btSourceCaseBase = new JButton("Select file");
        btSourceCaseBase.setIcon(ImagesContainer.LOAD_ARROW);
        btSourceCaseBase.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                    JDialog frame = (JDialog) btSourceCaseBase.getTopLevelAncestor();
                    FileDialog fd = new FileDialog(frame, "Load ontology from file...",
                            FileDialog.LOAD);
                    fd.setVisible(true);
                    if (fd.getFile() != null)
                        tfSourceCaseBase.setText("file:///"+fd.getDirectory() + fd.getFile());
            }

        });
                
        lblHost = new JLabel("Host:");
        tfHost = new JTextField(10);
        tfHost.setText("localhost");
        
        lblPort = new JLabel("Port:");
        tfPort = new JTextField(10);
        tfPort.setText("8080");
        
        lblType = new JLabel("Type");
        cbType = new JComboBox();
        
        cbType.addItem(ReasonerType.PELLET);
        cbType.addItem(ReasonerType.DIG);

        
        cbType.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                ReasonerType rt = (ReasonerType)cbType.getSelectedItem();
                boolean enabled =  rt == ReasonerType.DIG;;
                tfHost.setEnabled(enabled);
                lblHost.setEnabled(enabled);
                lblPort.setEnabled(enabled);
                tfPort.setEnabled(enabled);   
            }
     
        });
        

        tfHost.setEnabled(false);
        lblHost.setEnabled(false);
        lblPort.setEnabled(false);
        tfPort.setEnabled(false);  
        
        //First column of components.
        pnlAux1 = new JPanel(new SpringLayout());
        pnlAux1.add(lblType);
        pnlAux1.add(cbType);
        pnlAux1.add(new JPanel());
        pnlAux1.add(lblSourceCaseBase);
        pnlAux1.add(tfSourceCaseBase);
        pnlAux1.add(btSourceCaseBase);
        emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        titleBorder = BorderFactory
                .createTitledBorder(lineBorder, "");
        compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
                emptyBorder);
        SpringUtilities.makeCompactGrid(pnlAux1, 2, 3, 5, 5, 5, 5);
        
        //Second column of components.
        pnlAux2 = new JPanel(new SpringLayout());

        
        pnlAux2.add(lblHost);
        pnlAux2.add(tfHost);
        
        pnlAux2.add(lblPort);
        pnlAux2.add(tfPort);
        
        SpringUtilities.makeCompactGrid(pnlAux2, 2, 2, 5, 5, 5, 5);     
        pnlRes.add(pnlAux1);
        pnlRes.add(pnlAux2);
        this.add(pnlRes);
    }
    

}
