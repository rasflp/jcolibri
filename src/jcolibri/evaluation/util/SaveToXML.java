package jcolibri.evaluation.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Empresa: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveToXML {

    public static String JCOLIBRI_EVALUATION="jCOLIBRIevaluation";
    public static String ID="id";
    public static String EVALUATOR_CLASS="evaluatorClass";
    public static String CYCLE_EVALUATOR_CLASS="cycleEvaluatorClass";
    public static String PARAMETERS="parameters";
    public static String PARAMETER="parameter";
    public static String PARAMETER_NAME="parameterName";
    public static String PARAMETER_VALUE="parameterValue";
    

    /**
     * Guarda en un documento XML la configuración del evaluador. Para guardar en
     * XML se hace en forma de árbol, donde cada Element es un nodo de ese árbol,
     * y para añadir hijos a un nodo se hace mediante el método
     * padre.appendChild(hijo) donde el hijo es también un nodo
     *
     * Antes de nada es necesario crear el documento de tipo Document sobre el que
     * cargamos el árbol XML
     */
    public static void save(String nombreArchivo, String id, String evaluatorClassName, HashMap<String,String> parameters, String cycleEvaluatorClassName) {
      try {
          Document document;
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.newDocument();

          Element configuration = document.createElement(JCOLIBRI_EVALUATION);
          document.appendChild(configuration);

          Element eid = document.createElement(ID);
          eid.appendChild(document.createTextNode(id));
          configuration.appendChild(eid);

          Element ecn = document.createElement(EVALUATOR_CLASS);
          ecn.appendChild(document.createTextNode(evaluatorClassName));
          configuration.appendChild(ecn);

          Element cecn = document.createElement(CYCLE_EVALUATOR_CLASS);
          cecn.appendChild(document.createTextNode(cycleEvaluatorClassName));
          configuration.appendChild(cecn);

          Element pars = document.createElement(PARAMETERS);
          configuration.appendChild(pars);
          
          for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
              Element param = document.createElement(PARAMETER);
              pars.appendChild(param);

              String paramname = iter.next();

              Element pname = document.createElement(PARAMETER_NAME);
              pname.appendChild(document.createTextNode(paramname));
              param.appendChild(pname);
              
              Element pvalue = document.createElement(PARAMETER_VALUE);
              pvalue.appendChild(document.createTextNode(parameters.get(paramname)));
              param.appendChild(pvalue);
          }

          // transform DOM -> XML
          OutputFormat of = new OutputFormat(document, "ISO-8859-1", true);
          of.setLineWidth(0);
          of.setLineSeparator("\n");
          FileWriter file = null;
          try {
            file = new FileWriter(nombreArchivo);
            XMLSerializer serializer = new XMLSerializer(file, of);
            serializer.serialize(document);
            file.close();
          }
          catch (IOException ex2) {
            System.out.print(ex2);
          }
      }
      catch (DOMException ex) {
        System.out.print(ex);
      }
      catch (ParserConfigurationException ex) {
        System.out.print(ex);
      }
      catch (FactoryConfigurationError ex) {
        System.out.print(ex);
      }
    }

  }