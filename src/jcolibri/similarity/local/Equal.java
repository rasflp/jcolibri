package jcolibri.similarity.local;

import java.util.HashMap;
import jcolibri.datatypes.StringEnum;
import jcolibri.cbrcase.Individual;
import jcolibri.similarity.SimilarityFunction;

/**
 * This function returns 1 if both individuals are equal, otherwise returns 0
 */
public class Equal implements SimilarityFunction {

	/**
	 * Constructor.
	 */
	public Equal() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#compute(jcolibri.cbrcase.Individual,
	 *      jcolibri.cbrcase.Individual)
	 */
	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(), o2.getValue());
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 *            Object.
	 * @param o2
	 *            Object.
	 * @return the result of apply the similarity function.
	 */
    private double compute(Object o1, Object o2) {
        if ((o1 == null) || (o2 == null))
            return 0;
        if((o1 instanceof StringEnum)||(o2 instanceof StringEnum))
        {
            String v1 = null;
            String v2 = null;
            if(o1 instanceof StringEnum)
            {
                StringEnum sEcase = (StringEnum)o1;
                v1 = sEcase.getValue();
            }
            else
                v1 = (String)o1;
            if(o2 instanceof StringEnum)
            {
                StringEnum sEcase = (StringEnum)o2;
                v2 = sEcase.getValue();
            }
            else
                v2 = (String)o2;
            return v1.equals(v2) ? 1 : 0;
        }
        else
            return o1.equals(o2) ? 1 : 0;
    }


	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#getParametersInfo()
	 */
	public HashMap getParametersInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {
	}
}
