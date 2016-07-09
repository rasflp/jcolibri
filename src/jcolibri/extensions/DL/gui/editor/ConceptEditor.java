package jcolibri.extensions.DL.gui.editor;


import javax.swing.JComponent;
import javax.swing.JComboBox;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;

import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.extensions.DL.util.OntoBridgeSingleton;
import jcolibri.util.CBRLogger;
/**
 * @author A. Luis Diez Hernández.
 * */
public class ConceptEditor extends JComboBox implements
jcolibri.gui.ParameterEditor {
	private static final long serialVersionUID =1L;
	private final String EMPTY = "<empty>";
	
	private OntModel ONT_MODEL;
	private OntClass ONT_CLASS;
	
	public ConceptEditor(){
		this(null,"");
	}
	

	public ConceptEditor(OntModel m, String c){
		ONT_MODEL = m;
	}
	
	public Object getEditorValue() {
		try {
			Object val = this.getSelectedItem();
			if(val.equals(EMPTY)){ 
				return null;
				}
			else 
			{
				String value = (String)this.getSelectedItem();
				ConceptType ret = new ConceptType(value, true); 
				return ret;
			}
		} catch (Exception e) {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, e.getMessage());
			return null;
		}
	}

	public JComponent getJComponent() {
		return (JComponent) this;
	}

	public void setConfig(Object config) {
		try{
			String ont_class = null;
					
			ONT_MODEL = OntoBridgeSingleton.getOntoBridge().getOntmodel();
			
			ont_class = attToConcept((String)config);
			
			ONT_CLASS = ONT_MODEL.getOntClass(ont_class);
			
			this.addItem(EMPTY);
            
            /*
			Iterator itC = ONT_CLASS.listSubClasses();
			while(itC.hasNext()){
				OntClass nxt = (OntClass)itC.next();
				String name = nxt.getLocalName();
				if((name!=null)&&(!name.equalsIgnoreCase("Nothing"))){
					this.addItem(name);
				}
			}
			*/
			Iterator it = ONT_CLASS.listInstances();
			while(it.hasNext()){
				Individual nxt = (Individual)it.next();
				String name = nxt.getLocalName();
				if(name!=null){
					this.addItem(name);
				}
			}
		}
		catch(Exception e){
			CBRLogger.log(this.getClass(), CBRLogger.INFO,
					"Error setting ConceptEditorConfig: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setDefaultValue(Object defaultValue) {
        String value = null;
        if (defaultValue instanceof ConceptType)
        {
            ConceptType ct = (ConceptType)defaultValue;
            value = ct.getOntIndividual().getLocalName();
        }
        else if (defaultValue instanceof String)
			value  = (String) defaultValue;
        else
            return;
		this.setSelectedItem(value);
	}
    
    private String attToConcept(String att)
    {
        String concept = "NO_CONCEPT";
        StringTokenizer st = new StringTokenizer(att,".");
        while(st.hasMoreTokens())
            concept = st.nextToken();
        return OntoBridgeSingleton.getOntoBridge().getNamespace() + concept;
    }
}
