package jcolibri.extensions.web;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import jcolibri.util.CBRLogger;


import org.apache.catalina.startup.Bootstrap;

public class TomcatLauncher
{
    private static String CATALINA_HOME = null;
    private static String CATALINA_BASE = null;
    private static Bootstrap init = null;
    
    public static void init(String CATALINA_PATH) throws Exception
    {
        CATALINA_BASE = CATALINA_HOME = CATALINA_PATH; 
        init = new Bootstrap();
        init.setCatalinaBase(CATALINA_BASE);
        init.setCatalinaHome(CATALINA_HOME); 
        init.init();
    }
    
    public static void init() throws Exception
    {
        if((CATALINA_HOME != null)&&(CATALINA_BASE != null))
            return;
        CATALINA_HOME = System.getenv("CATALINA_HOME");
        CATALINA_BASE = System.getenv("CATALINA_BASE");
        if((CATALINA_HOME != null)&&(CATALINA_BASE != null))
            return;
        CBRLogger.log(TomcatLauncher.class, CBRLogger.INFO, "Configure your CATALINA_HOME and CATALINA_BASE enviroment variables to skip choosing the Tomcat's directory");
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Select Tomcat Directory");
        int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            CATALINA_BASE = CATALINA_HOME = file.getAbsolutePath();
        } else {
            throw new Exception("Cannot find Tomcat/Catalina path");
        }

        init = new Bootstrap();
        init.setCatalinaBase(CATALINA_BASE);
        init.setCatalinaHome(CATALINA_HOME);        
        init.init();
    }
    
  
    
    public static void startTomcat() throws Exception
    {
        init.start();
        CBRLogger.log(TomcatLauncher.class, CBRLogger.INFO, "Tomcat Server started");
    }
    
    public static void stopTomcat() throws Exception
    {
        init.stop();
        CBRLogger.log(TomcatLauncher.class, CBRLogger.INFO, "Tomcat Server stoped");        
    }
    
    
    
    /**
     * Test method for Tomcat Launcher
     * @param args
     */
    public static void main(String[] args)
    {
         try
        {
            
             TomcatLauncher.init("C:\\software\\apache-tomcat-5.5.16\\apache-tomcat-5.5.16");
            
            JFrame frame = new JFrame();
            JButton bstart = new JButton("Start");
            bstart.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        TomcatLauncher.startTomcat();
                    } catch (Exception e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            JButton bstop  = new JButton("Stop");
            bstop.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        TomcatLauncher.stopTomcat();
                    } catch (Exception e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            
            JButton bexit  = new JButton("Exit");
            bexit.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        System.exit(0);
                    } catch (Exception e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(bstart);
            frame.getContentPane().add(bstop);
            frame.getContentPane().add(bexit);
            
            frame.pack();
            frame.setVisible(true);
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
