The sematic analysis of the cool copiler is done by following the procedures given below:
1. Default class installation
2.Generating inheritance graph
3.Traversing the inheritance graph
4.Inserting all the values into Scope table.
5.Naming And Scoping
6.Type checking

Default class installation: The basic classes of the cool language are stored into a hash table with the key as name of the class and the value as the basic_class class written in the program .basic_class class is similar to that of the AST.class_ which has class name,parent of the class,attribute list (AST.attr)and method list(AST.method).The basic classes of cool language are Object,String ,Int,Bool,IO.The main has parent as object.Basic classes invlove the following.
Object:Has methods
	abort():Object :-Halts the program execution with an error message
	type_name() :String:-Returns string with the name of the class of the object
	copy() :SELF_TYPE :-Produces a shallow copy of the object
IO: Has methods
	out_string(x:String) :SELF_TYPE:-Prints the argument and returns self value
	out_int(X:Int):SELF_TYPE :- Prints the argument and returns self value
	in_string():String :- Reads an String form the standard user input
	in_int() :Int :-Reads an int form the standard user input
Int:There are no methods special to Int.
String:Has methods
	length() :Int :-Returns length of the self parameter.
	concat(s:String) :String :-returns the string formed by concatenating s after self.
	Substr(i:Int,l:Int) :String :-returns the substring of its self parameter.
Bool:The Bool class provides true and false.There are neither methods nor attributes.
These basic classes can't be redefined. String, Int, Bool of basic classes can't be inherited.
SELF_TYPE handling is done to some extent through the functions conforms and parent_type.


Generating inheritance graph:The inheritance graph is built in adjacency list manner.First the classes object and IO are inserted into the graph.Object is the class root of the inheritance graph.If a new class arrives, here we are checking if the class belongs to one of the basic classes of cool language .If the class belongs to one of the basic classes then report error .If the class parent belongs to basic classes of cool language then report error.If the above two conditions doesnot hold add the class to the list by adding an empty list corresponding to that class.If a classA inherits calss B then an edge is added from class B to class A int the adjacency list.

Traversing the inheritance graph:The constructed inheritance graph above is travesrsed three times
1.Cycle check: For cycle check the tree is traversed in DFS-order.We are traversing the graph along the depth and checking if a node is visited twice .if the node is visited twice report error. If many cycles exits in the graph all cycles are reported. If cycles are found in the tree then exit.
2. We are inserting the classes for semantic check inside the class in BFS order so that all the attributes and methods are inherited .
3. The semantic error check for all the attributes and methods of the class is done .The semantic errors involve the following.
For Attributes:
	Multiple definition of attribute.
	Redefinition of the attribute of inherited class.
For Methods:
	Multiple definition of methods.
	Incompatible number of parameters in the redefinition of the method of inherited class.
	Miss match of Return type in the redefinition of the method of inherited class.
	Miss match in the parameter types in the redefinition of the method of inherited class.
Conforms function:conforms function which recusively checks whether the two types or equal or not .
	Handles self_type virtually.
	If the two types are exactly equal then it returns true
	else it again calls conform for the first parameter.parent and second parameter if the first parameter.parent is not null.
	Else returns false.
	In the conforms calling we give first parameter as value type and second parameter as assigned type id
Parent-type:Performs the join operation as mentioned in the cool manual.
Type Checking:
	Type checking for method return type id and method body return type are done using conforms function with first parameter as method.body.type and second type as method,typeid
	Type checking for Attributes is also done using confomrs method with attr.value.type as first argument and attr.typeid as second argument.
	The type checking in typecase and condition expressions is done using parent_type method.
Every program must have a class Main. The Main class must have the method main that takes no formal parameters. The main method must be defined in class Main (not inherited from another class).
All class names are globally visible. Classes may not be redefined.
No method name may be defined multiple times in a class, and no attribute name may be defined multiple times in a class, but a method and an attribute may have the same name.
All attributes have scope local to the class, and all methods have global scope.
Most of the static type constraints are being checked.

Expression:
Assignment: ID <- expr
	The static type of the expression must conform to the declared type of the identifier. 
	The static type of an assignment is the static type of expr.
Dispatch and Static Dispatch:
	<expr>.<id>(<expr>,...,<expr>)
	<id>(<expr>,...,<expr>)
	<expr>@<type>.id(<expr>,...,<expr>)(Static Dispatch)
	The semantic checks invloves:
	The class method being called by the caller must be defined in the class.
	The dispatch and the methods should have same number of parameters.
	The dispatch parameter’s type and the method parameter types sholud be matched in the order of parameters.
	The return type of the dispatch and the return type of the method must be same.
	The second form is similar to self.<id>(<expr>,...,<expr>)
	For the Static dispatch an extra check is used.The type of the epression to the left of @ and to that to the right of @ must be matched.
Conditionals: if expr then expr else expr fi
	The predicate is evaluated first. The predicate must have static type Bool.
	The branches may have any static types. 
	To represent join operation in finding the static type of the conditional, parent_type function is defined. 
	The static type of the conditional is join of static types of branches of the conditional.
Loops: while expr loop expr pool
	The predicate is evaluated before each iteration of the loop. 
	The predicate must have static type Bool. 
	The body may have any static type. 
	The static type of a loop expression is Object.
Blocks: { [[expr; ]] + }
	The expressions of a block may have any static types. 
	The static type of a block is the static type of the last expression.
Let: let ID : TYPE [ <- expr ] [[, ID : TYPE [ <- expr ]]] * in expr
	The optional expressions are initialization; the other expression is the body. 
	Every let expression must introduce at least one identifier.
	Identifiers introduced by let also hide any definitions for the same names in containing scopes.
	The type of an initialization expression must conform to the declared type of the identifier. 
	The type of let is the type of the body.
Case: case expr of [[ID : TYPE => expr; ]] + esac
	The static type of a case expression is join of static type of all branches.
	The identifier id introduced by a branch of a case hides any variable or attribute definition for id visible in the containing scope.
new TYPE
	The static type is <type>.
isvoid expr
Binary arithmetic operations:
	expr1 + expr2
	expr1 − expr2
	expr1 * expr2
	expr1 / expr2
	first expr1 is evaluated and then expr2.
	The static types of the two sub-expressions must be Int.
	The static type of the expression is Int.
Comparision operators:	
	expr1 < expr2
	expr1 <= expr2
	expr1 = expr2
	For < and <=, handling is same as the binary arithmetic operations, except that the static type of the result is Bool.
	For = : If either <expr1> or <expr2> has static type Int, Bool, or String, then the other must have the same static type. Any other types, including SELF TYPE, may be freely compared.
Unary arithmetic operator:  ̃expr
	The expression <expr> must have static type Int and the entire expression has static type Int.
Unary logical operator: not expr
	The expression must have static type Bool and the entire expression has static type Bool.
Object
	Valid identifier.
Int
	The default initialization for variables of type Int is 0 (not void). So, node.type is "Int".
String
Bool
	The default initialization for variables of type Bool is false (not void). So, node.type is "Bool".



