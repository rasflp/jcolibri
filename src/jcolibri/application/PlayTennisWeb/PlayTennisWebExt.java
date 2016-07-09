/**
 * 
 */
package jcolibri.application.PlayTennisWeb;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * PlayTennis final application.
 * Runs the cycle continously until the administrator exits.
 * @author Juan A. Recio-García
 *
 */
public class PlayTennisWebExt extends PlayTennisWeb
{

    public class CycleThread extends Thread{
        PlayTennisWebExt app;
        boolean run = false;
        
        public CycleThread(PlayTennisWebExt app)
        {
            super();
            this.app = app;
        }
        public void run()
        {
            run = true;
            while(run)
               app.executeCycle();
            
        }
        
        public void stopCycle()
        {
            run = false;
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        PlayTennisWebExt app = new PlayTennisWebExt();
        app.init();
        
        //Run the preCycle
        app.executePreCycle();
        
        //Run the cycle in another Thread
        CycleThread ct = app.new CycleThread(app);
        ct.start();     
        System.out.println("PlayTennisWeb is running. Type \"exit\" to idem.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String string = "";
        do{
            try
            {
                string = br.readLine();
            } catch (IOException e)
            {}
        }while(!string.equals("exit"));
        
        //Run the post cycle
        app.executePostCycle();
        
        System.exit(0);

    }

}
