package jcolibri.extensions.textual.representation;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 * This class stores data in a general way using a hashtable instead of class
 * attributes.
 * </p>
 * <p>
 * It has the typical setdata() and getdata()
 * </p>
 * <p>
 * getData() has a modification that allows user to filter the returned data.
 * For example, if you are working with tokens, you may only want the tokens
 * that are not stopwords.
 * </p>
 * <p>
 * Filters are defined using hashtables. In the previous example, you should put
 * a Boolean(true) in the ISNOTSTOPWORD attribute:
 * <p>
 * Hashtable filter;
 * <p>
 * filter.put(Token.ISNOTSTOPWORD, new Boolean(true));
 * <p>
 * This way, Tokens.getData() will return only the Tokens that comply this
 * condition. This function returns a null value when the object doesn't comply
 * the conditions.
 * <p>
 * Really, filters are a Hashtable array. All the conditions stored in the same
 * Hashtable are computed with a logic AND. And later a logic OR is performed
 * with the hashtable conditions.
 * <p>
 * Also, filters are stored using a static method, so it's applied to all the
 * instances of the specified class.
 * <p>
 * To remove a filter you must store a null value as filter using
 * class.setFilter();
 * <p>
 * Developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class DataFilter {
	Hashtable _data;

	static Hashtable _filters = new Hashtable();

	public DataFilter() {
		_data = new Hashtable();
	}

	/**
	 * Returns the attribute value if it complies the filter.
	 * 
	 * @param key
	 *            Attribute name
	 * @return Atrribute value
	 */
	public Object getData(String key) {
		if (checkConditions())
			return _data.get(key);
		else
			return null;
	}

	/**
	 * Stores data.
	 * 
	 * @param key
	 *            Attribute name
	 * @param _object
	 *            Attribute value
	 */
	public void setData(String key, Object _object) {
		_data.put(key, _object);
	}

	/**
	 * Set a class filter.
	 * 
	 * @param classname
	 *            Class name
	 * @param filter
	 *            filter to apply
	 */
	public static void setFilter(String classname, Hashtable[] filter) {
		if (filter == null)
			_filters.remove(classname);
		else
			_filters.put(classname, filter);
	}

	/**
	 * Returns the filter associated to the class
	 * 
	 * @param classname
	 *            Name of the class
	 * @return Filter (hashtable array)
	 */
	public static Hashtable[] getFilter(String classname) {
		return (Hashtable[]) _filters.get(classname);
	}

	/**
	 * Checks if the current object complies the conditions.
	 */
	public boolean checkConditions() {
		Hashtable[] _filter = getFilter(this.getClass().getName());
		if (_filter == null)
			return true;

		boolean result = false;
		for (int f = 0; f < _filter.length; f++) {
			Hashtable condition = _filter[f];
			boolean parcialResult = true;
			Enumeration keys = condition.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				Object o1 = condition.get(key);
				Object o2 = _data.get(key);
				parcialResult = parcialResult && o1.equals(o2);
			}
			result = result || parcialResult;
		}
		return result;
	}
}