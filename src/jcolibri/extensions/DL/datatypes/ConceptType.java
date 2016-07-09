package jcolibri.extensions.DL.datatypes;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import jcolibri.cbrcore.DataType;
import jcolibri.connectors.TypeAdaptor;
import jcolibri.extensions.DL.util.OntoBridgeSingleton;
import jcolibri.util.CBRLogger;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


/**
 * Type of Ontology Concepts.
 * 
 * @author A. Luis Diez Hernández.
 */
public class ConceptType extends DataType implements TypeAdaptor, Serializable{
	private static final long serialVersionUID =1L;
	
    //The concept that this instance belogns to
    private OntClass PROP_ONT_CONCEPT;
    
    //The Instance
	private com.hp.hpl.jena.ontology.Individual PROP_ONT_INDIVIDUAL;

    
    
	/**
	 * Default Constructor with all attributes empty and representing nothing.
	 * */
	public ConceptType(){
		super();
	}
	
	/**
	 * Creates a ConceptType representing an instance. 
	 * 
	 * @param concept Concept of the instance
	 * @param instance Instance.
	 * */
	public ConceptType(OntClass concept, com.hp.hpl.jena.ontology.Individual instance){
		PROP_ONT_CONCEPT = concept;
		PROP_ONT_INDIVIDUAL = instance;
	}

	

	public ConceptType(String concept, String instance, boolean localnames){
        OntModel model = OntoBridgeSingleton.getOntoBridge().getOntmodel();
        String namespace = OntoBridgeSingleton.getOntoBridge().getNamespace();
        if(localnames)
        {
            PROP_ONT_CONCEPT = model.getOntClass(namespace+concept);
            PROP_ONT_INDIVIDUAL = model.getIndividual(namespace+instance);          
        }
        else
        {
        PROP_ONT_CONCEPT = model.getOntClass(concept);
        PROP_ONT_INDIVIDUAL = model.getIndividual(instance);	
        }
    }

    public ConceptType(String concept, String instance){
        this(concept,instance,true);
    }

    
    public ConceptType(String instance, boolean localname) throws Exception
    {
        loadIndividual(instance, localname);
    }
    
    private void loadIndividual(String instanceName, boolean localname) throws Exception
    {
        OntModel model = OntoBridgeSingleton.getOntoBridge().getOntmodel();
        String namespace = OntoBridgeSingleton.getOntoBridge().getNamespace();
        if(localname)
            PROP_ONT_INDIVIDUAL = model.getIndividual(namespace+instanceName);
        else
            PROP_ONT_INDIVIDUAL = model.getIndividual(instanceName);
        if(PROP_ONT_INDIVIDUAL == null)
        {
            CBRLogger.log(this.getClass(),CBRLogger.ERROR, "Instance: "+instanceName +" not found in loaded ontology ("+OntoBridgeSingleton.getFile()+")");
            throw new Exception("Instance: "+instanceName +" not found in loaded ontology ("+OntoBridgeSingleton.getFile()+")");
        }
        PROP_ONT_CONCEPT = getParent(model);
    }
    

	public OntClass getConcept(){
		return PROP_ONT_CONCEPT;
	}	
	

	/**
	 * Answer the Concept Individual
	 * */
	public com.hp.hpl.jena.ontology.Individual getOntIndividual(){
		return PROP_ONT_INDIVIDUAL;		
	}
	
	
	public boolean isSubClass(OntClass cls){
	    ExtendedIterator inst = cls.listInstances();
	    while(inst.hasNext()){
				Resource nxt = (Resource)inst.next();
				if(nxt.getLocalName().equalsIgnoreCase(
						PROP_ONT_INDIVIDUAL.getLocalName())) return true;
	    }

		return false;
	}
	
    
    
	public int maxDistance(OntClass cls){
        OntClass cparent = commonParent(cls);
        int maxdistance = 1;
        while(!cparent.isHierarchyRoot())
        {
            cparent = cparent.getSuperClass();
            maxdistance++;
        }
        return maxdistance;
	}
   
	
	/**
	 * Answer the superclass of the concept indicated by the given level
	 * */
	public OntClass getParent(int level){
		OntClass ret;
		switch(level){
		case 0:
			ret = this.PROP_ONT_CONCEPT;
		default:
			if(PROP_ONT_CONCEPT != null){
				Vector itSuper = getParents();
				ret = (OntClass)itSuper.elementAt(level);
			}
			else ret = null;
			break;
		}
		return ret;		
	}

