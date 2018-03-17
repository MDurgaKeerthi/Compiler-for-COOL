# COOL Compiler #

---------------------------------------------------------------------------------------
DESIGN:

The Program AST starts with node (AST.program). A program is a class_list followed by EOF.
Program has its corresponding action. The action is embedded with creating new AST.program which is embedded with class_list.

Class_list =[class_ SEMICOLON]+  
class_list returns an array list of type AST.class_  
Class_list on initializing creates a new Arraylist of type AST.class_  
Each class_ in the class_list is embedded with an action which is embedded with the value of class_ .The action is wriiten inside the parsing rule to print all the childs.  
class_returns Ast.class_ value  
class_:CLASS TYPEID LBRACE feature_list RBRACE|CLASS TYPEID INHERITS TYPEID LBRACE feature_list RBRACE  
if the first rule is applied then the action involves creating new AST.class_ with typeid,filename,object,feature_list.value and line no  
if the second rule is applied then the action involes creating new AST.class_ with typeid of the class i.e class , filename,inherited class name,feature_list.value,line no  
  
feature_list:returns arryalist of type AST.feature.This is similar to class_list  
feature is similar to class_ with its grammar rules.   
Feature_list is list of entities of a class .Each feature of feature_list can be either a method or an attribute (attr in program).Attr is basically an assignment or a declaration of variable.   
  
Method contains a list of parameters called formal_list.   
Methods consists of expressions. Expressions lie inside the body of the method.   
ANTLR4 is uses LL parsing it handles left recursion and left factoring.Precedence is followed as ANTLR mathes to the first rule in the grammar.if the first rule for expr is expr*expr and follwed by expr+expr then for the a=b+c*d it first maps to expr*expr then maps to expr+expr os that the semantic analysis is done here itslef.  
  
In order to handle the precedence the expression relues are written as follows:  
1.Dispatch   
2.static dispatch    
3.Loops and Conditional expressions like IF,WHILE,CASE,LET   
4.Block expressions   
5.logical expressions(comp,isvoid)   
6.arithmetic expressions(mul,div,add,sub LT,LE,Equals ,NOT in order)   
7.Assignment   
8.Type constants(integer, string,bool (true/false) )   
   
Dispatch and static dispatch are expressions ehich involve calling a function of a different class.  
  
Logical expressions invlove compliment of the expression.  
  
arithmetic expressions involve all the arithmetic operations precedence is as follows  
*,/  
+,-  
<=,<,=  
not  
  
Asignment expression: assigning va lue to the variable a<-1   
   
Loops and Conditional expressions :Inclueds IF then ElSE,WHILE loop.CASE expressions,LET Expressions.   
    
LET exressions are recurrsive expressions.They contain attributes.We have to map several attributes to one node so the nesting of all these let expressions is done in a loop.Every let expression to the next of a let expression becomes becomes its body.The list of theses let expressions is mapped to let_list in he program.  
   
Block expressions: Bolck expressions are list of expressions seperated by semicolons enclosed in braces.These bolck expressions are mapped to block_expr_list in the program.  
   
Type Constants:For INT_CONST we do Integer.parseInt and print the value for STRING_CONST we direclty print the value.For BOOL_CONST we do Boolean.parsebool and print 1 if the value is true ,print 0 if the value is false  
  
The program invloves two types of rules   
1.Rules involving * and + (i.e listing rules)   
2.Rules which doesnot have * and +   
For the 1 st type of rules returns ArrayList of their respective node type . On @init the action ivloves creating a new arraylist of AST nodes of its respective type.These rules actions for AST are defined in the same way as class_list
For the 2 nd type of rule The just return AST node of their type.There is no @ init declaration and no action involved with it.  Actions are involved with the rules only.These rules actions are defined in the same way as class_  
   
TYPES OF AST NODES:   
1.AST.program          : For the program   
2.AST.class_           : For class_   
3.AST.feature          : For feature   
4.AST.attr             : For attribute   
5.AST.method           : For methods of the class  
6.AST.formal           : For formal_list list of formals i.e variable declarations  
7.AST.expression       : For expressions  
8.AST.branch           : For branch_list i.e case body  expressions  
9.AST.dispatch         : For dispatch  
10.AST.staticdispatch  : For static dispatch   
11.AST.comp	           : For compliment   
12AST.isvoid	         : For isvoid   
13.AST.mul             : For multiplication   
14.Ast.divide          : For division  
15.AST.plus            : For addition   
16:AST.sub             : For substraction  
17.AST.lt              : For less than   
18.AST.leq             : For less than equal  
19.AST.eq              : For equals   
20.AST.neg             : For negation    
21.AST.assign          : For assignment    
22.AST.cond            : For condition expressions   
23.AST.loop            : For loop expressions   
24.AST.let             : For let expressions     
25.AST.typcase         : For case expression     
26.AST.new_            : For new     
27.AST.object          : For objectid     
28.Ast.int_const       : For INT_CONST    
29.AST.string_const    : For STRING_CONST    
30.AST.bool_const      : for BOOL_CONST   
   
HANDLING ERRORS:   
*each line in the CoolParser.g4 corresponds to a rule as provided in the file. Sometimes a syntactic rule is split among different rules in parser for ease of readability of code.  
*the parser rule names are very close to those in syntax   
*Each parser rule is just like reading the rule in English    
    
----------------------------------------------------------------   
TEST CASES:   
good.cl : This contains all correct syntactic forms - just to demonstrate that parser is able to detect them   

ERRONEOUS PROGRAMS :programs are wriiten in the way to detect error for each ine of the grammar given in cool manual   
error1.cl : demonstrates the incorrect use of  paranthesis in expressions   
error2.cl : demonstrates the incorrect use of while expression   
error3.cl : demonstrates the incorrect use of class_list    
error4.cl : demonstrates the incorrect use of feature (no return type for method)   
error5.cl : demonstrates the incorrect use of feature second case and formal   
error6.cl : demonstrates the incorrect use of assigning the value to id (expr case1)  
error7.cl : demonstrates the incorrect use of function call of same class (expr case3)   
error8.cl : demonstrates the incorrect use of let expression   
error9.cl : demonstrates the incorrect use of braces (expr case 6)  
error10.cl : demonstrates the incorrect use of isvoid expression    
error11.cl : demonstrates the incorrect use of binary operations on two expressions(addition of two exprssions)   
error12.cl : demonstrates the incorrect use of new expression (new TYPE)   
error13.cl : demonstrates the incorrect use of if then else expression   
error14.cl : demonstrates the incorrect use of case expression    
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
   
