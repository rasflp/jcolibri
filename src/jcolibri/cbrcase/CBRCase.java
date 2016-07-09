package jcolibri.cbrcase;

/**
 * Interface that will represent any Case structure built to be used on JColibrí
 */
public interface CBRCase extends CBRQuery {

	public Individual getDescription();

	/**
	 * Return the solution of the case.
	 * 
	 * @return the solution of the case.
	 */
	public Individual getSolution();

	/**
	 * Returns if the result of the case.
	 * 
	 * @return the result of the case.
	 */
	public Individual getResult();
}