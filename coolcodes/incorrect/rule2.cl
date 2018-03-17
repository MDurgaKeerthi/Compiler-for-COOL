--section 10.2

class Main inherits IO {
    
   str : String;
    
    main() : SELF_TYPE {
	{
         
	    out_string("This is \not ok");
	    --Strings are enclosed in double quotes "...".
	    str <- '\0'; --A string may not contain the null (character \0)
	      
	    out_string(str);
	    
	    
	}
    };
};

