//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2004.07.27 at 10:46:52 CEST 
//

package jcolibri.xml.methods;

/**
 * Java content class for Type element declaration.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this java content object.
 * <p>
 * 
 * <pre>
 *   &lt;element name=&quot;Type&quot;&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *       &lt;enumeration value=&quot;Decomposition&quot;/&gt;
 *       &lt;enumeration value=&quot;Resolution&quot;/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/element&gt;
 * </pre>
 * 
 */
public interface Type extends javax.xml.bind.Element {

	java.lang.String getValue();

	void setValue(java.lang.String value);

}