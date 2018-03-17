--section 10.1

class Main inherits IO {
    
   Size : int;  --Type identifiers begin with a capital letter; object identifiers begin with a lower case letter
   
    main() : SELF_TYPE {
	{
         
	    out_string("enter something \n");
	    
	    Size <- in_int();
	    
	    out_int(Size);
	    
	    
	}
    };
};

