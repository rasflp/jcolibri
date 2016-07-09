package jcolibri.connectors;


/**
 * Interface that must implement the classes which want to be used in the
 * connectors. By default, connectors manage basic java types: integers,
 * booleans, ... If developers want to use complex types, they must obey this
 * interface.
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */
public interface TypeAdaptor{
    
    /**
	 * Returns a string representation of the type.
	 * 
	 * @return
	 */
	public abstract String toString();

	/**
	 * Reads the type from a string.
	 * 
	 * @param content
	 */
	public abstract void fromString(String content) throws Exception;
}