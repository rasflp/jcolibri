/**
 * 
 */
package jcolibri.extensions.DL.method;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import jcolibri.cbrcase.CBRCase;
import jcolibri.cbrcore.CBRContext;
import jcolibri.cbrcore.exception.ExecutionException;
import jcolibri.extensions.DL.datatypes.ConceptType;
import jcolibri.extensions.DL.util.*;
import jcolibri.cbrcore.CBRMethod;
import jcolibri.util.CBRLogger;
import jcolibri.util.CaseCreatorUtils;

import jcolibri.extensions.DL.method.AdaptationMethodUtils.*;
import jcolibri.extensions.DL.method.AdaptationMethodUtils.AdaptationRule.Condition;

/**
 * @author A. Luis Diez Hernández
 *
 */
public class DLAdaptationMethod extends CBRMethod {
	private static final long serialVersionUID = 1L;
	
    
	private static final String CONFIG_RULES_FILE = "Rules file";
	private static final String SOLUTION_ATTRIBUTE = "Solution Attribute";	
    
    
	/**
	 * Relation stream to reach thu substituible instance
	 * */
	
	
	private static OntoBridge PROP_BRIDGE;
	private AdaptationParser ap;
	private CBRCase _case;
	private ConceptType solution;
	private Individual solution_individual;
	private Vector<String> original_names;
	
	public DLAdaptationMethod(){}
	
	public CBRContext execute(CBRContext context)
	throws jcolibri.cbrcore.exception.ExecutionException {
		try{	
			PROP_BRIDGE = OntoBridgeSingleton.getOntoBridge();
            
            String solutionAttribute = (String)this.getParameterValue(SOLUTION_ATTRIBUTE);
            
			
			//Acceder a los arrays
			String file_rule = (String)this.getParameterValue(CONFIG_RULES_FILE);
			ap = new AdaptationParser(file_rule);
			ap.analize();
			
			List list = context.getCases();
			for(Iterator it = list.iterator();it.hasNext();){
				
				_case = (CBRCase)it.next();
				
				saveSolution(solutionAttribute);
                
				Iterator ruleIt = ap.getRules().iterator();
				while(ruleIt.hasNext()){
					AdaptationRule rule = (AdaptationRule)ruleIt.next();
					evaluate(_case, rule);
				}
			}
			
			return null;
		}
		catch(Exception e){
			throw new jcolibri.cbrcore.exception.ExecutionException(
					"Error adaptating with DL's: " + e.getMessage());
		}
	}
	
	
	private void evaluate(CBRCase _case, AdaptationRule ar){
		Individual suspect;
		OntClass parent;
		String parent_name;
		String[] idOnto;
		Resource res,next;
			
		idOnto = ar.getIdOnto();
		
		
		res = solution_individual; 
		
		parent_name = idOnto[idOnto.length];
		parent = PROP_BRIDGE.getConceptByLocalName(parent_name);
		
		//We load the idOnto
		for(int i=0;i<idOnto.length;i++){
			int mod = i%2;
			if(mod == 1){
				//If an odd number, it's a property
				Property p = PROP_BRIDGE.getPropertyByLocalName(idOnto[i]);
				
				next = res.getProperty(p).getResource();
				res = next;
			}
			else{
				//If an even number, it's a concept
			}
		}
		try{
			suspect = (Individual)res;		
		}
		catch(ClassCastException exc){
			CBRLogger.log(this.getClass(),CBRLogger.ERROR,
					"An error ocurred analizing the idOnto");
			return;
		}
		
		//Evaluate the condition.
		AdaptationRule.Condition cond = ar.getDecision_condition();
		establishCondition(suspect,cond);
		boolean change = cond.evaluate();
		
		if(!change) return;
		
		//Now, we have to adapt.
		
		String method = ar.getAdaptation_method();
		
		adaptation(method,ar,parent,suspect);
		
		Property dep_prop = PROP_BRIDGE.getPropertyByLocalName(ar.getDependence());
		if(dep_prop!=null)	dependencies(suspect,parent,dep_prop,ar);
		
	}	
	
