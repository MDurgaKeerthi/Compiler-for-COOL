package cool;
import java.util.HashMap;

public class Basic_class {
	public String name;
	public String parent = null;
	public HashMap <String, AST.attr> attrlist;
	public HashMap <String, AST.method> methodlist;
	
	Basic_class(String name_, String parent_, HashMap<String, AST.attr> attrs, HashMap<String, AST.method> meth) {
		name = new String(name_);
		
		if(parent_ != null) 
			parent = new String(parent_);
			
		attrlist = new HashMap <String, AST.attr>();
		attrlist.putAll(attrs);
		
		methodlist = new HashMap <String, AST.method>();
		methodlist.putAll(meth);
	}
}
