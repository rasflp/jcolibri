/**
 * 
 */
package jcolibri.tools.gui.casestruct;

import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStructure;

/**
 * @author Juanan
 *
 */
public class PnlPropCase extends PnlPropType
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CaseStructure c;
    
    public PnlPropCase()
    {
        c = new CaseStructure();
    }
    
    @Override
    public String getCaseCompClassName()
    {
        return CaseStructure.class.getName();
    }
   
    
    @Override
    public void showCaseComp(CaseStComponent comp)
    {        
    }

    @Override
    public CaseStComponent getCaseComp()
    {
        return c;
    }

}
