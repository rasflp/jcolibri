/**
 * 
 */
package jcolibri.extensions.DL.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.util.CBRLogger;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.dig.DIGReasoner;
import com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 * 
 * Provides connection to an Ontology
 * 
 * @author A. Luis Diez Hernández.
 *
 */
public class OntoBridge {
    
    public enum ReasonerType {
        PELLET, DIG
    };
    
	private String PROP_REASONER_HOST;

	private int PROP_REASONER_PORT;

	private OntModel PROP_ONT_MODEL;

	private String PROP_NAMESPACE;
	
	private String PROP_FILE_NAME;
	
	private ReasonerType PROP_TYPE;
	
	protected OntoBridge(String host, int port, String file, ReasonerType type) throws Exception{
		PROP_REASONER_HOST = host;
		PROP_REASONER_PORT = port;
		PROP_FILE_NAME = file;
		PROP_TYPE = type;
		connect();
	}
	
	

	/**
	 * Answer the file path for the OWL ontology.
	 * */
	public String getFile() {
		return PROP_FILE_NAME;
	}
	
	/**
	 * Answer the basic namespace of the ontology
	 * */
	public String getNamespace() {
		return PROP_NAMESPACE;
	}

	/**
	 * Answer the Jena object which contains the ontology
	 * */
	public OntModel getOntmodel() {
		return PROP_ONT_MODEL;
	}

	/**
	 * Answer the reasoner host
	 * */
	public String getHost() {
		return PROP_REASONER_HOST;
	}

	/**
	 * Answer the reasoner port
	 * */
	public int getPort() {
		return PROP_REASONER_PORT;
	}
	
	/**
	 * Answer the reasoner type (DIG or PELLET)
	 * */
	public ReasonerType getType() {
		return PROP_TYPE;
	}
	
	/**
	 * Answer the OntClass defined by the given local name
	 * */
	public OntClass getConceptByLocalName(String local_name){
		return PROP_ONT_MODEL.getOntClass(PROP_NAMESPACE+local_name);
	}
	
	/**
	 * Answer the OntClass defined by the given uri
	 * */
	public OntClass getConceptByURI(String uri){
		return PROP_ONT_MODEL.getOntClass(uri);
	}
	
	
	/**
	 * Answer the Individual defined by the given local name
	 * */
	public Individual getInstanceByLocalName(String local_name){
		return PROP_ONT_MODEL.getIndividual(PROP_NAMESPACE+local_name);
	}
	
	/**
	 * Answer the Individual defined by the given uri
	 * */
	public Individual getInstanceByURI(String uri){
		return PROP_ONT_MODEL.getIndividual(uri);
	}

	/**
	 * Answer the Property defined by the given local name
	 * */
	public Property getPropertyByLocalName(String local_name){
		return PROP_ONT_MODEL.getProperty(PROP_NAMESPACE+local_name);
	}
	
	/**
	 * Answer the Property defined by the given uri
	 * */
	public Property getPropertyByURI(String uri){
		return PROP_ONT_MODEL.getProperty(uri);
	}
	
	/**
	 * Answer the Resource defined by the given local name
	 * */
	public Resource getResourceByLocalName(String local_name){
		return PROP_ONT_MODEL.getResource(PROP_NAMESPACE+local_name);
	}
	
	/**
	 * Answer the Resource defined by the given uri
	 * */
	public Resource getResourceByURI(String uri){
		return PROP_ONT_MODEL.getResource(uri);
	}
	
    
	/**
	 * Answer an ExtendedIterator with the ontology concepts
	 * */
	public ExtendedIterator listConcepts(){
		return PROP_ONT_MODEL.listClasses();
	}

	/**
	 * Answer an ExtendedIterator with the ontology concepts
	 * */
	public ExtendedIterator listInstances(){
		return PROP_ONT_MODEL.listIndividuals();
	}

	/**
	 * Answer an ExtendedIterator with the instances of the given concept
	 * */
	public ExtendedIterator listInstances(String concept_name){
		OntClass concept = getConceptByLocalName(concept_name);
		
		return concept.listInstances();
	}

    private void precalculateParents()
    {
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Optimizing ontology access. This may take few minutes.");
        parentsCache.clear();
        for(Iterator classIter = PROP_ONT_MODEL.listClasses(); classIter.hasNext();)
        {
            OntClass c = (OntClass)classIter.next();
            if(c==null)
                continue;
            if(c.getNode().isBlank())
                continue;
            
            for(Iterator instIter = c.listInstances(); instIter.hasNext(); )
            {
                Individual instance = (Individual)instIter.next();
                if(parentsCache.get(instance)== null)
                    parentsCache.put(instance, c);
            }           
        }
        for(Individual i :parentsCache.keySet())
            System.err.println(i+" <-> "+parentsCache.get(i));
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Optimization finished.");
    }
    
