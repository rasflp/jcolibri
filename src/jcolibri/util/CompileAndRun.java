package jcolibri.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities for compile and run and application in a java source file.
 * 
 * @author Juan A. Recio-García
 */
public class CompileAndRun {

    
    protected static String getClassPath()
    {
        
        String CLASSPATH = "bin";
        
        try
        {
            DocumentBuilder db = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder();
            Document doc = db.parse(".classpath");
                  
            NodeList nl = doc.getElementsByTagName("classpathentry");
            for(int i=0; i<nl.getLength(); i++)
            {
                Node n = nl.item(i);
                NamedNodeMap nnm = n.getAttributes();
                if(nnm.getNamedItem("kind").getNodeValue().equals("lib"))
                {
                    String lib = nnm.getNamedItem("path").getNodeValue();
                    CLASSPATH += File.pathSeparator + lib;
                }
            }
        } catch (Exception e)
        {
            CBRLogger.log(CompileAndRun.class, CBRLogger.ERROR, "Error obtaining classpath. Revise your .classpath file \n"+e.getMessage());
        }

        return CLASSPATH;
    }
    
	/**
	 * Compiles and runs a java application from a java source file.
	 * 
	 * @param javaSourceFile
	 */
	public static void compileAndRun(String javaSourceFile) {
		try {
		    String CLASSPATH = getClassPath();
		    
			String command = "javac -classpath " + CLASSPATH + " -d bin "
					+ javaSourceFile;
			CBRLogger.log("jcolibri.util.CompleAndRun", CBRLogger.INFO,
					"Executing: " + command);
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			if (process.exitValue() != 0)
				throw new Exception("Error compiling" + javaSourceFile);
			String javaClassFile = javaSourceFile.replaceAll(".java", "");
			javaClassFile = javaClassFile.replace("src\\", "");
			javaClassFile = javaClassFile.replace("src/", "");
			javaClassFile = javaClassFile.replace("\\", ".");
			javaClassFile = javaClassFile.replace("/", ".");
			
			command = "java -classpath " + CLASSPATH + "  " + javaClassFile;
			CBRLogger.log("jcolibri.util.CompleAndRun", CBRLogger.INFO,
					"Executing: " + command);
			process = Runtime.getRuntime().exec(command);

			CompileAndRun cr = new CompileAndRun();
            StreamGobbler errorGobbler = cr.new
            StreamGobbler(process.getErrorStream(), "[TEMPLATE]");            
        
	        // any output?
	        StreamGobbler outputGobbler = cr.new
	            StreamGobbler(process.getInputStream(), "[TEMPLATE]");
	            
	        // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
			
		} catch (Exception e) {
			CBRLogger
					.log(
							"jcolibri.util.CompleAndRun",
							CBRLogger.ERROR,
							"Error compiling and running java application.\nCheck that javac and java applications are in your classPath\n"
									+ e.getMessage());
			e.printStackTrace();
		}
	}

	public class StreamGobbler extends Thread
	{
	    java.io.InputStream is;
	    String type;
	    
	    StreamGobbler(java.io.InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
	            java.io.BufferedReader br = new java.io.BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null)
	                System.out.println(type + ">" + line);    
	            } catch (java.io.IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	    
	}

	
	public static void main(String[] args) {
		//compileAndRun("src\\jcolibri\\application\\travel\\travel.java");
        System.out.println(getClassPath());
	}
}
