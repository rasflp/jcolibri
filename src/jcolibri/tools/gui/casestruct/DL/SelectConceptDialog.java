/**
 * 
 */
package jcolibri.tools.gui.casestruct.DL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import jcolibri.gui.CBRArmGUI;
import jcolibri.tools.gui.SpringUtilities;
import jcolibri.util.ImagesContainer;

/**
 * @author Juanan
 *
 */
public class SelectConceptDialog extends JDialog
{

    private static final long serialVersionUID = 1L;
    boolean selectConceptMandatory = false;
    JCheckBox checkBox = null;
    JButton select = null;
    
    public SelectConceptDialog(boolean selectConceptMandatory)
    {
        super(CBRArmGUI.getReference());
        this.setModal(true);
        this.selectConceptMandatory = selectConceptMandatory;
        initComponents();
        pack();
    }
    
    private String selectedConcept = null;
    
    public String getSelectedConcept(){
        return selectedConcept;
    }
    
    
    private void initComponents(){     
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.add(PnlReasonerProperties.getInstance(), BorderLayout.NORTH);

        select = new JButton("Done");

        select.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                selectConceptAction();
            }
        
        });
        JPanel p = new JPanel();
        p.add(select);
        cp.add(p, BorderLayout.SOUTH);        

        
        if(selectConceptMandatory)
        {
            cp.add(PnlConceptsTree.getInstance(), BorderLayout.CENTER);
            select.setText("Select Concept");
        } 
        /*
        else
        {
            JPanel sc = new JPanel();
            sc.setLayout(new BoxLayout(sc,BoxLayout.PAGE_AXIS));
            checkBox = new JCheckBox("Select Concept");
            checkBox.setSelected(true);
            checkBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    checkBoxAction();
                }
            });
            sc.add(checkBox);
            sc.add(PnlConceptsTree.getInstance());
            cp.add(sc, BorderLayout.CENTER);
        }
        */

        this.getContentPane().add(cp);
    }
    
    void checkBoxAction()
    {
        if(checkBox.isSelected())
        {
            PnlConceptsTree.getInstance().setEnabled(true);
            select.setText("Select Concept");
        }else
        {
            PnlConceptsTree.getInstance().setEnabled(false);
            select.setText("Done");            
        }
        
    }
    
    void selectConceptAction()
    {
        selectedConcept = PnlConceptsTree.getInstance().getSelectedConcept();
        PnlReasonerProperties.getInstance().configureOntoBridge();
        setVisible(false);
        dispose();
    }

}