    HashMap<Individual, OntClass> parentsCache = null;
    /**
     * Answer the superclass of the concept indicated by the given level
     * */
    public OntClass getParent(Individual instance){
        
        OntClass ret = null;
        Iterator clsIt, insIt;
        OntClass clsAux;
        Individual indAux;
        
        if(parentsCache == null)
        {
            parentsCache = new HashMap<Individual, OntClass>();
            precalculateParents();
        }
        
        clsAux = parentsCache.get(instance);
        if(clsAux != null)
            return clsAux;
        
        String localName = instance.getLocalName();
        clsIt = PROP_ONT_MODEL.listClasses();
        boolean clNext =clsIt.hasNext(); 
        while(clNext)
        {
            clsAux = (OntClass)clsIt.next();
            if(clsAux!=null)
            {
                if(!clsAux.getNode().isBlank())
                {
                    insIt = clsAux.listInstances();
                    boolean inNext =insIt.hasNext(); 
                    while(inNext)
                    {  
                        indAux = (Individual)insIt.next();
                        if(indAux.getLocalName().equalsIgnoreCase(localName))
                        {
                            ret = clsAux;
                            parentsCache.put(instance,clsAux);
                            inNext = false;
                        }
                        inNext = inNext && insIt.hasNext(); 
                    }
                }
            }
            clNext = (ret == null) && clsIt.hasNext();
        }
        
        return ret;     
    }
    
    
	/**
	 * Answer an ExtendedIterator with the ontology concepts
	 * */
	public Iterator listProperties(){

		return PROP_ONT_MODEL.listObjectProperties();
	}
	
    
	/**
	 * Answer an StmtIterator with the ontology statements
	 * */
	public Iterator listStatements(){
		return PROP_ONT_MODEL.listStatements();
	}
	
	/**
	 * Creates an instance of the given concept with the given name
	 * */
	public Individual createInstance(String instance_name, String concept_name){
		OntClass cls = getConceptByLocalName(concept_name);
		return PROP_ONT_MODEL.createIndividual(instance_name,cls);
	}
    
    /**
     * Creates a copy of a given Instance.
     * 
     * @param - original. The instance to duplicate.
     * @param - parent. The parent concept of both instances.
     * */
    
    public Individual duplicateInstance(Individual original, OntClass parent){
        int endian = (int)System.currentTimeMillis()%100;
        if(endian < 0) endian *= -1;
        String copy_name = PROP_NAMESPACE + original.getLocalName()+"_"+endian;
        
        Individual copy = PROP_ONT_MODEL.createIndividual(copy_name,parent);
        for(StmtIterator it= original.listProperties();it.hasNext();){
            Statement nxtProp = it.nextStatement();
            Property prop = nxtProp.getPredicate();
            Resource sibling =nxtProp.getResource();
            
            copy.addProperty(prop,sibling);
            //En principio parece que no es buena idea duplicar los relacionados.
            /*if(parent_prop != null){
                OntResource resource = parent_prop.getRange();
                
                OntClass new_parent = (OntClass)parent_prop.getRange().asClass();
                
                Individual sibling_copy = duplicateInstance(sibling,new_parent);
                
            }*/
        }
        return copy;
    }
	
    
    /**
     * Answer the declared property of a given Concept, if exists.
     * 
     *  @param concept - The concept.
     *  @param property - The property that we want to found.
     *  
     *   @return the declared property
     * */
        public OntProperty getDeclaredProperty(OntClass concept, Property property){
            ExtendedIterator it = concept.listDeclaredProperties();
            OntProperty ret = null;
            boolean seek = it.hasNext();
            while(seek){
                OntProperty prop = (OntProperty)it.next();
                if(prop.getLocalName().equalsIgnoreCase(
                        property.getLocalName())){
                    seek = false;
                    ret = prop;
                }
                else seek = it.hasNext();
            }
            return ret;
        }
    
	/**
	 * Save the current ontology into a file
	 * */
	public void commit(){
		try{
			String path = PROP_FILE_NAME.substring(8);
			
			File file = new File(path);
			file.delete();
			FileOutputStream os = new FileOutputStream(file);
			
			PROP_ONT_MODEL.write(os,"RDF/XML-ABBREV");
			os.close();
		}
		catch(Exception ex){
			CBRLogger.log(this.getClass(),CBRLogger.ERROR,
			"Unable to write in persistence layer");
		}
	}
	
