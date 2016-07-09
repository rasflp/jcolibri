package jcolibri.extensions.DL.connectors;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcase.CBRCaseRecord;
import jcolibri.cbrcase.Connector;
import jcolibri.cbrcase.IndividualRelation;
import jcolibri.cbrcore.exception.InitializingException;
import jcolibri.extensions.DL.cbrcase.CBRCaseJena;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.extensions.DL.util.OntoBridge;
import jcolibri.extensions.DL.util.OntoBridgeSingleton;
import jcolibri.tools.data.CaseStDescription;
import jcolibri.tools.data.CaseStSimpleAttConcept;
import jcolibri.tools.data.CaseStructure;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;
import jcolibri.util.ProgressBar;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * <p>
 * Implements a complex Jena connector.
 * </p>
 * It can also contain another connector to manage persistence
 * 
 * @author A. Luis Diez Hernández
 * 
 */
public class JenaConnector implements Connector
{

    private static JenaConnector instance = null;

    public static JenaConnector getJenaConnector()
    {
        if(instance == null)
            instance = new JenaConnector();
        return instance;
    }

    private OntoBridge PROP_ONTO_BRIDGE;

    private String PROP_CASE_ID_CONCEPT;

    /** Case Structure file */
    private String CaseStructureXMLfile = null;

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbrcase.Connector#init(java.util.Properties)
     */
    public void init(Properties props) throws InitializingException
    {
        CaseStructureXMLfile = props.getProperty(CONFIG_FILE);

        CaseStructure str = new CaseStructure();
        str.readFromXMLFile(CaseStructureXMLfile);
        PROP_CASE_ID_CONCEPT = str.getConcept();

        PROP_ONTO_BRIDGE = OntoBridgeSingleton.getOntoBridge();
        instance = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbrcase.Connector#close()
     */
    public void close()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbrcase.Connector#storeCases(java.util.Collection)
     */
    public void storeCases(Collection<CBRCase> cases)
    {
        OntModel ontModel = PROP_ONTO_BRIDGE.getOntmodel();
        for (CBRCase c: cases)
            storeCase(c, ontModel);
        PROP_ONTO_BRIDGE.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbrcase.Connector#deleteCases(java.util.Collection)
     */
    public void deleteCases(Collection cases)
    {
        for (Iterator iter = cases.iterator(); iter.hasNext();)
        {
            try
            {
                CBRCaseRecord _case = (CBRCaseRecord) iter.next();
                String name = (String) _case.getValue();
                OntModel ontModel = PROP_ONTO_BRIDGE.getOntmodel();
                String nmSpace = PROP_ONTO_BRIDGE.getNamespace();
                Individual ind = ontModel.getIndividual(nmSpace + name);
                ontModel.remove(ind.listProperties());
            } catch (Exception e)
            {
                CBRLogger.log(this.getClass(), CBRLogger.ERROR,
                        "Error deleting cases: " + e.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbrcase.Connector#retrieveAllCases()
     */
    public Collection<CBRCase> retrieveAllCases()
    {
        java.util.Vector<CBRCase> cases = new java.util.Vector<CBRCase>();
        jcolibri.cbrcase.CBRCaseRecord _case = null;

        CaseStructure str = new CaseStructure();
        str.readFromXMLFile(CaseStructureXMLfile);
        CaseStDescription description = str.getDescription();

        try
        {
            Date date = new Date();
            Date date2 = new Date();
            int total = 0;
            date.setTime(System.currentTimeMillis());
            
            OntClass ppalClass = PROP_ONTO_BRIDGE.getConceptByLocalName(PROP_CASE_ID_CONCEPT);
            
            Iterator pInd = ppalClass.listInstances();
            for (; pInd.hasNext(); pInd.next())
                total++;
            pInd = ppalClass.listInstances();
            int cnt = 0;

            ProgressBar.PROGRESSBAR.init("Loading ontology", total);

            while (pInd.hasNext())
            {
                ProgressBar.PROGRESSBAR.step();
                com.hp.hpl.jena.ontology.Individual ind;
                ind = (com.hp.hpl.jena.ontology.Individual) pInd.next();
                if (ind != null)
                {
                    cnt++;
                    // Create the case with the principal individual
                    String case_id = ind.getLocalName();
                    _case = CaseCreatorUtils
                            .createCase(this.CaseStructureXMLfile);
                    CaseCreatorUtils.setCaseName(_case, case_id);

                    // We retrieve the properties of the individual which are
                    // mapped
                    for (int i=0; i<description.getNumChildrens(); i++)
                    {
                        CaseStSimpleAttConcept attributeSt = (CaseStSimpleAttConcept) description.getChild(i);
                        
                        String relationName = attributeSt.getRelation();
                        String attributeName = attributeSt.getName();
                        double weight = attributeSt.getWeight();
                        
                        Property p = PROP_ONTO_BRIDGE.getPropertyByLocalName(relationName);
                        if (ind.hasProperty(p))
                        {
                            com.hp.hpl.jena.ontology.Individual indObj;
                            try
                            {
                                ResourceImpl objRes = (ResourceImpl) ind.getProperty(p).getObject();
                                indObj = (com.hp.hpl.jena.ontology.Individual) objRes;
                            } catch (ClassCastException ccE)
                            {
                                String obj = ind.getProperty(p).asTriple().getObject().getURI();
                                indObj = PROP_ONTO_BRIDGE.getInstanceByURI(obj);
                            }
                            OntClass objClass = getConcepto(attributeName);

                            ConceptType value = new ConceptType(objClass,indObj);

                            _case.addAttribute(relationName, value, weight, "Concept");
                        }
                    }
                    cases.add(_case);
                }
            }
            date2.setTime(System.currentTimeMillis());

            int time = (int) ((date2.getTime() - date.getTime()) / 1000);
            CBRLogger.log(this.getClass(), CBRLogger.INFO, cnt
                    + " cases loaded in " + time + " seconds");

            ProgressBar.PROGRESSBAR.setVisible(false);
        } catch (Exception e)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR,
                    "Error retrieving cases: " + e.getMessage());
            e.printStackTrace();

        }
        return cases;
    }

    public Collection<CBRCase> retrieveSomeCases(String filter)
    {
        return null;
    }

    /**
     * 
     * This method retrieves a list of cases from the case base.
     * 
     * @param instances
     *            Collection
     * @return Collection
     */
    public Collection<CBRCase> retrieveSomeCases(Collection instances)
    {

        Collection<CBRCase> list = new LinkedList<CBRCase>();

        for (Iterator it = instances.iterator(); it.hasNext();)
        {

            String name = (String) ((CBRCaseRecord) it.next()).getValue();

            com.hp.hpl.jena.ontology.Individual qind = PROP_ONTO_BRIDGE
                    .getInstanceByLocalName(name);

            list.add(new CBRCaseJena(qind));

        }

        return list;

    }


    /**
     * This method adds a new case in the case base.
     * 
     * @param query
     *            CBRCase
     */
    public void storeCase(CBRCase query, OntModel ontModel)
    {
        String nmspace = PROP_ONTO_BRIDGE.getNamespace();
        String name = nmspace + query.getValue().toString();
        if (ontModel.getIndividual(name) != null)
        {
            CBRLogger.log("DLConnector", CBRLogger.ERROR,
                    "Trying to store a Case that already exists");
        } else
        {
            com.hp.hpl.jena.ontology.Individual qind = ontModel
                    .createIndividual(name, ontModel.getOntClass(nmspace
                            + PROP_CASE_ID_CONCEPT));
            Collection rels = query.getRelations();
            for (Iterator it = rels.iterator(); it.hasNext();)
            {
                // rels = Result, Solution, Description
                IndividualRelation ir = (IndividualRelation) it.next();
                for (Iterator it2 = ir.getTarget().getRelations().iterator(); it2
                        .hasNext();)
                {
                    IndividualRelation ir2 = (IndividualRelation) it2.next();
                    if (ir2.getWeight() > 0)
                    {
                        String relation = ir2.getDescription();
                        String target = ir2.getTarget().getValue().toString();
                        Property prop = ontModel.getOntProperty(nmspace+relation);
                            if (prop != null)
                            {
                                Resource tar = ontModel.getResource(nmspace + target);
                                qind.addProperty(prop, tar);
                            }
                    }
                }
            }
        }

    }

    /**
     * Answer the Jena object which contains the asked concept.
     */
    public OntClass getConcepto(String nombreConcepto)
    {
        OntModel ontModel = PROP_ONTO_BRIDGE.getOntmodel();
        String nmSpace = PROP_ONTO_BRIDGE.getNamespace();

        return ontModel.getOntClass(nmSpace + nombreConcepto);
    }

    /**
     * Answer an ExtendedIterator with all the instances of the named concept.
     */
    public ExtendedIterator getInstancesConcepto(String nombreConcepto)
    {
        OntClass concepto = getConcepto(nombreConcepto);

        return concepto.listInstances();
    }

    public OntModel getOntModel()
    {
        return PROP_ONTO_BRIDGE.getOntmodel();
    }

    public String getNamespace()
    {
        return PROP_ONTO_BRIDGE.getNamespace();
    }


}
