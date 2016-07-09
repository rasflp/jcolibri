/**
 * 
 */
package jcolibri.extensions.DL.method.AdaptationMethodUtils;


import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import jcolibri.util.CBRLogger;

/**
 * @author A. Luis Diez Hernández
 *
 */
public class AdaptationRule {
	
	/**
	 * Relations to reach the attribute to possibly change
	 * */
	private String[] idOnto = null;
	
	/**
	 * Attribute name from the case
	 * */
	private String case_attribute = null;
	
	/**
	 * Defines the type of adaptation to use
	 * 
	 * substitution
	 * direct modification
	 * anyotherinstanceof modification
	 * */
	private String adaptation_method = null;
	
	/**
	 * Stores the criterion to adapt a case or not
	 * */
	private Condition decision_condition = null;
	
	/**
	 * The adaptation method may have a condition,
	 * that is stored here
	 * */
	private Condition adaptation_condition = null;
	
	/**
	 * The adaptation method may need properties hierachy to find
	 * a substitute
	 */
	private String[] adaptation_properties = null;
	
	/**
	 * The adaptation method may have to solve dependencies
	 * */
	private String dependence = null;
	
	
	/**
	 * The adaptation method may need an instance or a concept,
	 * in that case its local name is stored here.
	 * */
	private String adaptation_resource;
	


	/**
	 * Creates an empty rule.
	 * */
	public AdaptationRule(){
		idOnto = null;
		case_attribute = null;
		adaptation_method = null;
		decision_condition = null;
		adaptation_condition = null;
		adaptation_properties = null;
		dependence = null;
		adaptation_resource = null;
	}
	
	
	public String[] getAdaptation_properties() {
		return adaptation_properties;
	}


	public void setAdaptation_properties(String[] adaptation_properties) {
		this.adaptation_properties = adaptation_properties;
	}

	public String getAdaptation_resource() {
		return adaptation_resource;
	}


	public void setAdaptation_resource(String adaptation_resource) {
		this.adaptation_resource = adaptation_resource;
	}

	
	public String getDependence() {
		return dependence;
	}


	public void setDependence(String dependence) {
		this.dependence = dependence;
	}


	public void setAdaptation_condition(Object first, Object second, boolean crit) {
		this.adaptation_condition = new Condition(first,second,crit);
	}

	public void setAdaptation_condition(Condition cond){
		this.adaptation_condition = cond;
	}
	
	public String getAdaptation_method() {
		return adaptation_method;
	}

	public void setAdaptation_method(String adaptation_method) {
		this.adaptation_method = adaptation_method;
	}

	public String getCase_attribute() {
		return case_attribute;
	}

	public void setCase_attribute(String case_attribute) {
		this.case_attribute = case_attribute;
	}

	
	public void setDecision_condition(Object first, Object second, boolean crit) {
		this.decision_condition = new Condition(first,second,crit);
	}

	public void setDecision_condition(Condition cond){
		this.decision_condition = cond;
	}
	
	
	public String[] getIdOnto() {
		return idOnto;
	}


	public void setIdOnto(String[] idOnto) {
		this.idOnto = idOnto;
	}


	/**
	 * Answer if the condition is satisfied
	 * */
	public boolean has_to_adapt(){
		return decision_condition.evaluate();
	}
	
	/**
	 * Answer if the adaptation method's condition is satisfied
	 * */
	public boolean is_valid(){
		if(adaptation_condition == null) return true;
		return adaptation_condition.evaluate();
	}
	
	public String toString(){
		String ret="";
		ret = "IdOnto: [";
		for(int i = 0; i<idOnto.length;i++){
			ret += " "+idOnto[i];
			if(i!=idOnto.length-1) ret += ",";
		}
		ret += "]"+"\n";
		
		ret +="Condition: " + decision_condition.toString()+"\n";
		
		ret += "Adaptation: "+ adaptation_method;
		if(adaptation_properties!=null){
			ret += ": "+Condition.propToString(adaptation_properties)+": ";
			ret += adaptation_resource;
		}
		if(adaptation_condition!=null){
			ret+= "# "+adaptation_condition.toString()+" #";
		}
		if(dependence != null){
			ret+=" Dependence: "+dependence+"\n";
		}
		return ret;
	}
	
	/**
	 * Implements an equality/inequality condition
	 * */
	public static class Condition{
		
		/**
		 * = 1 - (IDPROPERTY (=|!=) IDCASE)
		 * = 2 - (IDCASE (=|!=) String)
		 * = 3 - (IDPROPERTY instanceOf Concept)
		 * */
		private int cond_type;
		
		/**
		 * First member of equation
		 * */
		private Object first;
		
