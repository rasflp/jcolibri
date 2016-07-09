package jcolibri.method;

import java.util.Collection;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRQuery;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;
import jcolibri.util.ProgressBar;

/**
 * Simple adaptation method. Computes the value of a simple solution attribute
 * related to a simple description attribute as proportional to the actual
 * values of these attributes in a retrieved case.
 * 
 * @author
 * @version 1.0
 */
public class CombineQueryAndCasesMethod extends jcolibri.cbrcore.CBRMethod {
	private static final long serialVersionUID = 1L;

    /** Parameter name containing the case structure file path. */
    public static final String CASE_STRUCTURE_FILE = "Case Structure";
	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.cbrcore.CBRMethod#execute(jcolibri.cbrcore.CBRContext)
	 */
	public CBRContext execute(CBRContext context) throws ExecutionException {
		try {            
            CBRQuery query = context.getQuery();
            Collection<CBRCase> cases = context.getCases();

            ProgressBar.PROGRESSBAR.init(this.getClass().getName(), context
                    .getCases().size());
            
            for(CBRCase _case : cases)
            {           
                CaseCreatorUtils.copyIndividualValues(query.getDescription(), _case.getDescription());
                ProgressBar.PROGRESSBAR.step();
            }
                        
    	}catch(Exception e)
        {
            CBRLogger.log(
                    this.getClass(),
                    CBRLogger.ERROR,
                    "Error copying query description into working cases. Error message: "+ e.getMessage());
            e.printStackTrace();
        }
        ProgressBar.PROGRESSBAR.setVisible(false);
        return context;
    }
}
