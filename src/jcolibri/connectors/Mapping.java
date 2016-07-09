package jcolibri.connectors;

import java.util.Hashtable;
import java.util.List;

/**
 * Auxiliar class that store data mapping information. It must not be used
 * outside the package.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public class Mapping {

	/** Information about the attribute part of a mapping. */
	private class MapInfo {
		/** Attribute name. */
		String attribute;

		/** Relation name. */
		String relation;

		/** Weight. */
		double weight;

		/** Type of the attribute. */
		String type;
	}

	/** Mapping between the columns and the MapInfo. */
	private Hashtable<String, MapInfo> column2MapInfo;

	/** Columns of the persistence layer. */
	private List<String> columns;

	/**
	 * Constructor.
	 * 
	 */
	public Mapping() {
		column2MapInfo = new Hashtable<String,MapInfo>();
		columns = null;
	}

	/**
	 * Adds a mapping between a column and an attribute.
	 * 
	 * @param column
	 *            column name.
	 * @param attribute
	 *            attribute name.
	 * @param relation
	 *            relation name.
	 * @param weight
	 *            weight of the relation.
	 * @param type
	 *            type name of the attribute.
	 */
	public void map(String column, String attribute, String relation, double weight,
			String type) {
		MapInfo info = new MapInfo();
		info.attribute = attribute;
		info.relation = relation;
		info.weight = weight;
		info.type = type;

		column2MapInfo.put(column, info);
	}

	/**
	 * Sets the column names of the persistence layer.
	 * 
	 * @param l
	 */
	public void setColumns(List<String> l) {
		columns = l;
	}

	/**
	 * Return the columns names of the persistence layer.
	 * 
	 * @return the columns.
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * Returns the relation name associated to a column.
	 * 
	 * @param column
	 *            column name.
	 * @return relation name.
	 */
	public String getRelation(String column) {
		MapInfo info = (MapInfo) column2MapInfo.get(column);
		if (info == null)
			return null;
		return info.relation;
	}

	/**
	 * Returns the attribute name associated to a column.
	 * 
	 * @param column
	 *            column name.
	 * @return attribute name.
	 */
	public String getAttribute(String column) {
		MapInfo info = (MapInfo) column2MapInfo.get(column);
		if (info == null)
			return null;
		return info.attribute;
	}

	/**
	 * Returns the weight associated to a column.
	 * 
	 * @param column
	 *            column name.
	 * @return weight.
	 */
	public double getWeight(String column) {
		MapInfo info = (MapInfo) column2MapInfo.get(column);
		if (info == null)
			return 1.0;
		return info.weight;
	}

	/**
	 * Returns the type name associated to a column.
	 * 
	 * @param column
	 *            column name.
	 * @return type name.
	 */
	public String getType(String column) {
		MapInfo info = (MapInfo) column2MapInfo.get(column);
		if (info == null)
			return null;
		return info.type;
	}
}