package jcolibri.extensions.web.bridge;


import java.util.HashMap;

/**
 * 
 * @author Juan A. Recio-Garcia
 */
public class WebBridge {
    
    
    private static HashMap<String,Object> data = new HashMap<String,Object>();
          
    
    public static Object getData(String key)
    {
        return data.get(key);
    }
    
    public static void putData(String key, Object d)
    {
        data.put(key,d);
    }
    
}
