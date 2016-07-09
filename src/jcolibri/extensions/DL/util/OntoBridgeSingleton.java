/**
 * 
 */
package jcolibri.extensions.DL.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jcolibri.util.CBRLogger;

/**
 * @author Juanan
 *
 */
public class OntoBridgeSingleton
{
    private static String host;
    private static int port;
    private static String file;
    private static OntoBridge.ReasonerType type;
    
    private static OntoBridge bridge= null;
    
    public static OntoBridge getOntoBridge(){
        if(bridge == null)
            try
            {
                if((type == null) || (file == null))
                    return null;
                bridge = new OntoBridge(host,port,file,type);
            } catch (Exception e)
            { 
                CBRLogger.log(OntoBridgeSingleton.class, CBRLogger.INFO, "Cannot initialize OntoBrige.");
            }
        return bridge;         
    }
    
    /**
     * @return Returns the file.
     */
    public static String getFile()
    {
        return file;
    }
    /**
     * @param file The file to set.
     */
    public static void setFile(String file)
    {
        if((OntoBridgeSingleton.file==null)||(!OntoBridgeSingleton.file.equals(file)))
        {
            OntoBridgeSingleton.file = file;
            OntoBridgeSingleton.bridge = null;
        }
    }
    /**
     * @return Returns the host.
     */
    public static String getHost()
    {
        return host;
    }
    /**
     * @param host The host to set.
     */
    public static void setHost(String host)
    {
        if((OntoBridgeSingleton.host==null)||(!OntoBridgeSingleton.host.equals(host)))
        {
            OntoBridgeSingleton.host = host;
            OntoBridgeSingleton.bridge = null;
        }
    }
    /**
     * @return Returns the port.
     */
    public static int getPort()
    {
        return port;
    }
    /**
     * @param port The port to set.
     */
    public static void setPort(int port)
    {
        if(OntoBridgeSingleton.port != port)
        {
            OntoBridgeSingleton.port = port;
            OntoBridgeSingleton.bridge = null;
        }
    }
    /**
     * @return Returns the type.
     */
    public static OntoBridge.ReasonerType getType()
    {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public static void setType(OntoBridge.ReasonerType type)
    {
        if(OntoBridgeSingleton.type != type)
        {
            OntoBridgeSingleton.type = type;
            OntoBridgeSingleton.bridge = null;
        }
    }
    
    public static Element toXMLDOM(Document document)
    {
        Element node = document.createElement("Reasoner");
        
        Element elemAux = document.createElement("Type");
        elemAux.setTextContent(type.toString());
        node.appendChild(elemAux);

        elemAux = document.createElement("Source");
        elemAux.setTextContent(file);
        node.appendChild(elemAux);

        if(type == OntoBridge.ReasonerType.DIG)
        {        
            elemAux = document.createElement("Host");
            elemAux.setTextContent(host);
            node.appendChild(elemAux);
    
            // <Connector><Jena><Port>.
            elemAux = document.createElement("Port");
            elemAux.setTextContent(Integer.toString(port));
            node.appendChild(elemAux);
        }
                
        return node;
    }
    
    public static void fromXMLDOM(Element elem)
    {
        String reasonerType =  elem.getElementsByTagName("Type").item(0).getTextContent();
        if(reasonerType.equals("DIG"))
            type = OntoBridge.ReasonerType.DIG;
        else
            type = OntoBridge.ReasonerType.PELLET;
        file = elem.getElementsByTagName("Source").item(0).getTextContent();
        
        if(type == OntoBridge.ReasonerType.DIG)
        {
            host = elem.getElementsByTagName("Host").item(0).getTextContent();
            port = Integer.parseInt(elem.getElementsByTagName("Port").item(0).getTextContent());
        }
    }
    
}
