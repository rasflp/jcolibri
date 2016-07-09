/**
 * 
 */
package jcolibri.extensions.web.bridge;

import java.util.concurrent.Semaphore;


/**
 * @author Juanan
 *
 */
public class Synchronizer {

        private static Semaphore web = new Semaphore(1,true);
        private static String currentWebSession = null;        
        private static Semaphore cycle = new Semaphore(0,true);
        
        private enum Turn {WEB, CYCLE};
        
        private static Turn turn;

        
        
        public static void webSessionAdquieresCycle(String id) throws SessionOutException
        {
            if(web.tryAcquire())
            {
                currentWebSession = id;
                cycle.release();
            }
            else throw new SessionOutException();

        }
        
        public static void cycleWaitsForWebSession()
        {
            try
            {
                cycle.acquire();
            } catch (InterruptedException e)
            { }

        }
        
        public static void endCurrentWebSession()
        {
            System.out.println("end current web session");
            currentWebSession = null;            
            web.release();
        }
        
        
        public static void webSessionReleasesTurn(String id) throws SessionOutException
        {
            System.out.println("web session releases turn");
            if(!id.equals(currentWebSession))
                throw new SessionOutException();
            turn = Turn.CYCLE;
        }
        
        public static void webSessionWaitsTurn(String id) throws SessionOutException
        {
            System.out.println("web session waits turn");
            if(!id.equals(currentWebSession))
                throw new SessionOutException();
           
            try
            {
                while (turn != Turn.WEB)   
                    Thread.sleep(1000);
            } catch (InterruptedException e)
            {
            }

        }
        
        public static void cycleReleasesTurn()
        {
            System.out.println("cycle releases turn");
            turn = Turn.WEB;
        }
        
        public static void cycleWaitsTurn(long timeout) throws SessionOutException
        {
            System.out.println("cycle waits turn");
            try
            {
                
                while (turn != Turn.CYCLE)
                {
                    Thread.sleep(1000);
                    timeout--;
                    if(timeout == 0)
                        throw new SessionOutException("Timeout while cycle was waiting turn");
                }

            } catch (InterruptedException e)
            {  }
        }
    
}
