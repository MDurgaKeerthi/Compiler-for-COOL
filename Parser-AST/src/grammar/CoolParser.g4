/*  AST generation - parser phase B  

    cs15btech11021
    cs15btech11024   */
    
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
	Add Grammar rules and appropriate actions for building AST below.
*/

program returns [AST.program value] : 
	cl=class_list EOF 
	{
		$value = new AST.program($cl.value, $cl.value.get(0).lineNo);
	}
	;

/* (class_ SEMICOLON)+  */
class_list returns [ArrayList<AST.class_> value]
	@init
	{
		$value = new ArrayList<AST.class_>();
	}
	:
		(cla = class_ SEMICOLON {$value.add($cla.value);})+
	;


/*CLASS TYPEID (INHERITS TYPEID)? LBRACE (feature SEMICOLON)* RBRACE
  */
class_ returns  [AST.class_ value]  :
	ct=CLASS typeid=TYPEID LBRACE fea_li=feature_list RBRACE
	{
		$value = new AST.class_($typeid.getText(), filename, "Object", $fea_li.value, $ct.getLine());
	}
	| ct=CLASS typeid=TYPEID INHERITS in_typeid=TYPEID LBRACE fea_li=feature_list RBRACE
	{
		$value = new AST.class_($typeid.getText(), filename, $in_typeid.getText(), $fea_li.value, $ct.getLine());
	}
	;


feature_list returns [ArrayList<AST.feature> value]
	@init    /*   (feature SEMICOLON)*    */
	{
		$value = new ArrayList<AST.feature>();
	}
	:
		(i = feature SEMICOLON {$value.add($i.value);})*
   ;
   
feature returns [AST.feature value] :
	function = method
	{
		$value = $function.value;
	}
	| variable = attr
	{
		$value = $variable.value;
	}
    ;


/* OBJECTID COLON TYPEID ( ASSIGN expr )?    */
attr returns [AST.attr value]	:
	id=OBJECTID COLON typeid=TYPEID
	{
		$value = new AST.attr($id.getText(), $typeid.getText(), new AST.no_expr($id.getLine()), $id.getLine());
	}
	| id=OBJECTID COLON typeid=TYPEID ASSIGN ex=expression
	{
		$value = new AST.attr($id.getText(), $typeid.getText(), $ex.value, $id.getLine());
	}
	;

/* OBJECTID LPAREN ( formal (COMMA formal)*)? RPAREN COLON TYPEID LBRACE expr RBRACE   */
method returns [AST.method value] :
	id=OBJECTID LPAREN RPAREN COLON typeid=TYPEID LBRACE ex=expression RBRACE
	{
		$value = new AST.method($id.getText(), new ArrayList<AST.formal>(), $typeid.getText(), $ex.value, $id.getLine());
	}
	| id=OBJECTID LPAREN fr_list=formal_list RPAREN COLON typeid=TYPEID LBRACE ex=expression RBRACE
	{
		$value = new AST.method($id.getText(), $fr_list.value, $typeid.getText(), $ex.value, $id.getLine());
	}
	;

formal_list returns [ArrayList<AST.formal> value]
	@init     /*  formal (COMMA formal)*   */
	{
		$value = new ArrayList<AST.formal>();
	}
	:
		f=formal {$value.add($f.value);} (COMMA f1=formal {$value.add($f1.value);})*
	;

formal returns [AST.formal value]   :
	id=OBJECTID COLON typeid=TYPEID
	{
		$value = new AST.formal($id.getText(), $typeid.getText(), $id.getLine()) ;
	}
	;

expression_list returns [ArrayList<AST.expression> value]
	@init  /* expr (COMMA expr)* */
	{
		$value = new ArrayList<AST.expression>();
	}
	:
		( ex = expression {$value.add($ex.value);} (COMMA ex = expression {$value.add($ex.value);})* )?
	;

block_expr_list returns [ArrayList<AST.expression> value]
	@init   /* (expr SEMICOLON)+   */
	{
		$value = new ArrayList<AST.expression>();
	}
	:
		(ex = expression SEMICOLON {$value.add($ex.value);})+
	;

case_expr_list returns [ArrayList<AST.branch> value]
	@init  /* (OBJECTID COLON TYPEID DARROW expr SEMICOLON)+  */
	{
		$value = new ArrayList<AST.branch>();
	}
	:
		(id=OBJECTID COLON typeid=TYPEID DARROW ex=expression SEMICOLON  {$value.add(new AST.branch($id.getText(), $typeid.getText(), $ex.value, $id.getLine()));})+
	;

let_expr_list returns [ArrayList<AST.attr> value]
	@init   /*OBJECTID COLON TYPEID ( ASSIGN expr )? ( COMMA OBJECTID COLON TYPEID ( ASSIGN expr )?)* */
	{
		$value = new ArrayList<AST.attr>();
	}
	:
	attr1 = attr { $value.add($attr1.value); }
	(COMMA attr2 = attr {$value.add($attr2.value);})*
	;
						