	private void establishCondition(Resource suspect, AdaptationRule.Condition cond){
		Resource res,next;
		switch(cond.getCond_type()){
		case 1:
			//(IDPROPERTY (=|!=) IDCASE)
			String[] frst = (String[])cond.getFirstDescripition();
			String sec = (String)cond.getSecondDescription(); 
			String sec_val = (String)CaseCreatorUtils.getAttribute(_case,sec).getValue();
			cond.setSecond(sec_val);
			res = suspect;
			for(int i=0;i<frst.length;i++){
				int mod = i%2;
				if(mod == 0){
					//If an even number, it's a property
					Property p = PROP_BRIDGE.getPropertyByLocalName(frst[i]);
					next = res.getProperty(p).getResource();
					res = next;
				}
				else{
					//If an odd number, it's a concept
				}
			}
			
			cond.setFirst(res);
						
			
			break;
		case 2:
			//(IDCASE (=|!=) String)
			String first = (String)cond.getFirstDescripition();
			String first_val = (String)CaseCreatorUtils.getAttribute(_case,first).getValue();
			sec = (String)cond.getSecondDescription();
			cond.setFirst(first_val);
			cond.setSecond(sec);
			break;
		case 3:
			//(IDPROPERTY instanceOf Concept)
			frst = (String[])cond.getFirstDescripition();
			sec = (String)cond.getSecondDescription(); 
			
			cond.setSecond(sec);
			res = suspect;
			for(int i=0;i<frst.length;i++){
				int mod = i%2;
				if(mod == 0){
					//If an even number, it's a property
					Property p = PROP_BRIDGE.getPropertyByLocalName(frst[i]);
					next = res.getProperty(p).getResource();
					res = next;
				}
				else{
					//If an odd number, it's a concept
				}
			}
			
			cond.setFirst(res);
			
			OntClass cls = PROP_BRIDGE.getConceptByLocalName(sec);
			
			cond.setSecond(cls);
			break;
		default:return;
		}
	}
	
	private void saveSolution(String solutionAttributeName) throws ExecutionException{
        if(!solutionAttributeName.startsWith("Solution."))
            solutionAttributeName = "Solution."+solutionAttributeName;
        jcolibri.cbrcase.Individual attribute = CaseCreatorUtils.getAttribute(_case, solutionAttributeName);
        if(attribute == null)
            throw new ExecutionException("Cannot find attribute to adapt: "+ solutionAttributeName);

        try{
            solution =(ConceptType) attribute.getValue();
        }
        catch(ClassCastException exc)
        {
            throw new ExecutionException("Attribute to adapt: "+ solutionAttributeName+ " doesn't contain a Concept value");            
        }
		original_names = new Vector<String>();
		solution_individual = solution.getOntIndividual();
		save_individual_rec(solution_individual);
	} 
	
	private void save_individual_rec(Individual ind) throws ExecutionException{
		original_names.add(ind.getLocalName());
		
		for(StmtIterator it = ind.listProperties();it.hasNext();){
			Resource res = it.nextStatement().getResource();
			try{
				Individual next_ind = (Individual)res;
				save_individual_rec(next_ind);
			}
			catch(ClassCastException exc){
			    throw new ExecutionException(exc);
            }
		}	
	}
	
