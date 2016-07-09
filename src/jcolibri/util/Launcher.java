/**
 * 
 */
package jcolibri.util;


/**
 * This class launches jCOLIBRI
 * @author Juanan
 *
 */
public class Launcher
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String cp = CompileAndRun.getClassPath();
        System.out.println("Setting classpath:");
        System.out.println(cp);
        String command = "java -classpath " + cp + "  " + "jcolibri.gui.CBRArmGUI";
        System.out.println("Executing: " + command);
        try
        {
            Runtime.getRuntime().exec(command);
        } catch (Exception e)
        {
            System.err.println("Error launching jCOLIBRI");
            e.printStackTrace();
        }
        System.exit(0);
    }

}
