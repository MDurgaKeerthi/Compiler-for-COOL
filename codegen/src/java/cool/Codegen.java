
package cool;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;


public class Codegen{
	
	ArrayList < ArrayList <Integer> > classGraph;
	ArrayList <String> allstr = new ArrayList <String>(); //all string constants stored
	ArrayList <Integer> str_len = new ArrayList <Integer>();//string length of the string constants
	HashMap <Integer, String> idxName;
	HashMap <String, AST.class_> classmap;//classname:AST.class_

	HashMap <String, Integer> strval = new HashMap <String, Integer> ();//attribute of string type and its corresponding boll value treu or false for printing
	HashMap <String, Integer> new_check = new HashMap <String, Integer> ();//attribute assigned to new or not
	HashMap <String, String> attributes = new HashMap <String, String> ();//attributes name and class_prsent
	HashMap<String,List<String>> cl_at= new HashMap<String,List<String>>();//classname:[attributes names]
	HashMap<String,List<String>> cl_me= new HashMap<String,List<String>>(); //classname:[method names] 
	HashMap <String, String> formals_list ;//formals and method
   HashMap<String,List<String>> cl_ob= new HashMap<String,List<String>>(); 
	HashMap<String,List<Integer>> cl_ob_check= new HashMap<String,List<Integer>>();     

	HashMap<String,HashMap<String,List<String>>> cl_me_for= new HashMap<String,HashMap<String,List<String>>>();  //classname:methodname:[formals names]
	HashMap<String,HashMap<String,List<String>>> cl_me_for_ty= new HashMap<String,HashMap<String,List<String>>>(); //classname:methodname:[formals type]

	HashMap <String, String> meth_cl = new HashMap <String, String> ();//methods and class_present
	HashMap <String, String> meth_type = new HashMap <String, String> ();//classname+methname :type
	HashMap <String, String> obj_varco ; 
	HashMap <String, List<String>> cl_at_ty = new HashMap <String, List<String>>();//methods and class_present
	String presentcl; //present classname
	String presentme;//present method name
	int varcount=0; //present varcount 
	int concount = 0;
	int strCount = 0;//present stringconst count
	boolean subexpr=false;//subexpr for dispacth
	int denom = 1;
	
	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
       
        // go through all classes. For each class make object structures, then include virtual table for a class.
		CodegenInit(out);
        