	private void changeIndividual(String[] idOnto, Individual new_individual){
		Hashtable<String, Resource> parents = new Hashtable<String, Resource>();			
		Resource next, res = solution_individual;
		for(int i = 0;i<idOnto.length;i++){
			int mod = i%2;
			if(mod == 1){
				//If an even number, it's a property.
				Property p = PROP_BRIDGE.getPropertyByLocalName(idOnto[i]);
				parents.put(idOnto[i],res);
				next = res.getProperty(p).getResource();
				res = next;
			}
			//Else (an odd number) it's a concept.
				
		}
		
		res = new_individual;
		
		for(int i = idOnto.length-1;i>=0;i--){
			int mod = i%2;
			if(mod == 1){
				//It's a property.
				Property p = PROP_BRIDGE.getPropertyByLocalName(idOnto[i]);
				Individual prnt = (Individual)parents.get(idOnto[i]);
				
				if(!original_names.contains(prnt.getLocalName()))
					prnt.setPropertyValue(p,res);
				else{
					OntClass conc = PROP_BRIDGE.getConceptByLocalName(idOnto[i-1]);
					prnt = PROP_BRIDGE.duplicateInstance(prnt,conc);
					prnt.setPropertyValue(p,res);
				}
				
				res = prnt;
			}
			//else it's a concept.
				
			
		}
	}
	
	private void changeProperty(String[] properties, Individual suspect,String res_name, boolean instance){
		Resource res = suspect;
		Hashtable<String, Resource> parents = new Hashtable<String,Resource>();
		Property p = null;
		for(int i = 0; i < properties.length; i++){
			int mod = i%2;
			if(mod == 0){
				//Its a property
				p = PROP_BRIDGE.getPropertyByLocalName(properties[i]);
				Resource nxt = res.getProperty(p).getResource();
				original_names.add(nxt.getLocalName());
				parents.put(properties[i],nxt);
				res = nxt;
			}
		}
		if(original_names.contains(res.getLocalName())){
			OntClass cncpt = PROP_BRIDGE.getConceptByLocalName(
					properties[properties.length-1]);
			res = PROP_BRIDGE.duplicateInstance((Individual)res,cncpt);
		}
		Individual new_instance;
		OntClass new_concept;
		if(instance){ 
			new_instance = PROP_BRIDGE.getInstanceByLocalName(res_name);
			((Individual)res).setPropertyValue(p,new_instance);
		}
		else {
			new_concept = PROP_BRIDGE.getConceptByLocalName(res_name);
			new_instance = chooseAnInstance(new_concept,null,null);
		}
		
		((Individual)res).setPropertyValue(p,new_instance);
		
		for(int i = properties.length-1;i>=0;i--){
			int mod = i%2;
			if(mod == 0){
				//It's a property.
				p = PROP_BRIDGE.getPropertyByLocalName(properties[i]);
				Individual prnt = (Individual)parents.get(properties[i]);
				
				if(!original_names.contains(prnt.getLocalName()))
					prnt.setPropertyValue(p,res);
				else{
					OntClass conc = PROP_BRIDGE.getConceptByLocalName(properties[i-1]);
					prnt = PROP_BRIDGE.duplicateInstance(prnt,conc);
					prnt.setPropertyValue(p,res);
				}
				
				res = prnt;
			}
			//else it's a concept
		}
	}
	
	private Individual chooseAnInstance(OntClass parent, Condition cond, Condition cond2){
		Iterator it = parent.listInstances();
		boolean cont = it.hasNext();
		boolean found = false;
		Individual new_individual = null;
		while(cont){
			Individual ind = (Individual)it.next();
			if(cond != null){
				establishCondition(ind,cond);
				if(!cond.evaluate()){
					if(cond2 != null){
						establishCondition(ind,cond2);
						if(cond2.evaluate()){
							found = true;
							cont = false;
							new_individual = ind;
						}
					}
					else{
						found = true;
						cont = false;
						new_individual = ind;
					}
				}
				else cont = it.hasNext();
			}
			else{
				if(cond2 != null){
					establishCondition(ind,cond2);
					if(cond2.evaluate()){
						found = true;
						cont = false;
						new_individual = ind;
					}
				}
				else{
					found = true;
					cont = false;
					new_individual = ind;
				}
			}
		}
		if(!found){
			CBRLogger.log(this.getClass(),CBRLogger.ERROR,
					"No available substitute found");
			return null;
		}
		return new_individual;
	}
	
