package jcolibri.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Utility class with help method to manage classes
 */
public class JColibriClassHelper {

	private static ArrayList<String> list = new ArrayList<String>();

	static {
		list.add("jcolibri.cbrcore.CBRTerm");
	}

	/**
	 * Cheks if a class given by its name is a valid instance of CBRTerm
	 * 
	 * @param name
	 *            of the class
	 * @return true if it is a valid instance
	 */
	@SuppressWarnings("unchecked")
    public static boolean isValidCBRTermInstance(String name) {
		try {
			Class cbrterm = Class.forName("jcolibri.cbrcore.CBRTerm");
			Class aux = Class.forName(name);
			if (cbrterm.isAssignableFrom(aux)) {
				list.add("jcolibri.cbrcore.CBRTerm");
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public String[] getCBRTermInstancesInClasspath() {
		ArrayList<String> list = new ArrayList<String>();
		String classpath = System.getProperty("java.class.path");
		StringTokenizer st = new StringTokenizer(classpath,
				java.io.File.pathSeparator);
		while (st.hasMoreTokens()) {
			// etc etc etc TODO maybe some day
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Serializes an object as a string.
	 * 
	 * @param o
	 *            object.
	 * @return string that represents the object.
	 * @throws IOException
	 */
	public static String serializeObject(Serializable o) throws IOException {
		final StringWriter sw = new StringWriter();
		OutputStream os = (new OutputStream() {
			public void write(int c) throws IOException {
				sw.write(c);
			}
		});
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(o);
		os.flush();
		os.close();
		return sw.toString();
	}

	/**
	 * Deserializes an object from a string.
	 * 
	 * @param str
	 *            string that represents the object.
	 * @return object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object deserializeObject(final String str)
			throws ClassNotFoundException, IOException {
		InputStream is = (new InputStream() {
			final StringReader sr = new StringReader(str);

			public int read() throws IOException {
				return sr.read();
			}
		});
		ObjectInputStream ois = new ObjectInputStream(is);
		Object aux = (Object) ois.readObject();
		ois.close();
		return aux;
	}
}
