package cool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.*;
import java.util.Map.Entry;


import cool.AST.class_;


public class Semantic{
	private boolean errorFlag = false;
	public void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}

/*
	Don't change code above this line
*/
	 
	ScopeTable<AST.attr> scopeTable = new ScopeTable<AST.attr>();    //symbol table as provided
	String filename;																//filename for printing error msgs	
	List<String> static_types = Arrays.asList("Object","String","Int","Bool","IO");  //remaining static types other than classtypes
	
	public Semantic(AST.program program){
		
		createbasic(); 								//Initialization of class_list
		graph_traversal(program.classes);		//creates the basic graph, to add nodes later on
		
		for(AST.class_ clist : program.classes) {
			filename = clist.filename;					
			scopeTable.enterScope();			
			scopeTable.insert("self", new AST.attr("self", clist.name, new AST.no_expr(clist.lineNo), clist.lineNo));		
			/*HashMap<String,AST.method>hash=classes_list.get(e.name).methodlist;
			for(AST.method m :hash.values()){
				//System.out.println(m.name+" "+m.typeid);
				if(m.typeid.equals("SELF_TYPE")){
			   	m.typeid = e.name;
					//System.out.println(m.name+" "+m.typeid);
					}
			}*/
			
			scopeTable.insertAll(classes_list.get(clist.name).attrlist);		
			put_node(clist);
			scopeTable.exitScope();				
		}
		//No main class
		if(classes_list.get("Main")== null)
			reportError(filename, 1, "Program does not contain class 'Main'");
		else if(classes_list.get("Main").methodlist.containsKey("main") == false)  //no main method in main class
			reportError(filename, 1, "'Main' class does not contain 'main' method");
	}
	
	private HashMap<String, Basic_class> classes_list = new HashMap<String, Basic_class>();	 //hash table of all classes with their attr s and methods stored in new basic_class class
	private HashMap<String, Integer> height = new HashMap<String, Integer>();	//height of the Asttree		
	
	private void createbasic() {  //inserts all the basic classe of cool
		//Inserting Object class and its methods ot method list of the basic_class with name Object 
		classes_list.put("Object", new Basic_class("Object", null, new HashMap<String, AST.attr>(), new HashMap <String, AST.method>()));
		classes_list.get("Object").methodlist.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		classes_list.get("Object").methodlist.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("Object").methodlist.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "SELF_TYPE", new AST.no_expr(0), 0));
		height.put("Object", 0);  //after inserting object class the height of the class is zero
		
		//inserting IO class and its methods into hash table of class_list
		List <AST.formal> int_formals = new ArrayList<AST.formal>();
		int_formals.add(new AST.formal("out_string", "String", 0));
		List <AST.formal> string_formals = new ArrayList<AST.formal>();
		string_formals.add(new AST.formal("out_int", "Int", 0));		
		
		classes_list.put("IO", new Basic_class("IO", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
		classes_list.get("IO").methodlist.put("out_string", new AST.method("out_string", int_formals, "IO", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("out_int", new AST.method("out_int", string_formals, "IO", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("in_string", new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("in_int", new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("IO").methodlist.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "SELF_TYPE", new AST.no_expr(0), 0));
		height.put("IO", 1);
		//inserting Int class to the class_list
		classes_list.put("Int", new Basic_class("Int", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
		classes_list.get("Int").methodlist.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		classes_list.get("Int").methodlist.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("Int").methodlist.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "SELF_TYPE", new AST.no_expr(0), 0));
		height.put("Int", 1);
		//Inserting Bool class and its methods to the class_list 
		classes_list.put("Bool", new Basic_class("Bool", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
		classes_list.get("Bool").methodlist.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		classes_list.get("Bool").methodlist.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("Bool").methodlist.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "SELF_TYPE", new AST.no_expr(0), 0));
		height.put("Bool", 1);
		
		List<AST.formal> f_concat = new ArrayList<AST.formal>();
		f_concat.add(new AST.formal("s", "String", 0));
		
		List<AST.formal> f_substr = new ArrayList<AST.formal>();
		f_substr.add(new AST.formal("i", "Int", 0));
		f_substr.add(new AST.formal("l", "Int", 0));
		
		classes_list.put("String", new Basic_class("String", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
		classes_list.get("String").methodlist.put("length", new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		classes_list.get("String").methodlist.put("concat", new AST.method("concat", f_concat, "String", new AST.no_expr(0), 0));
		classes_list.get("String").methodlist.put("substr", new AST.method("substr", f_substr, "String", new AST.no_expr(0), 0));
		
		classes_list.get("String").methodlist.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		classes_list.get("String").methodlist.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		classes_list.get("String").methodlist.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "SELF_TYPE", new AST.no_expr(0), 0));
		height.put("String", 1);//after inserting IO,String,Bool, Int the height is 1 because all these classes have Object as parent 
	}
	
	
	private void class_insertion(AST.class_ clist) { //Checks for semantic errors inside the class i.e for inheritance handling of Attr and methods 
		Basic_class base = new Basic_class(clist.name, clist.parent, classes_list.get(clist.parent).attrlist, classes_list.get(clist.parent).methodlist);	
		HashMap <String, AST.method> tempm = new HashMap <String, AST.method>();
		HashMap <String, AST.attr> tempa = new HashMap<String, AST.attr>();
		int errorflag = 0;
		
		for(AST.feature feat : clist.features) {
			if(feat instanceof AST.attr) {
				AST.attr attrs = (AST.attr)feat;
				if(tempa.containsKey(attrs.name))  //If attribute is multilpy defined report error
					reportError(clist.filename, attrs.lineNo, "Attribute " + attrs.name + " is multiply defined in class.");
				else{
					tempa.put(attrs.name, attrs);
					if(base.attrlist.containsKey(attrs.name)){ //redifiniton of attribute of inherited class
						String str = "Attribute " + attrs.name + " is an attribute of an inherited class";
						reportError(clist.filename, attrs.lineNo, str);
						}
					else {
						base.attrlist.put(attrs.name, attrs);
					}
				}	
			}
			else if(feat instanceof AST.method) {
				AST.method meths = (AST.method) feat;
				if(tempm.containsKey(meths.name))   //method is multiply defined
					reportError(clist.filename, meths.lineNo, "Method " + meths.name + " is multiply defined.");
				else{
					tempm.put(meths.name, meths);
					errorflag = 0;
					if(base.methodlist.containsKey(meths.name)) {
						AST.method parent_me = base.methodlist.get(meths.name);
						if(meths.formals.size() != parent_me.formals.size()) {  //miss match in the no:of parameters of redifined method of inherited class
							reportError(clist.filename, meths.lineNo, "Incompatible number of formal parameters in redefined method." + meths.name);
							errorflag = 1;
						}
						else {  //miss match in the return types of redefined method of inherited class
							if(meths.typeid.equals(parent_me.typeid) == false) {
								reportError(clist.filename, meths.lineNo, "In redefined method " + meths.name + ", return type " + meths.typeid + " is different from original return type." + parent_me.typeid);
								errorflag = 1;
							}
							for(int i = 0; i < meths.formals.size(); ++i) {
								if(meths.formals.get(i).typeid.equals(parent_me.formals.get(i).typeid) == false) {//miss match in the parameter types of the redefined method of the inherited calss
									reportError(clist.filename, meths.lineNo, "In redefined method " + meths.name + ", parameter type"	+ meths.formals.get(i).typeid + " is different from original type." + parent_me.formals.get(i).typeid);
									errorflag = 1;
								}
							}
						}
					}
					if (errorflag ==0)
						base.methodlist.put(meths.name, meths);
				}
			}
		}

		height.put(clist.name, height.get(clist.parent) + 1); //height increased by 1 unit
		classes_list.put(clist.name, base); //add the class to the class_list
		
	}
	
	
	private boolean conforms(String x, String y) {
	   //checks if the value.type and actual assigned types are equal(inheritedly or exactly same)
		if(x.equals("SELF_TYPE") || y.equals("SELF_TYPE"))
			return true;
		else if(x.equals(y))
			return true;
		else {
			x =  classes_list.get(x).parent;
			if(x == null) 
				return false;
			else 
				return conforms(x, y);
		}
	}
	//For SELF_TYPE
	private List<String> remove(String a){
		List<String> array = Arrays.asList("Object","String","Int","Bool","IO");
		List<String> newarray = new ArrayList<String>();
		for(String str1: array){
			if(!str1.equals(a))
				newarray.add(str1);
		}
		return newarray;
	}
	
	private boolean basicconforms(String a, String b) {
		List<String> array = Arrays.asList("Object","String","Int","Bool","IO");
		if(array.contains(a)&&array.contains(b)){
			if(a.equals(b)) return false;
			else if(b.equals("Object") && array.contains(a)){
					return false;
				}
			else {
			List<String>temp = remove(a);
			if(temp.contains(b))
			return true;
			}
		}
		return false;
	}
	
	
	private String parent_type(String type1, String type2) {   //join operation
		if(type1.equals(type2) || type2.equals("SELF_TYPE")) 
			return type1;
		else if(type1.equals("SELF_TYPE")) 
			return type2;	
		else if(height.get(type1) < height.get(type2))		
			return parent_type(type2, type1);
		else
			return parent_type(classes_list.get(type1).parent, type2);
	}
	 
	private String findkey(HashMap <String, Integer> passedmap, Integer val){  //gets key for the value in hashmap
	   String returnstr = "";
		for (Entry<String, Integer> entry : passedmap.entrySet()) {
            if (entry.getValue().equals(val)) {
                returnstr = entry.getKey();
            }
		}
		return returnstr;
	}
	
	  
	private void graph_traversal(List <AST.class_> classes) {
		
		HashMap <String, AST.class_> index_cont = new HashMap <String, AST.class_> (); 
		HashMap <String, Integer> class_index = new HashMap <String, Integer> ();
		
		Integer size = 0;	
		class_index.put("Object", size);
		size++;
		class_index.put("IO", size);
		size++;
		//add the Object and Io to the graph 
		ArrayList < ArrayList <Integer> > graph = new ArrayList < ArrayList <Integer> >();
		graph.add(new ArrayList <Integer> (Arrays.asList(1))); //add edge between Object and IO
		graph.add(new ArrayList <Integer>());	
		
		for(AST.class_ cl : classes) {
			if(static_types.contains(cl.name)) { //redifintition of class belonging to basic class 
				reportError(cl.filename, cl.lineNo, "Cannot redefine class : " + cl.name);
				System.out.println("Compilation halted due to static semantic errors.");
				System.exit(1);
			}
			else if(cl.parent.equals("String") || cl.parent.equals("Bool")|| cl.parent.equals("Int") ) {//basic calsses cannot be inherited
				reportError(cl.filename, cl.lineNo, "Class cannot inherit : " + cl.parent);
				System.out.println("Compilation halted due to static semantic errors.");
				System.exit(1);
			}
			else if(class_index.containsKey(cl.name) == false) {
				class_index.put(cl.name,  size++);			
				index_cont.put(cl.name, cl);				
				graph.add(new ArrayList <Integer> ()); //new class add to the graph
			}
		}
		
		Integer x, y;
		for(AST.class_ cl : classes) {
			if(class_index.containsKey(cl.parent) == false) { //parent class is not found
				reportError(cl.filename, cl.lineNo, "Parent class not found : " + cl.parent);
				System.exit(1);
				}
			x = class_index.get(cl.parent);
			y = class_index.get(cl.name);
			graph.get(x).add(y);	//add edge from parent->child in the graph		
		}
		
		boolean cycles=false;
		boolean []visited = new boolean[size];
		boolean []recStack = new boolean[size];
		Arrays.fill(visited, Boolean.FALSE);
		Arrays.fill(recStack, Boolean.FALSE);
		//tarverse the graph and check for cycles uding DFS-order if multiple cycles found all are reported   
	   for(int i = 0; i <  size; i++){
			if (dfs_cycle_ckeck(i, visited, recStack,graph)){
				String keyforval = findkey(class_index,i);
				reportError(index_cont.get(keyforval).filename, 1, "Class " + keyforval + ", or an ancestor of " + keyforval + ", is involved in an inheritance cycle.");
				cycles= true;
			    }
			}
      if(cycles == true)
      	System.exit(1);	
                          
		Queue<Integer> que = new LinkedList<Integer>(); 
		que.offer(0);		
		
		int u;
		while (!que.isEmpty()) {
			u = que.poll();
			if(u != 1 && u != 0) 
				class_insertion(index_cont.get(findkey(class_index,u)));	//checking for the inheritance of attr and methods in BFS-order so that it can be easily checked	
			for(Integer v : graph.get(u)) {
				que.offer(v);
				}
			}
	}
	//DFs cycle check
	private boolean dfs_cycle_ckeck(int v, boolean[] visited, boolean []recStack,ArrayList < ArrayList <Integer> > adj){
   	if(visited[v] == false){
      	visited[v] = true;
        	recStack[v] = true;
        	for(Integer i:adj.get(v)){
            if ( !visited[i] && dfs_cycle_ckeck(i, visited, recStack, adj))
                return true;
            else if (recStack[i])
                return true;
        		}
        }
		recStack[v] = false;  
      return false;
    }
	
	private void put_node(AST.class_ class_) {   
		//Checks the semantic errors in the class and adds to ScopeTable if no errors found
		String errorstr = "";
		
		for(AST.feature cl_feat : class_.features) {
		
			if(cl_feat instanceof AST.method) {
				scopeTable.enterScope();
				AST.method method = (AST.method)cl_feat;
				
				for(AST.formal me_formal : method.formals) {
					AST.feature feat = scopeTable.lookUpLocal(me_formal.name); //lookup in the Scopetable
					
					if(feat != null && feat instanceof AST.attr) {
						AST.attr attr_fea = (AST.attr) feat; //if attr is multiplly defined 
						reportError(filename, attr_fea.lineNo, "Formal parameter " + attr_fea.name + " is multiply defined.");
					}
					//if no error found add the attr to the Scpoe table
					scopeTable.insert(me_formal.name, new AST.attr(me_formal.name, me_formal.typeid, new AST.no_expr(me_formal.lineNo), me_formal.lineNo));
				}
				//process the method body
				put_node(method.body);
				if(conforms(method.body.type,method.typeid) == false )  {//if method return type and actual assigned retrun type doesnot match report error here SELF_TYPE is also handled
					errorstr = "Inferred return type " + method.body.type + " of method " + method.name + " does not conform to declared return type " + method.typeid;
					reportError(filename, method.body.lineNo, errorstr);
			    }
				
				/*if(conforms(method.body.type,method.typeid) == false && conforms(method.typeid,method.body.type) == false  )  {
					reportError(filename, method.body.lineNo, "Inferred return type " + method.body.type + 
							" of method " + method.name + " does not conform to declared return type " + method.typeid);
			    }
				else if (basicconforms(method.body.type,method.typeid)){
					reportError(filename, method.body.lineNo, "Inferred return type " + method.body.type + 
							" of method " + method.name + " does not conform to declared return type " + method.typeid);
				}*/
				
				scopeTable.exitScope();
			}
			else if(cl_feat instanceof AST.attr) {
				AST.attr attr = (AST.attr)cl_feat ;
				
				if(attr.value.getClass() != AST.no_expr.class) {
					put_node(attr.value); //semantic check in the value of the attribute
					if(conforms(attr.value.type, attr.typeid) == false) {
					//if value and attr assigned return types doesnot matvh report error
						errorstr = "Inferred type " + attr.value.type + " of initialization of attribute " + attr.name + " does not conform to declared type " + attr.typeid;
						reportError(filename, attr.value.lineNo, errorstr);
						}
				}
			}
		}
	}
	
	
	
	private void put_node(AST.expression expr) {  //expression
		
		if(expr instanceof AST.assign){
			AST.assign node = (AST.assign)expr;
			put_node(node.e1);
			AST.attr nodeattr = scopeTable.lookUpGlobal(node.name);
			if(nodeattr == null)   //if object is properly created
				reportError(filename, node.lineNo, "Assignment to undeclared variable " + node.name);
			else if(conforms(node.e1.type, nodeattr.typeid) == false)
				reportError(filename, node.lineNo, "Type " + node.e1.type + " of assigned expression does not conform to declared type " + nodeattr.typeid + " of identifier " + nodeattr.name);
			node.type = node.e1.type;   //static type of assignment is static type of expr.
			}
		else if(expr instanceof AST.static_dispatch){
			AST.static_dispatch node = (AST.static_dispatch)expr;
			AST.method temp_md = null;
			String actualtype = "";
			String formaltype = "";
			
			put_node(node.caller);				
			for(AST.expression item : node.actuals)	
				put_node(item);

			Basic_class tempc = classes_list.get(node.typeid);
			node.type = "Object";
			if(tempc == null)
				reportError(filename, node.lineNo, "Static dispatch to undefined class " + node.typeid);
			else if(conforms(node.caller.type, tempc.name) == false)
				reportError(filename, node.lineNo, "Expression type " + node.caller.type + " does not conform to declared static dispatch type " + tempc.name);
			else {
				if(tempc.methodlist.containsKey(node.name)) {
					temp_md = tempc.methodlist.get(node.name);
					if(node.actuals.size() != temp_md.formals.size())
						reportError(filename, node.lineNo, "Method " + temp_md.name + " invoked with wrong number of arguments.");
					else {
						for(int i = 0; i < node.actuals.size(); ++i) {
							actualtype = node.actuals.get(i).type;
							formaltype = temp_md.formals.get(i).typeid;
							if(conforms(actualtype, formaltype) == false)
								reportError(filename, node.lineNo, "In call of method " + temp_md.name + ", type " + actualtype + " does not conform to declared type " + formaltype);			
							}
						}	
					node.type = temp_md.typeid;
					}
				else 
					reportError(filename, node.lineNo, "Static dispatch to undefined method " + node.name);
				}
			
			}
		else if(expr instanceof AST.dispatch){
			AST.dispatch node = (AST.dispatch)expr;
			AST.method temp_md = null;
			String actualtype = "";
			String formaltype = "";
			put_node(node.caller);
			for(AST.expression item : node.actuals)
				put_node(item);
		
			Basic_class tempc = classes_list.get(node.caller.type);
			if(tempc == null) {
				reportError(filename, node.lineNo, "Class " + node.caller.type + " is undefined.");
				node.type = "Object";
				}
			else {
				if(tempc.methodlist.containsKey(node.name)) {
					temp_md = tempc.methodlist.get(node.name);
					if(node.actuals.size() != temp_md.formals.size())
						reportError(filename, node.lineNo, "Method " + temp_md.name + " invoked with wrong number of arguments.");
					else {
						for(int i = 0; i < node.actuals.size(); ++i) {
							formaltype = temp_md.formals.get(i).typeid;
							actualtype = node.actuals.get(i).type;
							if(conforms(actualtype, formaltype) == false)
								reportError(filename, node.lineNo, "In call of method " + temp_md.name + ", type " + actualtype + " does not conform to declared type " + formaltype);			
						}
					}	
					node.type = temp_md.typeid;
				}
				else {
					reportError(filename, node.lineNo, "Dispatch to undefined method " + node.name);
					node.type = "Object";
					}
				}
			}
		else if(expr instanceof AST.cond){		 //if-else
																//predicate ifbody elsebody
			AST.cond node = (AST.cond)expr;
			put_node(node.predicate);
			if(node.predicate.type.equals("Bool") == false) {   
				reportError(filename, node.predicate.lineNo, "Predicate of 'if' does not have type Bool.");
			}
			put_node(node.ifbody);
			put_node(node.elsebody);
			
			if(classes_list.get(node.ifbody.type) == null && static_types.contains(node.ifbody.type) ) {   //static type
				reportError(filename, node.ifbody.lineNo, "If body does not conform to any static type.");
				}
			if(classes_list.get(node.elsebody.type) == null && static_types.contains(node.elsebody.type)) {
				reportError(filename, node.elsebody.lineNo, "ELse body does not conform to any static type.");
				}	
			node.type = parent_type(node.ifbody.type, node.elsebody.type); //join of ifbody type and elsebody type
			}
		else if(expr instanceof AST.loop){       	//loop predicate body
			AST.loop node = (AST.loop)expr;
			put_node(node.predicate);
			if(node.predicate.type.equals("Bool") == false) {
				reportError(filename, node.predicate.lineNo, "Loop condition does not have type Bool.");
				}
			put_node(node.body);
			if(classes_list.get(node.body.type) == null && static_types.contains(node.body.type)) { //static type
				reportError(filename, node.body.lineNo, "Loop body does not conform to any static type.");
				}
			node.type = "Object";
			}
		else if(expr instanceof AST.block){				//block expressionlist(l1)
			AST.block node = (AST.block) expr;
			for(AST.expression item : node.l1){
				put_node(item);
				if(classes_list.get(item.type) == null && static_types.contains(item.type))  //static type
					reportError(filename, item.lineNo, "Expression of the block does not conform to any static type.");
				}
				
			node.type = node.l1.get(node.l1.size() - 1).type; //block type is type of last expr
			}
		else if(expr instanceof AST.let){			//let name typeid value body
			AST.let node = (AST.let)expr;
			if(node.value.getClass() != AST.no_expr.class) {  //Every let expression must introduce at least one identifier
				put_node(node.value);
				if(conforms(node.value.type, node.typeid) == false){
					String ret = "Inferred type in " + node.value.type + " of initialization of " + node.name + " does not conform to the declared type " + node.typeid;
					reportError(filename, node.lineNo, ret);
					}		
				}
				scopeTable.enterScope(); //enterscope to hide the variables which might already be declared.
				scopeTable.insert(node.name, new AST.attr(node.name, node.typeid, node.value, node.lineNo));
				put_node(node.body);
				node.type = node.body.type; 
				scopeTable.exitScope();
				/*}
			else
				let.type = 	"_no_type";*/
			}
		else if(expr instanceof AST.typcase){			//case predicate branch
			AST.typcase node = (AST.typcase)expr;
			put_node(node.predicate);
			HashMap <String, Boolean> unique = new HashMap<String, Boolean> ();
				//takes all the branches of case to facilitate checking duplicate branches
			scopeTable.enterScope();
				//enterscope to hide the variables which might be globally declared.
			for(AST.branch item : node.branches) {
				if(classes_list.get(item.type) == null)    //undefined branch
					reportError(filename, item.lineNo, "Class " + item.type + " of case branch is undefined.");
				else if(unique.containsKey(item.type) == false){
					unique.put(item.type, true);
					scopeTable.insert(item.name, new AST.attr(item.name, item.type, item.value, item.lineNo));
					}
				else				//duplicate branch
					reportError(filename, item.lineNo, "Duplicate branch " + item.type + " in case statement.");
				
				put_node(item.value);
				node.type = parent_type(node.branches.get(0).value.type, item.value.type); //join of all branch types
				}
			scopeTable.exitScope();
			
			}
		else if(expr instanceof AST.new_){		//new object
			AST.new_ node = (AST.new_)expr;
			if(classes_list.get(node.typeid) == null) {
				reportError(filename, node.lineNo, "'new' used with undefined class " + node.typeid);
				node.type = "Object";
				}
			else
				node.type = node.typeid;
			}
		else if(expr instanceof AST.isvoid){		//isvoid
			AST.isvoid node = (AST.isvoid)expr;
			node.type = "Bool";
			}
		else if(expr instanceof AST.plus){		//arithmetic operator "+"
			AST.plus node = (AST.plus)expr;
			put_node(node.e1);						//expr1 + expr2
			put_node(node.e2);
			if(node.e1.type.equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments: " + node.e1.type + " + " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Int";
			}
		else if(expr instanceof AST.sub){		//arithmetic operator "-"
			AST.sub node = (AST.sub)expr;
			put_node(node.e1);
			put_node(node.e2);
			if(node.e1.type.equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments: " + node.e1.type + " - " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Int";
			}
		else if(expr instanceof AST.mul){			//arithmetic operator "*"
			AST.mul node = (AST.mul)expr;
			put_node(node.e1);
			put_node(node.e2);
			if(node.e1.type .equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments: " + node.e1.type + " * " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Int";
			}
		else if(expr instanceof AST.divide){		//arithmetic operator "/"
			AST.divide node = (AST.divide)expr;
			put_node(node.e1);
			put_node(node.e2);
			if(node.e1.type.equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments: " + node.e1.type + " / " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Int";
			}
		else if(expr instanceof AST.comp){				//not of expression
			AST.comp node = (AST.comp)expr;
			put_node(node.e1);
			if(node.e1.type.equals("Bool") == false){
				reportError(filename, node.lineNo, "Argument of 'not' has type " + node.e1.type + " instead of Bool.");
				node.type = "Object";
				}
			else
				node.type = "Bool";
			}
		else if(expr instanceof AST.lt){					//< operator
			AST.lt node = (AST.lt)expr;
			put_node(node.e1);
			put_node(node.e2);
			if(node.e1.type.equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments: " + node.e1.type + " < " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Bool";
			}
		else if(expr instanceof AST.leq){			//<= operator
			AST.leq node = (AST.leq)expr;
			put_node(node.e1);
			put_node(node.e2);
			if(node.e1.type.equals("Int") == false || node.e2.type.equals("Int") == false) {
				reportError(filename, node.lineNo, "non-Int arguments:" + node.e1.type + " <= " + node.e2.type);
				node.type = "Object";
				}
			else
				node.type = "Bool";
			}
		else if(expr instanceof AST.eq){					//equality operator
			AST.eq node = (AST.eq)expr;
			put_node(node.e1);
			put_node(node.e2);
			List <String> basic = Arrays.asList("String", "Int", "Bool");
			if(basic.contains(node.e1.type) || basic.contains(node.e2.type)) {
				if(node.e1.type.equals(node.e2.type) == false) {
					reportError(filename, node.lineNo, "Illegal comparison with a basic type.");
					node.type = "Object";
					return;
					}
				}
			node.type = "Bool";
			}
		else if(expr instanceof AST.neg){					// ~expression
			AST.neg node = (AST.neg)expr;
			put_node(node.e1);
			if(node.e1.type.equals("Int") == false){		//static type of expr is Int
				reportError(filename, node.lineNo, "Argument of '~' has type " + node.e1.type + " instead of Int");
				node.type = node.e1.type;
				}
			else	
				node.type = "Int";
			}
		else if(expr instanceof AST.object){					//IDENTIFIER
			AST.object node = (AST.object) expr;
			AST.attr node_attr = scopeTable.lookUpGlobal(node.name);
			if(node_attr == null) {
				reportError(filename, node.lineNo, "Undeclared identifier " + node.name);
				node.type = "Object";
				}
			else
				node.type = node_attr.typeid;
			}
		else if(expr instanceof AST.int_const){				//INT_CONST
			AST.int_const node = (AST.int_const)expr;
			node.type = "Int";
			}
		else if(expr instanceof AST.string_const){			//STRING_CONST
			AST.string_const node = (AST.string_const)expr;
			node.type = "String";
			}
		else if(expr instanceof AST.bool_const){          //BOOL_CONST
			AST.bool_const node = (AST.bool_const)expr;
			node.type = "Bool";
			}
	}
	
}
