/**
 * 
 */
package jcolibri.extensions.web.bridge;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Juanan
 *
 */
public class CopyOfSynchronizer {

        private static Semaphore web = new Semaphore(1,true);
        private static String currentWebSession = null;        
        private static Semaphore cycle = new Semaphore(0,true);
        
        private static Semaphore turnWeb = new Semaphore(0,true);
        private static Semaphore turnCycle = new Semaphore(1,true);

        private static void log()
        {
            
            System.out.println("LOG");
            System.out.println(web.availablePermits());
            System.out.println(cycle.availablePermits());
            System.out.println(turnWeb.availablePermits());
            System.out.println(turnCycle.availablePermits());
            
        }
        
        
        public static void webSessionAdquieresCycle(String id) throws SessionOutException
        {
            System.out.println("web session adquieres cycle");
            log();
            if(web.tryAcquire())
            {
                currentWebSession = id;
                cycle.release();
                turnCycle = new Semaphore(0,true);
                turnWeb   = new Semaphore(0,true);
            }
            else throw new SessionOutException();
            log();

        }
        
        public static void cycleWaitsForWebSession()
        {
            System.out.println("cycle waits for web session");
            log();
            try
            {
                cycle.acquire();
            } catch (InterruptedException e)
            { }
            log();

        }
        
        public static void endCurrentWebSession()
        {
            log();
            System.out.println("end current web session");
            currentWebSession = null;            
            web.release();
            log();
        }
        
        
        public static void webSessionReleasesTurn(String id) throws SessionOutException
        {
            log();
            System.out.println("web session releases turn");
            if(!id.equals(currentWebSession))
                throw new SessionOutException();
            turnCycle.release();
            log();
        }
        
        public static void webSessionWaitsTurn(String id) throws SessionOutException
        {
            log();
            System.out.println("web session waits turn");
            if(!id.equals(currentWebSession))
                throw new SessionOutException();
            try
            {
                turnWeb.acquire();
            } catch (InterruptedException e)
            { }            
            log();

        }
        
        public static void cycleReleasesTurn()
        {
            log();
            System.out.println("cycle releases turn");
            turnWeb.release();
            log();
        }
        
        public static void cycleWaitsTurn(long timeout) throws SessionOutException
        {
            log();
            System.out.println("cycle waits turn");
            try
            {
                if(!turnCycle.tryAcquire(timeout, TimeUnit.SECONDS))
                {
                    endCurrentWebSession();
                    throw new SessionOutException();
                }
                log();

            } catch (InterruptedException e)
            {  }
        }
    
}