	public String toString(){
		return PROP_ONT_INDIVIDUAL.getLocalName();
	}
	
    
    public void setConcept(String conceptName)
    {
        OntModel model = OntoBridgeSingleton.getOntoBridge().getOntmodel();
        String namespace = OntoBridgeSingleton.getOntoBridge().getNamespace();
        PROP_ONT_CONCEPT = model.getOntClass(namespace + conceptName);
    }
    
    
    /**
     * Doesn't loads the concept
     */
	public void fromString(String content) throws Exception{
        OntModel model = OntoBridgeSingleton.getOntoBridge().getOntmodel();
        String namespace = OntoBridgeSingleton.getOntoBridge().getNamespace();
        PROP_ONT_INDIVIDUAL = model.getIndividual(namespace+content);
        if(PROP_ONT_INDIVIDUAL == null)
        {
            CBRLogger.log(this.getClass(),CBRLogger.ERROR, "Instance: "+content +" not found in loaded ontology ("+OntoBridgeSingleton.getFile()+")");
            throw new Exception("Instance: "+content +" not found in loaded ontology ("+OntoBridgeSingleton.getFile()+")");
        }
	}
	
	
	/**
	 * Answer the superclass of the concept indicated by the given level
	 * */
	private OntClass getParent(OntModel m){
	    return OntoBridgeSingleton.getOntoBridge().getParent(PROP_ONT_INDIVIDUAL);
	}

	
	/**
	 * Answer the number of edges that connect two individuals of a class.
	 * */
	public int edgeDistance(OntClass cls){
		int ret=Integer.MAX_VALUE;
		OntClass parent = commonParent(cls);
		
		Vector myParents = getParents();
		Vector itsParents = getParents(cls);
		
		ret = myParents.indexOf(parent)+1;
		ret += itsParents.indexOf(parent)+1;
		
		return ret;
	}
	
	/**
	 * Answer common parent of the current Concept and the given one.
	 * */
	
	public OntClass commonParent(OntClass cls){
		OntClass ret = null;
		if(cls.getLocalName().equalsIgnoreCase(
				PROP_ONT_CONCEPT.getLocalName())) return PROP_ONT_CONCEPT;
		
		if((cls.getLocalName().equalsIgnoreCase(
				PROP_ONT_CONCEPT.getLocalName()))) return PROP_ONT_CONCEPT;
		
        Vector myParents = getParents();
		Vector itsParents = getParents(cls);
		
		int cent = 0;
		boolean end = false;
		
		while(!end){
			OntClass aux = (OntClass)myParents.elementAt(cent);
			if(itsParents.contains(aux)){
				end = true;
				ret = aux;
			}
			else{
				cent++;
				end = cent>=myParents.size();
			}
		}
		return ret;
	}
    
	/*
	 *	Answer the inmediately parent of the concept 
	 * 
	public OntClass getParent(){
		OntClass ret = null;
		try {
			String name;
//			Resource rs = PROP_ONT_CLASS.;
				name = PROP_ONT_CLASS.getLocalName();
				if(name!=null){
					Property p = PROP_ONT_MODEL.getProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
					Resource parent = nearestParent(PROP_ONT_CLASS,p,PROP_ONT_MODEL);
					ret = PROP_ONT_MODEL.getOntClass(parent.getURI());
				}
		} catch (Exception e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error reading ontology structure " + e.getMessage());
		}
		return ret;
	}
	*/
	public int getLevel(){
		int ret = 0;
		Vector parents = getParents();
		ret = parents.size();
		return ret;
	}

	public int getLevel(Individual ind, OntClass cls){
		int ret = 0;
		Vector parents = getParents(cls);
		ret = parents.size()+1;
		return ret;
	}

	
	public int getLevel(OntClass cls){
		int ret = 0;
		Vector parents = getParents(cls);
		ret = parents.size();
		return ret;
	}
	
	/*
	 * Answer the local name of the nearest parent to a given resource.
	 * 
	private Resource nearestParent(Resource res, Property p,OntModel m){
		Iterator stmIt = res.listProperties(p);
		Resource nearest = m.getResource("http://www.w3.org/2002/07/owl#Thing");
		while(stmIt.hasNext()){
			Statement stm = (Statement)stmIt.next();
			Resource next = stm.getResource();
			if(!next.equals(res)){
				if(inferior(next,nearest,p)){
					nearest = next;
				}
			}				
		}
		return nearest;
	}*/
	
	/**
	 * Answer whether the first given resource is located under the second given 
	 * resource with the given property. 
	 **/
	private boolean inferior(Resource r1, Resource r2, Property p){
		boolean ret = false;
		  Iterator stmIt = r1.listProperties(p);
		  while(stmIt.hasNext()){
			  Statement stm = (Statement)stmIt.next();
			  if(stm!=null){
				  	Resource stmRes = stm.getResource();
				  	if(stmRes.equals(r2)) ret = true;
			  }
		  }
		return ret;
	}

	
	private Vector getParents(){
		return getParents(PROP_ONT_CONCEPT);		
	}
	
	private Vector<Resource> getParents(OntClass cls){
		Vector<Resource> v = new Vector<Resource>();
		v.add(cls);
		StmtIterator stmIt = cls.listProperties();
		while(stmIt.hasNext()){
			Statement stm = stmIt.nextStatement();
			Resource obj = stm.getResource();
			Property p = stm.getPredicate();
			if(p.getLocalName().equalsIgnoreCase("subClassOf")){
				boolean colocado = false;
				int pos=1;
				while(!colocado){
					if(pos>=v.lastIndexOf(v.lastElement())){
						v.add(obj);
						colocado = true;
					} 
					else{
						Resource nxt = (Resource)v.elementAt(pos);
						if(inferior(obj,nxt,p)){
							colocado = true;
							v.add(pos,obj);
						}
						else pos++;
					}
				}
			}
		}
		return v;
	}
	
    public boolean equals(Object o)
    {
        if(! (o instanceof ConceptType))
            return false;
        ConceptType other = (ConceptType) o;
        return other.toString().equals(this.toString());
            
    }
}
