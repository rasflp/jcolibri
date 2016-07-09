package jcolibri.extensions.DL.method.AdaptationMethodUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

/**
 * @author A. Luis Diez Hernández
 *
 */
public class AdaptationParser {
	private AdaptLex lex;
	
	private Vector<AdaptationRule> rules;
	
	public AdaptationParser(){}
	public AdaptationParser(String path_file){
		try{
			FileReader fr = new FileReader(path_file);
			
			BufferedReader br = new BufferedReader(fr);
			
			String txt,line;
			txt ="";
			while((line = br.readLine())!= null) txt +=(line+"\n");
			
			lex = new AdaptLex(txt);
			
					
		}
		catch(Exception exc){
			
		}
	}
	
	public void analize(){
		int cod = lex.take_step();
		rules = new Vector<AdaptationRule>();
		while((cod!=ParserConstants.LEX_END)&&(cod!=ParserConstants.ERROR))
			cod = parseRule(cod);
	}
	
	public void analize(String txt){
		lex = new AdaptLex(txt);
		int cod = lex.take_step();
		rules = new Vector<AdaptationRule>();
		while((cod!=ParserConstants.LEX_END)&&(cod!=ParserConstants.ERROR))
			cod = parseRule(cod);
		
	}
	
	public Vector getRules(){
		return rules;
	}
	
	private int parseRule(int cod){
		if(cod == ParserConstants.ONTO_SEPARATOR){
			AdaptationRule ar = new AdaptationRule();
			cod = parseIdOnto(cod,ar);
			if(cod == ParserConstants.RULE_SEPARATOR){
				AdaptationRule.Condition cond = new AdaptationRule.Condition();;
				cod = lex.take_step();
				cod = parseCondition(cod,cond);
				ar.setDecision_condition(cond);
				if(cod == ParserConstants.RULE_SEPARATOR){
					cod = lex.take_step();
					cod = parseAdaptation(cod,ar);					
				}
				else return ParserConstants.ERROR;
			}
			else return ParserConstants.ERROR;
			System.out.println("Rule nº "+(rules.size()+1));
			System.out.println(ar.toString());
			rules.add(ar);
			return cod;
		}
		else return ParserConstants.ERROR;
		
	}
	
	private int parseIdOnto(int cod, AdaptationRule ar){
		if(cod == ParserConstants.ONTO_SEPARATOR){
			Vector<String> idOntoVec = new Vector<String>();
			int nextValidCod = ParserConstants.ONTO_SEPARATOR;
			while(cod == nextValidCod){
				if(nextValidCod == ParserConstants.WORD){
					idOntoVec.add(lex.getToken());
					nextValidCod = ParserConstants.ONTO_SEPARATOR;
				}
				else nextValidCod = ParserConstants.WORD;
				cod = lex.take_step();
			}
			String[] idOnto = new String[idOntoVec.size()];
			
			for(int i=0;i<idOntoVec.size();i++) idOnto[i] = (String)idOntoVec.elementAt(i);
			
			ar.setIdOnto(idOnto);
		}
		return cod;
	}

	private int parseIdCase(int cod, AdaptationRule.Condition cond){
		if(cod == ParserConstants.CASE){
			String idCase = "";
			cod = lex.take_step();
			boolean avoid_first = true;
			int nextValidCod = ParserConstants.CASE_SEPARATOR;
			while(cod == nextValidCod){
				if(nextValidCod == ParserConstants.WORD){
					idCase+=lex.getToken();
					nextValidCod = ParserConstants.CASE_SEPARATOR;
				}
				else{
					if(avoid_first){
						avoid_first = false;
					}
					else idCase+=ParserConstants.translate(cod);
					nextValidCod = ParserConstants.WORD;
				}
				cod = lex.take_step();
			}
			if(cond.getCond_type() == 1) cond.setSecondDescription(idCase);			
			else cond.setFirstDescription(idCase);
		}
		return cod;
	}

	
	private int parseCondition(int cod, AdaptationRule.Condition cond){
		
		if(cod == ParserConstants.CASE){
				cond.setCond_type(2);
				cod = parseIdCase(cod, cond);
				if(cod == ParserConstants.EQUAL) cond.setCrit(true);
				else if(cod == ParserConstants.UNEQUAL) cond.setCrit(false);
				else return ParserConstants.ERROR;
				
				cod = lex.take_step();
				if(cod == ParserConstants.WORD){
					cond.setSecondDescription(lex.getToken());
					cod = lex.take_step();
				}
				else return ParserConstants.ERROR;
			}
			else{
				if(cod == ParserConstants.WORD){
					cod = parseProperty(cod, cond);
					 if (cod == ParserConstants.NOT){
							cond.setCond_type(3);
							cod = lex.take_step();
							cond.setCrit(false);
					}
					
						if(cod == ParserConstants.INSTANCE){
							if(cond.getCond_type()==-1){
							cond.setCond_type(3);
							cond.setCrit(true);
							}
							
							cod = lex.take_step();
							if(cod == ParserConstants.WORD) cond.setSecondDescription(lex.getToken());
							else return ParserConstants.ERROR; 
							cod = lex.take_step();
						}
						else {
							cond.setCond_type(1);
							switch(cod){
							case ParserConstants.EQUAL:
								cond.setCrit(true);
								break;
							case ParserConstants.UNEQUAL:
								cond.setCrit(false);
								break;
							default:return ParserConstants.ERROR;
							}
						cod = lex.take_step();
						cod = parseIdCase(cod, cond);
					}
				}
				else return ParserConstants.ERROR;
			}
			return cod;
	}
	
