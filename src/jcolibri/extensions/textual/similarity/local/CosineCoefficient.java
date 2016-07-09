package jcolibri.extensions.textual.similarity.local;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

import jcolibri.cbrcase.Individual;
import jcolibri.extensions.textual.representation.Text;
import jcolibri.extensions.textual.representation.Token;
import jcolibri.similarity.SimilarityFunction;

/**
 * Cossine Coefficient Similarity.
 * <p>
 * This function computes: |intersection(o1,o2)| / (sqrt(|o1|)*sqrt(|o2|)).
 * </p>
 * <p>
 * Tokens filter and comparation attribute must be indicated using FILTER and
 * DATA paramethers.
 * </p>
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class CosineCoefficient implements SimilarityFunction {

	HashMap _parameters;

	public static final String DATA = "data";

	public static final String FILTER = "filter";

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#compute(jcolibri.cbrcase.Individual,
	 *      jcolibri.cbrcase.Individual)
	 */
	public double compute(Individual o1, Individual o2) {
		return this.compute(o1.getValue(), o2.getValue());
	}

	private double compute(Object o1, Object o2) {
		if ((o1 == null) || (o1 == null))
			return 0;
		if (!(o1 instanceof Text))
			return 0;
		if (!(o2 instanceof Text))
			return 0;

		Text to1 = (Text) o1;
		Text to2 = (Text) o2;

		// Default configuration
		Hashtable[] filter = new Hashtable[1];
		Hashtable condition = new Hashtable();
		condition.put(Token.ISNOT_STOPWORD, new Boolean(true));
		filter[0] = condition;

		String _data = (String) _parameters.get(DATA);
		if (_data == null)
			_data = Token.STEMMEDWORD;
		Hashtable[] _filter = (Hashtable[]) _parameters.get(FILTER);
		if (_filter == null)
			_filter = filter;

		Token.setFilter(_filter);
		Collection c1 = (Collection) to1.getTokensList();
		Collection c2 = (Collection) to2.getTokensList();
		Token.setFilter(null);

		double inter = TextualSimilarityUtils.Intersection(c1, c2, _data);

		return inter
				/ (Math.sqrt(TextualSimilarityUtils.size(c1)) * Math
						.sqrt(TextualSimilarityUtils.size(c2)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#setParameters(java.util.HashMap)
	 */
	public void setParameters(HashMap parameters) {
		_parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcolibri.similarity.SimilarityFunction#getParametersInfo()
	 */
	public HashMap getParametersInfo() {
		return _parameters;
	}

}