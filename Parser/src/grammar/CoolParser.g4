parser grammar CoolParser;

options {
	tokenVocab = CoolLexer;
}

@header{
	import cool.AST;
	import java.util.List;
}

@members{
	String filename;
	public void setFilename(String f){
		filename = f;
	}

/*
	DO NOT EDIT THE FILE ABOVE THIS LINE
	Add member functions, variables below.
*/

}

/*
	Add Grammar rules and appropriate error messages(As per need). Return Null in first rule.
*/

program returns [AST.program value]	: 
						cl=class_list EOF 
							{
								/*  Null in $value */
								$value = null;
							}
					;

//	program ::= [class;]+				
class_list : (class_ SEMICOLON)+	;

//class ::= class TYPE [inherits TYPE] {[feature;]*}
class_ : CLASS TYPEID (INHERITS TYPEID)? LBRACE (feature SEMICOLON)* RBRACE ;

/*feature ::= ID([formal [, formal]*] ) : TYPE { expr }
            | ID : TYPE [ <- expr ]
*/
feature  
   : OBJECTID LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPEID LBRACE expr RBRACE
	| dec_assign	
	;

//ID : TYPE [ <- expr ]
dec_assign : OBJECTID COLON TYPEID (ASSIGN expr)?	;

//formal : ID : TYPE 
formal : OBJECTID COLON TYPEID ;
	
						
expr   
   : OBJECTID ASSIGN expr   //ID <- expr
   
   // expr[@TYPE].ID([expr[, expr]*])
	| expr (ATSYM TYPEID)? DOT OBJECTID LPAREN expr_list RPAREN 
	
	// ID([expr[,expr]*])
	| OBJECTID LPAREN expr_list RPAREN  
	
	// if expr then expr else expr fi
	| IF expr THEN expr ELSE expr FI 
	
	// while expr loop expr pool
	| WHILE expr LOOP expr POOL
	
	// {[expr]+} 
	| LBRACE block_expr_list RBRACE	
	
	// let ID : TYPE [<-TYPE] [, ID:TYPE[<-expr]]* in expr
	| LET let_list IN expr
	
	// case expr of [ID:TYPE => expr;]+ esac
	| CASE expr OF case_list ESAC
	
	| NEW TYPEID	           // new TYPE
	| ISVOID expr             // isvoid expr       
	| expr PLUS expr	        // expr + expr 
	| expr MINUS expr         // expr - expr 
	| expr STAR expr	        // expr * expr 
	| expr SLASH expr	        // expr / expr 
	| TILDE expr              // ~expr 
	| expr LT expr            // expr < expr 
	| expr LE expr            // expr <= expr 
	| expr EQUALS expr        // expr = expr 
	| NOT expr                // not expr 
	| LPAREN expr RPAREN      // (expr) 
	| OBJECTID		           // ID 
	| INT_CONST  	           // integer       
	| STR_CONST		           //string
	| BOOL_CONST	           // true || false 
	;
	
expr_list : ( expr (COMMA expr)* )?	;		

block_expr_list : (expr SEMICOLON )+ ;

//[ID:TYPE => expr;]+
case_list : (OBJECTID COLON TYPEID DARROW expr SEMICOLON)+ ;

//ID : TYPE [<-TYPE] [, ID:TYPE[<-expr]]*
let_list :	dec_assign (COMMA dec_assign )* ;