expression returns [AST.expression value]   :
        /*  expr DOT OBJECTID LPAREN (expr (COMMA expr)*)? RPAREN     */
        ex=expression DOT id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.dispatch($ex.value, $id.getText(), $expr_list.value, $ex.value.lineNo);
	}
       /*  expr (ATSYM TYPEID) DOT OBJECTID LPAREN (expr (COMMA expr)*)? RPAREN     */
	| ex=expression ATSYM typeid=TYPEID DOT id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.static_dispatch($ex.value, $typeid.getText(), $id.getText(), $expr_list.value, $ex.value.lineNo);
	}
	
	/*  OBJECTID LPAREN (expr (COMMA expr)*)? RPAREN  */
	| id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.dispatch(new AST.object("self" , $id.getLine()) , $id.getText() , $expr_list.value , $id.getLine()); 
	}
	
	/*    IF expr THEN expr ELSE expr FI   */	
	| token=IF ex=expression THEN if_body=expression ELSE else_body=expression FI 
	{
			$value = new AST.cond($ex.value, $if_body.value, $else_body.value, $token.getLine());
	}
	
	/*  WHILE expr LOOP expr POOL   */
	| token=WHILE ex=expression LOOP while_body=expression POOL
	{
			$value = new AST.loop($ex.value, $while_body.value, $token.getLine());
	}
	
	/*   LBRACE (expr SEMICOLON)+ RBRACE   */
	| token=LBRACE bel=block_expr_list RBRACE	
	{
			$value = new AST.block($bel.value, $token.getLine());
	}
	
	/*  LET OBJECTID COLON TYPEID ( ASSIGN expr )? ( COMMA OBJECTID COLON TYPEID ( ASSIGN expr )?)* IN expr   */
	| token=LET let_li=let_expr_list IN ex1=expression
	{ 
		$value = $ex1.value; 
                AST.attr attr3;
                int i;
		for(i = $let_li.value.size() - 1; i >= 0; i--) 
		{
			attr3 = $let_li.value.get(i);
			$value = new AST.let(attr3.name, attr3.typeid, attr3.value, $value, $token.getLine());
		}
	}
	
	/* CASE expr OF (OBJECTID COLON TYPEID DARROW expr SEMICOLON)+ ESAC */
	| token=CASE ex=expression OF case_li=case_expr_list ESAC
	{	
			$value = new AST.typcase($ex.value, $case_li.value, $token.getLine());
	}
	/*new TYPE*/
	| token=NEW typeid=TYPEID
	{
			$value = new AST.new_($typeid.getText(), $token.getLine());
	}
        /*TILDE expr*/
	| token=TILDE ex=expression
	{
			$value = new AST.comp($ex.value, $token.getLine());
	}
        /*ISVOID expr*/		
	| token=ISVOID ex=expression
	{
			$value = new AST.isvoid($ex.value, $token.getLine());
	}
        /*expr ∗ expr*/
	| ex1=expression STAR ex2=expression
	{
			$value = new AST.mul($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr / expr*/
	| ex1=expression SLASH ex2=expression
	{
			$value = new AST.divide($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr + expr*/
	| ex1=expression PLUS ex2=expression
	{
			$value = new AST.plus($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr − expr*/
	| ex1=expression MINUS ex2=expression
	{
			$value = new AST.sub($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr < expr*/
	| ex1=expression LT ex2=expression
	{
			$value = new AST.lt($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr <= expr*/
	| ex1=expression LE ex2=expression
	{
			$value = new AST.leq($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*expr = expr*/
	| ex1=expression EQUALS ex2=expression
	{
			$value = new AST.eq($ex1.value, $ex2.value, $ex1.value.lineNo);
	}
        /*not expr*/
	| token=NOT ex=expression
	{
			$value = new AST.neg($ex.value, $token.getLine());
	}
        /*ID <- expr*/
	|<assoc=right> id=OBJECTID ASSIGN ex=expression
	{
			$value = new AST.assign($id.getText(), $ex.value, $id.getLine());  
	}
        /*(expr)*/
	| LPAREN ex=expression RPAREN
	{
			$value = $ex.value;
	}
        /*OBJECTID*/
	| id=OBJECTID
	{
			$value = new AST.object($id.getText(), $id.getLine());
	}
        /*integer*/
	| x=INT_CONST
	{
			$value = new AST.int_const(Integer.parseInt($x.getText()), $x.getLine());
	}
        /*string*/
	| y=STR_CONST
	{
			$value = new AST.string_const($y.getText(), $y.getLine());
	}
        /*true|false*/
	| z=BOOL_CONST
	{
			$value = new AST.bool_const(Boolean.parseBoolean($z.getText()), $z.getLine());
	}
	;