      graph_traversal(program.classes); //generating all hash maps
      //main lable generation
		if(cl_at_ty.get("Main").size() !=0){
			out.println("define i32 @main() #0 {\nentry:\n  %retval = alloca i32, align 4\n %m = alloca %class.Main, align 4\n  store i32 0, i32* %retval, align 4\n  call void @Maingvars(%class.Main* %m) #4\n  %call = call "+meth_type.get("Mainmain")+" @Mainmain(%class.Main* %m)\n  ret i32 0\n}\n");
			}
		else{
			out.println("define i32 @main() #0 {\nentry:\n  %retval = alloca i32, align 4\n %m = alloca %class.Main, align 4\n  store i32 0, i32* %retval, align 4\n  call "+meth_type.get("Mainmain")+" @Mainmain(%class.Main* %m)\n  ret i32 0\n}\n");
			}
      //attributes label generation
      for(AST.class_ e : program.classes) { 
			obj_varco =new HashMap <String, String> ();  
			strval = new HashMap <String, Integer> ();
			new_check = new HashMap <String, Integer> () ;
			//System.out.println(e.name);
			String ar = "i32";
			
			
			if(cl_at_ty.get(e.name).size()==0){
				out.println("%class."+e.name+" = type {"+ar+"}");
				out.println("define void @"+e.name+"gvars(%class."+e.name+"* %this) unnamed_addr {\nentry:\n  %this.addr = alloca %class."+e.name+"*, align 8\n  store %class."+e.name+"* %this, %class."+e.name+"** %this.addr, align 8\n  %this1 = load %class."+e.name+"*, %class."+e.name+"** %this.addr, align 8\n  ret void\n}\n");
            }

			else{
				ar = cl_at_ty.get(e.name).get(0);
				for(int a = 1; a<cl_at.get(e.name).size(); a++)
					ar=ar+","+cl_at_ty.get(e.name).get(a);
				out.println("%class."+e.name+" = type {"+ar+"}");
				List<AST.feature> theFeatures = new ArrayList<AST.feature>();
		     	theFeatures = e.features;
				presentcl = e.name;
				varcount = 0;
				out.println("define void @"+e.name+"gvars(%class."+e.name+"* %this) unnamed_addr {\nentry:\n  %this.addr = alloca %class."+e.name+"*, align 8\n  store %class."+e.name+"* %this, %class."+e.name+"** %this.addr, align 8\n  %this1 = load %class."+e.name+"*, %class."+e.name+"** %this.addr, align 8");

							//inherited  class attributes bitcasting
				AST.class_ tempe = e;
				//System.out.println(tempe.parent);
				while(!(tempe.parent.equals("Object") || tempe.parent.equals("IO") )){ 
					AST.class_ par = classmap.get(tempe.parent);
					out.println("  %"+varcount+" = bitcast %class."+e.name+"* %this1 to %class."+par.name+"*\n  call void @"+par.name+"gvars(%class."+par.name+"* %"+varcount+")");
					tempe = par;
					}
				
				int atcounter = 0;
				//assingning the values i=to attributes if present
				cl_ob_check.put(e.name, new ArrayList<Integer>());
				for(int i = 0; i < theFeatures.size(); ++i){	
					AST.feature ftr = new AST.feature();
				  	ftr = theFeatures.get(i);
				  	if(ftr.getClass() == AST.attr.class){     
				   	AST.expression expr = new AST.expression();
				   	AST.attr temp = (AST.attr)ftr;
				   	expr = temp.value;
				        //getting attribute element pointer
			     		if(temp.value.getClass() != AST.no_expr.class){
			     			out.println("  %"+varcount+" = getelementptr inbounds %class."+e.name+", %class."+e.name+"* %this1, i32 0, i32 "+atcounter);
							int v=varcount;
							varcount++;
			     			//System.out.println("vale...."+temp.value.getClass());
							
							 /*store i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str, i32 0, i32 0), i8** %s, align 8*/
			     			ProcessStr(expr,out); //process the assign expression
							if(expr.getClass()==AST.string_const.class){  //if the string const assign
								//out.println("%"+varcount+" =  alloca i8*, align 4");
								//varcount++;
								int s_c=strCount-1;
				 				int len=str_len.get(s_c);
								out.println("  store i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0), i8** %"+v+", align 4");//store the string value to the pointer
								strval.put(temp.name,0);
								//System.out.println(strval.get(temp.name));
								}
							else{
								int index=cl_at.get(e.name).indexOf(temp.name);
								out.println("  store "+cl_at_ty.get(e.name).get(index)+" %"+(varcount-1)+", "+cl_at_ty.get(e.name).get(index)+"* %"+v+", align 4");

								if(!basetypes.contains(temp.typeid)){
						   		cl_ob_check.get(e.name).add(1);
									new_check.put(temp.name,1);
									}
								}
			     			}
			     		
			     		if(expr.getClass() == AST.string_const.class && temp.value.getClass() == AST.no_expr.class)
			     				strval.put(temp.name,0);
			     		atcounter++;	
				     
						if(!basetypes.contains(temp.typeid) && temp.value.getClass() == AST.no_expr.class){
							new_check.put(temp.name,0);
							cl_ob_check.get(e.name).add(0);
							}
						}	
					}
				out.println("  ret void\n}\n"); //return void for the attributes label
				}
			//generating method labels	
			List<AST.feature> theFeatures = new ArrayList<AST.feature>();
        	theFeatures = e.features;
			presentcl = e.name;
			varcount = 0;
			for(int i = 0; i < theFeatures.size(); ++i){	
		   	AST.feature ftr = new AST.feature();
		     	ftr = theFeatures.get(i);
	       	
	       	if(ftr.getClass() == AST.method.class){
	       		concount = 0;
		      	AST.method temp = (AST.method)ftr;
               String type =temp.typeid;
					formals_list = new HashMap <String, String> ();
					String str12="";
					String straddr="  ";
					String strstore="  ";
					varcount=0;
					if(type.equals("IO"))   //if IO return type then define label with void return type 
					   out.print("define void @"+e.name+temp.name+"(%class."+e.name+"* %this ");
					else
					   out.print("define "+meth_type.get(presentcl+temp.name)+" @"+e.name+temp.name+"(%class."+e.name+"* %this ");//else with the method return type
				       
					for(int k=0;k<temp.formals.size();k++){
						String s=temp.formals.get(k).name;
						String typ_for;
						
						if(temp.formals.get(k).typeid.equals("Bool"))
							typ_for="i32";
						else if(temp.formals.get(k).typeid.equals("String"))
							typ_for="i8*";
						else
							typ_for="i32";
						formals_list.put(s,typ_for);//generating formals list formalname:formal type
                  		str12=str12+", "+typ_for+" %"+s;
					   straddr=straddr+"%"+s+".addr = alloca "+typ_for+", align 4\n  "; //assign address for the formals
					   strstore=strstore+"store "+typ_for+" %"+s+", "+typ_for+"* %"+s+".addr, align 4\n  ";//store the formal value to its address
		            }
					
						//System.out.println("allc"+formals_list);
					if(temp.formals.size()==0 )
					     out.print(") { \n");
					else
					    out.print(str12 + " ) { \n");
					out.println("entry: ");//method definition entry lable
					out.println("  %this.addr = alloca %class."+e.name+"*, align 8\n"+straddr+"store %class."+e.name+"* %this, %class."+e.name+"** %this.addr, align 8\n"+strstore+"%this1 = load %class."+e.name+"*, %class."+e.name+"** %this.addr, align 8");

		        	AST.expression expr = new AST.expression();
	        		expr = temp.body;
	        		ProcessStr(expr,out);//process the method body 

					//return the method 
					if(type.equals("IO"))
					   out.println("  ret void");
					else
					   out.println("  ret "+ meth_type.get(presentcl+temp.name)+" %"+(varcount-1));
					if(concount>0){   
						out.println("exitpro:\n  %"+varcount+" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.divide, i32 0, i32 0))\n  call void @exit(i32 1)\n br label %continuepro1\n}\n");
						varcount++;   
						}
					else out.println("\n}\n");
		        	}
				}
			}
		//all the strings constants are generated at last 
		String eachstr, eachstr1;
		int ind,len = allstr.size();
		for (int counter = 0; counter < len; counter++) { 
		  eachstr = allstr.get(counter);			      
		  /*ind = eachstr.indexOf('\n');
		  if(ind>=0){
			eachstr1 = eachstr.substring(0,ind-1)+"\\0A"+eachstr.substring(ind+1);
		  }
		  else
			eachstr1 = eachstr;
		  string_len[counter]=eachstr1.length();*/
        out.println("@.str."+counter+" = private unnamed_addr constant ["+str_len.get(counter)+" x i8] c\""+eachstr+"\", align 1"); 		
        }
		out.println("@.str.int = private unnamed_addr constant [3 x i8] c\""+"%d"+"\\00\", align 1");
		out.println("@.str.str = private unnamed_addr constant [3 x i8] c\""+"%s"+"\\00\", align 1");
		out.println("@.str.static = private unnamed_addr constant [25 x i8] c\""+"Static Dispatch on void\\0A\\00\", align 1");
		out.println("@.str.divide = private unnamed_addr constant [26 x i8] c\""+"Divide by zero undefined\\0A\\00\", align 1");
		} 
		
		
	List<String> basetypes = Arrays.asList("Object","String","Int","Bool","IO");
	List<String> io_methods = Arrays.asList("out_string","out_int","in_int","in_string");
	private String parent_type(String clas, String atr,int offset) {  //inherited classname of the attribute or method being called
		//System.out.println(clas+" "+atr+" "+offset);
		AST.class_ curr_cl =classmap.get(clas);
		//System.out.println(attributes.get(atr));
		if(offset==1 && curr_cl.parent.equals(attributes.get(atr)))		
			return curr_cl.parent;
		else if(offset==2 && curr_cl.parent.equals(meth_cl.get(atr)))		
			return curr_cl.parent;
		else
			return parent_type( curr_cl.parent, atr,offset);
		}

	private String parent_type1(String clas, String atr,int offset) { //inherited classname of the attribute or method being called offset 1 for attributes 2 for methods
		//System.out.println(clas+" "+atr+" "+offset);
		AST.class_ curr_cl = classmap.get(clas);
		//System.out.println(attributes.get(atr));
	 	if(offset==1 && cl_at.get(curr_cl.parent).contains(atr))	//if curr class conatins the attribute name the return curr classname	
			return curr_cl.parent;
		else if(offset==2 && cl_me.get(curr_cl.parent).contains(atr))	//if curr class conatins the method name the return curr classname	
			return curr_cl.parent;
		else
			return parent_type1( curr_cl.parent, atr,offset);//else find the attribute name in inherited class
		}

