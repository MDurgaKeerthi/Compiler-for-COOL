# COOL Compiler #

DESIGN:
COOL SYNTAX:-   
&nbsp;&nbsp;&nbsp;&nbsp; program ::= [class;]+  
&nbsp;&nbsp;&nbsp;&nbsp; class ::= class TYPE [inherits TYPE] {[feature;]* }  
&nbsp;&nbsp;&nbsp;&nbsp; feature ::= ID([formal [, formal]*] ) : TYPE { expr } | ID : TYPE [ <- expr ]  
&nbsp;&nbsp;&nbsp;&nbsp;   formal ::= ID : TYPE   
&nbsp;&nbsp;&nbsp;&nbsp;   expr ::= ID <- expr  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| expr[@TYPE].ID([expr[, expr]*])  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| ID([expr[,expr]*])   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| if expr then expr else expr fi    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| while expr loop expr pool   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| {[expr]+}    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| let ID : TYPE [<-TYPE] [, ID:TYPE[<-expr]]* in expr  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| case expr of [ID:TYPE => expr;]+ esac  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| new TYPE   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| isvoid expr          
	    | expr + expr    
	    | expr - expr   
	    | expr * expr   
	    | expr / expr   
	    | ~expr   
	    | expr < expr   
	    | expr <= expr   
	    | expr = expr   
	    | not expr    
	    | (expr)    
	    | ID    
	    | integer         
	    | string   
	    | true   
	    | false   
  
*each line in the CoolParser.g4 corresponds to a rule in this syntax. Sometimes a syntactic rule is split among different rules in parser for ease of readability of code.  
*the parser rule names are very close to those in syntax  
*Each parser rule is just like reading the rule in English   
 For example if the syntactic rule is expr + expr, the corresponding parser rule  is expr PLUS expr - just like how we read it.  
*The CoolParser.g4 grammar covers all the rules(which are stated above) as far as possible.    
----------------------------------------------------------------   
TEST CASES:   
good.cl : This contains all correct syntactic forms - just to demonstrate that parser is able to detect them  
All bad_NUM.cl programs demonstrate the error checking.  
bad1.cl  : program without a class   
bad2.cl  : class without a feature   
bad3.cl  : function feature without proper syntax - missing {expr}   
bad4.cl  : formal rule is violated - missing TYPE  
bad5.cl  : "if expr then expr else expr fi" rule is violated by missing "fi"   
bad6.cl  : "while expr loop expr pool" rule is violated by missing "expr" after while   
bad7.cl  : syntax for let is wrong - expr in the end of let rule is missing  
bad8.cl  : "case expr of [ID:TYPE => expr;]+ esac" rule is violated by missing semicolon after expr   
bad9.cl  : "{[expr,]+}" rule is violated - braces should contain an expression   
bad10.cl : "ID<-expr" rule is violated by missing expression on right hand side   
bad11.cl : "expr[@TYPE].ID([expr[,expr]*])" rule is violatd by missing TYPE after DOT   
bad12.cl : "ID([expr[,expr]*])" rule is violated by missing expr after COMMA   
bad13.cl : Rules of type expr+expr(binary operands), ~expr(unary operands), true are violated   
All bad_NUM.cl programs give same error as done with coolc as these examples correctly verify the grammar. These cover most of the error cases. These many test_cases are needed because this parser only shows the first error message.   
  
Other programs are included just for checking, they are all correct programs taken from examples provided for coolc setup, except for hellowolrd.cl.