	private void adaptation(String method, AdaptationRule ar, OntClass parent, Individual suspect){
		Condition cond = ar.getAdaptation_condition();
		String[] idOnto = ar.getIdOnto();
		if(method.equalsIgnoreCase(ParserConstants.SUBS_STRING)){
			Condition cond2 = ar.getAdaptation_condition();
			Individual new_individual = chooseAnInstance(parent,cond,cond2);
			changeIndividual(idOnto,new_individual);
		}
		else if(method.equalsIgnoreCase(ParserConstants.DIRECT_STRING)){
			if(original_names.contains(suspect.getLocalName())){
				OntClass cncpt = PROP_BRIDGE.getConceptByLocalName(idOnto[idOnto.length-1]);
				suspect = PROP_BRIDGE.duplicateInstance(suspect,cncpt);
			}
			String[] properties = ar.getAdaptation_properties();
			
			changeProperty(properties,suspect,ar.getAdaptation_resource(),true);
		}
		else if(method.equalsIgnoreCase(ParserConstants.ANY_STRING)){
			if(original_names.contains(suspect.getLocalName())){
				OntClass cncpt = PROP_BRIDGE.getConceptByLocalName(idOnto[idOnto.length-1]);
				suspect = PROP_BRIDGE.duplicateInstance(suspect,cncpt);
			}
			String[] properties = ar.getAdaptation_properties();
			
			changeProperty(properties,suspect,ar.getAdaptation_resource(),false);
		}
	}
	
	private void dependencies(Individual suspect,OntClass parent,Property p, AdaptationRule ar){
		if(original_names.contains(suspect.getLocalName())){
			suspect = PROP_BRIDGE.duplicateInstance(suspect,parent);
		}
		OntProperty dp = PROP_BRIDGE.getDeclaredProperty(parent,p); 
		OntClass new_parent = (OntClass)dp.getRange();
		if(new_parent.equals(parent)) new_parent = (OntClass)dp.getDomain();
		for(StmtIterator it = suspect.listProperties(p);it.hasNext();){
			Statement next = it.nextStatement();
			Individual dependent = (Individual)next.getResource();
			adaptation(ar.getAdaptation_method(),ar,new_parent,dependent);
		}
	}
	
	public static void main(String[] args){
		OntoBridge br = OntoBridgeSingleton.getOntoBridge();
		
		OntClass vac_case = br.getConceptByLocalName("VACATION_CASE");
		Iterator it = vac_case.listInstances();
		Individual origin = null,copy = null;
		if(it.hasNext()){
			origin = (Individual)it.next();
			
			copy = br.duplicateInstance(origin,vac_case);
		}
		StmtIterator it1,it2;
		it1 = origin.listProperties();
		it2 = copy.listProperties();
		int numProps = 1;
		for(it1 = origin.listProperties(), it2 = copy.listProperties();(it1.hasNext())&&(it2.hasNext());){
		
			Statement stm1 = it1.nextStatement();
			Statement stm2 = it2.nextStatement();
			if(stm1!=null){
				String subj = stm1.getSubject().getLocalName();
				String obj = stm1.getResource().getLocalName();
				String prop = stm1.getPredicate().getLocalName();
			
				System.out.println("Original, Propiedad nº: "+numProps);
				System.out.println("----------------------------------------");
				System.out.println(subj+" "+prop+" "+obj);
				System.out.println("----------------------------------------");
				System.out.println("----------------------------------------");
			}
			if(stm2!=null){
				String subj = stm2.getSubject().getLocalName();
				String obj = stm2.getResource().getLocalName();
				String prop = stm2.getPredicate().getLocalName();
			
				System.out.println("Copia, Propiedad nº: "+numProps);
				System.out.println("----------------------------------------");
				System.out.println(subj+" "+prop+" "+obj);
				System.out.println("----------------------------------------");
				System.out.println("----------------------------------------");
			}
			numProps++;
		}
	}
}