//Ir code generation for easch ASt nod etype
	private void ProcessStr(AST.expression expr_str, PrintWriter out){
		
		// assign
		if(expr_str.getClass() == AST.assign.class){
		   //System.out.println("assign");
			AST.assign str = (AST.assign)expr_str;
			AST.expression exp = str.e1;
			String name = str.name;
		   int glob_var = 0;
		  
			if(formals_list.containsKey(name)){  //if the assignement is to formal of the method
				if(exp.getClass()==AST.int_const.class||exp.getClass()==AST.bool_const.class){
					//out.print("  store i32 " );
					ProcessStr(exp,out);//process the assign expr and assign to the formalname.addr label
		      	out.print("  store i32 %"+(varcount-1)+", i32* %"+name+ ".addr, align 4\n");
					}
				else if(exp.getClass() == AST.string_const.class){
					int s_c = strCount-1;
					int len = str_len.get(s_c); //if string-const is being assigned the do the below one
					out.println("  store i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0), i8** %"+name+glob_var+", align 4");
					strval.put(name,0);
					}
				else{//assign expr is either dispatch or static dispatch or airthmetic assign
					subexpr = true;
					ProcessStr(exp,out);
					subexpr = false;
					int var_c = varcount-1;
		         out.print("  store "+formals_list.get(name)+" %"+var_c+", "+formals_list.get(name)+"* %"+name+ ".addr, align 4\n");//store the value to the formal.addr
					}
				}

			else { //if the assignment is to attribute of either present class or inherited class
				int ind1;
				String class_na;
            if(cl_at.get(presentcl).contains(name)){
            		//if presnet class get the attribute elementpointer and store the value to the pointer address
					//System.out.println("here if");
					class_na = presentcl;
					ind1 = cl_at.get(presentcl).indexOf(name);
					out.println("  %"+name+varcount+" = getelementptr inbounds %class."+presentcl+", %class."+presentcl+"* %this1, i32 0, i32 "+ind1);
					obj_varco.put(name, name+varcount);
					glob_var = varcount;
					}
				else {
					//if not in the present class get the inherited class name and bitcast the present class pointer to the inherited calss pointer
				   //System.out.println("here else");
					String inher_cl = parent_type1(presentcl,name,1);
					class_na = inher_cl;
					//System.out.println(inher_cl);
					ind1 = cl_at.get(inher_cl).indexOf(name);
					out.println("  %"+varcount +" = bitcast %class."+presentcl+"* %this1 to %class."+inher_cl+"*");//bitcasting form present class to the inherited class
					varcount++;
					out.println("  %"+name+varcount+" = getelementptr inbounds %class."+inher_cl+", %class."+inher_cl+"* %"+(varcount-1)+", i32 0, i32 "+ind1);//get the sttribute element pointer of the inherited class
				   obj_varco.put(name, name+varcount);
					glob_var = varcount;
		         }          
 			
			ProcessStr(exp,out);//process the assign expression

			if(exp.getClass() == AST.string_const.class){
				int s_c = strCount-1;
				int len = str_len.get(s_c);//if the assignment is string_const
				out.println("  store i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0), i8** %"+name+glob_var+", align 4");
				strval.put(name,0);
				}
			//else if(cl_at_ty)
			else{	
				//System.out.println(ind1);
				//if the assignment is not string-const
				String typ1 = cl_at_ty.get(class_na).get(ind1);	
				int var_c = varcount-1;
		      out.print("  store "+typ1+" %"+var_c+", "+typ1+"* %"+name+glob_var+ ", align 4\n");
				}
			
			if(exp.getClass()==AST.dispatch.class){
				AST.dispatch node = (AST.dispatch)exp;
				if(node.name.equals("in_string"))
					strval.put(name,1);
				}
				
			if(exp.getClass()==AST.new_.class){
				new_check.put(name,1);
				cl_ob_check.get(class_na).add(1);
				}
			}
		}

		// static dispatch
		else if(expr_str.getClass() == AST.static_dispatch.class){
		   //System.out.println("static_dispatch");
			AST.static_dispatch str = (AST.static_dispatch)expr_str;
			List<AST.expression> expression_listsd = new ArrayList<AST.expression>();
         expression_listsd = str.actuals;
			
			//AST.new_ node=(AST.new_)str.caller;
			String classname = str.typeid; //name of caller (new c)
			/*System.out.println(str.name);
			System.out.println(str.caller);
			System.out.println(str.actuals);
			System.out.println("type---"+str.typeid);
			//AST.method func=classmap.get()
         //System.out.println(str.typeid);
			//System.out.println("new check "+new_check);*/
			
			if(str.caller.getClass() == AST.object.class){  //if the caller type is ASt.object type
				AST.object cal = (AST.object)str.caller;
				if(new_check.containsKey(cal.name) && new_check.get(cal.name)==0){ //if new class is not assigned to attribute then run tme error dispatch on void
					//if(str.caller.getClass()!=AST.new_.class)
					out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str.static, i32 0, i32 0))\n  call void @exit(i32 1)");
					varcount++;
					}
				}

			//get the classname of the func being claaed either in the class on whcih it is called or in the inherited class
			if( !classname.equals("IO") && !(cl_me.get(str.typeid).contains(str.name))){
				//if(!(meth_cl.get(str.name).equals(str.typeid)))
		   	classname = parent_type1(str.typeid,str.name,2);
		   	}

			if(!classname.equals("IO") && !presentcl.equals(classname)){ //if present in inherted class the bitcast the class pointer to the inherited class pointer
		   	out.println("  %bit"+varcount+" = bitcast %class."+presentcl+"* %this1 to %class."+classname+"*");
		  		out.println("  call void @"+classname+"gvars(%class."+classname+"* %bit"+varcount+")");//call the class attributes label for attribute initialization
				}
		   	
			String args = "(%class."+classname+"* %bit"+varcount;

			if(str.name.equals("out_string")){  //if function is outstring
				expr_str = expression_listsd.get(0);
				ProcessStr(expr_str,out);
				
				if(expr_str.getClass()==AST.string_const.class){ //if print string_const
					int s_c = strCount-1;
				 	int len = str_len.get(s_c);
				 	out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0))");
					varcount++;
					}
				else {  //out_string on object
					AST.object obname = (AST.object)expr_str;
					if(strval.get(obname.name)==1){				
						out.println("  %"+varcount+"= alloca i8*, align 4");
						varcount++;
						out.println("  store i8* %"+(varcount-2)+", i8** %"+(varcount-1)+",align 4");
						out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8** %"+(varcount-1)+")");
						varcount++;
						}
					else{
						out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8* %"+(varcount-1)+")");
						varcount++;
						}
					}
				}
			else if(str.name.equals("out_int")){ //out_int same case for int_const and object
				expr_str = expression_listsd.get(0);
				ProcessStr(expr_str,out);
				out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.int, i32 0, i32 0), i32 %"+(varcount-1)+")");
				varcount++;
				}
			else if(str.name.equals("in_int")){//int_int
				out.println("  %"+varcount+"= alloca i32, align 4");
				varcount++;
				out.println("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.int, i32 0, i32 0), i32* %"+(varcount-1)+")");
				varcount++;
				out.println("  %"+varcount+"=load i32, i32* %"+(varcount-2)+", align 4");
				varcount++;
				}
			else if(str.name.equals("in_string")){ //in_string
				//System.out.println("here in instring");
				out.println("  %"+varcount+"= alloca i8*, align 4");
				varcount++;
				out.println("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8** %"+(varcount-1)+")");
				varcount++;
				out.println("  %"+varcount+"=load i8*, i8** %"+(varcount-2)+", align 4");
				varcount++;
				}

  		 else{
             //if the method names are not the method names of IO class
             // process each of the actuals of the method being called
         for(int i = 0; i < expression_listsd.size(); i++){
				expr_str = expression_listsd.get(i);
				ProcessStr(expr_str,out);
				if(expr_str.getClass()==AST.string_const.class){
					int s_c = strCount-1;
			 		int len = str_len.get(s_c);
			 		args += (", "+cl_me_for_ty.get(classname).get(str.name).get(i)+"getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0)");
					//out.println("  %"+varcount+"= getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0), align 4");
					//varcount++;
				   }
				else
					args+=(", "+cl_me_for_ty.get(classname).get(str.name).get(i)+" %"+(varcount-1));
				}
			if(subexpr)
				out.print("  %"+varcount+" =");
			if(!meth_type.get(classname+str.name).equals("void"))
         	varcount++;		
			out.println("  call "+meth_type.get(classname+str.name)+ " @"+classname+str.name+args+")");  //call the method 
			//varcount++;
			subexpr=false;
			
				 
		    //out.println("call "+i32+ "@_"+ZN1A3funEi+"(%class."+A+"* %1, "+i32 %add4)");
			}
		}

		// dispatch
		else if(expr_str.getClass() == AST.dispatch.class){
		   //System.out.println("dispatch");
			AST.dispatch str = (AST.dispatch)expr_str;   
			List<AST.expression> expression_listsd = new ArrayList<AST.expression>();
         expression_listsd = str.actuals;
			boolean self = false;
			String classname = "";

			if(str.name.equals("length")){ //string.length implementation
				AST.object obj = (AST.object) str.caller;
				 int ind = cl_at.get(presentcl).indexOf(obj.name);
				if(cl_at_ty.get(presentcl).get(ind).equals("i8*")){
					ProcessStr(obj,out);
					out.println(" %"+varcount+" = call i32 @strlen(i8* %"+(varcount-1)+") ");
					varcount++;
					}	
				}
			else if(str.name.equals("concat"))	{ //string.concat implementation
				 AST.object obj = (AST.object) str.caller;
				 //System.out.println("in concat"+str.caller);
				 //System.out.println("in concat"+obj.name);
				 int ind = cl_at.get(presentcl).indexOf(obj.name);
				 if(cl_at_ty.get(presentcl).get(ind).equals("i8*")){
				 	ProcessStr(obj,out);
					int v3 = varcount-1;
					AST.expression expr_str1 = expression_listsd.get(0);
					ProcessStr(expr_str1,out);
					int s_c = strCount-1;
					int len = str_len.get(s_c);
					out.println(" %"+varcount+" = call i8* @strcat(i8* %"+v3+",i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0)) ");
					varcount++;
					out.println("store i8* %"+(varcount-1)+", i8** %"+obj.name+(v3+1)+" , align 4");
					strval.put(obj.name,1);
					}	
				}
			else {
				if (str.caller.getClass()==AST.new_.class){
					AST.new_ node = (AST.new_)str.caller; //object name//if new_class then bitcast this calss pointer to that class pointer 
					classname = node.typeid;
					if(!classname.equals("IO"))
						out.println("  %bit1"+varcount+" = bitcast %class."+presentcl+"* %this1 to %class."+classname+"*");
					}
				else {
					AST.object obj = (AST.object) str.caller;
					//System.out.println("name .."+obj.name);
				
		         if( obj.name.equals("self" )){//if the method form the present class or inherited class is called 
		            if(cl_me.get(presentcl).contains(str.name)){
				      	classname = presentcl; //if present class
							self = true;
							}
				      else {
				      	if(!io_methods.contains(str.name)){ //if inherited class the bitcast
				      		classname=parent_type1(presentcl,str.name,2);
				      		out.println("  %bit1"+varcount+" = bitcast %class."+presentcl+"* %this1 to %class."+classname+"*");
				      		}
							}	
		          	}

		         else {
				      if(cl_at.get(presentcl).equals(obj.name))
				      	classname = presentcl;
				      else 
				      	classname=parent_type1(presentcl,obj.name,1);
				      out.println("  %bit1"+varcount+" = bitcast %class."+presentcl+"* %this1 to %class."+classname+"*");
						}	
					}
			
				/*System.out.println("dispatc.."+str.name);
				System.out.println("dispatc.."+str.caller);
				System.out.println("dispatc.."+str.actuals);*/
			
				String args;
				//AST.method func=classmap.get()
				if(self)
					args = "(%class."+classname+"* %this1";
				else
					args = "(%class."+classname+"* %bit1"+varcount;
		       //IO class methods implementation same as static dispatch
				if(str.name.equals("out_string")){
					expr_str = expression_listsd.get(0);
					ProcessStr(expr_str,out);
					
					if(expr_str.getClass()==AST.string_const.class){
						int s_c = strCount-1;
					 	int len = str_len.get(s_c);
					 	out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds (["+len+" x i8], ["+len+" x i8]* @.str."+s_c+", i32 0, i32 0))");
						varcount++;
						}
					else {
						AST.object obname=(AST.object)expr_str;
						if(!strval.containsKey(obname.name)){
							out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8* %"+(varcount-1)+")");
							varcount++;
							}
						else{ 
							if(strval.get(obname.name)==1){				
								out.println("  %"+varcount+"= alloca i8*, align 4");
								varcount++;
								out.println("  store i8* %"+(varcount-2)+", i8** %"+(varcount-1)+",align 4");
								out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8** %"+(varcount-1)+")");
								varcount++;
								}
							else{
								out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8* %"+(varcount-1)+")");
								varcount++;
								}
							}
						}
					}
				else if(str.name.equals("out_int")){
					expr_str = expression_listsd.get(0);
					ProcessStr(expr_str,out);
					out.println("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.int, i32 0, i32 0), i32 %"+(varcount-1)+")");
					varcount++;
					}
				else if(str.name.equals("in_int")){
					out.println("  %"+varcount+"= alloca i32, align 4");
					varcount++;
					out.println("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.int, i32 0, i32 0), i32* %"+(varcount-1)+")");
					varcount++;
					out.println("  %"+varcount+"=load i32, i32* %"+(varcount-2)+", align 4");
					varcount++;
					}
				else if(str.name.equals("in_string")){
					//System.out.println("here in instring");
					out.println("  %"+varcount+"= alloca i8*, align 4");
					varcount++;
					out.println("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.str, i32 0, i32 0), i8** %"+(varcount-1)+")");
					varcount++;
					out.println("  %"+varcount+"=load i8*, i8** %"+(varcount-2)+", align 4");
					varcount++;
					}
				else{//if not the IO class the process each of the actual of the dispatch
					for(int i = 0; i < expression_listsd.size(); i++){				
						expr_str = expression_listsd.get(i);
						ProcessStr(expr_str,out);
						//if(expr_str.getClass()==AST.int_const.class)
						args+=(", i32 %"+(varcount-1));
						}
			 
					if(subexpr)
						out.print("  %"+varcount+" =");
					if(!meth_type.get(classname+str.name).equals("void"))
         			varcount++;		
					out.println("  call "+meth_type.get(classname+str.name)+ " @"+classname+str.name+args+")");//call the method
					subexpr=false;
					}
 				}
			}

		// if-then-else
		else if(expr_str.getClass() == AST.cond.class){
		   //
			AST.cond str = (AST.cond)expr_str;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			ProcessStr(e1,out); //process if predicate
			//System.out.println("chss"+str.predicate.getClass());
			if(str.predicate.getClass()==AST.object.class || str.predicate.getClass()==AST.bool_const.class){ //if predicate is of type object or bool_const
				out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
				varcount++;
				}
			if(str.predicate.getClass()==AST.dispatch.class ){ //if the predicate type is dispatch 
				AST.dispatch verytemp = (AST.dispatch)str.predicate;
				if(meth_type.get(presentcl+verytemp.name).equals("i32")){
					out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
					varcount++;
					}
				}	
			int v = varcount;
			out.println("  br i1 %"+(v-1)+", label %if.then"+v+", label %if.else"+v+"\n");
			out.println("if.then"+v+":");
			ProcessStr(e2,out); //process the if body
			out.println("  br label %if.end"+v+"\nif.else"+v+":");
			ProcessStr(e3,out); //process else body
			out.println("  br label %if.end"+v+"\n");
			
			if(e2.getClass()==AST.int_const.class || e2.getClass()==AST.bool_const.class){ //if body is int_const/bool_const type then return
				out.println("if.end"+v+":");
				ProcessStr(e2,out);
				//out.println("  ret i32 %"+(varcount-1));
				//varcount++;
				}
			else if(e3.getClass()==AST.int_const.class || e3.getClass()==AST.bool_const.class){ //else body is int_const/bool_const type then return
				out.println("if.end"+v+":");
				ProcessStr(e3,out);
				//out.println("  ret i32 %"+(varcount-1));
				//varcount++;
				}	
			else {
				out.println("if.end"+v+":"); //start end if label and rreturn to the next step
				}
			
			}
			  /*  %cmp = icmp eq i32 %0, 10
				  br i1 %cmp, label %if.then, label %if.else

				if.then:                                          ; preds = %entry
				  %call = call dereferenceable(272) %"class.std::basic_ostream"* @_ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc(%"class.std::basic_ostream"* dereferenceable(272) @_ZSt4cout, i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str, i32 0, i32 0))
				  br label %if.end

				if.else:                                          ; preds = %entry
				  %call3 = call dereferenceable(272) %"class.std::basic_ostream"* @_ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc(%"class.std::basic_ostream"* dereferenceable(272) @_ZSt4cout, i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.1, i32 0, i32 0))
				  br label %if.end

				if.end:                                           ; preds = %if.else, %if.then
				  %ans4 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 0
				  %1 = load i32, i32* %ans4, align 4
				  ret i32 %1
				}*/
		

		// block
		else if(expr_str.getClass() == AST.block.class){
			//System.out.println("block");
			//process each step of the block expr
			AST.block str = (AST.block)expr_str;
			List<AST.expression> listExp = new ArrayList<AST.expression>();
			listExp = str.l1;
			for(int i = 0; i < listExp.size(); ++i){
	        	AST.expression e2 = new AST.expression();
	        	e2 = listExp.get(i);
	        	//System.out.println(e2);         
				ProcessStr(e2,out);
	         }
			}

		// while loop
		else if(expr_str.getClass() == AST.loop.class){
		   //System.out.println("while");
			AST.loop str = (AST.loop)expr_str;
			int v = varcount-1;
			out.println("  br label %while.cond"+v+"\n\nwhile.cond"+v+":"); //branch on while condition
			
			if(str.predicate.getClass()==AST.cond.class){
				//System.out.println("while--cond"); //if while has if else in its body which is same as while .predicate having cond type
				AST.cond ifstr = (AST.cond)str.predicate;
				AST.expression ife1 = ifstr.predicate;
				AST.expression ife2 = ifstr.ifbody;
				AST.expression ife3 = ifstr.elsebody;
				ProcessStr(ife1,out);
				if(ifstr.predicate.getClass()==AST.object.class){
					out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
					varcount++;
					}
				int vin = varcount;
				out.println("  br i1 %"+(vin-1)+", label %if.then"+vin+", label %if.else"+vin+"\n");
				out.println("if.then"+vin+":"); //start if body
				ProcessStr(ife2,out); //process if body
				out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
				varcount++;
  				out.println("  br i1 %"+(varcount-1)+", label %while.body"+v+", label %while.end"+v+"\n");
  				
				out.println("  if.else"+vin+":");//start else body 
				ProcessStr(ife3,out); //process else body
				out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
				varcount++;
  				out.println("  br i1 %"+(varcount-1)+", label %while.body"+v+", label %while.end"+v+"\n");//if true goto while-body else while-end
  				//varcount++;
				}
			else{
				ProcessStr(str.predicate,out); //normal while loop with prediacte as bool value
				if(str.predicate.getClass()==AST.object.class){
					out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+" , 1");
					varcount++;
					}
				out.println("  br i1 %"+(varcount-1)+", label %while.body"+v+", label %while.end"+v+"\n");
				}
		   out.println("\nwhile.body"+v+": ");		
			ProcessStr(str.body,out);
			out.println("  br label %while.cond"+v+"\n\nwhile.end"+v+":");
			
			/*br i1 %cmp, label %while.body, label %while.end*/
			 /*br label %while.cond*/
			}

		// comparision operator (less than jaisa)
		else if(expr_str.getClass() == AST.eq.class){
			AST.eq str = (AST.eq)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v1 = varcount-1;
			ProcessStr(str.e2, out);//process expr 2
			out.println("  %"+varcount+" = icmp eq i32 %"+v1+" , %"+(varcount-1));//compare expr1 and expr 2
			varcount++;	
			}

		else if(expr_str.getClass() == AST.leq.class){
			AST.leq str = (AST.leq)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v1 = varcount-1;
			ProcessStr(str.e2, out);//process expr 2
			out.println("  %"+varcount+" = icmp sle i32 %"+v1+" , %"+(varcount-1));	//compare expr1 and expr 2
			varcount++;	
			}

		else if(expr_str.getClass() == AST.lt.class){
			AST.lt str = (AST.lt)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v1 = varcount-1;
			ProcessStr(str.e2, out);//process expr 2
			out.println("  %"+varcount+" = icmp slt i32 %"+v1+" , %"+(varcount-1));	//compare expr1 and expr 2
			varcount++;	
			}
		
		

		else if(expr_str.getClass() == AST.plus.class){
			/* %0 = load i32, i32* %l, align 4
			  %1 = load i32, i32* %m, align 4
			  %add = add nsw i32 %0, %1*/

			AST.plus str = (AST.plus)expr_str;
			//System.out.println("plus "+str.e1);
			ProcessStr(str.e1, out);//process expr 1
			int v = varcount;	
			ProcessStr(str.e2, out);//process expr 2	
			out.println("  %"+varcount+" = add nsw i32 %"+(v-1)+", %"+(varcount-1));//add expr1 and expr 2
			varcount++;
			}

		else if(expr_str.getClass() == AST.sub.class){
		   AST.sub str = (AST.sub)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v = varcount;	
			ProcessStr(str.e2, out);//process expr 2	
			out.println("  %"+varcount+" = sub nsw i32 %"+(v-1)+", %"+(varcount-1));//subtract expr1 and expr 2
			varcount++;
			}

		else if(expr_str.getClass() == AST.mul.class){
		   AST.mul str = (AST.mul)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v = varcount;	
			ProcessStr(str.e2, out);//process expr 2		
			out.println("  %"+varcount+" = mul nsw i32 %"+(v-1)+", %"+(varcount-1));//multiply expr1 and expr 2
			varcount++;
			}

		else if(expr_str.getClass() == AST.divide.class){
		   AST.divide str = (AST.divide)expr_str;
			ProcessStr(str.e1, out);//process expr 1
			int v = varcount;	
			ProcessStr(str.e2, out);//process expr 2	
			concount++;
			out.println("  %"+varcount+" = icmp eq i32 %"+(varcount-1)+", 0\n  br i1 %"+varcount+", label %exitpro, label %continuepro"+concount);//check if expr2 is zero
			varcount++;		
			out.println("continuepro"+concount+":");
			out.println("  %"+varcount+" = sdiv i32 %"+(v-1)+", %"+(varcount-2));//if expr2 is not zero divide expr1 nad expr 2
			varcount++;
			
			}


		else if(expr_str.getClass()==AST.new_.class){
			//System.out.println("in new");
			//if new class is assigned 
			AST.new_ node = (AST.new_) expr_str;
			out.println("  %"+varcount+" = call i8* @_Znwm(i64 "+size_class(node.typeid)+" )");
			varcount++;
			out.println("  %"+varcount+" = bitcast i8* %"+(varcount-1)+" to %class."+node.typeid+"*\n  call void @"+node.typeid+"gvars(%class."+node.typeid+"* %"+varcount+" )");//bit cast to that class pointer and call that class attribute label for attribute initialization
			varcount+=1;
           
			 /*%call = call i8* @_Znwm(i64 4) #7
			  %2 = bitcast i8* %call to %class.A*
			  call void @_ZN1AC2Ev(%class.A* %2) #2
			  store %class.A* %2, %class.A** %a, align 8*/

			}

		else if(expr_str.getClass() == AST.int_const.class){//int_const
		   //System.out.println("int_const");
			AST.int_const str = (AST.int_const)expr_str;
			out.println("  %"+varcount+" = alloca i32, align 4\n  store i32 "+str.value+", i32* %"+varcount+", align 4");//alloicte new memoery store the int_const to that memory and load to a new register label
			varcount++;
			out.println("  %"+varcount+" = load i32, i32* %"+(varcount-1)+", align 4");
			varcount++;
			denom = str.value;
			}

		else if(expr_str.getClass() == AST.comp.class)	{  //not expr
			AST.comp str = (AST.comp)expr_str;//compliment of an expression
			ProcessStr(str.e1, out);	
			out.println("  %"+varcount+" = xor i32 %"+(varcount-1)+", 1");
			varcount++;
			
		  /*%1 = load i32, i32* %c, align 4
			  %tobool = icmp ne i32 %1, 0
			  br i1 %tobool, label %if.end, label %if.then*/
	 		}
	
		else if(expr_str.getClass()==AST.neg.class){
			AST.neg node = (AST.neg)expr_str; //negation of an int value if i give -1
			ProcessStr(node.e1, out);
			out.println("%"+varcount+" = xor i32 %"+(varcount-1)+", -1");
			varcount++;
			/*%0 = load i32, i32* %l, align 4
		  %neg = xor i32 %0, -1
		  store i32 %neg, i32* %n, align 4*/
			}
	
		else if(expr_str.getClass() == AST.isvoid.class)	{
			AST.isvoid str = (AST.isvoid)expr_str;
			//System.out.println("isvaoid"+str.e1.getClass() );
			//ProcessStr(str.e1, out);	
			if(str.e1.getClass() == AST.object.class){
				AST.object cal=(AST.object)str.e1;  //if attribute a:A if a is assigned to new A  then return true else return false
				if(new_check.containsKey(cal.name) && new_check.get(cal.name)==0){
					out.println("  %"+varcount+" = icmp eq i64 0, 0");
					varcount++;
					}
			   else if(new_check.containsKey(cal.name) && new_check.get(cal.name)==1){
					out.println("  %"+varcount+" = icmp eq i64 1, 0");
					varcount++;
					}
				else{ //isvaoid on attributes of type bool/int/string is false
					out.println("  %"+varcount+" = icmp eq i64 1, 0");
					varcount++;
					}
				}
			
				/*%0 = load i32, i32* %ans2, align 4
				  %conv = sext i32 %0 to i64
				  %cmp = icmp eq i64 %conv, 0  */
			}

		else if(expr_str.getClass() == AST.object.class){
		   //System.out.println("object");
			AST.object str = (AST.object)expr_str;
			int glob_var1;
			if(formals_list.containsKey(str.name)){ //if the oject belongs to formals list of the method
				//System.out.println("formals_list"+formals_list);
				String ty_fo = formals_list.get(str.name);
				out.println("  %"+varcount+" = load "+ty_fo+", "+ty_fo+"* %"+str.name+".addr, align 4"); //load the value form the formal.addr
				}
			else{
			//if the object belongs to attribute
				if(cl_at.get(presentcl).contains(str.name)){
				   //System.out.println("here if");  //object is attribute of present class 
					int ind1 = cl_at.get(presentcl).indexOf(str.name);
					out.println("  %"+str.name+(varcount+1)+" = getelementptr inbounds %class."+presentcl+", %class."+presentcl+"* %this1, i32 0, i32 "+ind1);//get the attribute elemnt pointer
					glob_var1 = varcount+1;
					}
				else {
				   //System.out.println("here else");
				   	//if the object is attribute of inherited calss
					String inher_cl = parent_type1(presentcl,str.name,1);
					//System.out.println(inher_cl);
					int ind1 = cl_at.get(inher_cl).indexOf(str.name);
					out.println("  %"+varcount +" = bitcast %class."+presentcl+"* %this1 to %class."+inher_cl+"*");//bitcast from present class to inherited class
					varcount++;
					out.println("  %"+str.name+(varcount+1)+" = getelementptr inbounds %class."+inher_cl+", %class."+inher_cl+"* %"+varcount+", i32 0, i32 "+ind1);//get the attribute element pointer
					glob_var1 = varcount+1;
           	   }  
				int ind = cl_at.get(presentcl).indexOf(str.name);
				String ty = cl_at_ty.get(presentcl).get(ind);
				out.println("  %"+varcount+" = load "+ty+", "+ty+"* %"+str.name+glob_var1+", align 4");}//load the value to a new register label
				varcount++;
			}
	
		// string const
		else if(expr_str.getClass() == AST.string_const.class){
		   //System.out.println("stringconst");
			AST.string_const str = (AST.string_const)expr_str;
			String val = str.value;
			String eachstr1;
			//store all the strings in an array
			int ind = val.indexOf('\n');
		   if(ind >= 0){
				eachstr1 = val.substring(0,ind)+"\\0A"+val.substring(ind+1);
		   	}
		   else
				eachstr1 = val;
			str_len.add(val.length()+1);
			allstr.add(eachstr1+"\\00");
			strCount++;
			}

		else if(expr_str.getClass() == AST.int_const.class){
		   //System.out.println("int_const");
			AST.int_const str = (AST.int_const)expr_str;
			out.println("  %"+varcount+" = alloca i32, align 4\n  store i32 "+str.value+", i32* %"+varcount+", align 4");
			varcount++;
			out.println("  %"+varcount+" = load i32, i32* %"+(varcount-1)+", align 4");
			varcount++;
			denom = str.value;
			}
			
		else if(expr_str.getClass() == AST.bool_const.class){ //bool_const
			AST.bool_const str = (AST.bool_const)expr_str;
			//System.out.println(str.value);
		 	if(str.value==true){ //if true store 1
				out.println("  %"+varcount+" = alloca i32, align 4\n  store i32 1"+", i32* %"+varcount+", align 4");
				varcount++;
				out.println("  %"+varcount+" = load i32, i32* %"+(varcount-1)+", align 4");
				varcount++;
				}
			if(str.value==false){ //if false store 0
				out.println("  %"+varcount+" = alloca i32, align 4\n  store i32 0"+", i32* %"+varcount+", align 4");
				varcount++;
				out.println("  %"+varcount+" = load i32, i32* %"+(varcount-1)+", align 4");
				varcount++;
				}
			}
		}

	private int size_class(String class_name){ //gets the size of the class
		if(!cl_at_ty.containsKey(class_name))
			return 0;
		else{
			int si = 0;
			for(int k=0;k<cl_at_ty.get(class_name).size();k++){
				if(cl_at_ty.get(class_name).get(k).equals("i8*")) //size 8
					si = si+8;
				else if(cl_at_ty.get(class_name).get(k).equals("i32")) //size 4
					si = si+4;
				else
					si = si+size_class(cl_at.get(class_name).get(k));
				}
			AST.class_ curr_cl =classmap.get(class_name);//plus size of inherited class
			si=si+size_class(curr_cl.parent);
		   return si;
      	}
		}
	
	//first pass - gets the virtual gtables
	private void graph_traversal(List <AST.class_> classes) {
		classmap = new HashMap <String, AST.class_>();
		//HashMap <String, String> temp = new HashMap <String, String> ();
		for(AST.class_ cl : classes) {
			classmap.put(cl.name,cl);
			
			List<String> new_at = new ArrayList<String>();
			List<String> new_me = new ArrayList<String>();
			List<String> new_at_ty = new ArrayList<String>();
			List<String> new_ob = new ArrayList<String>();
			HashMap<String,List<String>> sub = new HashMap<String,List<String>>();	
			HashMap<String,List<String>> sub_ty = new HashMap<String,List<String>>();	
			
			for(AST.feature at : cl.features){
				if(at.getClass() == AST.attr.class){
					AST.attr at1 = (AST.attr)at;
					new_at.add(at1.name);
					String typ="";
					if(at1.typeid.equals("Int"))
						typ="i32";
					else if(at1.typeid.equals("Bool"))
						typ="i32";
					else if(at1.typeid.equals("String"))
						typ="i8*";
					else{ 
						typ="%class."+at1.typeid+"*";
						new_ob.add(at1.name);
						}
					new_at_ty.add(typ);//add typ of the attribute to list
					attributes.put(at1.name,cl.name);//add attribute name classname to hash map 
					}
				
				if(at.getClass() == AST.method.class){//if method type
					AST.method at1 = (AST.method)at;
					new_me.add(at1.name);
					meth_cl.put(at1.name,cl.name);
					List<String> new_for = new ArrayList<String>();
					List<String> new_for_ty = new ArrayList<String>();
						
					for(AST.formal f:at1.formals){
						new_for.add(f.name);
						String typ = "";
						if(f.typeid.equals("Int"))
							typ = "i32";
						else if(f.typeid.equals("Bool"))
							typ = "i32";
						else if(f.typeid.equals("String"))
							typ = "i8*";
						new_for_ty.add(typ);
						}
					sub.put(at1.name,new_for);
					sub_ty.put(at1.name,new_for_ty);//add typ of the formals to list
					
					String typ = "";
					if(at1.typeid.equals("Int"))
						typ = "i32";
					else if(at1.typeid.equals("Bool"))
						typ = "i32";
					else if(at1.typeid.equals("String"))
						typ = "i8*";
					else if(at1.typeid.equals("IO"))
						typ = "void";
					else 
						typ = "%class."+cl.name+"*";
					//System.out.println(typ);
					meth_type.put(cl.name+at1.name,typ);//add classname+methodname and type of the method to the hashmap
					//methods.put(temp,at1);
					}
					//generation of al hashmaps by addind subahspmaps if exits
				cl_at.put(cl.name,new_at);
				cl_me.put(cl.name,new_me);
				cl_at_ty.put(cl.name,new_at_ty);
				cl_ob.put(cl.name,new_ob);
				cl_me_for.put(cl.name,sub);
				cl_me_for_ty.put(cl.name,sub_ty);
				}			
			}
			
		/*System.out.println(cl_ob);
		System.out.println(cl_at_ty);
		System.out.println(cl_me);
		System.out.println(cl_me_for_ty);
		System.out.println(attributes);
		System.out.println(meth_cl);
		System.out.println(classmap);
		System.out.println("meth......"+meth_type);*/
	}
	
	//generating constants of all the IRs
	static final String DATA_LAYOUT = "target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"";
	static final String TARGET_TRIPLE = "target triple = \"x86_64-unknown-linux-gnu\"";
	static final String CMETHODS = "declare i32 @printf(i8*, ...)\n"
			+ "declare i32 @scanf(i8*, ...)\n"
			+ "declare i32 @strcmp(i8*, i8*)\n"
			+ "declare i8* @strcat(i8*, i8*)\n"
			+ "declare i8* @strcpy(i8*, i8*)\n"
			+ "declare i8* @strncpy(i8*, i8*, i32)\n"
			+ "declare i32 @strlen(i8*)\n"
			+ "declare i8* @malloc(i64)\n"
			+ "declare void @exit(i32)\n"
			+ "declare noalias i8* @_Znwm(i64)\n";
			
	private void CodegenInit(PrintWriter out) {
		out.println(DATA_LAYOUT);
		out.println(TARGET_TRIPLE);
		out.println(CMETHODS);
		
		}
	
	}