	private int parseProperty(int cod, AdaptationRule.Condition cond){
		if(cod == ParserConstants.WORD){
			Vector<String> idPropVec = new Vector<String>();
			int nextValidCod = ParserConstants.WORD;
			while(cod == nextValidCod){
				if(nextValidCod == ParserConstants.WORD){
					idPropVec.add(lex.getToken());
					nextValidCod = ParserConstants.ONTO_SEPARATOR;
				}
				else nextValidCod = ParserConstants.WORD;
				cod = lex.take_step();
			}
			String[] idProp = new String[idPropVec.size()];
			
			for(int i=0;i<idPropVec.size();i++) idProp[i] = (String)idPropVec.elementAt(i);
			
			cond.setFirstDescription(idProp);
		}
		return cod;
	}
	
	private int parseProperty(int cod, AdaptationRule ar){
		if(cod == ParserConstants.WORD){
			Vector<String> idPropVec = new Vector<String>();
			int nextValidCod = ParserConstants.WORD;
			while(cod == nextValidCod){
				if(nextValidCod == ParserConstants.WORD){
					idPropVec.add(lex.getToken());
					nextValidCod = ParserConstants.ONTO_SEPARATOR;
				}
				else nextValidCod = ParserConstants.WORD;
				cod = lex.take_step();
			}
			String[] idProp = new String[idPropVec.size()];
			
			for(int i=0;i<idPropVec.size();i++) idProp[i] = (String)idPropVec.elementAt(i);
			
			ar.setAdaptation_properties(idProp);
		}
		return cod;
	}
	
	private int parseAdaptation(int cod, AdaptationRule ar){
		if(cod == ParserConstants.SUBS){
			cod = parseSubstitution(cod,ar);
		}
		else cod = parseModify(cod,ar); 
		return cod;
	}
	
	private int parseSubstitution(int cod, AdaptationRule ar){
		if(cod == ParserConstants.SUBS){
			ar.setAdaptation_method(ParserConstants.SUBS_STRING);
			cod = lex.take_step();
			if(cod == ParserConstants.CELL){
				AdaptationRule.Condition cond = new AdaptationRule.Condition();
				cod = lex.take_step();
				cod = parseCondition(cod,cond);
				ar.setAdaptation_condition(cond);
				if(cod == ParserConstants.CELL){
					cod = lex.take_step();
				}
				else return ParserConstants.ERROR;
			}
			cod = parseDependencies(cod, ar);
		}
		else return ParserConstants.ERROR;
		return cod;
	}
	
	private int parseModify(int cod, AdaptationRule ar){
		if(cod == ParserConstants.DIRECT)	ar.setAdaptation_method(ParserConstants.DIRECT_STRING);
		else if(cod == ParserConstants.ANY)	ar.setAdaptation_method(ParserConstants.ANY_STRING);
		else return ParserConstants.ERROR;
		cod = lex.take_step();
			if(cod == ParserConstants.DOUBLE_SPOT){
				cod = lex.take_step();
				cod = parseProperty(cod, ar);
				if(cod == ParserConstants.DOUBLE_SPOT){
					cod = lex.take_step();
					if(cod == ParserConstants.WORD){
						ar.setAdaptation_resource(lex.getToken());
						cod = lex.take_step();
					}
					else return ParserConstants.ERROR;
					if(((cod == ParserConstants.CELL) && ar.getAdaptation_method().equalsIgnoreCase(
							ParserConstants.ANY_STRING))){
						AdaptationRule.Condition cond = new AdaptationRule.Condition();
						cod = lex.take_step();
						cod = parseCondition(cod,cond);
						ar.setAdaptation_condition(cond);
						if(cod == ParserConstants.CELL){
							cod = lex.take_step();
						}
						else return ParserConstants.ERROR;
					}
					
					cod = parseDependencies(cod, ar);
				}
				else return ParserConstants.ERROR;
			}
			else return ParserConstants.ERROR;
			return cod;
	}
	
	private int parseDependencies(int cod, AdaptationRule ar){
		if(cod == ParserConstants.DEPEND){
			cod = lex.take_step();
			if(cod == ParserConstants.WORD){
				ar.setDependence(lex.getToken());
				cod = lex.take_step();
			}
			else return ParserConstants.ERROR;
		}
		return cod;
	}
	
	public static void main(String[] args){
		
		String path= "c:/trainer.txt";
		AdaptationParser ap = new AdaptationParser(path);
		
		ap.analize();
/*		String txt = "/Entrenamiento/tienePartePrincipal/PartePrincipal/tieneEjercicio/Ejercicio@"+
"parteCuerpo/eSuperior == CASE.Description.lesion@"+
"ANYOTHERINSTANCEOF:parteCuerpo/eSuperior:Cuerpo #parteCuerpo/Cuerpo not instanceof eSuperior#"
+"\n"+
"/Entrenamiento/tienePartePrincipal/PartePrincipal/tieneEjercicio/Ejercicio@"+
"CASE.Solution.formaFisica == baja@"+
"DIRECT:hasIntensidad/Intensidad: Baja"
+"\n"+
"/Entrenamiento/tienePartePrincipal/PartePrincipal/tieneEjercicio/Ejercicio@"+
"CASE.Solution.formaFisica == baja@"+
"DIRECT:hasIntensidad/Intensidad: Baja";

		ap.analize(txt);*/
	}
}
