/**
 * 
 */
package jcolibri.extensions.web.method;

import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.extensions.web.TomcatLauncher;

/**
 * @author Juanan
 *
 */
public class StopTomcatMethod extends CBRMethod
{
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
     */
    @Override
    public CBRContext execute(CBRContext context) throws ExecutionException
    {
        try
        {
            TomcatLauncher.stopTomcat();
        } catch (Exception e)
        {
            throw new ExecutionException(e);
        }
        
        return context;
    }

}
