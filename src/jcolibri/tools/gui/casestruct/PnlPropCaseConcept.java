/**
 * 
 */
package jcolibri.tools.gui.casestruct;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;
import jcolibri.tools.gui.casestruct.DL.SelectConceptDialog;
import jcolibri.util.ImagesContainer;

/**
 * @author Juanan
 *
 */
public class PnlPropCaseConcept extends PnlPropType
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CaseStructure c;
    
    JTextField caseConcept;
    JPanel panel = new JPanel();
    
    public PnlPropCaseConcept()
    {
        c = new CaseStructure();
 
        caseConcept = new JTextField();
        JLabel label = new JLabel("Case Concept:");
        JButton connectToOntology = new JButton("Select Case Concept",ImagesContainer.CONCEPT);
        connectToOntology.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                SelectConceptDialog ctod = new SelectConceptDialog(true);
                ctod.setVisible(true);
                caseConcept.setText(ctod.getSelectedConcept());
                c.setConcept(ctod.getSelectedConcept());
            }
        });
        panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS) );
        panel.add(label);
        panel.add(caseConcept);
        panel.add(connectToOntology);
        this.add(panel);
    }
    
    @Override
    public String getCaseCompClassName()
    {
        return CaseStructure.class.getName()+"Concept";
    }


    
    
    @Override
    public void showCaseComp(CaseStComponent comp)
    {        
        CaseStructure cs = (CaseStructure)comp;
        caseConcept.setText(cs.getConcept());
    }

    @Override
    public CaseStComponent getCaseComp()
    {
        return c;
    }

}
