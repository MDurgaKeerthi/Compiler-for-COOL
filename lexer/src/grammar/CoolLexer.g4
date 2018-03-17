lexer grammar CoolLexer;

tokens{
	ERROR,
	TYPEID,
	OBJECTID,
	BOOL_CONST,
	INT_CONST,
	STR_CONST,
	LPAREN,
	RPAREN,
	COLON,
	ATSYM,
	SEMICOLON,
	COMMA,
	PLUS,
	MINUS,
	STAR,
	SLASH,
	TILDE,
	LT,
	EQUALS,
	LBRACE,
	RBRACE,
	DOT,
	DARROW,
	LE,
	ASSIGN,
	CLASS,
	ELSE,
	FI,
	IF,
	IN,
	INHERITS,
	LET,
	LOOP,
	POOL,
	THEN,
	WHILE,
	CASE,
	ESAC,
	OF,
	NEW,
	ISVOID,
	NOT
}

/*
  DO NOT EDIT CODE ABOVE THIS LINE
*/

@members{

	/*
		YOU CAN ADD YOUR MEMBER VARIABLES AND METHODS HERE
	*/

	/**
	* Function to report errors.
	* Use this function whenever your lexer encounters any erroneous input
	* DO NOT EDIT THIS FUNCTION
	*/
	public void reportError(String errorString){
		setText(errorString);
		setType(ERROR);
	}
	
   public void not_matched(){    /*to print non matched text*/
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
		reportError(text);
	}
	
	/*public void lengthString() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();

		if(text.length() > 1022) {
         reportError("String constant too long");
   		}
	   } 
	   
	   public void commentcheck() {   
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
      
      int len = text.length();
      int count = 0;
      int commentcount = 0;
      reportError(text);
      while(count < len){
         if(text.charAt(count) == '('){
            if(text.charAt(count+1) == '*'){
               commentcount++;
               count = count + 2;
               }}
         else if(text.charAt(count) == '*'){
            if(text.charAt(count+1) == ')'){
               commentcount--;
               count = count + 2;
               }}
         else if(text.charAt(count) == EOF){
              reportError("EOF in comment");
               return;
   		} 
   		count = count + 1; 
      }
      if(commentcount < 0){
         reportError("unmatched");
               return;
   		}
   	else	if(commentcount > 0){
         reportError("EOF in comment");
               return;
   		} 
   }
	   */
	   
	public void processString() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();

		//write your code to test strings here
		
		int len = text.length() - 2;
		text = text.substring(1, text.length()-1);   //eliminating quotes outside
		if(len > 1024) {
         reportError("String constant too long");
         return;
   		}
   	else{	
		   int count = 0;
		   String returntext = "";
		   while(count < len) {
		      if(text.charAt(count) == '\0'){
		         reportError("String constant contains null character");
		         return;
		         }
		      else if(text.charAt(count) == '\n'){         //non escaped newline
		         reportError("Unterminated String constant");
		         return;
		         }   
		      else if(text.charAt(count) == '\\'){   //inserting special characters by case checkig
		         switch(text.charAt(count+1)){ 
		            case 'n':{
		               returntext += '\n';
		               break; 
		               }
		            case 'b':{
		               returntext += '\b';
		               break; 
		               }
		            case 't':{
		               returntext += '\t';
		               break; 
		               }
		            case 'f':{
		               returntext += '\f';
		               break; 
		               }
		            default :
						   returntext += text.charAt(count+1);
               }
               count += 2;
            }
            else {
                returntext += text.charAt(count);   
                count++;      
            }     
         }   
		   setText(returntext);
      }   

	}     //end of function
}
/*
	WRITE ALL LEXER RULES BELOW
*/

ERROR		: '"' (.*? ('\\u0000'))+ ~["\nEOF]* ["\nEOF] { reportError("String contains null character"); }
			| '"' ~('"'|'\n')* (.EOF) { reportError("EOF in string constant"); }
			| '"' ~('"'|'\n')* '\n' {reportError("Unterminated string constant");};
			
		
			
WS           : [ \n\f\r\t\v]+ -> skip ;   //whitespace

LINECOMMENT  : '--' ~[\n\r]* ->skip;    //escaping line comments

ONLY_END_COMMENT  : '*)'  {reportError("Unmatched *)"); } ;  //unmatched coment



LPAREN      : '(';         //normal lexer rules   
RPAREN      : ')';      
COLON       : ':';
ATSYM       : '@'; 
SEMICOLON   : ';';
COMMA       : ',';
PLUS        : '+';
MINUS       : '-';
STAR        : '*';
SLASH       : '/';
TILDE       : '~';
LT          : '<';
EQUALS      : '=';
LE          : '<=';
LBRACE      : '{';
RBRACE      : '}';
DOT         : '.';
ASSIGN      : '<-';
DARROW      : '=>';


CLASS       : [cC][lL][aA][sS][sS];       //for keywords
ELSE        : [eE][lL][sS][eE];
FI          : [fF][iI];
IF          : [iI][fF];
IN          : [iI][nN]; 
INHERITS    : [iI][nN][hH][eE][rR][iI][tT][sS];
LET         : [lL][eE][tT];
LOOP        : [lL][oO][oO][pP];
POOL        : [pP][oO][oO][lL];
THEN        : [tT][hH][eE][nN];
WHILE       : [wW][hH][iI][lL][eE];
CASE        : [cC][aA][sS][eE];
ESAC        : [eE][sS][aA][cC];
OF          : [oO][fF];
NEW         : [nN][eE][wW];
ISVOID      : [iI][sS][vV][oO][iI][dD];
NOT         : [nN][oO][tT];



BOOL_CONST  : [t][rR][uU][eE]                //for constants
              | [f][aA][lL][sS][eE];
INT_CONST   : [0-9]+;

TYPEID      : [A-Z][_a-zA-Z0-9]*;
OBJECTID    : [a-z][_a-zA-Z0-9]*;

NON_ESCAPE  : '"' '\\' '"' {reportError("String contains non-escapable characters");}; 

STR_CONST   : '"' ('\\"'|~'"')* '"' {processString();};
             
EVERYTHING_ELSE : . {not_matched();};         
     
BLOCKCOMMENT : '(*'-> pushMode(COMMENT), skip;        //for comments

mode COMMENT;
ON_EOF     	: .(EOF) { reportError("EOF in comment"); } ;
PUSHCOMMENT	: '(*' -> pushMode(COMMENT), skip ;
POPCOMMENT	: '*)' -> popMode, skip ;
SKIPCOMMENT : . -> skip ;

             
            

