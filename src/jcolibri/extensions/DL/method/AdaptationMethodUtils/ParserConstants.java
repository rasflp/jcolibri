package jcolibri.extensions.DL.method.AdaptationMethodUtils;


/**
 * @author A.Luis Diez Hernández
 *
 */
public class ParserConstants {
	public static final int ERROR = 0;
	
	public static final int WORD = 1;
	
	//Symbol :
	public static final int DOUBLE_SPOT = 2;
	
	//Symbol :=
	public static final int ASSIGN = 3;
	
	//Symbol =
	public static final int EQUAL = 4;
	
	//Symbol !=
	public static final int UNEQUAL = 5;
	
	//Symbol [
	public static final int SQ_BR_START = 6;
	
	//Symbol ]
	public static final int SQ_BR_END = 7;
	
	//Symbol (
	public static final int BR_START = 8;
	
	//Symbol )
	public static final int BR_END = 9;
	
	public static final int LEX_END = 10;
	
	public static final int OR = 11;
	
	public static final int AND = 12;
		
	public static final int NOT = 13;

	//Symbol: /
	public static final int ONTO_SEPARATOR = 14;
	
	//Symbol: @
	public static final int RULE_SEPARATOR = 15;
	
	public static final int CASE = 16;
	
	//Symbol: #
	public static final int CELL = 17;
	
	public static final int SUBS = 18;
	
	public static final int DIRECT = 19;
	
	public static final int ANY = 20;
	
	public static final int DEPEND = 21;
	
	public static final int CASE_SEPARATOR = 22;
	
	public static final int INSTANCE = 23;
	
	public static final String SUBS_STRING = "SUBSTITUTE";
	
	public static final String DIRECT_STRING = "DIRECT";
	
	public static final String ANY_STRING = "ANYOTHERINSTANCEOF";
	
	public static final String CASE_STRING = "CASE";
	
	public static final String DEPEND_STRING = "DEPENDENCIES";
	
	public static final String NOT_STRING = "not";
	
	public static final String INST_STRING = "instanceOf";
	
	public static int isReservedWord(String word){
		if(word.equalsIgnoreCase(SUBS_STRING)) return SUBS;
		if(word.equalsIgnoreCase(DIRECT_STRING)) return DIRECT;
		if(word.equalsIgnoreCase(ANY_STRING)) return ANY;
		if(word.equalsIgnoreCase(DEPEND_STRING)) return DEPEND;
		if(word.equalsIgnoreCase(NOT_STRING)) return NOT;
		if(word.equalsIgnoreCase(INST_STRING)) return INSTANCE;
		if(word.equalsIgnoreCase(CASE_STRING)) return CASE;
		return WORD;
	}
	
	public static String translate(int cod){
		switch(cod){
		case ERROR:return "ERROR";
		
		case WORD:return "WORD";
		
		case DOUBLE_SPOT: return ":";
		
		case ASSIGN: return ":=";
		
		case EQUAL: return "=";
		
		case UNEQUAL: return "!=";
		
		case SQ_BR_START: return "[";
		
		case SQ_BR_END: return "]";
		
		case BR_START: return "(";
		
		case BR_END: return ")";
		
		case LEX_END: return "LEX END";
		
		case OR: return "OR";
		
		case AND: return "AND";
			
		case NOT: return "NOT";

		case ONTO_SEPARATOR: return "/";
		
		case RULE_SEPARATOR: return "@";
		
		case CASE: return CASE_STRING;
		
		case CELL: return "#";
		
		
		case SUBS: return SUBS_STRING;
		
		case DIRECT: return DIRECT_STRING;
		
		case ANY: return ANY_STRING;
		
		case DEPEND: return DEPEND_STRING;
		
		case CASE_SEPARATOR: return ".";
		
		case INSTANCE: return INST_STRING;
		
		default: return "UNKNOWN";
		
		}
	}
	
	public static final char ERROR_CHAR = '$';
}
