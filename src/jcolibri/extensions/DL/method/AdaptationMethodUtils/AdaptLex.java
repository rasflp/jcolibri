package jcolibri.extensions.DL.method.AdaptationMethodUtils;
/**
 * @author A. Luis Diez Hernández
 *
 */
public class AdaptLex {
	private char[] text;
	private int sentinel;
	
	private String act_token;
	
	public AdaptLex(String text){
		this.text = text.toCharArray();
		sentinel = 0;
	}
	
	public String getToken(){
		return act_token;
	}
	
	public int take_step(){
		char act_char = ' ';
		if(sentinel<text.length){
			act_char = text[sentinel++];
		}
		
		while(sentinel<text.length){
			//Eliminate tabulations, blanks and line jumps
			while(((act_char==' ')||(act_char =='\t')||(act_char=='\n'))&&(sentinel<text.length)){
				act_char = text[sentinel++];
			}
			
				if(act_char == '['){
//					act_char = text[sentinel++];
					return ParserConstants.SQ_BR_START;
				}
				else if(act_char == ']'){
//					act_char = text[sentinel++];
					return ParserConstants.SQ_BR_END;
				}
				else if (act_char == '/'){
//		    	   act_char = text[sentinel++];
		    	   return ParserConstants.ONTO_SEPARATOR;
				}
				else if((act_char == ':')/*&&(sentinel<text.length)*/){
//			    	   act_char = text[sentinel++];
			    	   return ParserConstants.DOUBLE_SPOT;
			       }
			       else if(act_char == '='){
			    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];
			    	   if(act_char != '=') sentinel--;
			    	   return ParserConstants.EQUAL;
			       }
			       else if((act_char == '!')&&(sentinel < text.length)){
			    	   act_char = text[sentinel++];
			    	   if((act_char == '=')/*&&(sentinel < text.length)*/){
//			    		   act_char = text[sentinel++];
			    		   return ParserConstants.UNEQUAL;
			    	   }
			    	   else return ParserConstants.ERROR;
			       }
			       else if(act_char == '('){
	/*		    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];*/
			    	   return ParserConstants.BR_START;
			       }
			       else if(act_char == ')'){
/*			    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];*/
			    	   return ParserConstants.BR_END;
			       }
			       else if(act_char == '#'){
/*			    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];*/
			    	   return ParserConstants.CELL;
			       }
			       else if(act_char == '@'){
/*			    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];*/
			    	   return ParserConstants.RULE_SEPARATOR;
			       }
			       else if(act_char == '.'){
/*			    	   if(sentinel<text.length)
			    		   act_char = text[sentinel++];*/
			    	   return ParserConstants.CASE_SEPARATOR;
			       }
			       else /* We try to form a complete word */
					if (((act_char>='A')&&(act_char<='Z'))||((act_char>='a')
		    		   &&(act_char<='z'))||(act_char=='-')){
		        act_token="";
		        while ((((act_char>='A')&&(act_char<='Z'))||
		        		((act_char>='a')&&(act_char<='z'))||
		        		((act_char>='0')&&(act_char<='9'))||
		        		(act_char=='_')||(act_char=='-'))
		        		&&(sentinel<text.length)){
		          act_token = act_token + act_char;
		          try{
		        	  act_char = text[sentinel++];
		          }
		          catch(ArrayIndexOutOfBoundsException aob){
		        	  sentinel++;
		          }
		        }
		        if (((act_char>='A')&&(act_char<='Z'))||((act_char>='a')
			    		   &&(act_char<='z'))||(act_char=='-')&&(sentinel==text.length))
		        	act_token= act_token+ act_char;
		        else sentinel--;
		        return ParserConstants.isReservedWord(act_token);
		       }
		}
		return ParserConstants.LEX_END;
	}
	
}
