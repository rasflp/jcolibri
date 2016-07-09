package jcolibri.extensions.web.bridge;

public class SessionOutException extends Exception
{
    private static final long serialVersionUID = 1L;
    public SessionOutException(String msg){
        super(msg);
    }
    
    public SessionOutException(){
        super();
    }
}