		/**
		 * Description of the first member of equation
		 * e.g.: If it's a Property, this attribute
		 * it will be a String[]that says the way to 
		 * reach the asked property.
		 * */
		private Object first_description;
		/**
		 * Second member of equation
		 * */
		private Object second;
		/**
		 * Description of the second member of equation
		 * e.g.: If it's a Property, this attribute
		 * it will be a String[]that says the way to 
		 * reach the asked property.
		 * */
		private Object second_description;
		
		
		/**
		 * If cond_type != 3.
		 * 		It says if the condition criterion is equality(true)
		 * 		or inequality(false)
		 * If cond_type = 3.
		 * 		It says if the condition is positive or negative
		 * */
		private boolean crit;
		
				
		/**
		 * 
		 * Creates a condition
		 * 
		 * @param f - The first member of equation
		 * @param s - The second member of equation
		 * @param crit - The criterion of equation
		 */
		public Condition(Object f, Object s, boolean crit){
			first = f;
			second = s;
			this.crit = crit;
		}
		
		/**
		 * Creates an empty condition
		 * */
		public Condition(){
			first = second = null;
			cond_type = -1;
			first_description = null;
			second_description = null;
			crit = false;
		}
		
		public boolean evaluate(){
			if(first == null) return false;
			if(second == null) return false;
			
			boolean ret;
			switch(cond_type){
			case 1:
				//(IDPROPERTY (=|!=) IDCASE)
				Individual first_casted;
				try{
					first_casted = (Individual) first;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String[], found: "+
							first.getClass());
					return false;
				}
				String second_casted;
				try{
					second_casted = (String)first;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String, found: "+
							second.getClass());
					return false;
				}
				ret = first_casted.getLocalName().equalsIgnoreCase(second_casted);
				return crit & ret;
			case 2:
				//(IDCASE (=|!=) String)
				String first_casted2;
				try{
					first_casted2 = (String) first;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String[], found: "+
							first.getClass());
					return false;
				}
				String second_casted2;
				try{
					second_casted2 = (String)first;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String, found: "+
							second.getClass());
					return false;
				}
				ret = first_casted2.equalsIgnoreCase(second_casted2);
				return crit & ret;
			case 3:
				//(IDPROPERTY instanceOf Concept)
				Individual first_casted3;
				try{
					first_casted3 = (Individual) first;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String[], found: "+
							first.getClass());
					return false;
				}
				OntClass second_casted3;
				try{
					second_casted3 = (OntClass)second;
				}
				catch(ClassCastException exc){
					CBRLogger.log(this.getClass(),CBRLogger.ERROR,
							"Wrong parameter of condition: needed String, found: "+
							second.getClass());
					return false;
				}
				ret = false;
				Iterator it = second_casted3.listInstances();
				boolean cont = it.hasNext();
				while(cont){
					Individual nxt = (Individual)it.next();
					if(nxt.getLocalName().equalsIgnoreCase(first_casted3.getLocalName())){
						cont = false;
						ret = true;
					}
					else cont = it.hasNext();
				}
				return crit & ret;
			default:return false;
			}
		}

		public void setCond_type(int cond_type) {
			this.cond_type = cond_type;
		}

		public void setCrit(boolean crit) {
			this.crit = crit;
		}

		public void setFirstDescription(Object firstD) {
			this.first_description = firstD;
		}

		public void setSecondDescription(Object secondD) {
			this.second_description = secondD;
		}

		public int getCond_type() {
			return cond_type;
		}

		public boolean isCrit() {
			return crit;
		}

		public Object getFirstDescripition() {
			return first_description;
		}

		public Object getSecondDescription() {
			return second_description;
		}
		
		public String toString(){
			String ret="";
			switch(cond_type){
			case 1:
				ret += propToString(first_description);
				if(crit) ret += "= ";
				else ret += "!= ";
				ret += (String)second_description;
				break;
			case 2:
				ret += (String)first_description;
				if(crit) ret += "= ";
				else ret += "!= ";
				ret += (String)second_description;
				break;
			case 3:
				ret += propToString(first_description);
				if(!crit)ret += " not"; 
				ret += " instance of ";
				ret += (String)second_description;
				break;
			default: 
				ret += "ERROR";
				break;
			}
			
			return ret;
		}
		
		public static String propToString(Object properties){
			String ret = "";
			try{
				String[] props = (String[])properties;
				for(int i = 0; i < props.length; i++){
					ret += props[i];
					if(i<props.length-1) ret+=".";
				}
			}
			catch(ClassCastException ex){
				ret = "Not a property";
			}
			catch(NullPointerException ex){
				ret = "Condition not created";
			}
			catch(Exception ex){
				ret= "Unespected error";
			}
			return ret;
		}

		public Object getFirst() {
			return first;
		}

		public void setFirst(Object first) {
			this.first = first;
		}

		public Object getSecond() {
			return second;
		}

		public void setSecond(Object second) {
			this.second = second;
		}
	}

	public Condition getAdaptation_condition() {
		return adaptation_condition;
	}


	public Condition getDecision_condition() {
		return decision_condition;
	}

}