	/**
	 * Method to connect with the ontology
	 * */
	private void connect() throws Exception{
        
        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Loading ontology. This may take a while...");
        
		/**
		 * This connector can work with two DL reasoners, a DIG reasoner
		 * (such as RACER or FACT) and PELLET. Depending on the kind of
		 * reasoner we must set some parameters or others.
		 */
		if (this.PROP_TYPE == ReasonerType.DIG) {
		    CBRLogger.log(this.getClass(), CBRLogger.INFO, "Using DIG to connect with the DL reasoner");
            
			//Stablish a resource that contains the URI
			Model cModel = ModelFactory.createDefaultModel();
			Resource conf = cModel.createResource();
			String res = "http://" + this.PROP_REASONER_HOST + ":" + this.PROP_REASONER_PORT;
			conf.addProperty(ReasonerVocabulary.EXT_REASONER_URL, cModel
					.createResource(res));
			// Create a Reasoner and the model
			DIGReasonerFactory drf = (DIGReasonerFactory) ReasonerRegistry
					.theRegistry().getFactory(DIGReasonerFactory.URI);

			DIGReasoner r = (DIGReasoner) drf.create(conf);

			OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
			spec.setReasoner(r);
			this.PROP_ONT_MODEL = ModelFactory.createOntologyModel(spec);
			
		} else if (PROP_TYPE == ReasonerType.PELLET) {
            CBRLogger.log(this.getClass(), CBRLogger.INFO, "Using PELLET reasoner");

            // If we use PELLET we only need the jar file in its directory

			// Create the model and set the reasoner specifications

			PROP_ONT_MODEL = ModelFactory
					.createOntologyModel(PelletReasonerFactory.THE_SPEC);

		} else {
			throw new InitializingException("An error ocurred while trying to load the ontology");
		}				

		// read the ontology file.
		PROP_ONT_MODEL.read(PROP_FILE_NAME);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(PROP_FILE_NAME);
		
		// read the usual ontology namespace
		Node el = doc.getElementsByTagName("rdf:RDF").item(0);
		PROP_NAMESPACE = el.getAttributes().getNamedItem("xml:base").getTextContent();
        if(!PROP_NAMESPACE.endsWith("#"))
            PROP_NAMESPACE+="#";

        CBRLogger.log(this.getClass(), CBRLogger.INFO, "Ontology loaded successfully into reasoner");

    }
    
    public static void main(String[] args)
    {
        try
        {
            OntoBridge ob = new OntoBridge("",0,
                    "file:///C:\\doctorado\\JColibri\\jCOLIBRI_1.1-Entrenador\\jCOLIBRI\\examples\\trainer\\ampliado.owl", 
                    OntoBridge.ReasonerType.PELLET);
            OntModel model = ob.getOntmodel();
            String namespace = ob.getNamespace();
            
            Vector<Vector<String>> instances = new Vector<Vector<String>>();
            
            Resource instance = model.getIndividual(namespace+"Entrenamiento_Inferior");
                      
            //Instancias de calentamiento
            Property p = ob.getPropertyByLocalName("has_calentamiento");
            Resource calentamiento = (Resource)instance.getProperty(p).getResource();
            
            p = ob.getPropertyByLocalName("has_exercise");
            Vector<String> instanciasCalentamiento = new Vector<String>();
            System.out.println("Instancias Calentamiento");
            StmtIterator ni = calentamiento.listProperties(p);            
            while (ni.hasNext())
            {
                Statement exercise = (Statement)ni.next();
                System.out.println(exercise.getResource().getLocalName());
                instanciasCalentamiento.add(exercise.getResource().getLocalName());
            }
            
            //Instancias de Principal
            p = ob.getPropertyByLocalName("has_principal");
            Resource principal = (Resource)instance.getProperty(p).getResource();
            
            p = ob.getPropertyByLocalName("has_exercise");
            Vector<String> instanciasPrincipal = new Vector<String>();
            System.out.println("Instancias PartePrincipal");
            ni = principal.listProperties(p);            
            while (ni.hasNext())
            {
                Statement exercise = (Statement)ni.next();
                System.out.println(exercise.getResource().getLocalName());
                instanciasPrincipal.add(exercise.getResource().getLocalName());
            }

            //Instancias de Vuelta a la Calma
            p = ob.getPropertyByLocalName("has_calma");
            Resource vueltaCalma = (Resource)instance.getProperty(p).getResource();
            
            p = ob.getPropertyByLocalName("has_exercise");
            Vector<String> instanciasVCalma = new Vector<String>();
            System.out.println("Instancias Vuelta a la calma");
            ni = vueltaCalma.listProperties(p);            
            while (ni.hasNext())
            {
                Statement exercise = (Statement)ni.next();
                System.out.println(exercise.getResource().getLocalName());
                instanciasVCalma.add(exercise.getResource().getLocalName());
            }
            
            instances.add(instanciasCalentamiento);
            instances.add(instanciasPrincipal);
            instances.add(instanciasVCalma);
            
        } catch (Exception e)
        {
          System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
}
